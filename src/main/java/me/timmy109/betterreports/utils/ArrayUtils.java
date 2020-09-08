package me.timmy109.betterreports.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    private static List<String> adminHelp = new ArrayList<>();

    private static List<String> playerHelp = new ArrayList<>();

    private static List<String> debug = new ArrayList<>();

    private static List<String> reload = new ArrayList<>();

    public static ArrayList<String> getAdminHelpList() {
        return (ArrayList<String>) adminHelp;
    }

    public static void setAdminHelpList(ArrayList<String> list) {
        ArrayUtils.adminHelp = list;
    }

    public static ArrayList<String> getPlayerHelpList() {
        return (ArrayList<String>) playerHelp;
    }

    public static void setPlayerHelpList(ArrayList<String> list) {
        ArrayUtils.playerHelp = list;
    }

    public static ArrayList<String> getDebugList() {
        return (ArrayList<String>) debug;
    }

    public static void setDebugList(ArrayList<String> list) {
        ArrayUtils.debug = list;
    }

    public static ArrayList<String> getReloadList() {
        return (ArrayList<String>) reload;
    }

    public static void setReloadList(ArrayList<String> list) {
        ArrayUtils.reload = list;
    }
}

