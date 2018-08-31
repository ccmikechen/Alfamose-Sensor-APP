package sensor;

import java.util.ArrayList;
import java.util.List;

import data.NikeDataGetter;
import data.NikeDataListener;
import edu.kuasmis.wmc.nike.NikeInsoleMaster;
import gnu.io.NoSuchPortException;

public class NikeSensor extends NikeDataGetter {
	
	private SensorPoints sensorPoints;
	
	public NikeSensor(Direction direction) {
		super(direction);
	}
	
	@Override
	public void dataCallBack(float[] signals) {
		float[] msSignals = gForceToMS2(signals);
		super.dataCallBack(msSignals);

	}
	
	private float[] gForceToMS2(float[] signals) {
		float[] result = new float[] {
			signals[0] * 9.8f,
			signals[1] * 9.8f,
			signals[2] * 9.8f,
			signals[3],
			signals[4],
			signals[5],
			signals[6],
			signals[7]
		};
		return result;
	}
	
	public void setSensorPoints(SensorPoints sensorPoints) {
		this.sensorPoints = sensorPoints;
	}
	
	public ShoePoint getRealCoP() {
		return NikeSensorResource.getCenterOfPressurePoint(
				this.sensorPoints, 
				getA() + 1, getB() + 1, getC() + 1, getD() + 1);
	}
	

}
