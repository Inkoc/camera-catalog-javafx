package hr.algebra.camera.utils;

import hr.algebra.camera.auth.SessionManager;
import hr.algebra.camera.event.EventListener;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.model.User;
import javafx.concurrent.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public final class XmlActionLogger implements EventListener {
    //TODO Add to config
    private static final Path FILE =
            Path.of(System.getProperty("user.home"), ".camera-catalog", "action-log.xml");

    private static final XmlActionLogger INSTANCE = new XmlActionLogger();

    private XmlActionLogger() {}

    public static EventListener getInstance() {
        return INSTANCE;
    }


    @Override
    public void onEvent(DataChangedEvent event) {
        User current = SessionManager.getInstance().getCurrentUser();
        String user = (current == null) ? "anonymous" : current.getName();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                append(user, event);
                return null;
            }
        };
        ThreadManager.run(task);
    }

    private synchronized void append(String user, DataChangedEvent event) {
        try {
            Files.createDirectories(FILE.getParent());
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document;

            if (Files.exists(FILE) && Files.size(FILE) > 0) {
                try (java.io.InputStream in = Files.newInputStream(FILE)) {
                    document = builder.parse(in);
                }
            } else {
                document = builder.parse(FILE.toFile());
                document.appendChild(document.createElement("actionLog"));
            }
            Element element = document.createElement("action");
            element.setAttribute("timestamp", LocalDateTime.now().toString());
            element.setAttribute("user", user);
            element.setAttribute("entity", event.getEntityType());
            element.setAttribute("type", event.getAction());
            element.setAttribute("entityId", String.valueOf(event.getEntityId()));
            document.getDocumentElement().appendChild(element);

            //TODO nicer print
            Transformer t = TransformerFactory.newInstance().newTransformer();
            try (java.io.OutputStream out = Files.newOutputStream(FILE)) {
                t.transform(new DOMSource(document), new StreamResult(out));
            }
        } catch (Exception e) {
            System.err.println("Failed to write action log: " + e.getMessage());
        }
    }
}
