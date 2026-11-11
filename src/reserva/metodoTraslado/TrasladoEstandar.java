package reserva.metodoTraslado;

import reserva.Reserva;

public class TrasladoEstandar implements Traslado {
	private PlataformaTransporte plataforma;

	public TrasladoEstandar(PlataformaEstandar plataforma) {
		this.plataforma = plataforma;
	}
	
	@Override
	public double calcularCosto(Reserva reserva) {
		return plataforma.estimarTraslado(reserva.getCantidadPasajeros(), Direccion direccionDestino);
	}
	
	
	
}
