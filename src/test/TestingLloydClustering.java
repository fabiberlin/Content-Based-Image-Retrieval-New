package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import de.htw.cbir.model.Cluster;
import de.htw.cbir.model.ColorValue;
import de.htw.cbir.model.HausdorffMetric;
import de.htw.cbir.model.LloydClusters;

public class TestingLloydClustering {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@org.junit.Test
	public void testColorValues() {
		ColorValue value1 = new ColorValue(12, 23, 34);
		ColorValue value2 = new ColorValue(12, 24, 34);
		ColorValue value3 = new ColorValue(12, 23, 34);
		
		assertEquals("Same ColorValues", true, value1.equals(value3));
		assertEquals("Different ColorValues", false, value1.equals(value2));
	}
	
	@org.junit.Test
	public void testCluster() {
		Cluster cluster = new Cluster();
		int[] argbs = {1,2,3,4,5,6,6,6,6};
		cluster.setColorValuesFromArray(argbs);
		//System.out.println(cluster.toString());
		assertEquals("Setting up Cluster from ARGB Array", 6, cluster.getEntries().size());
	}
	
//	@org.junit.Test
//	public void testClusterSplitting() {
//		Cluster cluster = new Cluster();
//		int[] argbs = {1,2,3,4,5,6,6,6,6};
//		cluster.setColorValuesFromArray(argbs);
//		//System.out.println(cluster.toString());
//		assertEquals("Setting up Cluster from ARGB Array", 6, cluster.getEntries().size());
//	}
	
	@org.junit.Test
	public void testLloydClustering() {
		int[] argbs = {1,2,3,4,5,6,6,6,6,3,7,8,9,6,89,34,23,45,83,1,23,44,22,88,54,2,23,98,89,78,67,56,45,34,23,12,55,66,77,88,99,83,26,96};
		LloydClusters lloydClusters = new LloydClusters(argbs);
		lloydClusters.runLloydClustering();
		System.out.println(lloydClusters.toString());
		System.out.println(Arrays.toString(lloydClusters.toFeatureVector()));
	}
	
	@org.junit.Test
	public void testHausdorffDistance() {
		int[] argbs1 = new int[100];
		int[] argbs2 = new int[100];
		Random random = new Random();
		for (int i = 0; i < argbs1.length; i++) {
			argbs1[i]=random.nextInt(100);
			argbs2[i]=random.nextInt(100);
		}
		LloydClusters lloydClusters1 = new LloydClusters(argbs1);
		LloydClusters lloydClusters2 = new LloydClusters(argbs2);
		
		lloydClusters1.runLloydClustering();
		lloydClusters2.runLloydClustering();
		
		float[] fv1 = lloydClusters1.toFeatureVector();
		float[] fv2 = lloydClusters2.toFeatureVector();
		
		HausdorffMetric hausdorffMetric = new HausdorffMetric(fv1, fv2);
		float distance = hausdorffMetric.getDistance();
		System.out.println(distance);
	}
}
