package com.cardbookvr.renderbox.materials;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.cardbookvr.renderbox.RenderBox;
import com.cardbookvr.solarsystem.R;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Jonathan on 12/5/2015.
 */
public class SolidColorMaterial extends Material {
    float[] color = new float[4];
    static int program;
    static int positionParam;
    static int colorParam;
    static int MVPParam;

    FloatBuffer vertexBuffer;
    ShortBuffer indexBuffer;
    int numIndices;

    public SolidColorMaterial(float[] c){
        super();
        setColor(c);
    }
    public SolidColorMaterial(float[] c, String name){
        super(name);
        setColor(c);
    }
    public static void setupProgram(){
        //Create shader program
        program = createProgram(R.raw.solid_color_vertex, R.raw.solid_color_fragment);

        //Get vertex attribute parameters
        positionParam = GLES20.glGetAttribLocation(program, "v_Position");

        //Enable vertex attribute parameters
        GLES20.glEnableVertexAttribArray(positionParam);

        //Shader-specific paramteters
        colorParam = GLES20.glGetUniformLocation(program, "v_Color");
        MVPParam = GLES20.glGetUniformLocation(program, "u_MVP");

        RenderBox.checkGLError("Solid Color params");
    }
    void setColor(float[] c){
        color = c;
    }
    public void setBuffers(FloatBuffer vertexBuffer, ShortBuffer indexBuffer, int numIndices){
        this.vertexBuffer = vertexBuffer;
        this.indexBuffer = indexBuffer;
        this.numIndices = numIndices;
    }

    @Override
    public void draw(float[] view, float[] perspective, float[] model) {
        Matrix.multiplyMM(modelView, 0, view, 0, model, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);

        GLES20.glUseProgram(program);

        // Set the position buffer
        GLES20.glVertexAttribPointer(positionParam, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Set the ModelViewProjection matrix in the shader.
        GLES20.glUniformMatrix4fv(MVPParam, 1, false, modelViewProjection, 0);

        GLES20.glUniform4fv(colorParam, 1, color, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numIndices, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
