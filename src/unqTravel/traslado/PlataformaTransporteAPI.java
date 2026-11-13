package unqTravel.traslado;

import unqTravel.catalogo.Direccion;

public interface PlataformaTransporteAPI {
	public double estimarTraslado(int cantidadPasajeros, Direccion direccionDestino);
}
