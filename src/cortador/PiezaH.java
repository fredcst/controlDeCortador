package cortador;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Version en numeros reales de la clase Pieza
 * 
 * La idea es que TODOS los calculos intermedios se hagan usando numeros reales.
 * Solo en el momento de desplieqgue en pantalla grafica se puede obtener, por
 * escalamiento, dimensiones enteras. Las Piezas estan con dimensiones etre cero
 * y 1, salvo la plancha decorte, que tiene dimensiones 1. para el alto , y un
 * numero muy grade , mayor que 1., para el ancho. Cada pieza se distingue de
 * las otras por un alto y ancho reales, que reducen sus dimensiones a numeros
 * menores que 1. Asemas, cada pieza tiene HUECOS internos, que son
 * rectangulares. Estos podran ser llenados por columnas de piezas, dusrante el
 * proceso de ordenacion sobre la correa de corte. La correa de corte se
 * considera una pieza de alto 1. con 1 HUECO gigante que va desde posiscion
 * x=0, relativa a origen (0,0) de pieza t tiene alto :1 En resumen, cada pieza
 * es un rectangulo con dimensiones entre 0. y 1., con HUECOS rectangulares, sin
 * trslape, colocados a diferentes distancias del origen del rectangulo de cada
 * pieza.
 * 
 * En una primera etapa de desarrollo del algoritmo, solo se trabajara con
 * rectangulos y huecos. Luego, cada rectangulo sera llenado con las partes
 * visibles de las piezas que representan. .
 * 
 * @author
 *
 */
public class PiezaH {
	private double EPS = 1.0;// mientras mas pequeno mas grande los huecos[0,1.]
	private double ancho = 1., alto = 1.;
	LinkedList<Path> pathList = null; // Lugar donde se almacenan curvas de Bezier
	LinkedList<Path> pathListMod = null; // va ha contener figuras rotadas NO SE USA
	LinkedList<Path> pathListMod2 = null; // va ha contener figuras rotadas NO SE USA
	private double area = 0;
	private int indice;
	private String nombre = "-";
	private double posX = 0, posY = 0;// coordenadas c/r alHueco que las contiene
	private double posXP = 0, posYP = 0;// coordenadas c/r a la pieza que las contiene
	private double posXP0 = 0, posYP0 = 0;// coordenadas c/r al correa (pieza 0)que las contiene

	private RectanguloH rect0 = null; // rectangulo con cordenadas en situacion random
	private RectanguloH rectEjeX = null; // rectangulo con cordenadas en situacin alineada ejeX
	private RectanguloH rectCols = null; // rectangulo con cordenadas en situacin alineada en cols
	private RectanguloH rectF = null; // rectangulo con cordenadas en situacion final

	private Color colorP = null;
	private boolean usado = false; // indica si pieza esta sobre alguna columna
	private ArrayList<Hueco> myListaDeHuecos = null;
	private ArrayList<PiezaH> myPiezasHInsertadas = null;
	private ArrayList<PiezaH> myPiezasHInsertadasTotal = null;// incluye todo el arbol
	private ArrayList<PiezaH> myPiezasHInsertadasTotalI = null;// incluye todo el arbol se incluye a si mismo
	private ArrayList<Double> myDistanciasCrOPiezas = null;
	private boolean nivel1 = false;
	private int nivel = 0;
	private boolean estaRotadaM90 = false;
	private boolean tieneHuecosInternos = false;

	private boolean estaInsertada = false;// permite saber si pieza esta insertada en columna

	private boolean tienePiezasInsertadas = false;

	private int noHuecos = 0;
	double[] xis = null;
	double[] yis = null;
	boolean DEBUG1 = false;
	boolean DEBUG = false;
	boolean DEBUG2 = false;
	private boolean cabeRectaEnHueco = true;

	private int noDePiezasInsertadas = 0;
	private PiezaH estoyInsertadaEn = null;// Ludar donde posiblemente estara insertada
	// private Columna ultimaColumnaIncompletaDondeCabe = null;
	// private ColumnaFinalEnHueco myColumnaFinalEnHueco = null;
	private boolean estaCompleto = false;// =completa
	private boolean completa = false;// =estaCompleta
	private ColumnaH lugarDeInsercion = null;// columna en hueco donde se insertara
	private boolean MUESTRA = false;

	public PiezaH() {
		myPiezasHInsertadas = new ArrayList<PiezaH>();
		myPiezasHInsertadasTotal = new ArrayList<PiezaH>();
		myListaDeHuecos = new ArrayList<Hueco>();
	}

	/**
	 * Constructor neutro Se usa para la correa que tiene un hueco gigante la pieza
	 * 0 tiene qncho >1 muy grande y alto fijo=1.
	 */
	public PiezaH(int indice, double ancho) {
		if (ancho < 0)
			ancho = 0.;
		// if (ancho > 1)// La pieza cero tiene abcho >1 !!!!!
		// ancho = 1.;
		// if (indice==0)ancho=10.;

		if (indice < 0)
			indice = 0;
		this.indice = indice;
		this.ancho = ancho;
		this.alto = 1.0;
		Hueco h = new Hueco(ancho);
		myListaDeHuecos = new ArrayList<Hueco>();
		myListaDeHuecos.add(h);
		setTieneHuecosInternos(true);
		area = alto * ancho;
		myPiezasHInsertadas = new ArrayList<PiezaH>();
		myPiezasHInsertadasTotal = new ArrayList<PiezaH>();
	}

	/**
	 * Constructor de PiezaH CON UN SOLO HUECO
	 * 
	 * @param indice indice de Pieza
	 * @param nombre nombre de Pieza
	 * @param ancho  ancho de rectangulo que contiene a la Pieza
	 * @param alto   alto del rectangulo que contine a la Pieza
	 */
	public PiezaH(int indice, double ancho, double alto) {
		if (ancho < 0)
			ancho = 0.;
		if (ancho > 1)
			ancho = 1.;
		if (alto < 0)
			alto = 0.;
		if (alto > 1)
			alto = 1.;
		if (noHuecos < 0)
			noHuecos = 0;
		if (indice < 0)
			indice = 0;
		this.indice = indice;
		this.ancho = ancho;
		this.alto = alto;
		ordenaDimensionesDePieza(1.);// alto > ancho
		area = alto * ancho;
		generaHuecosArificialesEnRectanguloDePieza(1);
		myPiezasHInsertadas = new ArrayList<PiezaH>();
		myPiezasHInsertadasTotal = new ArrayList<PiezaH>();
	}

	/**
	 * Crea piezaH con noHuecos
	 * 
	 * @param indice
	 * @param nombre
	 * @param ancho
	 * @param alto
	 * @param noHuecos
	 */
	public PiezaH(int indice, double ancho, double alto, int noHuecos) {
		if (ancho < 0)
			ancho = 0.;
		if (ancho > 1)
			ancho = 1.;
		if (alto < 0)
			alto = 0.;
		if (alto > 1)
			alto = 1.;
		if (noHuecos < 0)
			noHuecos = 0;
		if (indice < 0)
			indice = 0;
		this.indice = indice;
		this.ancho = ancho;
		this.alto = alto;
		this.noHuecos = noHuecos;
		ordenaDimensionesDePieza(1.);// alto > ancho
		area = alto * ancho;
		generaHuecosArificialesEnRectanguloDePieza(noHuecos);
		myPiezasHInsertadas = new ArrayList<PiezaH>();
		myPiezasHInsertadasTotal = new ArrayList<PiezaH>();
	}

	/**
	 * Crea piezaH con noHuecos Se usa el parametro EPS para regular el tamano de
	 * los huecos artificiales 0<=EPS<=1 . Si EPS=0 Huecos grandes. Si EPS=1 huecos
	 * mas pequenos
	 * 
	 * @param indice   indice de pieza. Sirve para identificarla
	 * @param nombre   Nombre de la pieza
	 * @param ancho    ancho de la pieza .No real pozitivo entre 0 y 1
	 * @param alto     alto de la pieza .No real pozitivo entre 0 y 1
	 * @param noHuecos No de huecos en la pieza Entero >=0
	 */
	public PiezaH(int indice, double ancho, double alto, int noHuecos, double eps) {
		if (ancho < 0)
			ancho = 0.;
		if (ancho > 1)
			ancho = 1.;
		// if(indice==1)ancho=2.;
		if (alto < 0)
			alto = 0.;
		if (alto > 1)
			alto = 1.;
		if (noHuecos < 0)
			noHuecos = 0;
		if (indice < 0)
			indice = 0;
		this.indice = indice;
		this.ancho = ancho;
		this.alto = alto;
		this.noHuecos = noHuecos;
		if (eps >= 1.)
			eps = 1.;
		if (eps < 0)
			eps = 0.;
		EPS = eps;
		ordenaDimensionesDePieza(1.);// alto > ancho
		area = alto * ancho;
		generaHuecosArificialesEnRectanguloDePieza(noHuecos);
		myPiezasHInsertadas = new ArrayList<PiezaH>();
		myPiezasHInsertadasTotal = new ArrayList<PiezaH>();
	}

	public int getNoHuecos() {
		return noHuecos;
	}

	public void setNoHuecos(int noHuecos) {
		this.noHuecos = noHuecos;
	}

	/**
	 * 
	 * @return ancho de la pieza
	 */
	public double getAncho() {
		return ancho;
	}

	public void setAncho(double ancho) {
		this.ancho = ancho;
	}

	public double getAlto() {
		return alto;
	}

	public void setAlto(double alto) {
		this.alto = alto;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene coordenada X c/r al origen del Hueco que las contiene
	 * 
	 * @return
	 */
	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosXP() {
		return posXP;
	}

	/**
	 * Genera lista de huecos internos en dobles a partir de curvas de Bezier. En
	 * myListaDeHuecos se guardan los huecos internos cuanso la pieza esta o NO
	 * rotada. Se usa : pathList. Los huecos salen ordenados de mayor a menor area
	 * en la pieza
	 */
	public void generaListaHuecosInternosAPartirDeCDeBezier() {
		// System.out.println("En generaListaHuecosInternosAPartirDeCDeBezier en Pieza;
		// "+ getNombre());
		double x10, y10, x1, y1, x2, y2, dX, dY;
		double rX1, rX2, rY1, rY2, rX3, rX4, rY3, rY4, minx, maxx, miny, maxy;
		minx = 100000.;
		maxx = -10000.;
		miny = 100000.;
		maxy = -10000.;
		EleCalculo e;
		LinkedList<EleCalculo> pathx = null;
		if (pathList != null) {
			ArrayList<Hueco> myListaDeHuecosL = new ArrayList<Hueco>();
			int tam = pathList.size();
			// int s=0;
			for (Path p : pathList) {
				if (p.flag_vis == 0) {
					// System.out.println("Path con flag_vis=0 "+ s);
					tieneHuecosInternos = true;

					// System.out.println("En generaListaHuecosInternos vis=0");
					// if (myListaDeHuecos == null)
					// myListaDeHuecos = new ArrayList<Hueco>();
					// myListaDeHuecos.clear();

					pathx = p.path;

					e = pathx.get(0);
					x10 = e.xP1;
					y10 = e.yP1;
					x2 = e.xP2;
					y2 = e.yP2;
					dX = x2 - x10;
					// System.out.println("x0: " + x10 + " y10: " + y10 + " x2: " + x2 + " y2: " +
					// y2);

					e = pathx.get(1);
					x1 = e.xP1;
					y1 = e.yP1;
					x2 = e.xP2;
					y2 = e.yP2;
					dY = y2 - y10;
					// System.out.println("x1: " + x1 + " y1: " + y1 + " x2: " + x2 + " y2: " + y2);
					// System.out.println("x10: " + x10 + " y10: " + y10 + " DX: " + dX + " DY: " +
					// dY);
					myListaDeHuecosL.add(new Hueco(x10, y10, dX, dY));
					// s++;
				}

			}
			Hueco[] recHuecosInternosHArray = new Hueco[myListaDeHuecosL.size()];
			recHuecosInternosHArray = myListaDeHuecosL.toArray(recHuecosInternosHArray);
			HuecoSort myHuecoSort = new HuecoSort();
			myHuecoSort.quickSort(recHuecosInternosHArray, 0, recHuecosInternosHArray.length - 1);
			if (myListaDeHuecos == null)
				myListaDeHuecos = new ArrayList<Hueco>();
			for (int i = 0; i < recHuecosInternosHArray.length; i++)
				myListaDeHuecos.add(recHuecosInternosHArray[i]);
		}
		// System.out.println(getNombre()+ "tiene: "+myListaDeHuecos.size()+" huecos");
	}

	public void generaPathListMod() {

		pathListMod = creaCopiaDeCurvasDeBezier(pathList);
	}

	/*
	 * En general NO USAR El escalamiento se produce via los rectangulos de
	 * despliegue de fotos
	 * 
	 */
	public void escalaCurbasDeBezier(double escalax, double escalay) {
		if (!pathList.isEmpty()) {
			int tam = pathList.size();
			Path pathx = null;
			LinkedList<EleCalculo> path = null;
			for (int i = 0; i < tam; i++) {
				pathx = pathList.get(i);
				path = pathx.path;
				for (int j = 0; path != null && j < path.size(); j++) {
					path.get(j).setxP1(escalax * path.get(j).getxP1());
					path.get(j).setxP2(escalax * path.get(j).getxP2());
					path.get(j).setxP3(escalax * path.get(j).getxP3());
					path.get(j).setxP4(escalax * path.get(j).getxP4());

					path.get(j).setyP1(escalay * path.get(j).getyP1());
					path.get(j).setyP2(escalay * path.get(j).getyP2());
					path.get(j).setyP3(escalay * path.get(j).getyP3());
					path.get(j).setyP4(escalay * path.get(j).getyP4());

					/*
					 * path.get(j).xP1 = escalax * path.get(j).xP1; path.get(j).xP2 = escalax *
					 * path.get(j).xP2; path.get(j).xP4 = escalax * path.get(j).xP3; path.get(j).xP4
					 * = escalax * path.get(j).xP4; path.get(j).yP1 = escalay * path.get(j).yP1;
					 * path.get(j).yP2 = escalay * path.get(j).yP2; path.get(j).yP4 = escalay *
					 * path.get(j).yP3; path.get(j).yP4 = escalay * path.get(j).yP4;
					 */
				}
			}
		}
	}

	public static LinkedList<Path> creaCopiaDeCurvasDeBezier(LinkedList<Path> paths) {
		LinkedList<Path> pathsCopia = null;
		if (paths != null) {
			pathsCopia = new LinkedList<Path>();
			if (!paths.isEmpty()) {
				Path pathx = null;
				Path pathxCopia = null;
				int tam = paths.size();
				LinkedList<EleCalculo> path = null;
				LinkedList<EleCalculo> pathCopia = null;
				int id = 0;
				int flag_vis = 0;
				for (int i = 0; i < tam; i++) {
					pathx = paths.get(i);
					path = pathx.path;
					id = pathx.id;
					flag_vis = pathx.flag_vis;
					pathCopia = new LinkedList<EleCalculo>();
					EleCalculo ex = null;
					for (int j = 0; path != null && j < path.size(); j++) {
						ex = new EleCalculo(path.get(j).t, path.get(j).xP1, path.get(j).yP1, path.get(j).xP2,
								path.get(j).yP2, path.get(j).xP3, path.get(j).yP3, path.get(j).xP4, path.get(j).yP4);
						pathCopia.add(ex);
					}

					pathxCopia = new Path(id, flag_vis, pathCopia);
					pathsCopia.add(pathxCopia);
				}
			}
		}
		return pathsCopia;
	}

	public void setPosXP(double posXP) {
		this.posXP = posXP;
	}

	public double getPosYP() {
		return posYP;
	}

	public void setPosYP(double posYP) {
		this.posYP = posYP;
	}

	public double getPosXP0() {
		return posXP0;
	}

	public void setPosXP0(double posXP0) {
		this.posXP0 = posXP0;
	}

	public double getPosYP0() {
		return posYP0;
	}

	public void setPosYP0(double posYP0) {
		this.posYP0 = posYP0;
	}

	public double getEPS() {
		return EPS;
	}

	public void setEPS(double ePS) {
		EPS = ePS;
	}

	/**
	 * Obtiene coordenada Y c/r al origen del Hueco que las contiene
	 * 
	 * @return
	 */
	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public Color getColorP() {
		return colorP;
	}

	public void setColorP(Color colorP) {
		this.colorP = colorP;
	}

	public boolean isCompleta() {
		return completa;
	}

	public void setCompleta(boolean completa) {
		this.completa = completa;
		this.estaCompleto = completa;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;

	}

	public ArrayList<Hueco> getMyListaDeHuecos() {
		return myListaDeHuecos;
	}

	public void setMyListaDeHuecos(ArrayList<Hueco> recHuecosInternosH) {
		this.myListaDeHuecos = recHuecosInternosH;
	}

	/**
	 * Permite indicar que la pieza y sus huecos fue rotada
	 * 
	 * @return true si esta rotada
	 */
	public boolean isEstaRotadaM90() {
		return estaRotadaM90;
	}

	/**
	 * Setea el flag de que la pieza esta rotada
	 * 
	 * @param estaRotadaM90
	 */
	public void setEstaRotadaM90(boolean estaRotadaM90) {
		this.estaRotadaM90 = estaRotadaM90;
	}

	public boolean isTieneHuecosInternos() {
		return tieneHuecosInternos;
	}

	public void setTieneHuecosInternos(boolean tieneHuecosInternos) {
		this.tieneHuecosInternos = tieneHuecosInternos;
	}

	public boolean isEstaInsertada() {// ==esta Usada
		return estaInsertada;
	}

	public void setEstaInsertada(boolean estaInsertada) {
		this.estaInsertada = estaInsertada;
	}

	public boolean isTienePiezasInsertadas() {
		return tienePiezasInsertadas;
	}

	public void setTienePiezasInsertadas(boolean tienePiezasInsertadas) {
		this.tienePiezasInsertadas = tienePiezasInsertadas;
	}

	public void generaColorDePieza() {
		int rC = Utilidades.getRandomNumberInRange(0, 200);
		int gC = Utilidades.getRandomNumberInRange(0, 200);
		int bC = Utilidades.getRandomNumberInRange(0, 200);
		colorP = new Color(rC, gC, bC);
	}

	public LinkedList<Path> getPathListMod() {
		return pathListMod;
	}

	public void setPathListMod(LinkedList<Path> pathListMod) {
		this.pathListMod = pathListMod;
	}

	public LinkedList<Path> getPathListMod2() {
		return pathListMod2;
	}

	public void setPathListMod2(LinkedList<Path> pathListMod2) {
		this.pathListMod2 = pathListMod2;
	}

	public void generaColorDePieza(int r, int g, int b) {
		int rC = Utilidades.getRandomNumberInRange(0, r);
		int gC = Utilidades.getRandomNumberInRange(0, g);
		int bC = Utilidades.getRandomNumberInRange(0, b);
		colorP = new Color(rC, gC, bC);
	}

	/**
	 * Ordena dimensiones de pieza( alto > ancho) El ancho estara sobre eje X de
	 * correa
	 */
	public void ordenaDimensionesDePieza() {
		// ancho sera lo mas delgado en pieza!!!
		double pivot = ancho;
		if (ancho > alto) {
			ancho = alto;
			alto = pivot;
		}
	}

	public void ordenaDimensionesDePieza(double lim) {
		// ancho sera lo mas delgado en pieza!!!
		double pivot = ancho;
		if (ancho > alto) {
			ancho = alto;
			alto = pivot;
		} // ordena que ancho < alto
		if (ancho < lim && alto > lim) {
			pivot = alto;
			alto = ancho;
			ancho = pivot;
		} // pero si alto > lim, vuelve atras
		if (ancho >= lim && alto >= lim) {// pieza cuadrada dentro de plancha
			ancho = lim * 0.999999999;
			alto = lim * 0.9999999999;
		}

		return;
	}

	public Color getColorDePieza() {
		return colorP;
	}

	public boolean isNivel1() {
		return nivel1;
	}

	public void setNivel1(boolean nivel1) {
		this.nivel1 = nivel1;
	}

	public int getNoDePiezasInsertadas() {
		return noDePiezasInsertadas;
	}

	public void setNoDePiezasInsertadas(int noDePiezasInsertadas) {
		noDePiezasInsertadas = noDePiezasInsertadas;
	}

	public void calculaArea() {
		area = ancho * alto;
	}

	public PiezaH getEstoyInsertadaEn() {
		return estoyInsertadaEn;
	}

	public void setEstoyInsertadaEn(PiezaH estoyInsertadaEn) {
		this.estoyInsertadaEn = estoyInsertadaEn;
	}

	/**
	 * Copia color de pieza
	 * 
	 * @return
	 */
	public void setColorDePieza(Color colorP) {
		this.colorP = colorP;
	}

	/**
	 * Copia aspectos basicos de pieza. FALTA INFORMACION
	 * 
	 * @param newP
	 */
	public void copiaPieza(PiezaH newP) {
		newP.setAlto(alto);
		newP.setAncho(ancho);
		newP.setColorDePieza(this.getColorDePieza());
		return;
	}

	/**
	 * Genera huecos para pieza:this y los ordena de mayor a menor area Huecos mas
	 * grandes primero
	 * 
	 * @param NoHuecos
	 */
	public void generaHuecosArificialesEnRectanguloDePieza(int NoHuecos) {
		myListaDeHuecos = new ArrayList<Hueco>();
		ArrayList<Hueco> myListaDeHuecosL = new ArrayList<Hueco>();

		if (noHuecos > 0) {
			setTieneHuecosInternos(true);
			setEstaCompleto(false);
			xis = new double[noHuecos];
			for (int i = 0; i < noHuecos; i++) {
				xis[i] = Math.random();
			}
			QuikSort myQuikSort = new QuikSort();
			myQuikSort.quickSort(xis, 0, noHuecos - 1);

			yis = new double[noHuecos];
			for (int i = 0; i < noHuecos; i++) {
				yis[i] = Math.random();
			}

			myQuikSort.quickSort(yis, 0, noHuecos - 1);
			// Genera una grilla que tendra huecos
			for (int i = 0; i < noHuecos - 1; i++) {
				myListaDeHuecosL.add(new Hueco(xis[i] * ancho, yis[i] * alto,
						ancho * (xis[i + 1] - xis[i]) * (1. - EPS / (1 + getIndice()) * Math.random()),
						alto * (yis[i + 1] - yis[i]) * (1. - EPS / (1 + getIndice()) * Math.random())));
				// Los huecos comienzan en los puntos de la grilla y tienen un ancho
				// y alto menor que los espacios de
				// la grilla
			}
			setTieneHuecosInternos(true);
		}
		if (noHuecos == 0) {// Genera una pieza homogenea con hueco en extremo inf der con tamano =0
			noHuecos = 1;
			xis = new double[1];
			xis[0] = 1.0;
			yis = new double[1];
			yis[0] = 1.0;
			myListaDeHuecosL.add(new Hueco(ancho, alto, 0., 0.));
			setTieneHuecosInternos(false);
			setEstaCompleto(true);// No podra ser llenada
		}
		// Ordena los huecos de mayor a menor area
		Hueco[] recHuecosInternosHArray = new Hueco[myListaDeHuecosL.size()];
		recHuecosInternosHArray = myListaDeHuecosL.toArray(recHuecosInternosHArray);
		HuecoSort myHuecoSort = new HuecoSort();
		myHuecoSort.quickSort(recHuecosInternosHArray, 0, recHuecosInternosHArray.length - 1);

		for (int i = 0; i < recHuecosInternosHArray.length; i++)
			myListaDeHuecos.add(recHuecosInternosHArray[i]);
		// myPiezasHInsertadas = new ArrayList<PiezaH>();// Inicializa la lista de
		// piezas insertadas
		// En recHuecosInternosH estan los huecos ordenados de mayor a menor area
	}

	public ArrayList<PiezaH> getMyPiezasHInsertadas() {
		return myPiezasHInsertadas;
	}

	public void setMyPiezasHInsertadas(ArrayList<PiezaH> piezasHInsertadas) {
		this.myPiezasHInsertadas = piezasHInsertadas;
	}

	/**
	 * Coordenadas X de red de huecos
	 * 
	 * @return
	 */
	public double[] getXis() {
		return xis;
	}

	public void setXis(double[] xis) {
		this.xis = xis;
	}

	/**
	 * Coordenadas Y de red de huecos
	 * 
	 * @return
	 */
	public double[] getYis() {
		return yis;
	}

	public void setYis(double[] yis) {
		this.yis = yis;
	}

	/**
	 * Se verifica si this cabe en la pieza base pzBasex
	 * 
	 * @param pzBasex
	 * @return
	 */
	/*
	 * public boolean cabeEn(PiezaH pzBasex) { if (DEBUG1)
	 * System.out.println("--- Entre a cabeEn-- para probar segmento: (" +
	 * getAncho() + "," + getAlto() + ") si cabe en :(" + pzBasex.getAncho() + "," +
	 * pzBasex.getAlto() + ")"); // TODO Auto-generated method stub ArrayList<Hueco>
	 * huecos = pzBasex.getRecHuecosInternosH(); ArrayList<Columna> cols = null;
	 * boolean res = false; if (huecos != null && !huecos.isEmpty()) { //
	 * System.out.println("Espacios !=null"); int tamHuecos = huecos.size(); Hueco e
	 * = null; boolean SIGO = true; int i = 0; while (SIGO && i < tamHuecos) {
	 * 
	 * 
	 * if (DEBUG1) System.out.println("espacio de llenado previo: " +
	 * e.getLlenado_previo()); if (escala < (e.getAncho() - e.getLlenado_previo()))
	 * { if (DEBUG1) System.out.println("tamano: " + getEscala() +
	 * " cabe en hueco: " + e.getAncho()); res = res || true; } if (DEBUG1)
	 * System.out.println("");
	 * 
	 * e = huecos.get(i); if (e.cabeEn(this)) {//Si this cabe en hueco e se sale
	 * SIGO = false; res = true; }
	 * 
	 * i++; }
	 * 
	 * }
	 * 
	 * if (DEBUG1) System.out.println("cabe en es: " + res); if (DEBUG1)
	 * System.out.println("__Termine se chequear si segmento cabe en: (" +
	 * pzBasex.getAncho() + "," + pzBasex.getAlto() + ")"); if (DEBUG1)
	 * System.out.println(""); return res; }
	 */
	/**
	 * Entrega si la pieza cabe SIN rotaciones en algun hueco donde cabe
	 * 
	 * @return true si es asi. false, si cabe rotada
	 */
	public boolean isCabeRectaEnHueco() {
		return cabeRectaEnHueco;
	}

	/**
	 * Permite setear la condicion si cabe rec o ratada en algun hueco
	 * 
	 * @param cabeRectaEnHueco : indica que pieza cabe recta en hueco
	 */
	public void setCabeRectaEnHueco(boolean cabeRectaEnHueco) {
		this.cabeRectaEnHueco = cabeRectaEnHueco;
	}

	/**
	 * Trata de insertar la pieza x en this.
	 * 
	 * @param x    :Pieza a insertar
	 * @param colx
	 * @return true si la pieza ha sido insertada
	 */
	/*
	 * public boolean inserta(PiezaH x, ColumnaH colx) { if (DEBUG)
	 * System.out.println(""); if (DEBUG)
	 * System.out.println("---------Insertando----------: (" + x.getAncho() + "," +
	 * x.getAlto() + ")"); boolean res = false; ArrayList<Hueco> huecos =
	 * getMyListaDeHuecos();// obtiene los huecos internos de this.
	 * ArrayList<PiezaH> segs = null; if (huecos != null) { //
	 * System.out.println("Dentro de espacios"); // for (Hueco e : huecos) { if
	 * (!x.isUsado() && x.cabeEn(this)) {// Pieza x NO ha sido insertada antes y
	 * cabe en pieza ColumnaFinalEnHueco colHueco = x.getMyColumnaFinalEnHueco();//
	 * obtiene datos donde insertar // recupera datos donde insertar Hueco e =
	 * colHueco.getHuecoDondeInsertar();// hueco Columna col =
	 * colHueco.getColDondeInsertar();// columna PiezaH pzh =
	 * colHueco.getPiezaDondeInsertar();// pieza que debe ser this if (pzh == this)
	 * {// verifica que en esta pieza hay que insertar
	 * 
	 * e.inserta(x, col);// inserta en hueco e en columna col
	 * e.getPiezasHInsertadas().add(x);// actuaiza piezas insertadas en hueco
	 * myPiezasHInsertadas.add(x);// actualiza piezas insertadas en esta pieza
	 * tienePiezasInsertadas = true;// indica que tiene piezas insertadas
	 * x.setUsado(true);// coloca a la pieza x como usada
	 * 
	 * } else {
	 * System.out.println("PiezaH:inserta:INCOMPATIVILIDAD EN PIEZAS Salgo...");
	 * System.exit(0); } if (DEBUG)
	 * System.out.println("segmento a insertar No usado antes");
	 * 
	 * if (x.getAncho() < (e.getAnc() - e.getLlenado_previo())) {
	 * if(DEBUG)System.out.println("segmento a insertar cabe en hueco de segmento");
	 * e.setLlenado_previo(e.getLlenado_previo() + x.getEscala());
	 * if(DEBUG)System.out.
	 * println("ancho  de llenado de hueco despues de insertar : " +
	 * e.getLlenado_previo()); if(DEBUG)System.out.println( "seg de tamano: " +
	 * x.getEscala() + " insertando en hueco de tamano: " +
	 * e.getAncho()+" en segmento de ancho: "+getEscala()); x.setEstaUsado(true);
	 * e.getGuardados().add(x); segs = e.getGuardados();
	 * if(DEBUG)System.out.println("Lista de segmentos guardados en hueco: "+e.
	 * getAncho()); if (segs != null) { for (Segmento seg : segs) {
	 * if(DEBUG)seg.muestraSegmento();
	 * if(DEBUG)System.out.println("________________"); } } res = true; if
	 * (segInsertados != null) segInsertados.add(x); }
	 * 
	 * 
	 * } if (DEBUG) System.out.println("--------Termine de Insertar-----------"); if
	 * (DEBUG) System.out.println(""); } else { // Si no tiene huecos esta completa
	 * this.setEstaCompleto(true); res = false;
	 * 
	 * }
	 * 
	 * // TODO Auto-generated method stub return res; }
	 */
	/**
	 * Rutina CLAVE en algoritmo Se llama una vez que la pieza a insertar esta
	 * completa y se ha establecido que cabe en la columna de un hueco de: this
	 * 
	 * @param x:    PiezaH a insertar
	 * @param colx: columna de un hueco de: this, donde se insertara x
	 */
	public void inserta(PiezaH x, ColumnaH colx) {
		// Antes de entrar a esta rutina se ha establecido que x puede
		// ser insertada en la colomna colx
		colx.getMyPiezasH().add(x);
		x.setPiezaDondeEstaInsertada(this);
		x.setEstaInsertada(true);
		if (myPiezasHInsertadas != null) {
			myPiezasHInsertadas.add(x);
			noDePiezasInsertadas++;
			colx.reajustaMaxDeAnchoDeColConAnchoDe(x);// reajusta el ancho maximo
			colx.reajustaAltoTotalDeColConAltoDe(x);// reajusta el alto total de columna
			// de columna
		} else {
			System.out.println("PiezaH:inserta:myPiezasHInsertadas==null. Salgo...");
			System.exit(0);

		}
		return;
	}

	private void setPiezaDondeEstaInsertada(PiezaH piezaH) {
		estoyInsertadaEn = piezaH;
		// TODO Auto-generated method stub

	}

	/**
	 * Se llama una vez que se han revisado todas las piezas tratando de inserlas en
	 * los huecos de :this Establece que: this esta completa y lista para insertase
	 * en algun hueco.
	 * 
	 * @param b
	 */
	public void setEstaCompleto(boolean b) {
		// TODO Auto-generated method stub
		estaCompleto = b;
		completa = b;
	}

	/**
	 * Responde si: this esta completa
	 * 
	 * @return
	 */
	public boolean isEstaCompleto() {
		return estaCompleto;
	}

	public boolean estaCompleta() {
		// TODO Auto-generated method stub
		return completa;
	}

	/**
	 * Guarda ultima columna incompleta donde cabe para luego ser insertada
	 * 
	 * @param ultimaColumnaIncompleta
	 */

	/*
	 * public void setUltimaColumnaIncompletaDondeCabe(Columna
	 * ultimaColumnaIncompleta) { // TODO Auto-generated method stub
	 * ultimaColumnaIncompletaDondeCabe = ultimaColumnaIncompleta; }
	 * 
	 * public Columna getUltimaColumnaIncompletaDondeCabe() { // TODO Auto-generated
	 * method stub return ultimaColumnaIncompletaDondeCabe; }
	 * 
	 * public ColumnaFinalEnHueco getMyColumnaFinalEnHueco() { return
	 * myColumnaFinalEnHueco; }
	 * 
	 * public void setMyColumnaFinalEnHueco(ColumnaFinalEnHueco
	 * myColumnaFinalEnHueco) { this.myColumnaFinalEnHueco = myColumnaFinalEnHueco;
	 * }
	 */

	/**
	 * Rota a la pieza. Esta rutina hay que llamarla antes de empezar a completar:
	 * this con otras piezas
	 */
	public void rotaM90() {
		// TODO Auto-generated method stub
		if (tienePiezasInsertadas == false) {// Solo se debe usar antes de ser
			// completada
			double pivot = alto; // intercambia sus dimensiones!!!!
			alto = ancho;
			ancho = pivot;

			// Todos los huecos internos son transformados(rotados M90 y trasladados)
			if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {
				for (Hueco h : myListaDeHuecos) {
					pivot = h.getY0();
					h.setY0(h.getX0());// Rota corrdenadas y corre a 1er cuadrante
					h.setX0(-pivot + 1.0);

					pivot = h.getDy();
					h.setDy(h.getDx());// intercambia alto por ancho del hueco
					h.setDx(pivot);
				}

			}
			setEstaRotadaM90(true);// Informa que: this esta rotada

		} else {
			System.out.println("No se puede rotar solo la pieza pues tiene otras piezas insertadas!!");
			System.out.println("Salgo...");
			System.exit(0);
		}
		return;
	}

	/**
	 * Rota los vectores de Bezier de la Pieza
	 */
	public void rotaM90VectoresDeBezierConCorrimientoEnX() {

		if (pathList != null) {
			if (!pathList.isEmpty()) {
				int no_paths = pathList.size();
				double pivot = 0.;
				setEstaRotadaM90(true);
				for (int i = 0; i < no_paths; i++) {
					Path pathx = pathList.get(i);

					LinkedList<EleCalculo> path = pathx.path;
					int largo = path.size();
					for (int j = 0; j < largo; j++) {
						EleCalculo elex = path.get(j);
						pivot = elex.yP1;
						elex.yP1 = elex.xP1;
						elex.xP1 = -pivot + 1.;

						pivot = elex.yP2;
						elex.yP2 = elex.xP2;
						elex.xP2 = -pivot + 1.;

						pivot = elex.yP3;
						elex.yP3 = elex.xP3;
						elex.xP3 = -pivot + 1.;

						pivot = elex.yP4;
						elex.yP4 = elex.xP4;
						elex.xP4 = -pivot + 1.;

					}
				}
			}
		}
	}

	/*******************************************************************************************/
	/**
	 * Rota simultaneamente los bectores deBezier y los huecos
	 */
	public void rotaM90BezierYHuecos() {
		rotaM90VectoresDeBezierConCorrimientoEnX();
		rotaM90();
	}

	/*******************************************************************************************/
	/**
	 * Entrega la lista de Paths de la Pieza
	 * 
	 * @return Lista de Path de la Pieza
	 */

	public LinkedList<Path> getPathList() {
		return pathList;
	}

	/**
	 * Setea la lista de Path de la Pieza
	 * 
	 * @param pathList Lista de Path de la Pieza
	 */

	public void setPathList(LinkedList<Path> pathList) {
		this.pathList = pathList;
	}

	/**
	 * Setea la lista de Piezas insertadas Totales del arbol a nivel 1 incluyendose
	 * a si mismo.
	 * 
	 * @param myPiezasHInsertadasTotalI
	 */

	public void setMyPiezasHInsertadasTotalI(ArrayList<PiezaH> myPiezasHInsertadasTotalI) {
		this.myPiezasHInsertadasTotalI = myPiezasHInsertadasTotalI;
	}

	/**
	 * Rutina BASE Revisa si: this cabe en un hueco CAMBIO; ACEPTA PIEZAS ROTADAS EN
	 * COLUMNAS DESDE PRIMERA POSICION UP
	 */
	public boolean cabeEn(Hueco h) {
		boolean res = false;
		ArrayList<ColumnaH> cols = h.getMyColumnasH();
		ColumnaH col = null;
		if (cols != null) {
			if (cols.isEmpty()) {
				h.creaNuevaColumnaH();// sin elementos y altura y anchoMax=0
				col = cols.get(0);// obtiene columna recien creada
				if ((h.contieneDerechaAPiezaEnCol(this, 0))) {// Preguna si contiene deracha a :this
					setLugarDeInsercion(col);
					res = true;
				}
				// else if ((h.contieneRotM90APiezaEnCol(this, 0))) {// Cambio Acepta piezas
				// rotadas
				// rotaM90BezierYHuecos();// Da orden de rotar incyute VBezier
				// setLugarDeInsercion(col);
				// res = true;
				// }
			} else {// hay columnas creadas previamente
				int tam = cols.size();
				boolean SIGO = true;
				for (int i = 0; SIGO && i < tam; i++) {
					col = cols.get(i);
					if (!col.estaEnTop()) { // No se esta en TOP
						if ((h.contieneDerechaAPiezaEnCol(this, i))) {
							res = true;
							SIGO = false;
							setLugarDeInsercion(col);
						}
						// else if ((h.contieneRotM90APiezaEnCol(this, i))) {// Cambio
						// res = true;
						// SIGO = false;
						// rotaM90();// Da orden de rotar
						// rotaM90BezierYHuecos();// Da orden de rotar incyute VBezier
						// setLugarDeInsercion(col);
						// }
						else if (h.contieneDerechaAPiezaAfueraDeCol(this, i)) { // Pieza cabe en nueva olumna
							col.setEstaEnTop(true);// se conjela el anchoMax de columna
							h.creaNuevaColumnaH();// se coloca anchoMax=0,alto=0 ,NO esta enTOP
							int ind = h.getMyColumnasH().size() - 1;// Obtiene nuevo lugar de ultima columna
							setLugarDeInsercion(h.getMyColumnasH().get(ind));// entrega esta columna para insertarse
							res = true;
							SIGO = false;
						}

					} else { // Se esta en TOP //utiliza anchoMax de columna para chequear si pieza cabe
						if (h.contieneDerechaAPiezaEnTopDeCol(this, col)) {
							setLugarDeInsercion(col);
							res = true;
							SIGO = false;
						} else if (h.contieneRotM90APiezaEnTopDeCol(this, col)) {
							setLugarDeInsercion(col);
							res = true;
							rotaM90BezierYHuecos();// Da orden de rotar incyute VBezier
							// rotaM90();// Da la oreden de rotar a pieza //Internamente s cambiara
							// alto<->alto y huecos
							// seran rotados y //desplazados
							SIGO = false;
						}
					}
				} // for
			} // else
		}
		return res;

	}

	/**
	 * Esta rutina revisa si: this cabe en los huecos de otra pieza Llama a
	 * cabeEnHueco Sale al encontrar el primer hueco de la lista donde cabe
	 * 
	 * @param pz
	 * @return
	 */
	public boolean cabeEn(PiezaH pz) {
		boolean res = false;
		if (pz.isTieneHuecosInternos()) {
			int tam = pz.getMyListaDeHuecos().size();
			boolean SIGO = true;
			int j = 0;
			while (SIGO && j < tam) {
				Hueco h = pz.getMyListaDeHuecos().get(j);
				if (this.cabeEn(h)) {
					res = true;
					SIGO = false;
				}
				j++;
				if (j == tam)
					SIGO = false;
			}
		}
		return res;
	}

	private void setLugarDeInsercion(ColumnaH col) {
		lugarDeInsercion = col;

	}

	public ColumnaH getLugarDeInsercion() {
		// TODO Auto-generated method stub
		return lugarDeInsercion;
	}

	/**
	 * Entrega la coordenada X total de pieza c/r a la pieza inicial donde quedo
	 * insertada.
	 * 
	 * @return
	 */
	public double getCoordenadaXPTotal() {
		if (DEBUG2)
			System.out.println("Soy pieza: " + getIndice());

		double res = getPosXP();
		if (DEBUG2)
			System.out.println("Posicion xP c/e origen de pieza precedente " + res);
		boolean estaI = isEstaInsertada();
		if (DEBUG2)
			System.out.println("Estoy insertada: " + isEstaInsertada());
		PiezaH pz = this;

		while (estaI) {
			PiezaH pzIn = pz.getEstoyInsertadaEn();
			if (DEBUG2)
				System.out.println("Estoy insertada en: " + pzIn.getIndice());
			res = res + pzIn.getPosXP();// Posicion c/r a pieza donde directamente esta insertada
			if (DEBUG2)
				System.out.println("Posicion acumulada c/e origen de pieza precedente " + res);
			estaI = pzIn.isEstaInsertada();// Cheque que esta insertada en alguna pieza
			if (DEBUG2)
				System.out.println("Esta insertada pz precedente: " + estaI);
			pz = pzIn;
		}
		setPosXP0(res);// Coloca coordenada total relativa a pieza cero en variable XP0
		if (DEBUG2)
			System.out.println("Posicion acumulada c/e origen de piezas precedente: " + getPosXP0());
		return res;

	}

	/**
	 * Calcula coordenadas X e Y totales con respecto a pieza0
	 */
	public void calculaYGuardaCoordenadasXeYTotalesCrAPieza0() {
		getCoordenadaXPTotal();
		getCoordenadaYPTotal();
	}

	public ArrayList<Double> getMyDistanciasCrOPiezas() {
		return myDistanciasCrOPiezas;
	}

	public void setMyDistanciasCrOPiezas(ArrayList<Double> myDistanciasCrOPiezas) {
		this.myDistanciasCrOPiezas = myDistanciasCrOPiezas;
	}

	/**
	 * Entrega la coordenada Y total de pieza c/r a la pieza inicial donde quedo
	 * insertada.
	 * 
	 * @return
	 */
	public double getCoordenadaYPTotal() {
		double res = getPosYP();
		boolean estaI = isEstaInsertada();
		PiezaH pz = this;

		while (estaI) {
			PiezaH pzIn = pz.getEstoyInsertadaEn();
			res = res + pzIn.getPosYP();// Posicion c/r a pieza donde directamente esta insertada
			estaI = pzIn.isEstaInsertada();// Cheque que esta insertada en alguna pieza
			pz = pzIn;
		}
		setPosYP0(res);// Coloca coordenada total relativa a pieza cero en variable YP0
		return res;
	}

	/**
	 * 
	 * SOLO USAR EN HUECO DE CORREA!! Se usa para equilibrar el resultado en el
	 * acercamiento de las piezas ya que las piezas pequenas tiene tendencia a
	 * acumularse en los TOPS de las columnas Si no se hace se genera hueco total en
	 * la region inferior derecha del hueco de correa con un perfil inclinado, que
	 * aumenta el largo sobre el eje X ocupado por las columnas. Crea lista de
	 * columnas con top alternado Este algoritmo debe hacerse solo para pieza cezo
	 * antes de proceder al acercamiento. Inmediatamente despues de hacerlo es
	 * necesario reajustar posiciones de piezas en la pieza para todas las piezas
	 * .Obtener los nuevos XP YP Tambien hay que recalcular los XP0 y YP0 ppara
	 * todas las piezas. Solo despues se procede a acercar las columnas
	 * 
	 * @param hcorrea =hueco de pieza0
	 *****/
	public void alternaTopDeColumnasH(Hueco hcorrea) {
		// System.out.println("Estoy dentro de alternaTopDeColumnas pz id: " +
		// getIndice() + " hueco Ancho: "
		// + hcorrea.getDx() + " hueco Alto: " + hcorrea.getDy());
		double lim = hcorrea.getDy();
		// System.out.println("lim: " + lim);
		ArrayList<ColumnaH> cols = hcorrea.getMyColumnasH();
		if (cols != null) {
			ColumnaH col = null;

			ArrayList<PiezaH> piezas = null;
			int noPiezas = 0;
			int tam = cols.size();
			// Utilidades.aviso(true, "No de columnas: " + tam);
			for (int i = 1; i < tam; i = i + 2) {
				// Solo columnas intercaladas desde la 2 hacia adelante
				col = cols.get(i);
				piezas = col.getArrayDeColunna();
				noPiezas = piezas.size();
				// Utilidades.aviso(true, "col: id: " + i + " no piezas: " + noPiezas);
				double dy = 0;
				for (int j = 0; j < noPiezas; j++) {
					dy = dy + piezas.get(j).getAlto();
				} // dy es alto total de columna
					// Utilidades.aviso(true, "col: alto: " + dy);
				double dypar = 0;// dypar es alto parcial de columna
				for (int j = 0; j < noPiezas; j++) {
					// System.out.println("Antes pz: " + j + " pos y: " + piezas.get(j).getPosY());
					dypar = dypar + piezas.get(j).getAlto();
					piezas.get(j).setPosY(dy - dypar);
					// System.out.println("despues pz: " + j + " pos y: " +
					// piezas.get(j).getPosY());
				} // Trabaja con coluna truncada en top
				double top = lim - dy;// top de la columna
				double ypos = 0.;
				for (int j = 0; j < noPiezas; j++) {
					ypos = piezas.get(j).getPosY();
					piezas.get(j).setPosY(ypos + top);// corrimiento hacia extremo superior
					// de columna
				}
			}
		}
	}

	public void alternaTopsDeTodosLosHuecos() {
		if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {
			for (Hueco h : myListaDeHuecos) {
				alternaTopDeColumnasH(h);
			}
		}
	}

	/**
	 * Rutina que cambia la posicion de todas las piezas dentro de un hueco ,que
	 * esta abierto al lado izquierdo de la Pieza, de modo que estas esten lo mas
	 * insertadas dentro de la pieza que es posible Los huecos abiertos al lado
	 * izquierdo se caracterizan por tener coordenada de origen X=0 Esta rutina hay
	 * que llamarla antes de calculular posiciones absolutas de piezas
	 */
	public void corrigePosicionDeColumnasIzquierdasEnHuecoAbiertos(Hueco h) {
		if (h != null && h.getX0() == 0.) {// Hueco abierto al lado izquierdo
			ArrayList<ColumnaH> cols = h.getMyColumnasH();
			double ancho = 0.;
			double anchoHueco = 0.;
			double dif = 0.;
			anchoHueco = h.getDx();
			// Utilidades.aviso(true, "anchoHueco: " + anchoHueco);
			if (cols != null && !cols.isEmpty()) {
				// Utilidades.aviso(true, "No de Cols: " + cols.size());
				for (ColumnaH col : cols) {
					ancho = ancho + col.getAnchoMaxCol();
				}
				// Utilidades.aviso(true, "anchoDeCos: " + ancho);
				dif = anchoHueco - ancho;
				// Utilidades.aviso(true, "dif: " + dif);
				for (ColumnaH col : cols) {
					if (col != null) {
						for (PiezaH pz : col.getArrayDeColunna()) {
							if (pz != null)
								pz.setPosX(dif + pz.getPosX());
						}
					}
				}

			}
		}
	}

	public void corrigePosicionDeColumnasIzquierdasEnHuecoAbiertosDeTodosLosHuecos() {
		if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {
			for (Hueco h : myListaDeHuecos) {
				corrigePosicionDeColumnasIzquierdasEnHuecoAbiertos(h);
			}
		}
	}

	public RectanguloH getRect0() {
		return rect0;
	}

	public void setRect0(RectanguloH rect0) {
		this.rect0 = rect0;
	}

	public RectanguloH getRectEjeX() {
		return rectEjeX;
	}

	public void setRectEjeX(RectanguloH rectEjeX) {
		this.rectEjeX = rectEjeX;
	}

	public RectanguloH getRectCols() {
		return rectCols;
	}

	public void setRectCols(RectanguloH rectCols) {
		this.rectCols = rectCols;
	}

	public RectanguloH getRectF() {
		return rectF;
	}

	public void setRectF(RectanguloH rectF) {
		this.rectF = rectF;
	}

	/**
	 * 
	 * Rutina que corrige huecos izquierdos en pieza
	 */
	public void corrigePosicionDeColumnasIzquierdasEnHuecoAbiertosDePieza() {

		ArrayList<Hueco> hs = getMyListaDeHuecos();
		if (hs != null && !hs.isEmpty()) {
			for (Hueco h : hs)
				corrigePosicionDeColumnasIzquierdasEnHuecoAbiertos(h);
		}
	}

	public void muestraEnConsolaArbolDeInsercionDePieza(PiezaH pz0) {
		PiezaH pz = pz0;
		while (pz.isEstaInsertada()) {
			PiezaH pzIn = pz.getEstoyInsertadaEn();
			System.out.println("Pieza id: " + pz.getIndice() + " esta insertada en: " + pzIn.getIndice());
			pz = pzIn;
		}
		return;
	}

	public void muestraEnConsolaPiezasEnHuecos() {
		if (noDePiezasInsertadas > 0) {
			if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {
				int i = 0;
				for (Hueco h : myListaDeHuecos) {
					System.out.println("idHueco: " + i);
					h.muestraEnConsolaPiezasDelHueco();
					i++;
				}
			}
		}
	}

	public void ajustaCoordenadasDePiezasConRespectoAOrigenDeTodosHuecosEnPieza() {
		if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {
			int i = 0;
			for (Hueco h : myListaDeHuecos) {
				h.ajustaCoordenadasDePiezasConRespectoAOrigenDelHueco();
			}
		}
	}

	public void ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDeTodosLosHuecos() {
		if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {

			for (Hueco h : myListaDeHuecos) {
				h.ajustaCoordenadasDePiezasConRespectoAOrigenDelHueco();
			}
		}
	}

	public void ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDePiezaX() {
		ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDeTodosLosHuecos();
		alternaTopsDeTodosLosHuecos();
		if (getIndice() != 0)
			corrigePosicionDeColumnasIzquierdasEnHuecoAbiertosDeTodosLosHuecos();
		ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDePieza();
	}

	/**
	 * Esta rutina se llama DESPUES de ordenar defitivamente las coordenadas de las
	 * piezas de todos los huecos usando la rutina
	 * ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDeTosoLosHuecos y de
	 * spues de cualquier alternacion de TOPS u corrimiento de columnas en huecos.
	 */
	public void ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDePieza() {
		// ajustaCoordenadasDePiezasContenidasConRespectoAOrigenDeTodosLosHuecos();
		if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {
			int i = 0;
			ArrayList<ColumnaH> cols = null;
			for (Hueco h : myListaDeHuecos) {
				// h.ajustaCoordenadasDePiezasConRespectoAOrigenDelHueco();// Piezas ordenadas
				// c/r a hueco
				cols = h.getMyColumnasH();
				if (cols != null && !cols.isEmpty()) {
					for (ColumnaH col : cols) {
						if (col != null && !col.isEmpty()) {
							for (PiezaH pz : col.getArrayDeColunna()) {
								pz.setPosXP(pz.getPosX() + h.getX0());// agrega coordenada de hueco a
								pz.setPosYP(pz.getPosY() + h.getY0());// coordenada c/r a hueco
								if (MUESTRA) {
									System.out.println("id Pieza :" + pz.getIndice() + " contenida en: "
											+ pz.getEstoyInsertadaEn().getIndice() + " tiene coordenadas  xP: "
											+ pz.getPosXP() + " yP: " + pz.getPosYP());
								}

							}
						}
					}
				}
			}
		}
	}

	public void muestraEnConsolaCoordenadasDePiezasContenidas() {
		System.out.println("====================================");
		System.out.println("Pieza id: " + getIndice() + " contiene: ");
		System.out.println("====================================");
		if (noDePiezasInsertadas > 0) {
			if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {
				ArrayList<ColumnaH> cols = null;
				for (Hueco h : myListaDeHuecos) {
					cols = h.getMyColumnasH();
					if (cols != null && !cols.isEmpty()) {
						for (ColumnaH col : cols) {
							if (col != null && !col.isEmpty()) {
								for (PiezaH pz : col.getArrayDeColunna()) {
									System.out.println("id pieza: " + pz.getIndice() + " X: " + pz.getPosX() + " Y: "
											+ pz.getPosY());
								}
							}
						}
					}
				}
			}
		}
		System.out.println("====================================");
	}

	public void muestraEnConsolaCoordendasDePiezaConRespectoAPiezaQueLaContiene() {
		String frase = String.format("id:\t%d\tposXP:\t%f\tposYP:\t%f\tancho:\t%f\talto:\t%f", indice, posXP, posYP,
				ancho, alto);
		System.out.println(frase);
	}

	public void muestraEnConsolaCoordenadasDeTodasLasPiezasContenidasConRespectoAPieza() {
		if (noDePiezasInsertadas > 0) {
			System.out.println("-------------------------------------------------");
			if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {
				if (getNoDePiezasInsertadas() > 0) {
					int j = 0;
					for (Hueco h : myListaDeHuecos) {
						System.out.println("id hueco: " + j + " :Origen: (" + h.getX0() + " , " + h.getY0() + ")");
						h.muestraEnConsolaCoordenadasDePiezasDelHuecoCrAPiezaQueLasContiene();
						j++;
					}
				}
			}
		}
	}

	public void muestraEnConsolaAltoTotalDeTodasLasColumnasContenidasEnPieza() {

		if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {

			for (Hueco h : myListaDeHuecos) {
				h.muestraEnConsolaAltoTotalDeColumnasEnHueco();
			}
		}
	}

	public void muestraEnConsolaCoordenadasDeTodasPiezasCRHuecosDePieza() {

		if (noDePiezasInsertadas > 0) {
			if (myListaDeHuecos != null && !myListaDeHuecos.isEmpty()) {

				for (Hueco h : myListaDeHuecos) {
					h.muestraEnConsolaCoordenadasDePiezasDelHuecoCrAHueco();
				}
			}
		}
	}

	/**
	 * Rutina que almacena las piezas directamente almacenadas en pieza0 y ademas,
	 * almacena las piezas insertadas en cada una de las piezas insertadas en pieza0
	 * (piezas insertadas en pieza0=piezas de nivel 1) (piezas insertadas en piezas
	 * de nivel 1=piezas de nivel 2) Las piezas de nivel 2 o mas NO tienen piezas
	 * insertadas totales. Esta rutina permite MOVER como bloque todas las piezas
	 * que estan directamente insertadas en pieza0, las cuales pueden contiener
	 * ellas mismas piezas insertadas.GENERA:MyPiezasHInsertadasTotal UTIL EN
	 * ACERCAMIENTO DE PIEZAS
	 */
	public void generaTodasLasPiezasContenidasEnArbolHastanivel0y1() {

		if (getIndice() != 0) {// Solo las piezas distinas de cero estan insertadas en alguna
			PiezaH pz = null, pzFinal = null;
			pzFinal = getEstoyInsertadaEn();// Falla para pieza cero
			if (pzFinal == null) {
				System.out.println("pz id: " + getIndice()
						+ " PiezaH:generaTodasLasPiezasContenidasEnArbolHastanivel0y1:pzFinal es null");
				System.exit(0);
			}
			if (pzFinal.getIndice() == 0) {// Valido para piezas insertdas en pieza0
				pzFinal.getMyPiezasHInsertadasTotal().add(this);// pzFinal es pieza cero
				// Si pieza anterior a : this es Pieza cero, se almacena en cero: this
			} else {// Pieza anterior a : this no es pieza0
				while (pzFinal.getIndice() != 0) {// Acumula piezas en ramas del
													// arbol hasta llegar a
													// pieza0 antrior a pieza0
					pz = pzFinal;
					pzFinal = pzFinal.getEstoyInsertadaEn();
				}
				pz.getMyPiezasHInsertadasTotal().add(this);// Guarda en pieza anterior a pieza0
				// pz es pieza anterior a pieza0
			}
			// pz.generaMyPiezasHInsertadasTotalI();// Se agega a si mismo
		}
	}

	public void muestraEnConsolaArbolTotalDePiezEnNivel1(PiezaH pz) {
		if (pz.isEstaInsertada() && (pz.getEstoyInsertadaEn().getIndice() == 0)) {
			System.out.println("Piezas insertadas en arbol de: " + pz.getIndice());
			String frase = "";
			if (pz.getMyPiezasHInsertadasTotalI() != null)
				for (PiezaH pzr : pz.getMyPiezasHInsertadasTotalI()) {
					frase = frase + String.format("%d ", pzr.getIndice());
				}
			System.out.println(frase);
		} else {
			// System.out.println("Piezas : "+pz.getIndice()+" no es de nivel 1");
		}
	}

	public void generaMyPiezasHInsertadasTotalI() {
		if (getIndice() != 0)
			if (getEstoyInsertadaEn().getIndice() == 0) {
				if (myPiezasHInsertadasTotalI == null)
					myPiezasHInsertadasTotalI = new ArrayList<PiezaH>();
				myPiezasHInsertadasTotalI.add(this);
				if (myPiezasHInsertadasTotal != null && !myPiezasHInsertadasTotal.isEmpty()) {
					for (PiezaH pz : myPiezasHInsertadasTotal)
						myPiezasHInsertadasTotalI.add(pz);
				}
			}
	}

	public void muestraEnConsolaPiezasDelArbolANivel1ConDistanciasAPiezaPrecedente() {
		System.out.println("-----------------------------------");
		if (myPiezasHInsertadasTotalI != null && !myPiezasHInsertadasTotalI.isEmpty()) {
			for (PiezaH pz : myPiezasHInsertadasTotalI) {
				System.out.println(
						"id: " + pz.getIndice() + " estoy Insertada en: " + pz.getEstoyInsertadaEn().getIndice()
								+ " con coorimiento (X,y): (" + pz.getPosXP() + "," + pz.getPosYP() + ")");
			}
		}
		System.out.println("-----------------------------------");
	}

	public void calculaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0() {
		if (getIndice() != 0 && getEstoyInsertadaEn().getIndice() == 0) {
			if (myPiezasHInsertadasTotalI != null && !myPiezasHInsertadasTotalI.isEmpty()) {
				for (PiezaH pz : myPiezasHInsertadasTotalI) {
					double resX = 0.;
					double resY = 0.;
					PiezaH pzx = pz;

					while (pzx.getIndice() != 0) {
						// System.out.println("dentro de while id pz:" + pzx.getIndice());
						resX = resX + pzx.getPosXP();
						resY = resY + pzx.getPosYP();
						pzx = pzx.getEstoyInsertadaEn();

					}

					pz.setPosXP0(resX);
					pz.setPosYP0(resY);

				}
			}
		}

	}

	public void muestraDondeEstaDirectamenteInsertada() {
		if (isEstaInsertada()) {
			System.out.println(
					"id Pz: " + getIndice() + " estoy insertada directamente en:" + getEstoyInsertadaEn().getIndice());
		}
	}

	public void muestraEnConsolaDistanciaAbsolutaDePiezasDeArbolANivel1CrAPieza0() {
		// System.out.println("&&&&&&&&&&&&&&&&&&");
		if (getIndice() != 0 && getEstoyInsertadaEn().getIndice() == 0) {
			System.out.println("-----------------------------------");
			if (myPiezasHInsertadasTotalI != null && !myPiezasHInsertadasTotalI.isEmpty()) {
				for (PiezaH pz : myPiezasHInsertadasTotalI) {
					System.out.println(
							"id Pieza: " + pz.getIndice() + " xP0: " + pz.getPosXP0() + " yP0: " + pz.getPosYP0());
				}
			}
		}
		// System.out.println("&&&&&&&&&&&&&&&&&");
	}

	public void muestraEnConsolaPiezasInsertadas() {
		System.out.println("-----------------------------------");
		System.out.println("Piezas Insertadas en: " + getIndice());
		System.out.println("-----------------------------------");
		if (getMyPiezasHInsertadas() != null) {
			for (PiezaH pz : getMyPiezasHInsertadas()) {
				System.out.format("%d ", pz.getIndice());
			}
			System.out.format("%n");
		}
		System.out.println("-----------------------------------");
	}

	public void muestraEnConsolaPiezasInsertadasTotalI() {
		if (isEstaInsertada() && (getEstoyInsertadaEn().getIndice() == 0)) {
			System.out.println("-----------------------------------");
			System.out.println("Piezas Insertadas Totales en nivel 1: " + getIndice());
			System.out.println("-----------------------------------");
			if (getMyPiezasHInsertadasTotalI() != null) {
				for (PiezaH pz : getMyPiezasHInsertadasTotalI()) {
					System.out.format("%d ", pz.getIndice());
				}
				System.out.format("%n");
			}
			System.out.println("-----------------------------------");
		}
	}

	/**
	 * Entrega informacion sobre las piezas que estan insertadas en cada pieza que
	 * esta insertada en pieza0
	 */
	public void muestraEnConsolaPiezasInsertadasEnPiezasInsertadasEnPieza0() {
		if (indice != 0) {
			if (getEstoyInsertadaEn() != null && getEstoyInsertadaEn().getIndice() != 0) {
				System.out.println("id: " + getIndice() + " estoy insertada en: " + getEstoyInsertadaEn().getIndice()
						+ " y tengo insertadas: " + getMyPiezasHInsertadas().size() + " piezas");
				if (getMyPiezasHInsertadas() != null && getMyPiezasHInsertadas().size() > 0) {
					ArrayList<PiezaH> array = getMyPiezasHInsertadas();
					for (PiezaH pz : array) {
						System.out.format("%d ", pz.getIndice());
					}
					System.out.format("%n");
				}
			}
		}
	}

	public void revisaNivelDePieza() {
		if (getIndice() != 0 && getEstoyInsertadaEn().getIndice() == 0) {
			nivel = 1;
			setNivel1(true);
		} else {
			nivel = 2;
			setNivel1(false);
		}
		if (getIndice() == 0)
			nivel = 0;

	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public void muestraTodasLasPiezasContenidasEnArbolHastaNivel0y1() {
		String frase = null;
		String frase1 = " . Es pieza de nivel >1";
		revisaNivelDePieza();
		frase = String.format("Pieza id: %d nivel: %d ccontiene a: %d  piezas insertadas directas, y %d a nivel 1,",
				getIndice(), getNivel(), getMyPiezasHInsertadas().size(), getMyPiezasHInsertadasTotal().size());
		if (getIndice() != 0) {
			frase = frase + String.format("y esta insertado en : %d ", getEstoyInsertadaEn().getIndice());
			if (getEstoyInsertadaEn().getIndice() > 0 && getMyPiezasHInsertadasTotal().size() == 0)
				frase = frase + frase1;
		}
		System.out.println(frase);

		for (PiezaH pz : getMyPiezasHInsertadasTotal()) {
			System.out.format("%d ", pz.getIndice());
		}
		System.out.format("%n");
		System.out.println("--------------------------------------");
	}

	public ArrayList<PiezaH> getMyPiezasHInsertadasTotal() {
		return myPiezasHInsertadasTotal;
	}

	public void setMyPiezasHInsertadasTotal(ArrayList<PiezaH> myPiezasHInsertadasTotal) {
		this.myPiezasHInsertadasTotal = myPiezasHInsertadasTotal;
	}

	public ArrayList<PiezaH> getMyPiezasHInsertadasTotalI() {
		// TODO Auto-generated method stub
		return myPiezasHInsertadasTotalI;
	}

	/*
	 * Rutina que crea una lista de las Curvas de Bezier NORMALIZADAS a partir de un
	 * archivo Las curvas se guardan en : pathList
	 * 
	 * @param nombre_svgP nombre del archivo dentro del directorio:
	 * ./objetos/normal_svg/
	 */
	public void obtiene_curvas_de_bezier_normal_de_pieza(String nombre_svgP) {// nombre_svg es el nombre del
																				// archivo en
																				// directorio : ./objetos/normal_svg
		nombre = nombre_svgP;

		String directorio_normal = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "normal_svg");

		String nombre_svg = directorio_normal + File.separator + nombre_svgP;
		File file = new File(nombre_svg);
		if (!file.exists() || !file.isFile()) {
			System.out.println("No pude abrir archivo. " + nombre_svg + " Salgo...");
			System.exit(0);
		}

		Scanner sc = null;
		try {
			sc = new Scanner(file);

			int vis = 0;
			pathList = null;
			char tipo = '=';
			double X1 = 0.;
			double Y1 = 0.;
			double X2 = 0.;
			double Y2 = 0.;
			double X3 = 0.;
			double Y3 = 0.;
			double X4 = 0.;
			double Y4 = 0.;
			LinkedList<EleCalculo> eleCalList = null;

			int ipath = 0;
			while (sc.hasNext()) {
				if (sc.hasNext()) {
					vis = sc.nextInt();
				}
				if (sc.hasNext()) {
					tipo = (sc.next()).charAt(0);
				}
				if (sc.hasNext()) {
					X1 = (sc.nextDouble());
				}
				if (sc.hasNext()) {
					Y1 = (sc.nextDouble());
				}
				if (sc.hasNext()) {
					X2 = (sc.nextDouble());
				}
				if (sc.hasNext()) {
					Y2 = (sc.nextDouble());
				}
				if (sc.hasNext()) {
					X3 = (sc.nextDouble());
				}
				if (sc.hasNext()) {
					Y3 = (sc.nextDouble());
				}
				if (sc.hasNext()) {
					X4 = (sc.nextDouble());
				}
				if (sc.hasNext()) {
					Y4 = (sc.nextDouble());
				}

				if (tipo != 'X' && tipo != 'x' && tipo != '$') {
					if (eleCalList == null)
						eleCalList = new LinkedList<EleCalculo>();
					EleCalculo elex = new EleCalculo(tipo, X1, Y1, X2, Y2, X3, Y3, X4, Y4);
					eleCalList.add(elex);
				}
				if (tipo == 'X' || tipo == 'x') {
					if (pathList == null)
						pathList = new LinkedList<Path>();// lista de paths
					Path path = new Path(ipath, vis, eleCalList);// guarda path con visibilidad
					pathList.add(path);// agrega path
					ipath++;
					eleCalList = null;// comienza otro path.
				}

			}

			sc.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Utilidades.escribe("No pude abrir scanner");
			Utilidades.salgo();
			// e.printStackTrace();
		}

		return;
	}

	public void dibuja_pieza(Graphics g, int presicion, RectanguloH rech, int escalax, int escalay, int xOrigen,
			int yOrigen) {
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
		int no_paths = getPathList().size();
		// System.out.println("id: " + getIndice() + " rech.posX: " + rech.getPosX() + "
		// ancho: " + getAncho());
		for (int i = 0; i < no_paths; i++) {
			pathX = getPathList().get(i);
			if (pathX.flag_vis == 1) {
				for (int j = 0; pathX.path != null && j < pathX.path.size(); j++) {
					elx = pathX.path.get(j);

					P1.x = elx.xP1 * rech.getDX() * escalax + rech.getPosX() * escalax + xOrigen;
					P1.y = elx.yP1 * rech.getDY() * escalay + rech.getPosY() * escalay + yOrigen;
					P2.x = elx.xP2 * rech.getDX() * escalax + rech.getPosX() * escalax + xOrigen;
					P2.y = elx.yP2 * rech.getDY() * escalay + rech.getPosY() * escalay + yOrigen;
					P3.x = elx.xP3 * rech.getDX() * escalax + rech.getPosX() * escalax + xOrigen;
					P3.y = elx.yP3 * rech.getDY() * escalay + rech.getPosY() * escalay + yOrigen;
					P4.x = elx.xP4 * rech.getDX() * escalax + rech.getPosX() * escalax + xOrigen;
					P4.y = elx.yP4 * rech.getDY() * escalay + rech.getPosY() * escalay + yOrigen;
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
							g.drawLine((int) res_prevG.x, (int) res_prevG.y, (int) resG.x, (int) resG.y);
							// g.drawLine((int) (ANCHO_WIN * res_prevG.x), (int) (ALTO_WIN * res_prevG.y),
							// (int) (ANCHO_WIN * resG.x), (int) (ALTO_WIN * resG.y));
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

	/**
	 * Esta rutina determina si hay cruce en las curvas deBezier visibles de dos
	 * piezas. Utiliea las coordenadas absolutas de las piezas c/r al la pieza0
	 * 
	 * @param pz1       piezaH 1
	 * @param pz2       piezaH 2
	 * @param presicion :no de puntos de interpolacion por cada curva de Bezier
	 * @param epsilon   : no. real que se usa para introducir perturbacion a
	 *                  pendientes de lineas rectas en la rutina de determinacion de
	 *                  cruce. Evita casos de curbas con pendiente infinita o curbas
	 *                  paralelas. Es un numero muy pequeno menor que 1.
	 * @return true si hay cruce , false si no lo hay
	 */
	public static boolean determinaSiHayCruceDePiezasDesdeRectangulos(PiezaH pz1, PiezaH pz2, int presicion,
			double epsilon) {
		Point P11, P12, P13, P14, P21, P22, P23, P24, PresG1, PresG2, PresG1prev, PresG2prev;
		EleCalculo elx1, elx2;
		Path pathX1, pathX2;
		P11 = new Point();
		P12 = new Point();
		P13 = new Point();
		P14 = new Point();
		P21 = new Point();
		P22 = new Point();
		P23 = new Point();
		P24 = new Point();
		PresG1 = new Point();
		PresG2 = new Point();
		PresG1prev = new Point();
		PresG2prev = new Point();
		double y11 = 0, y12 = 0, x11 = 0, x12 = 0, y21 = 0, y22 = 0, x21 = 0, x22 = 0;
		double distanciam = 0., distancia = 0.;
		int no_datos = 0;
		boolean esPrimero1 = true;
		boolean esPrimero2 = true;
		boolean res = false;
		// Point p1=NULL,p2=NULL,p3=NULL,p4=NULL;
		// p1= MALLOC(point);p2= MALLOC(point);p3= MALLOC(point);p4= MALLOC(point);
		// Point res=MALLOC(point);Point res_prev=MALLOC(point);
		boolean esPrimero = true;
		double tx1, tx2, del = 1. / presicion, valorPar;

		LinkedList<cortador.Path> pathListx1 = pz1.pathList;
		LinkedList<cortador.Path> pathListx2 = pz2.pathList;

		int no_paths1 = pathListx1.size();
		for (int i1 = 0; !res && i1 < no_paths1; i1++) {
			pathX1 = pathListx1.get(i1);
			// System.out.println("i1: "+i1);
			if (pathX1.flag_vis == 1) {
				esPrimero1 = true;
				for (int j1 = 0; !res && j1 < pathX1.path.size(); j1++) {
					elx1 = pathX1.path.get(j1);

					P11.x = (double) (elx1.xP1 * pz1.getAncho() + pz1.getPosXP0());
					P11.y = (double) (elx1.yP1 * pz1.getAlto() + pz1.getPosYP0());
					P12.x = (double) (elx1.xP2 * pz1.getAncho() + pz1.getPosXP0());
					P12.y = (double) (elx1.yP2 * pz1.getAlto() + pz1.getPosYP0());

					P13.x = (double) (elx1.xP3 * pz1.getAncho() + pz1.getPosXP0());
					P13.y = (double) (elx1.yP3 * pz1.getAlto() + pz1.getPosYP0());
					P14.x = (double) (elx1.xP4 * pz1.getAncho() + pz1.getPosXP0());
					P14.y = (double) (elx1.yP4 * pz1.getAlto() + pz1.getPosYP0());

					// System.out.format("pz1: %f %f %f %f %f %f %f
					// %f%n",P11.x,P11.y,P12.x,P12.y,P13.x,P13.y,P14.x,P14.y);

					// System.out.format("%f %f %f %f %f %f %f
					// %f%n",P1.x,P1.y,P2.x,P2.y,P3.x,P3.y,P4.x,P4.y);
					// System.out.println("j1: "+j1);
					for (int k1 = 0; !res && k1 < presicion + 1; k1++) {
						tx1 = del * k1;
						if (elx1.t == 'c' || elx1.t == 'C')
							PresG1 = Bezier.vectorBezierC(tx1, PresG1, P11, P12, P13, P14);
						if (elx1.t == 'q' || elx1.t == 'Q')
							PresG1 = Bezier.vectorBezierQ(tx1, PresG1, P11, P12, P13);
						if (elx1.t == 'L' || elx1.t == 'l')
							PresG1 = Bezier.vectorBezierL(tx1, PresG1, P11, P12);
						// System.out.println("k1: "+k1);
						// System.out.format("pz1:k1 %d %f %f %n",k1,PresG1.x,PresG1.y);

						if (!esPrimero1) {
							// dibuja linea entre puntos res y res_pr
							y11 = PresG1prev.y;
							y12 = PresG1.y;
							x11 = PresG1prev.x;
							x12 = PresG1.x;
							// g.drawLine((int) (ANCHO_WIN * res_prevG.x), (int) (ALTO_WIN * res_prevG.y),
							// (int) (ANCHO_WIN * resG.x), (int) (ALTO_WIN * resG.y));
						}
						PresG1prev.x = PresG1.x;
						PresG1prev.y = PresG1.y;
						esPrimero1 = false;

						int no_paths2 = pathListx2.size();
						for (int i2 = 0; !res && i2 < no_paths2; i2++) {
							// System.out.println("i2: "+i2);
							pathX2 = pathListx2.get(i2);
							if (pathX2.flag_vis == 1) {
								esPrimero2 = true;
								for (int j2 = 0; !res && j2 < pathX2.path.size(); j2++) {
									// System.out.println("j2: "+j2);
									elx2 = pathX2.path.get(j2);
									// Obtiene posicion absoluta de curbas de Bezier con la esca guardad en rec2

									P21.x = (double) (elx2.xP1 * pz2.getAncho() + pz2.getPosXP0());
									P21.y = (double) (elx2.yP1 * pz2.getAlto() + pz2.getPosYP0());
									P22.x = (double) (elx2.xP2 * pz2.getAncho() + pz2.getPosXP0());
									P22.y = (double) (elx2.yP2 * pz2.getAlto() + pz2.getPosYP0());

									P23.x = (double) (elx2.xP3 * pz2.getAncho() + pz2.getPosXP0());
									P23.y = (double) (elx2.yP3 * pz2.getAlto() + pz2.getPosYP0());
									P24.x = (double) (elx2.xP4 * pz2.getAncho() + pz2.getPosXP0());
									P24.y = (double) (elx2.yP4 * pz2.getAlto() + pz2.getPosYP0());

									// System.out.format("pz2: %f %f %f %f %f %f %f
									// %f%n",P21.x,P21.y,P22.x,P22.y,P23.x,P23.y,P24.x,P24.y);

									for (int k2 = 0; !res && k2 < presicion + 1; k2++) {
										// System.out.println("k2: "+k2);
										tx2 = del * k2;
										if (elx2.t == 'c' || elx2.t == 'C')
											PresG2 = Bezier.vectorBezierC(tx2, PresG2, P21, P22, P23, P24);
										if (elx2.t == 'q' || elx2.t == 'Q')
											PresG2 = Bezier.vectorBezierQ(tx2, PresG2, P21, P22, P23);
										if (elx2.t == 'L' || elx2.t == 'l')
											PresG2 = Bezier.vectorBezierL(tx2, PresG2, P21, P22);
										// ayCruce(double y11, double y12, double x11, double x12, double y21, double
										// y22, double x21,
										// double x22, double eps)

										if (!esPrimero2) {
											// dibuja linea entre puntos res y res_pr
											y21 = PresG2prev.y;
											y22 = PresG2.y;
											x21 = PresG2prev.x;
											x22 = PresG2.x;
											// g.drawLine((int) (ANCHO_WIN * res_prevG.x), (int) (ALTO_WIN *
											// res_prevG.y),
											// (int) (ANCHO_WIN * resG.x), (int) (ALTO_WIN * resG.y));
										}
										PresG2prev.x = PresG2.x;
										PresG2prev.y = PresG2.y;

										if (!esPrimero1 && !esPrimero2) {
											boolean response = false;
											// Correccion de seguridad
											// response = hayCruceCompleto(y11,y12,x11, x12, y21, y22,
											// x21, x22, epsilon);
											for (int itest = 0; itest < 2; itest++) {

												res = hayCruceRan(y11, y12, x11, x12, y21, y22, x21, x22, epsilon);
												response = response || res;
											}
											res = response;
										}

										

									}
									esPrimero2 = false;

								}

							}
						}
					}

				}

			}

		}

		return res;

	}

	/**
	 * Rutina que permite saber si hay cruce entre dos segmentos de rectas
	 * 
	 * @param y11 coordenada y inicial de segmento 1
	 * @param y12 coordenada y final de segmento 1
	 * @param x11 coordenada x inicial de segmento 1
	 * @param x12 coordenada x final de segmento 1
	 * @param y21 coordenada y inicial de segmento 2
	 * @param y22 coordenada y final de segmento 2
	 * @param x21 coordenada x inicial de segmento 2
	 * @param x22 coordenada x final de segmento 2
	 * @param eps perturbacion a valores de coordenadas extremas para evitar casos
	 *            criticos
	 * @return true si hay cruce, false si no lo hay
	 */
	public static boolean hayCruceRan(double y11, double y12, double x11, double x12, double y21, double y22,
			double x21, double x22, double eps) {
		boolean res = false;
		double m1 = 0.;
		double m2 = 0.;
		double yc = 0.;
		double xc = 0.;
		double x11r, x12r, x21r, x22r, y11r, y12r, y21r, y22r;
		// caso de rectas "normales"
		// Agrega al azar un numero muy pequeno para "evitar" casos criticos
		//
		x11r = x11 + (Math.random() - 0.5) * eps;
		x12r = x12 + (Math.random() - 0.5) * eps;
		x21r = x21 + (Math.random() - 0.5) * eps;
		x22r = x22 + (Math.random() - 0.5) * eps;
		y11r = y11 + (Math.random() - 0.5) * eps;
		y12r = y12 + (Math.random() - 0.5) * eps;
		y21r = y21 + (Math.random() - 0.5) * eps;
		y22r = y22 + (Math.random() - 0.5) * eps;
		m2 = (y22r - y21r) / (x22r - x21r);
		m1 = (y12r - y11r) / (x12r - x11r);
		xc = (m1 * x11r - m2 * x21r) / (m1 - m2) + (y21r - y11r) / (m1 - m2);// punto de intercepcion
		if (((x11r <= xc) && (xc <= x12r) || (x12r <= xc) && (xc <= x11r))
				&& ((x21r <= xc) && (xc <= x22r) || (x22r <= xc) && (xc <= x21r))) {
			res = true;

			// System.out.println("caso de cruce normal");
		} else // caso de situaciones de puntos.
			res = false;

		return res;
	}

}
