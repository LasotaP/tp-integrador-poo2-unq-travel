package unqTravel.reserva;

public class SolicitudPendiente extends Estado{
		
    @Override
    public void agregarItemReserva(Reserva reserva, ItemReserva itemReserva) {
        reserva.agregarItem(itemReserva);
    }

    @Override
    public void quitarItemReserva(Reserva reserva, ItemReserva itemReserva) {
        reserva.quitarItem(itemReserva);
    }

    @Override
    public void confirmar(Reserva reserva) {
    	reserva.reservarServicios();
        reserva.cambiarEstado(new Confirmada());
    }

    @Override
    public void cancelar(Reserva reserva) {
        reserva.cambiarEstado(new Cancelada());
    }

    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.SOLICITUD_PENDIENTE;
    }
    
}
	
	
	

