package proyectovbf.persistence;

import proyectovbf.model.Grupo;
import proyectovbf.model.Usuario;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class LectorDat {

    private String filename;

    public LectorDat(String filename) {
        this.filename = filename;
    }

    public void cargar(List<Usuario> usuarios, List<Grupo> grupos) {
        usuarios.clear();
        grupos.clear();
        try (ObjectInputStream fich =
                     new ObjectInputStream(new FileInputStream(filename))) {
            while (true) {
                Object o = fich.readObject();
                if (o instanceof Usuario u) {
                    usuarios.add(u);
                } else if (o instanceof Grupo g) {
                    grupos.add(g);
                }
            }
        } catch (EOFException eof) {
            System.out.println("FIN DEL FICHERO");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERROR CARGA " + e);
        }
    }
}