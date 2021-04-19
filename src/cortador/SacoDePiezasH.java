package cortador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



/**
 * Clase que genera un conjunto de piezasH Una de ellas es la plancha , que
 * tiene 1 solo hueco que la cubre completamente
 * 
 * @author
 *
 */
public class SacoDePiezasH implements FotosDePiezas {
	int noPiezasH = 0;
	int FACTOR_DE_HUECOS = 5;// No maximo de huecos por PiezaH
	double LARGO_EJE_X = 100.;// largo de la plancha
	ArrayList<PiezaH> piezaOrdenados = null;
	int indiceDePiezaHEnRevison = 0;
	int t_pres = 5;
	private int EPS2 = 0;// miesntras mas grande EPS2 mas pequenas las piezas.mas pequenas piezas con
							// mayor indice
	private double eps1 = 1.;// Mientras mas pequeno mas grande hueco [0,1]
	// ArrayList<Segmento> segs = null;
	protected PiezaH[] saco = null;
	private MuestrarioDeSacoH muestrarioDeSacoH = null;

	public SacoDePiezasH() {
		muestrarioDeSacoH = this.new MuestrarioDeSacoH();
	}

	public SacoDePiezasH(int noPiezasH) {
		this.noPiezasH = noPiezasH;
		piezaOrdenados = new ArrayList<PiezaH>();
		// generaPiezas();
		muestrarioDeSacoH = this.new MuestrarioDeSacoH();
	}

	public SacoDePiezasH(int noPiezasH, int eps2) {
		this.noPiezasH = noPiezasH;
		piezaOrdenados = new ArrayList<PiezaH>();
		EPS2 = eps2;
		// generaPiezas();
		muestrarioDeSacoH = this.new MuestrarioDeSacoH();
	}

	public SacoDePiezasH(int noPiezasH, int factorDeHuecos, int eps2) {
		this.noPiezasH = noPiezasH;
		piezaOrdenados = new ArrayList<PiezaH>();
		FACTOR_DE_HUECOS = factorDeHuecos;
		EPS2 = eps2;
		// generaPiezas();
		muestrarioDeSacoH = this.new MuestrarioDeSacoH();
	}

	public SacoDePiezasH(int noPiezasH, int factorDeHuecos, double eps1, int eps2) {
		this.noPiezasH = noPiezasH;
		piezaOrdenados = new ArrayList<PiezaH>();
		FACTOR_DE_HUECOS = factorDeHuecos;
		this.eps1 = eps1;
		EPS2 = eps2;
		// generaPiezas();
		muestrarioDeSacoH = this.new MuestrarioDeSacoH();
	}

	/*
	 * Permite cambiar condiciones sin recomenzar todo
	 */
	public void reset(int noPiezasH, int factorDeHuecos) {
		this.noPiezasH = noPiezasH;
		FACTOR_DE_HUECOS = factorDeHuecos;
		if (piezaOrdenados != null)
			piezaOrdenados.clear();
		// generaPiezas();
	}

	public int getFACTOR_DE_HUECOS() {
		return FACTOR_DE_HUECOS;
	}

	public void setFACTOR_DE_HUECOS(int fACTOR_DE_HUECOS) {
		FACTOR_DE_HUECOS = fACTOR_DE_HUECOS;
	}

	public int getIndiceDePiezaHEnRevison() {
		return indiceDePiezaHEnRevison;
	}

	public void setIndiceDePiezaHEnRevison(int indiceDePiezaHEnRevison) {
		this.indiceDePiezaHEnRevison = indiceDePiezaHEnRevison;
	}

	public void setSegs(ArrayList<PiezaH> pzs) {
		this.piezaOrdenados = pzs;
	}

	public int getNoPiezasH() {
		return noPiezasH;
	}

	public void leeSacoDesdeFile_Bezier_Normal() {
		// String directorio = DirectorioTrabajo.directorio;// Esta guardado directorio
		// raiz del proyecto
		// String directorio_normal = directorio + File.separator + "objetos" +
		// File.separator + "normal_svg";

		String directorio_normal = null;
		// DirectorioBase dbase = new DirectorioBase();
		File directorio_normalD = null;

		// final File directorio_normalD = new File(directorio_normal);
		directorio_normal = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "normal_svg");
		directorio_normalD = new File(directorio_normal);
		// directorio_normalD = DirectorioBase.getFileFromResources("objetos" +
		// File.separator + "normal_svg");
		noPiezasH = directorio_normalD.listFiles().length;
		System.out.format("No total de piezas:%d%n", noPiezasH);
		creaSaco(noPiezasH);
		if (saco != null) {
			int i = 0;
			for (final File fileEntry : directorio_normalD.listFiles()) {
				if (fileEntry.isDirectory()) {
					// listFilesForFolder(fileEntry);
				} else {
					String nombre_svgP = fileEntry.getName();

					// noPiezas = scanner.nextInt();
					saco[i] = new PiezaH();
					saco[i].obtiene_curvas_de_bezier_normal_de_pieza(nombre_svgP);
					i++;
				}

			}
		}

	}

	public void setNoPiezasH(int noPiezasH) {
		this.noPiezasH = noPiezasH;
	}

	public void generaTamanosAleatoriosEnSaco(double anchoMin, double anchoMax, double altoMin, double altoMax) {

		if (saco != null) {
			for (int i = 0; i < noPiezasH; i++) {
				saco[i].setIndice(i);
				double an = Utilidades.getRandomDoubleNumberInRange(anchoMin, anchoMax);
				saco[i].setAncho(an);
				double la = Utilidades.getRandomDoubleNumberInRange(altoMin, altoMax);
				saco[i].setAlto(la);
			}
		}
	}

	private void generaPiezas() {

		ArrayList<PiezaH> piezaNOrdenados = new ArrayList<PiezaH>();// Array provisorio
		PiezaH seg = new PiezaH(0, LARGO_EJE_X);// Se crea Pieza del del ejeX
		// seg.creaSegmento();
		piezaNOrdenados.add(seg);
		for (int i = 1; i < noPiezasH; i++) {

			seg = new PiezaH(i, 1. / (1. + EPS2 * i) * Math.random(), 1. / (1. + EPS2 * i) * Math.random(),
					(int) (FACTOR_DE_HUECOS * Math.random()), eps1);
			// Se crea piezaH con ancho y alto al azar entre 0 y1 y No de huecos al azar
			// entre 0 y FACTOR_DE_HUECOS
			piezaNOrdenados.add(seg);
		}
		PiezaH[] segsA = new PiezaH[piezaNOrdenados.size()];// Para hacer el sort se parsa a ARRAY
		segsA = piezaNOrdenados.toArray(segsA);

		QsortH myQsortH = new QsortH();// Se hace sort por area de piezaH de mayor a menor
		myQsortH.sort((PiezaH[]) segsA, 0, segsA.length - 1, QsortH.tipoComoNumero("area"));

		for (int i = 0; i < segsA.length; i++) {
			piezaOrdenados.add(segsA[i]);// se guarda resultado en piezaOrdenados
		}
	}

	private void generaPiezas(int noPiezasH, double anchoMin, double anchoMax, double altoMin, double altoMax) {

		ArrayList<PiezaH> piezaNOrdenados = new ArrayList<PiezaH>();// Array provisorio
		PiezaH seg = new PiezaH(0, LARGO_EJE_X);// Se crea Pieza del del ejeX
		// seg.creaSegmento();
		piezaNOrdenados.add(seg);
		for (int i = 1; i < noPiezasH; i++) {

			seg = new PiezaH(i, 1. / (1. + EPS2 * i) * Math.random(), 1. / (1. + EPS2 * i) * Math.random(),
					(int) (FACTOR_DE_HUECOS * Math.random()), eps1);
			// Se crea piezaH con ancho y alto al azar entre 0 y1 y No de huecos al azar
			// entre 0 y FACTOR_DE_HUECOS
			piezaNOrdenados.add(seg);

			double an = Utilidades.getRandomDoubleNumberInRange(anchoMin, anchoMax);
			seg.setAncho(an);
			double la = Utilidades.getRandomDoubleNumberInRange(altoMin, altoMax);
			seg.setAlto(la);
			seg.generaColorDePieza(200, 200, 200);
			seg.ordenaDimensionesDePieza(1.);
			seg.calculaArea();
		}

		PiezaH[] segsA = new PiezaH[piezaNOrdenados.size()];// Para hacer el sort se parsa a ARRAY
		segsA = piezaNOrdenados.toArray(segsA);

		QsortH myQsortH = new QsortH();// Se hace sort por area de piezaH de mayor a menor
		myQsortH.sort((PiezaH[]) segsA, 0, segsA.length - 1, QsortH.tipoComoNumero("area"));

		for (int i = 0; i < segsA.length; i++) {
			piezaOrdenados.add(segsA[i]);// se guarda resultado en piezaOrdenados
		}
	}

	public double getEps1() {
		return eps1;
	}

	public void setEps1(double eps1) {
		this.eps1 = eps1;
	}

	public int getEPS2() {
		return EPS2;
	}

	public void setEPS2(int ePS2) {
		EPS2 = ePS2;
	}

	public ArrayList<PiezaH> getPiezasOrdenadas() {
		return piezaOrdenados;
	}

	public void setPiezaOrdenados(ArrayList<PiezaH> piezaOrdenados) {
		this.piezaOrdenados = piezaOrdenados;
	}

	public PiezaH[] getSaco() {
		return saco;
	}

	public void setSaco(PiezaH[] saco) {
		this.saco = saco;
	}

	/*******************************************************************************************/

	/**
	 * Rutina basica de llenado de piezas
	 * 
	 * @param pz :pieza base donde se hara el llenado
	 * @param j  :indice de la pieza base dentro del arreglo de piezas
	 */
	public void llenar(PiezaH pz, int j) {
		// Utilidades.aviso(true, "entrando a llenar: pz id "+pz.getIndice());
		int i = j + 1;
		// Utilidades.aviso(true, "j: "+j+" i: "+i);
		boolean SIGO = true;
		PiezaH pzn = null;
		while (SIGO && i < noPiezasH) {
			// Utilidades.aviso(true, "SIGO: "+SIGO+" i: "+i+" noPiezasH: "+noPiezasH );
			pzn = piezaOrdenados.get(i);
			// Utilidades.aviso(true, "pzn id "+pzn.getIndice());
			// Utilidades.stop();
			if (!pzn.isUsado()) {

				if (pzn.cabeEn(pz)) {// se guardo TAMBIEN el lugar de insercion para pzn!!!

					if (!pzn.estaCompleta()) {

						llenar(pzn, i);
					}
					ColumnaH colx = pzn.getLugarDeInsercion();
					pz.inserta(pzn, colx);
					pzn.setUsado(true);
				}
			}

			i = i + 1;
			// System.out.println(" antes de salir i: " + i);
			if (i == piezaOrdenados.size()) {
				pz.setEstaCompleto(true);
				SIGO = false;
			}
		}

	}

	/**
	 * Rutina que crea una arreglo de noPiezas PiezasH
	 * 
	 * @param noPiezas
	 */

	protected void creaSaco(int noPiezas) {
		this.noPiezasH = noPiezas;
		saco = new PiezaH[this.noPiezasH];
	}

	/********************************
	 * Rutina de acercamientro entre columnas SEGURO Es necesario saber que piezas
	 * insertadas estan en cada pieza de columna Usa el arbol de piezas que nace en
	 * cada pieza de nivel 1(pieza insertada directamente en Pieza0)
	 * 
	 * @param iCol: columna que se acercara a las columnas previas
	 * @param delta :valor de cada acercamiento
	 * @return : el valor neto de minimo acercamiento entre piezas hasta tocar.
	 *         Algunas piezas se acercan mas que otras. Las que se acercan menos son
	 *         las que entregan su valor de acercamiento.
	 */
	public double acercaParesDeColumnasH(int iCol, double delta) {
		// Solo los elementos de las columnas iCol e iCol-1 son comparados.
		// Pero si no encuentra colision con la columna anterior sigue buscando
		// en otras columnas<----------------IMPORTANTE!!!
		// su distancia ente columnas cosecutivas
		// Mueve cada pieza de la columna: j+1 hacia la izquierda hasta que toquen algun
		// elemento de la columna: j. El movimiento es en la direccion -x y se termina
		// con indicacion de cruce de curvas de Bezier. Luego se retraza en 2 unidades.
		// Se procede desde coluna 1 c/r a columna 0. Luego con col. 2 c/r a 1 , que ya
		// ha
		// sido corrrida. Y asi por el estilo. Se continua hasta que se acaben las
		// columnas.
		// Se corrige posicion simulteamente de todas las piezas del arbol de nivel1

		Hueco hCorrea = piezaOrdenados.get(0).getMyListaDeHuecos().get(0);// Hueco de pieza0

		ArrayList<ColumnaH> colList = hCorrea.getMyColumnasH();// Columnas en Hueco de pieza0 Piezas inseradas
																// directamente en pieza0
		double corrimientoMin = 1000000000.;
		if (iCol >= 1 && colList != null) {
			ColumnaH colx = null, colprev = null;
			int tam = colList.size();
			ArrayList<PiezaH> piezas = null;
			ArrayList<PiezaH> piezasPrev = null;
			ArrayList<PiezaH> pz2sI = null;// piezas del arbol de cada pieza de nivel 1
			ArrayList<PiezaH> pz1sI = null;// piezas del arbol de cada pieza de nivel 1
			boolean toco = false;
			int tamcol = 0;
			int tamcolPrev = 0;
			// System.out.println("tam " + tam);
			int ixPrev = 0;
			for (int i = iCol; i < tam && i < iCol + 1; i++) {// Solo corre 1 vez!!!!!!! //(1)

				colx = colList.get(i);// columna en estudio
				piezas = colx.getArrayDeColunna();// arreglo de piezas de columna en estudio
				tamcol = piezas.size();// tamano del arreglo de piezas=nopiezas en columana en estudio

				double origenX1 = 0.;
				for (int idPz = 0; idPz < tamcol; idPz++) {// recorre columna en estudio //(2)
					// for (int idPz =0 ; idPz<tamcol; idPz++) {// recorre columna en estudio
					// System.out.println("idPz: " + idPz);
					PiezaH pz1 = piezas.get(idPz);// pieza en estudio

					toco = false;
					origenX1 = pz1.getPosXP0();// ubicacion antes de desplazarse
												// en pieza en estudio
					double corrimiento = 0.;
					while (origenX1 >= 0. && !toco) {// //(3)

						pz1sI = pz1.getMyPiezasHInsertadasTotalI();// Piezas insertadas del arbol (pz1) en nivel 1,
																	// incluye a pz1!!
						if (pz1sI != null && !pz1sI.isEmpty()) { // (4)
							// Aqui hay que usar el grupo de piezas de nivel>1 que pertenece a pz1!!
							int tamI = pz1sI.size();

							for (int itamI = 0; !toco && itamI < tamI; itamI++) {// recore todas las piezas en el arbol
																					// //(5)
								PiezaH pzcol1 = pz1sI.get(itamI);// pieza del arbol pz1
								// de la pieza nivel 1

								//
								/////////////////////////////////////////////////////////////////////////////////////
								// CRITERIO DE COLISION

								// DEBE CAMBIARSE DE COLUMNA PREVIA SI NO HAY COLISION!!!!
								for (ixPrev = i - 1; !toco && (ixPrev >= 0); ixPrev--) {// recorre todas las columnas
																						// //(6)
																						// previas
																						// para chequear colision
									colprev = colList.get(ixPrev);
									piezasPrev = colprev.getArrayDeColunna();
									tamcolPrev = piezasPrev.size();
									toco = false;
									for (int idPzPrev = 0; !toco && idPzPrev < tamcolPrev; idPzPrev++) { // (7)

										PiezaH pz2 = piezasPrev.get(idPzPrev);// Pieza en la columna previa
										toco = false;

										pz2sI = pz2.getMyPiezasHInsertadasTotalI();// Piezas insertadas en
																					// nivel 1 del arbol de pz2
										// Aqui hay que usar el grupo de piezas de nivel>=1 que pertenece a pz2!!
										if (pz2sI != null && !pz2sI.isEmpty()) { // (8)
											int tamI2 = pz2sI.size();

											for (int itamI2 = 0; !toco && itamI2 < tamI2; itamI2++) { // (9)
												PiezaH pzcol2 = pz2sI.get(itamI2);

												// toco entre pzcol1 y pzcol2

												toco = PiezaH.determinaSiHayCruceDePiezasDesdeRectangulos(pzcol2,
														pzcol1, 5, 0.009000001);
												// Trabaja
												// hasta que la pieza en estudio toca con alguna pieza de la columna
												// previa
											} // (9)
										} // (8)
									} // si toco=true sale del for y while //(7)
								} // (6)
							} // (5)
								//
								////////////////////////////////////////////////////////////////////////////////////
								//
						} // (4)
						if (!toco) {
							corrimiento = corrimiento - delta;
							origenX1 -= delta;// Se acerca cantidad delta
							// origenX1 = pz1.getPosXP0() - delta;
							// pz1.setPosXP0(origenX1);// MODIFICA RECTANGULO M1

							// pz1sI = pz1.getMyPiezasHInsertadasTotalI();// Piezas insertadas en nivel 1
							for (PiezaH pzr : pz1sI)
								pzr.setPosXP0(pzr.getPosXP0() - delta);// CORRE TODAS LAS PIEZAS DEL ARBOL

							// Aqui hay que usar el grupo de piezas de nivel>=1 que pertenece a pz1!!
							// int tamI = pz1sI.size();

						}

						if (toco) {// si toco=true retrocede 1 unidades
							origenX1 += delta;
							corrimiento = corrimiento + delta;
							for (PiezaH pzr : pz1sI)
								pzr.setPosXP0(pzr.getPosXP0() + delta);// Retroceden todas las del arbol al mismo tiempo
						}
					} // while de acercamiento, funciona mientra toco==false y origenX1>=0 //(3) //
						// (3)
					if (corrimientoMin > corrimiento)
						corrimientoMin = corrimiento;
				} // Elije otra pieza de la columna en estudio y repite todo // (2)

			} // Se cambia de columna en estudio // (1)
				// resorre columnas desde columna 1 hacia adelante
		} else {// No exiten columnas para estudiar
			System.out.println(" acercaColumnas():colListMod==null");
			System.exit(0);

		}
		return corrimientoMin;
	}

	/**
	 * Se acercan las piezas de las columnas del Hueco de Pieza0
	 * 
	 * @param delta: valor de intento de acercamiento de cada pieza.
	 */
	public void acercaPiezasDeColumnasDePieza0(double delta) {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			Hueco hCorrea = piezaOrdenados.get(0).getMyListaDeHuecos().get(0);// Hueco de pieza0

			ArrayList<ColumnaH> colList = hCorrea.getMyColumnasH();
			if (colList != null && !colList.isEmpty()) {
				int tam = colList.size();
				double deltax, deltaxT = 0.;
				for (int i = 1; i < tam; i++) {
					deltax = acercaParesDeColumnasH(i, delta);
					 deltaxT =deltaxT + deltax;
					// deltaxT = deltax-0.01;
					//deltaxT = 0.;
					corrigeCoordenasCrPieza0deColumna(i + 1, deltaxT);
					// Lugar para ajustar las coordenas absolutas de las piezas en la columna i+1
					// antes de entrar a la rutina
				}
			}
		}
	}

	public void acercaPiezasDeColumnasDePieza0ConAceleracion(int idCol, double delta, boolean acel) {
		double deltax = 0.;
		deltax = acercaParesDeColumnasH(idCol, delta);

		if (acel)
			corrigeCoordenasCrPieza0deColumna(idCol + 1, deltax);
	}

	public double obtieneXDeCorteUsandoXP0() {
		double res = 0., resMax = 0.;
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {// solo piezas diferentes de pieza0
				if (pz.getIndice() != 0) {
					res = pz.getPosXP0() + pz.getAncho();
					if (resMax < res)
						resMax = res;
				}
			}
		}
		return resMax;
	}

	/**
	 * Rutina que corrige las coordenads X absolutas de las piezas insertadas en
	 * columna iCol de hueco de pieza0 en la canridad correccion.
	 * 
	 * @param iCol       : indice de la columna a corregir
	 * @param correccion : cantidad a corregir en coordenada X absoluta
	 */
	public void corrigeCoordenasCrPieza0deColumna(int iCol, double correccion) {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			Hueco hCorrea = piezaOrdenados.get(0).getMyListaDeHuecos().get(0);// Hueco de pieza0
			ArrayList<ColumnaH> colList = hCorrea.getMyColumnasH();
			if (colList != null && !colList.isEmpty() && iCol < colList.size()) {
				ColumnaH col = colList.get(iCol);
				ArrayList<PiezaH> pzs = col.getArrayDeColunna();
				if (pzs != null && !pzs.isEmpty()) {
					for (PiezaH pz : pzs) {
						ArrayList<PiezaH> pzis = pz.getMyPiezasHInsertadasTotalI();
						if (pzis != null && !pzis.isEmpty()) {
							for (PiezaH pzr : pzis) {
								pzr.setPosXP0(pzr.getPosXP0() + correccion);
							}
						}
					}
				}
			}
		}
	}

	/********************************************************************************************/

	public void muestraEnConsolaCoordenadasTotalesDePiezasInseradas() {
		System.out.println("Coordenadas Totales de Piezas en Pieza 0");
		System.out.println("----------------------------------------");
		for (PiezaH pz : piezaOrdenados) {
			System.out.println("ind pieza: " + pz.getIndice() + " xT: " + pz.getCoordenadaXPTotal() + " yT:  "
					+ pz.getCoordenadaXPTotal());
		}
		System.out.println("----------------------------------------");
	}

	public void muestraPiezasEnConsola() {

		System.out.println(
				"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");

		System.out.println(
				"++++++++++++++++++++++++   Propiedades de Todas las piezas    ++++++++++++++++++++++++++++++ ");

		for (PiezaH pz : piezaOrdenados) {
			System.out.println("Pieza ; " + pz.getNombre() + " con id: " + pz.getIndice() + " :");
			System.out.println(" ancho: " + pz.getAncho() + " alto: " + pz.getAlto() + " area: " + pz.getArea());
			System.out
					.println("---------------------Lista de Huecos-------------------------------------------------- ");
			System.out.println("No De Huecos: " + pz.getMyListaDeHuecos().size());
			for (Hueco h : pz.getMyListaDeHuecos()) {

				System.out.println("x0; " + h.getX0() + " y0: " + h.getY0() + " dx: " + h.getDx() + " dy: " + h.getDy()
						+ " area: " + h.getArea());
			}
			System.out.println(
					"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");

		}

		return;

	}

	public void muestraPieza(PiezaH pz) {
		System.out.println("++++++++++++++++++++++++   Propiedades de pieza     +++++++++++++++++++++++++++++++++++ ");

		System.out.println("Pieza ; " + pz.getIndice() + " :");
		System.out.println(" ancho: " + pz.getAncho() + " alto: " + pz.getAlto() + " area: " + pz.getArea());
		System.out.println("---------------------Lista de Huecos-------------------------------------------------- ");
		for (Hueco h : pz.getMyListaDeHuecos()) {
			System.out.println("x0; " + h.getX0() + " y0: " + h.getY0() + " dx: " + h.getDx() + " dy: " + h.getDy()
					+ " area: " + h.getArea());
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");

	}

	protected void muestraEnConsolaEstadoFinalDeLLenadoDePiezas() {

		// for (PiezaH pzt : getPiezasOrdenadas()) {
		// System.out.println("No de piezas insertadas en: " + pzt.getIndice() + " : "
		// + pzt.getMyPiezasHInsertadas().size() + " Completa? : " + pzt.estaCompleta()
		// + " estado?: "
		// + pzt.isUsado() + " insertada? " + pzt.isEstaInsertada()+" donde:
		// "+((pzt.getIndice()==0)?-1: pzt.getEstoyInsertadaEn().getIndice()));

		String frase = "";
		for (PiezaH pzt : getPiezasOrdenadas()) {
			frase = "";
			if (pzt.getIndice() != 0 && pzt.isEstaInsertada()) {

				frase = String.format(" donde: %d ", pzt.getEstoyInsertadaEn().getIndice());
			} else
				frase = "  donde: ----";
			System.out.println("No de piezas insertadas en: " + pzt.getIndice() + " : "
					+ pzt.getMyPiezasHInsertadas().size() + " Completa? : " + pzt.estaCompleta() + " estado?: "
					+ pzt.isUsado() + " insertada? " + pzt.isEstaInsertada() + frase);

			// for (PiezaH pzi : pzt.getMyPiezasHInsertadas()) {
			// mySacoDePiezasH.muestraPieza(pzi);
			// }

		}
	}

	private void muestraEnConsolaPiezasDentroDePiezas() {

		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				System.out.println("id Pieza: " + pz.getIndice());
				pz.muestraEnConsolaPiezasEnHuecos();
			}

		}

	}

	/**
	 * Esta rutina hay que llamarla despues de rutina :llenar. Establece las
	 * coordenadas de las piezas en los huecos Despues, estas coordenadas se pueden
	 * modificar con la aternacion de TOPS y corrimiento de columnas. A
	 * continuacion, en otra rutina se calculan las coordenadas con respecto al
	 * origen de las piezas. y posteriormente, con respecto al origen de la pieza0.
	 */
	public void ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDeTodosLosHuecosDeTodasLasPiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDeTodosLosHuecos();
			}
		}

	}

	protected void ajustaCoordenadasDeTodasLasPiezasconRespectoAOrigenDePiezas() {
		// System.out.println("=====================================================================");
		// System.out.println("Coordenadas de Piezas con respecto a origen de Pieza que
		// las contiene");
		// System.out.println("---------------------------------------------------------------------");
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDePiezaX();
			}
		}
		// System.out.println("=====================================================================");
	}

	private void muestraEnConsolaCoordenadasDeTodasLasPiezasConRepectoALaPiezaQueLasContiene() {

		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				if (pz.getNoDePiezasInsertadas() > 0) {
					System.out.println("***************** x ***********************");
					System.out.println(
							"pieza; " + pz.getIndice() + " contiene: " + pz.getNoDePiezasInsertadas() + " piezas");
					pz.muestraEnConsolaCoordenadasDeTodasLasPiezasContenidasConRespectoAPieza();
				}
			}
		}
	}

	public void muestraDondeEstaDirectamenteInsertadaParaTodasLasPiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.muestraDondeEstaDirectamenteInsertada();
			}
		}
	}

	private void muestraEnConsolaElNoTotalDePiezasUsadas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			int i = 0;
			for (PiezaH pz : piezaOrdenados) {
				if (pz.isEstaInsertada()) {
					// System.out.println("id pieza No insertada; " + pz.getIndice());
					i++;
				}
			}
			System.out.println("No Total de piezas insertadas; " + i + " de  : " + noPiezasH);
		}
	}

	private void muestraEnConsolaAltoTotalDeTodasLasColumnasEnTodasLasPiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.muestraEnConsolaAltoTotalDeTodasLasColumnasContenidasEnPieza();
			}
		}
	}

	private void muestraEnConsolaCoordenadasCrHuecosDeTodasLasPiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.muestraEnConsolaCoordenadasDeTodasPiezasCRHuecosDePieza();
			}
		}
	}

	protected void calculaYGuardaCoordenadasXeYTotalesCrAPieza0DeTodasLasPiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.calculaYGuardaCoordenadasXeYTotalesCrAPieza0();
			}
		}
	}

	protected void generaTodasLasPiezasContenidasEnArbolHastaNivel0y1ParaTodasLasPiezas() {

		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.generaTodasLasPiezasContenidasEnArbolHastanivel0y1();
			}
		}

	}

	protected void alternaTopDecolumnasEnPieza0() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			Hueco h = piezaOrdenados.get(0).getMyListaDeHuecos().get(0);// Obtiene hueco de Pieza0
			piezaOrdenados.get(0).alternaTopDeColumnasH(h);
		}
	}

	private void muestraEnConsolaPiezasInsertadasEnPiezasInsertadasEnPieza0ParaTodasLasPiezas() {
		System.out.println("==========================================================");
		System.out.println("Piezas contenidas en el arbol de cada pieza insertada en 0");
		System.out.println("==========================================================");
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.muestraEnConsolaPiezasInsertadasEnPiezasInsertadasEnPieza0();
			}
		}
		System.out.println("==========================================================");
	}

	public void muestraTodasLasPiezasContenidasEnArbolHastaNivel0y1ParaTodasLasPiezas() {
		System.out.println("===========================================");
		System.out.println("Piezas contenidas en el arbol de cada pieza");
		System.out.println("Las piezas que no tienen piezas son piezas de nivel >1");
		System.out.println("===========================================");
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.muestraTodasLasPiezasContenidasEnArbolHastaNivel0y1();
			}
		}
		System.out.println("===========================================");
	}

	private void muestraEnConsolaPiezasInsertadasTotalIParaTodasLasPiezas() {
		System.out.println("===========================================");
		System.out.println("Piezas insertadas  en el arbol de piezas a nivel 1");
		System.out.println("===========================================");
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.muestraEnConsolaPiezasInsertadasTotalI();
			}
		}
		System.out.println("===========================================");
	}

	public void generaMyPiezasHInsertadasTotalIParaTodasLasPiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.generaMyPiezasHInsertadasTotalI();
			}
		}
	}

	public void muestraEnConsolaPiezasDelArbolANivel1ConDistanciasAPiezaPrecedenteParaTodasLasPiezas() {
		System.out.println("===========================================");
		System.out.println("Arbol de piezas a nivel 1 con posiciones relativas");
		System.out.println("===========================================");
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.muestraEnConsolaPiezasDelArbolANivel1ConDistanciasAPiezaPrecedente();
			}
		}

		System.out.println("===========================================");

	}

	public void calculaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0ParaTodasLasPiezas() {

		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.calculaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0();
			}

		}

	}

	public void muestraEnConsolaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0ParaTodasLasPiezas() {
		System.out.println("===========================================");
		System.out.println("Distancias absolutas de piezas c/r a Pieza 0 en cada arbol");
		System.out.println("===========================================");
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.muestraEnConsolaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0();
			}

		}
		System.out.println("===========================================");
	}

	/**
	 * Rutina que corrige la posicion izquierda de huecos en todos los huecos
	 * izquierdos
	 */
	public void corrigePosicionDeColumnasIzquierdasEnHuecoAbiertosDePiezaEnTodasLasPiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.corrigePosicionDeColumnasIzquierdasEnHuecoAbiertosDePieza();
			}
		}
	}

	/**
	 * Rutina que crea un rectangulo para cada piezaH donde se almacenara las
	 * coordenadas absolutas de las piezas en la posicion cuando estan formando
	 * columnas sin acercamiento Es como una FOTOGRAFIA de posicion de piezas
	 */
	public void asociaRectColsAPosicinP0Depiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.setRectCols(new RectanguloH(pz.getPosXP0(), pz.getPosYP0(), pz.getAncho(), pz.getAlto()));
			}
		}
	}

	/**
	 * Rutina que crea un rectangulo para cada piezaH donde se almacenara las
	 * coordenadas absolutas de las piezas en la posicion cuando estan
	 * aleatoriamente sobre correa Es como una FOTOGRAFIA de posicion de piezas
	 */
	public void asociaRect0APosicinP0Depiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.setRect0(new RectanguloH(pz.getPosXP0(), pz.getPosYP0(), pz.getAncho(), pz.getAlto()));
			}
		}
	}

	/**
	 * Rutina que crea un rectangulo para cada piezaH donde se almacenara las
	 * coordenadas absolutas de las piezas en la posicion cuando estan en posicion
	 * final sobre correa, despues de acercamiento Es como una FOTOGRAFIA de
	 * posicion de piezas
	 */
	public void asociaRectFAPosicinP0Depiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.setRectF(new RectanguloH(pz.getPosXP0(), pz.getPosYP0(), pz.getAncho(), pz.getAlto()));
			}
		}
	}

	/**
	 * Rutina que crea un rectangulo para cada piezaH donde se almacenara las
	 * coordenadas absolutas de las piezas en la posicion cuando estan prdenadas
	 * sobre Eje X de correa. Es como una FOTOGRAFIA de posicion de piezas
	 */
	public void asociaRectEjeXAPosicinP0Depiezas() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				pz.setRectEjeX(new RectanguloH(pz.getPosXP0(), pz.getPosYP0(), pz.getAncho(), pz.getAlto()));
			}
		}

	}

	/**
	 * Rutina que crea una foto de las piezas formando columnas antes del
	 * acercamiento y despues de el llenado de huecos Crea rectangulos: RectCols
	 */

	public void creaFotoRectCols() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				if (pz.getIndice() != 0) {
					pz.setRectCols(new RectanguloH(pz.getPosXP0(), pz.getPosYP0(), pz.getAncho(), pz.getAlto()));
				}
			}
		}
	}

	/**
	 * Rutina que crea una foto de las piezas sobre el eje X ordenadas desde mayor
	 * area a menor area Crea rectangulos: RectEjeX
	 */
	public void creaFotoRectEjeX() {
		if (saco != null) {
			saco[1].setRectEjeX(new RectanguloH(0, 0, saco[1].getAncho(), saco[1].getAlto()));
			for (int i = 2; i < noPiezasH; i++) {
				saco[i].setRectEjeX(
						new RectanguloH(saco[i - 1].getRectEjeX().getDX() + saco[i - 1].getRectEjeX().getPosX(), 0,
								saco[i].getAncho(), saco[i].getAlto()));
			}
		}

	}

	/**
	 * Rutina que crea una foto de las piezas despues que columnas se han acercado
	 * Crea rectangulos: RectF
	 */
	public void creaFotoRectF() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				if (pz.getIndice() != 0) {
					pz.setRectF(new RectanguloH(pz.getPosXP0(), pz.getPosYP0(), pz.getAncho(), pz.getAlto()));
				}
			}
		}
	}

	public void ejecuta0() {
		generaPiezas();
		creaFotoRect0();
	}

	public void ejecuta1() {
		creaFotoRectEjeX();
	}

	public void ejecuta2() {
		PiezaH pz = piezaOrdenados.get(0);
		llenar(pz, pz.getIndice());
		System.out.println("Termine...");
		alternaTopDecolumnasEnPieza0();
		generaTodasLasPiezasContenidasEnArbolHastaNivel0y1ParaTodasLasPiezas();
		generaMyPiezasHInsertadasTotalIParaTodasLasPiezas();
		corrigePosicionDeColumnasIzquierdasEnHuecoAbiertosDePiezaEnTodasLasPiezas();
		ajustaCoordenadasDeTodasLasPiezasconRespectoAOrigenDePiezas();
		calculaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0ParaTodasLasPiezas();
		creaFotoRectCols();
	}

	/**
	 * Esta funcion se usa desde el DibujoDeCortador cuando se usa el contructor sin
	 * parametros!!
	 * 
	 * @param noPiezasH
	 * @param factorDeHuecos
	 * @param eps1
	 * @param eps2
	 */
	public void trabajoCompleto(int noPiezasH, int factorDeHuecos, double eps1, int eps2) {
		this.noPiezasH = noPiezasH;
		piezaOrdenados = new ArrayList<PiezaH>();
		FACTOR_DE_HUECOS = factorDeHuecos;
		this.eps1 = eps1;
		EPS2 = eps2;
		ejecuta0();
		ejecuta1();
		// mySacoDePiezasH.generaPiezas();
		muestraPiezasEnConsola();
		ejecuta2();
		muestraEnConsolaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0ParaTodasLasPiezas();
	}

	public static void main(String[] args) {
		// SacoDePiezasH mySacoDePiezasH = new SacoDePiezasH(50);
		SacoDePiezasH mySacoDePiezasH = new SacoDePiezasH(100, 3, 0.1, 5);
		mySacoDePiezasH.ejecuta0();
		mySacoDePiezasH.ejecuta1();
		// mySacoDePiezasH.generaPiezas();
		mySacoDePiezasH.muestraPiezasEnConsola();
		mySacoDePiezasH.ejecuta2();
		mySacoDePiezasH.muestraEnConsolaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0ParaTodasLasPiezas();

		/*
		 *
		 * PiezaH pz = mySacoDePiezasH.getPiezasOrdenadas().get(0);
		 * mySacoDePiezasH.llenar(pz, 0); System.out.println("Termine...");
		 *
		 * // mySacoDePiezasH.muestraEnConsolaPiezasDentroDePiezas();
		 * mySacoDePiezasH.muestraEnConsolaEstadoFinalDeLLenadoDePiezas();
		 * //Utilidades.aviso(true,
		 * "pase muestraEnConsolaEstadoFinalDeLLenadoDePiezas");
		 * //mySacoDePiezasH.alternaTopDecolumnasEnPieza0(); //Utilidades.aviso(true,
		 * "pase alternaTopDecolumnasEnPieza0");
		 * 
		 * //
		 * mySacoDePiezasH.calculaYGuardaCoordenadasXeYTotalesCrAPieza0DeTodasLasPiezas(
		 * ); mySacoDePiezasH.
		 * generaTodasLasPiezasContenidasEnArbolHastaNivel0y1ParaTodasLasPiezas();
		 * //Utilidades.aviso(true,
		 * "pase generaTodasLasPiezasContenidasEnArbolHastaNivel0y1ParaTodasLasPiezas");
		 * // mySacoDePiezasH.
		 * muestraTodasLasPiezasContenidasEnArbolHastaNivel0y1ParaTodasLasPiezas();
		 * mySacoDePiezasH.muestraEnConsolaElNoTotalDePiezasUsadas();
		 * mySacoDePiezasH.generaMyPiezasHInsertadasTotalIParaTodasLasPiezas(); //
		 * mySacoDePiezasH.
		 * muestraEnConsolaPiezasInsertadasEnPiezasInsertadasEnPieza0ParaTodasLasPiezas(
		 * ); //
		 * mySacoDePiezasH.muestraEnConsolaAltoTotalDeTodasLasColumnasEnTodasLasPiezas()
		 * ; // mySacoDePiezasH.muestraEnConsolaCoordenadasCrHuecosDeTodasLasPiezas() ;
		 * //
		 * mySacoDePiezasH.muestraEnConsolaPiezasInsertadasTotalIParaTodasLasPiezas();
		 * // mySacoDePiezasH.muestraDondeEstaDirectamenteInsertadaParaTodasLasPiezas();
		 * mySacoDePiezasH.
		 * corrigePosicionDeColumnasIzquierdasEnHuecoAbiertosDePiezaEnTodasLasPiezas();
		 * mySacoDePiezasH.ajustaCoordenadasDeTodasLasPiezasconRespectoAOrigenDePiezas()
		 * ; // mySacoDePiezasH.
		 * muestraEnConsolaCoordenadasDeTodasLasPiezasConRepectoALaPiezaQueLasContiene()
		 * ; // mySacoDePiezasH.
		 * muestraEnConsolaPiezasDelArbolANivel1ConDistanciasAPiezaPrecedenteParaTodasLasPiezas
		 * ();
		 * 
		 * mySacoDePiezasH.
		 * calculaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0ParaTodasLasPiezas();
		 * mySacoDePiezasH.asociaRectColsAPosicinP0Depiezas();
		 * 
		 * 
		 */
		// mySacoDePiezasH.muestraEnConsolaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0ParaTodasLasPiezas();

		/************************************************************************************************/
		/*************
		 * Zona de desplieque de columnas
		 ****************************************************/

		/****************************************************************************************************/
		/**
		 * Zona donde deben implementarse la rutina de acercamiento
		 ****************************************/

		/****************************************************************************************************/
		/***************************************
		 * Zona de desplieque resultados de acercamiento
		 *************/

	}

	public void creaFotoRect0() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				if (pz.getIndice() != 0) {
					double ratioDespligue = DibujoDeCortador.getRECT_WIDTH() / DibujoDeCortador.getRECT_HEIGHT();
					double posYAlea = Utilidades.getRandomDoubleNumberInRange(0., 1. - pz.getAlto());
					double posXAlea = Utilidades.getRandomDoubleNumberInRange(0., ratioDespligue - pz.getAncho());
					pz.setRect0(new RectanguloH(posXAlea, posYAlea, pz.getAncho(), pz.getAlto()));
				}
			}
		}
	}

	public int getT_pres() {
		return t_pres;
	}

	public void setT_pres(int t_pres) {
		this.t_pres = t_pres;
	}

	public MuestrarioDeSacoH getMuestrarioDeSacoH() {
		return muestrarioDeSacoH;
	}

	public void setMuestrarioDeSacoH(MuestrarioDeSacoH muestrarioDeSacoH) {
		this.muestrarioDeSacoH = muestrarioDeSacoH;
	}

	@SuppressWarnings("serial")
	class MuestrarioDeSacoH extends JPanel implements ActionListener {
		int RECT_X = 20;
		int RECT_Y = 20;
		int RECT_HEIGHT = 200;
		int RECT_WIDTH = 400;
		private double ANCHO_WIN = 100;
		private double ALTO_WIN = 100;
		private JButton button = null;
		private int indPieza = 0;

		public int getRECT_X() {
			return RECT_X;
		}

		public int getRECT_Y() {
			return RECT_Y;
		}

		public int getRECT_WIDTH() {
			return RECT_WIDTH;
		}

		public int getRECT_HEIGHT() {
			return RECT_HEIGHT;
		}

		public void muestra_piezas_en_pane() {
			JFrame frame = new JFrame();
			setLayout(new BorderLayout());
			// setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
			// setLayout(null);
			// JLabel label = new JLabel("Suivant");
			button = new JButton();
			// add(label, BorderLayout.EAST);
			add(button, BorderLayout.SOUTH);

			// button.setLocation(250, 100);
			button.setText("Suivant");
			button.addActionListener(this);
			// add(label);
			// add(button);
			frame.add(this);
			frame.setSize(RECT_WIDTH, RECT_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}

		public void muestra_protos() {
			setLayout(new BorderLayout());
			button = new JButton();
			// add(label, BorderLayout.EAST);
			add(button, BorderLayout.SOUTH);
			button.setText("Suivant");
			button.addActionListener(this);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// draw the rectangle here
			Color cOld = g.getColor();
			Color cNew = new Color(0, 0, 0);
			g.setColor(cNew);

			// dibuja_pieza(g, saco[0], 100);
			// for (int i = 0; i < saco.length; i++) {
			if (saco.length > 0) {
				int i_fig = indPieza % saco.length;
				dibuja_pieza(g, saco[i_fig], 100, 100, 10);
				String nombre = saco[i_fig].getNombre();
				g.drawString(nombre, 10, 150);
			} else
				System.out.println("saco.length=0");
			/*
			 * Color cp = saco[i].getColorDePieza(); g.setColor(cp); // g.setColor(new
			 * Color(0, 0, 0)); int pX = saco[i].getRect().getPosX(); int pY =
			 * saco[i].getRect().getPosY(); int dX = saco[i].getRect().getDX(); int dY =
			 * saco[i].getRect().getDY(); g.fillRect(RECT_X + pX, RECT_Y + pY, dX, dY);
			 */

			g.setColor(cOld);

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getActionCommand() == button.getText()) {
				// System.out.println("Boton apretado");
				indPieza++;
				repaint();
			}

		}

		public void dibuja_pieza(Graphics g, PiezaH pz, int presicion, int xOrigen, int yOrigen) {
			Point P1, P2, P3, P4, resG, res_prevG;
			EleCalculo elx;
			Path pathX;
			P1 = new Point();
			P2 = new Point();
			P3 = new Point();
			P4 = new Point();
			resG = new Point();
			res_prevG = new Point();
			// Point p1=NULL,p2=NULL,p3=NULL,p4=NULL;
			// p1= MALLOC(point);p2= MALLOC(point);p3= MALLOC(point);p4= MALLOC(point);
			// Point res=MALLOC(point);Point res_prev=MALLOC(point);
			boolean esPrimero = true;
			double tx, del = 1. / presicion;
			int no_paths = pz.pathList.size();
			for (int i = 0; i < no_paths; i++) {
				pathX = pz.pathList.get(i);
				if (pathX.flag_vis == 1) {
					for (int j = 0; pathX.path != null && j < pathX.path.size(); j++) {
						elx = pathX.path.get(j);

						P1.x = elx.xP1 * pz.getAncho() + pz.getPosX() + xOrigen;
						P1.y = elx.yP1 * pz.getAlto() + pz.getPosY() + yOrigen;
						P2.x = elx.xP2 * pz.getAncho() + pz.getPosX() + xOrigen;
						P2.y = elx.yP2 * pz.getAlto() + pz.getPosY() + yOrigen;
						P3.x = elx.xP3 * pz.getAncho() + pz.getPosX() + xOrigen;
						P3.y = elx.yP3 * pz.getAlto() + pz.getPosY() + yOrigen;
						P4.x = elx.xP4 * pz.getAncho() + pz.getPosX() + xOrigen;
						P4.y = elx.yP4 * pz.getAlto() + pz.getPosY() + yOrigen;
						for (int k = 0; k < presicion + 1; k++) {
							tx = del * k;
							if (elx.t == 'c' || elx.t == 'C')
								resG = Bezier.vectorBezierC(tx, resG, P1, P2, P3, P4);
							if (elx.t == 'q' || elx.t == 'Q')
								resG = Bezier.vectorBezierQ(tx, resG, P1, P2, P3);
							if (elx.t == 'L' || elx.t == 'l')
								resG = Bezier.vectorBezierL(tx, resG, P1, P2);
							if (!esPrimero) {
								// dibuja linea entre puntos res y res_pr
								//g.drawLine((int) res_prevG.x, (int) res_prevG.y, (int) resG.x, (int) resG.y);
								 g.drawLine((int) (ANCHO_WIN * res_prevG.x), (int) (ALTO_WIN * res_prevG.y),
								 (int) (ANCHO_WIN * resG.x), (int) (ALTO_WIN * resG.y));
							}
							res_prevG.x = resG.x;
							res_prevG.y = resG.y;
							esPrimero = false;
							// printf("%d %lf\t%lf\t%lf\n",path->id,t,res->x,res->y);
						}
						esPrimero = true;
					}
				}
			}

			return;
		}

		@Override
		public Dimension getPreferredSize() {
			// so that our GUI is big enough
			return new Dimension(RECT_WIDTH + 2 * RECT_X, 3 * RECT_HEIGHT + 2 * RECT_Y);
		}
	}

}
