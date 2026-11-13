package reserva;

import java.time.LocalDate;

import servicios.Servicio;

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
		
		// Validar fechas
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
	
	
}
