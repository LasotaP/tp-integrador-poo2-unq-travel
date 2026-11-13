package servicios.reportes;

import java.util.*;

import servicios.PaqueteTuristico;
import servicios.ServicioIndividual;

/**
 * ConcreteVisitor del patrón Visitor: recorre el catálogo y cuenta cuántos servicios
 * individuales aporta cada proveedor. Con esos datos se arma el reporte de
 * "Exposición por proveedor" (riesgo de concentración).
 */
public class VisitanteExposicion implements Visitante {

    // Acumulador: proveedor -> cantidad de servicios individuales que aporta.
    // LinkedHashMap mantiene el orden en que se fueron encontrando los proveedores.
    private final Map<String, Integer> porProveedor = new LinkedHashMap<>();

    // Servicios que ya conté. Es un Set por identidad (==) y no por equals(), así que
    // si un mismo objeto servicio aparece en varios paquetes, solo se cuenta una vez.
    // (Collections.newSetFromMap permite armar un Set a partir de un IdentityHashMap.)
    private final Set<ServicioIndividual> vistos = Collections.newSetFromMap(new IdentityHashMap<>());

    // Se invoca cuando el visitor "llega" a un servicio individual (via servicio.aceptar(this)).
    @Override
    public void visitarServicio(ServicioIndividual servicio) {

        // add() devuelve false si el servicio ya estaba en el Set:
        // así evito contar dos veces un servicio repetido dentro de distintos paquetes.
        if (vistos.add(servicio)) {

            // Sumo 1 al contador de su proveedor. Si el proveedor todavía no estaba
            // en el mapa, merge lo crea con valor 1; si estaba, le suma 1 (Integer::sum).
            porProveedor.merge(servicio.getProveedor(), 1, Integer::sum);
        }
    }

    // Se invoca al llegar a un paquete. No hace nada porque el paquete no tiene
    // proveedor propio: lo que cuenta son los servicios que contiene. Esos llegan
    // igual, porque PaqueteTuristico.aceptar reenvía la visita a sus hijos.
    @Override
    public void visitarPaquete(PaqueteTuristico paquete) {
        // sin accion: los hijos son visitados por paquete.aceptar(...)
    }

    // Convierte lo acumulado en filas listas para armar el ReporteExposicionProveedor.
    public List<FilaExposicion> getFilas() {

        // Total de servicios individuales distintos del catálogo (suma de todos los contadores).
        // Es el 100% sobre el que se calcula cada porcentaje.
        int total = porProveedor.values().stream().mapToInt(Integer::intValue).sum();

        List<FilaExposicion> filas = new ArrayList<>();

        // Una fila por proveedor: nombre, cantidad de servicios y porcentaje del catálogo
        // (cantidad * 100 / total; el 100.0 fuerza división decimal y no entera).
        porProveedor.forEach((prov, cant) -> filas.add(new FilaExposicion(prov, cant, cant * 100.0 / total)));

        // Ordeno de mayor a menor porcentaje: los proveedores de los que más depende
        // el catálogo (mayor riesgo de concentración) quedan primero.
        filas.sort(Comparator.comparingDouble(FilaExposicion::getPorcentaje).reversed());

        return filas;
    }
}
