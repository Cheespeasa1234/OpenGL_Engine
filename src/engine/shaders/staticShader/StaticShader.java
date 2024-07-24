package engine.shaders.staticShader;

import org.lwjgl.util.vector.Matrix4f;

import engine.entities.Light;
import engine.shaders.ShaderProgram;

public class StaticShader extends ShaderProgram {
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	
	public static final String vertexFile = "src/engine/shaders/staticShader/vertexShader.vert";
	public static final String fragmentFile = "src/engine/shaders/staticShader/fragmentShader.frag";
	
	public StaticShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		System.out.println("Using " + vertexFile + " and " + fragmentFile);
	}
	
	public static StaticShader init() {
		return new StaticShader(vertexFile, fragmentFile);
	}
	
	@Override protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	@Override protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPositon");
		location_lightColor = super.getUniformLocation("lightColor");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrixToUniform(matrix, location_transformationMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrixToUniform(matrix, location_projectionMatrix);
	}

	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrixToUniform(matrix, location_viewMatrix);
	}
	
	public void loadLight(Light light) {
		super.loadVectorToUniform(light.getPosition(), location_lightPosition);
		super.loadVectorToUniform(light.getColor(), location_lightColor);
	}

}
