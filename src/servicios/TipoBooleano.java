package servicios;

public class TipoBooleano implements TipoDeDato{
	
	public Boolean valor;
	public TipoBooleano(boolean valor) {
		this.valor = valor;
		esValido();
	}

	@Override
	public boolean esValido() {
		return valor != null;
	}

}
