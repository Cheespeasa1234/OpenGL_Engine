package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class MathUtils {
	
	public static final Vector3f X_VECTOR = new Vector3f(1, 0, 0);
	public static final Vector3f Y_VECTOR = new Vector3f(0, 1, 0);
	public static final Vector3f Z_VECTOR = new Vector3f(0, 0, 1);

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), X_VECTOR, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), Y_VECTOR, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), Z_VECTOR, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), X_VECTOR, viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), Y_VECTOR, viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), Z_VECTOR, viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	
}
