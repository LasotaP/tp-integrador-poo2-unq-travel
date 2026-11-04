package servicios;

public class TipoNumerico implements TipoDeDato {

	@Override
	public boolean esValido(TipoDeDato dato) {
		
		return Number.class.isInstance(dato);
	}
	/*public boolean esFinito(TipoDeDato dato) {
		 double numero = dato.doubleValue();
		 return !Double.isNaN(numero);
	}*/
}
