package de.htw.cbir.model;

import java.util.ArrayList;

public class FeatureSignature {

	public ArrayList<SignatureEntry> sigEntries;
	
	public FeatureSignature(int numOfEntries) {
		this.sigEntries = new ArrayList<>();
	}
	
	public void addSigEntry(int r, int g, int b, int amount) {
		SignatureEntry e = new SignatureEntry();
		e.centroid = new ColorValue(r, g, b);
		e.wheight = amount;
		this.sigEntries.add(e);
	}
	
	public SignatureEntry[] asArray (){
		SignatureEntry[] sigEntriesAsArray = new SignatureEntry[this.sigEntries.size()];
		this.sigEntries.toArray(sigEntriesAsArray);
		return sigEntriesAsArray;
	}
	
	public ColorValue[] getCentroids (){
		SignatureEntry[] sigEntriesAsArray = this.asArray();		
		ColorValue[] centroids = new ColorValue[sigEntriesAsArray.length];
		for (int i = 0; i < centroids.length; i++) {
			centroids[i] = sigEntriesAsArray[i].centroid;
		}
		return centroids;
	}
	
	public int[] getWheights (){
		SignatureEntry[] sigEntriesAsArray = this.asArray();		
		int[] wheights = new int[sigEntriesAsArray.length];
		for (int i = 0; i < wheights.length; i++) {
			wheights[i] = sigEntriesAsArray[i].wheight;
		}
		return wheights;
	}
}
