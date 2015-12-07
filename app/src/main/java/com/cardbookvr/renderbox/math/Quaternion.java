package com.cardbookvr.renderbox.math;

import android.opengl.Matrix;

/**
 * Created by Jonathan on 12/6/2015.
 */
public class Quaternion {
    private static final String TAG = "RenderBox.Quaternion";
    public float x,y,z,w;
    public Quaternion(){
        w = 1;
    }
    public Quaternion(Quaternion quat){
        x = quat.x;
        y = quat.y;
        z = quat.z;
        w = quat.w;
    }
    public Quaternion setEulerAngles (float yaw, float pitch, float roll) {
        return setEulerAnglesRad(yaw * MathUtils.degreesToRadians, pitch * MathUtils.degreesToRadians, roll
                * MathUtils.degreesToRadians);
    }
    public Quaternion setEulerAnglesRad (float yaw, float pitch, float roll) {
        final float hr = roll * 0.5f;
        final float shr = (float)Math.sin(hr);
        final float chr = (float)Math.cos(hr);
        final float hp = pitch * 0.5f;
        final float shp = (float)Math.sin(hp);
        final float chp = (float)Math.cos(hp);
        final float hy = yaw * 0.5f;
        final float shy = (float)Math.sin(hy);
        final float chy = (float)Math.cos(hy);
        final float chy_shp = chy * shp;
        final float shy_chp = shy * chp;
        final float chy_chp = chy * chp;
        final float shy_shp = shy * shp;

        x = (chy_shp * chr) + (shy_chp * shr); // cos(yaw/2) * sin(pitch/2) * cos(roll/2) + sin(yaw/2) * cos(pitch/2) * sin(roll/2)
        y = (shy_chp * chr) - (chy_shp * shr); // sin(yaw/2) * cos(pitch/2) * cos(roll/2) - cos(yaw/2) * sin(pitch/2) * sin(roll/2)
        z = (chy_chp * shr) - (shy_shp * chr); // cos(yaw/2) * cos(pitch/2) * sin(roll/2) - sin(yaw/2) * sin(pitch/2) * cos(roll/2)
        w = (chy_chp * chr) + (shy_shp * shr); // cos(yaw/2) * cos(pitch/2) * cos(roll/2) + sin(yaw/2) * sin(pitch/2) * sin(roll/2)
        return this;
    }
    public Quaternion conjugate () {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }
    public Quaternion multiply(final Quaternion other){
        final float newX = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y;
        final float newY = this.w * other.y + this.y * other.w + this.z * other.x - this.x * other.z;
        final float newZ = this.w * other.z + this.z * other.w + this.x * other.y - this.y * other.x;
        final float newW = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.w = newW;
        return this;
    }
    //	public Matrix4 toMatrix4(){
//		Matrix4 result = new Matrix4();
//		final float[] matrix = result.val;
//		final float xx = x * x;
//		final float xy = x * y;
//		final float xz = x * z;
//		final float xw = x * w;
//		final float yy = y * y;
//		final float yz = y * z;
//		final float yw = y * w;
//		final float zz = z * z;
//		final float zw = z * w;
//		// Set matrix from quaternion
//		matrix[Matrix4.M00] = 1 - 2 * (yy + zz);
//		matrix[Matrix4.M01] = 2 * (xy - zw);
//		matrix[Matrix4.M02] = 2 * (xz + yw);
//		matrix[Matrix4.M03] = 0;
//		matrix[Matrix4.M10] = 2 * (xy + zw);
//		matrix[Matrix4.M11] = 1 - 2 * (xx + zz);
//		matrix[Matrix4.M12] = 2 * (yz - xw);
//		matrix[Matrix4.M13] = 0;
//		matrix[Matrix4.M20] = 2 * (xz - yw);
//		matrix[Matrix4.M21] = 2 * (yz + xw);
//		matrix[Matrix4.M22] = 1 - 2 * (xx + yy);
//		matrix[Matrix4.M23] = 0;
//		matrix[Matrix4.M30] = 0;
//		matrix[Matrix4.M31] = 0;
//		matrix[Matrix4.M32] = 0;
//		matrix[Matrix4.M33] = 1;
//		return result;
//	}
    public float[] toMatrix4(){
        float[] result = new float[16];
        Matrix.setRotateEulerM(result, 0, getPitch(), getYaw(), getRoll());
        return result;
    }
    /** Get the pole of the gimbal lock, if any.
     * @return positive (+1) for north pole, negative (-1) for south pole, zero (0) when no gimbal lock */
    public int getGimbalPole() {
        final float t = y*x+z*w;
        return t > 0.499f ? 1 : (t < -0.499f ? -1 : 0);
    }
    /** Get the roll euler angle in radians, which is the rotation around the z axis. Requires that this quaternion is normalized.
     * @return the rotation around the z axis in radians (between -PI and +PI) */
    public float getRollRad() {
        final int pole = getGimbalPole();
        return pole == 0 ? MathUtils.atan2(2f*(w*z + y*x), 1f - 2f * (x*x + z*z)) : (float)pole * 2f * MathUtils.atan2(y, w);
    }

    /** Get the roll euler angle in degrees, which is the rotation around the z axis. Requires that this quaternion is normalized.
     * @return the rotation around the z axis in degrees (between -180 and +180) */
    public float getRoll() {
        return getRollRad() * MathUtils.radiansToDegrees;
    }

    /** Get the pitch euler angle in radians, which is the rotation around the x axis. Requires that this quaternion is normalized.
     * @return the rotation around the x axis in radians (between -(PI/2) and +(PI/2)) */
    public float getPitchRad() {
        final int pole = getGimbalPole();
        return pole == 0 ? (float)Math.asin(MathUtils.clamp(2f*(w*x-z*y), -1f, 1f)) : (float)pole * MathUtils.PI * 0.5f;
    }

    /** Get the pitch euler angle in degrees, which is the rotation around the x axis. Requires that this quaternion is normalized.
     * @return the rotation around the x axis in degrees (between -90 and +90) */
    public float getPitch() {
        return getPitchRad() * MathUtils.radiansToDegrees;
    }

    /** Get the yaw euler angle in radians, which is the rotation around the y axis. Requires that this quaternion is normalized.
     * @return the rotation around the y axis in radians (between -PI and +PI) */
    public float getYawRad() {
        return getGimbalPole() == 0 ? MathUtils.atan2(2f*(y*w + x*z), 1f - 2f*(y*y+x*x)) : 0f;
    }

    /** Get the yaw euler angle in degrees, which is the rotation around the y axis. Requires that this quaternion is normalized.
     * @return the rotation around the y axis in degrees (between -180 and +180) */
    public float getYaw() {
        return getYawRad() * MathUtils.radiansToDegrees;
    }
    public String toString(){
        return "Quaternion(" + x + ", " + y + ", " + z + ", " + ", " + w + ")";
    }
}
