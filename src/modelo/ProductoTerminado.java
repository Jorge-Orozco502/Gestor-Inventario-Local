package modelo;

import java.math.BigDecimal;

public class ProductoTerminado extends Producto{

    public ProductoTerminado(String codigo, String nombre, String categoria, String descripcion, String unidadMedida, BigDecimal precioCompra, BigDecimal precioVenta, double existenciaMinima) {
        super(codigo, nombre, categoria, descripcion, unidadMedida, precioCompra, precioVenta, existenciaMinima);
    }

    public boolean esVendible(){
        return true;
    }


}
