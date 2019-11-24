package com.nemosw.spigot.tap.event.specific;

final class SpecificEventKey
{

    private Class<?> eventClass;

    private SpecificExtractor<?> extractor;

    private int hashCode;

    SpecificEventKey set(Class<?> eventClass, SpecificExtractor<?> extractor)
    {
        this.eventClass = eventClass;
        this.extractor = extractor;
        this.hashCode = eventClass.hashCode() ^ extractor.hashCode();

        return this;
    }

    @Override
    public int hashCode()
    {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SpecificEventKey)
        {
            SpecificEventKey other = (SpecificEventKey) obj;

            return this.eventClass == other.eventClass && this.extractor == other.extractor;
        }

        return false;
    }

}
