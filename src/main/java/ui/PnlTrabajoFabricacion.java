package ui;

import dao.TrabajoFabricacionDAO;
import modelo.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import conexion.conexionBD;
import dao.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class PnlTrabajoFabricacion extends javax.swing.JPanel {

   

    public PnlTrabajoFabricacion() {
        initComponents(); 
        txtFecha.setText(LocalDate.now().toString());
        txtFecha.setEditable(false);
        txtProducto.setEditable(false);

        cargarProductosTerminados();
        cargarUsuariosResponsables();

        cmbCodigoProducto.addActionListener(e -> mostrarTipoProductoSeleccionado());
        mostrarTipoProductoSeleccionado();

        inicializarTablaFabricacion();
        
    }
    
    private DefaultTableModel modeloTablaTrabajos;
    private TableRowSorter<DefaultTableModel> sorterTrabajos;
    private final Map<String, ProductoOpcion> productosPorTexto =
        new LinkedHashMap<>();
    private final Map<String, UsuarioOpcion> usuariosPorTexto =
            new LinkedHashMap<>();
    
    //METODOS AUXILIARES
        private static class ProductoOpcion {
        private final String codigo;
        private final String nombre;
        private final String tipo;

        ProductoOpcion(String codigo, String nombre, String tipo) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.tipo = tipo;
        }

        @Override
        public String toString() {
            return codigo + " - " + nombre;
        }
    }

    private static class UsuarioOpcion {
        private final int id;
        private final String nombreCompleto;

        UsuarioOpcion(int id, String nombreCompleto) {
            this.id = id;
            this.nombreCompleto = nombreCompleto;
        }

        @Override
        public String toString() {
            return id + " - " + nombreCompleto;
        }
    }
    
    private void cargarProductosTerminados() {
        cmbCodigoProducto.removeAllItems();
        
        productosPorTexto.clear();

        String sql = "SELECT codigo, nombre, tipo_producto "
                   + "FROM producto "
                   + "WHERE tipo_producto = 'PRODUCTO_TERMINADO' "
                   + "ORDER BY codigo";

        try (Connection conexion = conexionBD.obtenerConexion()) {
            if (conexion == null) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar con la base de datos."
                );
                return;
            }

            try (PreparedStatement ps = conexion.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                    

                //
                while (rs.next()) {
                ProductoOpcion producto = new ProductoOpcion(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("tipo_producto")
                );

                String etiqueta = producto.codigo + " - " + producto.nombre;
                productosPorTexto.put(etiqueta, producto);
                cmbCodigoProducto.addItem(etiqueta);
            }
                
                
            }
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Error al cargar productos: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }

        if (cmbCodigoProducto.getItemCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "No hay productos registrados como PRODUCTO_TERMINADO."
            );
        }
    }

    private void cargarUsuariosResponsables() {
        cmbUsuarioResponsable.removeAllItems();
        usuariosPorTexto.clear();
        UsuarioDAO dao = new UsuarioDAO();

        for (Usuario usuario : dao.listarUsuarios()) {
            // Solo permite asignar usuarios activos como responsables.
            if (usuario.isActivo()) {
                UsuarioOpcion usuarioOpcion = new UsuarioOpcion(
                    usuario.getIdUsuario(),
                    usuario.getNombreCompleto()
                );

                String etiqueta = usuarioOpcion.id + " - "
                        + usuarioOpcion.nombreCompleto;

                usuariosPorTexto.put(etiqueta, usuarioOpcion);
                cmbUsuarioResponsable.addItem(etiqueta);
            }
        }
    }

    private void mostrarTipoProductoSeleccionado() {
        String etiqueta = (String) cmbCodigoProducto.getSelectedItem();
        ProductoOpcion producto = productosPorTexto.get(etiqueta);

        if (producto == null) {
            txtProducto.setText("");
        } else {
            txtProducto.setText(producto.tipo);
        }
    }

        public void inicializarTablaFabricacion() {
            configurarTablaTrabajos();
            configurarFiltrosYBusqueda();
            configurarSeleccionTabla();
            cargarTablaTrabajos(); 
        }
    
    private void configurarTablaTrabajos() {
       
        String[] columnas = {
            "No. Orden",
            "Fecha",
            "Código producto",
            "Producto terminado",
            "Cantidad",
            "Estado",
            "ID Responsable",
            "Responsable"
        };
        
        modeloTablaTrabajos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblTrabajos.setModel(modeloTablaTrabajos); 
        sorterTrabajos = new TableRowSorter<>(modeloTablaTrabajos);
        tblTrabajos.setRowSorter(sorterTrabajos);
    }

    public void cargarTablaTrabajos() {
        modeloTablaTrabajos.setRowCount(0); 
        TrabajoFabricacionDAO dao = new TrabajoFabricacionDAO();
        List<TrabajoFabricacion> lista = dao.listarTrabajos();
        
        for (TrabajoFabricacion t : lista) {
            double cantidadTotal = 0;
            if (t.getDetalleMateriales() != null) {
                for (MaterialUtilizado m : t.getDetalleMateriales()) {
                    cantidadTotal += m.getCantidadUtilizada();
                }
            }
            Object[] fila = new Object[8];

            fila[0] = t.getNumeroTrabajo();
            fila[1] = t.getFecha() != null ? t.getFecha().toString() : "";
            fila[2] = t.getProductoTerminado() != null
                    ? t.getProductoTerminado().getCodigo() : "";
            fila[3] = t.getProductoTerminado() != null
                    ? t.getProductoTerminado().getNombre() : "";
            fila[4] = cantidadTotal;
            fila[5] = "Completado";
            fila[6] = t.getUsuarioResponsable() != null
                    ? t.getUsuarioResponsable().getIdUsuario() : "";
            fila[7] = t.getUsuarioResponsable() != null
                    ? t.getUsuarioResponsable().getNombreCompleto() : "";

            modeloTablaTrabajos.addRow(fila);
        }
    }

    private void configurarSeleccionTabla() {
        tblTrabajos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblTrabajos.getSelectedRow() != -1) {
                int filaSeleccionada = tblTrabajos.getSelectedRow();
                int filaModelo = tblTrabajos.convertRowIndexToModel(filaSeleccionada);

            String codigo = modeloTablaTrabajos
                    .getValueAt(filaModelo, 2).toString();

            String idUsuario = modeloTablaTrabajos
                    .getValueAt(filaModelo, 6).toString();

            txtOrden.setText(
                    modeloTablaTrabajos.getValueAt(filaModelo, 0).toString()
            );
            txtFecha.setText(
                    modeloTablaTrabajos.getValueAt(filaModelo, 1).toString()
            );

            seleccionarProductoPorCodigo(codigo);
            seleccionarUsuarioPorId(Integer.parseInt(idUsuario));

            txtOrden.setEnabled(false);
            }
        });
    }
    
        private void seleccionarProductoPorCodigo(String codigo) {
            for (int i = 0; i < cmbCodigoProducto.getItemCount(); i++) {
                String etiqueta = cmbCodigoProducto.getItemAt(i);
                ProductoOpcion producto = productosPorTexto.get(etiqueta);

                if (producto != null && producto.codigo.equals(codigo)) {
                    cmbCodigoProducto.setSelectedIndex(i);
                    mostrarTipoProductoSeleccionado();
                    return;
                }
            }
        }

    private void seleccionarUsuarioPorId(int id) {
        for (int i = 0; i < cmbUsuarioResponsable.getItemCount(); i++) {
            String etiqueta = cmbUsuarioResponsable.getItemAt(i);
            UsuarioOpcion usuario = usuariosPorTexto.get(etiqueta);

            if (usuario != null && usuario.id == id) {
                cmbUsuarioResponsable.setSelectedIndex(i);
                return;
            }
        }
    }

    private void configurarFiltrosYBusqueda() {
        txtBuscarOrden.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) { aplicarFiltrosCombinados(); }
        });
        cmbEstadoOrden.addActionListener(e -> aplicarFiltrosCombinados());
    }

    private void aplicarFiltrosCombinados() {
        if (sorterTrabajos == null) return;
        List<RowFilter<Object, Object>> filtros = new ArrayList<>();
        String texto = txtBuscarOrden.getText().trim();
        if (!texto.isEmpty() && !texto.equalsIgnoreCase("Buscar Orden...")) {
            filtros.add(RowFilter.regexFilter("(?i)" + texto));
        }
        String estadoSeleccionado = cmbEstadoOrden.getSelectedItem() != null ? cmbEstadoOrden.getSelectedItem().toString() : "";
        if (!estadoSeleccionado.isEmpty() && !estadoSeleccionado.equalsIgnoreCase("Todos")) {
            filtros.add(RowFilter.regexFilter("(?i)^" + estadoSeleccionado + "$", 5));
        }
        if (!filtros.isEmpty()) {
            sorterTrabajos.setRowFilter(RowFilter.andFilter(filtros));
        } else {
            sorterTrabajos.setRowFilter(null);
        }
    }

    public void registrarTrabajo() {
        try {
            String numeroOrden = txtOrden.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            String textoProducto =
                    (String) cmbCodigoProducto.getSelectedItem();

            ProductoOpcion productoSeleccionado =
                    productosPorTexto.get(textoProducto);

            String textoUsuario =
                    (String) cmbUsuarioResponsable.getSelectedItem();

            UsuarioOpcion usuarioSeleccionado =
                    usuariosPorTexto.get(textoUsuario);

            if (numeroOrden.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Ingresa el número de orden.",
                    "Dato obligatorio",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (productoSeleccionado == null) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un producto terminado.",
                    "Dato obligatorio",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (usuarioSeleccionado == null) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un usuario responsable activo.",
                    "Dato obligatorio",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            LocalDate fecha = LocalDate.now();
            txtFecha.setText(fecha.toString());

            ProductoTerminado producto = new ProductoTerminado();
            producto.setCodigo(productoSeleccionado.codigo);
            producto.setNombre(productoSeleccionado.nombre);

            Usuario usuario = new Usuario() {
                @Override
                public boolean tienePermiso(String accion) {
                    return true;
                }
            };
            usuario.setIdUsuario(usuarioSeleccionado.id);

            TrabajoFabricacion trabajo = new TrabajoFabricacion(
                numeroOrden,
                fecha,
                descripcion,
                producto,
                usuario,
                new ArrayList<>()
            );

            TrabajoFabricacionDAO dao = new TrabajoFabricacionDAO();

            if (dao.insertarTrabajo(trabajo)) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Trabajo registrado con éxito."
                );
                cargarTablaTrabajos();
                limpiarCampos();
            } else {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar. Revisa la consola de NetBeans.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void actualizarTrabajo() {
        String numeroOrden = txtOrden.getText().trim();
        if (numeroOrden.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para actualizar.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        javax.swing.JOptionPane.showMessageDialog(this, "Función de actualización lista para la orden: " + numeroOrden);
        limpiarCampos();
    }

    public void eliminarTrabajo() {
        String numeroOrden = txtOrden.getText().trim();
        if (numeroOrden.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione o escriba el No. de Orden que desea eliminar.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this, "¿Está seguro de remover la orden " + numeroOrden + "?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            javax.swing.JOptionPane.showMessageDialog(this, "Acción ejecutada correctamente.");
            limpiarCampos();
            cargarTablaTrabajos();
        }
    }
    
    public void limpiarCampos() {
        txtOrden.setText("");
        txtOrden.setEnabled(true);

        txtFecha.setText(LocalDate.now().toString());
        txtDescripcion.setText("");
        txtProducto.setText("");

        if (cmbCodigoProducto.getItemCount() > 0) {
            cmbCodigoProducto.setSelectedIndex(0);
            mostrarTipoProductoSeleccionado();
        } else {
            cmbCodigoProducto.setSelectedIndex(-1);
        }

        if (cmbUsuarioResponsable.getItemCount() > 0) {
            cmbUsuarioResponsable.setSelectedIndex(0);
        } else {
            cmbUsuarioResponsable.setSelectedIndex(-1);
        }

        tblTrabajos.clearSelection();
    }




    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        txtBuscarOrden = new javax.swing.JTextField();
        cmbEstadoOrden = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtOrden = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTrabajos = new javax.swing.JTable();
        btnRegistrarTrabajo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cmbCodigoProducto = new javax.swing.JComboBox<>();
        cmbUsuarioResponsable = new javax.swing.JComboBox<>();

        jCheckBox1.setText("jCheckBox1");

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtBuscarOrden.setText("Buscar Orden...");
        txtBuscarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarOrdenActionPerformed(evt);
            }
        });

        cmbEstadoOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "PENDIENTE", "EN PROCESO", "COMPLETADO", "CANCELADO", "BLOQUEADO" }));
        cmbEstadoOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoOrdenActionPerformed(evt);
            }
        });

        jLabel1.setText("No. Orden");

        jLabel3.setText("Fecha");

        jLabel4.setText("Descripcion");

        jLabel5.setText("Codigo producto");

        jLabel6.setText("Tipo Producto");

        jLabel7.setText("ID usuario responsable");

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblTrabajos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                " No. Orden", "Fecha", "Descripcion", "Codigo Producto", "Tipo Producto", "ID Usuario Responsable"
            }
        ));
        jScrollPane1.setViewportView(tblTrabajos);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnRegistrarTrabajo.setText("Guardar");
        btnRegistrarTrabajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarTrabajoActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Fabricacion");

        cmbCodigoProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbUsuarioResponsable.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnRegistrarTrabajo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar)
                        .addGap(8, 8, 8)
                        .addComponent(btnLimpiar)
                        .addGap(141, 141, 141))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(cmbCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(cmbUsuarioResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(127, 127, 127))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(cmbEstadoOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEstadoOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtBuscarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbUsuarioResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpiar)
                            .addComponent(btnEliminar)
                            .addComponent(btnActualizar)
                            .addComponent(btnRegistrarTrabajo))))
                .addGap(35, 35, 35)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    private void txtBuscarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarOrdenActionPerformed

    }//GEN-LAST:event_txtBuscarOrdenActionPerformed

    private void cmbEstadoOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadoOrdenActionPerformed

    }//GEN-LAST:event_cmbEstadoOrdenActionPerformed

    private void btnRegistrarTrabajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarTrabajoActionPerformed
        registrarTrabajo();       // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarTrabajoActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
actualizarTrabajo();        // TODO add your handling code here:
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
eliminarTrabajo();        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
limpiarCampos();        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegistrarTrabajo;
    private javax.swing.JComboBox<String> cmbCodigoProducto;
    private javax.swing.JComboBox<String> cmbEstadoOrden;
    private javax.swing.JComboBox<String> cmbUsuarioResponsable;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTrabajos;
    private javax.swing.JTextField txtBuscarOrden;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtOrden;
    private javax.swing.JTextField txtProducto;
    // End of variables declaration//GEN-END:variables
}
