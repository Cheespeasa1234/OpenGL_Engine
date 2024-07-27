package engine.shaders.staticShader;

import org.lwjgl.util.vector.Vector4f;

public class BorderedStaticShader extends StaticShader {

	public static final String vertexFile = "src/engine/shaders/staticShader/vertexShader.vert";
	public static final String fragmentFile = "src/engine/shaders/staticShader/borderedFragmentShader.frag";
	
	private int location_borderColor;
	
	public BorderedStaticShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}
	
	public static BorderedStaticShader init() {
		return new BorderedStaticShader(vertexFile, fragmentFile);
	}
	
	@Override protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		location_borderColor = super.getUniformLocation("borderColor");
	}
	
	public void loadBorderColor(Vector4f color) {
		super.loadVector4fToUniform(color, location_borderColor);
	}

}
