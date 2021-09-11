package com.github.alycecil.econ.util;

import com.fs.starfarer.api.Global;

public class Mods {
    public static volatile boolean indEvo;
    public static boolean isIndustrialEvo() {
        Global.getLogger(Mods.class).info("IndEvo detected : "+indEvo);
        return indEvo;
    }

    public static volatile boolean nexelin;
    public static boolean isNexelin() {
        return nexelin;
    }
}
