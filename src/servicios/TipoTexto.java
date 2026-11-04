package servicios;

public class TipoTexto implements TipoDeDato {

	@Override
	public boolean esValido(TipoDeDato dato) {
		return String.class.isInstance(dato);
	}

}
