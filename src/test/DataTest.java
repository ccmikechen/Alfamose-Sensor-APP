package test;

import edu.kuasmis.wmc.nike.DataGetterInterface;
import edu.kuasmis.wmc.nike.NikeInsoleMaster;
import sensor.Direction;

public class DataTest {

	public static void main(String[] args) {
		NikeInsoleMaster nim = new NikeInsoleMaster("COM3");
		nim.regist(new DataGetter(Direction.LEFT));
		NikeInsoleMaster nim2 = new NikeInsoleMaster("COM5");
		nim.regist(new DataGetter(Direction.RIGHT));
	}
	
}

class DataGetter implements DataGetterInterface {

	private Direction d;
	
	public DataGetter(Direction direction) {
		this.d = direction;
	}
	
	@Override
	public void dataCallBack(float[] arg0) {
		if (arg0[3] > 10 || arg0[4] > 10 || arg0[5] > 10 || arg0[6] > 10)
			System.out.println(d);
	}
	
}
