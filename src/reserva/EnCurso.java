package reserva;

public class EnCurso extends Estado {

    @Override
    public void finalizarViaje(Reserva reserva) {
        reserva.cambiarEstado(new Finalizada());
    }

    @Override
    public void cancelar(Reserva reserva) {
        reserva.cambiarEstado(new Cancelada());
    }

    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.EN_CURSO;
    }

	@Override
	public String getEstado() {
		return "Reserva en curso";
	}
}
