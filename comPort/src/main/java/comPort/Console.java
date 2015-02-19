package comPort;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Console extends JPanel {

    private static final long serialVersionUID = -4538532229007904362L;

    private JLabel commandLabel = new JLabel(">");
    private String prompt = "";
    public boolean ReadOnly = false;
    private ConsoleCommandLine commandLine = new ConsoleCommandLine();
    private ConsoleListener listener = null;
    private String oldText = "";
    private Vector history = new Vector();
    private int history_index = -1;
    private boolean history_mode = false;

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
                commandLabel.grabFocus();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                commandLabel.grabFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                commandLabel.grabFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        output.setAlignmentY(TOP_ALIGNMENT);
        body.add(output, BorderLayout.CENTER);

        commandLabel.setFocusable(true);
        commandLabel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKey(e);
            }
        });
        commandLabel.setBackground(new Color(255, 31, 88, 111));
        commandLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        output.setBackground(new Color(46, 222, 87, 222));
        output.setBorder(new BevelBorder(BevelBorder.LOWERED));
        body.add(commandLabel, BorderLayout.SOUTH);

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
        if (!commandLine.isEmpty()) {
            commandLine.removeLast();
            print();
        }
    }

    private void enter() {
        final String command = commandLine.toString();
        String result = "";
        if (listener != null) {
            result = listener.receiveCommand(command);
        }

        history.add(command);

        commandLine.clear();
        if (!result.equals("")) {
            result = result + "<br>";
        }
        final String commandLineText = commandLabel.getText().substring(6, commandLabel.getText().length() - 7);
        oldText = output.getText() + "<br>" + commandLineText.substring(0, commandLineText.length() - 1) + "<BR>" + result;
        output.setText("<HTML>" + oldText + "</HTML>");
        output.repaint();

        commandLabel.setText(">_");
    }

    private void print() {
        commandLabel.setText("<HTML>" + commandLine.toString() + "_</HTML>");
        commandLabel.repaint();
    }

    private void history(int dir) {
        if (history.isEmpty()) {
            return;
        }
        if (dir == 1) {
            history_mode = true;
            history_index++;
            if (history_index > history.size() - 1) {
                history_index = 0;
            }
            // System.out.println(history_index);
            commandLine.clear();
            String p = (String) history.get(history_index);
            commandLine.fromString(p.split(""));

        } else if (dir == 2) {
            history_index--;
            if (history_index < 0) {
                history_index = history.size() - 1;
            }
            // System.out.println(history_index);
            commandLine.clear();
            String p = (String) history.get(history_index);
            commandLine.fromString(p.split(""));
        }

        print();
    }

    private void handleKey(KeyEvent e) {
        System.out.println(e.getKeyCode());
        if (!ReadOnly) {
            if (e.getKeyCode() == 38 | e.getKeyCode() == 40) {
                if (e.getKeyCode() == 38) {
                    history(1);
                } else if (e.getKeyCode() == 40 & history_mode != false) {
                    history(2);
                }
            } else {
                history_index = -1;
                history_mode = false;
                if (e.getKeyCode() == 13 | e.getKeyCode() == 10) {
                    enter();
                } else if (e.getKeyCode() == 8) {
                    backspace();
                } else {
                    if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
                        commandLine.append(String.valueOf(e.getKeyChar()));
                        print();
                    }
                }
            }
        }
    }
}


