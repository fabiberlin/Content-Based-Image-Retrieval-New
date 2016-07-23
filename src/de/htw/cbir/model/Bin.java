package de.htw.cbir.model;

import java.util.ArrayList;

public class Bin {

	private ArrayList<ColorValue> colorvalues;
	private ColorValue represant;
	
	public Bin (int r, int g, int b){
		represant = new ColorValue(r, g, b);
		colorvalues = new ArrayList<>();
	}
	
	public void addToBin(ColorValue colorValue) {
		colorvalues.add(colorValue);
	}
	
	public void calcCenterPoint() {
		float x = 0;
		float y = 0;
		for (ColorValue colorValue : colorvalues) {
			x += colorValue.x;
			y += colorValue.y;
		}
		if (colorvalues.size()>0) {
			x /= colorvalues.size();			
			y /= colorvalues.size();			
		} else {
			x = 0;
			y = 0;
		}
	
		represant.x = x;
		represant.y = y;
	}

	public ColorValue getRepresant() {
		return represant;
	}	
}
