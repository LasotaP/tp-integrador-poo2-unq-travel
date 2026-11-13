package unqTravel.pago;

import java.time.LocalDate;

import unqTravel.comprobante.Factura;
import unqTravel.reserva.Reserva;

public abstract class MetodoDePago {
	
	public final void procesarPago(Reserva reserva) {
		this.validarDatos(reserva);
		this.reservarFondos(reserva);
		this.ejecutarTransaccion(reserva);
		this.notificarResultado(reserva);
		
	}
	
	public abstract void validarDatos(Reserva reserva);
	public void reservarFondos(Reserva reserva) {}
	public abstract void ejecutarTransaccion(Reserva reserva);
	
	public void notificarResultado(Reserva reserva) {
		reserva.addComprobante(new Factura(reserva.getNumeroReserva(), LocalDate.now(),reserva.getCliente(),reserva.getItemsReserva(),reserva.getCostoTotal()));
		// se guarda en la lista de comprobantes de reserva
		
		// CORREGIR LOS PARAMETROS!
	}
}
