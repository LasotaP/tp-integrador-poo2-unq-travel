package reserva;

import java.time.LocalDate;
import java.util.*;

import notificacion.comprobante.Voucher;
import servicios.Servicio;

public class Reserva {
	private Estado estado;
	private List<ItemReserva> itemsReserva;
	private Voucher voucherCredito;
    private final Cliente cliente;
    private final List<ObservadorReserva> observadores;
	
	public Reserva(Estado estado, Cliente cliente) {
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
		this.estado = new SolicitudPendiente();
		this.itemsReserva = new ArrayList<ItemReserva>(); 
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

	/*public void validarCupo() {
		servicios.forEach((servicio,cantidad)-> {
			if (!servicio.hayCupo(cantidad)){
				throw new IllegalArgumentException("No hay cupos suficientes de " + servicio.getNombre() + ", quedan" + servicio.cupoDisponible() );
			}
		});
	
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
    
	public void restaurarServicios() {
		this.itemsReserva.stream()
		 				 .forEach(i -> i.restaurarCupo());		
	}
	
    public List<ItemReserva> getItemsReserva() {
        return Collections.unmodifiableList(itemsReserva);
    }
	
	public void generarVoucher() {
		//new Voucher(LocalDate.now(),cliente,itemsReserva, itemsReserva.stream().max(fecFin));
	}
	
}
