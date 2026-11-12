package servicios.catalogo;

import servicios.Servicio;

public class CriterioPrecio implements CriterioBusqueda{
	
	private double precioMaximo;
	
	public CriterioPrecio(double precioMaximo) {
		this.precioMaximo = precioMaximo;
	}

	@Override
	public boolean cumpleCriterio(Servicio servicio) {
		return servicio.getPrecioFinal() <= precioMaximo;
	}
	
	
}
