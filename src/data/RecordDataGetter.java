package data;

import sensor.Direction;
import sensor.NikeSensorResource;
import sensor.SensorPoints;
import sensor.ShoePoint;

public class RecordDataGetter extends NikeDataGetter {
	
	private SensorPoints sensorPoints;
	
	public RecordDataGetter(Direction direction) {
		super(direction);
	}

	@Override
	public void dataCallBack(float[] signals) {
		super.dataCallBack(new float[] {
				signals[0],
				signals[1],
				signals[2],
				signals[3],
				signals[4],
				signals[5],
				signals[6],
				signals[7]
		});
	}
	
	public void setSensorPoints(SensorPoints sensorPoints) {
		this.sensorPoints = sensorPoints;
	}
	
	@Override
	public ShoePoint getRealCoP() {
		return NikeSensorResource.getCenterOfPressurePoint(
				this.sensorPoints, 
				getA() + 1, getB() + 1, getC() + 1, getD() + 1);
	}
	
}
