package unqTravel.catalogo.atributos;

@SuppressWarnings("serial")
public class ServicioInvalidoException extends RuntimeException {
    public ServicioInvalidoException(String mensaje) {
        super(mensaje);
    }
}