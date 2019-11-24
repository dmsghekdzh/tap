package com.nemosw.spigot.tap.event.specific;

final class SpecificEventKey
{

    private final Class<?> eventClass;

    private final SpecificExtractor<?> extractor;

    private int hashCode;

    SpecificEventKey(Class<?> eventClass, SpecificExtractor<?> extractor)
    {
        this.eventClass = eventClass;
        this.extractor = extractor;
        this.hashCode = eventClass.hashCode() ^ extractor.hashCode();
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
