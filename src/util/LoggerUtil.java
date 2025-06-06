package util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {

    public static Logger getLogger(Class<?> cls) {
        Logger logger = Logger.getLogger(cls.getName());

        // Evita logger duplicati
        if (logger.getHandlers().length == 0) {
            try {
                // Log su file
                String logFile = ConfigLoader.get("log.file", "log/app.log");
                Level level = Level.parse(ConfigLoader.get("log.level", "INFO"));

                FileHandler fileHandler = new FileHandler(logFile, true);
                fileHandler.setFormatter(new SimpleFormatter());

                logger.addHandler(fileHandler);
                logger.setLevel(level);
                logger.setUseParentHandlers(true); // stampa anche su console

            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Errore nel setup del logger: " + e.getMessage());
            }
        }

        return logger;
    }
}