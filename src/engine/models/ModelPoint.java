package engine.models;

public class ModelPoint {
	public float vertexX;
	public float vertexY;
	public float vertexZ;

	public float textureX;
	public float textureY;
	
	public float normalX;
	public float normalY;
	public float normalZ;
	public ModelPoint(float vertexX, float vertexY, float vertexZ, float textureX, float textureY, float normalX,
			float normalY, float normalZ) {
		super();
		this.vertexX = vertexX;
		this.vertexY = vertexY;
		this.vertexZ = vertexZ;
		this.textureX = textureX;
		this.textureY = textureY;
		this.normalX = normalX;
		this.normalY = normalY;
		this.normalZ = normalZ;
	}
	
	public ModelPoint transform(float x, float y, float z) {
		return new ModelPoint(vertexX + x, vertexY + y, vertexZ + z, textureX, textureY, normalX, normalY, normalZ);
		
	}
}