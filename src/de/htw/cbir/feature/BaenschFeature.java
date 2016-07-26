package de.htw.cbir.feature;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;

import de.htw.cbir.model.EdgeHistogram;
import de.htw.cbir.model.Histogram;
import de.htw.cbir.model.Histogram.HistoValue;
import de.htw.cbir.model.blockDct.DctEngine;
import de.htw.cbir.model.fullDct.FullDctEngine;
import de.htw.cbir.model.ImageProcessingHelper;
import de.htw.cbir.model.LloydClusters;
import de.htw.cbir.model.PMHD;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class BaenschFeature extends FeatureFactory
{
	public static float[] featureWheights = {0.094359875f, 0.015009696f, 17288.816f, 642.3941f}; //--> 0.62938446
//	public static float[] featureWheights = {0.028136322f, 1.8306704E-4f, 255.6206f, 7.2671905f};

	public BaenschFeature(Settings settings) {
		super(settings);
	}

	@Override
	public BufferedImage getFeatureImage(Pic image) {
		float[] featureVector = image.getFeatureVector();

		int w = featureVector.length;
		int h = 100;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();

		int[] pixels = new int[h * w];
		
//		float max = Float.MIN_VALUE;
//		for (int i = 0; i < featureVector.length; i++) {
//			
//		}
//		
//		for (int i = 0; i < values.length; i++) {
//			int argbValue = values[i].rgbValue;
//			double scaledAmount = values[i].scaledAmount;
//			int drawHeight = (int) (h*scaledAmount);
////			System.out.println("DrawHeight: "+drawHeight);
//			for (int y = h-1; y > h-drawHeight; y--) {
//				pixels[y*w+i]=argbValue;
//			}			
//		}

		BufferedImage bThumb = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		bThumb.setRGB(0, 0, w, h, pixels, 0, w);

		big.drawImage(bThumb, 0, 0, w, h, null);
		big.dispose();
		return bi;
		
	}

	@Override
	public float[] getFeatureVector(Pic image)  
	{
		BufferedImage bi = image.getDisplayImage();
		int width  = bi.getWidth();
		int height = bi.getHeight();
		int [] rgbValues = new int[width * height];
		bi.getRGB(0, 0, width, height, rgbValues, 0, width);
		
		//DCT
		FullDctEngine dctEngine = new FullDctEngine(rgbValues, width, height);
		dctEngine.runDct();
		dctEngine.generateHistogram();
		float[] dctFeature = dctEngine.getHisto();

		//Mpg7-Edge
		EdgeHistogram edgeHistogram = new EdgeHistogram(image);
		float[] edgeFeature = edgeHistogram.getHistogram();
		
		//Clustering
		LloydClusters lloydClusters = new LloydClusters(rgbValues);
		lloydClusters.runLloydClustering();
		float[] clusterFeature =  lloydClusters.toFeatureVector();
		
		//Histogram
		Histogram histogram = new Histogram(Settings.numOfHistogramBins);
		histogram.addValues(rgbValues);
		float[] histoFeature = histogram.toFeatureVector();
		
		float[] finalFeature = ImageProcessingHelper.concat(dctFeature, edgeFeature);
		finalFeature = ImageProcessingHelper.concat(finalFeature, histoFeature);
		finalFeature = ImageProcessingHelper.concat(finalFeature, clusterFeature);
		
		return finalFeature;
	}
	
	
	@Override
	public float getDistance(float[] fv1, float[] fv2) {
		//DCT
		float dctPart = featureWheights[0] * FullDctEngine.compareFeatureVectors(fv1, fv2);
		
		//Mpg7-Edge
		float[] edgeFv1 = new float[5];
		float[] edgeFv2 = new float[5];
		for (int i = 0; i < 5; i++) {
			edgeFv1[i] = fv1[30+i];
			edgeFv2[i] = fv2[30+i];
		}
		float edgePart = featureWheights[1] * FullDctEngine.squaredDifferences(edgeFv1, edgeFv2);
		
		//Histogramm
		float[] histoFv1 = new float[125];
		float[] histoFv2 = new float[125];
		for (int i = 0; i < 125; i++) {
			histoFv1[i] = fv1[35+i];
			histoFv2[i] = fv2[35+i];
		}
		float histogramPart = featureWheights[2] * getSquaredChordDistance(histoFv1, histoFv2);
		
		//Clustering
		int clusterDim = fv1.length-160;
		float[] clusterFv1 = new float[clusterDim];
		float[] clusterFv2 = new float[clusterDim];
		for (int i = 0; i < clusterDim; i++) {
			clusterFv1[i] = fv1[160+i];
			clusterFv2[i] = fv2[160+i];
		}		
		PMHD pmhd = new PMHD(clusterFv1, clusterFv2);
		float clusterPart = featureWheights[3] *  pmhd.getDistance();	
		
		return dctPart + edgePart + histogramPart + clusterPart;
	}

	@Override
	public String getName() {
		return "BaenschFeature";
	}
}
