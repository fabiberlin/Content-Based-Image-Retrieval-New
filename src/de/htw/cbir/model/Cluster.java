package de.htw.cbir.model;

import java.util.ArrayList;

import javax.print.attribute.standard.Sides;

public class Cluster implements Comparable<Cluster>{
	ArrayList<ColorValue> entries = new ArrayList<>();
	ColorValue centerPoint;
	
	public ArrayList<ColorValue> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<ColorValue> entries) {
		this.entries = entries;
	}
	
	public void clearEntries() {
		this.entries = new ArrayList<>();
	}
	
	public ColorValue getCenterPoint() {
		return centerPoint;
	}
	public void setCenterPoint(ColorValue centerPoint) {
		this.centerPoint = centerPoint;
	}
	
	public void calculateCenterpoint(){
		int r = 0, g = 0, b = 0, sum = 0;
		for (ColorValue colorValue : entries) {
			r += colorValue.r;
			g += colorValue.g;
			b += colorValue.b;
			sum++;
		}
		this.centerPoint = new ColorValue(r/sum, g/sum, b/sum);
	}
	
	public void setColorValuesFromArray(int[] values){
		for (int i = 0; i < values.length; i++) {
			ColorValue colorValue = new ColorValue(values[i]);
			int index = entries.indexOf(colorValue);
			if (index == -1) {
				//not contained
				entries.add(colorValue);
			}else{
				entries.get(index).amount++;
			}
		}
	}
	
	public ColorValue calcSplittedNeighborCenter() {
		int r = validateRange(centerPoint.r + 1);
		int g = validateRange(centerPoint.g + 1);
		int b = validateRange(centerPoint.b + 1);
		return new ColorValue(r, g, b);		
	}
	
	public int validateRange(int value){
		if(value > 255) return 254;
		if(value < 0) return 1;
		return value;
	}
	
	public int distanceToCenter(ColorValue colorValue) {
		return this.centerPoint.distanceTo(colorValue);		
	}
	
	@Override
	public String toString() {
		String s = "Center: "+centerPoint.r+" "+centerPoint.g+" "+centerPoint.b+"\tEntries: "+entries.size();
		return s;
	}
	@Override
	public int compareTo(Cluster arg0) {
		// TODO Auto-generated method stub
		if (this.entries.size() > arg0.entries.size()) {
			return -1;
		}else if (this.entries.size() < arg0.entries.size()) {
			return 1;
		}else{
			return 0;
		}
	}
	
}
