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

public class FormatoTextoPlano implements FormatoReporte {

    @Override
    public String formatearMasReservados(ReporteMasReservados r) {
        StringBuilder sb = encabezado(r);
        int pos = 1;
        for (FilaServicioReservado f : r.getFilas()) {
            sb.append(String.format(Locale.ROOT, "%d. %s | unidades: %d | precio promedio: $%.2f%n",
                    pos++, f.getServicio().getNombre(), f.getUnidadesVendidas(), f.getPrecioPromedio()));
        }
        return sb.toString();
    }

    @Override
    public String formatearOcupacion(ReporteOcupacionCupo r) {
        StringBuilder sb = encabezado(r);
        for (FilaOcupacion f : r.getFilas()) {
            sb.append(String.format(Locale.ROOT, "%s | total: %d | reservados: %d | libres: %d%n",
                    f.getCategoria(), f.getTotal(), f.getReservados(), f.getLibres()));
        }
        return sb.toString();
    }

    @Override
    public String formatearExposicion(ReporteExposicionProveedor r) {
        StringBuilder sb = encabezado(r);
        for (FilaExposicion f : r.getFilas()) {
            sb.append(String.format(Locale.ROOT, "%s | servicios: %d | %.1f%% del catálogo%n",
                    f.getProveedor(), f.getCantidadServicios(), f.getPorcentaje()));
        }
        return sb.toString();
    }

    private StringBuilder encabezado(Reporte r) {
        return new StringBuilder(r.getTitulo()).append(System.lineSeparator())
                .append("=".repeat(r.getTitulo().length())).append(System.lineSeparator());
    }
}
