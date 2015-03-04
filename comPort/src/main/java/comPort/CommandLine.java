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
    private int historyIndex = 0;

    private static final int HISTORY_UP = 1;
    private static final int HISTORY_DOWN = 2;

    private List<Listener> listeners = new ArrayList<Listener>();

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
        repaint();
    }

    @Override
    public String toString() {
        return command.toString();
    }

    public void clear() {
        command = new StringBuilder();
        repaint();
    }

    public boolean isEmpty() {
        return command.length() == 0;
    }

    public CommandLine appendSymbol(String str) {
        command.append(str);
        repaint();
        return this;
    }

    public CommandLine removeLastSymbol() {
        command.delete(command.length() - 1, command.length());
        repaint();
        return this;
    }

    private void handleKey(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_UP:
                loadFromHistory(keyCode == KeyEvent.VK_UP ? HISTORY_DOWN : HISTORY_UP);
                break;
            case KeyEvent.VK_ENTER:
                enterCommand();
                break;
            case KeyEvent.VK_BACK_SPACE:
                removeLastSymbol();
                break;
            case KeyEvent.CHAR_UNDEFINED:
                break;
            default:
                appendSymbol(String.valueOf(e.getKeyChar()));
        }
    }

    public void addConsoleCommandLineListener(Listener listener) {
        listeners.add(listener);
    }

    private void notifyObservers(String command) {
        for (final Listener listener : listeners) {
            listener.receiveCommand(command.toString());
        }
    }

    private void enterCommand() {
        addToHistory(command.toString());
        notifyObservers(command.toString());
        clear();
    }

    private void addToHistory(String command) {
        history.add(command);
        historyIndex = history.size() - 1;
    }

    private void loadFromHistory(int dir) {
        if (history.isEmpty()) {
            return;
        }
        if (dir == HISTORY_UP) {
            historyIndex++;
            if (history_index > history.size() - 1) {
                history_index = 0;
            }
            // System.out.println(history_index);
            commandLine2.clear();
            String p = (String) history.get(history_index);
            commandLine2.fromString(p.split(""));
        } else if (dir == HISTORY_DOWN) {
            history_index--;
            if (history_index < 0) {
                history_index = history.size() - 1;
            }
            // System.out.println(history_index);
            commandLine2.clear();
            String p = (String) history.get(history_index);
            commandLine2.fromString(p.split(""));
        }

        repaint();
    }

    public static interface Listener {
        String receiveCommand(String command);
    }

    private void repaint() {

    }
}
