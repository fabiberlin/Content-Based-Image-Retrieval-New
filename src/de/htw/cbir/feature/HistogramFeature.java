package de.htw.cbir.feature;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.htw.cbir.model.Histogram;
import de.htw.cbir.model.Histogram.HistoValue;
import de.htw.cbir.model.ImageProcessingHelper;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class HistogramFeature extends FeatureFactory
{

	public HistogramFeature(Settings settings) {
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
		
		Histogram histogram = new Histogram(Settings.numOfHistogramBins);
		histogram.fromFeatureVector(featureVector);
//		System.out.println(histogram.toString());
		HistoValue[] values = histogram.getValues();
		
		for (int i = 0; i < values.length; i++) {
			int argbValue = values[i].rgbValue;
			double scaledAmount = values[i].scaledAmount;
			int drawHeight = (int) (h*scaledAmount);
//			System.out.println("DrawHeight: "+drawHeight);
			for (int y = h-1; y > h-drawHeight; y--) {
				pixels[y*w+i]=argbValue;
			}			
		}

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
		
		Histogram histogram = new Histogram(Settings.numOfHistogramBins);
		histogram.addValues(rgbValues);
		return histogram.toFeatureVector();
	}
	
	@Override
	public float getDistance(float[] fv1, float[] fv2) {
		return getSquaredChordDistance(fv1, fv2);
	}

	@Override
	public String getName() {
		return "HistogramFeature";
	}
}
