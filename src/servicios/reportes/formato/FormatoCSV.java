package servicios.reportes.formato;

import java.util.Locale;

import servicios.reportes.*;
import servicios.reportes.FilaServicioReservado;

public class FormatoCSV implements FormatoReporte {

    @Override
    public String formatearMasReservados(ReporteMasReservados r) {
        StringBuilder sb = new StringBuilder("servicio,unidades,precioPromedio\n");
        for (FilaServicioReservado f : r.getFilas()) {
            sb.append(esc(f.getServicio().getNombre())).append(',')
              .append(f.getUnidadesVendidas()).append(',')
              .append(String.format(Locale.ROOT, "%.2f", f.getPrecioPromedio())).append('\n');
        }
        return sb.toString();
    }

    @Override
    public String formatearOcupacion(ReporteOcupacionCupo r) {
        StringBuilder sb = new StringBuilder("categoria,total,reservados,libres\n");
        for (FilaOcupacion f : r.getFilas()) {
            sb.append(esc(String.valueOf(f.getCategoria()))).append(',')
              .append(f.getTotal()).append(',')
              .append(f.getReservados()).append(',')
              .append(f.getLibres()).append('\n');
        }
        return sb.toString();
    }

    @Override
    public String formatearExposicion(ReporteExposicionProveedor r) {
        StringBuilder sb = new StringBuilder("proveedor,servicios,porcentaje\n");
        for (FilaExposicion f : r.getFilas()) {
            sb.append(esc(f.getProveedor())).append(',')
              .append(f.getCantidadServicios()).append(',')
              .append(String.format(Locale.ROOT, "%.1f", f.getPorcentaje())).append('\n');
        }
        return sb.toString();
    }

    private String esc(String valor) {
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
