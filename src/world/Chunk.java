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
	
	public int getIndexFromCoords(int[] coords) {
		return coords[0] + (coords[1] * HEIGHT) + (coords[2] * SIZE);
	}
	
	public Block getBlock(int x, int y, int z) {
	    if (x < 0 || y < 0 || z < 0 || x >= SIZE || y >= HEIGHT || z >= SIZE) {
	        return new Block(Block.BlockInformation.BLOCKID_AIR, Block.BlockInformation.PROPERTIES_NONE);
	    }

	    int position = getIndexFromCoords(new int[] {x, y, z});
	    int paletteIndex = blocks[position];
	    return blockPalette.get(paletteIndex);
	}
	
	public float scale = 0.15f;
	
	
	
	public boolean blockIsVisible(int x, int y, int z) {
//		System.out.println(x + "," + y + "," + z);
//		if (x == 0 || x == SIZE - 1 || y == 0 || y == HEIGHT - 1 || z == 0 || z == SIZE - 1) {
//			return true;
//		}
		
		if (Block.isTransparent(getBlock(x+1,y,z))) return true;
		if (Block.isTransparent(getBlock(x-1,y,z))) return true;
		if (Block.isTransparent(getBlock(x,y+1,z))) return true;
		if (Block.isTransparent(getBlock(x,y-1,z))) return true;
		if (Block.isTransparent(getBlock(x,y,z+1))) return true;
		if (Block.isTransparent(getBlock(x,y,z-1))) return true;
		
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
	        	
	        	chunkMesh.appendMesh(Mesh.getCube(coords[0], coords[1], coords[2], scale));
	        }
	    }
	    
	    float chunkXTransform = ((float) chunkX) * ((float) SIZE) * scale * 4.0f;
	    float chunkZTransform = ((float) chunkZ) * ((float) SIZE) * scale * 4.0f;
	    chunkMesh = chunkMesh.transform(chunkXTransform, 0, chunkZTransform);
	    return chunkMesh;
	}
	
	public Mesh getChunkBorderMesh() {
		float chunkXTransform = ((float) chunkX) * ((float) SIZE) * scale * 4.0f;
		float chunkZTransform = ((float) chunkZ) * ((float) SIZE) * scale * 4.0f;
		Mesh chunkBorderMesh = Mesh.getPrism(0, 0, 0, scale * SIZE, scale * HEIGHT, scale * SIZE);
		chunkBorderMesh = chunkBorderMesh.transform(chunkXTransform, 0, chunkZTransform);
		return chunkBorderMesh;
	}
	
	public Mesh getMesh() {
	    // Array Definitions
	    Mesh chunkMesh = new Mesh();
	    
	    // For every voxel in the chunk:
	    for (int i = 0; i < blocks.length; i++) {
	        if (blockPalette.get(blocks[i]).getBlockId() != Block.BlockInformation.BLOCKID_AIR) {
	            
	        	int[] coords = getCoordsFromIndex(i);
	        	
                chunkMesh.appendMesh(Mesh.getCube(coords[0], coords[1], coords[2], scale));
	        }
	    }
	    
	    float chunkXTransform = ((float) chunkX) * ((float) SIZE) * scale * 4.0f;
	    float chunkZTransform = ((float) chunkZ) * ((float) SIZE) * scale * 4.0f;
	    chunkMesh = chunkMesh.transform(chunkXTransform, 0, chunkZTransform);
	    return chunkMesh;
	}


}
