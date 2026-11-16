package unqTravel.reserva;

import java.time.LocalDate;

import unqTravel.catalogo.Servicio;

public class ItemReserva {
	private LocalDate fecIni;
	private LocalDate fecFin;
	private Integer cantidad;
	private Servicio servicio;
	
	public ItemReserva (LocalDate fecIni, LocalDate fecFin, Integer cantidad, Servicio servicio) {
		this.fecIni = fecIni;
		this.fecFin = fecFin;
		this.cantidad = cantidad;
		this.servicio = servicio;
		
		validar();
	}
	
	public void reservarCupo() {
		servicio.reservarCupo(fecIni, fecFin, cantidad);
	}
	
	public void restaurarCupo() {
		servicio.restaurarCupo(fecIni, fecFin, cantidad);
	}
	
	public double costoTotalaItem() {
		return servicio.getPrecioFinal() * this.cantidad;
	}
	
	public Servicio getServicio() {
		return this.servicio;
	}
	
	public Integer getCantidadPasajeros() {
		return cantidad;
	}
	
	public LocalDate getFechaFin() {
		return this.fecFin;
	}
	
	public LocalDate getFechaInicio() {
		return this.fecIni;
	}
	
	public boolean estaEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
		return !getFechaFin().isBefore(fechaInicio) && !getFechaInicio().isAfter(fechaFin);
	}
	
	private void validar() {
		if(fecIni == null || fecFin == null) {
			throw new IllegalArgumentException("Las fechas no pueden ser nulas");
		}
		if(fecFin.isBefore(fecIni)) {
			throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
		}
		if(cantidad == null || cantidad < 1 ) {
			throw new IllegalArgumentException("Error: no podes reservar 0 o menos lugares");
		}
		if(servicio == null) {
			throw new IllegalArgumentException("El servicio no puede ser nulo");
		}
	}
	
}
