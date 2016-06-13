package de.htw.cbir.feature;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.htw.cbir.model.ColorValue;
import de.htw.cbir.model.HausdorffMetric;
import de.htw.cbir.model.Histogram;
import de.htw.cbir.model.LloydClusters;
import de.htw.cbir.model.PMHD;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;
import de.htw.cbir.model.Histogram.HistoValue;

public class ColorSignaturePHMD extends FeatureFactory
{

	public ColorSignaturePHMD(Settings settings) {
		super(settings);
	}

	///////////////////////////////////////////
	// visualize the feature data as image
	// Featurevektor abwechselnd mit welche Farbe / wo oft kommt die vor
	// letzeres wird bei nächster Übung gebraucht
	//
	@Override
	public BufferedImage getFeatureImage(Pic image) {
		int w = (int) Math.pow(2, settings.getNumOfNSquareClusters());
		int h = 100;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();

		int[] pixels = new int[h * w];

		float[] featureVector = image.getFeatureVector();
		
		float[] fvClone = featureVector.clone();
		
		float max = Float.MIN_VALUE;
		for (int i = 0; i < fvClone.length; i+=4) {
			float curMax = fvClone[i+3];
			if (curMax >= max) {
				max = curMax;
			}
		}
		for (int i = 0; i < fvClone.length; i+=4) {
			fvClone[i+3] /= max;
		}
	
		for (int i = 0; i < fvClone.length; i+=4) {
			
			int r = (int) fvClone[i+0];
			int g = (int) fvClone[i+1];
			int b = (int) fvClone[i+2];
			
			int argbValue = (0xFF << 24) | (r << 16) | (g << 8) | b;
			float scaledAmount = fvClone[i+3];
			
			int drawHeight = (int) (h*scaledAmount);
			for (int y = h-1; y > h-drawHeight; y--) {
				pixels[y*w+(i/4)]=argbValue;
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

		LloydClusters lloydClusters = new LloydClusters(rgbValues, settings);
		lloydClusters.runLloydClustering();

		return lloydClusters.toFeatureVector();
	}
	
	@Override
	public float getDistance(float[] fv1, float[] fv2) {
		//return getL1Distance(fv1, fv2);
		PMHD pmhd = new PMHD(fv1, fv2);
		return pmhd.getDistance();		
	}

	@Override
	public String getName() {
		return "ColorSignaturePHMD";
	}
}
