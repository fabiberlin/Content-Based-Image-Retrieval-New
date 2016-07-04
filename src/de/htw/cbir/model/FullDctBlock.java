package de.htw.cbir.model;

public class FullDctBlock {
	
	private int dim;
	
	private int[][] argb;
	private float[][] coeffs;
	
	public FullDctBlock (int[][] argbValues){
		this.argb = argbValues;
		this.dim = argbValues.length;
		this.coeffs = new float[6][6];
	}
	
	public void calcCoeffs (){
		
		FullDctLookUpTable dctLookUpTable =  new FullDctLookUpTable(dim);
		
		for (int u = 0; u < 6; u++) {
			for (int v = 0; v < 6; v++) {
				float ck = dctLookUpTable.getCi(u); //TODO
				float ci = dctLookUpTable.getCi(v); //TODO
				float sum = 0;
				for (int i = 0; i < dim; i++) {
					for (int j = 0; j < dim; j++) {
						int arbgValue = argb[i][j];
						float left = dctLookUpTable.getCosTerm(i, u); //TODO
						float right = dctLookUpTable.getCosTerm(j, v); //TODO
						sum += arbgValue*left*right;
					}
				}
				float value = (2/new Float(dim))*ck*ci*sum;
				coeffs[u][v] = value;
			}
		}
	}
	
	public float getCoeff (int x, int y){
		return coeffs[x][y];
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "";
		for (int i = 0; i < coeffs.length; i++) {
			for (int j = 0; j < coeffs[i].length; j++) {
				s+=coeffs[i][j]+"  ";
			}
			s+="\n";
		}
		return s;
	}
}
