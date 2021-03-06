package de.htw.cbir;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import javax.swing.JFileChooser;

import de.htw.cbir.evaluation.CBIREvaluation;
import de.htw.cbir.feature.BaenschFeature;
import de.htw.cbir.feature.BlockDctHistogramFeature;
import de.htw.cbir.feature.ColorSignaturePHMD;
import de.htw.cbir.feature.DctHistogramFeature;
import de.htw.cbir.feature.EdgeHistogramFeature;
import de.htw.cbir.feature.FeatureFactory;
import de.htw.cbir.feature.HistoDistanceFeature;
import de.htw.cbir.feature.HistogramFeature;
import de.htw.cbir.model.Histogram;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;
import de.htw.cbir.model.fullDct.FullDctFeatureRegion;

public class FeatureTester {

	private static String startDirectory = "images";
	
	public static void main(String[] args) throws IOException {
		
		//testLlyodPHMD();
		//testEdge();
		//testDctWheights(); //Final MAP: 0.32519796 Wheights: [0.19639395, 0.3846143, 0.33273768, 0.31192625]
		testHistogram();
		//testDistanceHistogram();
		//testBlockDct();
		
	}
	
	private static void testDistanceHistogram() throws IOException {
		Settings settings = new Settings();
		final File imageDirectory = askDirectory(startDirectory);
		PicManager imageManager = new PicManager();
		imageManager.loadImages(imageDirectory);	
		/////////////////////////////////////////////////////////////////
		/**
		 	MAP: 0.047440797 (numOfHistogramBins: 1)
			MAP: 0.45241612 (numOfHistogramBins: 2)
			MAP: 0.5461781 (numOfHistogramBins: 3)
			MAP: 0.55250484 (numOfHistogramBins: 4)
			MAP: 0.5535956 (numOfHistogramBins: 5)
			MAP: 0.5457065 (numOfHistogramBins: 6)
			MAP: 0.5317276 (numOfHistogramBins: 7)
			MAP: 0.51839125 (numOfHistogramBins: 8)
			MAP: 0.51433975 (numOfHistogramBins: 9)
			MAP: 0.5075679 (numOfHistogramBins: 10)
		 */
		FeatureFactory featureFactory = new HistoDistanceFeature(settings);
		float maxMap = 0;
		String maxS = "";
		for (int i = 1; i < 16; i++) {

			Settings.numOfHistogramBins = i;

			for (Pic image : imageManager.getImages()) {
				image.setFeatureVector(featureFactory.getFeatureVector(image));
				image.setFeatureImage(featureFactory.getFeatureImage(image));			
			}
			
			float finalMap = getMAP(imageManager, featureFactory);
			System.out.println("MAP: "+finalMap+" (numOfHistogramBins: "+Settings.numOfHistogramBins+")");
			if(finalMap >= maxMap){
				maxMap = finalMap;
				maxS = "Best MAP: "+finalMap+" (numOfHistogramBins: "+Settings.numOfHistogramBins+")";
			}
			
		}
		System.out.println(maxS);
		
	}

	private static void testBlockDct() throws IOException{
		Settings settings = new Settings();
		final File imageDirectory = askDirectory(startDirectory);
		PicManager imageManager = new PicManager();
		imageManager.loadImages(imageDirectory);	
		/////////////////////////////////////////////////////////////////
		FeatureFactory featureFactory = new BlockDctHistogramFeature(settings);
		float maxMap = 0;
		String maxS = "";
		for (int i = 0; i < 1; i++) {

			Settings.dctThreshold = i/new Float(1);

			for (Pic image : imageManager.getImages()) {
				image.setFeatureVector(featureFactory.getFeatureVector(image));
				image.setFeatureImage(featureFactory.getFeatureImage(image));			
			}
			
			float finalMap = getMAP(imageManager, featureFactory);
			System.out.println("MAP: "+finalMap+" (dctThreshold: "+Settings.dctThreshold+")");
			if(finalMap >= maxMap){
				maxMap = finalMap;
				maxS = "Best MAP: "+finalMap+" (dctThreshold: "+Settings.dctThreshold+")";
			}
			
		}
		System.out.println(maxS);
		//Best MAP: 0.21432452 (dctThreshold: 1.97)
	}
	
	private static void testHistogram() throws IOException{
		Settings settings = new Settings();
		final File imageDirectory = askDirectory(startDirectory);
		PicManager imageManager = new PicManager();
		imageManager.loadImages(imageDirectory);	
		/////////////////////////////////////////////////////////////////
		/**
		 	MAP: 0.047440797 (numOfHistogramBins: 1)
			MAP: 0.47093093 (numOfHistogramBins: 2)
			MAP: 0.57562184 (numOfHistogramBins: 3)
			MAP: 0.58866304 (numOfHistogramBins: 4)
			MAP: 0.5947595 (numOfHistogramBins: 5)
			MAP: 0.5871637 (numOfHistogramBins: 6)
			MAP: 0.577903 (numOfHistogramBins: 7)
			MAP: 0.5679039 (numOfHistogramBins: 8)
			MAP: 0.56190693 (numOfHistogramBins: 9)
			MAP: 0.5581122 (numOfHistogramBins: 10)
		 */
		FeatureFactory featureFactory = new HistogramFeature(settings);
		float maxMap = 0;
		String maxS = "";
		for (int i = 1; i < 16; i++) {

			Settings.numOfHistogramBins = i;

			for (Pic image : imageManager.getImages()) {
				image.setFeatureVector(featureFactory.getFeatureVector(image));
				image.setFeatureImage(featureFactory.getFeatureImage(image));			
			}
			
			float finalMap = getMAP(imageManager, featureFactory);
			System.out.println("MAP: "+finalMap+" (numOfHistogramBins: "+Settings.numOfHistogramBins+")");
			if(finalMap >= maxMap){
				maxMap = finalMap;
				maxS = "Best MAP: "+finalMap+" (numOfHistogramBins: "+Settings.numOfHistogramBins+")";
			}
			
		}
		System.out.println(maxS);
	}

	private static void testEdge() throws IOException{
		Settings settings = new Settings();
		final File imageDirectory = askDirectory(startDirectory);
		PicManager imageManager = new PicManager();
		imageManager.loadImages(imageDirectory);	
		/////////////////////////////////////////////////////////////////
		FeatureFactory featureFactory = new EdgeHistogramFeature(settings);
		float maxMap = 0;
		String maxS = "";
		for (int i = 0; i < 100; i++) {

			Settings.edgeThreshold = i/new Float(1);

			for (Pic image : imageManager.getImages()) {
				image.setFeatureVector(featureFactory.getFeatureVector(image));
				image.setFeatureImage(featureFactory.getFeatureImage(image));			
			}
			
			float finalMap = getMAP(imageManager, featureFactory);
			System.out.println("MAP: "+finalMap+" (edgeThreshold: "+Settings.edgeThreshold+")");
			if(finalMap >= maxMap){
				maxMap = finalMap;
				maxS = "Best MAP: "+finalMap+" (edgeThreshold: "+Settings.edgeThreshold+")";
			}
			
		}
		System.out.println(maxS);
		
		
	}

	private static void testLlyodPHMD() throws IOException {
		Settings settings = new Settings();
		final File imageDirectory = askDirectory(startDirectory);
		PicManager imageManager = new PicManager();
		imageManager.loadImages(imageDirectory);	
		/////////////////////////////////////////////////////////////////
		FeatureFactory featureFactory = new ColorSignaturePHMD(settings);
		//Final MAP: 0.44783583 (nClusters: 3 | iterations: 9) --> weitermachen mit 0 | 1
		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 10; j++) {
				Settings.numOfNSquareClusters = i;
				Settings.numOfClusterIterations = j;

				for (Pic image : imageManager.getImages()) {
					image.setFeatureVector(featureFactory.getFeatureVector(image));
					image.setFeatureImage(featureFactory.getFeatureImage(image));			
				}
				
				float finalMap = getMAP(imageManager, featureFactory);
				System.out.println("Final MAP: "+finalMap+" (nClusters: "+i+" | iterations: "+j+")");
			}
		}
	}
	
	private static float createNewValue(float old){
		return (float) (old + 0.05 * old * (Math.random()*2-1));
	}

	private static float getMAP(PicManager imageManager, FeatureFactory featureFactory) {
		
		Pic[] allImages = imageManager.getImages();
		CBIREvaluation eval = new CBIREvaluation(featureFactory, allImages);
		float map = eval.test(allImages, false, "Testing all images");
		
		//System.out.printf("MAP: "+  map);
		return map;
	}
	
	/**
	 * Frage den Anwender nach einem Verzeichnis 
	 * 
	 * @param startDirectory
	 * @return
	 */
	public static File askDirectory(final String dir) {
		final JFileChooser fc = new JFileChooser(dir);

		// Nur komplette Ordner koennen ausgewaehlt werden
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final int returnVal = fc.showOpenDialog(null);

		if (returnVal != JFileChooser.APPROVE_OPTION)
			System.exit(-1);

		// Liest alle Dateien des Ordners und schreibt sie in ein Array
		return fc.getSelectedFile();
	}

}
