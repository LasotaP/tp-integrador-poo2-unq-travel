package servicios;

import java.time.LocalDate;

public class TipoFecha implements TipoDeDato {

	@Override
	public boolean esValido(TipoDeDato dato) {
		return LocalDate.class.isInstance(dato);
	}

}
