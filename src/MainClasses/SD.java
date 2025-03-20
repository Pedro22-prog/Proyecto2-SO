/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

import EDD.Lista;
import EDD.Nodo;
import java.awt.Color;

/**
 *
 * @author david
 */
public class SD {
    private Block[] bloques;
    private static final int TAMANO_SD = 35;

    public SD() {
        bloques = new Block[TAMANO_SD];
        // Inicializar todos los bloques con su ID y estado libre
        for (int i = 0; i < TAMANO_SD; i++) {
            bloques[i] = new Block(i);
        }
    }

    // Método para asignar bloques a un archivo (asignación encadenada)
    public boolean asignarBloques(Archivo archivo, int bloquesNecesarios) {
        if (archivo.tipo != Archivo.Tipo.ARCHIVO) return false;
        if (bloquesDisponibles() < bloquesNecesarios) return false;

        int bloquesAsignados = 0;
        Block bloqueAnterior = null;

        // Buscar bloques libres y encadenarlos
        for (Block bloque : bloques) {
            if (!bloque.ocupado) {
                bloque.ocupado = true;
                
                bloque.color = archivo.color;
                archivo.listaBloques.insertarAlFinal(bloque.id); // Guardar ID en lista del archivo
                
                if (bloqueAnterior != null) {
                    bloqueAnterior.siguiente = bloque; // Enlazar bloques
                }
                bloqueAnterior = bloque;
                bloquesAsignados++;
                
                if (bloquesAsignados == bloquesNecesarios) break;
            }
        }
        return true;
    }

    // Método para liberar bloques de un archivo
    public void liberarBloques(Archivo archivo) {
        if (archivo.tipo != Archivo.Tipo.ARCHIVO) return;
    Nodo<Integer> nodo = archivo.listaBloques.getpFirst();
    while (nodo != null) {
        int idBloque = nodo.gettInfo();
        bloques[idBloque].ocupado = false;
        bloques[idBloque].color = Color.LIGHT_GRAY; // Restablecer color
        bloques[idBloque].siguiente = null; // Eliminar enlace
        nodo = nodo.getpNext();
    }
    archivo.listaBloques.vaciar(); // Limpiar la lista de bloques del archivo
}

    // Método auxiliar: contar bloques disponibles
    public int bloquesDisponibles() {
        int contador = 0;
        for (Block bloque : bloques) {
            if (!bloque.ocupado) contador++;
        }
        return contador;
    }

    // Getter para acceder a los bloques desde la interfaz gráfica
    public Block[] getBloques() {
        return bloques;
    }

    public void setBloques(Block[] bloques) {
        this.bloques = bloques;
    }
}
