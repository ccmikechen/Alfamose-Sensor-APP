package data;

import java.util.ArrayList;
import java.util.List;

import edu.kuasmis.wmc.nike.DataGetterInterface;

public class DataGetterManager implements DataGetterInterface {

	private List<DataGetterInterface> dataGetterList;

	public DataGetterManager() {
		this.dataGetterList = new ArrayList<DataGetterInterface>();
	}
	
	@Override
	public void dataCallBack(float[] signals) {
		for (DataGetterInterface dataGetter : dataGetterList)
			dataGetter.dataCallBack(signals);
	}
	
	public void add(DataGetterInterface dataGetter) {
		this.dataGetterList.add(dataGetter);
	}
	
}
