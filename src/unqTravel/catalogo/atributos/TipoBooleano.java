package unqTravel.catalogo.atributos;

public class TipoBooleano implements TipoValor {
    public boolean esConsistente(String valor) {
        return "true".equalsIgnoreCase(valor) || "false".equalsIgnoreCase(valor);  //compara dos textos sin distinguir mayusculas de minusculas
    }
}

// el "true" y "false" van adelante por si valor es null