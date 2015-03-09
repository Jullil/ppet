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
    private final String greeting;

    private StringBuilder command = getEmptyCommand();
    private List<String> history = new ArrayList<String>();
    private int historyIndex = 0;

    private static final int HISTORY_NEXT = 1;
    private static final int HISTORY_PREVIOUS = 2;

    private List<Listener> listeners = new ArrayList<Listener>();

    public CommandLine(String greeting) {
        this.greeting = greeting;

        init();
    }

    private void init() {
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

    private StringBuilder getEmptyCommand() {
        return new StringBuilder(greeting);
    }

    public void fromString(String string) {
        command = getEmptyCommand();
        command.append(string);
        update();
    }

    @Override
    public String toString() {
        return command.toString();
    }

    public void clear() {
        command = getEmptyCommand();
        update();
    }

    public boolean isEmpty() {
        return command.length() == 0;
    }

    public void appendSymbol(String str) {
        command.append(str);
        update();
    }

    public void removeLastSymbol() {
        command.delete(command.length() - 1, command.length());
        update();
    }

    private void handleKey(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_UP:
                loadFromHistory(keyCode == KeyEvent.VK_UP ? HISTORY_PREVIOUS : HISTORY_NEXT);
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
            listener.receiveCommand(command);
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

    private void loadFromHistory(int direction) {
        if (history.isEmpty()) {
            return;
        }
        if (direction == HISTORY_NEXT) {
            historyIndex++;
            if (historyIndex >= history.size()) {
                historyIndex = history.size() - 1;
            }
        } else if (direction == HISTORY_PREVIOUS) {
            historyIndex--;
            if (historyIndex < 0) {
                historyIndex = 0;
            }
        }
        fromString(history.get(historyIndex));
    }

    public static interface Listener {
        void receiveCommand(String command);
    }

    private void update() {
        setText(command.toString());
        repaint();
    }
}
