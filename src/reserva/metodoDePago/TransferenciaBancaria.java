package reserva.metodoDePago;

import reserva.Reserva;

public class TransferenciaBancaria extends MetodoDePago{
	private String CBU;
	private String alias;
	private TransferenciaBancariaAPI apiTB;
	
	@Override
	public void validarDatos(Reserva reserva) {
		apiTB.validarDatos(CBU, alias);
	}
	
	@Override
	public void ejecutarTransaccion(Reserva reserva) {
		this.apiTB.ejecutar(reserva.getCostoTotal());
	}
}
