package servicios.cicloDeVida;

public class SolicitudPendiente extends Estado{
	
	@Override
	public String getEstado() {
		return "Solicitud pendiente";
	}
	
	@Override
	public void confirmar(Reserva reserva) {
		reserva.validarCupo();
		reserva.decrementarCupo();
		reserva.setEstado(new ConfirmadaPagada());
		
	}
	
	@Override
	public void cancelar(Reserva reserva) {
		reserva.setEstado(new Cancelada());
	}
	
	
	
}
