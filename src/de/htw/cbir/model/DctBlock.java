package de.htw.cbir.model;

public class DctBlock {
	
	static final int DIM = 8;
	
	private int[][] argb;
	private float[][] coeffs;
	
	public DctBlock (int[][] argbValues){
		this.argb = argbValues;
		this.coeffs = new float[DIM][DIM];
	}
	
	public void calcCoeffs (){
		
		for (int k = 0; k < DIM; k++) {
			for (int l = 0; l < DIM; l++) {
				float ck = getCi(k);
				float ci = getCi(l);
				float sum = 0;
				for (int m = 0; m < DIM; m++) {
					for (int n = 0; n < DIM; n++) {
						int arbgValue = argb[m][n];
						float left = (float) Math.cos(Math.PI/16*(2*m+1)*k); //TODO
						float right = (float) Math.cos(Math.PI/16*(2*n+1)*l); //TODO
						sum += arbgValue*left*right;
					}
				}
				coeffs[k][l] = ck*ci*sum;
			}
		}
	}
	
	private float getCi(int i){
		if (i==0) {
			return (float) Math.sqrt(0.125);
		} else {
			return (float) Math.sqrt(0.25);
		}
	}
	
	public float getCoeff (int x, int y){
		return coeffs[x][y];
	}
}
