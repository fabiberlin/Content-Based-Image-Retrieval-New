package de.htw.cbir.model;

public class DctEngine {
	
	int[] origArgb;
	int[][] processedArgb;
	int width;
	int height;
	DctBlock[] dctBlocks;
	
	public DctEngine(int[] argb, int width, int height) {
		this.origArgb = argb;
		this.width = width;
		this.height = height;
		initProcessedArgb();
	}

	private void initProcessedArgb() {
		this.processedArgb = new int[this.width][this.height];
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				int pos = y*this.width+x;
				int greyValue = (((origArgb[pos] >> 16) & 0xff)
						+ ((origArgb[pos] >> 8) & 0xff) + (origArgb[pos] & 0xff)) / 3;
				processedArgb[x][y] = greyValue;
			}
		}
	}
	
	public void runDct() {
		int dim = DctBlock.DIM;
		int numXblocks = width/8;
		int numYblocks = height/8;
		dctBlocks = new DctBlock[numXblocks*numYblocks];
		int blockIndex = 0;
		
		for (int xOffset = 0; xOffset < numXblocks; xOffset++) {
			for (int yOffset = 0; yOffset < numYblocks; yOffset++) {
				int[][] argbBlock = getBlock(xOffset, yOffset, dim);
				DctBlock dctBlock = new DctBlock(argbBlock);
				dctBlock.calcCoeffs();
				dctBlocks[blockIndex] = dctBlock;				
			}
		}
	}

	private int[][] getBlock(int xOffset, int yOffset, int dim) {
		int[][] block = new int[dim][dim];
		for (int x = 0; x < dim; x++) {
			for (int y = 0; y < dim; y++) {
				block[x][y] = this.processedArgb[xOffset + x][yOffset + y];
			}
		}
		return block;
	}
	
	public void generateHistogram (){
		
	}
	

}
