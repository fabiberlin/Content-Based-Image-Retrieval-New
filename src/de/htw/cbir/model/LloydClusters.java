package de.htw.cbir.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class LloydClusters {
	
	ArrayList<Cluster> clusters;
	ArrayList<ColorValue> allColorValues;
	int numOfClusterSplits = 3;
	int numOfIterations = 5;

	public LloydClusters(int[] pic) {
		super();
		this.clusters = new ArrayList<>();
		Cluster cluster = new Cluster();
		cluster.setColorValuesFromArray(pic);
		this.clusters.add(cluster);
		this.allColorValues = (ArrayList<ColorValue>) cluster.getEntries().clone();
		this.calcCenterpoints();
	}
	
	public void runLloydClustering() {
		for (int i = 0; i < this.numOfClusterSplits; i++) {
			//split or insert a new one at Clustercenter
			this.splitClusters();
			
			for (int j = 0; j < this.numOfIterations; j++) {
				//rearrange Clusterentries
				this.rearrangeClustersEntries();
				
				//calc new Centerpoints
				this.calcCenterpoints();
			}
		}
	}
	
	public void calcCenterpoints() {
		for (Cluster cluster : this.clusters) {
			cluster.calculateCenterpoint();
		}
	}
	
	public void rearrangeClustersEntries() {
		this.clearClusterEntries();
		for (ColorValue colorValue : allColorValues) {
			int minDistance = Integer.MAX_VALUE;
			Cluster minCluster = null;
			for (Cluster cluster : this.clusters) {
				int actualDistance = cluster.distanceToCenter(colorValue);
				if (actualDistance < minDistance) {
					minDistance = actualDistance;
					minCluster = cluster;
				}
			}
			if (minCluster != null) {
				minCluster.getEntries().add(colorValue);
			}
		}
	}
	
	public void clearClusterEntries() {
		for (Cluster cluster : this.clusters) {
			cluster.clearEntries();
		}
	}
	
	public void splitClusters() {
		ArrayList<Cluster> toAdd = new ArrayList<>();
		for (Cluster cluster : this.clusters) {
			ColorValue neighborCenter = cluster.calcSplittedNeighborCenter();
			Cluster clusterneighbor = new Cluster();
			clusterneighbor.setCenterPoint(neighborCenter);
			toAdd.add(clusterneighbor);
		}
		// cause adding in a foreach isnt a good idea
		for (Cluster cluster : toAdd) {
			this.clusters.add(cluster);
		}
	}
	
	public float[] toFeatureVector() {
		float[] featureVector = new float[4*(numOfClusterSplits*numOfClusterSplits-1)];

		Cluster[] clusterArr = this.clusters.toArray(new Cluster[clusters.size()]);
		
//		Arrays.sort(clusterArr);

		for (int i = 0; i < clusterArr.length; i++) {
			featureVector[(4*i)+0] = clusterArr[i].centerPoint.r;
			featureVector[(4*i)+1] = clusterArr[i].centerPoint.g;
			featureVector[(4*i)+2] = clusterArr[i].centerPoint.b;
			featureVector[(4*i)+3] = clusterArr[i].getEntries().size();
		}		
		//scale entries, cause of reusage		
		float max = 0;
		for (int i = 3; i < featureVector.length; i+=4) {
			float current = featureVector[i];
			if (current > max) {
				max = current;
			}
		}
		for (int i = 3; i < featureVector.length; i+=4) {
			featureVector[i] /= max;
		}		
		return featureVector;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (Cluster cluster : this.clusters) {
			s += cluster.toString() + "\n";
		}
		return s;
	}
	
}
