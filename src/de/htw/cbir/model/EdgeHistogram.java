package de.htw.cbir.model;

import java.awt.image.BufferedImage;

public class EdgeHistogram {
	
	private float[] histogram;
	private float[] normalizedHistogram;
	
	public float[] getHistogram() {
		return histogram;
	}

	public float[] getNormalizedHistogram() {
		return normalizedHistogram;
	}

	public EdgeHistogram(Pic pic) {
		
		//System.out.println("Start EdgeHistogram");
		
		BufferedImage bi = pic.getDisplayImage();
		int width  = bi.getWidth();
		int height = bi.getHeight();
		int [] rgbValues = new int[width * height];
		bi.getRGB(0, 0, width, height, rgbValues, 0, width);
		ImageProcessingHelper.convertToBrighness(rgbValues);
		
		float[][] histos = new float[5][width/2 * height/2];
		
		histos[0] = EdgeFilter.filter(rgbValues, width, height, EdgeFilter.VERTICAL);
		histos[1] = EdgeFilter.filter(rgbValues, width, height, EdgeFilter.HORIZONTAL);
		histos[2] = EdgeFilter.filter(rgbValues, width, height, EdgeFilter.DIAGONAL_45);
		histos[3] = EdgeFilter.filter(rgbValues, width, height, EdgeFilter.DIAGONAL_135);
		histos[4] = EdgeFilter.filter(rgbValues, width, height, EdgeFilter.NON_DIRECTIONAL);
		
		histogram = new float[5];
		normalizedHistogram = new float[5];
		
		for (int i = 0; i < histos[0].length; i++) {
			//for each entry
			float maxValue = Float.MIN_VALUE;
			int maxIndex = 0;
			for (int j = 0; j < histogram.length; j++) {
				float current = histos[j][i];
				if (current > maxValue) {
					maxValue = current;
					maxIndex = j;
				}
			}
			if (maxValue >= Settings.edgeThreshold) {
				histogram[maxIndex]++;
			}			
		}
		
		//normalize;
		float maxValue = Float.MIN_VALUE;
		for (int i = 0; i < histogram.length; i++) {
			if (histogram[i] > maxValue) {
				maxValue = histogram[i];
			}
		}
		
		for (int i = 0; i < histogram.length; i++) {
//			normalizedHistogram[i] = histogram[i]/maxValue;
			normalizedHistogram[i] = histogram[i]/(width*height);
		}		
	}
}
