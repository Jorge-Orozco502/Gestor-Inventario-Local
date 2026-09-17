package modelo;

import java.time.LocalDate;
import java.math.BigDecimal;


public class Compra {
    //Atributos
    private String numeroCompra;
    private LocalDate fechaCompra;
    private FormaPago formaPago;
    private BigDecimal total;

    //Constructor

    public Compra(String numeroCompra, LocalDate fechaCompra, FormaPago formaPago, BigDecimal total) {
        this.numeroCompra = numeroCompra;
        this.fechaCompra = fechaCompra;
        this.formaPago = formaPago;
        this.total = total;
    }

    //Get y Set
    public String getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(String numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
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
        this.total = total;
    }


}