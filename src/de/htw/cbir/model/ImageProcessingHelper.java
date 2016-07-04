package de.htw.cbir.model;

import java.util.Arrays;
import java.util.Locale;

/**
 * This class contains some useful methods for image processing
 * 
 * @author Fabian Bänsch <fabian.baensch@student.htw-berlin.de>
 * @version 1.2
 * @since 2015-04-27
 */
public abstract class ImageProcessingHelper {

	/**
	 * @param pixels ColorPixels from argument will be greyscale afterwards
	 */
	public static void convertToGreyScale(int[] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			int newValue = (((pixels[i] >> 16) & 0xff)
					+ ((pixels[i] >> 8) & 0xff) + (pixels[i] & 0xff)) / 3;
			pixels[i] = ((0xFF << 24) | ((newValue) << 16) | ((newValue) << 8) | (newValue));
		}
	}

	public static void convertToBrighness(int[] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			int newValue = (((pixels[i] >> 16) & 0xff)
					+ ((pixels[i] >> 8) & 0xff) + (pixels[i] & 0xff)) / 3;
			pixels[i] = newValue;
		}
	}
	
	/**
	 * @param pixels Pixelarray in Color (no changes in argument)
	 * @return a new pixelarray in Greyscale
	 */
	public static int[] getAsGreyScale(int[] pixels) {
		int[] returnpixels = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			int newValue = (((pixels[i] >> 16) & 0xff)
					+ ((pixels[i] >> 8) & 0xff) + (pixels[i] & 0xff)) / 3;
			returnpixels[i] = ((0xFF << 24) | ((newValue) << 16)
					| ((newValue) << 8) | (newValue));
		}
		return returnpixels;
	}
	
	public static void convertToBitonal(int[] pixels, int threshold) {
		for (int i = 0; i < pixels.length; i++) {
			int newValue = (((pixels[i] >> 16) & 0xff)
					+ ((pixels[i] >> 8) & 0xff) + (pixels[i] & 0xff)) / 3;
			if(newValue<threshold){
				newValue=0;
			}
			else{
				newValue=255;
			}
			pixels[i] = ((0xFF << 24) | ((newValue) << 16) | ((newValue) << 8) | (newValue));
		}
	}
	
	public static int[] getAsBitonal(int[] pixels, int threshold) {
		int[] returnpixels = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			int newValue = (((pixels[i] >> 16) & 0xff)
					+ ((pixels[i] >> 8) & 0xff) + (pixels[i] & 0xff)) / 3;
			if(newValue<threshold){
				newValue=0;
			}
			else{
				newValue=255;
			}
			returnpixels[i] = ((0xFF << 24) | ((newValue) << 16)
					| ((newValue) << 8) | (newValue));
		}
		return returnpixels;
	}
	
	public static int[] getHorizontalDPCM(int[] pixels) {
		int[] errorArray = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			int prediction;
			if (i != 0) {
				prediction = pixels[i - 1] & 0xff;
			} else {
				prediction = 128;
			}
			int error = ((pixels[i]) & 0xff) - prediction;
			errorArray[i] = error;
		}
		return errorArray;
	}

	/**
	 * @param pix The PixelArray (only BW)
	 * @return The Entropy of the PixelArray
	 */
	public static double getEntropyBW(int[] pix) {
		double entropy = 0;
		int[] histo = new int[256];
		for (int i = 0; i < pix.length; i++) {
			histo[pix[i] & 0xff]++;
		}
		for (int i = 0; i < histo.length; i++) {
			double p = histo[i] / new Double(pix.length);
			if (p > 0) {
				entropy += (-1) * p * log2(p);
			}
		}
		return entropy;
	}

	/**
	 * @param pixels PixelArray
	 * @return The Histogram as a Frequency Table (Sum > 1)
	 */
	public static int[] getHistogrammFrequency(int[] pixels) {
		int[] histo = new int[256];
		for (int i = 0; i < pixels.length; i++) {
			histo[pixels[i] & 0xff]++;
		}
		return histo;
	}

	/**
	 * @param pixels PixelArray
	 * @return The Histogram as a Odds Table (Sum = 1)
	 */
	public static double[] getHistogrammOdds(int[] pixels) {
		double[] histo = new double[256];
		for (int i = 0; i < pixels.length; i++) {
			histo[pixels[i] & 0xff]++;
		}
		for (int i = 0; i < histo.length; i++) {
			histo[i] /= pixels.length;
		}
		return histo;
	}

	/**
	 * @param errorArray ErrorArray (from -255 to 255 values)
	 * @return Sorted Histogramm of Input as a Odds Table ([0] = 0; [1] = -1; [2] = 1; [3] = -2; [4] = 2 )
	 */
	public static double[] getHistogrammOddsFromErrorArray(int[] errorArray) {
		double[] histo = new double[512];
		for (int i = 0; i < errorArray.length; i++) {
			int value = errorArray[i];
			int index;
			if(value<0){
				index = (-1)*(value*2+1);
			}
			else{
				index = value * 2;
			}
			histo[index]++;
		}
		for (int i = 0; i < histo.length; i++) {
			histo[i] /= errorArray.length;
		}
		return histo;
	}

	/**
	 * @param a pixels from A
	 * @param b pixels from B
	 * @return the MSE of GreyscalePixels
	 */
	public static double getMeanSquareErrorBW(int[] a, int[] b) {
		double msa = 0;
		for (int i = 0; i < a.length; i++) {
			int valueA = a[i] & 0xff;
			int valueB = b[i] & 0xff;
			double squareError = (valueA - valueB) * (valueA - valueB);
			msa += squareError;
		}
		return msa / a.length;
	}

	/**
	 * @param a pixels from A
	 * @param b pixels from B
	 * @return the MSE of ColorPixels
	 */
	public static double getMeanSquareErrorColor(int[] a, int[] b) {
		double bMSE = 0;
		double gMSE = 0;
		double rMSE = 0;
		for (int i = 0; i < a.length; i++) {
			int bA = a[i] & 0xff;
			int bB = b[i] & 0xff;
			int gA = a[i] >> 8 & 0xff;
		int gB = b[i] >> 8 & 0xff;
		int rA = a[i] >> 16 & 0xff;
		int rB = b[i] >> 16 & 0xff;
		bMSE += (bA - bB) * (bA - bB);
		gMSE += (gA - gB) * (gA - gB);
		rMSE += (rA - rB) * (rA - rB);
		}
		return (bMSE / a.length + gMSE / a.length + rMSE / a.length) / 3;
	}

	/**
	 * @param value
	 * @return The Value limited to 0 & 255 (including)
	 */
	public static int limit0to255(int value) {
		if (value < 0)
			return 0;
		if (value > 255)
			return 255;
		return value;
	}

	/**
	 * @param x
	 * @return the log2(param)
	 */
	public static double log2(double x) {
		return Math.log(x) / Math.log(2.0);
	}

	/**
	 * @param errorArray
	 * @return an Array with Values from 0 to 255, where each value is 128 higher
	 */
	public static int[] plus128Offset(int[] errorArray) {
		int[] toReturnasPic = new int[errorArray.length];
		for (int i = 0; i < errorArray.length; i++) {
			int argb = limit0to255((errorArray[i]) + 128);
			toReturnasPic[i] = ((0xFF << 24) | ((argb) << 16) | ((argb) << 8) | (argb));
		}
		return toReturnasPic;
	}
	
	/**
	 * @param d the double value
	 * @return doubleValue with only to numerics behind dot, as String
	 */
	public static String doubleToString(double d){
		return String.format(Locale.US, "%.2f", d);
	}
	
	/**
	 * from https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static float[] concat(float[] first, float[] second) {
		float[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	public static float[] normalize (float[] input){
		float[] result = new float[input.length];
		float dist = 0;
		for (int i = 0; i < input.length; i++) {
			dist += input[i]*input[i];
		}
		dist = (float) Math.sqrt(dist);
		for (int i = 0; i < input.length; i++) {
			result[i] = input[i]/dist;
		}
		return result;
	}

	
}