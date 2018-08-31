package sensor;

public class NikeSensorException extends Exception {

	private String message;
	
	public NikeSensorException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
	
}
