package comPort;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    private static Logger logger = Logger.getLogger(MainFrame.class);

    public static void main(String[] args) {
        final MainFrame window = new MainFrame();

    }

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(Math.round(Window.CENTER_ALIGNMENT), Math.round(Window.TOP_ALIGNMENT));
        setSize(1000, 600);

        createMenuBar();

        setVisible(true);
    }

    private void createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        final JMenuItem newConnectionItem = new JMenuItem("New connection");
        newConnectionItem.setMnemonic(KeyEvent.VK_N);
        newConnectionItem.setToolTipText("Create a new connection to COM port");
        newConnectionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                createConnectionDialog();
            }
        });

        fileMenu.add(newConnectionItem);
        menuBar.add(fileMenu);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Exit application");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    private void createConnectionDialog() {
        final ConnectionDialog dialog = new ConnectionDialog();
        dialog.addSuccessListener(new ConnectionDialog.SuccessListener() {
            @Override
            public void actionPerformed(ConnectionSettings settings) {
                createConnection(settings);
            }
        });
        dialog.setVisible(true);
    }

    public void createConnection(ConnectionSettings settings) {
        logger.debug("Try to get connection with settings: " + settings);

        SerialPort serialPort = new SerialPort(settings.getPortName());
        try {
            serialPort.openPort();
            logger.debug("Post opened successfuly");
            serialPort.setParams(settings.getBaudRate(), settings.getDataBits(), settings.getStopBits(), settings.getParity());
            logger.debug("Params has setted");
            serialPort.writeBytes("Hello World!!!".getBytes());
            logger.debug("\"Hello World!!!\" successfully writen to port: ");
            serialPort.closePort();
            logger.debug("Port closed");
        }
        catch (SerialPortException ex){
            logger.error("There are some errors with com-port connection", ex);
        }
    }
}
