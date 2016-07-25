package de.htw.cbir.model;

public class DistanceHistogram {
	
	int numOfBins;
	int[][][] histrogram; // [R][G][B]
	int numOfPixel;
	Bin[][][] histoBins;
	
	public DistanceHistogram(int numOfBins) {
		super();
		this.numOfBins = numOfBins;
		histrogram = new int[numOfBins][numOfBins][numOfBins];
		histoBins = new Bin[numOfBins][numOfBins][numOfBins];
		
		float stepSize = 255 / new Float(numOfBins);
		
		for (int r = 0; r < numOfBins; r++) {
			for (int g = 0; g < numOfBins; g++) {
				for (int b = 0; b < numOfBins; b++) {
					histoBins[r][g][b] = new Bin((int)(r*stepSize + stepSize/2), (int)(g*stepSize + stepSize/2), (int)(b*stepSize + stepSize/2));
				}
			}
		}	

	}
	
	public int numOfColors() {
		return numOfBins * numOfBins * numOfBins;
	}
	
	public void addValues(int[] argb, int width, int height){
		this.numOfPixel = argb.length;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pos = y * width + x;
				ColorValue colorValue = new ColorValue(argb[pos]);
				colorValue.x = x / new Float(width - 1);
				colorValue.y = y / new Float(height - 1);
				addValue(colorValue);
			}			
		}
		
		for (int r = 0; r < numOfBins; r++) {
			for (int g = 0; g < numOfBins; g++) {
				for (int b = 0; b < numOfBins; b++) {
					histoBins[r][g][b].calcCenterPoint();
				}
			}
		}	
		
		//System.out.println("Done");
	}

	public void addValue (ColorValue value){
		int r =  value.r;
		int g =  value.g;
		int b =  value.b;
		
		double step = 255 / new Double(numOfBins) + 1;
		
		int rIndex = (int) (r / step);
		int gIndex = (int) (g / step);
		int bIndex = (int) (b / step);
		
		histrogram[rIndex][gIndex][bIndex]++;
		histoBins[rIndex][gIndex][bIndex].addToBin(value);
	}
	
	public float[] toFeatureVector() {
		float[] featureVector = new float [3*(numOfBins * numOfBins * numOfBins)];
		int pos = 0;
//		System.out.println("numOfBins: "+numOfBins);
		for (int r = 0; r < numOfBins; r++) {
			for (int g = 0; g < numOfBins; g++) {
				for (int b = 0; b < numOfBins; b++) {
//					System.out.println("pos: " + pos);
					featureVector[pos] = histrogram[r][g][b] / new Float(numOfPixel);
					featureVector[pos+1] = histoBins[r][g][b].getRepresant().x;
					featureVector[pos+2] = histoBins[r][g][b].getRepresant().y;
					pos+=3;
				}
			}
		}		
		return featureVector;
	}
	
	
	
	public static float compareFeatureVectors(float[] fv1, float[] fv2){
		float dist = 0;
		for (int i = 0; i < fv1.length; i+=3) {
			double buff = Math.sqrt(fv1[i]) - Math.sqrt(fv2[i]);
			float distanceFromCenterPoints = (float) Math.sqrt((fv1[i+1]-fv2[i+1])*(fv1[i+1]-fv2[i+1])+(fv1[i+2]-fv2[i+2])*(fv1[i+2]-fv2[i+2]));
			distanceFromCenterPoints = (float) ((-1/Math.sqrt(2))*distanceFromCenterPoints+1);
			dist += (distanceFromCenterPoints+1)*(buff*buff);
		}
		return dist;
	}
}
