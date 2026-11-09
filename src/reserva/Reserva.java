package reserva;

import java.util.*;

import servicios.Servicio;

public class Reserva {
	private Estado estado;
	private List<Servicio> servicios;
    private final Cliente cliente;
    private final List<ObservadorReserva> observadores;
	
	public Reserva(Estado estado, Cliente cliente) {
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
		this.estado = new SolicitudPendiente();
		this.servicios = new ArrayList<Servicio>(); 
        this.observadores = new ArrayList<>();
		
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
	
	public Cliente getCliente() {
		return cliente;
	}
	
    public void confirmar() {
        estado.confirmar(this);
    }

    public void hacerCheckIn() {
        estado.hacerCheckIn(this);
    }

    public void iniciarViaje() {
        estado.iniciarViaje(this);
    }

    public void finalizarViaje() {
        estado.finalizarViaje(this);
    }

    public void cancelar() {
        estado.cancelar(this);
    }
    
    public void agregarObservador(ObservadorReserva observador) {
        observadores.add(Objects.requireNonNull(observador, "El observador no puede ser nulo"));
    }

    public void quitarObservador(ObservadorReserva observador) {
        observadores.remove(observador);
    }

    public CodigoEstado codigoEstado() {
        return estado.codigo();
    }
    
    // Visibilidad de paquete: solo los estados (mismo paquete, sino me sale que no se usa) pueden modificar la reserva.
    void cambiarEstado(Estado nuevoEstado) {
        Estado anterior = this.estado;
        this.estado = Objects.requireNonNull(nuevoEstado, "El estado no puede ser nulo");
        observadores.forEach(observador -> observador.estadoCambiado(this, anterior, nuevoEstado));
    }
    
    void registrarServicio(Servicio servicio) {
        servicios.add(Objects.requireNonNull(servicio, "El item no puede ser nulo"));
    }

    void descartarServicio(Servicio servicio) {
        servicios.remove(servicio);
    }

	public void restaurarCupos() {
		// TODO Auto-generated method stub
		
	}
	
    public List<Servicio> getServicios() {
        return Collections.unmodifiableList(servicios);
    }
	
	
	
}
