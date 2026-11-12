package servicios.catalogo;


import servicios.Servicio;

public class CriterioNot implements CriterioBusqueda {
		private CriterioBusqueda criterio;
		
		public CriterioNot(CriterioBusqueda criterio) {
			this.criterio = criterio;
			validar();
		}
		
		@Override
		public boolean cumpleCriterio(Servicio servicio) {
			return !(this.criterio.cumpleCriterio(servicio));
		}
		public void validar() {
			if (this.criterio == null) {
				throw new IllegalArgumentException("Error: El conector NOT necesita un criterio");
			}
		}

}
