package notificacion.comprobante;

import java.time.LocalDate;
import java.util.List;

import reserva.Cliente;
import reserva.ItemReserva;

public class Voucher extends Comprobante {
	
	private LocalDate fecFin;

    public Voucher(String codigo, LocalDate fecha, Cliente cliente, List<ItemReserva> itemsReserva, double monto, LocalDate fecFin) {
    	super("VOU" + codigo ,fecha, itemsReserva, cliente, monto);
        this.fecFin = fecFin;
    }
    
    public Voucher(String codigo, LocalDate fecha, Cliente cliente, List<ItemReserva> itemsReserva, double monto) {
    	super("VOU" + codigo, fecha,itemsReserva, cliente ,monto);
    }

	public LocalDate getFecFin() {
		return fecFin;
	}
}

