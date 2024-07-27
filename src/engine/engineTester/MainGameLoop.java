package engine.engineTester;

import java.util.Arrays;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.models.Mesh;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.Renderer;
import engine.shaders.staticShader.BorderedStaticShader;
import engine.shaders.staticShader.StaticShader;
import engine.textures.ModelTexture;
import util.MathUtils;
import util.PerlinNoise;
import world.Chunk;
import world.Generator;
import engine.renderEngine.OBJLoader;

public class MainGameLoop {
	
	private static HashMap<String, Long> timers = new HashMap<String, Long>();
	private static HashMap<String, String> timerMessages = new HashMap<String, String>();
	private static void startTimer(String message, String id) {
		timers.put(id, System.currentTimeMillis());
		timerMessages.put(id, message);
	}
	private static void endTimer(String id) {
		long elapsed = System.currentTimeMillis() - timers.get(id);
		System.out.println(timerMessages.get(id)+ elapsed + "ms");
		timers.remove(id);
		timerMessages.remove(id);
	}
	
	private static Entity[] prepareToRenderMeshes(Mesh[] meshes, Loader loader, ModelTexture texture) {
		TexturedModel[] texturedModels = Arrays.stream(meshes)
			.map(mesh -> new TexturedModel(OBJLoader.loadObjModel(mesh.getVertices(), mesh.getIndices(), loader), texture))
			.toArray(TexturedModel[]::new);
		Entity entities[] = Arrays.stream(texturedModels)
			.map(model -> new Entity(model, new Vector3f(0, 0, -25), 0, 0, 0, 1))
			.toArray(Entity[]::new);

		
		return entities;
	}
	
	private static Vector4f cycle(long tick) {
	    float hue = (tick % 360) / 360.0f; // Cycle hue based on tick
	    float saturation = 1.0f;           // Full saturation
	    float brightness = 1.0f;           // Full brightness
	    float alpha = 1.0f;                // Full alpha
	    
	    // Convert HSB to RGB
	    int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
	    
	    // Extract RGB components
	    float red = ((rgb >> 16) & 0xFF) / 255.0f;
	    float green = ((rgb >> 8) & 0xFF) / 255.0f;
	    float blue = (rgb & 0xFF) / 255.0f;
	    
	    // Return as Vector4f
	    return new Vector4f(red, green, blue, alpha);
	}
	
	private static String formatVector4f(Vector4f vector) {
	    return String.format("X: %4.3f Y: %4.3f Z: %4.3f W: %4.3f", vector.w, vector.x, vector.y, vector.z);
	}

	public static void main(String[] args) {
		
		// Load the display and the static shader
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = StaticShader.init();
		BorderedStaticShader borderShader = BorderedStaticShader.init();
		
		System.out.println(shader.toString());
		System.out.println(borderShader.toString());
		
		Renderer renderer = new Renderer(shader, borderShader);		
		
		// Load a model
		Generator chunkGen = new Generator();
		PerlinNoise noise = new PerlinNoise(10);
		
		// Generate the world
		int size = 4;
		Chunk[] chunks = new Chunk[size * size];
		startTimer("Generated " + chunks.length + " chunks in ", "generator");
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				int index = x + z * size;
				chunks[index] = chunkGen.generateChunk(x, z, noise);
			}
		}
		// Set neighbors for each chunk
		for (int x = 0; x < size; x++) {
		    for (int z = 0; z < size; z++) {
		        int index = x + z * size;
		        Chunk currentChunk = chunks[index];

		        currentChunk.northNeighbor = (z > 0) ? chunks[index - size] : null;
		        currentChunk.southNeighbor = (z < size - 1) ? chunks[index + size] : null;
		        currentChunk.westNeighbor = (x > 0) ? chunks[index - 1] : null;
		        currentChunk.eastNeighbor = (x < size - 1) ? chunks[index + 1] : null;
		    }
		}
		endTimer("generator");
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("res/wood_texture.png"));

		startTimer("Meshed in ", "meshing");
		Mesh[] meshes = Arrays.stream(chunks)
			.map(chunk -> chunk.getMesh())
			.toArray(Mesh[]::new);
		Mesh[] highlightedMeshes = Arrays.stream(chunks)
			.map(chunk -> chunk.getHighlightedMesh())
			.toArray(Mesh[]::new);
		Mesh[] chunkBorderMeshes = Arrays.stream(chunks)
			.map(chunk -> chunk.getChunkBorderMesh())
			.toArray(Mesh[]::new);
		endTimer("meshing");
		
		startTimer("Turned to entities in ", "entities");
		Entity[] normalChunks = prepareToRenderMeshes(meshes, loader, texture);
		Entity[] highlightedChunks = prepareToRenderMeshes(highlightedMeshes, loader, texture);
		Entity[] chunkBorders = prepareToRenderMeshes(chunkBorderMeshes, loader, texture);
		endTimer("entities");
		
		Light light = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Camera camera = new Camera();
		
		boolean renderMode = true;
		boolean rDownPrev = false;
		long framesRendered = 0;
		
		while (!Display.isCloseRequested()) {
			// game logic
//			entity.increaseRotation(0.5f, 0.5f, 0f);
			camera.move();
			Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
			
			boolean rDown = Keyboard.isKeyDown(Keyboard.KEY_R);
			if (!rDown && rDownPrev) {
				renderMode = !renderMode;
			}
			rDownPrev = rDown;
			
			// prepare to render
			renderer.prepare();

			// render
			// SHADER 1
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(viewMatrix);
			if (renderMode) {
				
				Arrays.stream(normalChunks)
					.forEach(chunk -> renderer.render(chunk, shader));
			}
			shader.stop();
			
			borderShader.start();
			borderShader.loadLight(light);
			borderShader.loadViewMatrix(viewMatrix);
			Vector4f borderColor = cycle(framesRendered);
			System.out.println(formatVector4f(borderColor));
			borderShader.loadBorderColor(borderColor);
			if (!renderMode) {
				
				Arrays.stream(highlightedChunks)
					.forEach(chunk -> renderer.render(chunk, borderShader));
				
			}
			
			Arrays.stream(chunkBorders)
				.forEach(chunkBorder -> renderer.render(chunkBorder, borderShader));
			borderShader.stop();
			
			DisplayManager.updateDisplay();
			
			framesRendered++;
		}
		
		shader.cleanUp();
		borderShader.cleanUp();
		
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}

}
