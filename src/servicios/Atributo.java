package servicios;

public class Atributo {
    
	private TipoDeDato valor;
	private String nombre;
    private boolean esObligatorio;

    public Atributo(String nombre, boolean esObligatorio, TipoDeDato valor){
        this.nombre = nombre;
        this.esObligatorio = esObligatorio;
        this.valor = valor;

        validarAtributo();
    }

    private void validarAtributo(){
        if(nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Error: nombre invalido");
        }
        if(valor == null) {
            throw new IllegalArgumentException("Error: valor del atributo invalido");
        }
    }

}
