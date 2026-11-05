package servicios;

public class TipoTexto implements TipoDeDato {
	
	public String valor;
	
	public TipoTexto(String valor) {
		//doble encapsulamiento con set y que setter valide
		this.valor = valor;
		
	}

	@Override
	public boolean esValido() {
		return valor != null && !valor.isBlank();
	}

}
