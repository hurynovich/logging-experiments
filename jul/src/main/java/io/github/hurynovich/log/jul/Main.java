package io.github.hurynovich.log.jul;

import java.net.URISyntaxException;
import java.util.logging.Logger;

import static java.lang.String.format;

public class Main {
    private static final boolean IS_CUSTOM_CONFIG_SET = ClasspathFileConfig.setSystemPropertyIfEmpty();

    private static final Logger log = Logger.getGlobal();
//    LogManager

    public static void main(String[] args) throws URISyntaxException {
        System.out.println(IS_CUSTOM_CONFIG_SET);
        log.severe("Hello");
    }
}
