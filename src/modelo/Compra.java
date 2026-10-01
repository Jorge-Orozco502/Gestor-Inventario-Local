package modelo;

import java.time.LocalDate;
import java.math.BigDecimal;


public class Compra {
    //Atributos
    private String numeroCompra;
    private LocalDate fechaCompra;
    private FormaPago formaPago;
    private BigDecimal total;
    private Proveedor proveedor;

    //Constructor
    public Compra(String numeroCompra, LocalDate fechaCompra, FormaPago formaPago, BigDecimal total, Proveedor proveedor) {
        this.numeroCompra = numeroCompra;
        this.fechaCompra = fechaCompra;
        this.formaPago = formaPago;
        this.total = total;
        this.proveedor = proveedor;
    }

    //Constructor Vacio
    public Compra(){
        this.total = BigDecimal.ZERO;
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

    //================================

    //Metodo toString para podere depurar o visualización rápida
    @Override
    public String toString(){
        return "Compra{" +
                "número de compra='" + numeroCompra + '\'' +
                ", fecha de compra=" + fechaCompra +
                ", proveedor=" + (proveedor != null ? proveedor.getNombreEmpresa(): "N/A") +
                ", forma de pago=" + formaPago +
                ", total=" + total +
                '}';
    }
}