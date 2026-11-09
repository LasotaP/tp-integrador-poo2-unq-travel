package servicios;

public class AtributoDinamico {
    private final String nombre;
    private final boolean obligatorio;
    private final TipoValor tipo;
    private String valor;

    public AtributoDinamico(String nombre, boolean obligatorio, TipoValor tipo) {
        validarTexto(nombre, "nombre");
        validarNoNulo(tipo, "tipo");

        this.nombre = nombre;
        this.obligatorio = obligatorio;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public boolean esObligatorio() { return obligatorio; }
    public TipoValor getTipo() { return tipo; }
    public String getValor() { return valor; }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean tieneValor() {
        return valor != null;
    }

    public boolean esValido() {
        if (!tieneValor()) {
            return !obligatorio;
        }
        return tipo.esConsistente(valor);
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ServicioInvalidoException("El campo " + campo + " es obligatorio");
        }
    }

    private void validarNoNulo(Object valor, String campo) {
        if (valor == null) {
            throw new ServicioInvalidoException("El campo " + campo + " es obligatorio");
        }
    }
}