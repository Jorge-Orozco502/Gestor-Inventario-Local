package modelo;

import java.math.BigDecimal;

public class DetalleVenta {
    //Atributos
    private double cantidadSolicitada;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal ivaLinea;
    private BigDecimal totalLinea;

    //Constructor
    public DetalleVenta(double cantidadSolicitada, BigDecimal precioUnitario, BigDecimal subtotal, BigDecimal ivaLinea, BigDecimal totalLinea) {
        this.cantidadSolicitada = cantidadSolicitada;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.ivaLinea = ivaLinea;
        this.totalLinea = totalLinea;
    }

    //Get y Set
    public double getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(double cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
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
}
