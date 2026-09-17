package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Venta {
    //Atributos
    private String numeroVenta;
    private LocalDate fecha;
    private FormaPago formaPago;
    private BigDecimal subtotalGeneral;
    private BigDecimal totalDescuentos;
    private BigDecimal totalIva;
    private BigDecimal totalFinal;

    //Constructor
    public Venta(String numeroVenta, LocalDate fecha, FormaPago formaPago, BigDecimal subtotalGeneral, BigDecimal totalDescuentos, BigDecimal totalIva, BigDecimal totalFinal) {
        this.numeroVenta = numeroVenta;
        this.fecha = fecha;
        this.formaPago = formaPago;
        this.subtotalGeneral = subtotalGeneral;
        this.totalDescuentos = totalDescuentos;
        this.totalIva = totalIva;
        this.totalFinal = totalFinal;
    }

    //Get y Set
    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public BigDecimal getSubtotalGeneral() {
        return subtotalGeneral;
    }

    public void setSubtotalGeneral(BigDecimal subtotalGeneral) {
        this.subtotalGeneral = subtotalGeneral;
    }

    public BigDecimal getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(BigDecimal totalIva) {
        this.totalIva = totalIva;
    }

    public BigDecimal getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(BigDecimal totalFinal) {
        this.totalFinal = totalFinal;
    }



}
