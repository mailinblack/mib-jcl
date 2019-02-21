package com.mib.primitives;

/**
 * Utility methods for booleans.
 */
public enum MibBooleans {
    ;

    /**
     * Returns {@code true} if all given boolean values are {@code true}.
     * <p>
     * Similar to <i>Apache Commons Lang3</i> {@code BooleanUtils.and()}
     * but does not have this <a href="https://stackoverflow.com/questions/49252638/how-to-use-apache-commons-booleanutils-and-method">issue</a>.
     *
     * @param firstOne the first boolean value
     * @param others the other booleans as a vararg
     * @return {@code true} if all given boolean values are {@code true}
     */
    public static boolean and(boolean firstOne, boolean... others) {
        if(!firstOne) {
            return false;
        }
        if(others == null || others.length == 0) {
            return true;
        }
        for (final boolean element : others) {
            if (!element) {
                return false;
            }
        }
        return true;
    }
}
