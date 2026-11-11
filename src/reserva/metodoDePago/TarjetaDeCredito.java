package reserva.metodoDePago;

import java.time.LocalDate;

import notificacion.comprobante.Voucher;
import reserva.Reserva;

public class TarjetaDeCredito extends MetodoDePago{
	private String numeroDeTarjeta;
	private String cvv;
	private LocalDate fechaDeVencimiento;
	
	private TarjetaDeCreditoAPI apiTJT;
	
	
	@Override
	public void validarDatos(Reserva reserva) {
		this.apiTJT.validarDatos(numeroDeTarjeta, cvv, fechaDeVencimiento);
		
	}

	@Override
	public void reservarFondos(Reserva reserva) {
		this.apiTJT.preAutorizacion(reserva.getCostoTotal());
		
	}

	@Override
	public void ejecutarTransaccion(Reserva reserva) {
		this.apiTJT.transferir(reserva.getCostoTotal());
	}
}
