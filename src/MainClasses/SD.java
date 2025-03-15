/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

import EDD.Lista;
import EDD.Nodo;

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
        if (bloquesDisponibles() < bloquesNecesarios) return false;

        int bloquesAsignados = 0;
        Block bloqueAnterior = null;

        // Buscar bloques libres y encadenarlos
        for (Block bloque : bloques) {
            if (!bloque.ocupado) {
                bloque.ocupado = true;
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
        Nodo<Integer> nodoBloque = archivo.listaBloques.getpFirst();
        while (nodoBloque != null) {
            int idBloque = nodoBloque.gettInfo();
            bloques[idBloque].ocupado = false;
            bloques[idBloque].siguiente = null; // Eliminar enlace
            nodoBloque = nodoBloque.getpNext();
        }
        archivo.listaBloques.setpFirst(null); // Vaciar lista del archivo
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
}
