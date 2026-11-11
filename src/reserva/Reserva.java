package reserva;

import java.time.LocalDate;
import java.util.*;

import notificacion.comprobante.Comprobante;
import reserva.metodoTraslado.MetodoTraslado;
import servicios.Direccion;
import servicios.ServicioIndividual;

public class Reserva {
	private Estado estado;
	private List<ItemReserva> itemsReserva;
	private List<Comprobante> comprobantes;
	private MetodoTraslado metodoTraslado;
    private final Cliente cliente;
    private final List<ObservadorReserva> observadores;
	
	public Reserva(Estado estado, Cliente cliente, Direccion direccion) {
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
		this.estado = new SolicitudPendiente();
		this.itemsReserva = new ArrayList<ItemReserva>(); 
		this.comprobantes = new ArrayList<Comprobante>();
        this.observadores = new ArrayList<>();
		
	}
	
	public void addItemReserva(ItemReserva itemReserva) {
		this.itemsReserva.add(itemReserva);
	}
	 
	public void quitarItemReserva(ItemReserva itemReserva) {
		if(this.itemsReserva.isEmpty()) {
			throw new IllegalArgumentException("No se pueden quitar servicios: la reserva actual está vacía."); 
		}
		
		if(!this.itemsReserva.contains(itemReserva)) {
			throw new IllegalArgumentException("El servicio que intentás remover no se encuentra en esta reserva.");			
		}
		
		estado.validarSiPuedoAgregarOQuitarServicios();
		this.itemsReserva.remove(itemReserva);
	}
	
	public void setEstado(Estado nuevoEstado) {
		this.estado = nuevoEstado;
	}

	public void reservarServicios() {
		this.itemsReserva.stream()
						 .forEach(i -> i.reservarCupo());
	}
	
	public void addComprobante(Comprobante comprobante) {
		this.comprobantes.add(comprobante);
	}

	/*public void validarCupo() {
		servicios.forEach((servicio,cantidad)-> {
			if (!servicio.hayCupo(cantidad)){
				throw new IllegalArgumentException("No hay cupos suficientes de " + servicio.getNombre() + ", quedan" + servicio.cupoDisponible() );
			}
		});
	
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
    
	public void restaurarServicios() {
		this.itemsReserva.stream()
		 				 .forEach(i -> i.restaurarCupo());		
	}
	
    public List<ItemReserva> getItemsReserva() {
        return Collections.unmodifiableList(itemsReserva);
    }
	
	
	public double getCostoReserva() {
		return itemsReserva.stream()
						   .mapToDouble(i -> i.costoTotalaItem())
						   .sum();
	}
	
	public Integer getCantidadPasajeros(ServicioIndividual servicio) { // Pido la cantidad maxima de pasajeros porque entiendo que es la cantidad que estan de "vacaciones". 
		return getItemsReserva().stream()
								.filter(i -> i.getServicio() == servicio)
								.mapToInt(ItemReserva::getCantidadPasajeros)
								.sum();
	}
	
	public LocalDate getFechaFinReserva() {
		return this.itemsReserva.stream()
								.map(ItemReserva::getFechaFin)
								.max(LocalDate::compareTo)
								.orElse(null);
								
	}
	
	public double getRembolso() {
		return this.itemsReserva.stream()
						   		.filter(i -> i.getFechaInicio() != null && !i.getFechaInicio().isBefore(LocalDate.now()))
						   		.mapToDouble(ItemReserva::costoTotalaItem)
						   		.sum();
	}

	public double getCostoTotal() {
		return getCostoReserva() + metodoTraslado.getCostoTraslado(this);
	}
	public void setMetodoDeTraslado(MetodoTraslado metodo) {
		this.metodoTraslado = metodo;
	}
	
}
