package servicios.cicloDeVida;

import java.time.LocalDate;

import servicios.Servicio;

public abstract class Estado {
	
	
	
	public abstract String getEstado();
	
	
	public void validarSiPuedoAgregarOQuitarServicios() {
		
		throw new IllegalArgumentException("No se puede agregar o quitar servicios en el estado" + getEstado());
	}
	
	public void confirmar(Reserva reserva) {
		throw new IllegalArgumentException("No se puede confirmar la reserva");
	}
	
	public void cancelar(Reserva reserva) {
		throw new IllegalArgumentException("No se puede cancelar la reserva");
	}
	
	public void registrar(Reserva reserva) {
		throw new IllegalArgumentException("No se puede registrar la reserva");
	}
	public void addServicio(Reserva reserva, Servicio servicio, LocalDate dia,Integer cupos) {
		
	}
	
}
