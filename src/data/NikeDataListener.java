package data;

import sensor.Direction;

public interface NikeDataListener {

	public void onDataCallBack(float[] signals, Direction direction);
	
}
