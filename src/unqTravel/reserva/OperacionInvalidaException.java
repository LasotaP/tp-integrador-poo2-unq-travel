package unqTravel.reserva;

/**
 * Excepcion de dominio: se intento una operacion que el estado actual de la reserva no permite.
 */
public class OperacionInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OperacionInvalidaException(String operacion, String estado) {
        super("No se puede " + operacion + " en estado " + estado);
    }
}
