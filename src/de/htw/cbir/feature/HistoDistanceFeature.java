package de.htw.cbir.feature;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.htw.cbir.model.DistanceHistogram;
import de.htw.cbir.model.Histogram;
import de.htw.cbir.model.Histogram.HistoValue;
import de.htw.cbir.model.ImageProcessingHelper;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class HistoDistanceFeature extends FeatureFactory
{

	public HistoDistanceFeature(Settings settings) {
		super(settings);
	}

	///////////////////////////////////////////
	// visualize the feature data as image
	//
	@Override
	public BufferedImage getFeatureImage(Pic image) {
		int w = Settings.numOfHistogramBins*Settings.numOfHistogramBins*Settings.numOfHistogramBins;
		int h = 100;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();

		int[] pixels = new int[h * w];

		float[] featureVector = image.getFeatureVector();
		

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
		
		DistanceHistogram distanceHistogram = new DistanceHistogram(Settings.numOfHistogramBins);
		distanceHistogram.addValues(rgbValues, width, height);
		return distanceHistogram.toFeatureVector();
	}
	
	@Override
	public float getDistance(float[] fv1, float[] fv2) {
		return DistanceHistogram.compareFeatureVectors(fv1, fv2);
	}

	@Override
	public String getName() {
		return "HistoDistanceFeature";
	}
}
