package de.htw.cbir.feature;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import de.htw.cbir.model.DctEngine;
import de.htw.cbir.model.EdgeHistogram;
import de.htw.cbir.model.FullDctEngine;
import de.htw.cbir.model.Histogram;
import de.htw.cbir.model.Histogram.HistoValue;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class DctHistogramFeature extends FeatureFactory
{

	public DctHistogramFeature(Settings settings) {
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
		
		DctEngine dctEngine = new DctEngine(rgbValues, width, height);
		dctEngine.runDct();
		dctEngine.generateHistogram();
		//dctEngine.normalizeHistogram();
		float[] featureVector = dctEngine.getHisto();
		System.out.println(Arrays.toString(featureVector));
		return featureVector;
	}
	
	
	@Override
	public float getDistance(float[] fv1, float[] fv2) {
		return FullDctEngine.squaredDifferences(fv1, fv2);
	}

	@Override
	public String getName() {
		return "DctHistogramFeature";
	}
}
