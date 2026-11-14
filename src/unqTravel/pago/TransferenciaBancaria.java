package unqTravel.pago;

import unqTravel.reserva.Reserva;

public class TransferenciaBancaria extends MetodoDePago{
	private String CBU;
	private String alias;
	private TransferenciaBancariaAPI apiTB;
	
	public TransferenciaBancaria(String cbu, String alias, TransferenciaBancariaAPI api) {
		this.CBU = cbu;
		this.alias = alias;
		this.apiTB = api;
	}
	
	@Override
	public void validarDatos(Reserva reserva) {
		apiTB.validarDatos(CBU, alias);
	}
	
	@Override
	public void ejecutarTransaccion(Reserva reserva) {
		this.apiTB.ejecutar(reserva.getCostoTotal());
	}
}
