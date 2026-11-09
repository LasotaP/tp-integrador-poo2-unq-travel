package reserva;


public class EnCheckIn extends Estado {

	@Override
	public String getEstado() {
		return "Reserva registrada";
	}
	

    @Override
    public void iniciarViaje(Reserva reserva) {
        reserva.cambiarEstado(new EnCurso());
    }

    @Override
    public void cancelar(Reserva reserva) {
        reserva.cambiarEstado(new Cancelada());
    }

    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.EN_CHECKIN;
    }   
}
