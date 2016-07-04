package de.htw.cbir.model;

public class FullDctFeatureRegion {
	public static final int [][] f1 = {
			{0,0}
			};
	public static final int [][] f2 = {
			{0,1},{0,2},{0,3},
			{0,4},{0,5},{1,2},
			{1,3},{1,4},{1,5}
			};
	public static final int [][] f3 = {
			{1,0},{2,0},{3,0},
			{4,0},{5,0},{2,1},
			{3,1},{4,1},{5,1}
			};
	public static final int [][] f4 = {
			{1,1},{2,2},{2,3},
			{3,2},{3,3},{3,4},
			{4,3},{4,4},{4,5},
			{5,4},{5,5}
			};
	
	public static final int[][][] all = 
		{
			{
				{0,0}
			},
			{
				{0,1},{0,2},{0,3},
				{0,4},{0,5},{1,2},
				{1,3},{1,4},{1,5}
			},
			{
				{1,0},{2,0},{3,0},
				{4,0},{5,0},{2,1},
				{3,1},{4,1},{5,1}
			},
			{
				{1,1},{2,2},{2,3},
				{3,2},{3,3},{3,4},
				{4,3},{4,4},{4,5},
				{5,4},{5,5}
			}		
	};
	
	public static final float[] wheights = {
			0.5f, 0.5f, 0.5f, 0.5f
	};
}