/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author david
 */
import MainClasses.Archivo;
import MainClasses.Directorio;
import MainClasses.SD;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JOptionPane;

public class FileSystemTree {
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private SD disco;
    private Directorio rootDirectorio;
    private boolean esAdmin;

    // -------------------- INICIALIZACIÓN --------------------
    public FileSystemTree(SD disco) {
        this.disco = disco;
        this.esAdmin = false;
        this.rootDirectorio = new Directorio("Raíz"); // Directorio raíz del sistema
        this.rootNode = new DefaultMutableTreeNode(rootDirectorio);
        this.treeModel = new DefaultTreeModel(rootNode);
    }

    // -------------------- ACTUALIZAR VISTA --------------------
    public void actualizarVistaCompleta() {
        rootNode.removeAllChildren();
        construirNodos(rootNode, rootDirectorio.primerArchivo);
        treeModel.reload();
    }

    private void construirNodos(DefaultMutableTreeNode padreUI, Archivo archivo) {
        while (archivo != null) {
            DefaultMutableTreeNode nodoUI = new DefaultMutableTreeNode(archivo);
            
            if (archivo instanceof Directorio) {
                Directorio dir = (Directorio) archivo;
                construirNodos(nodoUI, dir.primerArchivo);
            }
            
            padreUI.add(nodoUI);
            archivo = archivo.siguiente;
        }
    }

    // -------------------- OPERACIONES CRUD --------------------
    public boolean crearElemento(String nombre, boolean esDirectorio, DefaultMutableTreeNode nodoPadreUI) {
        if (!esAdmin) {
            JOptionPane.showMessageDialog(null, "Modo usuario: Operación no permitida");
            return false;
        }
        
        Directorio dirPadre = obtenerDirectorioDesdeNodo(nodoPadreUI);
        if (dirPadre == null) return false;

        // Validar nombre único
        if (existeNombre(nombre, dirPadre.primerArchivo)) {
            JOptionPane.showMessageDialog(null, "¡El nombre ya existe!");
            return false;
        }

        Archivo nuevo = esDirectorio 
            ? new Directorio(nombre) 
            : crearArchivoConBloques(nombre);

        if (nuevo != null) {
            agregarAestructura(dirPadre, nuevo);
            agregarAarbol(nuevo, nodoPadreUI);
            return true;
        }
        return false;
    }
    
        private void agregarAarbol(Archivo nuevoArchivo, DefaultMutableTreeNode nodoPadreUI) {
        DefaultMutableTreeNode nuevoNodo = new DefaultMutableTreeNode(nuevoArchivo);
        treeModel.insertNodeInto(nuevoNodo, nodoPadreUI, nodoPadreUI.getChildCount());
        
        // Si es directorio, agregar nodo placeholder
        if (nuevoArchivo instanceof Directorio) {
            nuevoNodo.add(new DefaultMutableTreeNode(""));
        }
        
        // Expandir el nodo padre automáticamente
        treeModel.reload(nodoPadreUI);
    }

    private Archivo crearArchivoConBloques(String nombre) {
        int bloquesNecesarios = solicitarTamano();
        if (bloquesNecesarios <= 0) return null;
        
        Archivo archivo = new Archivo(nombre, bloquesNecesarios);
        if (!disco.asignarBloques(archivo, bloquesNecesarios)) {
            JOptionPane.showMessageDialog(null, "¡Espacio insuficiente en el SD!");
            return null;
        }
        return archivo;
    }

    public boolean eliminarElemento(DefaultMutableTreeNode nodoUI) {
        if (!esAdmin) return false;
        
        Archivo objetivo = obtenerArchivoDesdeNodo(nodoUI);
        if (objetivo == null) return false;

        // Eliminar recursivamente si es directorio
        if (objetivo instanceof Directorio) {
            eliminarRecursivo((Directorio) objetivo);
        } else {
            disco.liberarBloques(objetivo);
        }

        // Eliminar de la estructura
        eliminarDeEstructura(objetivo);
        
        // Eliminar del árbol
        treeModel.removeNodeFromParent(nodoUI);
        return true;
    }

    // -------------------- MÉTODOS AUXILIARES --------------------
    private Directorio obtenerDirectorioDesdeNodo(DefaultMutableTreeNode nodoUI) {
        if (nodoUI == null) return rootDirectorio;
        Object obj = nodoUI.getUserObject();
        return (obj instanceof Directorio) ? (Directorio) obj : null;
    }

    private void agregarAestructura(Directorio padre, Archivo nuevo) {
        nuevo.siguiente = padre.primerArchivo;
        padre.primerArchivo = nuevo;
    }

    private void eliminarRecursivo(Directorio dir) {
        Archivo actual = dir.primerArchivo;
        while (actual != null) {
            if (actual instanceof Directorio) {
                eliminarRecursivo((Directorio) actual);
            } else {
                disco.liberarBloques(actual);
            }
            actual = actual.siguiente;
        }
    }

    private boolean existeNombre(String nombre, Archivo primerArchivo) {
        Archivo actual = primerArchivo;
        while (actual != null) {
            if (actual.nombre.equals(nombre)) return true;
            actual = actual.siguiente;
        }
        return false;
    }

    // -------------------- INTERFAZ PÚBLICA --------------------
    public void setModoAdministrador(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    // -------------------- CLASE ADICIONAL (DIÁLOGO) --------------------
    private int solicitarTamano() {
        String input = JOptionPane.showInputDialog("Tamaño en bloques:");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    // Método auxiliar para eliminar de la estructura de datos
private void eliminarDeEstructura(Archivo objetivo) {
    // Buscar en el directorio padre y eliminar el archivo de la lista
    Directorio padre = encontrarPadre(rootDirectorio, objetivo);
    if (padre != null) {
        if (padre.primerArchivo == objetivo) {
            padre.primerArchivo = objetivo.siguiente;
        } else {
            Archivo actual = padre.primerArchivo;
            while (actual.siguiente != null && actual.siguiente != objetivo) {
                actual = actual.siguiente;
            }
            if (actual.siguiente != null) {
                actual.siguiente = objetivo.siguiente;
            }
        }
    }
}

// Método para encontrar el directorio padre de un archivo
private Directorio encontrarPadre(Directorio actual, Archivo buscado) {
    if (actual.primerArchivo == buscado) return actual;
    
    Archivo temp = actual.primerArchivo;
    while (temp != null) {
        if (temp instanceof Directorio) {
            Directorio resultado = encontrarPadre((Directorio) temp, buscado);
            if (resultado != null) return resultado;
        }
        temp = temp.siguiente;
    }
    return null;
}

private Archivo obtenerArchivoDesdeNodo(DefaultMutableTreeNode nodoUI) {
    if (nodoUI == null) return null;
    Object userObject = nodoUI.getUserObject();
    return (userObject instanceof Archivo) ? (Archivo) userObject : null;
}
}
