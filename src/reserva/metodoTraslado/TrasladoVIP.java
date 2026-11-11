package reserva.metodoTraslado;

import reserva.Reserva;

public class TrasladoVIP implements MetodoTraslado{
	private TrasladoVIPAPI trasladoVipApi;

	@Override
	public double getCostoTraslado(Reserva reserva) {
		return trasladoVipApi.calcularCosto(reserva.getCostoReserva());
	}
}
