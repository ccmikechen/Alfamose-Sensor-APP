package sensor;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class NikeSensorResource {
	
	private static Image leftShoeBaseCover;
	
	private static Image rightShoeBaseCover;
	
	private final static int shoeWidth = 30;
	
	private final static int shoeHeight = 90;
	
	private final static ShoePoint[] leftSensorPoints =
			new ShoePoint[] {
					new ShoePoint(22, 14),
					new ShoePoint(9, 37),
					new ShoePoint(23, 31),
					new ShoePoint(21, 78)
				};
	
	private final static ShoePoint[] rightSensorPoints =
			new ShoePoint[] {
					new ShoePoint(7, 14),
					new ShoePoint(20, 37),
					new ShoePoint(6, 31),
					new ShoePoint(8, 78)
				};
	
	public static SensorPoints getSensorPoints(Direction direction) {
		if (direction == Direction.LEFT) {
			return new SensorPoints(shoeWidth, shoeHeight, leftSensorPoints);
		} else {
			return new SensorPoints(shoeWidth, shoeHeight, rightSensorPoints);
		}
	}
	
	synchronized public static ShoePoint getCenterOfPressurePoint(
			Direction direction, 
			float a, float b, float c, float d) {
		return getCenterOfPressurePoint(
				getSensorPoints(direction),
				a, b, c, d);
	}
	
	public static ShoePoint getCenterOfPressurePoint(
			SensorPoints points,
			float a, float b, float c, float d) {
		double xp = points.getPointA().x * a + 
					points.getPointB().x * b + 
					points.getPointC().x * c + 
					points.getPointD().x * d;
		 
		double yp =	points.getPointA().y * a + 
					points.getPointB().y * b + 
					points.getPointC().y * c + 
					points.getPointD().y * d;
		double p = a + b + c + d;
		return new ShoePoint((float) (xp / p), (float) (yp / p));
	}
	
	public static Image getShoeBaseCover(Direction direction) {
		if (direction == Direction.LEFT) {
			return leftShoeBaseCover;
		} else {
			return rightShoeBaseCover;
		}
	}
	
	public static void loadImageResources(Component c) {
		try {
			leftShoeBaseCover = ImageIO.read(new File("res/left.png"));
			rightShoeBaseCover = ImageIO.read(new File("res/right.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					c, 
					"Shoe base cover image not found in res/\n" +
					e.getMessage());
			System.exit(0);
		}
		System.out.println("Loaded shoe base cover images");
	}

	public static byte[][] getShoeBaseMask(Direction direction) {
		return ShoeBaseMask.getMask(direction);
	}
	
}
