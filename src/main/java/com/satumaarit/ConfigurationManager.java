package com.satumaarit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Class to read/write configurations in config.properties
 * @author satu
 */
public class ConfigurationManager {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * @return min_credits from config.properties
     */
    public static int getMinCredits() {
        String value = properties.getProperty("min_credits");
        return Integer.parseInt(value);
    }

    /**
     * @return max_credits from config.properties
     */
    public static int getMaxCredits() {
        String value = properties.getProperty("max_credits");
        return Integer.parseInt(value);
    }

    /**
     * @return min_grade from config.properties
     */
    public static int getMinGrade() {
        String value = properties.getProperty("min_grade");
        return Integer.parseInt(value);
    }

    /**
     * @return max_grade from config.properties
     */
    public static int getMaxGrade() {
        String value = properties.getProperty("max_grade");
        return Integer.parseInt(value);
    }

    /**
     * Check if sample_data_added is set to false or true in config.properties
     * @return boolean
     */
    public static boolean isSampleDataAdded() {
        String value = properties.getProperty("sample_data_added", "false");
        return Boolean.parseBoolean(value);
    }

    /**
     * Set sample_data_added to true in config.properties
     * @param added boolean
     */
    public static void setSampleDataAdded(boolean added) {
        properties.setProperty("sample_data_added", String.valueOf(added));

        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Configuration file");
        } catch (IOException e) {
            System.out.println("Error while saving configuration: " + e.getMessage());
        }
    }
}
