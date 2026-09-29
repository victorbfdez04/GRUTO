package proyectovbf.views;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import proyectovbf.model.GestorInfo;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;

    @FXML
    private void onLogin() {
        String usuario = txtUsuario.getText().trim();
        String clave = txtClave.getText().trim();

        if (usuario.isBlank() || clave.isBlank()) {
            mostrarError("Por favor, introduce usuario y contraseña.");
            return;
        }

        GestorInfo gestor = GestorInfo.getInstance();
        if (gestor.login(usuario, clave)) {
            try {
                App.mostrarPrincipal();
            } catch (Exception e) {
                mostrarError("Error al cargar la pantalla principal: " + e.getMessage());
            }
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
            txtClave.clear();
        }
    }

    @FXML
    private void onSalir() {
        System.exit(0);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de acceso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
