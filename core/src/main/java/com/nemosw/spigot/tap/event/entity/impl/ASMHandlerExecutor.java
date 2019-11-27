package com.nemosw.spigot.tap.event.entity.impl;

import com.nemosw.spigot.tap.event.entity.EntityListener;
import com.nemosw.tools.asm.ClassDefiner;
import org.bukkit.event.Event;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author Nemo
 */
final class ASMHandlerExecutor
{
    private static final Method METHOD_EXECUTOR;
    private static final HashMap<Method, HandlerExecutor> CACHE = new HashMap<>();
    private static int handlerNumber;

    static
    {
        try
        {
            METHOD_EXECUTOR = HandlerExecutor.class.getMethod("execute", EntityListener.class, Event.class);
        }
        catch (NoSuchMethodException e)
        {
            throw new AssertionError(e);
        }
    }

    static HandlerExecutor create(Method method)
    {
        return CACHE.computeIfAbsent(method, key -> {
            String className = Type.getInternalName(ASMHandlerExecutor.class) + "_" + key.getClass().getName() + "_" + key.getName() + "_" + handlerNumber++;
            byte[] classData = generateClassData(key, className);
            Class<?> executorClass = ClassDefiner.defineClass(className, classData, method.getDeclaringClass().getClassLoader());

            try
            {
                return (HandlerExecutor) executorClass.newInstance();
            }
            catch (Exception e)
            {
                throw new AssertionError(e);
            }
        });
    }

    private static byte[] generateClassData(Method method, String className)
    {
        String objectName = Type.getInternalName(Object.class);
        String listenerName = Type.getInternalName(method.getDeclaringClass());

        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_FINAL | ACC_SUPER, className, null, objectName, new String[]{Type.getInternalName(HandlerExecutor.class)});
        classWriter.visitSource(".dynamic", null);

        //Constructor
        {
            MethodVisitor methodVisitor = classWriter.visitMethod(0, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, objectName, "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, METHOD_EXECUTOR.getName(), Type.getMethodDescriptor(METHOD_EXECUTOR), null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, listenerName);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getParameterTypes()[0]));
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, listenerName, method.getName(), Type.getMethodDescriptor(method), false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 3);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }
}
