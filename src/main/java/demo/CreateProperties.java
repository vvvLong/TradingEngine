package demo;

import java.io.*;
import java.util.Properties;

public class CreateProperties {

    private static String path = "src/main/resources/config.properties";

    public static void write() {
        try (OutputStream out = new FileOutputStream(path)) {
            Properties prop = new Properties();
            prop.setProperty("SQL_URL", "jdbc:mysql:///java_practice_trading");
            prop.setProperty("SQL_USER", "root");
            prop.setProperty("SQL_PASSWORD", "long0088");

            prop.store(out, "add sql properties");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        try (InputStream in = new FileInputStream(path)) {
            Properties prop = new Properties();
            prop.load(in);
            prop.forEach((k, v) -> System.out.println(k + ": " + v));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        write();
        read();
    }
}
