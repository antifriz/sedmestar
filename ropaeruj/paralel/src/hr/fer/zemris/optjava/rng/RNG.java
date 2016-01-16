package hr.fer.zemris.optjava.rng;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by ivan on 1/16/16.
 */
public class RNG {
    private static IRNGProvider rngProvider;
    public static Class<IRNG> rngImpl;

    static {
        Properties properties = new Properties();
        ClassLoader classLoader = RNG.class.getClassLoader();
        try {
            properties.load(classLoader.getResourceAsStream("rng-config.properties"));
            rngImpl = (Class<IRNG>) classLoader.loadClass(properties.getProperty("rng-impl"));
            rngProvider = (IRNGProvider) classLoader.loadClass(properties.getProperty("rng-provider")).newInstance();
        } catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }

    public static IRNG getIRNGInstance() {
        try {
            return rngImpl.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
}