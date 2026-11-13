package unqTravel.comprobante;

import java.time.LocalDate;
import java.util.List;

import unqTravel.catalogo.Servicio;
import unqTravel.reserva.Cliente;
import unqTravel.reserva.ItemReserva;

public class Factura extends Comprobante {

    public Factura(String codigo, LocalDate fecha, Cliente cliente, List<ItemReserva> servicios,double monto) {
    	super("FAC" + codigo, fecha, servicios, cliente, monto);        
    }
    
   

}
