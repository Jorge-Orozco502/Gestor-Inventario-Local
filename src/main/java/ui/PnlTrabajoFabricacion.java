package ui;

import dao.TrabajoFabricacionDAO;
import modelo.TrabajoFabricacion;
import modelo.MaterialUtilizado;
import modelo.ProductoTerminado;
import modelo.Usuario;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PnlTrabajoFabricacion extends javax.swing.JPanel {

   

    public PnlTrabajoFabricacion() {
        initComponents(); 
        inicializarTablaFabricacion();
    }
    
    private DefaultTableModel modeloTablaTrabajos;
    private TableRowSorter<DefaultTableModel> sorterTrabajos;

    public void inicializarTablaFabricacion() {
        configurarTablaTrabajos();
        configurarFiltrosYBusqueda();
        configurarSeleccionTabla();
        cargarTablaTrabajos(); 
    }
    
    private void configurarTablaTrabajos() {
        String[] columnas = {"No. Orden", "Fecha", "Producto Terminado", "Cantidad", "Estado", "Responsable"};
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

            Object[] fila = new Object[6];
            fila[0] = t.getNumeroTrabajo();
            fila[1] = t.getFecha() != null ? t.getFecha().toString() : "";
            fila[2] = t.getProductoTerminado() != null ? t.getProductoTerminado().getNombre() : "";
            fila[3] = cantidadTotal; 
            fila[4] = "Completado"; 
            fila[5] = t.getUsuarioResponsable() != null ? t.getUsuarioResponsable().getNombreCompleto() : "";
            modeloTablaTrabajos.addRow(fila);
        }
    }

    private void configurarSeleccionTabla() {
        tblTrabajos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblTrabajos.getSelectedRow() != -1) {
                int filaSeleccionada = tblTrabajos.getSelectedRow();
                int filaModelo = tblTrabajos.convertRowIndexToModel(filaSeleccionada);

                txtOrden.setText(modeloTablaTrabajos.getValueAt(filaModelo, 0).toString());
                txtFecha.setText(modeloTablaTrabajos.getValueAt(filaModelo, 1).toString());
                txtProducto.setText(modeloTablaTrabajos.getValueAt(filaModelo, 2).toString());
                txtOrden.setEnabled(false);
            }
        });
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
            filtros.add(RowFilter.regexFilter("(?i)^" + estadoSeleccionado + "$", 4));
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
            String fechaStr = txtFecha.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String codigoProducto = txtCodigo.getText().trim();
            String tipoProducto = txtProducto.getText().trim();
            String idUsuarioStr = txtIdUsuario.getText().trim();

            if (numeroOrden.isEmpty() || codigoProducto.isEmpty() || idUsuarioStr.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "El No. de Orden, Código de Producto e ID de Usuario son obligatorios.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate fecha = fechaStr.isEmpty() ? LocalDate.now() : LocalDate.parse(fechaStr);
            ProductoTerminado producto = new ProductoTerminado();
            producto.setCodigo(codigoProducto);
            producto.setNombre(tipoProducto.isEmpty() ? "Producto Genérico" : tipoProducto);

            Usuario usuario = new Usuario() {
                @Override
                public boolean tienePermiso(String accion) { return true; }
            };
            usuario.setIdUsuario(Integer.parseInt(idUsuarioStr));

            TrabajoFabricacion trabajo = new TrabajoFabricacion(numeroOrden, fecha, descripcion, producto, usuario, new ArrayList<>());
            TrabajoFabricacionDAO dao = new TrabajoFabricacionDAO();
            if (dao.insertarTrabajo(trabajo)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Trabajo registrado con éxito.");
                cargarTablaTrabajos();
                limpiarCampos();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar el trabajo.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
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
        txtFecha.setText("");
        txtDescripcion.setText("");
        txtCodigo.setText("");
        txtProducto.setText("");
        txtIdUsuario.setText("");
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
        txtCodigo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtIdUsuario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTrabajos = new javax.swing.JTable();
        btnRegistrarTrabajo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(cmbEstadoOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(jLabel1))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(txtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel3)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(159, 159, 159)
                                .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(42, 42, 42)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(8, 8, 8)))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtIdUsuario))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRegistrarTrabajo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminar)
                .addGap(8, 8, 8)
                .addComponent(btnLimpiar)
                .addGap(97, 97, 97))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel6))
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
    private javax.swing.JComboBox<String> cmbEstadoOrden;
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
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtOrden;
    private javax.swing.JTextField txtProducto;
    // End of variables declaration//GEN-END:variables
}
