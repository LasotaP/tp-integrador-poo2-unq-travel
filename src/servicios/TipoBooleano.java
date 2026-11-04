package servicios;

public class TipoBooleano implements TipoDeDato{

	@Override
	public boolean esValido(TipoDeDato dato) {
		return Boolean.class.isInstance(dato);
	}

}
