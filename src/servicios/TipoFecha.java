package servicios;

import java.time.LocalDate;

public class TipoFecha implements TipoDeDato {
	
	public LocalDate valor;
	public TipoFecha(LocalDate valor) {
		this.valor = valor;
		esValido();
	}

	@Override
	public boolean esValido() {
		return valor != null;
	}

}
