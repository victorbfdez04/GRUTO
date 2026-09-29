package proyectovbf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private List<Usuario> miembros;
    private List<Pago> pagos;

    public Grupo(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del grupo no puede estar vacío");
        }
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
        this.pagos = new ArrayList<>();
    }

    public void addMiembro(Usuario u) {
        if (!miembros.contains(u)) {
            miembros.add(u);
        }
    }

    public boolean removeMiembro(Usuario u) {
        return miembros.remove(u);
    }

    public void addPago(Pago p) {
        pagos.add(p);
    }

    public boolean removePago(Pago p) {
        return pagos.remove(p);
    }

    public double getTotalGastos() {
        double total = 0;
        for (Pago p : pagos) total += p.getImporte();
        return total;
    }

    //Devuelve cuánto debe pagar cada miembro (división igualitaria)
    public double getCuotaPorPersona() {
        if (miembros.isEmpty()) return 0;
        return getTotalGastos() / miembros.size();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Usuario> getMiembros() { return miembros; }
    public List<Pago> getPagos() { return pagos; }

    @Override
    public String toString() { return nombre; }
}
