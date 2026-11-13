package unqTravel.traslado;

import unqTravel.reserva.Reserva;

public class TrasladoRecepcion implements MetodoTraslado{

	@Override
	public double getCostoTraslado(Reserva reserva) {
		return 0;
	}
	
}
