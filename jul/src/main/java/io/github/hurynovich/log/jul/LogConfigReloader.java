package io.github.hurynovich.log.jul;

import java.io.InputStream;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.*;

import static java.lang.String.format;

public final class LogConfigReloader {

    public static final String DEFAULT_CONFIG_PROD = "/logging.properties";
    public static final String DEFAULT_CONFIG_TEST = "/logging-test.properties";

    /**
     * Reloads logging config from '/logging-test.properties' file if it is present otherwise
     * reloads config from '/logging.properties'.
     */
    public static void reloadConfig(){
        var loader = LogConfigReloader.class;
        if(isResourcePresent(loader, DEFAULT_CONFIG_TEST)){
            reloadConfig(loader, DEFAULT_CONFIG_TEST);
        } else {
            reloadConfig(loader, DEFAULT_CONFIG_PROD);
        }
    }

    /**
     * Reloads logging config from file placed in classpath.
     *
     * @param loader - used to load file content from classpath.
     * @param configPath - path to configuration file within classpath.
     */
    public static void reloadConfig(Class<?> loader, String configPath){
        InputStream res = loader.getResourceAsStream(configPath);
        if (res == null){
            Logger.getGlobal().severe(format("'%s' file was not found in classpath.%n", configPath));
            return;
        }

        try (InputStream conf = res){
            //reload config
            var mng = LogManager.getLogManager();
            mng.updateConfiguration(conf, LogConfigReloader::getReplaceAllMapper);

        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, format("Failed to reload logging config from '%s' file.", configPath), e);
            return;
        }

        Logger.getGlobal().config(format("Logging config was successfully reloaded from '%s' file.", configPath));
    }

    /**
     * This method is used as mapper in {@link LogManager#updateConfiguration(Function)}.
     * And it overrides all old values by new values.
     */
    private static BiFunction<String, String, String> getReplaceAllMapper(String key) {
        return (oldVal, newVal) -> newVal;
    }

    private static boolean isResourcePresent(Class<?> loader, String resourcePath){
        try(var res = loader.getResourceAsStream(resourcePath)){
            return res != null;
        } catch (Exception e) {
            return false;
        }
    }
}
