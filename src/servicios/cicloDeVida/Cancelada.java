package servicios.cicloDeVida;

public class Cancelada extends Estado {

	@Override
	public String getEstado() {
		return "Reserva cancelada";
	}
	
	@Override
	public void registrar(Reserva reserva) {
		
	}
	
	

}
