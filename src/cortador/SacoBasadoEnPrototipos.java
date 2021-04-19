package cortador;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

public class SacoBasadoEnPrototipos extends SacoDePiezasH implements FotosDePiezas, PropertyChangeListener {
	BezierFull myBezierFull = null;
	Prototipos myPrototipos = null;
	LinkedList<CurvasDeBezierDeObjeto> myCurvasDeBezierLL = null;
	private int noPrototipos = 0;
	private LinkedList<CurvasDeBezierDeObjeto> curvasObjetoList = null;
	private int noPiezasH = 0;
	private DibujoDeCortador myDibujoDeCortador = null;
	CalculoLargoPar myCalculoLargoPar = null;
	private double xDeCorteF = 0.;
	private double xDeCorteCols = 0.;
	

	public SacoBasadoEnPrototipos(int noPiezasH) {
		super(noPiezasH);
		// TODO Auto-generated constructor stub
	}

	public SacoBasadoEnPrototipos() {
		super(1);
		// TODO Auto-generated constructor stub
	}

	public void generaPiezas() {
		{

			ArrayList<PiezaH> piezaNOrdenados = new ArrayList<PiezaH>();// Array provisorio
			PiezaH seg = new PiezaH(0, LARGO_EJE_X);// Se crea Pieza del del ejeX
			System.out.println("Hola Estoy generando piezas");

			if (myPrototipos != null) {
				myCurvasDeBezierLL = myPrototipos.getCurvasObjetoList();
				System.out.println("No de curvas de Bezier Leidas " + myCurvasDeBezierLL.size());
				for (CurvasDeBezierDeObjeto cB : myCurvasDeBezierLL) {
					System.out.println("No de segmentos de Bezier: " + cB.pathList.size());

				}
			}

			// Debe elegir al azar entre los prototipos y almacenar
			// las curbas de Bezier en cada piezaH creada.
		}
	}

	public int getNoPiezas() {
		return noPiezasH;
	}

	public void setNoPiezas(int noPiezas) {
		this.noPiezasH = noPiezas;
	}

	/**
	 * Rutina que explora el directorio :cortador/objetoq/normal_svg y lee todos los
	 * files que contienen, cada uno, listas normalizasdas de curbas de Bezier para
	 * cada prototipo.
	 */
	public void leePrototiposDesdeFile_Bezier_Normal() {

		String directorio_normal = null;

		File directorio_normalD = null;
		directorio_normal = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "normal_svg");

		directorio_normalD = new File(directorio_normal);

		noPrototipos = directorio_normalD.listFiles().length;
		System.out.format("No total de Prototipos:%d\n", noPrototipos);

		curvasObjetoList = new LinkedList<CurvasDeBezierDeObjeto>();
		CurvasDeBezierDeObjeto proto = null;
		if (curvasObjetoList != null) {
			for (final File fileEntry : directorio_normalD.listFiles()) {
				if (fileEntry.isDirectory()) {
					// listFilesForFolder(fileEntry);
				} else {
					String nombre_svgP = fileEntry.getName();
					proto = new CurvasDeBezierDeObjeto();

					// noPiezas = scanner.nextInt();
					proto.obtiene_curvas_de_bezier_normal(nombre_svgP);
					curvasObjetoList.add(proto);
				}

			}
		}

	}

	/**
	 * Cada objeto tiene su copia del set de curvas de Bezier de un prototipo
	 * elegido al azar.
	 * 
	 * @param noPiezasH No de piezas a crear a partir de prototipos
	 * @param anchoMin  ancho minimo en rango [0.,1.] double
	 * @param anchoMax  ancho max en rango [0.,1.] double
	 * @param altoMin   alto minimo en rango [0.,1.] double
	 * @param altoMax   alto mqximo en rango [0.,1.] double
	 */
	public void generaPiezasDesdePrototipoEnSaco(int noPiezasH, double anchoMin, double anchoMax, double altoMin,
			double altoMax) {
		this.noPiezasH = noPiezasH;
		if (anchoMin < 0)
			anchoMin = 0.;
		if (altoMin < 0)
			altoMin = 0.;
		if (anchoMin > anchoMax) {
			double pivot = anchoMax;
			anchoMax = anchoMin;
			anchoMin = pivot;
		}
		if (anchoMax > 1.)
			anchoMax = 1.;
		if (altoMax > 1.)
			altoMax = 1.;
		if (altoMin > altoMax) {
			double pivot = altoMax;
			altoMax = altoMin;
			altoMin = pivot;
		}
		creaSaco(noPiezasH);
		if (piezaOrdenados == null)
			piezaOrdenados = new ArrayList<PiezaH>();
		piezaOrdenados.clear();

		PiezaH seg = new PiezaH(0, LARGO_EJE_X);// Se crea Pieza del del ejeX
		// seg.creaSegmento();
		// piezaOrdenados.add(seg);

		leePrototiposDesdeFile_Bezier_Normal();// lee desde archivo los prototipos

		Utilidades.aviso(false, "Se leyeron prototipos");
		Utilidades.aviso(false, "NoPrototipos: " + String.format(" %d%n", noPrototipos));

		int id_proto = 0;

		if (noPrototipos > 0 && saco != null) {
			saco[0] = seg;
			// double posXEjeX=0.;
			for (int i = 1; i < noPiezasH; i++) {
				saco[i] = new PiezaH();

				id_proto = Utilidades.getRandomNumberInRange(0, noPrototipos - 1);
				// Utilidades.aviso(false, "id_proto " + String.format(" %d%n", id_proto));
				saco[i].setNombre(curvasObjetoList.get(id_proto).nombre);// asigna nombre de prototipo
				saco[i].setIndice(i);
				saco[i].setPathList(PiezaH.creaCopiaDeCurvasDeBezier(curvasObjetoList.get(id_proto).pathList));
				saco[i].generaPathListMod();// genera una copia de C de Bezier
				double an = Utilidades.getRandomDoubleNumberInRange(anchoMin, anchoMax);
				saco[i].setAncho(an);
				double la = Utilidades.getRandomDoubleNumberInRange(altoMin, altoMax);
				saco[i].setAlto(la);
				saco[i].generaColorDePieza(200, 200, 200);
				saco[i].ordenaDimensionesDePieza(1.);
				saco[i].calculaArea();

				saco[i].generaListaHuecosInternosAPartirDeCDeBezier();

				// Genera posiciones iniciales aleatorias de las piezas
				// estas seran colocadas en el RectO de las piezas
				// double ratioDespligue = DibujoDeCortador.getRECT_WIDTH() /
				// DibujoDeCortador.getRECT_HEIGHT();
				// double posYAlea = Utilidades.getRandomDoubleNumberInRange(0., 1. -
				// saco[i].getAlto());
				// double posXAlea = Utilidades.getRandomDoubleNumberInRange(0., ratioDespligue
				// - saco[i].getAncho());
				// saco[i].setRect0(new RectanguloH(posXAlea, posYAlea, saco[i].getAncho(),
				// saco[i].getAlto()));

			}

			QsortH myQsortH = new QsortH();// Se hace sort por area de piezaH de mayor a menor
			myQsortH.sort((PiezaH[]) saco, 0, saco.length - 1, QsortH.tipoComoNumero("area"));

			for (int i = 0; i < noPiezasH; i++)
				piezaOrdenados.add(saco[i]);
			escalaListaDeHuecos();

		} else {
			System.out.println("No de prototipos=0 o Saco no inicializado");
			System.exit(0);

		}
		System.out.println(noPiezasH + " piezasH creadas...");
	}

	public void escalaListaDeHuecos() {
		if (piezaOrdenados != null && !piezaOrdenados.isEmpty()) {
			for (PiezaH pz : piezaOrdenados) {
				if (pz.getIndice() != 0) {
					// System.out.println("pz. Id: "+pz.getIndice()+" no hs:
					// "+pz.getMyListaDeHuecos().size());
					if (pz.getMyListaDeHuecos() != null && !pz.getMyListaDeHuecos().isEmpty()) {
						for (Hueco h : pz.getMyListaDeHuecos()) {
							// System.out.println("antes; dx: "+ h.getDx());
							h.setDx(pz.getAncho() * h.getDx());
							// System.out.println("despues; dx: "+ h.getDx());
							// System.out.println("antes; dy: "+ h.getDy());
							h.setDy(pz.getAlto() * h.getDy());
							// System.out.println("despues; dy: "+ h.getDy());
							// System.out.println("antes; x0: "+ h.getX0());
							h.setX0(pz.getAncho() * h.getX0());
							// System.out.println("despues; x0: "+ h.getX0());
							// System.out.println("antes; y0: "+ h.getY0());
							h.setY0(pz.getAlto() * h.getY0());
							// System.out.println("despues; y0: "+ h.getY0());
							// System.out.println("-------");
							h.calculaAreaDeHueco();
						}
						// System.out.println("--------------------------------------");
					}
				}

			}
		}

	}

	/**
	 * Rutina que crea una foto de las piezas colocadas aleatoriamente sobre la
	 * correa Crea rectangulos: Rect0
	 */
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

	public void ejecutaBezierFull() {
		if (myBezierFull == null)
			myBezierFull = new BezierFull();
		myBezierFull.recorreDirectorio_inkscape_svg_Trabajando();
	}

	public void creaPrototipos() {
		if (myPrototipos == null)
			myPrototipos = new Prototipos();
		myPrototipos.leePrototiposDesdeFile_Bezier_Normal();
	}

	public Prototipos getMyPrototipos() {
		return myPrototipos;
	}

	public void setMyPrototipos(Prototipos myPrototipos) {
		this.myPrototipos = myPrototipos;
	}

	public BezierFull getMyBezierFull() {
		return myBezierFull;
	}

	public LinkedList<CurvasDeBezierDeObjeto> getMyCurvasDeBezierLL() {
		return myCurvasDeBezierLL;
	}

	public void setMyCurvasDeBezierLL(LinkedList<CurvasDeBezierDeObjeto> myCurvasDeBezierLL) {
		this.myCurvasDeBezierLL = myCurvasDeBezierLL;
	}

	public int getNoPrototipos() {
		return noPrototipos;
	}

	public void setNoPrototipos(int noPrototipos) {
		this.noPrototipos = noPrototipos;
	}

	public LinkedList<CurvasDeBezierDeObjeto> getCurvasObjetoList() {
		return curvasObjetoList;
	}

	public void setCurvasObjetoList(LinkedList<CurvasDeBezierDeObjeto> curvasObjetoList) {
		this.curvasObjetoList = curvasObjetoList;
	}

	public int getNoPiezasH() {
		return noPiezasH;
	}

	public double getxDeCorteF() {
		return xDeCorteF;
	}

	public void setxDeCorteF(double xDeCorteF) {
		this.xDeCorteF = xDeCorteF;
	}

	public double getxDeCorteCols() {
		return xDeCorteCols;
	}

	public void setxDeCorteCols(double xDeCorteCols) {
		this.xDeCorteCols = xDeCorteCols;
	}

	public void setNoPiezasH(int noPiezasH) {
		this.noPiezasH = noPiezasH;
	}

	public DibujoDeCortador getMyDibujoDeCortador() {
		return myDibujoDeCortador;
	}

	public void setMyDibujoDeCortador(DibujoDeCortador myDibujoDeCortador) {
		this.myDibujoDeCortador = myDibujoDeCortador;
	}

	public void setMyBezierFull(BezierFull myBezierFull) {
		this.myBezierFull = myBezierFull;
	}

	/**
	 * Ejecucion minima de Procesos dentro de SacoBasadoEnPrototipos
	 * 
	 * @param noPiezasH
	 * @param minDx
	 * @param maxDx
	 * @param minDy
	 * @param maxDy
	 */
	public void ejecuta0(int noPiezasH, double minDx, double maxDx, double minDy, double maxDy) {
		generaPiezasDesdePrototipoEnSaco(noPiezasH, minDx, maxDx, minDy, maxDy);
		creaFotoRect0();
	}

	public void ejecuta1() {
		creaFotoRectEjeX();
	}

	public void ejecuta2() {
		PiezaH pz = piezaOrdenados.get(0);
		llenar(pz, pz.getIndice());
		generaTodasLasPiezasContenidasEnArbolHastaNivel0y1ParaTodasLasPiezas();
		generaMyPiezasHInsertadasTotalIParaTodasLasPiezas();
		ajustaCoordenadasDeTodasLasPiezasconRespectoAOrigenDePiezas();
		calculaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0ParaTodasLasPiezas();
		creaFotoRectCols();
		setxDeCorteCols(obtieneXDeCorteUsandoXP0());
	}

	public void ejecuta3(double delta) {
		acercaPiezasDeColumnasDePieza0(delta);
		creaFotoRectF();
	}

	/**
	 * * Esta funcion se usa desde el DibujoDeCortador cuando se usa el contructor
	 * sin parametros!!
	 * 
	 * @param noPiezasH : no de piezas; entero > 1
	 * @param anchoMin  : ancho minimo double en[0.,1.]
	 * @param anchoMax: ancho maximo double en[0.,1.]
	 * @param altoMin:  alto minimo double en [0.,1.]
	 * @param altoMax   :alto maximo double en[0.,1.]
	 * @param delta     :cantidad de acercamiento de piezas (double) : 0.01
	 *                  corresponde aproximadamente a 1 pixel en despligue normal
	 *                  con escala (1.x 1.) -->(100x100)
	 * @param acelOn    ;: acelara o no acercamiento :false es mas seguro para
	 *                  despliegua
	 */
	public void trabajoCompleto(int noPiezasH, double anchoMin, double anchoMax, double altoMin, double altoMax,
			double delta, boolean acelOn) {
		
		ejecuta0(noPiezasH, anchoMin, anchoMax, altoMin, altoMax);
		ejecuta1();
		ejecuta2();
		myCalculoLargoPar = new CalculoLargoPar(myDibujoDeCortador, this, delta, acelOn);
		myCalculoLargoPar.execute();

	}

	public static void main(String[] args) {
		SacoBasadoEnPrototipos mySacoBasadoEnPrototipos = new SacoBasadoEnPrototipos();
		// mySacoBasadoEnPrototipos.ejecutaBezierFull();
		// mySacoBasadoEnPrototipos.creaPrototipos();
		// mySacoDePrototipos.gereraPiezas();
		mySacoBasadoEnPrototipos.trabajoCompleto(5, 0.05, 0.5, 0.05, 0.5, 0.001, false);
		// mySacoBasadoEnPrototipos.ejecuta0(50, 0.05, 0.5, 0.05, 0.5);
		// mySacoBasadoEnPrototipos.muestraPiezasEnConsola();
		// mySacoBasadoEnPrototipos.ejecuta1();
		// mySacoBasadoEnPrototipos.ejecuta2();

	}
	
	private class CalculoLargoPar extends SwingWorker<SacoBasadoEnPrototipos, Integer> {
		SacoBasadoEnPrototipos mySacoBasadoEnPrototipos = null;
		public int indCol = 1;
		private int tam;
		private DibujoDeCortador myDibujoDeCortador;
		private boolean seguir = true;
		private double delta = 0.;
		private boolean acelOn = false;

		public CalculoLargoPar(DibujoDeCortador myDibujoDeCortador, SacoBasadoEnPrototipos mySacoBasadoEnPrototipos,
				double delta, boolean acelOn) {
			this.mySacoBasadoEnPrototipos = mySacoBasadoEnPrototipos;
			this.myDibujoDeCortador = myDibujoDeCortador;
			this.delta = delta;
			this.acelOn = acelOn;
		}

		public void setSeguir(boolean seg) {
			seguir = seg;
		}

		@Override
		protected SacoBasadoEnPrototipos doInBackground() throws Exception {

			Hueco h0 = mySacoBasadoEnPrototipos.getPiezasOrdenadas().get(0).getMyListaDeHuecos().get(0);
			// h0 es el hueco de la pieza0, donde estan las columnas que se acercaran
			tam = h0.getMyColumnasH().size();
			for (int i = 1; seguir && i < tam; i++) {

				mySacoBasadoEnPrototipos.acercaPiezasDeColumnasDePieza0ConAceleracion(i, delta, acelOn);

				publish(i);// valor que se publica con process
				creaFotoRectF();

				setxDeCorteF(obtieneXDeCorteUsandoXP0());

			}

			return mySacoBasadoEnPrototipos;
		}

		protected void process(List<Integer> chunk) {
			int porcen = (int) (100. * (1. * chunk.get(chunk.size() - 1) / tam));
			System.out.println("% de work: " + porcen);
			myDibujoDeCortador.escribeEstadoDeProgreso(porcen);

		}

		protected void done() {

			System.out.println("Termine  ...");
			myDibujoDeCortador.setEstatusAcercamientoDePiezas(true);
			myDibujoDeCortador.repaint();
		}

	}

	public void stopCalculoLargoPar() {
		// myCalculoLargo.
		if (myCalculoLargoPar != null) {
			myCalculoLargoPar.setSeguir(false);
			myCalculoLargoPar.cancel(true);
		}

	}

	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getSource() == myCalculoLargoPar) {

			if (myCalculoLargoPar.getState() == StateValue.DONE) {

				myDibujoDeCortador.setEstatusAcercamientoDePiezas(true);
				myDibujoDeCortador.repaint();
			}

		}
	}

}
