package servicios.reportes;

public class FilaExposicion {
    private final String proveedor;
    private final int cantidadServicios;
    private final double porcentaje; // 0..100

    public FilaExposicion(String proveedor, int cantidadServicios, double porcentaje) {
        this.proveedor = proveedor;
        this.cantidadServicios = cantidadServicios;
        this.porcentaje = porcentaje;
    }

    public String getProveedor() { return proveedor; }
    public int getCantidadServicios() { return cantidadServicios; }
    public double getPorcentaje() { return porcentaje; }
}
