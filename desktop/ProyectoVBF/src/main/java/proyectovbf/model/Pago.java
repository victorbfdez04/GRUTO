package proyectovbf.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;

    private int id;
    private String concepto;
    private double importe;
    private LocalDate fecha;
    private Usuario pagador;
    private String grupo;

    public Pago(String concepto, double importe, Usuario pagador, String grupo) {

        if (concepto == null || concepto.isBlank()) {
            throw new IllegalArgumentException("El concepto no puede estar vacío");
        }

        if (importe <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que 0");
        }

        this.id = contadorId++;
        this.concepto = concepto;
        this.importe = importe;
        this.pagador = pagador;
        this.grupo = grupo;
        this.fecha = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    //Permite al lector CSV ajustar el contador tras cargar pagos persistidos.
    public static void setContadorId(int valor) {
        if (valor > contadorId) {
            contadorId = valor;
        }
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Usuario getPagador() {
        return pagador;
    }

    public void setPagador(Usuario pagador) {
        this.pagador = pagador;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    @Override
    public String toString() {
        return String.format(
                "[%d] %s - %.2f€  (pagado por %s el %s)",
                id, concepto, importe, pagador.getUsuario(), fecha
        );
    }
}