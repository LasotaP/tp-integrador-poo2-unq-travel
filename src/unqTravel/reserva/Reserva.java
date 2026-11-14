package unqTravel.reserva;

import java.time.LocalDate;
import java.util.*;

import unqTravel.catalogo.Direccion;
import unqTravel.catalogo.Servicio;
import unqTravel.catalogo.ServicioIndividual;
import unqTravel.comprobante.Comprobante;
import unqTravel.reserva.notificacion.ObservadorReserva;
import unqTravel.traslado.MetodoTraslado;

public class Reserva {
	private Estado estado;
	private List<ItemReserva> itemsReserva;
	private List<Comprobante> comprobantes;
	private MetodoTraslado metodoTraslado;
    private final Cliente cliente;
    private final List<ObservadorReserva> observadores;
    private String numeroReserva;
	
	public Reserva(Estado estado, Cliente cliente, Direccion direccion,String numeroReserva, MetodoTraslado metodo) {
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        this.metodoTraslado = Objects.requireNonNull(metodo, "El método de traslado no puede ser nulo");
		this.estado = new SolicitudPendiente();
		this.itemsReserva = new ArrayList<ItemReserva>(); 
		this.comprobantes = new ArrayList<Comprobante>();
        this.observadores = new ArrayList<>();
        this.numeroReserva = numeroReserva;
		
	}
	
	public void addItemReserva(ItemReserva itemReserva) {
		estado.validarSiPuedoAgregarOQuitarItemReserva();
		this.itemsReserva.add(itemReserva);
	}
	 
	public void quitarItemReserva(ItemReserva itemReserva) {
		if(this.itemsReserva.isEmpty()) {
			throw new IllegalArgumentException("No se pueden quitar servicios: la reserva actual está vacía."); 
		}
		
		if(!this.itemsReserva.contains(itemReserva)) {
			throw new IllegalArgumentException("El servicio que intentás remover no se encuentra en esta reserva.");			
		}
		
		estado.validarSiPuedoAgregarOQuitarItemReserva();
		this.itemsReserva.remove(itemReserva);
	}
	
	public void setEstado(Estado nuevoEstado) {
		this.estado = nuevoEstado;
	}

	public void reservarServicios() {
		List<ItemReserva> reservados = new ArrayList<>();
	    try {
	        for (ItemReserva item : itemsReserva) {
	            item.reservarCupo();
	            reservados.add(item);
	        }
	    } catch (RuntimeException e) {
	        reservados.forEach(ItemReserva::restaurarCupo);   // deshago solo lo que ya reservé
	        throw e;
	    }
	}
	
	public void addComprobante(Comprobante comprobante) {
		this.comprobantes.add(comprobante);
	}


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
        observadores.forEach(observador -> observador.notificar(this, anterior, nuevoEstado));
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
	
	public Integer getCantidadPasajeros(ServicioIndividual servicio) { 
		return getItemsReserva().stream()
								.filter(i -> i.getServicio().contiene(servicio))
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
		this.metodoTraslado = Objects.requireNonNull(metodo, "El método de traslado no puede ser nulo");
	}

	public String getNumeroReserva() {
		return numeroReserva;
	}
	
	public boolean estaPagada() { // Una reserva cuenta como venta si no está cancelada ni pendiente
		return this.codigoEstado() != CodigoEstado.CANCELADA
	            && this.codigoEstado() != CodigoEstado.SOLICITUD_PENDIENTE;
	}
	
	public List<ItemReserva> itemsDeReservaEntre(LocalDate fechaInicio, LocalDate fechaFin) {
		 return this.getItemsReserva().stream()
				 					  .filter(i -> i.estaEnPeriodo(fechaInicio,fechaFin))
				 					  .toList();		
	}
	
	
	
}
