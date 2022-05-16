import java.util.*;

public abstract class RecordedCommand implements Command {
    private static ArrayList<RecordedCommand> undoList = new ArrayList<>();
    private static ArrayList<RecordedCommand> redoList = new ArrayList<>();

    public abstract void undoMe();

    public abstract void redoMe();

    protected void addUndoCommand(RecordedCommand cmd) {
        undoList.add(cmd);
    }

    protected void addRedoCommand(RecordedCommand cmd) {
        redoList.add(cmd);
    }

    protected void clearRedoList() {
        redoList.clear();
    }

    public static void undoOneCommand() {
        if (undoList.size() == 0) {
            System.out.println("Nothing to undo.");
            return;
        }
        undoList.remove(undoList.size() - 1).undoMe();
    }

    public static void redoOneCommand() {
        if (redoList.size() == 0) {
            System.out.println("Nothing to redo.");
            return;
        }
        redoList.remove(redoList.size() - 1).redoMe();
    }
}