package unqTravel.pago;

import java.time.LocalDate;

public interface TarjetaDeCreditoAPI {
	
	public void validarDatos(String numeroDeTarjeta, String cvv, LocalDate fechaDeVencimiento);
	public void preAutorizacion(double monto);
	public void transferir(double monto);
}

