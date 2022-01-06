package demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TryLog {
    private static final Logger logger = LogManager.getLogger(TryLog.class);

    public static void main(String[] args) {

        System.out.print(logger.getName() + "\n");

        logger.trace("Hello World {}!", 555 + 111);

        logger.debug("{} == 5?", () -> getNumber());

        logger.info("Hello from Log4j 2");

        logger.error("THIS IS ERROR level...");

        logger.warn("this is at warn level");

        logger.fatal("fatal!!!");

    }

    static int getNumber() {
        return 5;
    }
}
