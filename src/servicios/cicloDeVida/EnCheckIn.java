package servicios.cicloDeVida;

public class EnCheckIn extends Estado {

	@Override
	public String getEstado() {
		return "Reserva registrada";
	}
	
	@Override
	public void confirmar(Reserva reserva) {
		
	}
}
