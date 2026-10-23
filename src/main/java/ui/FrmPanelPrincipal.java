/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

/**
 *
 * @author david
 */
public class FrmPanelPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPanelPrincipal.class.getName());

    /**
     * Creates new form FrmMenuPrincipal
     */
   
    public FrmPanelPrincipal() {
       initComponents();          // Fuerza las dimensiones fijas        
        // Llamar al método para cargar los datos en el Dashboard
    } // <-- Aquí cierra el constructor
   
    // MÉTODO FUERA DEL CONSTRUCTOR
    public void actualizarDashboard(String nombreUsuario, int cantidadVentas, double v1, double v2, double v3, 
                                   int stockBajoCount, String productoAgotado, int enProceso, int completadas) {
        
        // 1. Encabezado y saludo
        jLabel7.setText("Administrador: " + nombreUsuario);
        lblBienvenida.setText("Bienvenido " + nombreUsuario);
        
        // 2. Tarjeta Ventas
        lblNuevasVentas.setText("Nuevas Ventas: " + cantidadVentas);
        lblTotalVenta1.setText("Total Hoy: Q" + String.format("%.2f", v1));
        lblTotalVenta2.setText("Total Hoy: Q" + String.format("%.2f", v2));
        lblTotalVenta3.setText("Total Hoy: Q" + String.format("%.2f", v3));

        // 3. Tarjeta Inventario
        lblStockBajo.setText("Stock Bajo: " + stockBajoCount + " productos");
        lblProductoAgotado.setText(productoAgotado.isEmpty() ? "Sin productos agotados" : productoAgotado + ": Agotado");

        // 4. Tarjeta Fabricación
        lblEnProceso.setText("En proceso: " + enProceso);
        lblCompletadasHoy.setText("Completadas hoy: " + completadas);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupInventario = new javax.swing.JPopupMenu();
        itemMateriaPrima = new javax.swing.JMenuItem();
        popupVentas = new javax.swing.JPopupMenu();
        itemClientes = new javax.swing.JMenuItem();
        itemVentas = new javax.swing.JMenuItem();
        popupCompras = new javax.swing.JPopupMenu();
        itemRegistrarCompra = new javax.swing.JMenuItem();
        itemProvedores = new javax.swing.JMenuItem();
        popupProduccion = new javax.swing.JPopupMenu();
        itemOrdenesProduccion = new javax.swing.JMenuItem();
        itemProductosProceso = new javax.swing.JMenuItem();
        popupReportes = new javax.swing.JPopupMenu();
        itemReportes = new javax.swing.JMenuItem();
        itemHistorial = new javax.swing.JMenuItem();
        popupUsuarios = new javax.swing.JPopupMenu();
        itemUsuarios = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        pnlGeneral = new javax.swing.JPanel();
        pnlEncabezado = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pnlLateral = new javax.swing.JPanel();
        btnInventario = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnUsuarios = new javax.swing.JButton();
        btnPanelPrincipal = new javax.swing.JButton();
        btnProveedores = new javax.swing.JButton();
        btnReportes = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        btnProduccion = new javax.swing.JButton();
        pnlCentral = new javax.swing.JPanel();
        lblBienvenida = new javax.swing.JLabel();
        pnlVentasDia = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblNuevasVentas = new javax.swing.JLabel();
        lblTotalVenta1 = new javax.swing.JLabel();
        lblTotalVenta2 = new javax.swing.JLabel();
        lblTotalVenta3 = new javax.swing.JLabel();
        pnlInventarioCritico = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblStockBajo = new javax.swing.JLabel();
        lblProductoAgotado = new javax.swing.JLabel();
        pnlAccesosDirectos = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnAccesoHistorial = new javax.swing.JButton();
        btnAccesoRegistrarVenta = new javax.swing.JButton();
        pnlOrdenesFabricacion = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblEnProceso = new javax.swing.JLabel();
        lblCompletadasHoy = new javax.swing.JLabel();

        itemMateriaPrima.setText("Materia Prima");
        itemMateriaPrima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMateriaPrimaActionPerformed(evt);
            }
        });
        popupInventario.add(itemMateriaPrima);

        itemClientes.setText("Registrar Clientes");
        itemClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemClientesActionPerformed(evt);
            }
        });
        popupVentas.add(itemClientes);

        itemVentas.setText("Registrar Ventas");
        itemVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemVentasActionPerformed(evt);
            }
        });
        popupVentas.add(itemVentas);

        itemRegistrarCompra.setText("Registrar Compras");
        popupCompras.add(itemRegistrarCompra);

        itemProvedores.setText("Proveedores");
        popupCompras.add(itemProvedores);

        itemOrdenesProduccion.setText("Ordenes de Produccion");
        popupProduccion.add(itemOrdenesProduccion);

        itemProductosProceso.setText("Productos en Proceso ");
        popupProduccion.add(itemProductosProceso);

        itemReportes.setText("Reportes");
        popupReportes.add(itemReportes);

        itemHistorial.setText("Historial ");
        popupReportes.add(itemHistorial);

        itemUsuarios.setText("Usuarios");
        popupUsuarios.add(itemUsuarios);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlEncabezado.setBackground(new java.awt.Color(247, 237, 228));
        pnlEncabezado.setForeground(new java.awt.Color(51, 204, 255));
        pnlEncabezado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Administrador: Daniel Lopez ");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel7.setOpaque(true);
        pnlEncabezado.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, -1, -1));

        btnCerrarSesion.setText("CERRAR SESIÓN");
        btnCerrarSesion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        pnlEncabezado.add(btnCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 100, 20));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Sistema de Gestin - Ventanas de PVC ALVIDE ");
        pnlEncabezado.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -3, -1, 30));

        pnlLateral.setBackground(new java.awt.Color(247, 237, 228));
        pnlLateral.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnInventario.setBackground(new java.awt.Color(179, 217, 190));
        btnInventario.setText("Productos");
        btnInventario.setToolTipText("");
        btnInventario.setActionCommand("");
        btnInventario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnInventario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInventario.setOpaque(true);
        btnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioActionPerformed(evt);
            }
        });

        btnVentas.setBackground(new java.awt.Color(179, 217, 190));
        btnVentas.setText("Ventas/Clientes ");
        btnVentas.setToolTipText("");
        btnVentas.setActionCommand("");
        btnVentas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnVentas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });

        btnUsuarios.setBackground(new java.awt.Color(179, 217, 190));
        btnUsuarios.setText("Administracion de Usuarios");
        btnUsuarios.setToolTipText("");
        btnUsuarios.setActionCommand("");
        btnUsuarios.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnUsuarios.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuariosActionPerformed(evt);
            }
        });

        btnPanelPrincipal.setBackground(new java.awt.Color(179, 219, 193));
        btnPanelPrincipal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPanelPrincipal.setText("Panel Principal");
        btnPanelPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPanelPrincipalActionPerformed(evt);
            }
        });

        btnProveedores.setBackground(new java.awt.Color(179, 217, 190));
        btnProveedores.setText("Proveedores");
        btnProveedores.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedoresActionPerformed(evt);
            }
        });

        btnReportes.setBackground(new java.awt.Color(179, 217, 190));
        btnReportes.setText("Reportes");
        btnReportes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesActionPerformed(evt);
            }
        });

        btnHistorial.setBackground(new java.awt.Color(179, 217, 190));
        btnHistorial.setText("Historial");
        btnHistorial.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });

        btnProduccion.setBackground(new java.awt.Color(179, 217, 190));
        btnProduccion.setText("Fabricación");
        btnProduccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnProduccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProduccionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLateralLayout = new javax.swing.GroupLayout(pnlLateral);
        pnlLateral.setLayout(pnlLateralLayout);
        pnlLateralLayout.setHorizontalGroup(
            pnlLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLateralLayout.createSequentialGroup()
                        .addGroup(pnlLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                            .addComponent(btnInventario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReportes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLateralLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnPanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLateralLayout.createSequentialGroup()
                        .addComponent(btnProduccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnlLateralLayout.setVerticalGroup(
            pnlLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLateralLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(btnPanelPrincipal)
                .addGap(17, 17, 17)
                .addComponent(btnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnProduccion)
                .addGap(18, 18, 18)
                .addComponent(btnReportes)
                .addGap(18, 18, 18)
                .addComponent(btnHistorial)
                .addGap(18, 18, 18)
                .addComponent(btnUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );

        pnlCentral.setBackground(new java.awt.Color(255, 255, 255));
        pnlCentral.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBienvenida.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblBienvenida.setText("Bienvenido Daniel Lopéz ");
        lblBienvenida.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        pnlVentasDia.setBackground(new java.awt.Color(204, 0, 204));
        pnlVentasDia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 216, 192), 4));
        pnlVentasDia.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Resumen Ventas del Dia");

        lblNuevasVentas.setText("Nuevas Ventas:");

        lblTotalVenta1.setText("Total Hoy:");

        lblTotalVenta2.setText("Total Hoy:");

        lblTotalVenta3.setText("Total Hoy:");

        javax.swing.GroupLayout pnlVentasDiaLayout = new javax.swing.GroupLayout(pnlVentasDia);
        pnlVentasDia.setLayout(pnlVentasDiaLayout);
        pnlVentasDiaLayout.setHorizontalGroup(
            pnlVentasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentasDiaLayout.createSequentialGroup()
                .addGroup(pnlVentasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVentasDiaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlVentasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNuevasVentas)
                            .addComponent(lblTotalVenta1)
                            .addComponent(lblTotalVenta2)
                            .addComponent(lblTotalVenta3)))
                    .addGroup(pnlVentasDiaLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlVentasDiaLayout.setVerticalGroup(
            pnlVentasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentasDiaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNuevasVentas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalVenta1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalVenta2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalVenta3)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pnlInventarioCritico.setBackground(new java.awt.Color(246, 237, 230));
        pnlInventarioCritico.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 216, 192), 4));
        pnlInventarioCritico.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Inventario Critico");

        lblStockBajo.setText("Stock Bajo:");

        lblProductoAgotado.setText("Sin Productos/Agotado:");

        javax.swing.GroupLayout pnlInventarioCriticoLayout = new javax.swing.GroupLayout(pnlInventarioCritico);
        pnlInventarioCritico.setLayout(pnlInventarioCriticoLayout);
        pnlInventarioCriticoLayout.setHorizontalGroup(
            pnlInventarioCriticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioCriticoLayout.createSequentialGroup()
                .addGroup(pnlInventarioCriticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventarioCriticoLayout.createSequentialGroup()
                        .addGap(307, 307, 307)
                        .addGroup(pnlInventarioCriticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStockBajo)
                            .addComponent(lblProductoAgotado)))
                    .addGroup(pnlInventarioCriticoLayout.createSequentialGroup()
                        .addGap(367, 367, 367)
                        .addComponent(jLabel4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInventarioCriticoLayout.setVerticalGroup(
            pnlInventarioCriticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioCriticoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblStockBajo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblProductoAgotado)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlAccesosDirectos.setBackground(new java.awt.Color(246, 237, 230));
        pnlAccesosDirectos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 216, 192), 4));
        pnlAccesosDirectos.setOpaque(false);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("Accesos Directos");

        btnAccesoHistorial.setBackground(new java.awt.Color(179, 219, 193));
        btnAccesoHistorial.setText("Historial");
        btnAccesoHistorial.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAccesoHistorial.setSelected(true);
        btnAccesoHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccesoHistorialActionPerformed(evt);
            }
        });

        btnAccesoRegistrarVenta.setBackground(new java.awt.Color(179, 219, 193));
        btnAccesoRegistrarVenta.setText("Registrar Ventas");
        btnAccesoRegistrarVenta.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAccesoRegistrarVenta.setSelected(true);
        btnAccesoRegistrarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccesoRegistrarVentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAccesosDirectosLayout = new javax.swing.GroupLayout(pnlAccesosDirectos);
        pnlAccesosDirectos.setLayout(pnlAccesosDirectosLayout);
        pnlAccesosDirectosLayout.setHorizontalGroup(
            pnlAccesosDirectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAccesosDirectosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAccesoRegistrarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAccesoHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(228, 228, 228))
            .addGroup(pnlAccesosDirectosLayout.createSequentialGroup()
                .addGap(365, 365, 365)
                .addComponent(jLabel5)
                .addContainerGap(307, Short.MAX_VALUE))
        );
        pnlAccesosDirectosLayout.setVerticalGroup(
            pnlAccesosDirectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccesosDirectosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel5)
                .addGap(40, 40, 40)
                .addGroup(pnlAccesosDirectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccesoHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAccesoRegistrarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pnlOrdenesFabricacion.setBackground(new java.awt.Color(246, 237, 230));
        pnlOrdenesFabricacion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 216, 192), 4));
        pnlOrdenesFabricacion.setOpaque(false);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Ordenes de Fabricación Pendientes");

        lblEnProceso.setText("En proceso:");

        lblCompletadasHoy.setText("Completadas Hoy:");

        javax.swing.GroupLayout pnlOrdenesFabricacionLayout = new javax.swing.GroupLayout(pnlOrdenesFabricacion);
        pnlOrdenesFabricacion.setLayout(pnlOrdenesFabricacionLayout);
        pnlOrdenesFabricacionLayout.setHorizontalGroup(
            pnlOrdenesFabricacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOrdenesFabricacionLayout.createSequentialGroup()
                .addGroup(pnlOrdenesFabricacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOrdenesFabricacionLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel6))
                    .addGroup(pnlOrdenesFabricacionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlOrdenesFabricacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCompletadasHoy)
                            .addComponent(lblEnProceso))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        pnlOrdenesFabricacionLayout.setVerticalGroup(
            pnlOrdenesFabricacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOrdenesFabricacionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEnProceso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCompletadasHoy)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlCentralLayout = new javax.swing.GroupLayout(pnlCentral);
        pnlCentral.setLayout(pnlCentralLayout);
        pnlCentralLayout.setHorizontalGroup(
            pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCentralLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblBienvenida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlVentasDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlOrdenesFabricacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInventarioCritico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlAccesosDirectos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCentralLayout.setVerticalGroup(
            pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCentralLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblBienvenida)
                .addGap(18, 18, 18)
                .addGroup(pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlInventarioCritico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlVentasDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlAccesosDirectos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlOrdenesFabricacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(127, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlGeneralLayout = new javax.swing.GroupLayout(pnlGeneral);
        pnlGeneral.setLayout(pnlGeneralLayout);
        pnlGeneralLayout.setHorizontalGroup(
            pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGeneralLayout.createSequentialGroup()
                .addComponent(pnlLateral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, 1350, Short.MAX_VALUE)
        );
        pnlGeneralLayout.setVerticalGroup(
            pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGeneralLayout.createSequentialGroup()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlLateral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlCentral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioActionPerformed
     // Muestra el menú desplegable justo debajo del botón de Inventario                                             // Muestra el menú desplegable justo debajo del botón
     // TODO add your handling code here:
     PnlProducto pnlMateriaPrima = new PnlProducto();
    
    // 2. Limpiar el contenedor central y configurar el layout
    pnlCentral.removeAll();
    pnlCentral.setLayout(new java.awt.BorderLayout());
    
    // 3. Agregar el nuevo panel
    pnlCentral.add(pnlMateriaPrima, java.awt.BorderLayout.CENTER);
    
    // 4. Refrescar la interfaz
    pnlCentral.revalidate();
    pnlCentral.repaint();
    }//GEN-LAST:event_btnInventarioActionPerformed

    private void btnUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuariosActionPerformed
        ui.PnlUsuarios pnlVentas = new ui.PnlUsuarios();

        // 2. Limpiar solo el panel central (pnlCentral)
        pnlCentral.removeAll();
        pnlCentral.setLayout(new java.awt.BorderLayout());
        pnlCentral.add(pnlVentas, java.awt.BorderLayout.CENTER);
        pnlCentral.revalidate();
        pnlCentral.repaint();
    }//GEN-LAST:event_btnUsuariosActionPerformed

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
    popupVentas.show(btnVentas, 0, btnVentas.getHeight());
    PnlVentas pnlVentas = new PnlVentas(); 

    // 2. Limpiar y refrescar el contenedor principal
    
    pnlCentral.add(pnlVentas, java.awt.BorderLayout.CENTER);
    pnlCentral.revalidate();
    pnlCentral.repaint();
    // Muestra el menú desplegable justo debajo del botón de Ventas
       
    }//GEN-LAST:event_btnVentasActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        this.dispose();
        new FrmMenuInicio().setVisible(true);
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnPanelPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanelPrincipalActionPerformed
      // Opción A: Abrir de nuevo la ventana principal limpia (o reiniciar la vista)
    FrmPanelPrincipal nuevoPanel = new FrmPanelPrincipal();
    nuevoPanel.setVisible(true);
    this.dispose(); // Cierra la ventana actual
    }//GEN-LAST:event_btnPanelPrincipalActionPerformed

    private void btnAccesoRegistrarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccesoRegistrarVentaActionPerformed
        ui.PnlVentas pnlVentas = new ui.PnlVentas();
        pnlCentral.removeAll();
        pnlCentral.setLayout(new java.awt.BorderLayout());
        pnlCentral.add(pnlVentas, java.awt.BorderLayout.CENTER);
        pnlCentral.revalidate();
        pnlCentral.repaint();
    }//GEN-LAST:event_btnAccesoRegistrarVentaActionPerformed

    private void btnAccesoHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccesoHistorialActionPerformed
        ui.PnlHistorial pnlHistorial = new ui.PnlHistorial();
        pnlCentral.removeAll();
        pnlCentral.setLayout(new java.awt.BorderLayout());
        pnlCentral.add(pnlHistorial, java.awt.BorderLayout.CENTER);
        pnlCentral.revalidate();
        pnlCentral.repaint();
    }//GEN-LAST:event_btnAccesoHistorialActionPerformed

    private void itemMateriaPrimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMateriaPrimaActionPerformed
        // TODO add your handling code here:
        // Instanciar la vista de Materia Prima que acabas de diseñar
        // 1. Crear la instancia del panel que diseñaste
        // Instanciar la nueva vista JPanel
        PnlProducto pnlMateriaPrima = new PnlProducto();

        // Limpiar el contenedor central y cargar la vista
        pnlCentral.setLayout(new java.awt.BorderLayout());
        pnlCentral.removeAll();
        pnlCentral.add(pnlMateriaPrima, java.awt.BorderLayout.CENTER);

        // Refrescar la interfaz
        pnlCentral.revalidate();
        pnlCentral.repaint();
    }//GEN-LAST:event_itemMateriaPrimaActionPerformed

    private void itemClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemClientesActionPerformed
        ui.PnlClientes pnlClientes = new ui.PnlClientes();

    // 2. Limpiar y actualizar el panel central (pnlCentral)
    pnlCentral.removeAll();
    pnlCentral.setLayout(new java.awt.BorderLayout());
    pnlCentral.add(pnlClientes, java.awt.BorderLayout.CENTER);
    pnlCentral.revalidate();
    pnlCentral.repaint();
    }//GEN-LAST:event_itemClientesActionPerformed

    private void btnProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedoresActionPerformed
        // TODO add your handling code here:
        ui.PnlProveedores pnlVentas = new ui.PnlProveedores();

        // 2. Limpiar solo el panel central (pnlCentral)
        pnlCentral.removeAll();
        pnlCentral.setLayout(new java.awt.BorderLayout());
        pnlCentral.add(pnlVentas, java.awt.BorderLayout.CENTER);
        pnlCentral.revalidate();
        pnlCentral.repaint();
        
    }//GEN-LAST:event_btnProveedoresActionPerformed

    private void itemVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemVentasActionPerformed
        // TODO add your handling code here:
        ui.PnlVentas pnlVentas = new ui.PnlVentas();

        // 2. Limpiar solo el panel central (pnlCentral)
        pnlCentral.removeAll();
        pnlCentral.setLayout(new java.awt.BorderLayout());
        pnlCentral.add(pnlVentas, java.awt.BorderLayout.CENTER);
        pnlCentral.revalidate();
        pnlCentral.repaint();
    }//GEN-LAST:event_itemVentasActionPerformed

    private void btnReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesActionPerformed
        // TODO add your handling code here:
        PnlReportes pnlReportes = new PnlReportes();

        pnlCentral.removeAll();
        pnlCentral.setLayout(new java.awt.BorderLayout());

        pnlCentral.add(pnlReportes, java.awt.BorderLayout.CENTER);

        pnlCentral.revalidate();
        pnlCentral.repaint();
        
    }//GEN-LAST:event_btnReportesActionPerformed

    private void btnProduccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProduccionActionPerformed
        // TODO add your handling code here:
        PnlTrabajoFabricacion pnlTrabajoFabricacion = new PnlTrabajoFabricacion();

        pnlCentral.removeAll();
        pnlCentral.setLayout(new java.awt.BorderLayout());

        pnlCentral.add(pnlTrabajoFabricacion, java.awt.BorderLayout.CENTER);

        pnlCentral.revalidate();
        pnlCentral.repaint();
    }//GEN-LAST:event_btnProduccionActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        // TODO add your handling code here:
        PnlHistorial pnlHistorial = new PnlHistorial();

        pnlCentral.removeAll();
        pnlCentral.setLayout(new java.awt.BorderLayout());

        pnlCentral.add(pnlHistorial, java.awt.BorderLayout.CENTER);

        pnlCentral.revalidate();
        pnlCentral.repaint();
    }//GEN-LAST:event_btnHistorialActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    /* Establecer el Look and Feel nativo del sistema (Windows) */
    try {
        javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ex) {
        java.util.logging.Logger.getLogger(FrmPanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    /* Crear y mostrar el formulario */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new FrmPanelPrincipal().setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccesoHistorial;
    private javax.swing.JButton btnAccesoRegistrarVenta;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnInventario;
    private javax.swing.JButton btnPanelPrincipal;
    private javax.swing.JButton btnProduccion;
    private javax.swing.JButton btnProveedores;
    private javax.swing.JButton btnReportes;
    private javax.swing.JButton btnUsuarios;
    private javax.swing.JButton btnVentas;
    private javax.swing.JMenuItem itemClientes;
    private javax.swing.JMenuItem itemHistorial;
    private javax.swing.JMenuItem itemMateriaPrima;
    private javax.swing.JMenuItem itemOrdenesProduccion;
    private javax.swing.JMenuItem itemProductosProceso;
    private javax.swing.JMenuItem itemProvedores;
    private javax.swing.JMenuItem itemRegistrarCompra;
    private javax.swing.JMenuItem itemReportes;
    private javax.swing.JMenuItem itemUsuarios;
    private javax.swing.JMenuItem itemVentas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JLabel lblBienvenida;
    private javax.swing.JLabel lblCompletadasHoy;
    private javax.swing.JLabel lblEnProceso;
    private javax.swing.JLabel lblNuevasVentas;
    private javax.swing.JLabel lblProductoAgotado;
    private javax.swing.JLabel lblStockBajo;
    private javax.swing.JLabel lblTotalVenta1;
    private javax.swing.JLabel lblTotalVenta2;
    private javax.swing.JLabel lblTotalVenta3;
    private javax.swing.JPanel pnlAccesosDirectos;
    private javax.swing.JPanel pnlCentral;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlGeneral;
    private javax.swing.JPanel pnlInventarioCritico;
    private javax.swing.JPanel pnlLateral;
    private javax.swing.JPanel pnlOrdenesFabricacion;
    private javax.swing.JPanel pnlVentasDia;
    private javax.swing.JPopupMenu popupCompras;
    private javax.swing.JPopupMenu popupInventario;
    private javax.swing.JPopupMenu popupProduccion;
    private javax.swing.JPopupMenu popupReportes;
    private javax.swing.JPopupMenu popupUsuarios;
    private javax.swing.JPopupMenu popupVentas;
    // End of variables declaration//GEN-END:variables
}
