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
    	reserva.restaurarServicios();
    	reserva.generarVoucher();
    
    	// SE TIENE QUE REMBOLSAR EL COSTO TOTAL DE LOS SERVICIOS Y DE LAS TASAS
    	
        reserva.cambiarEstado(new Cancelada());
    }

    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.EN_CHECKIN;
    }   
}
