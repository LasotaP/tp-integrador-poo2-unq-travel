package unqTravel.pago;

public interface BilleteraVirtualAPI {
	
	public void validarSaldoSuficiente(double monto);
	public void bloquearSaldo(double monto);
	public void acreditar(double monto);
	public void notificar();
}
