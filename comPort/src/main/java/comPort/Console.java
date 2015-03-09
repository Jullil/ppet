package comPort;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Console extends JPanel {

    private static final long serialVersionUID = -4538532229007904362L;

    private CommandLine commandLine = new CommandLine(">");

    private String prompt = "";

    private ConsoleCommandLine commandLine2 = new ConsoleCommandLine();
    private CommandLine.Listener listener;

    final JLabel output = new JLabel();
    private List<CommandLine.Listener> listeners = new ArrayList<>();

    public Console(String title) {
        super();
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());

        if (!title.isEmpty()) {
            add(new Label(title), BorderLayout.PAGE_START);
        }

        final JPanel body = new JPanel(new BorderLayout());
        body.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(body, BorderLayout.CENTER);

/*        output.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                commandLine.grabFocus();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                commandLine.grabFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                commandLine.grabFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {}
        });*/

        output.setVerticalAlignment(JLabel.TOP);
        output.setVerticalTextPosition(JLabel.TOP);
        output.setAlignmentY(TOP_ALIGNMENT);
        output.setBackground(new Color(46, 222, 87, 222));
        output.setBorder(new BevelBorder(BevelBorder.LOWERED));
        body.add(output, BorderLayout.CENTER);

        commandLine.addConsoleCommandLineListener(new CommandLine.Listener() {
            @Override
            public void receiveCommand(String command) {
                appendOutput(command);
                notifyListeners(command);
            }
        });
        commandLine.setBackground(new Color(255, 31, 88, 111));
        commandLine.setBorder(new BevelBorder(BevelBorder.LOWERED));
        body.add(commandLine, BorderLayout.SOUTH);
    }

    private void notifyListeners(String command) {
        for (final CommandLine.Listener listener : listeners) {
            listener.receiveCommand(command);
        }
    }

    public void addCommandListener(CommandLine.Listener listener) {
        listeners.add(listener);
    }

    public void appendOutput(String data) {
        output.setText(output.getText() + data + '\n');
    }

    public void close() {
        setVisible(false);
    }
}


