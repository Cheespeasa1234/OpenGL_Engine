package engine.engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.Renderer;
import engine.shaders.StaticShader;
import engine.textures.ModelTexture;
import util.MathUtils;
import engine.renderEngine.OBJLoader;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);		
		
		RawModel model = OBJLoader.loadObjModel("res/chair.obj", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("res/wood_texture.png"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		Camera camera = new Camera();
		
		while (!Display.isCloseRequested()) {
			// game logic
			entity.increaseRotation(0.5f, 0.5f, 0f);
			camera.move();
			
			// render
			renderer.prepare();
			shader.start();
			shader.loadLight(light);
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
