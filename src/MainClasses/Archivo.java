/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;
import java.awt.Color;
import EDD.Lista;

/**
 *
 * @author david
 */
public class Archivo {
    public String nombre;
    public int tamano; // Número de bloques asignados
    public Lista listaBloques; // Lista enlazada de bloques del archivo
    public Archivo siguiente; // Referencia al siguiente archivo en la lista
    public Color color;

    public Archivo(String nombre, int tamano, Color color) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.listaBloques = new Lista(); // Inicializar lista de bloques
        this.siguiente = null; // Por defecto, no tiene enlace con otro archivo
        this.color = color;
    }
    public String toString() {
    return this.nombre; // Retorna el nombre para mostrar en el JTree
}


}
