package unqTravel.reportes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReporteExposicionProveedor implements Reporte {
    private final List<FilaExposicion> filas;

    public ReporteExposicionProveedor(List<FilaExposicion> filas) {
        this.filas = new ArrayList<>(filas);
    }

    public List<FilaExposicion> getFilas() {
        return Collections.unmodifiableList(filas);
    }

    @Override
    public String getTitulo() {
        return "Exposición por proveedor";
    }

    @Override
    public String aceptar(FormatoReporte formato) {
        return formato.formatearExposicion(this);
    }
}
