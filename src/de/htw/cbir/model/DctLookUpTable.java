package de.htw.cbir.model;

public class DctLookUpTable { 
	
	private static final DctLookUpTable OBJ = new DctLookUpTable(); 
	private static final int dim = 8;
	
	private float[][] table;
	private float sqrt1_4;
	private float sqrt1_8;
	
	private DctLookUpTable() {
		sqrt1_4 = (float) Math.sqrt(0.25);
		sqrt1_8 = (float) Math.sqrt(0.125);
		table = new float[dim][dim];
		
		for (int x = 0; x < dim; x++) {
			for (int y = 0; y < dim; y++) {
				table[x][y] = (float) Math.cos(Math.PI/16*(2*x+1)*y);
			}
		}
	}
	
	public static DctLookUpTable getInstance() {
		return OBJ;
	} 
	
	public float getCosTerm(int first, int second){
		return table[first][second];
	}
	
	public float getCi(int i){
		if (i==0) {
			return sqrt1_8;
		} else {
			return sqrt1_4;
		}
	}
}