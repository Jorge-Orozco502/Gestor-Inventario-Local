package modelo;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    //Atributos
    private String numeroCompra;
    private LocalDate fechaCompra;
    private FormaPago formaPago;
    private BigDecimal total;
    private Proveedor proveedor;
    private List<DetalleCompra> detallesCompra;

    //Constructor
    public Compra(String numeroCompra, LocalDate fechaCompra, FormaPago formaPago, BigDecimal total, Proveedor proveedor) {
        this.numeroCompra = numeroCompra;
        this.fechaCompra = fechaCompra;
        this.formaPago = formaPago;
        this.total = total != null ? total : BigDecimal.ZERO;
        this.proveedor = proveedor;
        this.detallesCompra = new ArrayList<>();
    }

    //Constructor Vacio
    public Compra(){
        this.total = BigDecimal.ZERO;
        this.detallesCompra = new ArrayList<>();
    }

    //Get y Set
    public String getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(String numeroCompra) {
        if (numeroCompra == null || numeroCompra.trim().isEmpty()){
            throw new IllegalArgumentException("El número de compra no puede estar vacío.");
        }
        this.numeroCompra = numeroCompra;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        if (fechaCompra == null){
            throw new IllegalArgumentException("La fecha de compra no puede ser nula.");
        }
        this.fechaCompra = fechaCompra;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El total de la compra no puede ser negativo");
        }
        this.total = total;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        if (proveedor == null){
            throw new IllegalArgumentException("La compra debe estar asociada a un proveedor.");
        }
        this.proveedor = proveedor;
    }

    public List<DetalleCompra> getDetallesCompra(){
        return detallesCompra;
    }

    public void setDetallesCompra(List<DetalleCompra> detallesCompra){
        this.detallesCompra = detallesCompra;
        recalcularTotal();
    }

    //================================

    //Metodo toString para poder depurar o visualización rápida

    //Metodo para agregar detalle a lo comprado
    public void agregarDetalleCompra(DetalleCompra detalle){
        if (detalle != null){
            this.detallesCompra.add(detalle);
            recalcularTotal();
        }
    }

    //Metodo para recalcular el total sumando los subtotales de cada detalle
    public void recalcularTotal(){
        BigDecimal sumaTotal = BigDecimal.ZERO;

        for (DetalleCompra detalle: this.detallesCompra){
            if (detalle.getSubtotal() != null){
                sumaTotal = sumaTotal.add(detalle.getSubtotal());
            }
        }
        this.total = sumaTotal;
    }

    //Metodo para confirmar la compra
    public List<MovimientoInventario> confirmarCompra(Usuario usuarioResponsable){
        List<MovimientoInventario> movimientos = new ArrayList<>();

        //antes de que el usuario confirme
        if (this.detallesCompra ==null || this.detallesCompra.isEmpty()){
            throw new IllegalArgumentException("No se puede confirmar una compra sin detalles de productos");
        }
        for (DetalleCompra detalle : this.detallesCompra){
            Producto producto = detalle.getProducto();

            if (producto == null){
                throw new IllegalArgumentException("Uno de los detalles de la compra no tiene un producto asociado");
            }

            //obtener la cantidad que se compro
            double cantidadComprada = detalle.getCantidadComprada();

            //aumentar la existencia del inventario
            producto.setExistenciaActual(producto.getExistenciaActual() + cantidadComprada);

            //entrada de inventario por la compra realizada
            MovimientoInventario movimiento = new MovimientoInventario(
                    LocalDate.now(),
                    "ENTRADA_COMPRA",
                    cantidadComprada,
                    producto.getExistenciaActual(),
                    "Compra de productos - Factura No:" + this.numeroCompra,
                    producto,
                    usuarioResponsable
            );
            movimientos.add(movimiento);
        }
        return movimientos;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "número de compra='" + numeroCompra + '\'' +
                ", fecha de compra=" + fechaCompra +
                ", proveedor=" + (proveedor != null ? proveedor.getNombreEmpresa() : "N/A") +
                ", forma de pago=" + formaPago +
                ", total=" + total +
                '}';
    }



}