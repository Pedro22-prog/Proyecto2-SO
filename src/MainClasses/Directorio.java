/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

/**
 *
 * @author david
 */
public class Directorio extends Archivo {
    public Archivo primerArchivo;

    public Directorio(String nombre) {
        super(nombre, 0); // Los directorios no ocupan bloques
        this.primerArchivo = null;
    }

    public void agregarArchivo(Archivo archivo) {
        archivo.siguiente = primerArchivo;
        primerArchivo = archivo;
    }
}
