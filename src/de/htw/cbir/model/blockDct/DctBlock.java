package de.htw.cbir.model.blockDct;

public class DctBlock {
	
	static final int DIM = 8;
	
	private int[][] argb;
	private float[][] coeffs;
	
	public DctBlock (int[][] argbValues){
		this.argb = argbValues;
		this.coeffs = new float[DIM][DIM];
	}
	
	public void calcCoeffs (){
		
		DctLookUpTable lookUpTable = DctLookUpTable.getInstance();
		
		for (int k = 0; k < DIM; k++) {
			for (int l = 0; l < DIM; l++) {
				float ck = lookUpTable.getCi(k);
				float ci = lookUpTable.getCi(l);
				float sum = 0;
				for (int m = 0; m < DIM; m++) {
					for (int n = 0; n < DIM; n++) {
						int arbgValue = argb[m][n];
						float left = lookUpTable.getCosTerm(m, k);
						float right = lookUpTable.getCosTerm(n, l);
						sum += arbgValue*left*right;
					}
				}
				coeffs[k][l] = ck*ci*sum;
			}
		}
	}
	
	public float getCoeff (int x, int y){
		return coeffs[x][y];
	}
}
