package cli;

public class SairCommand implements Command {

    private final Runnable onExit;

    public SairCommand(Runnable onExit) {
        this.onExit = onExit;
    }

    @Override
    public void execute() {
        System.out.println("Encerrando sistema...");
        onExit.run();
    }
}
