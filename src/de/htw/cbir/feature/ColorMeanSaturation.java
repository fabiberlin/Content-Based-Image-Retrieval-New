package de.htw.cbir.feature;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class ColorMeanSaturation extends FeatureFactory
{

	public ColorMeanSaturation(Settings settings) {
		super(settings);
	}

	@Override
	public BufferedImage getFeatureImage(Pic image) {
		int w = 1;
		int h = 1;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();

		int[] pixels = new int[h * w];

		float[] featureVector = image.getFeatureVector();

		double x = featureVector[0];
		double y = featureVector[1];
		double z = featureVector[2];
		
		int r = (int) (x-z);
		int b = (int) (x-y);
		int g = (int) (3*x -r -b);

		pixels[0] = (0xFF << 24) | (limit255(r) << 16) | (limit255(g) << 8) | limit255(b);

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

		float[] featureVector = new float[3];

		// loop over the block
		int r = 0; int g = 0; int b = 0; int sum = 0;

		for(int y=0; y < height; y++) {
			for (int x=0 ; x<width ; x++) {
				int pos = y*width + x;
				r +=  (rgbValues[pos] >> 16) & 255;
				g +=  (rgbValues[pos] >>  8) & 255;
				b +=  (rgbValues[pos]      ) & 255;
				sum++;
			}	
		}
				
		r /= sum;
		g /= sum;
		b /= sum;				
				
		float lum = (r+g+b)/3; 
		featureVector[0] = lum; 
		featureVector[1] = getSaturationSetting()*(lum-b);
		featureVector[2] = getSaturationSetting()*(lum-r);

		return featureVector;
	}
	
	private float getSaturationSetting() {
		return settings.getSaturation();
	}
	
	private int limit255 (double d){
		if (d <= 0) {
			return 0;
		}
		else if (d>=255) {
			return 255;
		}
		else{
			return (int) d;
		}		
	}
	
	@Override
	public float getDistance(float[] fv1, float[] fv2) {
		return getL1Distance(fv1, fv2);
	}

	@Override
	public String getName() {
		return "ColorMeanSaturation (Saturation: " + settings.getSaturation() + ")";
	}
}
