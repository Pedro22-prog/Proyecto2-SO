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
    
    // Create - Insertar elementos
    public void insertarAlInicio(T elem) {
        Nodo<T> nuevoNodo = new Nodo<>(elem);
        nuevoNodo.setpNext(pFirst);
        pFirst = nuevoNodo;
    }

    public void insertarAlFinal(T elem) {
        Nodo<T> nuevoNodo = new Nodo<>(elem);
        if (pFirst == null) {
            pFirst = nuevoNodo;
        } else {
            Nodo<T> actual = pFirst;
            while (actual.getpNext() != null) {
                actual = actual.getpNext();
            }
            actual.setpNext(nuevoNodo);
        }
    }
    public boolean isEmpty() {
    return pFirst == null;
}
    public void vaciar(){
        pFirst=null;
    }
    public T[] toArray() {
        Object[] array = new Object[size()];
        Nodo<T> actual = pFirst;
        int i = 0;
        while (actual != null) {
            array[i++] = actual.gettInfo();
            actual = actual.getpNext();
        }
        return (T[]) array;
    }
    public int size() {
        int count = 0;
        Nodo<T> actual = pFirst;
        while (actual != null) {
            count++;
            actual = actual.getpNext();
        }
        return count;
    }
    public T[] toArray(T[] array) {
    if (array.length < size()) {
        // Crear nuevo array del tipo correcto si es necesario
        array = (T[]) java.lang.reflect.Array.newInstance(
            array.getClass().getComponentType(), 
            size()
        );
    }
    
    int i = 0;
    Nodo<T> actual = pFirst;
    while (actual != null && i < array.length) {
        array[i] = actual.gettInfo();
        actual = actual.getpNext();
        i++;
    }
    
    return array;
}
}
