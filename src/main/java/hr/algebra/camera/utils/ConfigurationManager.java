package hr.algebra.camera.utils;

import hr.algebra.camera.exception.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public final class ConfigurationManager {
    private static final String CONFIG_PATH = "/hr/algebra/camera/config/application-config.xml";

    private static int windowWidth;
    private static int windowHeight;
    private static String windowTitle;

    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    private static boolean loaded = false;

    private ConfigurationManager() {
    }

    public static void load(){
        if (loaded) return;
        //TODO Improve exceptions
        try(InputStream inputStream = ConfigurationManager.class.getResourceAsStream(CONFIG_PATH)) {
            if (inputStream == null) throw new ConfigurationException("Config file not found");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            windowWidth = Integer.parseInt(readText(doc, "width"));
            windowHeight = Integer.parseInt(readText(doc, "height"));
            windowTitle = readText(doc, "title");
            dbUrl = readText(doc, "url");
            dbUsername = readText(doc, "username");
            dbPassword = readText(doc, "password");

            loaded = true;
        } catch (ConfigurationException e) {
            throw e;
        }catch (Exception e) {
            throw new ConfigurationException("Failed to read configuration", e);
        }

     }

    private static String readText(Document doc, String tag) {
        NodeList nodes = doc.getElementsByTagName(tag);
        if (nodes.getLength() == 0) {
            throw new ConfigurationException("Missing config element: <" + tag + ">");
        }
        return nodes.item(0).getTextContent().trim();
    }

    public static int getWindowWidth() {
        ensureLoaded();
        return windowWidth;
    }

    public static int getWindowHeight() {
        ensureLoaded();
        return windowHeight;
    }

    public static String getWindowTitle() {
        ensureLoaded();
        return windowTitle;
    }

    public static String getDbUrl() {
        ensureLoaded();
        return dbUrl;
    }

    public static String getDbUsername() {
        ensureLoaded();
        return dbUsername;
    }

    public static String getDbPassword() {
        ensureLoaded();
        return dbPassword;
    }

    private static void ensureLoaded() {
        if (!loaded) {
            throw new ConfigurationException("ConfigurationManager.load() was never called");
        }
    }
}
