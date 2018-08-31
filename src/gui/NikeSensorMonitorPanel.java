package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import data.NikeDataGetter;
import sensor.Direction;
import sensor.NikeSensorResource;
import sensor.SensorPoints;
import sensor.ShoePoint;

public class NikeSensorMonitorPanel extends JPanel {

	private static final int MAX_PRESSURE = 400000;
	
	private NikeDataGetter dataGetter;
	
	private byte[][] shoeBaseMask;
	
	private Image shoeBaseCover;
	
	private int shoeWidth;
	private int shoeHeight;
	
	private int gridWidth;
	private int gridHeight;
	
	private float a, b, c, d;
	
	private ShoePoint A, B, C, D;
	
	private double[][] distanceA;
	private double[][] distanceB;
	private double[][] distanceC;
	private double[][] distanceD;
	
	private List<ShoePoint> copList;

	private boolean isStartRecordCoP;
	
	private Font copStringFont = new Font("Times Romam", Font.PLAIN, 15);
	
	public NikeSensorMonitorPanel(Direction direction) {
		super();
		SensorPoints points = NikeSensorResource.getSensorPoints(direction);
		this.A = points.getPointA();
		this.B = points.getPointB();
		this.C = points.getPointC();
		this.D = points.getPointD();
		this.shoeWidth = points.getWidth();
		this.shoeHeight = points.getHeight();
		this.shoeBaseMask = NikeSensorResource.getShoeBaseMask(direction);
		this.shoeBaseCover = NikeSensorResource.getShoeBaseCover(direction);
		setDistances();
	}
	
	public void setDataGetter(NikeDataGetter dataGetter) {
		this.dataGetter = dataGetter;
	}
	
	public void removeDataGetter() {
		this.dataGetter = null;
	}
	
	private void setDistances() {
        distanceA = getDistanceArray(getSensorAreaList(2));
        distanceB = getDistanceArray(getSensorAreaList(3));
        distanceC = getDistanceArray(getSensorAreaList(4));
        distanceD = getDistanceArray(getSensorAreaList(5));
    }

    private List<ShoePoint> getSensorAreaList(int n) {
        List<ShoePoint> sensorAreaList = new ArrayList<ShoePoint>();
        for (int x = 0; x < shoeWidth; x++)
            for (int y = 0; y < shoeHeight; y++)
                if (shoeBaseMask[y][x] == n)
                    sensorAreaList.add(new ShoePoint(x, y));
        return sensorAreaList;
    }

    private double[][] getDistanceArray(List<ShoePoint> sensorAreaList) {
        double[][] distances = new double[shoeWidth][shoeHeight];
        for (int x = 0; x < shoeWidth; x++) {
            for (int y = 0; y < shoeHeight; y++) {
                distances[x][y] = Double.MAX_VALUE;
                for (ShoePoint sp : sensorAreaList) {
                    if (getPointsDistance(x, y, sp.x, sp.y) < distances[x][y]) {
                        distances[x][y] = getPointsDistance(x, y, sp.x, sp.y);
                    }
                }
            }
        }
        return distances;
    }
	
    private static double getPointsDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		gridWidth = this.getWidth() / shoeWidth + 1;
		gridHeight = this.getHeight() / shoeHeight + 1;
		if (this.dataGetter == null) {
			setPressureData(0, 0, 0, 0);
		} else {
			setPressureData(
					this.dataGetter.getA(),
					this.dataGetter.getB(),
					this.dataGetter.getC(),
					this.dataGetter.getD());	
		}
		drawGridColor(g);
		g.drawImage(this.shoeBaseCover, 0, 0, 
				this.getWidth(), this.getHeight(), null);
		if (this.dataGetter != null) {
			ShoePoint cop = this.dataGetter.getCenterOfPressurePoint();
			recordCoP(cop);
			drawCoPTraceLine(g);
			drawPressureCenterPoint(g, cop);
		}
		repaint();
	}
	
    private void setPressureData(float a, float b, float c, float d) {
        this.a = a + 1;
        this.b = b + 1;
        this.c = c + 1;
        this.d = d + 1;
    }
    
    private void drawGridColor(Graphics g) {
        for (int x = 0; x < shoeWidth; x++) {
            for (int y = 0; y < shoeHeight; y++) {
                if (shoeBaseMask[y][x] > 0) {
                    int pressure = getGridPressure(x, y);
                    drawGrid(g, x, y, pressureToColor(pressure));
                }
            }
        }
    }
    
    private void drawGrid(Graphics g, float x, float y, Color color) {
        g.setColor(color);
        g.fillRect(toRealX(x), toRealY(y), gridWidth, gridHeight);
        //g.fillOval(toRealX(x), toRealY(y), gridWidth, gridHeight);
    }
    
    private int toRealX(float x) {
        return (int) (x * this.getWidth() / shoeWidth);
    }

    private int toRealY(float y) {
        return (int) (y * this.getHeight() / shoeHeight);
    }
    
	private int getGridPressure(int x, int y) {
		int pa = (int) (a / (distanceA[x][y] / 2 + 1));
		int pb = (int) (b / (distanceB[x][y] / 2 + 1));
		int pc = (int) (c / (distanceC[x][y] / 2 + 1));
		int pd = (int) (d / (distanceD[x][y] / 2 + 1));
		double sum = Math.min(pa + pb + pc + pd, MAX_PRESSURE);
		return (int) (Math.sin(sum / MAX_PRESSURE * Math.PI / 2) * MAX_PRESSURE);
	}

    private Color pressureToColor(double pressure) {
        pressure = Math.min(pressure, MAX_PRESSURE);
        int c = Math.max(0, (int) (pressure * 450 / MAX_PRESSURE));

        if (c < 20)
            return new Color(60, 60, 60);
        else if (c < 215)
            return new Color(c + 40, 60, 60);
        else
            return new Color(255, c - 195, 60);
    }
    
    private void drawPressureCenterPoint(Graphics g, ShoePoint cop) {
    	if (this.dataGetter == null) {
    		return;
    	}
        int pointSize = this.getHeight() / 80;
        int pointWidth = 2;
        g.setColor(Color.WHITE);
        if (cop.x > 0 && cop.y > 0) {
            int x = toRealX(cop.x);
            int y = toRealY(cop.y);
            g.fillRect(x - pointSize, y - pointWidth, pointSize * 2, pointWidth * 2);
            g.fillRect(x - pointWidth, y - pointSize, pointWidth * 2, pointSize * 2);
            
            ShoePoint realCoP = this.dataGetter.getRealCoP();
            String copString = String.format("%.1f, %.1f", realCoP.x, realCoP.y);
            g.setColor(Color.CYAN);
            g.setFont(copStringFont);
            g.drawString(copString, x + pointSize * 2, y + pointSize * 2);
        }

    }
    
    public void startRecordCoP() {
    	this.copList = new ArrayList<ShoePoint>();
    	this.isStartRecordCoP = true;
    }
    
    public void stopRecordCoP() {
    	this.copList = null;
    	this.isStartRecordCoP = false;
    }
    
    public void restartRecordCoP() {
    	stopRecordCoP();
    	startRecordCoP();
    }
    
    private void recordCoP(ShoePoint cop) {
    	if (isStartRecordCoP && isNextCoP(cop)) {
    		this.copList.add(cop);
    	}
    }
    
    private boolean isNextCoP(ShoePoint cop) {
    	if (Float.isNaN(cop.x) || Float.isNaN(cop.y)) {
    		return false;
    	}
    	if ((cop.x == 0) && (cop.y == 0)) {
    		return false;
    	}
    	if (this.copList.size() > 1) {
    		ShoePoint lastCoP = this.copList.get(this.copList.size() - 1);
    		if (cop.equals(lastCoP)) {
    			return false;
    		}
		}
    	return true;
    }
    
    private void drawCoPTraceLine(Graphics g) {
    	if ((copList == null) || (copList.size() < 2)) {
    		return;
    	}
    	g.setColor(Color.pink);
    	for (int i = 1; i < copList.size(); i++) {
    		int ax = toRealX((int) (copList.get(i - 1).x));
    		int ay = toRealY((int) (copList.get(i - 1).y));
    		int bx = toRealX((int) (copList.get(i).x));
    		int by = toRealY((int) (copList.get(i).y));
    		Graphics2D g2d = (Graphics2D) g;
    		g2d.setStroke(new BasicStroke(3));
    		g2d.draw(new Line2D.Float(ax, ay, bx, by));
    	}
    }
    
}
