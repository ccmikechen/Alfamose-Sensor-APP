package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NikeDataLoader {

	private float[][] leftSignals;
	
	private float[][] rightSignals;
	
	private int size;
	
	public NikeDataLoader(File file) throws Exception {
		 load(file);
	}
	
	private void load(File file) throws Exception {
		List<float[]> leftDataList = new ArrayList<float[]>();
		List<float[]> rightDataList = new ArrayList<float[]>();
		Scanner scanner = new Scanner(file);
		scanner.nextLine();
		this.size = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().replace(" ", "");
			String[] data = line.split(",");
			float[] leftSignal = new float[] {
					Float.parseFloat(data[2]),
					Float.parseFloat(data[3]),
					Float.parseFloat(data[4]),
					Float.parseFloat(data[5]),
					Float.parseFloat(data[6]) + 3,
					Float.parseFloat(data[7]),
					Float.parseFloat(data[8]),
					Float.parseFloat(data[1])
			};
			leftDataList.add(leftSignal);
			float[] rightSignal = new float[] {
					Float.parseFloat(data[12]),
					Float.parseFloat(data[13]),
					Float.parseFloat(data[14]),
					Float.parseFloat(data[15]),
					Float.parseFloat(data[16]) + 3,
					Float.parseFloat(data[17]),
					Float.parseFloat(data[18]),
					Float.parseFloat(data[11])
			};
			rightDataList.add(rightSignal);
			this.size++;
		}
		
		leftSignals = leftDataList.toArray(new float[][]{});
		rightSignals = rightDataList.toArray(new float[][]{});
		scanner.close();
	}
	
	public float[][] getLeftSignals() {
		return this.leftSignals;
	}
	
	public float[][] getRightSignals() {
		return this.rightSignals;
	}
	
	public int size() {
		return this.size;
	}
	
}
