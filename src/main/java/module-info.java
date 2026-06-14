module hr.algebra.camera {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;


    opens hr.algebra.camera to javafx.fxml;
    exports hr.algebra.camera;
    exports hr.algebra.camera.controller;
    opens hr.algebra.camera.controller to javafx.fxml;
}