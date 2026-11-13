package unqTravel.catalogo.atributos;

public class TipoTexto implements TipoValor {
    public boolean esConsistente(String valor) {
        return valor != null && !valor.isBlank();
    }
}
