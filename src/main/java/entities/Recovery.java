package entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Recovery {
    private static List<String> listUndo = new ArrayList<>();
    private static List<String> listRedo = new ArrayList<>();

    public static List<String> getListUndo() {
        return listUndo;
    }

    public static List<String> getListRedo() {
        return listRedo;
    }

    public static void addToListUndo(String undo) {listUndo.add(undo); }

    public static void addToListRedo(String redo) {listRedo.add(redo);}

    public static void setListUndo(List<String> list) {listUndo = list;}

    public static void setListRedo(List<String> list){ listRedo = list; }

    public static void removeFromListUndo(String undo) {
        listUndo.remove(undo);
    }

    public static void removeFromListRedo(String redo) {
        listRedo.remove(redo);
    }

    public static void removeAllListUndo() {
        listUndo.clear();
    }

    public static void removeAllListRedo() {
        listRedo.clear();
    }



}
