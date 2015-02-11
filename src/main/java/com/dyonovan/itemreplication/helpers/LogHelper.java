package com.dyonovan.itemreplication.helpers;

import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.common.FMLLog;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {
    private static Logger logger = Logger.getLogger(Constants.MODID);

    public static void init() {

        logger.setParent((Logger) FMLLog.getLogger());
    }

    public static void log(Level logLevel, Object object) {

        logger.log(logLevel, object.toString());
    }

    public static void severe(Object object) {

        log(Level.SEVERE, object.toString());
    }

    public static void debug(Object object) {

        log(Level.WARNING, "[DEBUG] " + object.toString());
    }

    public static void warning(Object object) {

        log(Level.WARNING, object.toString());
    }

    public static void info(Object object) {

        log(Level.INFO, object.toString());
    }

    public static void config(Object object) {

        log(Level.CONFIG, object.toString());
    }

    public static void fine(Object object) {

        log(Level.FINE, object.toString());
    }

    public static void finer(Object object) {

        log(Level.FINER, object.toString());
    }

    public static void finest(Object object) {

        log(Level.FINEST, object.toString());
    }
}