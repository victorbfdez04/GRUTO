package proyectovbf.model;

import java.io.Serializable;
import java.util.Random;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int LONGITUD_PWD_DEF = 6;
    private static final int CAR_INICIAL_ALFABETO_CIFRADO = 33;
    private static final int CAR_FINAL_ALFABETO_CIFRADO = 125;

    public static final int PWD_MUY_FUERTE = 1;
    public static final int PWD_FUERTE = 2;
    public static final int PWD_NORMAL = 3;
    public static final int PWD_DEBIL = 4;
    public static final int PWD_MUY_DEBIL = 5;

    private String usuario;
    private String claveCifrada;
    private String email;

    public Usuario(String usuario, String claveClara, String email) {
        if (usuario.isBlank() || claveClara.isBlank() || email.isBlank()) {
            throw new IllegalArgumentException("Datos usuario incompletos");
        }
        this.usuario = usuario;
        this.claveCifrada = encriptar(claveClara, 3);
        this.email = email;
    }

    public Usuario(String usuario) {
        if (usuario.isBlank()) {
            throw new IllegalArgumentException("Usuario vacío");
        }
        this.usuario = usuario;
        String claveAleatoria = generarClave(LONGITUD_PWD_DEF);
        this.claveCifrada = encriptar(claveAleatoria, 3);
        this.email = "Desconocido";
    }

    public Usuario(String usuario, int longitud) {
        if (usuario.isBlank() || longitud <= 0) {
            throw new IllegalArgumentException("Datos incorrectos");
        }
        this.usuario = usuario;
        String claveAleatoria = generarClave(longitud);
        this.claveCifrada = encriptar(claveAleatoria, 3);
        this.email = "Desconocido";
    }

    private static String generarClave(int longitud) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            int codigo = r.nextInt(CAR_FINAL_ALFABETO_CIFRADO - CAR_INICIAL_ALFABETO_CIFRADO + 1)
                    + CAR_INICIAL_ALFABETO_CIFRADO;
            sb.append((char) codigo);
        }
        return sb.toString();
    }

    public static int getNivelFortaleza(String contraseña) {
        int nivel = PWD_MUY_DEBIL;
        int longitud = contraseña.length();
        int cNumeros = 0;
        int cSimbolos = 0;
        boolean hayMay = false;
        boolean hayMin = false;

        if (longitud < LONGITUD_PWD_DEF || contraseña.equals("admin-123") || contraseña.equals("1234")) {
            return nivel;
        }

        for (int i = 0; i < longitud; i++) {
            if (Character.isUpperCase(contraseña.charAt(i))) hayMay = true;
            else if (Character.isLowerCase(contraseña.charAt(i))) hayMin = true;
            else if (Character.isDigit(contraseña.charAt(i))) cNumeros++;
            else cSimbolos++;
        }

        if (hayMay && hayMin) {
            nivel = PWD_DEBIL;
            if (cNumeros >= 1 && longitud >= 8) {
                nivel = PWD_NORMAL;
                if (cSimbolos >= 1) {
                    nivel = PWD_FUERTE;
                    if (cNumeros >= 1 && longitud >= 12) {
                        nivel = PWD_MUY_FUERTE;
                    }
                }
            }
        }
        return nivel;
    }

    public static String encriptar(String claveClara, int despl) {
        StringBuffer cifradaSB = new StringBuffer(claveClara);
        for (int i = 0; i < claveClara.length(); i++) {
            char car = claveClara.charAt(i);
            int codigo = (int) car;
            for (int veces = 0; veces < despl; veces++) {
                if ((codigo + 1) > CAR_FINAL_ALFABETO_CIFRADO) {
                    codigo = CAR_INICIAL_ALFABETO_CIFRADO;
                } else {
                    codigo = codigo + 1;
                }
            }
            cifradaSB.setCharAt(i, (char) codigo);
        }
        return new String(cifradaSB);
    }

    //verificar clave contra la almacenada
    public boolean verificarClave(String claveClara) {
        return this.claveCifrada.equals(encriptar(claveClara, 3));
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getClaveCifrada() { return claveCifrada; }
    public void setClaveCifrada(String claveCifrada) { this.claveCifrada = claveCifrada; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return usuario + " (" + email + ")";
    }
}
