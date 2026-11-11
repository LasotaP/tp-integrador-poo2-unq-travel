package notificacion.comprobante;

import java.time.LocalDate;
import java.util.List;

import servicios.Servicio;
import reserva.Cliente;
import reserva.ItemReserva;

public class Voucher extends Comprobante {
	
	private LocalDate fecFin;

    public Voucher(String codigo, LocalDate fecha, Cliente cliente, List<ItemReserva> itemReserva, double monto, LocalDate fecFin) {
    	this.codigo = "VOU" + codigo;
    	super(fecha, cliente, monto);
        this.fecFin = fecFin;
    }
    
    public Voucher(String codigo, LocalDate fecha, Cliente cliente, List<ItemReserva> itemReserva, double monto) {
    	this.codigo = "VOU" + codigo;
    	super(fecha, cliente, monto);
    }

	public LocalDate getFecFin() {
		return fecFin;
	}
}

