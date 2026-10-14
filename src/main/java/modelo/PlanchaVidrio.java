package modelo;

import java.math.BigDecimal;
import java.util.Objects;

public class PlanchaVidrio extends MateriaPrima {
    //Atributos
    private TipoVidrio tipoVidrio;
    private int espesorMm;
    private int anchoMm;
    private int largoMm;

    //Constructor


    public PlanchaVidrio(String codigo, String nombre, String categoria, String descripcion,
                         String unidadMedida, BigDecimal precioCompra, BigDecimal precioVenta,
                         double existenciaActual, double existenciaMinima, EstadoProducto estadoProducto,
                         TipoVidrio tipoVidrio, int espesorMm, int anchoMm, int largoMm) {
        super(codigo, nombre, categoria, descripcion, unidadMedida, precioCompra, precioVenta, existenciaActual, existenciaMinima, estadoProducto);
        this.tipoVidrio = Objects.requireNonNull(tipoVidrio, "El tipo de vidrio no puede ser nulo.");
        setEspesorMm(espesorMm);
        setAnchoMm(anchoMm);
        setLargoMm(largoMm);
    }

    //Constuctor Vacío
    public PlanchaVidrio(){
        super();
    }

    //GET y SET
    public TipoVidrio getTipoVidrio() {
        return tipoVidrio;
    }

    public void setTipoVidrio(TipoVidrio tipoVidrio) {
        this.tipoVidrio = Objects.requireNonNull(tipoVidrio, "El tipo de vidrio no puede ser nulo.");
    }

    public int getEspesorMm() {
        return espesorMm;
    }

    public void setEspesorMm(int espesorMm) {
        if (espesorMm <= 0){
            throw new IllegalArgumentException("El espesor en milímetros debe ser mayor a cero.");
        }
        this.espesorMm = espesorMm;
    }

    public int getAnchoMm() {
        return anchoMm;
    }

    public void setAnchoMm(int anchoMm) {
        if( anchoMm <= 0){
            throw new IllegalArgumentException("El ancho en milímetros debe ser mayor a cero.");
        }
        this.anchoMm = anchoMm;
    }

    public int getLargoMm() {
        return largoMm;
    }

    public void setLargoMm(int largoMm) {
        if (largoMm <= 0){
            throw new IllegalArgumentException("El largo en milímetros debe ser mayor a cero.");
        }
        this.largoMm = largoMm;
    }

    //======================================

    //Método para calcular el area en metros cuadrados m2
    public double calcularAreaM2(){
        return (this.anchoMm * this.largoMm) / 1000000.0;
    }

    //Metodo para describir las medidas y el espesor
    public String getMedidasTexto(){
        return  anchoMm + "mm x" + largoMm + "mm (Espesor:" + espesorMm + "mm)";
    }

    //Metodo toString
    @Override
    public String toString(){
        return "PlanchaVidio{" +
                "código='" + getCodigo() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", tipoVidrio=" + tipoVidrio +
                ", medidas=" + getMedidasTexto() +
                ", areaM2=" + calcularAreaM2() +
                '}';
    }



}
