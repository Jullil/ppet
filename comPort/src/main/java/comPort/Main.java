package comPort;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main extends JFrame {
    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        final Main window = new Main();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1000, 600);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.darkGray);
        window.getContentPane().add(BorderLayout.EAST, rightPanel);

        JButton createConnectionBtn = new JButton("Start new window");
        createConnectionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                window.createConnection();
            }
        });
        rightPanel.add(createConnectionBtn);

        JPanel playingField = new JPanel();
        playingField.setLayout(new BorderLayout());
        playingField.setBackground(new Color(45, 151, 71));
        window.getContentPane().add(BorderLayout.CENTER, playingField);

        JPanel topPlayer = new JPanel();
        topPlayer.setBackground(Color.blue);
        playingField.add(BorderLayout.NORTH, topPlayer);

        JPanel bottomPlayer = new JPanel();
        bottomPlayer.setBackground(Color.blue);
        playingField.add(BorderLayout.SOUTH, bottomPlayer);

        JPanel gamePanel = new JPanel();
        gamePanel.setOpaque(false);
        playingField.add(BorderLayout.CENTER, gamePanel);


        window.setVisible(true);

    }

    public void createConnection() {
        SerialPort serialPort = new SerialPort("COM1");
        try {
            System.out.println("Port opened: " + serialPort.openPort());
            System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
            System.out.println("\"Hello World!!!\" successfully writen to port: " + serialPort.writeBytes("Hello World!!!".getBytes()));
            System.out.println("Port closed: " + serialPort.closePort());
        }
        catch (SerialPortException ex){
            logger.error("There are some errors with com-port connection", ex);
        }
    }
}
