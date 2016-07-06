package de.htw.cbir.model;

public class Histogram {
	
	int numOfBins;
	int[][][] histrogram; // [R][G][B]
	
	public Histogram(int numOfBins) {
		super();
		this.numOfBins = numOfBins;
		histrogram = new int[numOfBins][numOfBins][numOfBins];
	}
	
	public int numOfColors() {
		return numOfBins * numOfBins * numOfBins;
	}
	
	public void addValues(int[] argb){
		for (int i = 0; i < argb.length; i++) {
			addValue(argb[i]);			
		}
	}
	
	public void addValue (int value){
		int r =  (value >> 16) & 255;
		int g =  (value >>  8) & 255;
		int b =  (value      ) & 255;
		
		double step = 255 / new Double(numOfBins) + 1;
		
		int rIndex = (int) (r / step);
		int gIndex = (int) (g / step);
		int bIndex = (int) (b / step);
		
		histrogram[rIndex][gIndex][bIndex]++;
	}
	
	public float[] toFeatureVector() {
		float[] featureVector = new float [numOfBins * numOfBins * numOfBins];
		int pos = 0;
//		System.out.println("numOfBins: "+numOfBins);
		for (int r = 0; r < numOfBins; r++) {
			for (int g = 0; g < numOfBins; g++) {
				for (int b = 0; b < numOfBins; b++) {
//					System.out.println("pos: " + pos);
					featureVector[pos] = histrogram[r][g][b];
					pos++;
				}
			}
		}		
		return featureVector;
	}
	
	public void fromFeatureVector(float[] featureVector) {
		int pos = 0;		
		for (int r = 0; r < numOfBins; r++) {
			for (int g = 0; g < numOfBins; g++) {
				for (int b = 0; b < numOfBins; b++) {
					histrogram[r][g][b] = (int) featureVector[pos];
					pos++;
				}
			}
		}	
	}
	
	public HistoValue[] getValues(){
		int step = 255 / numOfBins;
		int pos = 0; 
		int max = 0;
		HistoValue[] values = new HistoValue[numOfBins * numOfBins * numOfBins];
		for (int r = 0; r < numOfBins; r++) {
			for (int g = 0; g < numOfBins; g++) {
				for (int b = 0; b < numOfBins; b++) {
					int amount = histrogram[r][g][b];
					if (amount > max) {
						max = amount;
					}
					int rValue = step*r + step/2;
					int gValue = step*g + step/2;
					int bValue = step*b + step/2;
					int rgbValue = (0xFF << 24) | rValue << 16 | gValue << 8 | bValue;
					values[pos] = new HistoValue();
					values[pos].amount = amount;
					values[pos].rgbValue = rgbValue;
					pos++;
				}
			}
		}
		//normalize
		for (int i = 0; i < values.length; i++) {
			values[i].scaledAmount = values[i].amount / new Double(max);
		}
		
		return values;
	}
	
	public class HistoValue{
		public int rgbValue;
		public int amount;
		public double scaledAmount;
	}

	@Override
	public String toString() {
		String s = "";
		HistoValue[] values = this.getValues();
		for (int i = 0; i < values.length; i++) {
			s += "i\t" + "rgb: "+ values[i].rgbValue+"\tamount: "+values[i].amount+"\tscaledAmount: "+values[i].scaledAmount+"\n";
		}
		return s;
	}
	
	

}
