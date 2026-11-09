package reserva;

public class Cancelada extends Estado {

	@Override
	public String getEstado() {
		return "Reserva cancelada";
	}
	
    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.EN_CHECKIN;
    }   
	
	

}
