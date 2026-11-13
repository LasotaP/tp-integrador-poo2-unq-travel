package servicios.reportes;

/**
 * Elemento del Visitor. El reporte solo conoce QUE datos recolecta,
 * nunca COMO se muestran: eso lo decide el FormatoReporte que lo visita.
 */
public interface Reporte {
    String getTitulo();
    String aceptar(FormatoReporte formato);
}
