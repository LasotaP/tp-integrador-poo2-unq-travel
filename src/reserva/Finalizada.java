package reserva;

/** Estado terminal: no permite ninguna operacion. */
public class Finalizada extends Estado {

    @Override
    public CodigoEstado codigo() {
        return CodigoEstado.FINALIZADA;
    }

	@Override
	public String getEstado() {
		return "Reserva finalizada";
	}
}
