package world;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
	public static final int SIZE = 16;
	public static final int HEIGHT = 64;
	
	private List<Block> blockPalette;
	private int[] blocks;
	
	public Chunk(Block[][][] uncompressedBlocks) {
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
						paletteIndex = blockPalette.size() + 1;
						blockPalette.add(block);	
					}
					
					// Enter the palleteIndex into the full list of blocks
					blocks[position] = paletteIndex;
					position++; // Used to convert the 3d array to a 1d array
				}
			}
		}
	}
	
	public Block getBlock(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x > SIZE || y > HEIGHT || z > SIZE) {
			return null;
		}
		
		int position = x + SIZE * (z + y * HEIGHT);
		int paletteIndex = blocks[position];
		return blockPalette.get(paletteIndex);
	}
}
