package de.htw.cbir.model;

public class FullDctEngine {
	
	int[] origArgb;
	int[][] processedArgb;
	int width;
	int height;
	int dim;
	FullDctBlock dctBlock;
	float[] histo;
	
	public float[] getHisto() {
		return histo;
	}

	public FullDctEngine(int[] argb, int width, int height) {
		this.origArgb = argb;
		this.width = width;
		this.height = height;
		
		if (width<=height) {
			this.dim = width;
		}else{
			this.dim = height;
		}
		initProcessedArgb();
	}

	private void initProcessedArgb() {
		this.processedArgb = new int[this.dim][this.dim];
		for (int y = 0; y < this.dim; y++) {
			for (int x = 0; x < this.dim; x++) {
				int pos = y*this.width+x;
				int greyValue = (((origArgb[pos] >> 16) & 0xff)
						+ ((origArgb[pos] >> 8) & 0xff) + (origArgb[pos] & 0xff)) / 3;
				processedArgb[x][y] = greyValue;
			}
		}
	}
	
	public void runDct() {
		dctBlock = new FullDctBlock(processedArgb);
		dctBlock.calcCoeffs();
//		System.out.println(dctBlock.toString());
			
	}
	
	public void generateHistogram (){
		histo = new float[30];
		int histoIndex = 0;
		
		int[][] f1 = FullDctFeatureRegion.f1;
		for (int i = 0; i < f1.length; i++) {
			histo[histoIndex] = dctBlock.getCoeff(f1[i][0], f1[i][1]);
			histoIndex++;
		}
		int[][] f2 = FullDctFeatureRegion.f2;
		for (int i = 0; i < f2.length; i++) {
			histo[histoIndex] = dctBlock.getCoeff(f2[i][0], f2[i][1]);
			histoIndex++;
		}
		int[][] f3 = FullDctFeatureRegion.f3;
		for (int i = 0; i < f3.length; i++) {
			histo[histoIndex] = dctBlock.getCoeff(f3[i][0], f3[i][1]);
			histoIndex++;
		}
		int[][] f4 = FullDctFeatureRegion.f4;
		for (int i = 0; i < f4.length; i++) {
			histo[histoIndex] = dctBlock.getCoeff(f4[i][0], f4[i][1]);
			histoIndex++;
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
	
	public float compareFeatureVectors (float[] fv1, float[] fv2){
		int fvCounter = 0;
		float fv1E[][] = new float[4][];
		float fv2E[][] = new float[4][];
		for (int fNumber = 0; fNumber < 4; fNumber++) {
			int[][] f = FullDctFeatureRegion.all[fNumber];
			fv1E[fNumber] = new float[f.length];
			fv2E[fNumber] = new float[f.length];
			for (int i = 0; i < f.length; i++) {
				fv1E[fNumber][i] = fv1[fvCounter];
				fv2E[fNumber][i] = fv2[fvCounter];
				fvCounter++;
			}
		}
		
		return 0;
	}
}