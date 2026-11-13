package unqTravel.reportes;

import java.time.LocalDate;
import java.util.*;

import unqTravel.catalogo.Categoria;
import unqTravel.catalogo.PaqueteTuristico;
import unqTravel.catalogo.ServicioIndividual;

/**
 * ConcreteVisitor del patrón Visitor (sobre el catálogo): recorre los servicios y
 * acumula, por categoría, el cupo total y el cupo reservado dentro de un período.
 * Con esos datos se arma el reporte de "Ocupación y cupo disponible".
 * Solo cuenta servicios individuales: los paquetes no tienen cupo propio, lo comparten
 * con sus componentes, así que contarlos duplicaría los números.
 */
public class VisitanteOcupacion implements Visitante {

    // Período a analizar. Ambas fechas son inclusive.
    private final LocalDate desde;
    private final LocalDate hasta;

    // Acumulador: categoría -> arreglo de 2 posiciones:
    //   [0] = cupo total acumulado, [1] = cupo reservado acumulado.
    // Uso un int[] como "contador mutable" para poder sumar sin reemplazar el valor
    // del mapa en cada paso. LinkedHashMap mantiene el orden en que aparecen las categorías.
    private final Map<Categoria, int[]> acumulado = new LinkedHashMap<>();

    // Servicios ya procesados, comparados por identidad (==). Si un mismo servicio está
    // en varios paquetes, el visitor llega a él más de una vez, pero solo se cuenta la primera.
    private final Set<ServicioIndividual> vistos = Collections.newSetFromMap(new IdentityHashMap<>());

    public VisitanteOcupacion(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    // Se invoca al llegar a un servicio individual (via servicio.aceptar(this)).
    @Override
    public void visitarServicio(ServicioIndividual servicio) {

        // add() devuelve false si ya estaba: en ese caso corto y no lo cuento de nuevo.
        if (!vistos.add(servicio)) return;

        // Busco el contador de la categoría del servicio; si es la primera vez que
        // aparece esa categoría, lo creo en [0, 0].
        int[] acc = acumulado.computeIfAbsent(servicio.getCategoria(), c -> new int[2]);

        // Recorro cada día del período. datesUntil() excluye el último día, por eso
        // uso hasta.plusDays(1): así el período queda inclusive en ambos extremos.
        // Para cada día sumo el cupo total y el reservado de este servicio.
        desde.datesUntil(hasta.plusDays(1)).forEach(f -> {
            acc[0] += servicio.cupoTotal(f);
            acc[1] += servicio.cupoReservado(f);
        });
    }

    // Se invoca al llegar a un paquete. No hace nada porque el paquete no tiene cupo
    // propio. Sus servicios llegan igual, porque PaqueteTuristico.aceptar reenvía
    // la visita a sus hijos.
    @Override
    public void visitarPaquete(PaqueteTuristico paquete) {
        // sin accion: los hijos son visitados por paquete.aceptar(...)
    }

    // Convierte lo acumulado en filas para armar el ReporteOcupacionCupo.
    public List<FilaOcupacion> getFilas() {
        List<FilaOcupacion> filas = new ArrayList<>();

        // Una fila por categoría con su cupo total y reservado.
        // Los libres no se guardan: FilaOcupacion los calcula como total - reservados.
        acumulado.forEach((cat, acc) -> filas.add(new FilaOcupacion(cat, acc[0], acc[1])));
        return filas;
    }
}
