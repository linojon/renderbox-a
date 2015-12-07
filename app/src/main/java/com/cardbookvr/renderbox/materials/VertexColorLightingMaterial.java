package com.cardbookvr.renderbox.materials;

import android.opengl.GLES20;
import android.opengl.Matrix;


import com.cardbookvr.solarsystem.R;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by mtsch on 11/9/2015.
 */
public class VertexColorLightingMaterial extends Material {
	static int program;

	static int positionParam;
	static int colorParam;
	static int normalParam;
	static int modelParam;
	static int MVParam;
	static int MVPParam;
	static int lightPosParam;

	FloatBuffer vertexBuffer;
	FloatBuffer normalBuffer;
	FloatBuffer colorBuffer;
	int numIndices;

	public VertexColorLightingMaterial(){
		super();
	}
	public VertexColorLightingMaterial(String name){
		super(name);
	}
	public static void setupProgram(){
		//Create shader program
		program = createProgram(R.raw.vertex_color_lighting_vertex, R.raw.vertex_color_lighting_fragment);

		//Get vertex attribute parameters
		positionParam = GLES20.glGetAttribLocation(program, "a_Position");
		normalParam = GLES20.glGetAttribLocation(program, "a_Normal");
		colorParam = GLES20.glGetAttribLocation(program, "a_Color");

		//Enable vertex attribute parameters
		GLES20.glEnableVertexAttribArray(positionParam);
		GLES20.glEnableVertexAttribArray(normalParam);
		GLES20.glEnableVertexAttribArray(colorParam);

		//Shader-specific paramteters
		modelParam = GLES20.glGetUniformLocation(program, "u_Model");
		MVParam = GLES20.glGetUniformLocation(program, "u_MVMatrix");
		MVPParam = GLES20.glGetUniformLocation(program, "u_MVP");
		lightPosParam = GLES20.glGetUniformLocation(program, "u_LightPos");

		//RenderBox.checkGLError("Solid Color Lighting params");
	}
	public void setBuffers(FloatBuffer vertexBuffer,  FloatBuffer colorBuffer, FloatBuffer normalBuffer, int numIndices){
		this.vertexBuffer = vertexBuffer;
		this.normalBuffer = normalBuffer;
		this.colorBuffer = colorBuffer;
		this.numIndices = numIndices;
	}

	@Override
	public void draw(float[] view, float[] perspective, float[] model) {
		Matrix.multiplyMM(modelView, 0, view, 0, model, 0);
		Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);

		GLES20.glUseProgram(program);

		//GLES20.glUniform3fv(lightPosParam, 1, RenderBox.instance.mainLight.lightPosInEyeSpace, 0);

		// Set the Model in the shader, used to calculate lighting
		GLES20.glUniformMatrix4fv(modelParam, 1, false, model, 0);

		// Set the ModelView in the shader, used to calculate lighting
		GLES20.glUniformMatrix4fv(MVParam, 1, false, modelView, 0);

		// Set the position of the cube
		GLES20.glVertexAttribPointer(positionParam, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

		// Set the ModelViewProjection matrix in the shader.
		GLES20.glUniformMatrix4fv(MVPParam, 1, false, modelViewProjection, 0);

		// Set the normal positions of the cube, again for shading
		GLES20.glVertexAttribPointer(normalParam, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
		GLES20.glVertexAttribPointer(colorParam, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

		// Set the position of the cube
		GLES20.glVertexAttribPointer(positionParam, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

		// Set the ModelViewProjection matrix in the shader.
		GLES20.glUniformMatrix4fv(MVPParam, 1, false, modelViewProjection, 0);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numIndices);
	}
}
