package unqTravel.catalogo.atributos;

public class TipoNumerico implements TipoValor {
    public boolean esConsistente(String valor) {
        try {
            Double.valueOf(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
