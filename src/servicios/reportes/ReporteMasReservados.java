package servicios.reportes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ConcreteElement del patrón Visitor (sobre los reportes): contiene los datos del
 * reporte de "Servicios más reservados". Solo guarda información; no sabe cómo se
 * presenta (texto, CSV, HTML): eso lo resuelve el FormatoReporte.
 */
public class ReporteMasReservados implements Reporte {

    // Una fila por servicio vendido, con sus unidades y su precio promedio cobrado.
    // Ya llegan ordenadas de mayor a menor cantidad de unidades: ese cálculo y ese
    // orden los hace UnqTravel.generarReporteServiciosMasReservados antes de crear el reporte.
    // Es final: el reporte es una "foto" ya calculada y no cambia después.
    private final List<FilaServicioReservado> filas;

    public ReporteMasReservados(List<FilaServicioReservado> filas) {

        // Copia defensiva: guardo una lista propia en vez de la que me pasaron.
        // Así, si quien creó el reporte modifica su lista después, mi reporte no cambia.
        this.filas = new ArrayList<>(filas);
    }

    public List<FilaServicioReservado> getFilas() {

        // Devuelvo una vista de solo lectura: se puede recorrer pero no modificar
        // (agregar o quitar filas lanzaría UnsupportedOperationException).
        // Protege el encapsulamiento de la lista interna.
        return Collections.unmodifiableList(filas);
    }

    // Título que cada formato muestra como encabezado del reporte.
    @Override
    public String getTitulo() {
        return "Servicios más reservados";
    }

    // Método "accept" del Visitor. Es el double dispatch:
    //  1) el cliente llama reporte.aceptar(formato) sin saber qué reporte es;
    //  2) acá el reporte se identifica llamando a formatearMasReservados(this),
    //     el método del formato pensado para este tipo de reporte.
    // No hace falta ningún if/instanceof, y el reporte solo depende de la interfaz
    // FormatoReporte, nunca de un formato concreto.
    @Override
    public String aceptar(FormatoReporte formato) {
        return formato.formatearMasReservados(this);
    }
}