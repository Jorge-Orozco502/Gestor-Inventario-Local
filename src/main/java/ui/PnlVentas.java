package ui;

public class PnlVentas extends javax.swing.JPanel {

    // Instancias DAO para acceso a la base de datos
    private final dao.VentaDAO ventaDAO = new dao.VentaDAO();
    private final dao.ProductoDAO productoDAO = new dao.ProductoDAO();
    private final dao.ClienteDAO clienteDAO = new dao.ClienteDAO();
    private final dao.UsuarioDAO usuarioDAO = new dao.UsuarioDAO();

    private modelo.Usuario usuarioActual;

    public PnlVentas() {
        this(null);
    }

    public PnlVentas(modelo.Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        initComponents();
        configurarTablaDetalle();
        cargarDatosDesdeBD();
    }

    //metodo para el comprobante de la venta hecha al cliente
    private void configurarTablaDetalle() {
        javax.swing.table.DefaultTableModel modeloTabla
                = new javax.swing.table.DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                            "Código producto",
                            "Cantidad",
                            "Precio unitario",
                            "Subtotal",
                            "IVA",
                            "Total"
                        }
                ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tblDetalleVenta.setModel(modeloTabla);
    }

    //METODO PARA APLICAR EL DESCUENTO AL PRODUCTO
    private void aplicarDescuentoSeleccionado() {
        int filaVista = tblDetalleVenta.getSelectedRow();

        if (filaVista < 0) {
            javax.swing.JOptionPane.showMessageDialog(
                    this, "Selecciona el producto al que aplicarás el descuento."
            );
            return;
        }

        try {
            java.math.BigDecimal porcentaje
                    = new java.math.BigDecimal(jTextField2.getText().trim());

            if (porcentaje.compareTo(java.math.BigDecimal.ZERO) < 0
                    || porcentaje.compareTo(new java.math.BigDecimal("100")) > 0) {
                throw new NumberFormatException();
            }

            int fila = tblDetalleVenta.convertRowIndexToModel(filaVista);
            javax.swing.table.DefaultTableModel model
                    = (javax.swing.table.DefaultTableModel) tblDetalleVenta.getModel();

            java.math.BigDecimal precio
                    = new java.math.BigDecimal(model.getValueAt(fila, 2).toString());
            java.math.BigDecimal cantidad
                    = new java.math.BigDecimal(model.getValueAt(fila, 1).toString());

            // El precio unitario incluye IVA.
            java.math.BigDecimal totalOriginal = precio.multiply(cantidad)
                    .setScale(2, java.math.RoundingMode.HALF_UP);

            java.math.BigDecimal descuento = totalOriginal
                    .multiply(porcentaje)
                    .divide(new java.math.BigDecimal("100"), 2,
                            java.math.RoundingMode.HALF_UP);

            java.math.BigDecimal totalConDescuento = totalOriginal.subtract(descuento);
            java.math.BigDecimal subtotalConDescuento = totalConDescuento
                    .divide(new java.math.BigDecimal("1.12"), 2,
                            java.math.RoundingMode.HALF_UP);
            java.math.BigDecimal ivaConDescuento
                    = totalConDescuento.subtract(subtotalConDescuento);

            model.setValueAt(subtotalConDescuento, fila, 3);
            model.setValueAt(ivaConDescuento, fila, 4);
            model.setValueAt(totalConDescuento, fila, 5);

            calcularTotalesYMontos();

        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    this, "Escribe un porcentaje válido entre 0 y 100."
            );
        }
    }

    // Cargar combos y fecha actual desde MySQL
    private void cargarDatosDesdeBD() {
        txtFecha.setText(java.time.LocalDate.now().toString());

        // Cargar Clientes
        cbCodigoCliente.removeAllItems();
        java.util.List<modelo.Cliente> listaClientes = clienteDAO.listarClientes();
        for (modelo.Cliente c : listaClientes) {
            cbCodigoCliente.addItem(c.getCodigoCliente());
        }

        // Empleado: solo puede registrar ventas a su propio nombre.
        // Si usuarioActual es null, conserva la lista para el flujo
        // que use el constructor sin parámetros.
        jComboBox3.removeAllItems();

        if (usuarioActual != null) {
            jComboBox3.addItem(usuarioActual.getNombreUsuario());
            jComboBox3.setSelectedIndex(0);
            jComboBox3.setEnabled(false);
        } else {
            java.util.List<modelo.Usuario> listaUsuarios
                    = usuarioDAO.listarUsuarios();

            for (modelo.Usuario u : listaUsuarios) {
                jComboBox3.addItem(u.getNombreUsuario());
            }
        }
    }

    // Método auxiliar para calcular y refrescar totales
    private void calcularTotalesYMontos() {
        try {
            javax.swing.table.DefaultTableModel model
                    = (javax.swing.table.DefaultTableModel) tblDetalleVenta.getModel();

            java.math.BigDecimal subtotalAcumulado = java.math.BigDecimal.ZERO;
            java.math.BigDecimal ivaAcumulado = java.math.BigDecimal.ZERO;
            java.math.BigDecimal totalAcumulado = java.math.BigDecimal.ZERO;
            java.math.BigDecimal descuentosAcumulados = java.math.BigDecimal.ZERO;

            for (int i = 0; i < model.getRowCount(); i++) {
                java.math.BigDecimal cantidad
                        = new java.math.BigDecimal(model.getValueAt(i, 1).toString());
                java.math.BigDecimal precio
                        = new java.math.BigDecimal(model.getValueAt(i, 2).toString());

                java.math.BigDecimal subtotal
                        = new java.math.BigDecimal(model.getValueAt(i, 3).toString());
                java.math.BigDecimal iva
                        = new java.math.BigDecimal(model.getValueAt(i, 4).toString());
                java.math.BigDecimal total
                        = new java.math.BigDecimal(model.getValueAt(i, 5).toString());

                java.math.BigDecimal totalOriginal = precio.multiply(cantidad)
                        .setScale(2, java.math.RoundingMode.HALF_UP);

                java.math.BigDecimal descuentoLinea = totalOriginal.subtract(total);

                subtotalAcumulado = subtotalAcumulado.add(subtotal);
                ivaAcumulado = ivaAcumulado.add(iva);
                totalAcumulado = totalAcumulado.add(total);

                if (descuentoLinea.compareTo(java.math.BigDecimal.ZERO) > 0) {
                    descuentosAcumulados = descuentosAcumulados.add(descuentoLinea);
                }
            }

            java.math.RoundingMode redondeo = java.math.RoundingMode.HALF_UP;

            jTextField1.setText(subtotalAcumulado.setScale(2, redondeo).toPlainString());
            jTextField3.setText(ivaAcumulado.setScale(2, redondeo).toPlainString());
            jTextField4.setText(totalAcumulado.setScale(2, redondeo).toPlainString());

            txtTotalDescuentos.setText(
                    descuentosAcumulados.setScale(2, redondeo).toPlainString()
            );
            txtTotalFinal.setText(totalAcumulado.setScale(2, redondeo).toPlainString());

        } catch (Exception e) {
            System.err.println("Error al calcular montos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cbFormaDePago = new javax.swing.JLabel();
        txtNumeroVenta = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        cbCodigoCliente = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalleVenta = new javax.swing.JTable();
        btnAgregarProducto = new javax.swing.JButton();
        btnEliminarSeleccion = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtSubtotalGeneral = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtTotalFinal = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnConfirmar = new javax.swing.JButton();
        txtTotalDescuentos = new javax.swing.JTextField();
        txtdescuentos = new javax.swing.JLabel();
        btnCalcularDescuentos = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setText("Número Venta");

        jLabel12.setText("Fecha");

        jLabel13.setText("Codigo Cliente");

        jLabel14.setText("Usuario Responsable");

        cbFormaDePago.setText("Forma de Pago");
        cbFormaDePago.setToolTipText("");

        txtNumeroVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroVentaActionPerformed(evt);
            }
        });

        cbCodigoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "INTERACTIVOS" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Subtotal");

        jLabel3.setText("% Descuento");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Total IVA");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel5.setText("Total");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtNumeroVenta)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel12)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel13)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(cbFormaDePago)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(54, 54, 54)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(cbFormaDePago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        tblDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Producto", "Cantidad", "Precio Unitario", "Subtotal", "IVA", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDetalleVenta.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(tblDetalleVenta);
        tblDetalleVenta.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        btnAgregarProducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregarProducto.setText("Agregar Producto");
        btnAgregarProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnEliminarSeleccion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarSeleccion.setText("Eliminar Selección");
        btnEliminarSeleccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnEliminarSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarSeleccionActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("TOTAL FINAL:");

        txtTotalFinal.setEditable(false);
        txtTotalFinal.setBackground(new java.awt.Color(255, 255, 255));
        txtTotalFinal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalFinal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalFinal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTotalFinal.setEnabled(false);
        txtTotalFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalFinalActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(153, 0, 0));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setSelected(true);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnConfirmar.setBackground(new java.awt.Color(0, 102, 0));
        btnConfirmar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnConfirmar.setForeground(new java.awt.Color(204, 255, 255));
        btnConfirmar.setText("CONFIRMAR");
        btnConfirmar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnConfirmar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConfirmar.setSelected(true);
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        txtTotalDescuentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalDescuentosActionPerformed(evt);
            }
        });

        txtdescuentos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtdescuentos.setText("Total Descuentos:");

        btnCalcularDescuentos.setText("Calcular Descuento");
        btnCalcularDescuentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularDescuentosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(txtSubtotalGeneral)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfirmar)
                .addGap(31, 31, 31))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(btnCalcularDescuentos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtdescuentos)
                .addGap(18, 18, 18)
                .addComponent(txtTotalDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(txtSubtotalGeneral))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtdescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalcularDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnEliminarSeleccion, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarSeleccion, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Registrar Venta ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        // 1. Obtener la lista completa de productos desde la BD
        java.util.List<modelo.Producto> lista = productoDAO.listarProductos();

        if (lista == null || lista.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "No hay productos registrados en la base de datos.", "Atención", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Crear un arreglo con los códigos y nombres de los productos
        String[] opciones = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            opciones[i] = lista.get(i).getCodigo() + " - " + lista.get(i).getNombre();
        }

        // 3. Mostrar un combo box interactivo para seleccionar el producto
        String seleccion = (String) javax.swing.JOptionPane.showInputDialog(
                this,
                "Seleccione un Producto de la BD:",
                "Agregar Producto",
                javax.swing.JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion != null) {
            // Extraer el código (lo que está antes del guión " - ")
            String codIngresado = seleccion.split(" - ")[0].trim();
            modelo.Producto productoBD = productoDAO.buscarProductoPorCodigo(codIngresado);

            if (productoBD != null) {
                String cantStr = javax.swing.JOptionPane.showInputDialog(
                        this,
                        "Cantidad a llevar:",
                        "1.0"
                );

                if (cantStr == null) {
                    return; // El usuario canceló.
                }

                double cantidad;

                try {
                    cantidad = Double.parseDouble(cantStr.trim());
                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Escribe una cantidad numérica válida."
                    );
                    return;
                }

                if (!Double.isFinite(cantidad) || cantidad <= 0) {
                    javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "La cantidad debe ser mayor que cero."
                    );
                    return;
                }

                java.math.BigDecimal precioUnitario = productoBD.getPrecioVenta();

                java.math.BigDecimal totalLinea = precioUnitario
                        .multiply(java.math.BigDecimal.valueOf(cantidad))
                        .setScale(2, java.math.RoundingMode.HALF_UP);

                java.math.BigDecimal subtotal = totalLinea
                        .divide(new java.math.BigDecimal("1.12"), 2,
                                java.math.RoundingMode.HALF_UP);

                java.math.BigDecimal iva = totalLinea.subtract(subtotal);

                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblDetalleVenta.getModel();
                model.addRow(new Object[]{
                    productoBD.getCodigo(),
                    cantidad,
                    precioUnitario,
                    subtotal,
                    iva,
                    totalLinea
                });

                calcularTotalesYMontos();
            }
        }
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnEliminarSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarSeleccionActionPerformed
        int fila = tblDetalleVenta.getSelectedRow();

        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Selecciona el producto que quieres quitar."
            );
            return;
        }

        javax.swing.table.DefaultTableModel modelo
                = (javax.swing.table.DefaultTableModel) tblDetalleVenta.getModel();

        modelo.removeRow(fila);
        calcularTotalesYMontos();
    }//GEN-LAST:event_btnEliminarSeleccionActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtNumeroVenta.setText("");
        jTextField1.setText("0.00");
        jTextField2.setText("0.00");
        jTextField3.setText("0.00");
        jTextField4.setText("0.00");
        txtTotalFinal.setText("0.00");
        txtTotalDescuentos.setText("0.00");

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblDetalleVenta.getModel();
        model.setRowCount(0);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        try {
            String numVenta = txtNumeroVenta.getText().trim();

            if (numVenta.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Escribe el número de venta."
                );
                return;
            }

            if (tblDetalleVenta.getRowCount() == 0) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Agrega al menos un producto antes de confirmar."
                );
                return;
            }

            java.time.LocalDate fecha;
            try {
                fecha = java.time.LocalDate.parse(txtFecha.getText().trim());
            } catch (java.time.format.DateTimeParseException ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "La fecha debe tener el formato AAAA-MM-DD."
                );
                return;
            }

            String formaStr = jComboBox2.getSelectedItem().toString().toUpperCase();
            modelo.FormaPago formaPago = modelo.FormaPago.EFECTIVO;
            if (formaStr.contains("TARJETA")) {
                formaPago = modelo.FormaPago.TARJETA;
            } else if (formaStr.contains("TRANSFERENCIA")) {
                formaPago = modelo.FormaPago.TRANSFERENCIA;
            }

            java.math.BigDecimal subtotal = new java.math.BigDecimal(jTextField1.getText().trim());

            java.math.BigDecimal descuento
                    = new java.math.BigDecimal(txtTotalDescuentos.getText().trim());

            java.math.BigDecimal iva = new java.math.BigDecimal(jTextField3.getText().trim());
            java.math.BigDecimal total = new java.math.BigDecimal(jTextField4.getText().trim());

            String codigoClienteSeleccionado
                    = cbCodigoCliente.getSelectedItem() == null
                    ? ""
                    : cbCodigoCliente.getSelectedItem().toString();

            modelo.Cliente cliente = clienteDAO.listarClientes().stream()
                    .filter(c -> codigoClienteSeleccionado.equals(c.getCodigoCliente()))
                    .findFirst()
                    .orElse(null);

            if (cliente == null) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Selecciona un cliente válido."
                );
                return;
            }

            if (cbCodigoCliente.getSelectedItem() != null) {
                cliente.setCodigoCliente(cbCodigoCliente.getSelectedItem().toString());
            } else {
                cliente.setCodigoCliente("CF");
            }


            modelo.Usuario usuario;

            if (usuarioActual != null) {
                // Sesión de empleado: la venta queda a nombre del usuario conectado.
                usuario = usuarioActual;
            } else {
                // Constructor sin usuario: mantiene el selector para ese flujo.
                String nombreUsuarioSeleccionado
                        = jComboBox3.getSelectedItem() == null
                        ? ""
                        : jComboBox3.getSelectedItem().toString();

                usuario = usuarioDAO.listarUsuarios().stream()
                        .filter(u -> nombreUsuarioSeleccionado.equals(
                        u.getNombreUsuario()))
                        .findFirst()
                        .orElse(null);
            }

            if (usuario == null) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Selecciona un usuario responsable válido."
                );
                return;
            }

            modelo.Venta nuevaVenta = new modelo.Venta(
                    numVenta,
                    fecha,
                    formaPago,
                    subtotal,
                    descuento,
                    iva,
                    total,
                    cliente,
                    usuario
            );

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblDetalleVenta.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                String codigoProducto = model.getValueAt(i, 0).toString();
                modelo.Producto prod
                        = productoDAO.buscarProductoPorCodigo(codigoProducto);

                if (prod == null) {
                    throw new IllegalArgumentException(
                            "No se encontró el producto con código " + codigoProducto
                    );
                }

                double cantidad
                        = Double.parseDouble(model.getValueAt(i, 1).toString());

                java.math.BigDecimal precio
                        = new java.math.BigDecimal(model.getValueAt(i, 2).toString());

                modelo.DetalleVenta detalle
                        = new modelo.DetalleVenta(prod, cantidad, precio);

                detalle.setSubtotal(
                        new java.math.BigDecimal(model.getValueAt(i, 3).toString())
                );
                detalle.setIvaLinea(
                        new java.math.BigDecimal(model.getValueAt(i, 4).toString())
                );
                detalle.setTotalLinea(
                        new java.math.BigDecimal(model.getValueAt(i, 5).toString())
                );

                nuevaVenta.agregarDetalle(detalle);

                nuevaVenta.agregarDetalle(detalle);
            }

            if (ventaDAO.insertarVenta(nuevaVenta)) {
                try {
                    java.io.File pdf
                            = ComprobanteVentaPDF.guardarPDF(nuevaVenta, this);

                    if (pdf != null) {
                        javax.swing.JOptionPane.showMessageDialog(
                                this,
                                "Venta registrada.\nComprobante guardado en:\n"
                                + pdf.getAbsolutePath(),
                                "Venta confirmada",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );

                        // Abre el PDF para que puedas revisarlo e imprimirlo.
                        if (java.awt.Desktop.isDesktopSupported()) {
                            try {
                                java.awt.Desktop.getDesktop().open(pdf);
                            } catch (java.io.IOException ex) {
                                javax.swing.JOptionPane.showMessageDialog(
                                        this,
                                        "El PDF se guardó, pero no se pudo abrir automáticamente.\n"
                                        + "Puedes abrirlo desde:\n"
                                        + pdf.getAbsolutePath(),
                                        "PDF guardado",
                                        javax.swing.JOptionPane.WARNING_MESSAGE
                                );
                            }
                        }
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                                this,
                                "La venta se guardó, pero cancelaste dónde guardar el PDF.",
                                "Venta confirmada",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                } catch (java.io.IOException
                        | org.openpdf.text.DocumentException ex) {

                    javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "La venta se guardó, pero ocurrió un error al generar el PDF:\n"
                            + ex.getMessage(),
                            "Error al generar el comprobante",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                }

                // Limpia el formulario al final, después de guardar la venta.
                btnCancelarActionPerformed(evt);

            } else {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar la venta en la base de datos.",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error en los datos de la venta: " + e.getMessage(), "Atención", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void txtNumeroVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroVentaActionPerformed
        cbCodigoCliente.requestFocus();;
    }//GEN-LAST:event_txtNumeroVentaActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        calcularTotalesYMontos();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        calcularTotalesYMontos();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void txtTotalFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalFinalActionPerformed

    private void btnCalcularDescuentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularDescuentosActionPerformed
        // TODO add your handling code here:
        aplicarDescuentoSeleccionado();
    }//GEN-LAST:event_btnCalcularDescuentosActionPerformed

    private void txtTotalDescuentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalDescuentosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalDescuentosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnCalcularDescuentos;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnEliminarSeleccion;
    private javax.swing.JComboBox<String> cbCodigoCliente;
    private javax.swing.JLabel cbFormaDePago;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTable tblDetalleVenta;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNumeroVenta;
    private javax.swing.JLabel txtSubtotalGeneral;
    private javax.swing.JTextField txtTotalDescuentos;
    private javax.swing.JTextField txtTotalFinal;
    private javax.swing.JLabel txtdescuentos;
    // End of variables declaration//GEN-END:variables

}
