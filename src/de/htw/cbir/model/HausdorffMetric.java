package de.htw.cbir.model;

//Find max distance from A to B (d(A,B))
//For each Point in A
	//for each Point in B
		//calc distance
	//save the min distance to each point in A to B
// (d(A,B)) = max of the distances in A

//Find max distance form B to A (d(A,B))
	//analog



public class HausdorffMetric {
	ColorValue[] valuesA;
	ColorValue[] valuesB;
	
	public HausdorffMetric (float[] fv1, float[] fv2){
		int numOfValues = fv1.length/4;
		valuesA = new ColorValue[numOfValues];
		valuesB = new ColorValue[numOfValues];
		for (int i = 0; i < valuesA.length; i++) {
			valuesA[i] = new ColorValue();
			valuesA[i].r = (int) fv1[(4*i)+0];
			valuesA[i].g = (int) fv1[(4*i)+1];
			valuesA[i].b = (int) fv1[(4*i)+2];
			valuesA[i].amount = (int) fv1[(4*i)+3]; //TODO - scaled
			valuesB[i] = new ColorValue();
			valuesB[i].r = (int) fv2[(4*i)+0];
			valuesB[i].g = (int) fv2[(4*i)+1];
			valuesB[i].b = (int) fv2[(4*i)+2];
			valuesB[i].amount = (int) fv2[(4*i)+3]; //TODO - scaled
		}
	}
	
	private float getMinDistance(ColorValue colorValueA, ColorValue[] colorValuesB) {
		float minDistance = Float.MAX_VALUE;
		for (int i = 0; i < colorValuesB.length; i++) {
			float currentDistance = colorValueA.distanceTo(colorValuesB[i]);
			if (currentDistance <= minDistance) {
				minDistance = currentDistance;
			}
		}
		return minDistance;
	}
	
	private float getMaxMinDistance(ColorValue[] colorValuesA, ColorValue[] colorValuesB){
		float maxDistance = Float.MIN_VALUE;
		for (int i = 0; i < colorValuesA.length; i++) {
			
			float currentDistance = getMinDistance(colorValuesA[i], colorValuesB);
			if (currentDistance >= maxDistance) {
				maxDistance = currentDistance;
			}
		}
		return maxDistance;
	}
	
	public float getDistance(){
		float d1 = getMaxMinDistance(valuesA, valuesB);
		float d2 = getMaxMinDistance(valuesB, valuesA);
		return Math.max(d1, d2);
	}
	
}