package sensor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ShoeBaseSizeLoader {

	private Map<String, SensorPoints> pointsList;
	
	private List<String> sizeNameList;
	
	public void load(String path) throws FileNotFoundException {
		this.pointsList = new HashMap<String, SensorPoints>();
		this.sizeNameList = new ArrayList<String>();
		Scanner scanner = new Scanner(new File(path));
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().replace(" ", "");
			if (line.startsWith("[") && line.endsWith("]")) {
				String size = line.substring(1, line.length() - 1);
				ShoePoint[] points = new ShoePoint[4];
				for (int i = 0; i < 4; i++) {
					String pointsLine = scanner.nextLine().replace(" ", "");
					int x = Integer.parseInt(pointsLine.split(",")[0]);
					int y = Integer.parseInt(pointsLine.split(",")[1]);
					points[i] = new ShoePoint(x, y);
				}
				pointsList.put(size, new SensorPoints(points));
				sizeNameList.add(size);
			}
		}
	}
	
	public SensorPoints getSensorPoints(String size) {
		return this.pointsList.get(size);
	}
	
	public String[] getSizeNameList() {
		return this.sizeNameList.toArray(new String[]{});
	}
	
}
