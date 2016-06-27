package de.htw.cbir.model;

public abstract class EdgeFilter {
	public static final float[][] VERTICAL = {{ 1, -1}, { 1, -1}};
	public static final float[][] HORIZONTAL = {{ 1,  1}, {-1, -1}};
	public static final float[][] DIAGONAL_45 = {{ (float) Math.sqrt(2),  0}, {0, (float) (-1* Math.sqrt(2))}};
	public static final float[][] DIAGONAL_135 = {{ 0,  (float) Math.sqrt(2)}, { (float) (-1* Math.sqrt(2)), 0}};
	public static final float[][] NON_DIRECTIONAL = {{ 2,  -2}, {-2, 2}};
	
	public static float[] filter (int[] argb, int w, int h, float[][] filter){
		float[] filtered = new float[(w/2) * (h/2)];
		int filteredIndex = 0;
		for (int y = 0; y < h/2; y++) {
			for (int x = 0; x < w/2; x++) {
				float sum = 0;
				for (int fy = 0; fy < filter.length; fy++) {
					for (int fx = 0; fx < filter[0].length; fx++) {
						int posInPic = (y*2 + fy) * w + x*2 + fx;
						float value = argb[posInPic] * filter[fy][fx];
						sum += value;
					}
				}
				filtered[filteredIndex] = sum;
				filteredIndex++;
			}
		}		
		return filtered;
	}
}
