package comPort;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author y.loykova
 */
public class CommandLine extends JLabel {
    private final String greeting = ">";

    private StringBuilder command = new StringBuilder();
    private List<String> history = new ArrayList<String>();

    private static final int HISTORY_UP = 1;
    private static final int HISTORY_DOWN = 2;

    public CommandLine() {
        setFocusable(true);
        addKeyListener(new KeyListener() {
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
    }

    @SuppressWarnings("unchecked")
    public void fromString(String[] strings) {
        for (final String string : strings) {
            command.append(string);
        }
    }

    @Override
    public String toString() {
        return command.toString();
    }

    public void clear() {
        command = new StringBuilder();
    }

    public boolean isEmpty() {
        return command.length() == 0;
    }

    public CommandLine append(String str) {
        command.append(str);
        return this;
    }

    public CommandLine removeLast() {
        command.delete(command.length() - 1, command.length());
        return this;
    }

    private void handleKey(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        System.out.println(keyCode);
        switch (keyCode) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_UP:
                loadFromHistory(keyCode == KeyEvent.VK_UP ? HISTORY_UP : HISTORY_DOWN);
                break;
            case KeyEvent.VK_ENTER:
                enter();
                break;
            case KeyEvent.VK_BACK_SPACE:
                backspace();
                break;
            case KeyEvent.CHAR_UNDEFINED:
                break;
            default:
                commandLine2.append(String.valueOf(e.getKeyChar()));
                print();
        }
    }

    private void loadFromHistory(int dir) {
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
            commandLine2.clear();
            String p = (String) history.get(history_index);
            commandLine2.fromString(p.split(""));
        } else if (dir == 2) {
            history_index--;
            if (history_index < 0) {
                history_index = history.size() - 1;
            }
            // System.out.println(history_index);
            commandLine2.clear();
            String p = (String) history.get(history_index);
            commandLine2.fromString(p.split(""));
        }

        print();
    }
}
