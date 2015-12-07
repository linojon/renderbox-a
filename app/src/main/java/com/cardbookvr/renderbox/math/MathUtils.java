package com.cardbookvr.renderbox.math;

/**
 * Mostly sourced from https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/MathUtils.java
 */
public class MathUtils {
    //Float overrides for Java math stuff
    static public final float PI = 3.1415927f;
    static public final float PI2 = PI * 2;
    static public final float degreesToRadians = PI / 180;
    static public final float radiansToDegrees = 180f / PI;

    static private final int SIN_BITS = 14; // 16KB. Adjust for accuracy.
    static private final int SIN_MASK = ~(-1 << SIN_BITS);
    static private final int SIN_COUNT = SIN_MASK + 1;

    static private final float radFull = PI * 2;
    static private final float degFull = 360;
    static private final float radToIndex = SIN_COUNT / radFull;
    static private final float degToIndex = SIN_COUNT / degFull;

    static private class Sin {
        static final float[] table = new float[SIN_COUNT];
        static {
            for (int i = 0; i < SIN_COUNT; i++)
                table[i] = (float)Math.sin((i + 0.5f) / SIN_COUNT * radFull);
            for (int i = 0; i < 360; i += 90)
                table[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * degreesToRadians);
        }
    }

    /** Returns the sine in radians from a lookup table. */
    static public float sin (float radians) {
        return Sin.table[(int)(radians * radToIndex) & SIN_MASK];
    }

    /** Returns the cosine in radians from a lookup table. */
    static public float cos (float radians) {
        return Sin.table[(int)((radians + PI / 2) * radToIndex) & SIN_MASK];
    }

    /** Returns the sine in radians from a lookup table. */
    static public float sinDeg (float degrees) {
        return Sin.table[(int)(degrees * degToIndex) & SIN_MASK];
    }

    /** Returns the cosine in radians from a lookup table. */
    static public float cosDeg (float degrees) {
        return Sin.table[(int)((degrees + 90) * degToIndex) & SIN_MASK];
    }

    // ---

    /** Returns atan2 in radians, faster but less accurate than Math.atan2. Average error of 0.00231 radians (0.1323 degrees),
     * largest error of 0.00488 radians (0.2796 degrees). */
    static public float atan2 (float y, float x) {
        if (x == 0f) {
            if (y > 0f) return PI / 2;
            if (y == 0f) return 0f;
            return -PI / 2;
        }
        final float atan, z = y / x;
        if (Math.abs(z) < 1f) {
            atan = z / (1f + 0.28f * z * z);
            if (x < 0f) return y < 0f ? atan - PI : atan + PI;
            return atan;
        }
        atan = PI / 2 - z / (z * z + 0.28f);
        return y < 0f ? atan - PI : atan;
    }

    static public short clamp (short value, short min, short max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    static public int clamp (int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    static public long clamp (long value, long min, long max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    static public float clamp (float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    static public double clamp (double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
