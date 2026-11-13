package unqTravel;

import servicios.Servicio;

public class FilaServicioReservado {
	private Servicio servicio;
	private Integer unidadesVendidas;
	private Double precioPromedio;
	
	public FilaServicioReservado(Servicio servicio, Integer unidadesVendidas, Double precioPromedio) {
		this.setServicio(servicio);
		this.setUnidadesVendidas(unidadesVendidas);
		this.setPrecioPromedio(precioPromedio);
	}

	public Servicio getServicio() {
		return servicio;
	}

	private void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Integer getUnidadesVendidas() {
		return unidadesVendidas;
	}

	private void setUnidadesVendidas(Integer unidadesVendidas) {
		this.unidadesVendidas = unidadesVendidas;
	}

	public Double getPrecioPromedio() {
		return precioPromedio;
	}

	public void setPrecioPromedio(Double precioPromedio) {
		this.precioPromedio = precioPromedio;
	}
	
	


}
