package pl.edu.agh.mpso.graphs;

import static pl.edu.agh.mpso.Simulation.NUMBER_OF_ITERATIONS;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.jswarm_pso.FitnessFunction;
import pl.edu.agh.mpso.fitness.Griewank;

public class Fields {
	private final static FitnessFunction fitnessFunction = new Griewank();
	private final static int IMAGE_SIZE = 2000;
	private final static double SEARCH_SPACE_SIZE = 5;
	private static int [] colors = null;
	private static double min;
	private static double max;
	
	
	public static void main(String[] args) throws IOException {
		NUMBER_OF_ITERATIONS = 4000;
		
		BufferedImage image = new BufferedImage(IMAGE_SIZE, (int) (IMAGE_SIZE * 1.1), BufferedImage.TYPE_INT_ARGB);
		drawFunction(image);
		drawLegend(image);
		ImageIO.write(image, "PNG", new File("../diagrams/field/" + fitnessFunction.getClass().getSimpleName() + ".png"));
	}
	
	private static void drawLegend(BufferedImage image){
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(Color.white);
		graphics.fillRect(0, IMAGE_SIZE, IMAGE_SIZE, (int) (IMAGE_SIZE * 0.1));
		
		int margin = (int) (IMAGE_SIZE * 0.02);
		int legendWidth = IMAGE_SIZE - 2 * margin;
		int legendHeight = margin * 3 / 2;
		
		for(int i = 0; i < legendWidth; i++){
			int color = getColor(0, legendWidth, i);
			
			for(int j = 0; j < legendHeight; j++){
				image.setRGB(margin + i, IMAGE_SIZE + margin + j, color);
			}
		}
		
		graphics.setPaint(Color.BLACK);
		Font font = graphics.getFont();
		Font newFont = new Font(font.getFontName(), font.getStyle(), font.getSize() * 6);
		graphics.setFont(newFont);
		
		graphics.drawString(String.valueOf(round(min, 1)), margin * 2, IMAGE_SIZE + margin + 2 * legendHeight);
		graphics.drawString(String.valueOf(round(max, 1)), legendWidth - 3 * margin, IMAGE_SIZE + margin + 2 * legendHeight);
		
		System.out.println("Legend drawn");
	}
	
	private static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	
	private static void drawFunction(BufferedImage image){
		min = Double.POSITIVE_INFINITY;
		max = Double.NEGATIVE_INFINITY;
		
		double [] position = new double[2];
		
		//find min and max value
		for(int i = 0; i < IMAGE_SIZE; i++){
			for(int j = 0; j < IMAGE_SIZE; j++){
				position[0] = getCoord(i);
				position[1] = getCoord(j);
				double value = fitnessFunction.evaluate(position);
				
				if(value > max){
					max = value;
				} else if(value < min){
					min = value;
				}
			}
		}
		
		//fill color array
		colors = createColors();

		for(int i = 0; i < IMAGE_SIZE; i++){
			for(int j = 0; j < IMAGE_SIZE; j++){
				position[0] = getCoord(i);
				position[1] = getCoord(j);
				double value = fitnessFunction.evaluate(position);
				int color = getColor(min, max, value);
				
				image.setRGB(i, j, color);
			}
		}
		
		System.out.println("Function drawn");
	}
	
	
	
	private static int [] createColors(){
		int r = 0, g = 0, b = 255;
		int index = 0;
		int [] colorArray = new int[4 * 256];
		
		//blue to cyan
		for(;g <= 255; g++){
			colorArray[index] = new Color(r, g, b).getRGB();
			index++;
		}
		g--;
		//cyan to green
		for(;b >= 0; b--){
			colorArray[index] = new Color(r, g, b).getRGB();
			index++;
		}
		b++;
		//green to yellow
		for(;r <= 255; r++){
			colorArray[index] = new Color(r, g, b).getRGB();
			index++;
		}
		r--;
		//yellow to red
		for(;g >= 0; g--){
			colorArray[index] = new Color(r, g, b).getRGB();
			index++;
		}
		
		return colorArray;
	}
	
	private static double getCoord(int c){
		return SEARCH_SPACE_SIZE * (double)(c - IMAGE_SIZE / 2) / (double)(IMAGE_SIZE / 2); 
	}
	
	private static int getColor(double min, double max, double value){
		int index = (int) (colors.length * (value - min) / (max - min));
		if(index >= colors.length) {
			return colors[colors.length - 1]; 
		}
		return colors[index];
	}
}
