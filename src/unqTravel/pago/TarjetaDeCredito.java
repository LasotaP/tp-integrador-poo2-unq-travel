package unqTravel.pago;

import java.time.LocalDate;

import unqTravel.comprobante.Voucher;
import unqTravel.reserva.Reserva;

public class TarjetaDeCredito extends MetodoDePago{
	private String numeroDeTarjeta;
	private String cvv;
	private LocalDate fechaDeVencimiento;
	
	private TarjetaDeCreditoAPI apiTJT;
	
	public TarjetaDeCredito(String numeroDeTarjeta, String cvv, LocalDate fechaVencimiento, TarjetaDeCreditoAPI api) {
		this.numeroDeTarjeta = numeroDeTarjeta;
		this.cvv = cvv;
		this.fechaDeVencimiento = fechaVencimiento;
		this.apiTJT = api;
	}
	
	
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
