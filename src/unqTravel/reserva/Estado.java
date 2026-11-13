package unqTravel.reserva;

public abstract class Estado {
	
	/*
	 Rol State. Por defecto toda operacion es invalida y lanza una
	 OperacionInvalidaException, cada estado sobrescribe solo las permitidas.
	 */	
	
 	public void validarSiPuedoAgregarOQuitarItemReserva() {
		
		throw new IllegalArgumentException("No se puede agregar o quitar servicios en el estado" + this.codigo());
	}
	
	
    public void agregarItemReserva(Reserva reserva, ItemReserva itemReserva) {
        throw operacionInvalida("agregar servicios");
    }

    public void quitarItemReserva(Reserva reserva, ItemReserva itemReserva) {
        throw operacionInvalida("quitar servicios");
    }

    public void confirmar(Reserva reserva) {
        throw operacionInvalida("confirmar");
    }

    public void hacerCheckIn(Reserva reserva) {
        throw operacionInvalida("hacer el check-in");
    }

    public void iniciarViaje(Reserva reserva) {
        throw operacionInvalida("iniciar el viaje");
    }

    public void finalizarViaje(Reserva reserva) {
        throw operacionInvalida("finalizar el viaje");
    }

    public void cancelar(Reserva reserva) {
        throw operacionInvalida("cancelar");
    }

    public abstract CodigoEstado codigo();

    private OperacionInvalidaException operacionInvalida(String operacion) {
        return new OperacionInvalidaException(operacion, codigo().name());
    }

}
