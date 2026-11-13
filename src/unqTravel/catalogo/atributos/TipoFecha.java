package unqTravel.catalogo.atributos;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TipoFecha implements TipoValor {
    public boolean esConsistente(String valor) {
        if (valor == null) {          // por las dudas(?
            return false;
        }
        try {
            LocalDate.parse(valor);   //intenta convertir el texto en un objeto LocalDate, espera el formato yyyy-mm-dd (ejem. "2026-11-12")
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}