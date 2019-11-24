package com.nemosw.spigot.tap.event.specific;

import com.google.common.reflect.TypeToken;
import com.nemosw.tools.asm.ClassDefiner;
import org.bukkit.event.Event;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

public final class ASMSpecificEventExecutor
{

    private static final DefaultExtractor[] DEFAULT_EXTRACTORS;

    private static final String EXECUTOR_CONST_DESC = "(L" + Type.getInternalName(SpecificExtractor.class) + ";)V";

    private static final String SUPER_NAME = Type.getInternalName(SpecificEventExecutor.class);

    private static final String SUPER_CONST_DESC = Type.getConstructorDescriptor(SpecificEventExecutor.class.getConstructors()[0]);

    private static final String EXECUTE_DESC;

    private static final String PRIORITY_NAME = Type.getInternalName(SpecificEventPriority.class);

    private static final String PRIORITY_SIGN = 'L' + PRIORITY_NAME + ';';

    private static final Map<Method, SpecificEventExecutor> CACHE = new HashMap<>();

    private static final Map<Class<?>, SpecificExtractor<?>> EXTRACTORS_BY_CLASS = new HashMap<>();

    private static int EXECUTOR_IDs;

    private static class DefaultExtractor
    {
        final Class<?> eventClass;

        final Class<?> extractorClass;

        @SuppressWarnings({"unchecked"})
        DefaultExtractor(Class<?> extractorClass)
        {
            this.eventClass = (Class<? extends Event>) ((ParameterizedType) extractorClass.getGenericSuperclass()).getActualTypeArguments()[0];
            this.extractorClass = extractorClass;
        }
    }

    static
    {
        try
        {
            EXECUTE_DESC = Type.getMethodDescriptor(SpecificEventExecutor.class.getMethod("execute", SpecificListener.class, Event.class));

        }
        catch (Exception e)
        {
            throw new AssertionError(e);
        }

        Class<?>[] classes = SpecificExtractor.class.getDeclaredClasses();
        List<DefaultExtractor> defaultExtractors = new ArrayList<>(classes.length);

        for (Class<?> clazz : classes)
        {
            if (SpecificExtractor.class.isAssignableFrom(clazz) && !Modifier.isPublic(clazz.getModifiers()))
                defaultExtractors.add(new DefaultExtractor(clazz));
        }

        DEFAULT_EXTRACTORS = defaultExtractors.toArray(new DefaultExtractor[0]);
    }

    private static Class<?> getDefaultExtractorClass(Class<?> clazz)
    {
        for (DefaultExtractor extractor : DEFAULT_EXTRACTORS)
        {
            if (extractor.eventClass.isAssignableFrom(clazz))
                return extractor.extractorClass;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    static SpecificEventExecutor[] createExecutors(Class<? extends SpecificListener> clazz)
    {
        int mod = clazz.getModifiers();

        if (!Modifier.isPublic(mod))
            throw new IllegalArgumentException("EntityEventListener's modifier must be public");

        Method[] methods = clazz.getMethods();

        class ListenerMaterial
        {
            final Method method;
            final Class<?> handlerClass;
            final SpecificExtractor<? extends Event> entityExtractor;
            final SpecificEventPriority priority;
            final boolean ignoreCancelled;

            ListenerMaterial(Method method, Class<?> handlerClass, SpecificExtractor<? extends Event> entityExtractor, SpecificEventPriority priority, boolean ignoreCancelled)
            {
                this.method = method;
                this.handlerClass = handlerClass;
                this.entityExtractor = entityExtractor;
                this.priority = priority;
                this.ignoreCancelled = ignoreCancelled;
            }
        }

        ArrayList<ListenerMaterial> materials = new ArrayList<>(methods.length);
        Set<? extends Class<?>> supers = TypeToken.of(clazz).getTypes().rawTypes();

        for (Method method : methods)
        {
            for (Class<?> superClass : supers)
            {
                try
                {
                    Method real = superClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    SpecificEventHandler specificEventHandler = real.getAnnotation(SpecificEventHandler.class);

                    if (specificEventHandler != null)
                    {
                        Class<?>[] parameterTypes = real.getParameterTypes();

                        if (parameterTypes.length != 1)
                            throw new IllegalArgumentException("@EntityEventHandler methods must require a single argument: " + method);

                        if (method.getReturnType() != void.class)
                            throw new IllegalArgumentException("@EntityEventHandler methods must return 'void': " + method);

                        Class<?> eventClass = parameterTypes[0];

                        if (!Event.class.isAssignableFrom(eventClass))
                            throw new IllegalArgumentException("'" + eventClass.getName() + "' is not event class : " + method);

                        Class<?> handlerClass;

                        try
                        {
                            handlerClass = eventClass.getMethod("getHandlerList").getDeclaringClass();
                        }
                        catch (NoSuchMethodException e)
                        {
                            throw new IllegalArgumentException('\'' + eventClass.getName() + "' has no HandlerList: " + method);
                        }

                        Class<?> extractorClass = specificEventHandler.extractor();

                        if (extractorClass == SpecificExtractor.class)
                        {
                            extractorClass = getDefaultExtractorClass(eventClass);

                            if (extractorClass == null)
                                throw new NullPointerException("Not found default EntityExtractor for " + eventClass);
                        }

                        SpecificExtractor<? extends Event> specificExtractor = EXTRACTORS_BY_CLASS.get(extractorClass);

                        if (specificExtractor == null)
                        {
                            try
                            {
                                specificExtractor = (SpecificExtractor<? extends Event>) extractorClass.newInstance();
                            }
                            catch (Exception e)
                            {
                                throw new AssertionError(e);
                            }

                            EXTRACTORS_BY_CLASS.put(extractorClass, specificExtractor);
                        }

                        materials.add(new ListenerMaterial(method, handlerClass, specificExtractor, specificEventHandler.priority(), specificEventHandler.ignoreCancelled()));
                    }
                }
                catch (NoSuchMethodException | SecurityException ignored)
                {
                }
            }
        }

        int size = materials.size();
        SpecificEventExecutor[] executors = new SpecificEventExecutor[size];

        for (int i = 0; i < size; i++)
        {
            ListenerMaterial material = materials.get(i);

            executors[i] = createListener(material.method, material.handlerClass, material.entityExtractor, material.priority, material.ignoreCancelled);
        }

        return executors;
    }

    private static SpecificEventExecutor createListener(Method method, Class<?> handlerClass, SpecificExtractor<?> specificExtractor, SpecificEventPriority priority, boolean ignoreCancelled)
    {
        if (CACHE.containsKey(method))
            return CACHE.get(method);

        try
        {
            Class<?> declaredClass = method.getDeclaringClass();
            Class<?> eventClass = method.getParameterTypes()[0];
            String name = SpecificEventExecutor.class.getName() + "_" + EXECUTOR_IDs++;
            String desc = name.replace('.', '/');
            String instType = Type.getInternalName(declaredClass);
            String eventType = Type.getInternalName(eventClass);

            ClassWriter cw = new ClassWriter(0);
            MethodVisitor mv;

            cw.visit(V1_6, ACC_PUBLIC | ACC_SUPER, desc, null, SUPER_NAME, null);
            cw.visitSource(".dynamic", null);

            {
                mv = cw.visitMethod(ACC_PUBLIC, "<init>", EXECUTOR_CONST_DESC, null, null);
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitLdcInsn(Type.getType(eventClass));
                mv.visitLdcInsn(Type.getType(handlerClass));
                mv.visitVarInsn(ALOAD, 1);
                mv.visitFieldInsn(GETSTATIC, PRIORITY_NAME, priority.name(), PRIORITY_SIGN);
                mv.visitInsn(ignoreCancelled ? ICONST_1 : ICONST_0);
                mv.visitMethodInsn(INVOKESPECIAL, SUPER_NAME, "<init>", SUPER_CONST_DESC, false);
                mv.visitInsn(RETURN);
                mv.visitMaxs(6, 2);
                mv.visitEnd();
            }

            {
                mv = cw.visitMethod(ACC_PUBLIC, "execute", EXECUTE_DESC, null, null);
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(CHECKCAST, instType);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitTypeInsn(CHECKCAST, eventType);
                mv.visitMethodInsn(INVOKEVIRTUAL, instType, method.getName(), Type.getMethodDescriptor(method), false);
                mv.visitInsn(RETURN);
                mv.visitMaxs(2, 3);
                mv.visitEnd();
            }

            SpecificEventExecutor executor = (SpecificEventExecutor) ClassDefiner.defineClass(name, cw.toByteArray(), declaredClass.getClassLoader()).getConstructor(SpecificExtractor.class)
                    .newInstance(specificExtractor);

            CACHE.put(method, executor);

            return executor;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            throw new AssertionError(t);
        }
    }

}