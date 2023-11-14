package entities;

import java.util.ArrayList;
import java.util.List;

public class Recovery {
    private static final Recovery recovery =  new Recovery();
    private static final List<String> listUndo = new ArrayList<>();
    private static final List<String> listRedo = new ArrayList<>();

    public static Recovery getInstance() {
        return recovery;
    }

    public List<String> getListUndo() {
        return listUndo;
    }

    public List<String> getListRedo() {
        return listRedo;
    }

    public void persistListUndo(String undo) {
        listUndo.add(undo);
    }

    public void persistListRedo(String redo) {
        listRedo.add(redo);
    }

    public void removeListUndo(String undo) {
        listUndo.remove(undo);
    }

    public void removeListRedo(String redo) {
        listRedo.remove(redo);
    }

    public void removeAllListUndo() {
        listUndo.clear();
    }

    public void removeAllListRedo() {
        listRedo.clear();
    }



}
