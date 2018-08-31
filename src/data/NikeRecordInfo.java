package data;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NikeRecordInfo {

	private static final String DEFAULT_DIRECTORY =
			"C:\\Pressure Records";
	
	private static File currentDir;
	
	private File directory;
	
	private String recordName = "test";
	
	private String tester = "Default";
	
	public NikeRecordInfo() {
		this.currentDir = new File(DEFAULT_DIRECTORY);
	}
	
	public void setDirectory(File dir) {
		this.directory = dir;
	}
	
	public void setRecordName(String name) {
		this.recordName = name;
	}
	
	public void setTester(String tester) {
		this.tester = tester;
	}
	
	public File getDirectory() {
		return this.directory;
	}
	
	public String getRecordName() {
		return this.recordName;
	}
	
	public String getTester() {
		return this.tester;
	}

	public File getTesterDirectory() {
		return new File(this.directory.getAbsolutePath() + "\\" + this.tester);
	}
	
	public File getRecordFile() {
		return new File(getTesterDirectory().getAbsolutePath() + "\\" + 
							getNowTimeString() + "_" +
							this.recordName + ".csv");
	}
	
	private String getNowTimeString() {
		SimpleDateFormat dateFormat = 
				new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}
	
}
