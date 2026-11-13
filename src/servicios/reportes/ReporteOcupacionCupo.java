package servicios.reportes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ConcreteElement del patrón Visitor (sobre los reportes): contiene los datos del
 * reporte de "Ocupación y cupo disponible" para un período. Solo guarda información;
 * no sabe cómo se presenta (texto, CSV, HTML): eso lo resuelve el FormatoReporte.
 */
public class ReporteOcupacionCupo implements Reporte {

    // Período que cubre el reporte (ambas fechas inclusive).
    // Son final: un reporte es una "foto" ya calculada y no cambia después.
    private final LocalDate desde;
    private final LocalDate hasta;

    // Una fila por categoría (Alojamiento, Vuelo, etc.) con cupo total, reservado y libre.
    private final List<FilaOcupacion> filas;

    public ReporteOcupacionCupo(LocalDate desde, LocalDate hasta, List<FilaOcupacion> filas) {
        this.desde = desde;
        this.hasta = hasta;

        // Copia defensiva: guardo una lista propia en vez de la que me pasaron.
        // Así, si quien creó el reporte modifica su lista después, mi reporte no cambia.
        this.filas = new ArrayList<>(filas);
    }

    // Los formatos necesitan las fechas para mostrarlas, por eso hay getters.
    public LocalDate getDesde() { return desde; }
    public LocalDate getHasta() { return hasta; }

    public List<FilaOcupacion> getFilas() {

        // Devuelvo una vista de solo lectura: quien use el reporte puede recorrer
        // las filas pero no agregarlas ni quitarlas (lanzaría UnsupportedOperationException).
        // Protege el encapsulamiento de la lista interna.
        return Collections.unmodifiableList(filas);
    }

    // Título que cada formato muestra como encabezado. Incluye el período
    // para que el reporte se entienda por sí solo.
    @Override
    public String getTitulo() {
        return "Ocupación y cupo disponible (" + desde + " a " + hasta + ")";
    }

    // Método "accept" del Visitor. Es el double dispatch:
    //  1) el cliente llama reporte.aceptar(formato) sin saber qué reporte es;
    //  2) acá el reporte se identifica llamando a formatearOcupacion(this),
    //     el método del formato pensado específicamente para este tipo de reporte.
    // Así no hace falta ningún if/instanceof, y el reporte no depende de ningún
    // formato concreto, solo de la interfaz FormatoReporte.
    @Override
    public String aceptar(FormatoReporte formato) {
        return formato.formatearOcupacion(this);
    }
}
