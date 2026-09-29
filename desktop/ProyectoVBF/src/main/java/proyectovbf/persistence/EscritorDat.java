package proyectovbf.persistence;

import proyectovbf.model.Grupo;
import proyectovbf.model.Usuario;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class EscritorDat {

    private String filename;

    public EscritorDat(String filename) {
        this.filename = filename;
    }

    public void guardar(List<Usuario> usuarios, List<Grupo> grupos) {
        try (ObjectOutputStream fich =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            for (Usuario u : usuarios) {
                fich.writeObject(u);
            }
            for (Grupo g : grupos) {
                fich.writeObject(g);
            }
        } catch (IOException io) {
            System.out.println("ERROR E/S " + io);
        }
    }
}