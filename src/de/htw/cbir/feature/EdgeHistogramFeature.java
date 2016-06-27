package de.htw.cbir.feature;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.htw.cbir.model.EdgeHistogram;
import de.htw.cbir.model.Histogram;
import de.htw.cbir.model.Histogram.HistoValue;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class EdgeHistogramFeature extends FeatureFactory
{

	public EdgeHistogramFeature(Settings settings) {
		super(settings);
	}

	@Override
	public BufferedImage getFeatureImage(Pic image) {
		int w = 5;
		int h = 100;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();

		int[] pixels = new int[h * w];

		float[] featureVector = image.getFeatureVector();
		
		for (int i = 0; i < featureVector.length; i++) {
			int argbValue = 0;			
			double scaledAmount = featureVector[i];
			int drawHeight = (int) (h*scaledAmount);
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
		EdgeHistogram edgeHistogram = new EdgeHistogram(image);
		return edgeHistogram.getNormalizedHistogram();
	}
	
	
	@Override
	public float getDistance(float[] fv1, float[] fv2) {
		return getL1Distance(fv1, fv2);
	}

	@Override
	public String getName() {
		return "EdgeHistogramFeature";
	}
}
