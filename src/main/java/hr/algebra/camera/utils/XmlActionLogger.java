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
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class XmlActionLogger implements EventListener {
    private static final Logger LOGGER = Logger.getLogger(XmlActionLogger.class.getName());

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
            Files.createDirectories(AppPaths.ACTION_LOG.getParent());
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document;

            if (Files.exists(AppPaths.ACTION_LOG) && Files.size(AppPaths.ACTION_LOG) > 0) {
                try (java.io.InputStream in = Files.newInputStream(AppPaths.ACTION_LOG)) {
                    document = builder.parse(in);
                }
            } else {
                document = builder.newDocument();
                document.appendChild(document.createElement("actionLog"));
            }
            Element element = document.createElement("action");
            element.setAttribute("timestamp", LocalDateTime.now().toString());
            element.setAttribute("user", user);
            element.setAttribute("entity", event.getEntityType().name());
            element.setAttribute("type", event.getAction().name());
            element.setAttribute("entityId", String.valueOf(event.getEntityId()));
            document.getDocumentElement().appendChild(element);

            Transformer t = TransformerFactory.newInstance().newTransformer();
            try (java.io.OutputStream out = Files.newOutputStream(AppPaths.ACTION_LOG)) {
                t.transform(new DOMSource(document), new StreamResult(out));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to write action log: ", e);
        }
    }
}
