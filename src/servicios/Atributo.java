package servicios;

public class Atributo<T> {
    private String nombre;
    private boolean esObligatorio;
    private T valor;

    public Atributo(String nombre, boolean esObligatorio, T valor){
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
