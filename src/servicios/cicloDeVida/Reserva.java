package servicios.cicloDeVida;

import java.time.LocalDate;
import java.util.*;

import servicios.Servicio;

public class Reserva {
	private Estado estado;
	private List<Servicio> servicios; // crear nueva clase itemReserva
	private List<LocalDate> diasDeReserva;
	
	
	public Reserva(Estado estado,ArrayList<LocalDate> diasDeReserva) {
		
		this.estado = new SolicitudPendiente();
		this.servicios = new ArrayList<Servicio>(); 
		this.diasDeReserva = diasDeReserva;
		
		
	}
	

	public void añadirServicios(Servicio servicio, LocalDate dia,Integer cupos) {
		this.estado.addServicio(this,servicio,dia,cupos);
		
	}
	public void quitarServicio(Servicio servicio) {
		if(servicios.isEmpty()) {
			throw new IllegalArgumentException("No se pueden quitar servicios: la reserva actual está vacía."); 
		}
		
		if(!servicios.contains(servicio)) {
			throw new IllegalArgumentException("El servicio que intentás remover no se encuentra en esta reserva.");			
		}
		
		estado.validarSiPuedoAgregarOQuitarServicios();
		this.servicios.remove(servicio);
	}
	
	public void setEstado(Estado nuevoEstado) {
		this.estado = nuevoEstado;
	}
	
	/*public void validarCupo() {
		servicios.forEach((servicio,cantidad)-> {
			if (!servicio.hayCupo(cantidad)){
				throw new IllegalArgumentException("No hay cupos suficientes de " + servicio.getNombre() + ", quedan" + servicio.cupoDisponible() );
			}
		});
	
	}

	public void decrementarCupo() {
		servicios.forEach((servicio,cantidad) -> servicio.decrementarCupo(cantidad));
	}

	public void restaurarCupos() {
		servicios.forEach((servicio,cantidad) -> servicio.aumentarCupo(cantidad));
	}
	*/
	
}
