/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

/**
 *
 * @author david
 */
public class Directorio {
    public Archivo primerArchivo;
    private String nombre;

    public Directorio(String nombre) {
        this.nombre=nombre; // Los directorios no ocupan bloques
        this.primerArchivo = null;
    }

    public void agregarArchivo(Archivo archivo) {
        archivo.siguiente = primerArchivo;
        primerArchivo = archivo;
    }
}
