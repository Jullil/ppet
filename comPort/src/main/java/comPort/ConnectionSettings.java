package comPort;

/**
* @author jul
*/
public class ConnectionSettings {
    private final String portName;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;

    public ConnectionSettings(String portName, int baudRate, int dataBits, int stopBits, int parity) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    public String getPortName() {
        return portName;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }

    @Override
    public String toString() {
        return "ConnectionSettings{" +
                "portName='" + portName + '\'' +
                ", baudRate=" + baudRate +
                ", dataBits=" + dataBits +
                ", stopBits=" + stopBits +
                ", parity=" + parity +
                '}';
    }
}
