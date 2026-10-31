package servicios.cicloDeVida;

public class ConfirmadaPagada extends Estado {

	@Override
	public String getEstado() {
		return "Reserva pagada";
	}
	
	@Override
	public void registrar(Reserva reserva) {
		reserva.setEstado(new EnCheckIn());
	}
	
	@Override
	public void cancelar(Reserva reserva) {
		reserva.restaurarCupos();
		reserva.setEstado(new Cancelada());
		
	}

}
