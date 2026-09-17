
package modelo;

public class Cliente {
    //Atributos
    private String codigoCliente; //analizar esto y verlo
    private String nombreCliente;
    private String telefono; 
    private String nitCF;
    
    //Constructor
    public Cliente(String codigoCliente, String nombreCliente, String telefono, String nitCF) {
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.nitCF = nitCF;
    }

    //Get y Set
    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNitCF() {
        return nitCF;
    }

    public void setNitCF(String nitCF) {
        this.nitCF = nitCF;
    }



}
