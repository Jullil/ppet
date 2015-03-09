package comPort;

import jssc.SerialPort;
import jssc.SerialPortList;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.EventListener;

/**
 * @author jul
 */
public class ConnectionDialog extends JDialog {
    private static Logger logger = Logger.getLogger(MainFrame.class);

    private static final int[] baudRates = {
        SerialPort.BAUDRATE_110,
        SerialPort.BAUDRATE_300,
        SerialPort.BAUDRATE_600,
        SerialPort.BAUDRATE_1200,
        SerialPort.BAUDRATE_4800,
        SerialPort.BAUDRATE_9600,
        SerialPort.BAUDRATE_14400,
        SerialPort.BAUDRATE_19200,
        SerialPort.BAUDRATE_38400,
        SerialPort.BAUDRATE_57600,
        SerialPort.BAUDRATE_115200,
        SerialPort.BAUDRATE_128000,
        SerialPort.BAUDRATE_256000
    };
    private static final int[] dataBits = {
        SerialPort.DATABITS_5,
        SerialPort.DATABITS_6,
        SerialPort.DATABITS_7,
        SerialPort.DATABITS_8
    };
    private static final int[] stopBits = {
        SerialPort.STOPBITS_1,
        SerialPort.STOPBITS_2,
        SerialPort.STOPBITS_1_5
    };
    private static final int[] parities = {
        SerialPort.PARITY_NONE,
        SerialPort.PARITY_ODD,
        SerialPort.PARITY_EVEN,
        SerialPort.PARITY_MARK,
        SerialPort.PARITY_SPACE,
    };
    
    private final JComboBox<String> comPortListField = createComPortListField();
    private final JComboBox<String> baudRatesField = createSettingsComboBoxField(baudRates);
    private final JComboBox<String> dataBitsField = createSettingsComboBoxField(dataBits);
    private final JComboBox<String> stopBitsField = createSettingsComboBoxField(stopBits);
    private final JComboBox<String> paritiesField = createSettingsComboBoxField(parities);
    private final JButton createBtn = new JButton("Create");

    public ConnectionDialog() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("New connection");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 200);


        JPanel form = new JPanel(new GridLayout(0, 2));
        add(form);

        JLabel label = new JLabel("COM-port", JLabel.TRAILING);
        label.setLabelFor(comPortListField);
        form.add(label);
        form.add(comPortListField);

        label = new JLabel("Baud rate", JLabel.TRAILING);
        label.setLabelFor(baudRatesField);
        form.add(label);
        form.add(baudRatesField);

        label = new JLabel("Data bits", JLabel.TRAILING);
        label.setLabelFor(dataBitsField);
        form.add(label);
        form.add(dataBitsField);

        label = new JLabel("Stop bits", JLabel.TRAILING);
        label.setLabelFor(stopBitsField);
        form.add(label);
        form.add(stopBitsField);

        label = new JLabel("Parity", JLabel.TRAILING);
        label.setLabelFor(paritiesField);
        form.add(label);
        form.add(paritiesField);


        createBtn.setAlignmentX(0.5f);
        add(createBtn);
    }

    public void addSuccessListener(final SuccessListener listener) {
        createBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                final ConnectionSettings settings = new ConnectionSettings(
                    (String) comPortListField.getSelectedItem(),
                    Integer.parseInt((String) baudRatesField.getSelectedItem()),
                    Integer.parseInt((String) dataBitsField.getSelectedItem()),
                    Integer.parseInt((String) stopBitsField.getSelectedItem()),
                    Integer.parseInt((String) paritiesField.getSelectedItem())
                );
                listener.actionPerformed(settings);
                dispose();
            }
        });
    }

    private JComboBox<String> createComPortListField() {
        final String[] portNames = SerialPortList.getPortNames();
        logger.info(portNames.length + " COM-ports have been founded: " + Arrays.asList(portNames));
        final JComboBox<String> field = new JComboBox<>(portNames);

        return field;
    }

    private JComboBox<String> createSettingsComboBoxField(int[] items) {
        final JComboBox<String> field = new JComboBox<>();
        for (final int item : items) {
            field.addItem(String.valueOf(item));
        }
        return field;
    }

    public interface SuccessListener extends EventListener {
        public void actionPerformed(ConnectionSettings settings);
    }
}
