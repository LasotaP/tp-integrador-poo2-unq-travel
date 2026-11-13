package unqTravel.reportes;

/**
 * Visitor concreto por formato de salida (texto plano, CSV, HTML, ...).
 * Agregar un formato nuevo = una clase nueva, sin tocar los reportes.
 */
public interface FormatoReporte {
    String formatearMasReservados(ReporteMasReservados reporte);
    String formatearOcupacion(ReporteOcupacionCupo reporte);
    String formatearExposicion(ReporteExposicionProveedor reporte);
}
