package world;

import util.PerlinNoise;

public class Generator {
	public Chunk generateChunk(int chunkX, int chunkZ, PerlinNoise noise) {
		long start = System.currentTimeMillis();
		Block[][][] blocks = new Block[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
		for (int x = 0; x < Chunk.SIZE; x++) {
			for (int z = 0; z < Chunk.SIZE; z++) {
//				System.out.println(x + "," + z);
				
				float normalizedHeight = (float) noise.smoothNoise(x / 2.0f / (float) Chunk.SIZE + (float) chunkX, 0, z / 2.0f / (float) Chunk.SIZE + (float) chunkZ, 4); // Assuming octaves is 4
//				float height = (float) Math.random() * (float) Chunk.HEIGHT;
				float height = (normalizedHeight / 2) * (float) Chunk.HEIGHT + Chunk.HEIGHT / 2;
				
				int y;
				for (y = 0; y < height; y++) {
					blocks[z][y][x] = new Block(Block.BlockInformation.BLOCKID_STONE, Block.BlockInformation.PROPERTIES_NONE);
				}
				for (; y < Chunk.HEIGHT; y++) {
					blocks[z][y][x] = new Block(Block.BlockInformation.BLOCKID_AIR, Block.BlockInformation.PROPERTIES_NONE);
				}
			}
		}
		
		long elapsed = System.currentTimeMillis() - start;
		System.out.println("Generated chunk [" + chunkX + "," + chunkZ + "] in " + elapsed + "ms");
		Chunk chunk = new Chunk(blocks, chunkX, chunkZ);
		return chunk;
	}
	
//	public static void main(String[] args) {
//		Chunk c = new Generator().generateChunk(0, 0, new PerlinNoise(100));
//		for (int y = 0; y < Chunk.HEIGHT; y++) {
//			System.out.println("Slice " + y + ": ");
//			for (int x = 0; x < Chunk.SIZE; x++) {
//				System.out.print("\t");
//				for (int z = 0; z < Chunk.SIZE; z++) {
//					Block block = c.getBlock(x, y, z);
//					System.out.print(block.getBlockId() == Block.BlockInformation.BLOCKID_AIR ? ". " : "+");
//				}
//				System.out.println();
//			}
//			System.out.println();
//			System.out.println();
//		}
//	}
}
