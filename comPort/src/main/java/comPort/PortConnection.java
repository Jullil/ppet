package comPort;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jul
 */
public class PortConnection {
    private static Logger logger = Logger.getLogger(MainFrame.class);

    private static final int eventMask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;

    private final ConnectionSettings settings;

    private final SerialPort connection;

    private List<Reader> readers = new ArrayList<>();

    public PortConnection(ConnectionSettings settings) {
        this.settings = settings;
        this.connection = new SerialPort(settings.getPortName());

        init();
    }

    private void init() {
        try {
            connection.setEventsMask(eventMask);
            connection.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    read(serialPortEvent);
                }
            });
        } catch (SerialPortException e) {
            logger.error("Ошибка при попытке инициализировать порт '" + settings.getPortName() + "' :(", e);
        }
    }

    public void open() {
        final String portName = settings.getPortName();
        try {
            logger.debug("Открываю порт");
            connection.openPort();
            logger.debug("Успешно. Передаю параметры");
            connection.setParams(settings.getBaudRate(), settings.getDataBits(), settings.getStopBits(), settings.getParity());
            logger.debug("Успешно. Ожидаю комманды.");
        } catch (final SerialPortException e) {
            logger.error("Ошибка соединения с портом '" + portName + "' :(", e);
        }
    }

    public void close() {
        final String portName = settings.getPortName();
        try {
            logger.debug("Закрываю порт '" + portName + "'");
            connection.closePort();
            logger.debug("Успешно");
        } catch (final SerialPortException e) {
            logger.error("Ошибка при попытке закрыть соединение с портом '" + portName + "' :(", e);
        }
    }

    public void write(String command) {
        if (!connection.isOpened()) {
            open();
        }

        final String portName = settings.getPortName();
        try {
            logger.debug("Отправляю комманду на порт '" + portName + "'");
            connection.writeString(command);
            logger.debug("Успешно");
        } catch (final SerialPortException e) {
            logger.error("Ошибка при попытке закрыть соединение с портом '" + portName + "' :(", e);
        }
    }

    public void addReader(Reader reader) {
        readers.add(reader);
    }

    private void read(SerialPortEvent serialPortEvent) {
        String result = "";

        if (serialPortEvent.isRXCHAR()) {//If data is available
            try {
                result = connection.readString();
            } catch (final SerialPortException e) {
                logger.error("Ошибка при попытке считать данные с порта '" + settings.getPortName() + "' :(", e);
            }
        }
        for (final Reader reader : readers) {
            reader.receive(result);
        }
    }

    public static abstract class Reader {
        public abstract void receive(String result);
    }
}
