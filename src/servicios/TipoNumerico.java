package servicios;

public class TipoNumerico implements TipoDeDato {
	
	public Integer valor; 
	public TipoNumerico(Integer valor) {
		this.valor = valor;
		esValido();
	}

	@Override
	public boolean esValido() {
		return valor != null && valor > 0;
	}
	/*public boolean esFinito(TipoDeDato dato) {
		 double numero = dato.doubleValue();
		 return !Double.isNaN(numero);
	}*/
}
