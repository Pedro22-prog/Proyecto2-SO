/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;
import java.awt.Color;

/**
 *
 * @author david
 */
public class Block {
    public int id;          // Identificador del bloque (posición en el array)
    public Block siguiente;  // Referencia directa al siguiente bloque (null si es el último)
    public boolean ocupado; // Estado del bloque (true = usado, false = libre)
    public Color color;

    public Block(int id) {
        this.id = id;
        this.siguiente = null;  // Por defecto, no tiene enlace
        this.ocupado = false;
        this.color = Color.LIGHT_GRAY;
}
}
