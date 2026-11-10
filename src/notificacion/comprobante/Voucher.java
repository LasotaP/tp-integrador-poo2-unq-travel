package notificacion.comprobante;

import java.time.LocalDate;
import java.util.List;

import servicios.Servicio;
import reserva.Cliente;
import reserva.ItemReserva;

public class Voucher extends Comprobante {
	
	private LocalDate fecFin;

    public Voucher(LocalDate fecha, Cliente cliente, List<ItemReserva> itemReserva, LocalDate fecFin) {
        super(fecha, cliente);
        this.fecFin = fecFin;
    }
}

