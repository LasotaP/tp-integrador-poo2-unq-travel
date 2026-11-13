package unqTravel.reportes.formato;

import java.util.Locale;

import unqTravel.reportes.FilaExposicion;
import unqTravel.reportes.FilaOcupacion;
import unqTravel.reportes.FilaServicioReservado;
import unqTravel.reportes.FormatoReporte;
import unqTravel.reportes.Reporte;
import unqTravel.reportes.ReporteExposicionProveedor;
import unqTravel.reportes.ReporteMasReservados;
import unqTravel.reportes.ReporteOcupacionCupo;

public class FormatoHTML implements FormatoReporte {

    @Override
    public String formatearMasReservados(ReporteMasReservados r) {
        StringBuilder sb = abrir(r, "Servicio", "Unidades", "Precio promedio");
        for (FilaServicioReservado f : r.getFilas()) {
            fila(sb, f.getServicio().getNombre(), String.valueOf(f.getUnidadesVendidas()),
                    String.format(Locale.ROOT, "$%.2f", f.getPrecioPromedio()));
        }
        return cerrar(sb);
    }

    @Override
    public String formatearOcupacion(ReporteOcupacionCupo r) {
        StringBuilder sb = abrir(r, "Categoría", "Total", "Reservados", "Libres");
        for (FilaOcupacion f : r.getFilas()) {
            fila(sb, String.valueOf(f.getCategoria()), String.valueOf(f.getTotal()),
                    String.valueOf(f.getReservados()), String.valueOf(f.getLibres()));
        }
        return cerrar(sb);
    }

    @Override
    public String formatearExposicion(ReporteExposicionProveedor r) {
        StringBuilder sb = abrir(r, "Proveedor", "Servicios", "% del catálogo");
        for (FilaExposicion f : r.getFilas()) {
            fila(sb, f.getProveedor(), String.valueOf(f.getCantidadServicios()),
                    String.format(Locale.ROOT, "%.1f%%", f.getPorcentaje()));
        }
        return cerrar(sb);
    }

    private StringBuilder abrir(Reporte r, String... columnas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>").append(esc(r.getTitulo())).append("</h1>\n<table>\n<tr>");
        for (String c : columnas) sb.append("<th>").append(esc(c)).append("</th>");
        return sb.append("</tr>\n");
    }

    private void fila(StringBuilder sb, String... celdas) {
        sb.append("<tr>");
        for (String c : celdas) sb.append("<td>").append(esc(c)).append("</td>");
        sb.append("</tr>\n");
    }

    private String cerrar(StringBuilder sb) {
        return sb.append("</table>\n").toString();
    }

    private String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
