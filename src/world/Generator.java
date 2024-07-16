package world;

import util.PerlinNoise;

public class Generator {
	public Chunk generateChunk(int chunkX, int chunkZ, PerlinNoise noise) {
		Block[][][] blocks = new Block[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
		for (int x = 0; x < Chunk.SIZE; x++) {
			for (int z = 0; z < Chunk.SIZE; z++) {
				int blockX = chunkX * Chunk.SIZE + x;
				int blockY = 0;
				int blockZ = chunkZ * Chunk.SIZE + z;
				
				float normalizedHeight = (noise.smoothNoise(blockX, blockY, blockZ, 8) + 1) / 2;
				float height = normalizedHeight * Chunk.HEIGHT;
				
				int y;
				for (y = 0; y < height; y++) {
					blocks[z][y][x] = new Block(Block.BlockInformation.BLOCKID_STONE, Block.BlockInformation.PROPERTIES_NONE);
				}
				for (; y < Chunk.HEIGHT; y++) {
					blocks[z][y][x] = new Block(Block.BlockInformation.BLOCKID_AIR, Block.BlockInformation.PROPERTIES_NONE);
				}
			}
		}
		
		Chunk chunk = new Chunk(blocks);
		return chunk;
	}
	
	public static void main(String[] args) {
		Chunk c = new Generator().generateChunk(0, 0, new PerlinNoise(100));
		for (int y = 0; y < Chunk.HEIGHT; y++) {
			System.out.println("Slice " + y + ": ");
			for (int x = 0; x < Chunk.SIZE; x++) {
				System.out.print("\t");
				for (int z = 0; z < Chunk.SIZE; z++) {
					Block block = c.getBlock(x, y, z);
					System.out.print(block.getBlockId() == Block.BlockInformation.BLOCKID_AIR ? ". " : "+");
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
		}
	}
}
