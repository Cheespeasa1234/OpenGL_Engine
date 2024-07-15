package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.MathUtils;
import toolbox.Shapes;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);		
		
		RawModel model = loader.loadToVAO(Shapes.vertices, Shapes.textureCoords, Shapes.indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("res/obama.png"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
		Camera camera = new Camera();
		
		while (!Display.isCloseRequested()) {
			// game logic
			entity.increaseRotation(0.5f, 0.5f, 0.5f);
			camera.move();
			
			// render
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(MathUtils.createViewMatrix(camera));
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}

}
