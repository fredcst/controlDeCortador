package cortador;

import java.util.ArrayList;

public class Hueco {

	/**
	 * Clase que representa los espacios dentro de un rectangulo asociado a una
	 * pieza.
	 * 
	 * @author
	 *
	 */

	private double x0 = 0., y0 = 0.; // coordenadas relativas al rectangulo que lo contiene
	private double dx = 0., dy = 0.;// ancho y alto del hueco
	private boolean tienePiezasHInsertadas = false;
	private ArrayList<PiezaH> piezasHInsertadas = null;
	private ArrayList<ColumnaH> myColumnasH = null;
	private double altoCubiertoPorUltimaColumnaH = 0.; // alto de columna incompleta
	private double anchoTotalCubiertoPorColumnasH = 0.;// ancho total de todas columnas ya completas
	private double area = 0.;
	private int noPiezasInsertadas = 0;

	private ColumnaH ultimaColumnaIncompleta = null;
	private boolean MUESTRA = false;

	public Hueco() {
		piezasHInsertadas = new ArrayList<PiezaH>();
		myColumnasH = new ArrayList<ColumnaH>();

	}

	/**
	 * Constructor general para todos los huecos de Piezas
	 * 
	 * @param x0 coordenada c/r a origen de pieza que lo contiene
	 * @param y0
	 * @param dx ancho del hueco. No debe superar 1.
	 * @param dy alto.
	 */
	public Hueco(double x0, double y0, double dx, double dy) {
		this.x0 = x0;
		this.y0 = y0;
		this.dx = dx;
		this.dy = dy;

		if (x0 + dx > 1.0)
			this.dx = 1. - this.x0; // se asegura que hueco esta en intervalo [0,1.]

		if (y0 + dy > 1.0)
			this.dy = 1. - this.y0;
		area = dx * dy;

		piezasHInsertadas = new ArrayList<PiezaH>();
		myColumnasH = new ArrayList<ColumnaH>();
	}

	/**
	 * Costructor que representa a la correa
	 * 
	 * @param dx ancho maximo de correa
	 */
	public Hueco(double dx) {
		this.dx = dx;
		this.dy = 1.0;
		this.x0 = 0.;
		this.y0 = 0;
		area = dx * dy;
		piezasHInsertadas = new ArrayList<PiezaH>();
		myColumnasH = new ArrayList<ColumnaH>();

	}

	public double getAltoCubiertoPorUltimaColumnaH() {
		return altoCubiertoPorUltimaColumnaH;
	}

	public void setAltoCubiertoPorUltimaColumnaH(double altoCubiertoPorUltimaColumnaH) {
		this.altoCubiertoPorUltimaColumnaH = altoCubiertoPorUltimaColumnaH;
	}

	public double getAnchoTotalCubiertoPorColumnaH() {
		return anchoTotalCubiertoPorColumnasH;
	}

	public void setAnchoTotalCubiertoPorColumnaH(double anchoTotalCubiertoPorColumnasH) {
		this.anchoTotalCubiertoPorColumnasH = anchoTotalCubiertoPorColumnasH;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public double getX0() {
		return x0;
	}

	public void setX0(double x0) {
		this.x0 = x0;
	}

	public double getY0() {
		return y0;
	}

	public void setY0(double y0) {
		this.y0 = y0;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public boolean isTienePiezasHInsertadas() {
		return tienePiezasHInsertadas;
	}

	public void setTienePiezasHInsertadas(boolean tienePiezasHInsertadas) {
		this.tienePiezasHInsertadas = tienePiezasHInsertadas;
	}

	public ArrayList<PiezaH> getPiezasHInsertadas() {
		return piezasHInsertadas;
	}

	public void setPiezasHInsertadas(ArrayList<PiezaH> piezasHInsertadas) {
		this.piezasHInsertadas = piezasHInsertadas;
	}

	public ArrayList<ColumnaH> getMyColumnasH() {
		return myColumnasH;
	}

	public void setMyColumnasH(ArrayList<ColumnaH> columnasHInsertadas) {
		this.myColumnasH = columnasHInsertadas;
	}

	public int getNoPiezasInsertadas() {
		return noPiezasInsertadas;
	}

	public void setNoPiezasInsertadas(int noPiezasInsertadas) {
		this.noPiezasInsertadas = noPiezasInsertadas;
	}

	/**
	 * Permite saber ancho total de columnas completas Entrega 0 si NO hay columnas
	 * insertadas
	 * 
	 * @return
	 */
	public double calculaAnchoTotalDeColumnasHCompletas() {

		anchoTotalCubiertoPorColumnasH = 0.;
		if (myColumnasH != null && !myColumnasH.isEmpty()) {

			for (ColumnaH col : myColumnasH) {
				if (col.estaEnTop()) {
					anchoTotalCubiertoPorColumnasH += col.getMaxAnchoCol();
				}
			}
		}
		return anchoTotalCubiertoPorColumnasH;
	}

	/**
	 * Permite saber el alto total de ultima columna Entrega 0 si NO hay columnas
	 * insertadas
	 * 
	 * @return altoCubiertoUltimaColumna
	 */
	public double calculaAltoCubiertoPorUltimaColumnaH() {
		altoCubiertoPorUltimaColumnaH = 0.;
		if (myColumnasH != null && !myColumnasH.isEmpty()) {

			ColumnaH col = myColumnasH.get(myColumnasH.size() - 1);
			ArrayList<PiezaH> pzs = col.getMyPiezasH();
			if (pzs != null && !pzs.isEmpty()) {
				for (PiezaH pz : pzs)
					altoCubiertoPorUltimaColumnaH += pz.getAlto();
			}

		}
		return altoCubiertoPorUltimaColumnaH;
	}

	/**
	 * Esta rutina se debe usar al fina de haber llenado el hueco con piezas de
	 * forma de saber las posiciones de los origenes de cada rectangulo con respecto
	 * al origen del hueco
	 * 
	 */
	public void ajustaCoordenadasDePiezasConRespectoAOrigenDelHueco() {
		if (myColumnasH != null && !myColumnasH.isEmpty()) {
			ArrayList<PiezaH> pzs = null;
			double ancho = 0., alto = 0.;
			if (MUESTRA)
				System.out.println("Mostrando coordenadas en hueco: " + dx + " " + dy);
			for (ColumnaH col : myColumnasH) {
				pzs = col.getMyPiezasH();
				alto = 0.;
				if (pzs != null && !pzs.isEmpty()) {
					for (PiezaH pz : pzs) {
						pz.setPosX(ancho);
						pz.setPosY(alto);
						if (MUESTRA)
							System.out.println(
									"indice " + pz.getIndice() + " x0: " + pz.getPosX() + " y0: " + pz.getPosY());
						alto = alto + pz.getAlto();// la posicion Y acumula el alto de las piezas
					}
					ancho = ancho + col.getMaxAnchoCol();// Todas las piezas quedan alineadas
					// a la izquierda al ancho maximo de columna anterior
				}
			}

		}
	}

	public void inserta(PiezaH x, ColumnaH col2) {

		// TODO Auto-generated method stub
		if (x.isCabeRectaEnHueco()) {
			col2.getArrayDeColunna().add(x);// la inserta sin rotar
			x.setUsado(true);
			noPiezasInsertadas++;
		} else if (!x.isCabeRectaEnHueco()) {
			// Ya esta rotado cuando
			// se encuentra que cabe
			// pues hay que llenarlo con la orientacion correcta si
			// es que tiene huecos llenables
			col2.getArrayDeColunna().add(x);
			x.setUsado(true);
			noPiezasInsertadas++;
		}

	}

	/**
	 * Crea una nueva columna en hueco, la resetea a ancho y alto cero y la agrega a
	 * la lista. Cuando crea una columna es porque las otras entraron a TOP . Para
	 * las que entraron a TOP solo sera posible llenar el espacio entre el limite
	 * inferior del TOP y la pared superior del hueco, con un ancho fijado por el
	 * ancho maximo de las piezas abajo del TOP.
	 */
	public void creaNuevaColumnaH() {
		if (myColumnasH.size() > 0)
			myColumnasH.get(myColumnasH.size() - 1).setEstaEnTop(true);
		ColumnaH col = new ColumnaH();
		col.setEstaEnTop(false);
		col.setAnchoMax(0.);
		col.setAltoTotal(0.);
		myColumnasH.add(col);
		return;
	}

	/**
	 * Revisa si una pieza cabe DERECHA afuera del conjunto de columnas, en el
	 * espacio entre la cara derecha de la ultima columna y la pared del hueco. |x x
	 * | |xxx | |xxx | |xxxô | ô cabe derecha <o cabe ROTADA M90
	 * 
	 * @param pz: pieza
	 * @param i   indice de la ultima columna a considerar en calculo de ancho
	 * @return true , si cabe . false si no cabe
	 */
	public boolean contieneDerechaAPiezaAfueraDeCol(PiezaH pz, int i) {
		boolean res = false;
		double anchox = 0.;
		anchox = getMaxAnchoTotalCols(i);

		if ((pz.getAncho() + anchox < dx) && (pz.getAlto() < dy)) {
			res = true;
			pz.setCabeRectaEnHueco(true);// Se usa para insertar
		}
		return res;
	}

	/**
	 * Revisa si una pieza cabe DERECHA sobre elementos de una columna con elementos
	 * Toma en cuenta todo el ancho cubierto por las columnas precedentes.
	 * 
	 * @param pz : Pieza a probar
	 * @param i  : indice de columna donde probar
	 * @return :true si cabe, false si no cabe
	 */
	public boolean contieneDerechaAPiezaEnCol(PiezaH pz, int i) {
		boolean res = false;
		double anchox = getMaxAnchoTotalCols(i - 1);
		double altoCol = getAltoTotalCol(i);
		if ((pz.getAncho() + anchox < dx) && (altoCol + pz.getAlto() < dy)) {
			res = true;
			pz.setCabeRectaEnHueco(true);// Se usa para insertar
		}

		return res;
	}

	/**
	 * Revisa si una pieza cabe ROTADA sobre elementos de una columna con elementos
	 * Toma en cuenta todo el ancho cubierto por las columnas precedentes.
	 * 
	 * @param pz : Pieza a probar
	 * @param i  : indice de columna donde probar
	 * @return :true si cabe, false si no cabe
	 */
	public boolean contieneRotM90APiezaEnCol(PiezaH pz, int i) {
		boolean res = false;
		double anchox = getMaxAnchoTotalCols(i - 1);
		double altoCol = getAltoTotalCol(i);
		if ((pz.getAlto() + anchox < dx) && (altoCol + pz.getAncho() < dy)) {
			res = true;
			pz.setCabeRectaEnHueco(false);// Se usa para insertar
		}

		return res;
	}

	/**
	 * Revisa si una pieza cabe ROTADA en M90 en el TOP de una columna. Para esto
	 * considera el espacio entre la ultima pieza de la columna y la pared superior
	 * del hueco, y el ancho previo de la columna, que esta conjelado cuando se esta
	 * en el TOP.
	 * 
	 * @param pz  Pieza a probar
	 * @param col columna donde se quiere insertar
	 * @return true si cabe , false si no cabe.
	 */
	public boolean contieneRotM90APiezaEnTopDeCol(PiezaH pz, ColumnaH col) {
		boolean res = false;
		double anchoCol = col.getMaxAnchoCol();
		double altoCol = col.getAltoTotalCol();
		if ((pz.getAlto() <= anchoCol) && (altoCol + pz.getAncho() < dy)) {
			res = true;
			pz.setCabeRectaEnHueco(false);// usa para insertar
		}

		return res;
	}

	/**
	 * Revisa si una pieza cabe DERECHA en el TOP de una columna. Para esto
	 * considera el espacio entre la ultima pieza de la columna y la pared superior
	 * del hueco, y el ancho previo de la columna, que esta conjelado cuando se esta
	 * en el TOP.
	 * 
	 * @param pz
	 * @param col
	 * @return
	 */
	public boolean contieneDerechaAPiezaEnTopDeCol(PiezaH pz, ColumnaH col) {
		boolean res = false;
		double anchoCol = col.getMaxAnchoCol();
		double altoCol = col.getAltoTotalCol();
		if ((pz.getAncho() <= anchoCol) && (altoCol + pz.getAlto() < dy)) {
			res = true;
			pz.setCabeRectaEnHueco(true);// Se usa para insertar
		}
		return res;
	}

	/**
	 * Calcula el ancho total de las columnas creadas en un hueco hasta cierta
	 * coluna, considerando la suma de los anchos maximos de todas las columnas ,
	 * incluida la ultima en cuestion.
	 * 
	 * @param i indice de la ultima colunmna incluida en en el calculo
	 * @return ; ancho total de las columnas hasta e incluida columna :i
	 */
	public double getMaxAnchoTotalCols(int i) {
		double ancho = 0.;
		int tam = myColumnasH.size();
		for (int j = 0; i >= 0 && j <= i && j < tam; j++) {
			ancho = ancho + myColumnasH.get(j).getMaxAnchoCol();
		}
		return ancho;
	}

	/**
	 * Calcula el alto total de una columna hasta el momento de la llamada
	 * 
	 * @param i :indice de la columna a considerar
	 * @return valor del alto total de la columna
	 */
	public double getAltoTotalCol(int i) {
		double altox = 0;
		ArrayList<PiezaH> pzs = myColumnasH.get(i).getMyPiezasH();
		for (PiezaH pz : pzs)
			altox = altox + pz.getAlto();
		return altox;
	}

	public void muestraEnConsolaPiezasDelHueco() {
		if (myColumnasH != null && !myColumnasH.isEmpty()) {
			int i = 0;
			for (ColumnaH col : myColumnasH) {
				System.out.println("idCol: " + i);
				col.muestraEnConsolaColumnaH();
				i++;
			}

		}
	}

	public void muestraEnConsolaCoordenadasDePiezasDelHuecoCrAHueco() {
		// if (noPiezasInsertadas > 0) {
		if (myColumnasH != null && !myColumnasH.isEmpty()) {
			int i = 0;
			for (ColumnaH col : myColumnasH) {
				System.out.println("idCol: " + i);
				for (PiezaH pz : col.getArrayDeColunna()) {
					System.out
							.println("indice: " + pz.getIndice() + " posX: " + pz.getPosX() + " posY: " + pz.getPosY());
					i++;
				}
			}
			// }
		}
	}

	public void muestraEnConsolaCoordenadasDePiezasDelHuecoCrAPiezaQueLasContiene() {
		// if (noPiezasInsertadas > 0) {
		if (myColumnasH != null && !myColumnasH.isEmpty()) {
			int i = 0;
			System.out.println("ancho: " + dx + " alto: " + dy);
			for (ColumnaH col : myColumnasH) {
				System.out.println("idCol: " + i);
				col.muestraEnConsolaCoordenadasDePiezasEnColumnaConRespectoAPiezaQueLasContiene();
				i++;
			}
			// }
		}
	}

	public double getAnchoTotalCubiertoPorColumnasH() {
		return anchoTotalCubiertoPorColumnasH;
	}

	public void setAnchoTotalCubiertoPorColumnasH(double anchoTotalCubiertoPorColumnasH) {
		this.anchoTotalCubiertoPorColumnasH = anchoTotalCubiertoPorColumnasH;
	}

	public ColumnaH getUltimaColumnaIncompleta() {
		return ultimaColumnaIncompleta;
	}

	public void setUltimaColumnaIncompleta(ColumnaH ultimaColumnaIncompleta) {
		this.ultimaColumnaIncompleta = ultimaColumnaIncompleta;
	}

	public void calculaAreaDeHueco() {
		area = dx * dy;

	}

	public void muestraEnConsolaAltoTotalDeColumnasEnHueco() {
		if (myColumnasH != null && !myColumnasH.isEmpty()) {
			int i = 0;
			for (ColumnaH col : myColumnasH) {
				System.out.println("idCol: " + i);
				col.muestraEnConsolaAltoTotalDeColumna();
				i++;
			}
		}
	}
}
