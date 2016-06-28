package de.htw.cbir.model;

public class DctEngine {
	
	int[] origArgb;
	int[][] processedArgb;
	int width;
	int height;
	DctBlock[] dctBlocks;
	float[] histo;
	
	public float[] getHisto() {
		return histo;
	}

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
				blockIndex++;
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
		histo = new float[63];
		for (int x = 0; x < DctBlock.DIM; x++) {
			for (int y = 0; y < DctBlock.DIM; y++) {
				// for num Of Blocks
				if (x == 0 && y == 0) {
					//bias ignore
				} else {
					histo[y*DctBlock.DIM + x - 1] = 0;
					for (int i = 0; i < dctBlocks.length; i++) {
						histo[y*DctBlock.DIM + x -1] += Math.abs(this.dctBlocks[i].getCoeff(x, y));
					}
				}
			}
		}
	}
	
	public void normalizeHistogram() {
		float max = Float.MIN_VALUE;
		for (int i = 0; i < histo.length; i++) {
			float current = histo[i];
			if (current > max) {
				max = current;
			}
		}
		for (int i = 0; i < histo.length; i++) {
			histo[i] /= max;
		}
	}
}