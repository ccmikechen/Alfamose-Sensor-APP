package data;
import java.util.ArrayList;
import java.util.List;

import edu.kuasmis.wmc.nike.DataGetterInterface;
import sensor.Direction;
import sensor.NikeSensorResource;
import sensor.SensorPoints;
import sensor.ShoePoint;

public abstract class NikeDataGetter implements DataGetterInterface {
	
	private List<NikeDataListener> dataListeners;
	
	protected Direction direction;
	
	private float pa, pb, pc, pd;
	
	private float x, y, z;
	
	private float seq;
	
	public NikeDataGetter(Direction direction) {	
		this.direction = direction;
		this.dataListeners = new ArrayList<NikeDataListener>();
	}
	
	@Override
	public void dataCallBack(float[] signals) {
		x = signals[0];
		y = signals[1];
		z = signals[2];
		pa = signals[3];
		pb = direction == Direction.RIGHT ? signals[5] : signals[4]-3;
		pc = direction == Direction.RIGHT ? signals[4]-3 : signals[5];
		pd = signals[6];
		seq = signals[7];
		for (NikeDataListener listener : dataListeners) {
			listener.onDataCallBack(new float[] {
					x, y, z, pa, pb, pc, pd, seq
			}, direction);
		}
	}


	public Direction getDirection() {
		return this.direction;
	}
	
	synchronized public float getX() {
		return x;
	}
	
	synchronized public float getY() {
		return y;
	}
	
	synchronized public float getZ() {
		return z;
	}
	
	synchronized public float getA() {
		return pa;
	}
	
	synchronized public float getB() {
		return pb;
	}
	
	synchronized public float getC() {
		return pc;
	}
	
	synchronized public float getD() {
		return pd;
	}
	
	synchronized public float getG() {
		return (float) Math.sqrt(x*x + y*y + z*z); 
	}

	synchronized public float getSequenceNumber() {
		return seq;
	}
	
	abstract public ShoePoint getRealCoP();
	
	synchronized public ShoePoint getCenterOfPressurePoint() {
		return NikeSensorResource.getCenterOfPressurePoint(
				direction, pa, pb, pc, pd);
	}
	
	public void addDataListener(NikeDataListener listener) {
		this.dataListeners.add(listener);
	}
	
	public void removeDataListener(NikeDataListener listener) {
		this.dataListeners.remove(listener);
	}
	
	
}
