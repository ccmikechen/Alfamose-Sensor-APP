package sensor;

import edu.kuasmis.wmc.nike.DataGetterInterface;
import edu.kuasmis.wmc.nike.NikeInsoleMaster;
import edu.kuasmis.wmc.nike.TwoDataGetterInterface;
import gnu.io.NoSuchPortException;

public class NikeSensorConnector 
		implements TwoDataGetterInterface{

	private boolean isConnected = false;
	
	private NikeInsoleMaster insoleMaster;
	
	private DataGetterInterface leftDataGetter;
	private DataGetterInterface rightDataGetter;
	
	public void setLeftDataGetter(DataGetterInterface dataGetter) {
		this.leftDataGetter = dataGetter;
	}
	
	public void setRightDataGetter(DataGetterInterface dataGetter) {
		this.rightDataGetter = dataGetter;
	}
	
	public void swapDataGetter() {
		DataGetterInterface dataGetterTemp = this.leftDataGetter;
		this.leftDataGetter = this.rightDataGetter;
		this.rightDataGetter = dataGetterTemp;
	}
	
	public void connect(String comPort) throws NoSuchPortException {
		this.insoleMaster = new NikeInsoleMaster();
		System.out.println("Start connection");
		if (!this.insoleMaster.openComPort(comPort)) {
			System.out.println("PortInUse");
		}
		System.out.println("End connection");
		this.insoleMaster.regist2(this);
		this.isConnected = true;
	}
	
	public void disconnect() {
		this.insoleMaster.teminate();
		this.insoleMaster.closeComPort();
		this.isConnected = false;
	}
	
	public boolean isConnected() {
		return this.isConnected;
	}

	@Override
	public void dataCallBack(float[] leftSignals, float[] rightSignals) {
		if (this.leftDataGetter != null) {
			this.leftDataGetter.dataCallBack(leftSignals);
		}
		if (this.rightDataGetter != null) {
			this.rightDataGetter.dataCallBack(rightSignals);
		}
	}
	
}
