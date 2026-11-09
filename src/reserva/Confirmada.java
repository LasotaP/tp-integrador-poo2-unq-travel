package reserva;

public class Confirmada extends Estado {

	@Override
	public String getEstado() {
		return "Reserva confirmada/pagada";
	}
	
	@Override
	public void hacerCheckIn(Reserva reserva) {
		reserva.cambiarEstado(new EnCheckIn());
	}
	
	@Override
	public void cancelar(Reserva reserva) {
		reserva.restaurarCupos();
		reserva.setEstado(new Cancelada());
		
	}
	
    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.CONFIRMADA;
    }   

}
