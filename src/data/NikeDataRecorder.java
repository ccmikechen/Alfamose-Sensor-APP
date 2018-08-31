package data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.kuasmis.wmc.nike.DataGetterInterface;
import sensor.Direction;
import sensor.NikeSensor;
import sensor.ShoePoint;

public class NikeDataRecorder extends NikeSensorAdapter {
	
	private PrintWriter dataPrinter;
	
	private PrintWriter leftRowDataPrinter;
	
	private PrintWriter rightRowDataPrinter;
	
	private boolean isStarted = false;
	
	private String recordPath;
	
	
	
	public NikeDataRecorder(NikeSensor leftSensor, NikeSensor rightSensor) {
		super(leftSensor, rightSensor);
	}
	
	@Override
	public void onDataCallBack(float[] signals, Direction direction) {
		super.onDataCallBack(signals, direction);
		String line = Arrays.toString(signals);
		
		if (isStarted) {
			if (direction == Direction.LEFT) {
				leftRowDataPrinter.println(line.substring(1, line.length() - 1));
			} else {
				rightRowDataPrinter.println(line.substring(1, line.length() - 1));
			}
		}
	}
	
	@Override
	public void dataCallBack(float[] signals) {
		if (isStarted) {
			ShoePoint leftCoP = this.leftSensor.getRealCoP();
			ShoePoint rightCoP = this.rightSensor.getRealCoP();
			dataPrinter.print(System.currentTimeMillis() + ",");
			dataPrinter.printf("%f,%f,%f,%f,%f,%f,%f,%f,%.1f,%.1f," +
						   "%f,%f,%f,%f,%f,%f,%f,%f,%.1f,%.1f\n",
						   signals[0], signals[1], signals[2], signals[3],
						   signals[4], signals[5], signals[6], signals[7],
						   leftCoP.x / 10, leftCoP.y / 10,
						   signals[8], signals[9], signals[10], signals[11],
						   signals[12], signals[13], signals[14], signals[15],
						   rightCoP.x / 10, rightCoP.y / 10);
		}
	}
	
	public void start(NikeRecordInfo recordInfo) throws IOException {
		File recordDirectory = recordInfo.getDirectory();
		if (!(recordDirectory.exists())) {
			recordDirectory.mkdir();
		}
		File testerDirectory = recordInfo.getTesterDirectory();
		if (!(testerDirectory.exists())) {
			testerDirectory.mkdir();
		}
		File recordFile = recordInfo.getRecordFile();
		start(recordFile);
	}
	
	public void start(File file) throws IOException {
		file.createNewFile();
		recordPath = file.getAbsolutePath();
		dataPrinter = new PrintWriter(new FileWriter(file), true);
		dataPrinter.println("Time,SEQ,LX,LY,LZ,LA,LB,LC,LD,LCX,LCY,SEQ,RX,RY,RZ,RA,RB,RC,RD,RCX,RCY");
		
		File rowDataFolder = new File(file.getParent() + "\\row_data");
		if (!(rowDataFolder.exists())) {
			rowDataFolder.mkdir();
		}
		File leftRowDataFile = 
				new File(rowDataFolder + "\\left_" + file.getName());
		leftRowDataFile.createNewFile();
		leftRowDataPrinter = new PrintWriter(new FileWriter(leftRowDataFile));
		
		File rightRowDataFile = 
				new File(rowDataFolder + "\\right_" + file.getName());
		rightRowDataFile.createNewFile();
		rightRowDataPrinter = new PrintWriter(new FileWriter(rightRowDataFile));
		
		isStarted = true;
	}
	
	public void stop() {
		isStarted = false;
		dataPrinter.close();
		leftRowDataPrinter.close();
		rightRowDataPrinter.close();
		removeSensors();
	}
	
	public String getRecordPath() {
		return this.recordPath;
	}
	
}
