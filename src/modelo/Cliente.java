
package modelo;

public class Cliente {
    //Atributos
    private int idCliente; //analizar esto y verlo
    private String nombreCliente;
    private String telefono; 
    private String nit;
    
    //Constructor

    public Cliente(int idCliente, String nombreCliente, String telefono, String nit) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.nit = nit;
    }
    
    //Métodos Get (lectura) 

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNit() {
        return nit;
    }
    
    //Metodos Set (escritura) 

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
    
    
    
    
    
    
    
}
