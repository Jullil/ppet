package comPort;

/**
 * @author jul
 */
public class PortConsole extends Console {
    public final ConnectionSettings settings;

    public final PortConnection connection;

    public PortConsole(String title, ConnectionSettings settings) {
        super(title);
        this.settings = settings;
        this.connection = new PortConnection(settings);

        init();
    }

    private void init() {
        addCommandListener(new CommandLine.Listener() {
            @Override
            public void receiveCommand(String command) {
                sendCommand(command);
            }
        });
        connection.addReader(new PortConnection.Reader() {
            @Override
            public void receive(String result) {
                outputCommandResult(result);
            }
        });
    }

    private void sendCommand(String command) {
        connection.write(command);
    }

    private void outputCommandResult(String result) {
        appendOutput(result);
    }

    @Override
    public void close() {
        super.close();
        connection.close();
    }
}
