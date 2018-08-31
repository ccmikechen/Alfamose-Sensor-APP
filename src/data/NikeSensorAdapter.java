package data;

import java.util.Arrays;

import edu.kuasmis.wmc.nike.DataGetterInterface;
import sensor.Direction;
import sensor.NikeSensor;

public abstract class NikeSensorAdapter 
	implements DataGetterInterface, NikeDataListener {
	
//	class DataChecker {
//		
//		private float[][] signalsTemp;
//		private int pointer = 0;
//		private float[] lastSignals;
//		private float diff = 0;
//		
//		private boolean isChecked = false;
//		private boolean isCorrect = false;
//		
//		public DataChecker(int size) {
//			signalsTemp = new float[size][];
//		}
//		
//		synchronized public void addSignals(float[] signals) {
//			if (!(isChecked)) {
//				signalsTemp[pointer] = signals;
//				lastSignals = getCheckedSignals();
//				pointer++;
//			} else {
//				diff++;
//				isCorrect = checkData(signals);
//				if (isCorrect) {
//					diff = 0;
//					lastSignals = signals;
//				} else {
//					System.out.println(lastSignals[7] + " " + signals[7] + " " + diff);
//				}
//			}
//		}
//		
//		private boolean checkData(float[] signals) {
//			float currentDiff = signals[7] - lastSignals[7];
//			return (currentDiff - diff) <= 2 || (currentDiff - (diff - 4096)) <= 2;
//		}
//		
//		private float[] getCheckedSignals() {
//			if (pointer < (signalsTemp.length - 1)) {
//				return null;
//			}
//			int max = 0;
//			int maxIndex = 0;
//			
//			for (int i = 1; i < signalsTemp.length; i++) {
//				int counts = 0;
//				for (int j = 0; j < i; j++) {
//					int diff = i - j;
//					if (signalsTemp[j][7] == (signalsTemp[i][7] - diff)) {
//						counts++;
//					}
//				}
//				if (counts >= max) {
//					max = counts;
//					maxIndex = i;
//				}
//			}
//			isChecked = true;
//			isCorrect = true;
//			return signalsTemp[maxIndex];
//		}
//		
//	}
	
	protected NikeSensor leftSensor;
	
	protected NikeSensor rightSensor;
	
	private float[] leftSignals = null;
	
	private float[] rightSignals = null;
	
	public NikeSensorAdapter(NikeSensor leftSensor, NikeSensor rightSensor) {
		addSensors(leftSensor, rightSensor);
	}
	
	@Override
	synchronized public void onDataCallBack(float[] signals, Direction direction) {
		if (direction == Direction.LEFT) {
			this.leftSignals = signals;
		} else {
			this.rightSignals = signals;
		}
		if ((this.leftSignals != null) && 
				(this.rightSignals != null)) {
			dataCallBack();
			this.leftSignals = null;
			this.rightSignals = null;
		}
	}
	
	synchronized private void dataCallBack() {
		dataCallBack(new float[] {
				leftSignals[7],
				leftSignals[0],
				leftSignals[1],
				leftSignals[2],
				leftSignals[3],
				leftSignals[4] - 3,
				leftSignals[5],
				leftSignals[6],
				rightSignals[7],
				rightSignals[0],
				rightSignals[1],
				rightSignals[2],
				rightSignals[3],
				rightSignals[4] - 3,
				rightSignals[5],
				rightSignals[6]
		});
	}
	
	public void addSensors(NikeSensor leftSensor, NikeSensor rightSensor) {
		removeSensors();
		
		this.leftSensor = leftSensor;
		leftSensor.addDataListener(this);
		
		this.rightSensor = rightSensor;
		rightSensor.addDataListener(this);
	}
	
	protected void removeSensors() {
		if (this.leftSensor != null) {
			this.leftSensor.removeDataListener(this);
		}
		if (this.rightSensor != null) {
			this.rightSensor.removeDataListener(this);
		}
	}
	
}
