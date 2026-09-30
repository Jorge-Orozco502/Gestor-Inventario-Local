package modelo;

import java.math.BigDecimal;

public class Herramienta extends Producto{

    public Herramienta(String codigo, String nombre, String categoria, String descripcion,
                       String unidadMedida, BigDecimal precioCompra, BigDecimal precioVenta, double existenciaMinima){
        super (codigo, nombre, categoria, descripcion, unidadMedida, precioCompra, precioVenta, existenciaMinima);

    }

}
