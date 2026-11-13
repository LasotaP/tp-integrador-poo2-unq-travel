package unqTravel.pago;

import unqTravel.reserva.Reserva;

public class BilleteraVirtual extends MetodoDePago {
	private BilleteraVirtualAPI apiBV;
	@Override
	public void validarDatos(Reserva reserva) {
		this.apiBV.validarSaldoSuficiente(reserva.getCostoTotal());
	}

	@Override
	public void ejecutarTransaccion(Reserva reserva) {
		this.apiBV.acreditar(reserva.getCostoTotal());
	}	
	
	@Override
	public void notificarResultado(Reserva reserva) {
		this.apiBV.notificar();
	}
}
