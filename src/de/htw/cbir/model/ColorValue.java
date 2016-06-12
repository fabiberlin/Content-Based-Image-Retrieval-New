package de.htw.cbir.model;

public class ColorValue{
	public int r;
	public int g;
	public int b;
	
	public int amount;
	
	public ColorValue(){
		this.r = 0;
		this.g = 0;
		this.b = 0;
		this.amount=1;
	}
	
	public ColorValue(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
		this.amount=1;
	}
	
	public ColorValue(int argb){
		this.r =  (argb >> 16) & 255;
		this.g =  (argb >>  8) & 255;
		this.b =  (argb      ) & 255;
		this.amount = 1;
	}
	
	public int getArgb (){
		return (0xFF << 24) | (r << 16) | (g << 8) | b;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ColorValue) {
			ColorValue colorObj = (ColorValue) obj;
			if (colorObj.r == this.r && colorObj.g == this.g && colorObj.b == this.b) {
				return true;
			}
		}
		return false;
	}
	
	public int distanceTo (ColorValue other){
		return (this.r - other.r)*(this.r - other.r)+(this.g - other.g)*(this.g - other.g)+(this.b - other.b)*(this.b - other.b);
	}
	
	@Override
	public String toString() {
		String s = "Value: "+r+" "+g+" "+b+"\t Amount: "+amount;
		return s;
	}
}
