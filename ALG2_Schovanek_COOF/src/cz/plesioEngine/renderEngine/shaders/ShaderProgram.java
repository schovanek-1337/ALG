package cz.plesioEngine.renderEngine.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL45;

public abstract class ShaderProgram {

	final int programID;
	private final int vertexShaderID;
	private final int fragmentShaderID;
	private int geometryShaderID = 0;

	private final float[] MATRIX_BUFFER = new float[16];

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);

		if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS)
			== GL11.GL_FALSE) {
			System.err.println("Failed to link program: " + programID);
			System.err.println(GL20.glGetProgramInfoLog(programID));
		}

		GL20.glValidateProgram(programID);

		if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS)
			== GL11.GL_FALSE) {
			System.err.println("Failed to validate program: " + programID);
			System.err.println(GL20.glGetProgramInfoLog(programID));
		}

		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		getCommonUniformLocations();
	}

	public ShaderProgram(String vertexFile, String fragmentFile,
		String geometryFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		geometryShaderID = loadShader(geometryFile, GL45.GL_GEOMETRY_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glAttachShader(programID, geometryShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);

		if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS)
			== GL11.GL_FALSE) {
			System.err.println("Failed to link program: " + programID);
			System.err.println(GL20.glGetProgramInfoLog(programID));
		}

		GL20.glValidateProgram(programID);

		if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS)
			== GL11.GL_FALSE) {
			System.err.println("Failed to validate program: " + programID);
			System.err.println(GL20.glGetProgramInfoLog(programID));
		}

		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteShader(geometryShaderID);
		getCommonUniformLocations();
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	protected int getUniformLocation(String uniformName) {
		int location = GL20.glGetUniformLocation(programID, uniformName);
		if (location == GL11.GL_INVALID_VALUE || location == -1) {
			System.err.println("Could not get uniform location: " + uniformName);
			System.err.println(GL20.glGetProgramInfoLog(programID));
		}
		return location;
	}

	protected abstract void getCommonUniformLocations();

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void loadVector(int location, Vector3f v) {
		GL20.glUniform3f(location, v.x(), v.y(), v.z());
	}

	protected void loadVector2f(int location, Vector2f v) {
		GL20.glUniform2f(location, v.x(), v.y());
	}

	protected void loadBoolean(int location, boolean bool) {
		float toLoad = 0;
		if (bool) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}

	protected void loadInt(int location, int integer) {
		GL20.glUniform1i(location, integer);
	}

	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.get(MATRIX_BUFFER);
		GL20.glUniformMatrix4fv(location, false, MATRIX_BUFFER);
	}

	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			InputStream in = Class.class.getResourceAsStream(file);
			BufferedReader reader =	new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = reader.readLine()) != null){
				shaderSource.append(line).append("//\n");
			}
			
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)
			== GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader! " + file);
			System.exit(-1);
		}
		return shaderID;
	}

}
