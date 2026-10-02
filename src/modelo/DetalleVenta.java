package modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DetalleVenta {
    //Atributos
    private double cantidadSolicitada;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal ivaLinea;
    private BigDecimal totalLinea;

    private Producto producto;

    //Constructor
    public DetalleVenta(Producto producto, double cantidadSolicitada,BigDecimal precioUnitario ){
        setProducto(producto);
        setCantidadSolicitada(cantidadSolicitada);
        setPrecioUnitario(precioUnitario != null ? precioUnitario : BigDecimal.ZERO);
        calcularTotales();
    }

    //Constructor vacio
    public DetalleVenta(){
        this.subtotal = BigDecimal.ZERO;
        this.ivaLinea = BigDecimal.ZERO;
        this.totalLinea = BigDecimal.ZERO;
    }

    //Get y Set
    public double getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(double cantidadSolicitada) {
        if (cantidadSolicitada <= 0){
            throw new IllegalArgumentException("La cantidad solicitada debe ser mayor a cero.");
        }
        this.cantidadSolicitada = cantidadSolicitada;
        calcularTotales();
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El precio unitario no puede ser negativo.");
        }
        this.precioUnitario = precioUnitario;
        calcularTotales();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIvaLinea() {
        return ivaLinea;
    }

    public void setIvaLinea(BigDecimal ivaLinea) {
        this.ivaLinea = ivaLinea;
    }

    public BigDecimal getTotalLinea() {
        return totalLinea;
    }

    public void setTotalLinea(BigDecimal totalLinea) {
        this.totalLinea = totalLinea;
    }

    public Producto getProducto(){
        return  producto;
    }

    public void setProducto(Producto producto){
        if (producto == null){
            throw new IllegalArgumentException("El detalle de venta debe estar asociado a un producto.");
        }
        this.producto = producto;
    }

    //================================================

    //Metodo para calcular los montos de cada línea
    public void calcularTotales(){
        if (this.precioUnitario != null && this.cantidadSolicitada >0){
           BigDecimal cantidadBD = BigDecimal.valueOf(this.cantidadSolicitada);

           //subtotal = el precio unitario * cantidad de productos
            this.subtotal = this.precioUnitario.multiply(cantidadBD).setScale(2, RoundingMode.HALF_UP);

            // Iva por linea (el iva es del 12%
            this.ivaLinea = this.subtotal.multiply(new BigDecimal("0.12")).setScale(2, RoundingMode.HALF_UP);

            //total de la linea = el subtotal + Iva
            this.totalLinea = this.subtotal.add(this.ivaLinea);
        } else {
            this.subtotal = BigDecimal.ZERO;
            this.ivaLinea = BigDecimal.ZERO;
            this.totalLinea = BigDecimal.ZERO;
        }
    }

    @Override
    public String toString(){
        return "DetalleVenta{" +
                "producto=" +   (producto != null ? producto.getNombre(): "N/A") +
                ",cantidad="    +   cantidadSolicitada  +
                ",precio unitario=" +   precioUnitario  +
                ",subtotal="    +   subtotal +
                ",ivaLinea=" + ivaLinea +
                ",totalLinea="  +   totalLinea+
                '}';
    }

}
