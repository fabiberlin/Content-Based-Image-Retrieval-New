package de.htw.cbir.model;

public class PMHD {

	FeatureSignature f1;
	FeatureSignature f2;
	
	public PMHD (float[] fv1, float[] fv2) {
		
		int numOfValues = fv1.length/4;		
		this.f1 = new FeatureSignature(numOfValues);
		this.f2 = new FeatureSignature(numOfValues);
		for (int i = 0; i < numOfValues; i++) {
			this.f1.addSigEntry((int) fv1[(4*i)+0], (int) fv1[(4*i)+1], (int) fv1[(4*i)+2], (int) fv1[(4*i)+3]);
			this.f2.addSigEntry((int) fv2[(4*i)+0], (int) fv2[(4*i)+1], (int) fv2[(4*i)+2], (int) fv2[(4*i)+3]);
		}
	}
	
	public float getDistance(){
		return Math.min(hw(f1,f2), hw(f2,f1));
	}

	private float hw(FeatureSignature sq, FeatureSignature so) {
		int[] wq = sq.getWheights();
		int[] wo = so.getWheights();
		ColorValue[] cq = sq.getCentroids();
		ColorValue[] co = so.getCentroids();

		double finalSum = 0;
		for (int i = 0; i < co.length; i++) {
			double wiq = wq[i];
			double minJ = Double.MAX_VALUE;
			for (int j = 0; j < co.length; j++) {
				double distance = co[i].distanceTo(cq[j]);
				double minWheigt = Math.min(wq[i], wo[j]);
				double currentMin = distance/minWheigt;
				if (currentMin < minJ) {
					minJ = currentMin;
				}
			}
			finalSum += wiq * minJ;
		}
		
		double sumWheightsWq = 0;
		for (int i = 0; i < co.length; i++) {
			sumWheightsWq += wq[i];
		}
		
		return (float) (finalSum/sumWheightsWq);
	}

}
