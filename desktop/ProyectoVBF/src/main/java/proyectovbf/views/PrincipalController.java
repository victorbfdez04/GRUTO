package proyectovbf.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import proyectovbf.model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {

    @FXML private Label lblUsuarioActual;
    @FXML private Label lblGrupoActual;

    @FXML private TextField txtNombreGrupo;
    @FXML private ComboBox<String> cboGrupos;

    @FXML private TextField txtNuevoMiembro;
    @FXML private ListView<String> lstMiembros;

    @FXML private TextField txtConceptoPago;
    @FXML private TextField txtImportePago;
    @FXML private ListView<String> lstPagos;

    @FXML private TextField txtNuevoUsuario;
    @FXML private TextField txtEmailNuevo;
    @FXML private PasswordField txtClaveNuevo;
    @FXML private ListView<String> lstUsuarios;

    @FXML private TextArea txtResultado;

    private GestorInfo gestor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gestor = GestorInfo.getInstance();

        lblUsuarioActual.setText(
                "Usuario: " + gestor.getUsuarioActual().getUsuario()
        );

        actualizarEtiquetaGrupo();

        refreshGrupos();
        refreshUsuarios();
    }

    //menu

    @FXML
    private void onMenuLogin() {

        gestor.logout();

        try {
            App.mostrarLogin();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onMenuSalir() {
        gestor.guardarTodo();
        System.exit(0);
    }

    //grupos

    @FXML
    private void onCrearGrupo() {

        String nombre = txtNombreGrupo.getText().trim();

        if (nombre.isBlank()) return;
        if (gestor.buscarGrupo(nombre) != null) return;

        Grupo g = new Grupo(nombre);

        g.addMiembro(gestor.getUsuarioActual());

        gestor.addGrupo(g);
        gestor.setGrupoActual(g);

        gestor.guardarTodo();

        txtNombreGrupo.clear();

        refreshGrupos();
        actualizarEtiquetaGrupo();
    }

    @FXML
    private void onSeleccionarGrupo() {

        String nombre = cboGrupos.getValue();

        if (nombre == null) return;

        Grupo g = gestor.buscarGrupo(nombre);

        if (g == null) return;

        gestor.setGrupoActual(g);

        actualizarEtiquetaGrupo();

        refreshMiembros();
        refreshPagos();
    }

    //miembros

    @FXML
    private void onAgregarMiembro() {

        Grupo g = gestor.getGrupoActual();

        if (g == null) return;

        Usuario u = gestor.buscarUsuario(
                txtNuevoMiembro.getText().trim()
        );

        if (u == null) return;

        g.addMiembro(u);

        gestor.guardarTodo();

        refreshMiembros();
    }

    @FXML
    private void onEliminarMiembro() {

        Grupo g = gestor.getGrupoActual();

        if (g == null) return;

        String sel = lstMiembros
                .getSelectionModel()
                .getSelectedItem();

        if (sel == null) return;

        Usuario u = gestor.buscarUsuario(sel);

        if (u != null) {

            g.removeMiembro(u);

            gestor.guardarTodo();

            refreshMiembros();
        }
    }

    //pagos

    @FXML
    private void onAgregarPago() {

        Grupo g = gestor.getGrupoActual();

        if (g == null) return;

        try {

            double importe = Double.parseDouble(
                    txtImportePago.getText().replace(",", ".")
            );

            Pago p = new Pago(
                    txtConceptoPago.getText(),
                    importe,
                    gestor.getUsuarioActual(),
                    g.getNombre()
            );

            g.addPago(p);

            gestor.guardarTodo();

            txtConceptoPago.clear();
            txtImportePago.clear();

            refreshPagos();

        } catch (Exception e) {
            mostrarError("Importe inválido");
        }
    }

    @FXML
    private void onEliminarPago() {

        Grupo g = gestor.getGrupoActual();

        if (g == null) return;

        String sel = lstPagos
                .getSelectionModel()
                .getSelectedItem();

        if (sel == null) return;

        g.getPagos().removeIf(
                p -> p.toString().equals(sel)
        );

        gestor.guardarTodo();

        refreshPagos();
    }

    //finalizar

    @FXML
    private void onFinalizar() {

        Grupo g = gestor.getGrupoActual();

        if (g == null || g.getPagos().isEmpty()) {

            mostrarError(
                    "No hay datos suficientes para calcular el reparto."
            );

            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("====================================\n");
        sb.append("      REPARTO DE GASTOS\n");
        sb.append("====================================\n");

        sb.append("Grupo: ")
          .append(g.getNombre())
          .append("\n\n");

        int n = g.getMiembros().size();

        double cuota = g.getCuotaPorPersona();

        double[] saldo = new double[n];
        String[] nombres = new String[n];

        //calcular saldos

        for (int i = 0; i < n; i++) {

            Usuario u = g.getMiembros().get(i);

            nombres[i] = u.getUsuario();

            double pagado = 0;

            for (Pago p : g.getPagos()) {

                if (p.getPagador()
                     .getUsuario()
                     .equals(u.getUsuario())) {

                    pagado += p.getImporte();
                }
            }

            saldo[i] = pagado - cuota;
        }

        //balance individual

        sb.append("---- BALANCE INDIVIDUAL ----\n");

        for (int i = 0; i < n; i++) {

            sb.append(nombres[i]).append(": ");

            if (saldo[i] > 0) {

                sb.append("le deben ")
                  .append(String.format("%.2f", saldo[i]))
                  .append(" €\n");

            } else if (saldo[i] < 0) {

                sb.append("debe ")
                  .append(String.format("%.2f", Math.abs(saldo[i])))
                  .append(" €\n");

            } else {

                sb.append("está en paz\n");
            }
        }

        sb.append("\n---- TRANSFERENCIAS NECESARIAS ----\n");

        //reparto real pagos

        for (int i = 0; i < n; i++) {

            if (saldo[i] >= 0) continue;

            for (int j = 0; j < n; j++) {

                if (saldo[j] <= 0) continue;
                if (saldo[i] == 0) break;

                double pago = Math.min(
                        -saldo[i],
                        saldo[j]
                );

                if (pago > 0) {

                    sb.append(nombres[i])
                      .append(" paga ")
                      .append(String.format("%.2f", pago))
                      .append(" € a ")
                      .append(nombres[j])
                      .append("\n");

                    saldo[i] += pago;
                    saldo[j] -= pago;
                }
            }
        }

        sb.append("\n====================================\n");

        txtResultado.setText(sb.toString());
    }

    //usuarios

    @FXML
    private void onAgregarUsuario() {

        String nombre = txtNuevoUsuario.getText().trim();

        String email = txtEmailNuevo.getText().trim();

        String clave = txtClaveNuevo.getText().trim();

        if (nombre.isBlank() || clave.isBlank()) return;

        Usuario u = email.isBlank()

                ? new Usuario(nombre, clave, "sin-email")

                : new Usuario(nombre, clave, email);

        gestor.addUsuario(u);

        refreshUsuarios();

        gestor.guardarTodo();
    }

    @FXML
    private void onEliminarUsuario() {

        String sel = lstUsuarios
                .getSelectionModel()
                .getSelectedItem();

        if (sel == null) return;

        gestor.removeUsuario(sel);

        gestor.guardarTodo();

        refreshUsuarios();
    }

    //refresh

    private void refreshGrupos() {

        ObservableList<String> list =
                FXCollections.observableArrayList();

        for (Grupo g : gestor.getGrupos()) {
            list.add(g.getNombre());
        }

        cboGrupos.setItems(list);
    }

    private void refreshMiembros() {

        Grupo g = gestor.getGrupoActual();

        if (g == null) return;

        ObservableList<String> list =
                FXCollections.observableArrayList();

        for (Usuario u : g.getMiembros()) {
            list.add(u.getUsuario());
        }

        lstMiembros.setItems(list);
    }

    private void refreshPagos() {

        Grupo g = gestor.getGrupoActual();

        if (g == null) return;

        ObservableList<String> list =
                FXCollections.observableArrayList();

        for (Pago p : g.getPagos()) {
            list.add(p.toString());
        }

        lstPagos.setItems(list);
    }

    private void refreshUsuarios() {

        ObservableList<String> list =
                FXCollections.observableArrayList();

        for (Usuario u : gestor.getUsuarios()) {
            list.add(u.getUsuario());
        }

        lstUsuarios.setItems(list);
    }

    private void actualizarEtiquetaGrupo() {

        Grupo g = gestor.getGrupoActual();

        lblGrupoActual.setText(
                "Grupo: " +
                (g != null ? g.getNombre() : "(ninguno)")
        );
    }

    private void mostrarError(String msg) {

        Alert a = new Alert(Alert.AlertType.ERROR);

        a.setContentText(msg);

        a.showAndWait();
    }
}