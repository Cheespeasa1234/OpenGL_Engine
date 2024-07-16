package engine.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	/**
	 * Sets up and displays the main window.
	 */
	public static void createDisplay() {
		
		// Configure environment
		ContextAttribs attribs = new ContextAttribs(3,2)
			.withForwardCompatible(true)
			.withProfileCore(true);
		
		// Set up the display
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("First LWJGL Project");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// Tell OpenGL where it belongs
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
	}
	
	/**
	 * Updates the display, in sync with the FPS cap.
	 * @see DisplayManager#FPS_CAP
	 */
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	/**
	 * Destroys 
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
	
}
