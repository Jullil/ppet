package comPort;

import java.util.Vector;

class ConsoleCommandLine {

    private StringBuilder command = new StringBuilder();

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

    public ConsoleCommandLine append(String str) {
        command.append(str);
        return this;
    }

    public ConsoleCommandLine removeLast() {
        command.delete(command.length() - 1, command.length());
        return this;
    }
}
