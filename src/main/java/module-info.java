module hr.algebra.camera {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.xml;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    requires jakarta.xml.bind;

    opens hr.algebra.camera.model to javafx.base;
    opens hr.algebra.camera to javafx.fxml;
    exports hr.algebra.camera;
    exports hr.algebra.camera.service.interfaces;
    exports hr.algebra.camera.controller;
    opens hr.algebra.camera.controller to javafx.fxml;
    opens hr.algebra.camera.model.dto to jakarta.xml.bind;
}