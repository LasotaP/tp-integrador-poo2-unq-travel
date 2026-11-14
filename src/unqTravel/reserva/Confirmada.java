package unqTravel.reserva;

public class Confirmada extends Estado {

	
	@Override
	public void hacerCheckIn(Reserva reserva) {
		reserva.cambiarEstado(new EnCheckIn());
	}
	
	@Override
	public void cancelar(Reserva reserva) {
		reserva.restaurarServicios();
		reserva.cambiarEstado(new Cancelada());
	}
	
    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.CONFIRMADA;
    }   

}
