package engine.models;

import engine.textures.ModelTexture;

/**
 * Stores a {@link RawModel} and a {@link ModelTexture} of a Model for rendering.
 */
public class TexturedModel {
	private RawModel rawModel;
	private ModelTexture texture;

	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
}
