package engine.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	/**
	 * 
	 */
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * Initialize a shader program, and link it with openGL. 
	 * It then binds the attributes and sets up uniforms.
	 * @param vertexFile The project-relative vertex shader file location
	 * @param fragmentFile The project-relative fragment shader file location
	 */
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);

		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	/**
	 * Runs when attributes are bound, meant for setting uniform locations to be managed.
	 */
	protected abstract void getAllUniformLocations();
	
	/**
	 * Returns the uniform location (for other uses) of a given uniform in this shader program.
	 * @param uniformName The string name of the uniform
	 * @return The location of the given uniform
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	/**
	 * Stops this program, deletes shaders and this program, and frees memory.
	 */
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * Bind the indices of named VAOs for the vertex shader parameters.
	 * For example, implement this by putting `super.bindAttribute(0, "position")`
	 */
	protected abstract void bindAttributes();
	
	protected void bindAttributeList(String... attributeNames) {
		for (int i = 0; i < attributeNames.length; i++) {
			this.bindAttribute(i, attributeNames[i]);
		}
	}
	
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected void loadFloatToUniform(float value, int location) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector3fToUniform(Vector3f vector, int location) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector4fToUniform(Vector4f vector, int location) {
		GL20.glUniform4f(location, vector.w, vector.x, vector.y, vector.z);
	}
	
	protected void loadBooleanToUniform(boolean bool, int location) {
		float valueToUse = bool ? 1 : 0;
		GL20.glUniform1f(location, valueToUse);
	}
	
	protected void loadMatrixToUniform(Matrix4f matrix, int location) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file " + file + " type " + type + ".");
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader " + file + " type " + type + ".");
			System.exit(-1);
		}
		
		return shaderID;
	}
	
	@Override public String toString() {
		return "vert:" + this.vertexShaderID + " frag:" + this.fragmentShaderID;
	}
	
}
