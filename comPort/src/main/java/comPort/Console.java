package comPort;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Console extends JPanel {
    private final String greeting = "> ";
    private CommandLine commandLine = new CommandLine(greeting);
    final JTextArea output = new JTextArea();
    private List<CommandLine.Listener> listeners = new ArrayList<>();

    public Console(String title, String description) {
        super();
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());

        if (title != null && !title.isEmpty()) {
            final JLabel titleLabel = new JLabel(title);
            titleLabel.setToolTipText(description);
            add(titleLabel, BorderLayout.PAGE_START);
        }

        final JPanel body = new JPanel(new BorderLayout());
        body.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(body, BorderLayout.CENTER);

        //output.setVerticalAlignment(JLabel.TOP);
        //output.setVerticalTextPosition(JLabel.TOP);
        output.setAlignmentY(TOP_ALIGNMENT);
        output.setAutoscrolls(true);
        body.add(new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        commandLine.addConsoleCommandLineListener(new CommandLine.Listener() {
            @Override
            public void receiveCommand(String command) {
                appendOutput(greeting + command + '\n');
                notifyListeners(command);
            }
        });
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
        output.append(data);
    }

    public void close() {
        setVisible(false);
    }
}


