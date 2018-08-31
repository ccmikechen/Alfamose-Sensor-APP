package test;

import gnu.io.CommPortIdentifier;

public class CommPortTest {
	public static void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
        	System.out.println(portIdentifier.getCurrentOwner());
        }     
    }
	
	public static void main(String[] args) throws Exception {
		connect("COM3");
	}
}
