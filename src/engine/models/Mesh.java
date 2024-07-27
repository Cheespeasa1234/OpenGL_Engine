package engine.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Mesh {
	private List<ModelPoint> vertices;
	private List<Integer> indices;
	
	public Mesh() {
		this.vertices = new ArrayList<ModelPoint>();
		this.indices = new ArrayList<Integer>();
	}
	
	public Mesh(ModelPoint[] vertices, Integer[] indices) {
		this.vertices = Arrays.asList(vertices);
		this.indices = Arrays.asList(indices);
	}

	public Mesh(List<ModelPoint> vertices, List<Integer> indices) {
		this.vertices = vertices;
		this.indices = indices;
	}
	
	public void appendMesh(Mesh mesh) {
		int len = vertices.size();
		vertices.addAll(mesh.vertices);
		indices.addAll(mesh.indices.stream().map(n -> n + len).collect(Collectors.toList()));
	}
	
	public void appendMesh(Mesh... meshes) {
		for (Mesh mesh: meshes) {
			if (mesh != null) {
				appendMesh(mesh);				
			}
		}
	}
	
	public ModelPoint[] getVertices() {
		return this.vertices.stream().toArray(ModelPoint[]::new);
	}
	
	public int[] getIndices() {		
		return this.indices.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public Mesh transform(float x, float y, float z) {
		Mesh copy = new Mesh(this.vertices
			.stream()
			.map(point -> point.transform(x, y, z)).collect(Collectors.toList()), this.indices);
		
		return copy;
	}
	
	public static Mesh frontFaceVertices(float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scaleX+x,-scaleY+y,scaleZ+z,0,0,0,0,1),  // 0
		    new ModelPoint(scaleX+x,-scaleY+y,scaleZ+z,1,0,0,0,1),  // 1
		    new ModelPoint(scaleX+x,scaleY+y,scaleZ+z,1,1,0,0,1),  // 2
		    new ModelPoint(-scaleX+x,scaleY+y,scaleZ+z,0,1,0,0,1)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public static Mesh backFaceVertices(float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scaleX+x,-scaleY+y,-scaleZ+z,0,0,0,0,-1),  // 0
		    new ModelPoint(scaleX+x,-scaleY+y,-scaleZ+z,1,0,0,0,-1),  // 1
		    new ModelPoint(scaleX+x,scaleY+y,-scaleZ+z,1,1,0,0,-1),  // 2
		    new ModelPoint(-scaleX+x,scaleY+y,-scaleZ+z,0,1,0,0,-1)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public static Mesh leftFaceVertices(float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scaleX+x,-scaleY+y,-scaleZ+z,0,0,-1,0,0),  // 0
		    new ModelPoint(-scaleX+x,-scaleY+y,scaleZ+z,1,0,-1,0,0),  // 1
		    new ModelPoint(-scaleX+x,scaleY+y,scaleZ+z,1,1,-1,0,0),  // 2
		    new ModelPoint(-scaleX+x,scaleY+y,-scaleZ+z,0,1,-1,0,0)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public static Mesh rightFaceVertices(float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(scaleX+x,-scaleY+y,-scaleZ+z,0,0,1,0,0),  // 0
		    new ModelPoint(scaleX+x,-scaleY+y,scaleZ+z,1,0,1,0,0),  // 1
		    new ModelPoint(scaleX+x,scaleY+y,scaleZ+z,1,1,1,0,0),  // 2
		    new ModelPoint(scaleX+x,scaleY+y,-scaleZ+z,0,1,1,0,0)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public static Mesh topFaceVertices(float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scaleX+x,scaleY+y,-scaleZ+z,0,0,0,1,0),  // 0
		    new ModelPoint(scaleX+x,scaleY+y,-scaleZ+z,1,0,0,1,0),  // 1
		    new ModelPoint(scaleX+x,scaleY+y,scaleZ+z,1,1,0,1,0),  // 2
		    new ModelPoint(-scaleX+x,scaleY+y,scaleZ+z,0,1,0,1,0)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public static Mesh bottomFaceVertices(float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scaleX+x,-scaleY+y,-scaleY+z,0,0,0,-1,0),  // 0
		    new ModelPoint(scaleX+x,-scaleY+y,-scaleY+z,1,0,0,-1,0),  // 1
		    new ModelPoint(scaleX+x,-scaleY+y,scaleY+z,1,1,0,-1,0),  // 2
		    new ModelPoint(-scaleX+x,-scaleY+y,scaleY+z,0,1,0,-1,0)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public static Mesh getCube(float x, float y, float z, float scale) {
		return getPrism(x, y, z, scale, scale, scale);
	}
	
	public static Mesh getPrism(float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
		Mesh frontFace = frontFaceVertices(  x * scaleX * 2.0f, y * scaleY * 2.0f, z * scaleZ * 2.0f, scaleX, scaleY, scaleZ);
		Mesh backFace = backFaceVertices(    x * scaleX * 2.0f, y * scaleY * 2.0f, z * scaleZ * 2.0f, scaleX, scaleY, scaleZ);
		Mesh leftFace = leftFaceVertices(    x * scaleX * 2.0f, y * scaleY * 2.0f, z * scaleZ * 2.0f, scaleX, scaleY, scaleZ);
		Mesh rightFace = rightFaceVertices(  x * scaleX * 2.0f, y * scaleY * 2.0f, z * scaleZ * 2.0f, scaleX, scaleY, scaleZ);
		Mesh topFace = topFaceVertices(      x * scaleX * 2.0f, y * scaleY * 2.0f, z * scaleZ * 2.0f, scaleX, scaleY, scaleZ);
		Mesh bottomFace = bottomFaceVertices(x * scaleX * 2.0f, y * scaleY * 2.0f, z * scaleZ * 2.0f, scaleX, scaleY, scaleZ);
		
		Mesh prism = new Mesh();
		prism.appendMesh(frontFace, backFace, leftFace, rightFace, topFace, bottomFace);
		
		return prism;
		
	}
}
