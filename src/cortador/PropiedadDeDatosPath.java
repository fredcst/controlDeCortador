package cortador;

public class PropiedadDeDatosPath {
	/**
	 * Clase utilitaria que guarda datos de cada path, detectados de archivo
	 * producido por inkscape.
	 */

	int k; // tamano de datos de path
	int k_ini; // posicion donde comienzan datos de path
	int vis; // visibilidad

	public PropiedadDeDatosPath() {
	}

	public PropiedadDeDatosPath(int k, int k_ini, int vis) {
		this.k = k;
		this.k_ini = k_ini;
		this.vis = vis;
	}

}
