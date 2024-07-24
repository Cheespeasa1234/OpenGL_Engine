package world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import engine.models.Mesh;
import engine.models.ModelPoint;
import engine.models.RawModel;
import engine.renderEngine.Loader;
import engine.renderEngine.OBJLoader;

public class Chunk {
	public static final int SIZE = 16;
	public static final int HEIGHT = 64;
	
	public int chunkX;
	public int chunkZ;
	
	private List<Block> blockPalette;
	private int[] blocks;
	
	public Chunk northNeighbor;
	public Chunk southNeighbor;
	public Chunk westNeighbor;
	public Chunk eastNeighbor;
	
	public Chunk() {
		
	}
	
	public Chunk(Block[][][] uncompressedBlocks, int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.blocks = new int[SIZE * SIZE * HEIGHT];
		blockPalette = new ArrayList<Block>();
		int position = 0;
		
		for (Block[][] slice : uncompressedBlocks) {
			for (Block[] row : slice) {
				for (Block block : row) {
					int paletteIndex = blockPalette.indexOf(block);
					
					// If the block is not yet in the palette
					if (paletteIndex == -1) {
						// Enter it into the palette
						paletteIndex = blockPalette.size();
						blockPalette.add(block);	
					}
					
					// Enter the palleteIndex into the full list of blocks
					blocks[position] = paletteIndex;
					position++; // Used to convert the 3d array to a 1d array
				}
			}
		}
	}

	public int[] getCoordsFromIndex(int index) {
		int z = index / (SIZE * HEIGHT);
        int y = (index % (SIZE * HEIGHT)) / SIZE;
        int x = index % SIZE;
		return new int[] {x, y, z};
	}
	
	public Block getBlock(int x, int y, int z) {
	    if (x < 0 || y < 0 || z < 0 || x >= SIZE || y >= HEIGHT || z >= SIZE) {
	        return new Block(Block.BlockInformation.BLOCKID_AIR, Block.BlockInformation.PROPERTIES_NONE);
	    }

	    int position = x + SIZE * (z + y * SIZE);
	    int paletteIndex = blocks[position];
	    return blockPalette.get(paletteIndex);
	}
	
	public float scale = 0.15f;
	
	public Mesh frontFaceVertices(float x, float y, float z) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scale+x,-scale+y,scale+z,0,0,0,0,1),  // 0
		    new ModelPoint(scale+x,-scale+y,scale+z,1,0,0,0,1),  // 1
		    new ModelPoint(scale+x,scale+y,scale+z,1,1,0,0,1),  // 2
		    new ModelPoint(-scale+x,scale+y,scale+z,0,1,0,0,1)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public Mesh backFaceVertices(float x, float y, float z) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scale+x,-scale+y,-scale+z,0,0,0,0,-1),  // 0
		    new ModelPoint(scale+x,-scale+y,-scale+z,1,0,0,0,-1),  // 1
		    new ModelPoint(scale+x,scale+y,-scale+z,1,1,0,0,-1),  // 2
		    new ModelPoint(-scale+x,scale+y,-scale+z,0,1,0,0,-1)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public Mesh leftFaceVertices(float x, float y, float z) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scale+x,-scale+y,-scale+z,0,0,-1,0,0),  // 0
		    new ModelPoint(-scale+x,-scale+y,scale+z,1,0,-1,0,0),  // 1
		    new ModelPoint(-scale+x,scale+y,scale+z,1,1,-1,0,0),  // 2
		    new ModelPoint(-scale+x,scale+y,-scale+z,0,1,-1,0,0)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public Mesh rightFaceVertices(float x, float y, float z) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(scale+x,-scale+y,-scale+z,0,0,1,0,0),  // 0
		    new ModelPoint(scale+x,-scale+y,scale+z,1,0,1,0,0),  // 1
		    new ModelPoint(scale+x,scale+y,scale+z,1,1,1,0,0),  // 2
		    new ModelPoint(scale+x,scale+y,-scale+z,0,1,1,0,0)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public Mesh topFaceVertices(float x, float y, float z) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scale+x,scale+y,-scale+z,0,0,0,1,0),  // 0
		    new ModelPoint(scale+x,scale+y,-scale+z,1,0,0,1,0),  // 1
		    new ModelPoint(scale+x,scale+y,scale+z,1,1,0,1,0),  // 2
		    new ModelPoint(-scale+x,scale+y,scale+z,0,1,0,1,0)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public Mesh bottomFaceVertices(float x, float y, float z) {
		return new Mesh(new ModelPoint[] {
			new ModelPoint(-scale+x,-scale+y,-scale+z,0,0,0,-1,0),  // 0
		    new ModelPoint(scale+x,-scale+y,-scale+z,1,0,0,-1,0),  // 1
		    new ModelPoint(scale+x,-scale+y,scale+z,1,1,0,-1,0),  // 2
		    new ModelPoint(-scale+x,-scale+y,scale+z,0,1,0,-1,0)   // 3
		}, new Integer[] {0, 1, 2, 2, 3, 0});
	}
	
	public boolean blockIsVisible(int x, int y, int z) {
		if (x == 0 || x == SIZE || y == 0 || y == HEIGHT || z == 0 || z == SIZE) {
			return true;
		}
		
		if (
			Block.isTransparent(this.getBlock(x + 1, y, z)) ||
			Block.isTransparent(this.getBlock(x - 1, y, z)) ||
			Block.isTransparent(this.getBlock(x, y + 1, z)) ||
			Block.isTransparent(this.getBlock(x, y - 1, z)) ||
			Block.isTransparent(this.getBlock(x, y, z + 1)) ||
			Block.isTransparent(this.getBlock(x, y, z - 1))) 
		{
			return true;
		}
		
		return false;
	}
	
	public Mesh getHighlightedMesh() {
		// Array Definitions
	    Mesh chunkMesh = new Mesh();
	    
	    // For every voxel in the chunk:
	    for (int i = 0; i < blocks.length; i++) {
	        if (blockPalette.get(blocks[i]).getBlockId() != Block.BlockInformation.BLOCKID_AIR) {
	            
	        	int[] coords = getCoordsFromIndex(i);
	        	if (blockIsVisible(coords[0], coords[1], coords[2])) {
	        		continue;
	        	}
	        	
	        	Mesh frontFace = frontFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh backFace = backFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh leftFace = leftFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh rightFace = rightFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh topFace = topFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh bottomFace = bottomFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            
	            Mesh cube = new Mesh();
                cube.appendMesh(frontFace);
                cube.appendMesh(backFace);
                cube.appendMesh(leftFace);
                cube.appendMesh(rightFace);
                cube.appendMesh(topFace);
                cube.appendMesh(bottomFace);
	            
	            chunkMesh.appendMesh(cube);
	        }
	    }
	    
	    return chunkMesh.transform(chunkX * scale * SIZE, 0, chunkZ * scale * SIZE);
	}
	
	public Mesh getMesh() {
	    // Array Definitions
	    Mesh chunkMesh = new Mesh();
	    
	    // For every voxel in the chunk:
	    for (int i = 0; i < blocks.length; i++) {
	        if (blockPalette.get(blocks[i]).getBlockId() != Block.BlockInformation.BLOCKID_AIR) {
	            
	        	int[] coords = getCoordsFromIndex(i);
	        	
	        	Mesh frontFace = frontFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh backFace = backFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh leftFace = leftFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh rightFace = rightFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh topFace = topFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            Mesh bottomFace = bottomFaceVertices(coords[0] * scale * 2, coords[1] * scale * 2, coords[2] * scale * 2);
	            
	            Mesh cube = new Mesh();
                cube.appendMesh(frontFace);
                cube.appendMesh(backFace);
                cube.appendMesh(leftFace);
                cube.appendMesh(rightFace);
                cube.appendMesh(topFace);
                cube.appendMesh(bottomFace);
	            
	            chunkMesh.appendMesh(cube);
	        }
	    }
	    
	    return chunkMesh.transform(chunkX * scale * SIZE, 0, chunkZ * scale * SIZE);
	}


}
