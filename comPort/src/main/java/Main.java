package comPort;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Hellod world!");
        logger.debug("Hello World!!!!!!!!!!!!!!!!!");

        SerialPort serialPort = new SerialPort("COM1");
        try {
            System.out.println("Port opened: " + serialPort.openPort());
            System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
            System.out.println("\"Hello World!!!\" successfully writen to port: " + serialPort.writeBytes("Hello World!!!".getBytes()));
            System.out.println("Port closed: " + serialPort.closePort());
        }
        catch (SerialPortException ex){
            System.out.println(ex);
        }
    }
}
