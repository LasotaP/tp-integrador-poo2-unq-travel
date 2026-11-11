package reserva.metodoDePago;

public interface TransferenciaBancariaAPI {

	public void validarDatos(String cbu, String alias);
	public void ejecutar(double monto);
}
