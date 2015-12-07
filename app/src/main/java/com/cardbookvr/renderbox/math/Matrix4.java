package com.cardbookvr.renderbox.math;

import android.opengl.Matrix;

/**
 * Created by Jonathan on 12/6/2015.
 */
public class Matrix4 {
    public final float[] val = new float[16];
    public Matrix4() {
        Matrix.setIdentityM(val, 0);
    }
    public Matrix4 toIdentity(){
        return this;
    }
    //public static Matrix4 TRS(Vector3 position, Quaternion rotation, Vector3 scale) {
    public static Matrix4 TRS(Vector3 position, Vector3 rotation, Vector3 scale) {
        Matrix4 result = new Matrix4();
        result.translate(position);
        result.rotate(rotation);
        result.scale(scale);
        return result;
    }
    public Matrix4 translate(Vector3 position){
        Matrix.translateM(val, 0, position.x, position.y, position.z);
        return this;
    }
    //public Matrix4 rotate(Quaternion rotation){
    public Matrix4 rotate(Vector3 rotation){
//		float[] tmp = new float[16];
//		Matrix.setRotateEulerM(tmp, 0, rotation.x, rotation.y, rotation.z);
//		multiply(tmp);
        Matrix.rotateM(val, 0, rotation.x, 1, 0, 0);
        Matrix.rotateM(val, 0, rotation.y, 0, 1, 0);
        Matrix.rotateM(val, 0, rotation.z, 0, 0, -1);
        return this;
    }
    public Matrix4 scale(Vector3 scale){
        Matrix.scaleM(val, 0, scale.x, scale.y, scale.z);
        return this;
    }
    public Vector3 multiplyPoint3x4(Vector3 v){
        float[] vec = v.toFloat4();
        Matrix.multiplyMV(vec, 0, val, 0, vec, 0);
        return new Vector3(vec);
    }
    public Matrix4 multiply(Matrix4 matrix){
        return multiply(matrix.val);
    }
    public Matrix4 multiply(float[] matrix){
        Matrix.multiplyMM(val, 0, val, 0, matrix, 0);
        return this;
    }


    /** XX: Typically the unrotated X component for scaling, also the cosine of the angle when rotated on the Y and/or Z axis. On
     * Vector3 multiplication this value is multiplied with the source X component and added to the target X component. */
    public static final int M00 = 0;
    /** XY: Typically the negative sine of the angle when rotated on the Z axis. On Vector3 multiplication this value is multiplied
     * with the source Y component and added to the target X component. */
    public static final int M01 = 4;
    /** XZ: Typically the sine of the angle when rotated on the Y axis. On Vector3 multiplication this value is multiplied with the
     * source Z component and added to the target X component. */
    public static final int M02 = 8;
    /** XW: Typically the translation of the X component. On Vector3 multiplication this value is added to the target X component. */
    public static final int M03 = 12;
    /** YX: Typically the sine of the angle when rotated on the Z axis. On Vector3 multiplication this value is multiplied with the
     * source X component and added to the target Y component. */
    public static final int M10 = 1;
    /** YY: Typically the unrotated Y component for scaling, also the cosine of the angle when rotated on the X and/or Z axis. On
     * Vector3 multiplication this value is multiplied with the source Y component and added to the target Y component. */
    public static final int M11 = 5;
    /** YZ: Typically the negative sine of the angle when rotated on the X axis. On Vector3 multiplication this value is multiplied
     * with the source Z component and added to the target Y component. */
    public static final int M12 = 9;
    /** YW: Typically the translation of the Y component. On Vector3 multiplication this value is added to the target Y component. */
    public static final int M13 = 13;
    /** ZX: Typically the negative sine of the angle when rotated on the Y axis. On Vector3 multiplication this value is multiplied
     * with the source X component and added to the target Z component. */
    public static final int M20 = 2;
    /** ZY: Typical the sine of the angle when rotated on the X axis. On Vector3 multiplication this value is multiplied with the
     * source Y component and added to the target Z component. */
    public static final int M21 = 6;
    /** ZZ: Typically the unrotated Z component for scaling, also the cosine of the angle when rotated on the X and/or Y axis. On
     * Vector3 multiplication this value is multiplied with the source Z component and added to the target Z component. */
    public static final int M22 = 10;
    /** ZW: Typically the translation of the Z component. On Vector3 multiplication this value is added to the target Z component. */
    public static final int M23 = 14;
    /** WX: Typically the value zero. On Vector3 multiplication this value is ignored. */
    public static final int M30 = 3;
    /** WY: Typically the value zero. On Vector3 multiplication this value is ignored. */
    public static final int M31 = 7;
    /** WZ: Typically the value zero. On Vector3 multiplication this value is ignored. */
    public static final int M32 = 11;
    /** WW: Typically the value one. On Vector3 multiplication this value is ignored. */
    public static final int M33 = 15;
}
