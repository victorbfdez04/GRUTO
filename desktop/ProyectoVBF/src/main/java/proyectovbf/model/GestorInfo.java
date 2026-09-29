package proyectovbf.model;

import proyectovbf.persistence.EscritorDat;
import proyectovbf.persistence.LectorDat;

import java.util.ArrayList;
import java.util.List;

public class GestorInfo {

    public static final String FICHERO_DATOS = "datos.dat";
    private static GestorInfo instancia;

    private List<Usuario> usuarios;
    private List<Grupo> grupos;

    private Usuario usuarioActual;
    private Grupo grupoActual;

    private GestorInfo() {
        usuarios = new ArrayList<>();
        grupos = new ArrayList<>();
        cargarDatos();

        if (usuarios.isEmpty()) {
            usuarios.add(new Usuario("admin", "admin123", "admin@gruto.app"));
            guardarTodo();
        }
    }

    public static GestorInfo getInstance() {
        if (instancia == null) {
            instancia = new GestorInfo();
        }
        return instancia;
    }

    //persistencia

    private void cargarDatos() {
        new LectorDat(FICHERO_DATOS).cargar(usuarios, grupos);
    }

    public void guardarTodo() {
        new EscritorDat(FICHERO_DATOS).guardar(usuarios, grupos);
    }

    //login

    public boolean login(String nombre, String clave) {
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(nombre) && u.verificarClave(clave)) {
                usuarioActual = u;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        usuarioActual = null;
        grupoActual = null;
    }

    //usuarios

    public void addUsuario(Usuario u) {
        for (Usuario existing : usuarios) {
            if (existing.getUsuario().equals(u.getUsuario())) {
                throw new IllegalArgumentException("Ya existe un usuario con ese nombre");
            }
        }

        usuarios.add(u);
        guardarTodo();
    }

    public boolean removeUsuario(String nombre) {
        boolean eliminado = usuarios.removeIf(u -> u.getUsuario().equals(nombre));

        if (eliminado) {
            guardarTodo();
        }

        return eliminado;
    }

    public Usuario buscarUsuario(String nombre) {
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(nombre)) {
                return u;
            }
        }
        return null;
    }

    //grupos

    public void addGrupo(Grupo g) {
        grupos.add(g);
        guardarTodo();
    }

    public Grupo buscarGrupo(String nombre) {
        for (Grupo g : grupos) {
            if (g.getNombre().equals(nombre)) {
                return g;
            }
        }
        return null;
    }

    //pagos

    public void addPago(Pago p) {
        if (grupoActual == null) {
            throw new IllegalStateException("No hay grupo activo");
        }

        grupoActual.addPago(p);
        guardarTodo();
    }

    //getters

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Grupo getGrupoActual() {
        return grupoActual;
    }

    public void setGrupoActual(Grupo g) {
        this.grupoActual = g;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }
}