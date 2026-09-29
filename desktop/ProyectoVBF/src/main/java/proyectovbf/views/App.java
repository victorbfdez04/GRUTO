package proyectovbf.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import proyectovbf.model.GestorInfo;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        mostrarLogin();
    }

    //cierre aplicacion

    @Override
    public void stop() {
        GestorInfo.getInstance().guardarTodo();
    }

    //ventana login

    public static void mostrarLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/proyectovbf/views/login.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("GRUTO - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //ventana principal

    public static void mostrarPrincipal() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/proyectovbf/views/principal.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root, 700, 550);

        primaryStage.setTitle("GRUTO - Gestión de Gastos Compartidos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //getters

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    //main

    public static void main(String[] args) {
        launch(args);
    }
}