package pl.edu.agh.miss.multidimensional;

import java.util.Arrays;

public class AlgebraUtils {
	
	public static double distance(double [] point){
		double [] point2 = new double[point.length];
		Arrays.fill(point2, 0.0);
		return distance(point, point2);
	}
	
	public static double distance(double [] point1, double [] point2){
		if(point1.length != point2.length) throw new InconsistentDimensionsExeption();
		
		double result = 0.0;
		for(int i = 0; i < point1.length; i++){
			result += Math.pow(point1[i] - point2[i], 2.0);
		}
		result = Math.sqrt(result);
		
		return result;
	}
}
