package world;

public class Block {
	
	public static class BlockInformation {
		public static final int BLOCKID_AIR = 0;
		public static final int BLOCKID_STONE = 1;
		
		public static final int PROPERTIES_NONE = 0b00000000000000000000000000000000;
	}
	
	private int blockId;
	private int properties;
	
	public Block(int blockId, int properties) {
		this.blockId = blockId;
		this.properties = properties;
	}
	
	public int getBlockId() {
		return blockId;
	}
	
	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	
	public int getProperties() {
		return properties;
	}
	
	public void setProperties(int properties) {
		this.properties = properties;
	}
	
	@Override public boolean equals(Object other) {
		return other instanceof Block && this.equals((Block) other);
	}
	
	public boolean equals(Block other) {
		return this.blockId == other.blockId && this.properties == other.properties;
	}
	
}
