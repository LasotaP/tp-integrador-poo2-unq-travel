package reserva.metodoTraslado;

import servicios.Direccion;

public interface PlataformaTransporteAPI {
	public double estimarTraslado(int cantidadPasajeros, Direccion direccionDestino);
}
