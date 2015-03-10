package comPort;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MainFrame extends JFrame {
    private static Logger logger = Logger.getLogger(MainFrame.class);

    private final JPanel container = new JPanel(new GridLayout(0, 2));

    private final List<PortConsole> consoles = new ArrayList<>();


    public static void main(String[] args) {
        final MainFrame window = new MainFrame();
        window.setVisible(true);
    }

    public MainFrame() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        ConnectionSettings settings = new ConnectionSettings("COM1", 1, 1, 1, 1);
        addConsole(settings);
        addConsole(settings);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                onExitApplication();
                System.exit(0);
            }
        });

        setLocation(Math.round(Window.CENTER_ALIGNMENT), Math.round(Window.TOP_ALIGNMENT));
        setSize(1000, 600);

        setJMenuBar(createMenuBar());
        add(container);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu fileMenu = new JMenu("Файл");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        final JMenuItem newConnectionItem = new JMenuItem("Новое соединения");
        newConnectionItem.setMnemonic(KeyEvent.VK_N);
        newConnectionItem.setToolTipText("Создать новое соединения с COM-портом");
        newConnectionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                createConnectionDialog();
            }
        });

        fileMenu.add(newConnectionItem);
        menuBar.add(fileMenu);

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Закрыть приложение");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        return menuBar;
    }

    private void createConnectionDialog() {
        final ConnectionDialog dialog = new ConnectionDialog();
        dialog.addSuccessListener(new ConnectionDialog.SuccessListener() {
            @Override
            public void actionPerformed(ConnectionSettings settings) {
                addConsole(settings);
            }
        });
        dialog.setVisible(true);
    }

    private void addConsole(ConnectionSettings settings) {
        final PortConsole console = new PortConsole(settings.getPortName(), settings);
        console.setVisible(true);
        consoles.add(console);

        container.add(console);
        repaint();
    }

    private void onExitApplication() {
        for (PortConsole console : consoles) {
            console.close();
        }
    }

    private void removeConsole(PortConsole console) {
        console.close();
        container.remove(console);
        consoles.remove(console);
    }
}
