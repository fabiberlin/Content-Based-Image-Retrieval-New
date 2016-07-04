package de.htw.cbir.model;

import java.util.Arrays;

public class FullDctLookUpTable {
	
	int dim;
	float sqrt2inverted;
	float[][]table;

	public FullDctLookUpTable(int dim) {
		super();
		this.dim = dim;
		calcValues();
	}

	private void calcValues() {
		this.sqrt2inverted = (float) (1/Math.sqrt(2));
		table = new float[dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int u = 0; u < dim; u++) {
				table[i][u] = (float) Math.cos(((2*i+1)*u*Math.PI)/(2*dim));
			}
		}
	}
	
	public float getCi(int i){
		if (i==0) {
			return sqrt2inverted;
		} else {
			return  1;
		}
	}
	
	public float getCosTerm(int first, int second){
		return table[first][second];
	}
}
