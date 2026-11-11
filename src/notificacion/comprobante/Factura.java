package notificacion.comprobante;

import java.time.LocalDate;
import java.util.List;

import servicios.Servicio;
import reserva.Cliente;
import reserva.ItemReserva;

public class Factura extends Comprobante {

    public Factura(String codigo, LocalDate fecha, Cliente cliente, List<ItemReserva> servicios,double monto) {
    	this.codigo = "FAC" + codigo;
    	super(fecha, cliente, monto);
        
        
    }
    
   

}
