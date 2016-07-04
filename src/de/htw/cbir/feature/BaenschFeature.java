package de.htw.cbir.feature;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;

import de.htw.cbir.model.DctEngine;
import de.htw.cbir.model.EdgeHistogram;
import de.htw.cbir.model.FullDctEngine;
import de.htw.cbir.model.Histogram;
import de.htw.cbir.model.Histogram.HistoValue;
import de.htw.cbir.model.ImageProcessingHelper;
import de.htw.cbir.model.LloydClusters;
import de.htw.cbir.model.PMHD;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class BaenschFeature extends FeatureFactory
{

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
		LloydClusters lloydClusters = new LloydClusters(rgbValues, settings);
		lloydClusters.runLloydClustering();
		float[] clusterFeature =  lloydClusters.toFeatureVector();
		
		float[] finalFeature = ImageProcessingHelper.concat(dctFeature, edgeFeature);
		finalFeature = ImageProcessingHelper.concat(finalFeature, clusterFeature);
		
		return finalFeature;
	}
	
	
	@Override
	public float getDistance(float[] fv1, float[] fv2) {
		//DCT
		float dctPart = FullDctEngine.compareFeatureVectors(fv1, fv2);
		
		//Mpg7-Edge
		float[] edgeFv1 = new float[5];
		float[] edgeFv2 = new float[5];
		for (int i = 0; i < 5; i++) {
			edgeFv1[i] = fv1[30+i];
			edgeFv2[i] = fv2[30+i];
		}		
		float edgePart = 5* getSquaredChordDistance(edgeFv1, edgeFv2);
		
		//Clustering
		int clusterDim = fv1.length-35;
		float[] clusterFv1 = new float[clusterDim];
		float[] clusterFv2 = new float[clusterDim];
		for (int i = 0; i < clusterDim; i++) {
			clusterFv1[i] = fv1[35+i];
			clusterFv2[i] = fv2[35+i];
		}		
		PMHD pmhd = new PMHD(clusterFv1, clusterFv2);
		float clusterPart = 400 * pmhd.getDistance();	
		
		
		return edgePart + dctPart + clusterPart;
	}

	@Override
	public String getName() {
		return "BaenschFeature";
	}
}
