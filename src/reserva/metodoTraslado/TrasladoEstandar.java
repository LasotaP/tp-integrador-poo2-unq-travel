package reserva.metodoTraslado;

import reserva.Reserva;
import servicios.ServicioIndividual;

public class TrasladoEstandar implements MetodoTraslado {
	private PlataformaTransporteAPI plataforma;
	private ServicioIndividual servicio;

	public TrasladoEstandar(PlataformaTransporteAPI plataforma, ServicioIndividual servicio) {
		this.plataforma = plataforma;
		this.servicio = servicio;
	}
	
	@Override
	public double getCostoTraslado(Reserva reserva) {
		return plataforma.estimarTraslado(reserva.getCantidadPasajeros(servicio), servicio.getDireccion());
	}
	
	
}
