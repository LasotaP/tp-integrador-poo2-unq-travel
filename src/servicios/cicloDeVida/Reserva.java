package servicios.cicloDeVida;

import java.util.*;

import servicios.Servicio;

public class Reserva {
	private Estado estado;
	private List<Servicio> servicios;
	
	public Reserva(Estado estado) {
		this.estado = new SolicitudPendiente();
		this.servicios = new ArrayList<Servicio>(); 
		
	}
	
	public void añadirServicios(Servicio servicio, Integer cantidad) {
		//validar
		//Si ya existia en el map el item que estoy agregando, recupero el stock con getOrDefault y le sumo la cantidad pasada por parametros
		//si no existia getOrDefault te devuelve 0, y le sumo la cantidad. 
		
		this.servicios.add(servicio);
		
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
