package comPort;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Console extends JPanel {

    private static final long serialVersionUID = -4538532229007904362L;

    private CommandLine commandLine = new CommandLine();

    private String prompt = "";
    public boolean ReadOnly = false;
    private ConsoleCommandLine commandLine2 = new ConsoleCommandLine();
    private ConsoleListener listener = null;
    private String oldText = "";
    private Vector history = new Vector();
    private int history_index = -1;

    final JLabel output = new JLabel();

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

        output.addMouseListener(new MouseListener() {
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
        });
        output.setVerticalAlignment(JLabel.TOP);
        output.setVerticalTextPosition(JLabel.TOP);
        output.setAlignmentY(TOP_ALIGNMENT);

        body.add(output, BorderLayout.CENTER);

        commandLine.setBackground(new Color(255, 31, 88, 111));
        commandLine.setBorder(new BevelBorder(BevelBorder.LOWERED));

        output.setBackground(new Color(46, 222, 87, 222));
        output.setBorder(new BevelBorder(BevelBorder.LOWERED));
        body.add(commandLine, BorderLayout.SOUTH);

    }

    public void registerConsoleListener(ConsoleListener c) {
        listener = c;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String s) {
        prompt = s;
    }

    private void backspace() {
        if (!commandLine2.isEmpty()) {
            commandLine2.removeLast();
            print();
        }
    }

    private void enter() {
        final String command = commandLine2.toString();
        String result = "";
        if (listener != null) {
            result = listener.receiveCommand(command);
        }

        history.add(command);

        commandLine2.clear();
        if (!result.equals("")) {
            result = result + "<br>";
        }
        final String commandLineText = commandLine.getText().substring(6, commandLine.getText().length() - 7);
        oldText = output.getText() + "<br>" + commandLineText.substring(0, commandLineText.length() - 1) + "<BR>" + result;
        output.setText("<HTML>" + oldText + "</HTML>");
        output.repaint();

        commandLine.setText(">_");
    }

    private void print() {
        commandLine.setText("<HTML>" + commandLine2.toString() + "_</HTML>");
        commandLine.repaint();
    }




}


