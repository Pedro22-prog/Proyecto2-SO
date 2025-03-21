/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import EDD.Lista;
 import com.google.gson.Gson;
 import com.google.gson.GsonBuilder;
 import javax.swing.JFileChooser;
 import java.io.*;
 import MainClasses.ConfiguracionEstado;
 import MainClasses.Archivo;
 import MainClasses.Block;
 import MainClasses.SD;
 import com.google.gson.TypeAdapter;
 import com.google.gson.stream.JsonReader;
 import com.google.gson.stream.JsonToken;
 import com.google.gson.stream.JsonWriter;
 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;
 import javax.swing.table.DefaultTableModel;
 import javax.swing.tree.DefaultMutableTreeNode;
 import javax.swing.tree.DefaultTreeModel;
 import java.awt.Color;
 // En la sección de imports de GUI.java
 import javax.swing.JPanel;
 import javax.swing.BorderFactory;
 import javax.swing.border.Border;
 import java.awt.GridLayout;
 import java.awt.Dimension;
 import java.util.Enumeration;
 import javax.swing.JButton;
 import javax.swing.JOptionPane;

/**
 *
 * @author pedro
 */
public class GUI extends javax.swing.JFrame {

    private DefaultTreeModel modelo;
    private DefaultMutableTreeNode nodoSeleccionado;
    private DefaultTableModel tableModel;
    private SD sd = new SD();
    private JButton btnCrearDirectorio;
    // En la clase GUI:
    private JPanel panelSD; // Reemplazar el JTextArea SD

// En el constructor:
    private void initSDVisual() {
        panelSD = new JPanel(new GridLayout(5, 7, 2, 2)); // 35 bloques en grid 5x7
        panelSD.setPreferredSize(new Dimension(380, 340));
        jScrollPane4.setViewportView(panelSD);
        actualizarSDVisual();
    }
    private void reconstruirListasBloques(ConfiguracionEstado estado) {
    DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo.getRoot();
    Enumeration<?> enumeration = raiz.depthFirstEnumeration();
    
    while (enumeration.hasMoreElements()) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
        Object userObject = node.getUserObject();
        
        if (userObject instanceof Archivo) {
            Archivo archivo = (Archivo) userObject;
            
            if (archivo.tipo == Archivo.Tipo.ARCHIVO) {
                // Buscar la fila correspondiente en la tabla
                for (ConfiguracionEstado.FilaTabla fila : estado.tablaArchivos.toArray(new ConfiguracionEstado.FilaTabla[0])) {
                    if (fila.nombre.equals(archivo.nombre)) {
                        // Obtener el ID del primer bloque
                        int primerBloqueId = Integer.parseInt(fila.primerBloque);
                        
                        // Recorrer la cadena de bloques para reconstruir listaBloques
                        archivo.listaBloques = new Lista<>();
                        Block bloqueActual = sd.getBloques()[primerBloqueId];
                        
                        while (bloqueActual != null) {
                            archivo.listaBloques.insertarAlFinal(bloqueActual.id);
                            bloqueActual = bloqueActual.siguiente;
                        }
                        break;
                    }
                }
            }
        }
    }
}
// Método para actualizar la visualización
    private void actualizarSDVisual() {
        panelSD.removeAll();
        Block[] bloques = sd.getBloques();

        for (Block bloque : bloques) {
            JPanel bloquePanel = new JPanel();
            bloquePanel.setBackground(bloque.color);
            bloquePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            panelSD.add(bloquePanel);
        }

        panelSD.revalidate();
        panelSD.repaint(); // Forzar repintado
    }

    private void crearDirectorio() {
        if (nodoSeleccionado == null) {
            agregarMensaje("Error", "Seleccione un directorio padre");
            return;
        }

        // Obtener el objeto Archivo del nodo seleccionado
        Archivo nodoPadre = (Archivo) nodoSeleccionado.getUserObject();

        // Validar que el nodo padre sea un directorio
        if (nodoPadre.tipo != Archivo.Tipo.DIRECTORIO) {
            agregarMensaje("Error", "No se pueden crear directorios dentro de archivos");
            return;
        }

        String nombre = JOptionPane.showInputDialog("Nombre del directorio:");
        if (nombre != null && !nombre.isEmpty()) {
            Archivo nuevoDir = new Archivo(nombre, Archivo.Tipo.DIRECTORIO);
            DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(nuevoDir);
            modelo.insertNodeInto(nodo, nodoSeleccionado, nodoSeleccionado.getChildCount());
        }

    }

// Modificar el método de creación de archivos:
    // Resto del código original para crear archivos...
    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        JButton btnGuardar = new JButton("Guardar Estado");
        btnGuardar.addActionListener(e -> guardarEstado());
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, 120, 25));

        JButton btnCargar = new JButton("Cargar Estado");
        btnCargar.addActionListener(e -> cargarEstado());
        getContentPane().add(btnCargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 600, 120, 25));
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(
                new Archivo("Raiz", Archivo.Tipo.DIRECTORIO) // Directorio raíz
        );
        lbnum.setText(String.valueOf(sizefile.getValue()));
        modelo = new DefaultTreeModel(raiz);
        arbol.setModel(modelo);
        showMovements.setText("");
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Nombre del archivo");
        tableModel.addColumn("Bloques Asignados");
        tableModel.addColumn("Direccion del primer bloque");
        tableModel.addColumn("Color");
        jTable1.setModel(tableModel);
        jTable1.getColumnModel().getColumn(3).setCellRenderer(new ColorRenderer());
        this.setLocationRelativeTo(null);
        initSDVisual();
        btnCrearDirectorio = new JButton("Nuevo directorio");
        btnCrearDirectorio.addActionListener(e -> crearDirectorio());
        getContentPane().add(btnCrearDirectorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 250, -1, -1));
        // Configurar el listener del JComboBox
        jComboBox1.addActionListener(e -> actualizarVisibilidadBotones());

        // Establecer visibilidad inicial según el modo predeterminado
        actualizarVisibilidadBotones();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        arbol = new javax.swing.JTree();
        jComboBox1 = new javax.swing.JComboBox<>();
        exit = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        showMovements = new javax.swing.JTextArea();
        selectNodo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        sizefile = new javax.swing.JSlider();
        jScrollPane4 = new javax.swing.JScrollPane();
        SD = new javax.swing.JTextArea();
        lbnum = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Root");
        arbol.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        arbol.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                arbolValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(arbol);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 180, 170));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Usuario" }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, -1, -1));

        exit.setBackground(new java.awt.Color(255, 0, 0));
        exit.setText("X");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        getContentPane().add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 10, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 280, 210));

        showMovements.setColumns(20);
        showMovements.setRows(5);
        jScrollPane3.setViewportView(showMovements);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 330, 140));
        getContentPane().add(selectNodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 160, -1));

        jLabel1.setText("Nombre del archivo: ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, -1));

        jLabel2.setText("Espacio del archivo: ");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, -1, -1));

        btnCreate.setText("Crear");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });
        getContentPane().add(btnCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, -1, -1));

        btnDelete.setText("Eliminar");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 150, -1, -1));

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, -1, -1));

        sizefile.setMaximum(35);
        sizefile.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sizefileStateChanged(evt);
            }
        });
        getContentPane().add(sizefile, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, -1, -1));

        SD.setColumns(20);
        SD.setRows(5);
        jScrollPane4.setViewportView(SD);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 290, 380, 340));

        lbnum.setText("jLabel3");
        getContentPane().add(lbnum, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 370, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_exitActionPerformed
    private void guardarEstado() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Color.class, new ColorAdapter())
                        .setPrettyPrinting()
                        .create();

                ConfiguracionEstado estado = new ConfiguracionEstado();
                estado.raiz = serializarNodo((DefaultMutableTreeNode) modelo.getRoot());
                estado.bloquesSD = serializarBloquesSD();
                estado.tablaArchivos = serializarTabla();
                estado.log = showMovements.getText();
                estado.modo = (String) jComboBox1.getSelectedItem();

                gson.toJson(estado, writer);
                agregarMensaje("Sistema", "Estado guardado correctamente");
            } catch (IOException ex) {
                agregarMensaje("Error", "Error al guardar: " + ex.getMessage());
            }
        }
    }

    private void cargarEstado() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileReader reader = new FileReader(fileChooser.getSelectedFile())) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Color.class, new ColorAdapter())
                        .create();

                ConfiguracionEstado estado = gson.fromJson(reader, ConfiguracionEstado.class);
                aplicarEstado(estado);
                agregarMensaje("Sistema", "Estado cargado correctamente");
            } catch (IOException ex) {
                agregarMensaje("Error", "Error al cargar: " + ex.getMessage());
            }
        }
    }
    
    private ConfiguracionEstado.NodoArbol serializarNodo(DefaultMutableTreeNode nodo) {
    Archivo archivo = (Archivo) nodo.getUserObject();
    ConfiguracionEstado.NodoArbol nodoEstado = new ConfiguracionEstado.NodoArbol();
    
    nodoEstado.nombre = archivo.nombre;
    nodoEstado.esDirectorio = (archivo.tipo == Archivo.Tipo.DIRECTORIO);
    nodoEstado.color = archivo.color;
    nodoEstado.tamano = archivo.tamano;
    
    Enumeration<?> hijos = nodo.children();
    while (hijos.hasMoreElements()) {
        DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) hijos.nextElement();
        nodoEstado.hijos.insertarAlFinal(serializarNodo(hijo));
    }
    
    return nodoEstado;
}

private Lista<ConfiguracionEstado.BloqueEstado> serializarBloquesSD() {
    Lista<ConfiguracionEstado.BloqueEstado> lista = new Lista<>();
    for (Block bloque : sd.getBloques()) {
        ConfiguracionEstado.BloqueEstado be = new ConfiguracionEstado.BloqueEstado();
        be.id = bloque.id;
        be.ocupado = bloque.ocupado;
        be.color = bloque.color;
        be.siguienteId = (bloque.siguiente != null) ? bloque.siguiente.id : null;
        lista.insertarAlFinal(be);
    }
    return lista;
}

private Lista<ConfiguracionEstado.FilaTabla> serializarTabla() {
    Lista<ConfiguracionEstado.FilaTabla> lista = new Lista<>();
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        ConfiguracionEstado.FilaTabla ft = new ConfiguracionEstado.FilaTabla();
        ft.nombre = (String) tableModel.getValueAt(i, 0);
        ft.bloques = (int) tableModel.getValueAt(i, 1);
        ft.primerBloque = (String) tableModel.getValueAt(i, 2);
        ft.color = (Color) tableModel.getValueAt(i, 3);
        lista.insertarAlFinal(ft);
    }
    return lista;
}

private void aplicarEstado(ConfiguracionEstado estado) {
    // Limpiar estado actual
    modelo.setRoot(new DefaultMutableTreeNode());
    tableModel.setRowCount(0);
    showMovements.setText("");
    
    // Cargar árbol
    DefaultMutableTreeNode nuevaRaiz = deserializarNodo(estado.raiz);
    modelo.setRoot(nuevaRaiz);
    arbol.setModel(modelo);
    
    // Cargar SD
    Block[] bloques = new Block[35];
    for (ConfiguracionEstado.BloqueEstado be : estado.bloquesSD.toArray(new ConfiguracionEstado.BloqueEstado[0])) {
        Block bloque = new Block(be.id);
        bloque.ocupado = be.ocupado;
        bloque.color = be.color;
        bloques[be.id] = bloque;
    }
    
    // Reconstruir enlaces
    for (ConfiguracionEstado.BloqueEstado be : estado.bloquesSD.toArray(new ConfiguracionEstado.BloqueEstado[0])) {
        if (be.siguienteId != null) {
            bloques[be.id].siguiente = bloques[be.siguienteId];
        }
    }
    sd.setBloques(bloques);
    
    // Cargar tabla
    for (ConfiguracionEstado.FilaTabla ft : estado.tablaArchivos.toArray(new ConfiguracionEstado.FilaTabla[0])) {
        tableModel.addRow(new Object[]{ft.nombre, ft.bloques, ft.primerBloque, ft.color});
    }
    
    reconstruirListasBloques(estado);
    
    // Cargar log y modo
    showMovements.setText(estado.log);
    jComboBox1.setSelectedItem(estado.modo);
    actualizarVisibilidadBotones();
    actualizarSDVisual();
}

private DefaultMutableTreeNode deserializarNodo(ConfiguracionEstado.NodoArbol nodoEstado) {
    Archivo archivo;
    if (nodoEstado.esDirectorio) {
        archivo = new Archivo(nodoEstado.nombre, Archivo.Tipo.DIRECTORIO);
    } else {
        archivo = new Archivo(nodoEstado.nombre, nodoEstado.tamano, nodoEstado.color);
    }
    
    DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(archivo);
    for (ConfiguracionEstado.NodoArbol hijo : nodoEstado.hijos.toArray(new ConfiguracionEstado.NodoArbol[0])) {
        nodo.add(deserializarNodo(hijo));
    }
    
    return nodo;
}

class ColorAdapter extends TypeAdapter<Color> {
    @Override
    public void write(JsonWriter out, Color color) throws IOException {
        if (color == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("r").value(color.getRed());
        out.name("g").value(color.getGreen());
        out.name("b").value(color.getBlue());
        out.endObject();
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        int r = 0, g = 0, b = 0;
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "r": r = in.nextInt(); break;
                case "g": g = in.nextInt(); break;
                case "b": b = in.nextInt(); break;
                default: in.skipValue();
            }
        }
        in.endObject();
        return new Color(r, g, b);
    }
}

    private void actualizarVisibilidadBotones() {
        String modo = (String) jComboBox1.getSelectedItem();

        if (modo.equals("Administrador")) {
            btnCreate.setVisible(true);
            btnUpdate.setVisible(true);
            btnDelete.setVisible(true);
            btnCrearDirectorio.setVisible(true);
        } else if (modo.equals("Usuario")) {
            btnCreate.setVisible(false);
            btnUpdate.setVisible(false);
            btnDelete.setVisible(false);
            btnCrearDirectorio.setVisible(false);
        }
    }
    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // TODO add your handling code here:
        if (nodoSeleccionado == null || ((Archivo) nodoSeleccionado.getUserObject()).tipo != Archivo.Tipo.DIRECTORIO) {
            agregarMensaje("Error", "Seleccione un directorio padre");
            return;
        }
        String nombreArchivo = this.selectNodo.getText();

        if (nombreArchivo.isEmpty()) {
            agregarMensaje("Error", "El nombre del archivo no puede estar vacío");
            return;
        }
        Enumeration<?> hijos = nodoSeleccionado.children();
        while (hijos.hasMoreElements()) {
            DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) hijos.nextElement();
            Archivo elemento = (Archivo) hijo.getUserObject();

            if (elemento.tipo == Archivo.Tipo.ARCHIVO && elemento.nombre.equals(nombreArchivo)) {
                agregarMensaje("Error", "Ya existe un archivo con ese nombre en el directorio");
                return;
            }
        }
        int bloquesNecesarios = sizefile.getValue(); // Obtener valor del slider

        if (bloquesNecesarios <= 0) {
            agregarMensaje("Error", "Tamaño inválido");
            return;
        }

        // Generar color único para el archivo
        Color colorArchivo = new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );

        // Crear nuevo archivo con color
        Archivo nuevoArchivo = new Archivo(nombreArchivo, bloquesNecesarios, colorArchivo);

        if (sd.asignarBloques(nuevoArchivo, bloquesNecesarios)) {
            // Crear nodo con objeto Archivo
            DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(nuevoArchivo);

            if (nodoSeleccionado != null) {
                modelo.insertNodeInto(nodo, nodoSeleccionado, nodoSeleccionado.getChildCount());

                // Agregar a la tabla
                String primerBloque = nuevoArchivo.listaBloques.isEmpty()
                        ? "N/A" : String.valueOf(nuevoArchivo.listaBloques.getpFirst().gettInfo());

                tableModel.addRow(new Object[]{
                    nombreArchivo,
                    bloquesNecesarios,
                    primerBloque,
                    colorArchivo
                });

                actualizarSDVisual();
                agregarMensaje("ADMIN: Creado", nombreArchivo);
            }
        } else {
            agregarMensaje("Error", "Espacio insuficiente");
        }

    }//GEN-LAST:event_btnCreateActionPerformed

    private void arbolValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_arbolValueChanged
        nodoSeleccionado = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
        if (nodoSeleccionado != null) {
            Object userObject = nodoSeleccionado.getUserObject();
            if (userObject instanceof Archivo) {
                Archivo archivo = (Archivo) userObject;
                selectNodo.setText(archivo.nombre);
            }
        }
    }//GEN-LAST:event_arbolValueChanged

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if (nodoSeleccionado != null) {
            Object userObject = nodoSeleccionado.getUserObject();

            if (userObject instanceof Archivo) {
                Archivo elemento = (Archivo) userObject;

                // Caso 1: Es un archivo
                if (elemento.tipo == Archivo.Tipo.ARCHIVO) {
                    // Liberar bloques en el SD
                    sd.liberarBloques(elemento);

                    // Eliminar de la tabla
                    eliminarDeTabla(elemento.nombre);

                    // Eliminar del árbol
                    modelo.removeNodeFromParent(nodoSeleccionado);
                    agregarMensaje("ADMIN: Eliminado", elemento.nombre);
                } // Caso 2: Es un directorio
                else if (elemento.tipo == Archivo.Tipo.DIRECTORIO) {
                    // Eliminar recursivamente el contenido del directorio
                    eliminarDirectorioRecursivo(nodoSeleccionado);

                    // Eliminar el directorio padre del árbol
                    modelo.removeNodeFromParent(nodoSeleccionado);
                    agregarMensaje("ADMIN: Eliminado", elemento.nombre);
                }

                actualizarSDVisual(); // Actualizar la vista del SD
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    // Método auxiliar para eliminar directorios y su contenido
    private void eliminarDirectorioRecursivo(DefaultMutableTreeNode nodoDirectorio) {
        // 1. Recolectar todos los hijos en una Lista personalizada
        Lista<DefaultMutableTreeNode> listaHijos = new Lista<>();
        Enumeration<?> hijosEnum = nodoDirectorio.children();

        while (hijosEnum.hasMoreElements()) {
            DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) hijosEnum.nextElement();
            listaHijos.insertarAlFinal(hijo);
        }

        // 2. Convertir la Lista a array para iterar
        DefaultMutableTreeNode[] hijos = listaHijos.toArray(new DefaultMutableTreeNode[0]);

        // 3. Procesar cada hijo
        for (DefaultMutableTreeNode hijo : hijos) {
            Archivo elementoHijo = (Archivo) hijo.getUserObject();

            if (elementoHijo.tipo == Archivo.Tipo.DIRECTORIO) {
                eliminarDirectorioRecursivo(hijo); // Llamada recursiva para subdirectorios
            } else {
                // Liberar bloques y eliminar de la tabla si es archivo
                sd.liberarBloques(elementoHijo);
                eliminarDeTabla(elementoHijo.nombre);
            }

            // Eliminar el nodo hijo del árbol
            modelo.removeNodeFromParent(hijo);
        }
    }

// Método para eliminar de la tabla
    private void eliminarDeTabla(String nombreArchivo) {
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            if (tableModel.getValueAt(i, 0).equals(nombreArchivo)) {
                tableModel.removeRow(i);
            }
        }
    }
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        if (nodoSeleccionado != null) {
            // Obtener el objeto Archivo/Directorio del nodo
            Archivo elemento = (Archivo) nodoSeleccionado.getUserObject();
            String nombreAnterior = elemento.nombre;
            String nuevoNombre = this.selectNodo.getText().trim();

            if (!nuevoNombre.isEmpty()) {
                // Actualizar nombre en el objeto
                elemento.nombre = nuevoNombre;

                // Actualizar JTree
                modelo.nodeChanged(nodoSeleccionado); // Notificar cambio

                // Actualizar JTable solo si es archivo
                if (elemento.tipo == Archivo.Tipo.ARCHIVO) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (tableModel.getValueAt(i, 0).equals(nombreAnterior)) {
                            tableModel.setValueAt(nuevoNombre, i, 0);
                            break;
                        }
                    }
                }

                agregarMensaje("ADMIN: Actualizado", nombreAnterior + " → " + nuevoNombre);
            }
        } else {
            agregarMensaje("Error", "Seleccione un nodo");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void sizefileStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sizefileStateChanged
    lbnum.setText(String.valueOf(sizefile.getValue()));        // TODO add your handling code here:
    }//GEN-LAST:event_sizefileStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea SD;
    private javax.swing.JTree arbol;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton exit;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbnum;
    private javax.swing.JTextField selectNodo;
    private javax.swing.JTextArea showMovements;
    private javax.swing.JSlider sizefile;
    // End of variables declaration//GEN-END:variables

    private void agregarMensaje(String accion, String nombreNodo) {
        String hora = obtenerHoraActual();
        String mensaje = "[" + hora + "] " + accion + ": " + nombreNodo; // Mensaje para el JTextArea
        showMovements.append(mensaje + "\n"); // Agrega el mensaje al JTextArea
        showMovements.setCaretPosition(showMovements.getDocument().getLength()); // Desplaza el scroll al final
    }

    private void agregarFilaATabla(String nombreArchivo, int bloquesAsignados, String direccionPrimerBloque, Color color) {
        tableModel.addRow(new Object[]{nombreArchivo, bloquesAsignados, direccionPrimerBloque, color});
    }

    private String obtenerHoraActual() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formatter);
    }

    class ColorRenderer extends javax.swing.table.DefaultTableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Color) {
                cell.setBackground((Color) value);
                cell.setForeground((Color) value);
            }
            return cell;
        }
    }
}
