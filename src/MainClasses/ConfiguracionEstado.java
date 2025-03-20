// Archivo: ConfiguracionEstado.java
package MainClasses;

import EDD.Lista;
import java.awt.Color;


public class ConfiguracionEstado {
    public NodoArbol raiz;
    public Lista<BloqueEstado> bloquesSD = new Lista<>();
    public Lista<FilaTabla> tablaArchivos = new Lista<>();
    public String modoUsuario;

    public static class NodoArbol {
        public String nombre;
        public boolean esDirectorio;
        public Lista<NodoArbol> hijos = new Lista<>();
        public Color color;
        public int tamano;
    }

    public static class BloqueEstado {
        public int id;
        public boolean ocupado;
        public Color color;
        public Integer siguienteId;
    }

    public static class FilaTabla {
        public String nombre;
        public int bloques;
        public String primerBloque;
        public Color color;
    }
}