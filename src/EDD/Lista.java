/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 *
 * @author pedro
 */

public class Lista<T> {
    private Nodo<T> pFirst;
    private String name;
    private int id;

    public Lista(String name, int id) {
        this.name = name;
        this.id = id;
        this.pFirst = null;
    }
    public Lista() {
        this.pFirst = null;
    }

    public Nodo<T> getpFirst() {
        return pFirst;
    }

    public void setpFirst(Nodo<T> pFirst) {
        this.pFirst = pFirst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
