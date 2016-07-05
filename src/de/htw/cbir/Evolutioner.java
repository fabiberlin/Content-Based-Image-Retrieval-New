package de.htw.cbir;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;

import de.htw.cbir.evaluation.CBIREvaluation;
import de.htw.cbir.feature.BaenschFeature;
import de.htw.cbir.feature.FeatureFactory;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class Evolutioner {

	private static String startDirectory = "images";
	
	public static void main(String[] args) throws IOException {
		
		Settings settings = new Settings();
		final File imageDirectory = askDirectory(startDirectory);
		PicManager imageManager = new PicManager();
		imageManager.loadImages(imageDirectory);		
		FeatureFactory featureFactory = new BaenschFeature(settings);
		
		float[] previousValues = Arrays.copyOf(BaenschFeature.featureWheights, BaenschFeature.featureWheights.length);
		float[] adjustedValues = Arrays.copyOf(BaenschFeature.featureWheights, BaenschFeature.featureWheights.length);
		
		float bestMAP = getMAP(imageManager, featureFactory);
		
		for (int i = 0; i < 200; i++) {
			for (int j = 0; j < adjustedValues.length; j++) {
				adjustedValues[j] = createNewValue(previousValues[j]);
			}
			BaenschFeature.featureWheights = adjustedValues;
			System.out.println("Wheights: " + Arrays.toString(BaenschFeature.featureWheights));
			float currentMAP = getMAP(imageManager, featureFactory);
			System.out.println("--> MAP: "+currentMAP);
			if (currentMAP >= bestMAP) {
				// its ok
				System.out.println("Changed Wheights");
				previousValues = adjustedValues;
			} else {
				//do nothing	
				System.out.println("Didnt Changed Wheights");
			}
			System.out.println();
			
		}
		
		float finalMap = getMAP(imageManager, featureFactory);
		System.out.println("Final MAP: "+finalMap+" Wheights: "+Arrays.toString(BaenschFeature.featureWheights));
	
	}
	
	private static float createNewValue(float old){
		return (float) (old + 0.1 * old * (Math.random()*2-1));
	}

	private static float getMAP(PicManager imageManager, FeatureFactory featureFactory) {
		for (Pic image : imageManager.getImages()) {
			image.setFeatureVector(featureFactory.getFeatureVector(image));
			image.setFeatureImage(featureFactory.getFeatureImage(image));			
		}
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
