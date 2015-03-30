package comPort;


import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author y.loykova
 */
public class CommandLine extends JLabel {
    private final String greeting;

    private StringBuilder command;
    private List<String> history = new ArrayList<String>();
    private int historyIndex = 0;

    private static final int HISTORY_NEXT = 1;
    private static final int HISTORY_PREVIOUS = 2;

    private List<Listener> listeners = new ArrayList<Listener>();

    public CommandLine(String greeting) {
        this.greeting = greeting;
        this.command = getEmptyCommand();

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
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                grabFocus();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        update();
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(new BevelBorder(BevelBorder.LOWERED));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(new EmptyBorder(2, 2, 2, 2));
            }
        });
    }

    private StringBuilder getEmptyCommand() {
        return new StringBuilder();
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
        if (isEmpty()) {
            return;
        }
        command.delete(command.length() - 1, command.length());
        update();
    }

    private void handleKey(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        if (e.isControlDown() && e.getKeyCode() != 0) {
            enterCommand();
        } else {
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
        setText(greeting + command.toString());
        repaint();
    }
}
