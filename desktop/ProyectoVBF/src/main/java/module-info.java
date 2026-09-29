module proyectovbf {
    requires javafx.controls;
    requires javafx.fxml;
    opens proyectovbf.views to javafx.fxml;
    exports proyectovbf.views;
}