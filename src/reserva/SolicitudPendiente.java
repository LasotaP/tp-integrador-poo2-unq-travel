package reserva;

import servicios.Servicio;

public class SolicitudPendiente extends Estado{
	
	@Override
	public String getEstado() {
		return "Solicitud pendiente";
	}
	
	/*@Override
	public void confirmar(Reserva reserva) {
		reserva.validarCupo();
		reserva.decrementarCupo();
		reserva.setEstado(new ConfirmadaPagada());
		
	}
	*/
	
    @Override
    public void agregarServicio(Reserva reserva, Servicio servicio) {
        reserva.registrarServicio(servicio);
    }

    @Override
    public void quitarServicio(Reserva reserva, Servicio servicio) {
        reserva.descartarServicio(servicio);
    }

    @Override
    public void confirmar(Reserva reserva) {
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
	
	
	

