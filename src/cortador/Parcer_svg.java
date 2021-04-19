package cortador;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

/* TRABAJA BIEN*//*PUEDE LEER PATHS DE GIMP*/
public class Parcer_svg {
	// static String directorio = "/home/ivan/work";
	static String directorio = DirectorioTrabajo.directorio;
	static boolean DEBUG = false;
	static boolean DEBUG0 = false;
	static boolean DEBUG2 = false;

	public Parcer_svg() {
	}

	public static void main(String[] args) {

		// genera elementos de calculo inclusive con signo
		// y une ultimo punto con primero con linea
		// hay multiple paths
		// cambio de criterio de correccion de numeros. Se hace c/r al primer punto
		// de la curva de bezier.
		// detecta si curva es visible o no
		// tambien sirve para path no cerrados
		// Recibe archivos generados por: simplex_svg, el cual emite
		// archivos
		// con la frase "simp_" agregada al comienzo de los archivos de input que vienen
		// de: inkscape
		// Usa secuencia <visibilidad><^> como indicador
		// Escribe archivos con la frase "bezier_" agregada al comienzo del archivo de
		// input.
		// Resumen:
		// inkscape:--> nombre.svg ----> :simplex_svg:--> simp_nombre.svg
		// simp_nombre.svg --> :parcer_svg: ---> bezier_simp_nombre.svg

		// System.out.println("No. de argumentos: " + args.length);
		// String directorio = "/home/ivan/work";
		String nombre_svgP = null;
		// int noNo = 0, flag_vis = 0;
		// int i = 0, inum = 0, ifrac = 0, flagH = 0, flagV = 0, flag_n = 0, flag_s = 0,
		// flag_d = 0, flag_finCal = 0,
		// fin_X = 0, fin_Y = 0;
		// double suma = 0., frac = 1., z = 0., xPoint = 0.;
		// LinkedList<PointYLetra> pList = null;
		// PointYLetra p1 = null;
		if (args.length > 1)
			uso();

		if (args.length == 1) {
			nombre_svgP = args[0];

		} else {
			// nom_archivo="simp_ws_drawing.svg";
			escribe("Usando default: simp_ws_drawing.svg ");
			nombre_svgP = "simp_ws_drawing.svg";
		}
		Parcer_svg parcer_svg = new Parcer_svg();

		// nombre_svgP = "simp_ws_ballenasinh.svg";
		System.out.println(nombre_svgP);
		aviso(nombre_svgP);
		nombre_svgP = parcer_svg.trabajo(nombre_svgP);
	}

	public String trabajo(String nombre_svgP) {

		int noNo = 0, flag_vis = 0;
		int i = 0, inum = 0, ifrac = 0, flagH = 0, flagV = 0, flagEND = 0, flagNor = 1, flag_n = 0, flag_s = 0,
				flag_d = 0, flag_finCal = 0, fin_X = 0, fin_Y = 0;
		double suma = 0., frac = 1., z = 0., xPoint = 0.;
		LinkedList<PointYLetra> pList = null;
		PointYLetra p1 = null;
		PointYLetra pYl = null;
		/*
		 * NOMBRE DE ARCHIVO DE ENTRADA
		 */

		// String directorio_simple= directorio+ File.separator+ "objetos"+
		// File.separator+ "simple_svg";
		// String nombre_svg = directorio_simple + File.separator + nombre_svgP;
		String directorio_inkscape = null;
		// DirectorioBase dbase = new DirectorioBase();
		// File directorio_inkscapeD = null;

		// final File directorio_normalD = new File(directorio_normal);
		directorio_inkscape = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "simple_svg");
		String nombre_svg = directorio_inkscape + File.separator + nombre_svgP;

		File file = new File(nombre_svg);
		if (!file.exists() || !file.isFile()) {
			System.out.println("No pude abrir archivo. " + nombre_svg + " Salgo...");
			System.exit(0);
		}

		DataInputStream data_in = null;
		try {
			data_in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			aviso("No pude generar Data stream");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		char car, carp = '=';
		int carIn = 0;
		// escribe("antes de entrar a recorrer archivo");
		// stop();
		try {
			while ((carIn = data_in.read()) != -1) {
				car = (char) carIn;
				// System.out.format("%c\n", car);

				if (SimbolosPermitidos.NO_ES_PERMITIDO(car)) {
					System.out.format("car: %c \n", car);
					escribe("Simbolo NO permitido..Solo:(m,M,c,C,q,Q,l,L,h,H,v,V). Salgo...%n");
					System.exit(0);
				}

				if (SimbolosPermitidos.ES_COMA(car)) {
					if (DEBUG0) {
						System.out.format("%c%n", car);
						stop();
					}

				}

				// COMIENZA PATH
				if (car == '^') {
					if (DEBUG0) {
						System.out.format("Encontre: %c%n", car);
						stop();
					}
				}

				if (car == '"') {
					if (DEBUG0) {
						System.out.format("Encontre: %c%n", car);
						stop();
					}
				}

				if ((carp == '1') && (car == '^')) {
					if (DEBUG0) {
						System.out.format("Path visible.Se detecto 1%n");
						stop();
					}
					flag_vis = 1;
				}
				if ((carp == '0') && (car == '^')) {
					if (DEBUG0) {
						System.out.format("Path invisible.Se detecto 0%n");
						stop();
					}
					flag_vis = 0;
				}

				if ((SimbolosPermitidos.ES_COMILLAS(carp)) && (car == 'm' || car == 'M')) {
					if (DEBUG0) {
						System.out.format("encontre \"m%n");
						stop();
					}
					i = 0;
					inum = 0;
					ifrac = 0;
					flagNor = 1;
					flagEND = 0;
					flagH = 0;
					flagV = 0;
					flag_n = 0;
					flag_s = 0;
					flag_d = 0;
					flag_finCal = 0;
					fin_X = 0;
					fin_Y = 0;
					suma = 0.;
					frac = 1.;
					carp = '=';
				}

				// TERMINA PATH
				if ((carp == 'Z' || carp == 'z') && (SimbolosPermitidos.ES_COMILLAS(car))) {
					if (DEBUG0) {
						System.out.format("termine path('Z''\"')%n");
						stop();
					}
				}
				if ((SimbolosPermitidos.ES_COMILLAS(car))) {
					if (DEBUG0) {
						System.out.format("encontre comillas('\"')%n");
						stop();
					}
				}

				if ((car == 'H' || car == 'h')) { // detecta si es elemento horizontal
					flagH = 1;
					flagV = 0;
					flagNor = 0;
					// printf("Encontre H o h\n");stop();
				}

				if ((car == 'V' || car == 'v')) { // detecta si es elemento vertical
					// printf("Encontre V o v\n");stop();
					flagV = 1;
					flagH = 0;
					flagNor = 0;
				}
				if (((SimbolosPermitidos.ES_MAYUSCULA(car)) || (SimbolosPermitidos.ES_MINUSCULA(car)))
						&& !(car == 'V' || car == 'v' || car == 'H' || car == 'h')) { // detecta si elemento es normal

					flagV = 0;
					flagH = 0;
					flagNor = 1;
					if (DEBUG0) {
						System.out.println("car: " + car + " flagNor=1");
						stop();
					}
				}
				if ((SimbolosPermitidos.ES_ESPACIO(carp)) && (SimbolosPermitidos.ES_DIGITO(car))) {
					// COMIENZA No X
					// printf("comienza X\n"); // printf("%c\n",car);
					if (DEBUG0) {
						System.out.format("Encontre <espacio><digito>%n");
						stop();
					}
					flag_s = 0;
					flag_n = 1;
					suma = 0.;
					inum = 0;
					flag_d = 0;
					fin_X = 0;
					fin_Y = 0;
				}

				if ((SimbolosPermitidos.ES_SIGNO(carp)) && (SimbolosPermitidos.ES_DIGITO(car))) {
					// COMIENZA No X
					if (DEBUG0) {
						System.out.format("Encontre <signo><digito>%n");
						stop();
					}

					flag_s = 1;
					flag_n = 1;
					suma = 0.;
					inum = 0;
					flag_d = 0;
					fin_X = 0;
					fin_Y = 0;
				}

				if ((SimbolosPermitidos.ES_DIGITO(carp)) && (SimbolosPermitidos.ES_COMA(car))) {
					// TERMINA No X
					if (DEBUG0) {
						System.out.format("Encontre <digito><coma>%n");
						stop();
					}
					flag_n = 0;
					flag_finCal = 1;
					fin_X = 1;
					fin_Y = 0;
				}
				if (SimbolosPermitidos.ES_DIGITO(carp) && (SimbolosPermitidos.ES_ESPACIO(car)
						|| SimbolosPermitidos.ES_NEWLINE(car) || SimbolosPermitidos.ES_COMILLAS(car)) && flagH == 1) {
					// TERMINA No X en casoH
					if (DEBUG0) {
						escribe("1*Encontre <digito><espacio>%n");
						stop();
					}
					flag_n = 0;
					flag_finCal = 1;
					fin_X = 1;
					fin_Y = 0;
				}
				if (SimbolosPermitidos.ES_DIGITO(carp) && (car == 'm' || car == 'M') && flagH == 1) {
					// TERMINA No X en casoH
					if (DEBUG0) {
						escribe("1*Encontre <digito><M>%n");
						stop();
					}
					flag_n = 0;
					flag_finCal = 1;
					fin_X = 1;
					fin_Y = 0;
					flagEND = 1;
				}

				if (SimbolosPermitidos.ES_DIGITO(carp) && (SimbolosPermitidos.ES_ESPACIO(car)
						|| SimbolosPermitidos.ES_NEWLINE(car) || SimbolosPermitidos.ES_COMILLAS(car)) && flagV == 1) {
					// TERMINA No Y en casoV
					if (DEBUG0) {
						escribe("2*Encontre <digito><espacio>%n");
						stop();
					}
					flag_n = 0;
					flag_finCal = 1;
					fin_X = 0;
					fin_Y = 1;
				}

				if (SimbolosPermitidos.ES_DIGITO(carp) && (car == 'm' || car == 'M') && flagV == 1) {
					// TERMINA No Y en casoV
					if (DEBUG0) {
						escribe("2*Encontre <digito><espacio>%n");
						stop();
					}
					flag_n = 0;
					flag_finCal = 1;
					fin_X = 0;
					fin_Y = 1;
					flagEND = 1;
				}

				if (SimbolosPermitidos.ES_PUNTO(car)) { // COMIENZA FRACCION
					if (DEBUG0) {
						escribe("Encontre <punto>%n");
						stop();
					}
					flag_d = 1;
					ifrac = 0;
				}

				if ((SimbolosPermitidos.ES_COMA(carp)) && (SimbolosPermitidos.ES_DIGITO(car))) {
					// COMIENZA No Y
					if (DEBUG0) {
						escribe("Encontre <coma><digito>%n");
						stop();
					}
					flag_s = 0;
					flag_n = 1;
					suma = 0.;
					inum = 0;
					flag_d = 0;
					fin_Y = 0;
					fin_X = 0;
				}

				if ((SimbolosPermitidos.ES_SIGNO(carp)) && (SimbolosPermitidos.ES_DIGITO(car))) {
					// COMIENZA No Y
					if (DEBUG0) {
						escribe("Encontre <signo><digito>%n");
						stop();
					}
					flag_s = 1;
					flag_n = 1;
					suma = 0.;
					inum = 0;
					flag_d = 0;
					fin_Y = 0;
					fin_X = 0;
				}
				// if (SimbolosPermitidos.ES_DIGITO(carp)
				// && (SimbolosPermitidos.ES_ESPACIO(car) ||
				// SimbolosPermitidos.ES_COMILLAS(car))
				// && (flagH == 0 && flagV == 0)) {
				if (SimbolosPermitidos.ES_DIGITO(carp) && (SimbolosPermitidos.ES_ESPACIO(car)
						|| SimbolosPermitidos.ES_NEWLINE(car) || SimbolosPermitidos.ES_COMILLAS(car)) && flagNor == 1) {
					// TERMINA No Y caso normal
					if (DEBUG2) {
						System.out.format("flagNor:%d Encontre <digito><espacio>%n", flagNor);
						stop();
					}
					flag_n = 0;
					flag_finCal = 1;
					fin_Y = 1;
					fin_X = 0;
				}
				if (SimbolosPermitidos.ES_DIGITO(carp) && (car == 'm' || car == 'M') && flagNor == 1) {
					// TERMINA No Y caso normal
					if (DEBUG2) {
						System.out.format("flagNor:%d Encontre <digito><espacio>%n", flagNor);
						stop();
					}
					flag_n = 0;
					flag_finCal = 1;
					fin_Y = 1;
					fin_X = 0;
					flagEND = 1;
				}
				if (flag_n == 1 && SimbolosPermitidos.ES_DIGITO(car)) {
					// agrega digitos a suma en potencia correcta
					int carN = car - '0';
					if (inum > 0)
						suma = suma * 10.;
					suma = suma + (double) carN;
					inum++;
					if (DEBUG0) {
						escribe("flag_n=1 Y encontre <digito>%n");
						stop();
					}
				}
				if (flag_d == 1 && flag_n == 1 && SimbolosPermitidos.ES_DIGITO(car)) {
					// calcula corrimiento de coma en // fraccion
					if (DEBUG0) {
						escribe("flag_d=1 Y encontre <digito>%n");
						stop();
					}
					frac = frac * 10.;
				}

				if (flagEND == 0 && (SimbolosPermitidos.ES_MAYUSCULA(car) || SimbolosPermitidos.ES_MINUSCULA(car))) {
					// empila codigo de operacion

					if (DEBUG0) {
						System.out.format("Encontre codigo   %c   de operacion%n", car);
						stop();
					}

					p1 = new PointYLetra();
					p1.flag_vis = flag_vis;
					p1.t = car;
					p1.x = 0;
					p1.y = 0;
					if (noNo == 0) {// primer letra
						noNo = 1;
						pList = new LinkedList<PointYLetra>();
					}
					pList.add(p1);
				}
				if (flag_finCal == 1) {
					// CALCULA No

					if (DEBUG0) {
						escribe("flag_finCal==1  fin de calculo de numero%n");
						stop();
					}
					// CALCULA No
					suma = suma / frac;
					z = suma;
					if (flag_s == 1)
						z = -z;// cambio de signo
					if (DEBUG0) {
						System.out.format("z = %f \n", z);
						stop();
					}
					flag_finCal = 0;// reinicializa
					frac = 1.;// reinicializa
					if (flagH == 1) {
						flagNor = 0;
						if (fin_X == 1) {
							if (DEBUG0) {
								System.out.println(
										"flagV: " + flagV + " flagH: " + flagH + " flagNor: " + flagNor + " z: " + z);
							}
							pYl = new PointYLetra('n', flag_vis, z, 0);
							if (DEBUG0) {
								System.out.println("pYl.flag_vis :" + pYl.flag_vis + " pYl.t :" + pYl.t + " pYl.x :"
										+ pYl.x + " pYl.y :" + pYl.y);
							}
							pList.add(pYl);

							// flagH = 0;
							if (DEBUG0)
								System.out.println(1);
						}
						// printf("flag_H==1 finX==1 empila X\n");stop();
					} else if (flagV == 1) {
						flagNor = 0;
						if (fin_Y == 1) {
							if (DEBUG0) {
								System.out.println(
										"flagV: " + flagV + " flagH: " + flagH + " flagNor: " + flagNor + " z: " + z);
							}
							pYl = new PointYLetra('n', flag_vis, 0, z);
							if (DEBUG0) {
								System.out.println("pYl.flag_vis :" + pYl.flag_vis + " pYl.t :" + pYl.t + " pYl.x :"
										+ pYl.x + " pYl.y :" + pYl.y);
							}
							pList.add(pYl);
							// flagV = 0;
							if (DEBUG0)
								System.out.println(2);
						}
						// printf("flag_V==1 finY==1 empila Y\n");stop();
					} else if (flagNor == 1) {
						if (DEBUG0) {
							System.out.println(
									"flagV: " + flagV + " flagH: " + flagH + " flagNor: " + flagNor + " z: " + z);
						}
						// flagNor=1;flagV = 0;flagH = 0;
						// caso normal
						// printf("flag_Y==1 finY==0 empila X,Y\n");stop();
						if (fin_X == 1) {
							if (DEBUG0)
								System.out.println(3);
							xPoint = z;
						} // termino calculo de X(lo guarda para empilarlo junto con Y)
						if (fin_Y == 1) {
							// pList.add(new PointYLetra('n', flag_vis, xPoint, z));

							pYl = new PointYLetra('n', flag_vis, xPoint, z);
							if (DEBUG0) {
								System.out.println("pYl.flag_vis : " + pYl.flag_vis + " pYl.t :" + pYl.t + " pYl.x :"
										+ pYl.x + " pYl.y :" + pYl.y);
							}
							pList.add(pYl);

							if (DEBUG0)
								System.out.println(4);
						} // termino calculo de Y
					}
				}
				// tambien empila el ultimo '"' como '$' para caso de PATH ABIERTO que termina
				// en <digito>"
				if ((car == '"') && (SimbolosPermitidos.ES_DIGITO(carp))) {

					if (pList != null)
						pList.add(new PointYLetra('$', flag_vis, 0., 0.));
					// empila codigo
				}
				if (flagEND == 1) {
					pList.add(new PointYLetra('$', flag_vis, 0., 0.));
					pList.add(new PointYLetra('M', flag_vis, 0., 0.));
					flagEND = 0;
					// empila codigo
				}
				carp = car;
				i++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			aviso("No puedo leer caracters");
		}
		try {
			data_in.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block

			escribe("No pude cerrar data_in input");
			System.exit(0);
			// e2.printStackTrace();
		}
		if (DEBUG2) {
			for (int r = 0; r < pList.size(); r++) {
				System.out.format("%c %f %f\n", pList.get(r).t, pList.get(r).x, pList.get(r).y);
			}
			salgo();
		}

		if (DEBUG) {
			escribe("Antes de comenzar analisis de elementos pointYLetra%n");
			stop();
		}

		/*************************************************************
		 * Pasada de creacion de objetos de calculo Interpreta los codigos y crea los
		 * segmentos de bezier o lineas rectas 1. Si encuentra L o l toma los puntos
		 * previo y proximo y los junta en elemento de tipo linea 2. Si encuentra c o C
		 * toma el punto previo y los 3 puntos que siguen y los coloca en elemento de
		 * tipo Bezier cubica 3. Si encuentra q o Q toma el punto anterior y los dos que
		 * siguen y los coloca en elemento de tipo Bezier cuadratica 4. Cuando se esta
		 * sonbre un numero, se continua con la interpretacion asociada al ultimo tipo
		 * de codigo de operacion, con la correccion que ahora la posicion del codigo es
		 * substituida por la posicion del numero encontrado. 4. Si encuentra m o M y
		 * existe numero posterior,toma el punto presente y el punto que sigue y hace
		 * elemento de tipo linea Las coordenadas del punto que sigue son absolutas, en
		 * caso M, o se les agrega el corrimiento del primer punto despues del simbolo,
		 * en el caso m.Se hacen lineas entre el punto presente y el que sigue. 5. Si se
		 * encuentra v o V , h o H. que corresponden al caso de un path rectangular, se
		 * generaran elementos de calculo tipo linea, utilizando el resultado de agragar
		 * los corrimentos de las coordendas que cambian en cada caso. Se calcularan
		 * valores absolutos o relativos dependiendo si el simbolo esta escrito con
		 * mayusculas o minusculas.
		 * 
		 * 
		 ***************************************************************/

		/*************************************************************
		 * 
		 * 1. chequea flags flagBezierC, flagBezierQ, flagBezierL,
		 * flagBezierM,flagBezierH,flagBezierV; 2. Si estan en cero es el comienzo de
		 * calculo Se generara un malloc para primer elemento de calculo. Despues solo
		 * hay empilamiento 3. Una vez que se establece el primer elemento de calculo
		 * uno de los flags esta activo. 4. cuando se encuentra codigo de operacion
		 * puede haber cambio de flag do{ 1)Siempre se revisa tipo de punto. I--Si este
		 * es de un numero 'n', se revisa el flag que esta activo, y se continua con
		 * asignacion a las coordenadas X1,X2,X3,X4 de un nuevo elemento de calculo
		 * asociadas este flag. Para cada elemento de calculo se corre el puntero px en
		 * las unidades adecuadas al tipo de flag activo(cada elemento de calculo
		 * considera, en general, varios numeros seguidos, por lo que hay que actualizar
		 * px al ultimo numero considerado).
		 * 
		 * II--Si es codigo de operacion(no es 'n'), se actualiza flag activo y se
		 * aplica, para el tipo de flag activo, las operaciones de asignacion a las
		 * coordenadas X1,X2,X3,X4 del nuevo elemento de calculo, de los elementos px
		 * consecutivos que le son adecuados. Luego se corrige la posicion del puntero
		 * px, analogo a lo que se hace en I.
		 * 
		 * III--Si es primer elemento de calculo se hace malloc. Si no es el primer
		 * elemento de calculo se empila.
		 * 
		 * px=px->sH; <--------- Corrimiento comun a todos los elementos de calculo.
		 * }while(px!=NULL); En resumen: los elementos pointYLetra (que estan en pila
		 * px) contienen informacion codificada que se interpreta segun el codigo de
		 * operacion dada por las letras. Cada tipo de operacion considera una secuencia
		 * de elementos de px consecutivos, que deben ser interpretados e insertados en
		 * las coordenadas x1,x2,x3,x4,y1,y2,y3,y4 de los elementos de calculo de tipo:
		 * eleCalculo, empilados en: e1.El puntero px "salta" de acuerdo al tipo de
		 * operacion vigente y si hay cambio de operacion o se continua con la anterior.
		 * Ej; n C n n n n n n L n n 1 2 3 4 5 6 7 8 9 10 11
		 * 
		 * Cuando se esta en C se consideran los puntos 1 3 4 y 5 para llenar eleCalculo
		 * tipo Bezier cubica. Despues del salto, y se esta en la posicion 6, se
		 * continua con la interpretacion C pero se usan los puntos: 5 6 7 8. Despues
		 * del salto, y cuando se esta en 9 se cambia de interpretacion y se usan los
		 * puntos 8 y 10 para una linea(Bezier lineal). Despues del salto y cuando se
		 * llega a 11 se continua con interpretacion L y se usan los puntos: 10 y 11.
		 */
		PathList listaDepath = null, listaDepathFirst = null, listaDepathLast = null;

		double x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0, x4 = 0, y4 = 0, xINI = 0, yINI = 0;
		EleCalculo e1last = null, e1first = null, e1 = null, e1firstPrev = null, e1lastPrev = null;

		char oper;
		int vis;
		int flagAccion = 0, flagBezierC = 0, flagBezierQ = 0, flagBezierL = 0, flagBezierV = 0, flagBezierH = 0,
				flagBezierM = 0, indBezierM = 0;
		int flagMin = 0;
		// px=p1first;

		PointYLetra p1firstPrev;
		int idP1first = 0;
		// PointYLetra px=pList.getFirst();

		/********************************************************************************
		 * Transformacion de linked list de PointYLetra a ARRAY de PointYLetra
		 */

		PointYLetra[] px = null;
		px = pList.toArray(new PointYLetra[0]);
		LinkedList<EleCalculo> listEleCal = new LinkedList<EleCalculo>();
		LinkedList<Path> listPath = new LinkedList<Path>();
		int flagPathid = 0;

		/********************************************************************************/
		// Hace copîa de PointYLetra[] px
		// PointYLetra[] pxC = null;
		// pxC = new PointYLetra[px.length];
		// pxC = px.clone();

		// System.out.println("px[0].t " + px[0].t + " px[0].x " + px[0].x);
		// System.out.println("pxC[0].t " + pxC[0].t + " pxC[0].x " + pxC[0].x);
		// System.exit(0);
		/*****************************************************************************/
		int no_PointYLetra = pList.size();// No de elementos en lista de PointYLetra
		int no_px = px.length;
		if (no_PointYLetra != no_px) {
			System.out.println("ERROR:Incompatibilidad en tamanos");
			System.exit(0);
		}
		int i_px = 0; // indice de px
		do { // LOOP EXTERNO i_px=i_px+1; => px[i_px]...
			flagAccion = 0;
			oper = px[i_px].t;
			flag_vis = px[i_px].flag_vis; // Obtiene visibilidad del path
			// todos los puntos del path tienen la misma visibilidad
			if (oper != 'n' && oper != 'N') {// 1 Encontro CODIGO en lista de : pointYLetra
				if (oper == 'm' || oper == 'M') {
					if (DEBUG)
						System.out.println(1);
					flagBezierC = 0;
					flagBezierQ = 0;
					flagBezierL = 0;
					flagBezierV = 0;
					flagBezierH = 0;
					flagBezierM = 1;
					idP1first = i_px;// guarda localizacion de m o n
					flagMin = 0;
					if (oper == 'm')
						flagMin = 1; // relativo
					if (DEBUG) {
						System.out.format("!N i_px: %d operacion: M o m flagMin:%d%n", i_px, flagMin);
						stop();
					}
				}
				if (oper == 'C' || oper == 'c') {
					if (DEBUG)
						System.out.println(2);
					flagBezierC = 1;
					flagBezierQ = 0;
					flagBezierL = 0;
					flagBezierV = 0;
					flagBezierH = 0;
					flagBezierM = 0;
					flagMin = 0;
					if (oper == 'c')
						flagMin = 1;
					if (DEBUG) {
						System.out.format("!N i_px: %d operacion: C o c flagMin:%d%n", i_px, flagMin);
						stop();
					}
				}
				if (oper == 'V' || oper == 'v') {
					if (DEBUG)
						System.out.println(3);
					flagBezierC = 0;
					flagBezierQ = 0;
					flagBezierL = 0;
					flagBezierH = 0;
					flagBezierV = 1;
					flagBezierM = 0;
					flagMin = 0;
					if (oper == 'v')
						flagMin = 1;
					if (DEBUG) {
						System.out.format("!N i_px: %d operacion: V o v flagMin:%d%n", i_px, flagMin);
						stop();
					}
				}
				if (oper == 'H' || oper == 'h') {
					if (DEBUG)
						System.out.println(4);
					flagBezierC = 0;
					flagBezierQ = 0;
					flagBezierL = 0;
					flagBezierV = 0;
					flagBezierH = 1;
					flagBezierM = 0;
					flagMin = 0;
					if (oper == 'h')
						flagMin = 1;
					if (DEBUG) {
						System.out.format("!N i_px: %d operacion: H o h flagMin:%d\n", i_px, flagMin);
						stop();
					}
				}
				if (oper == 'Q' || oper == 'q') {
					if (DEBUG)
						System.out.println(5);
					flagBezierC = 0;
					flagBezierQ = 1;
					flagBezierL = 0;
					flagBezierV = 0;
					flagBezierH = 0;
					flagBezierM = 0;
					flagMin = 0;
					if (oper == 'q')
						flagMin = 1;
					if (DEBUG) {
						System.out.format("!N i_px: %d operacion: Q o q flagMin:%d%n", i_px, flagMin);
						stop();
					}
				}

				if (oper == 'L' || oper == 'l') {
					if (DEBUG)
						System.out.println(6);
					flagBezierC = 0;
					flagBezierQ = 0;
					flagBezierL = 1;
					flagBezierV = 0;
					flagBezierH = 0;
					flagBezierM = 0;
					flagMin = 0;
					if (oper == 'l')
						flagMin = 1;
					if (DEBUG) {
						System.out.format("!N i_px: %d operacion: L o l flagMin:%d%n", i_px, flagMin);
						stop();
					}
				}

				if (oper == 'Z' || oper == 'z' || oper == '$') {// fz
					if (DEBUG)
						System.out.println(7);
					flagBezierC = 0;
					flagBezierQ = 0;
					flagBezierL = 0;
					flagBezierV = 0;
					flagBezierH = 0;
					flagBezierM = 0;

					if (oper == 'Z' || oper == 'z')
						oper = 'Z';// <-- se mantiene para usarlo como iniciador de path
					// <-------------- luego sera cam biado por L
					else
						oper = '$';// sirve para indicar paths abiertos

					x1 = 0.;
					y1 = 0.;
					x2 = 0.;
					y2 = 0.;
					x3 = 0.;
					y3 = 0.;
					x4 = 0.;
					y4 = 0.;
					// Para el cazo Z SE CORREGIRA DESPUES AL FINAL: UNION CON PRIMERO
					flagAccion = 1;// s}e generara elemento de calculo
					if (DEBUG) {
						System.out.format("!N i_px: %d operacion: Z o z o \"\n", i_px);
						stop();
					}
				}
				if (flagBezierC == 1) {
					if (DEBUG)
						System.out.println(8);
					if (((i_px + 1) < no_PointYLetra) && ((i_px + 2) < no_PointYLetra) && ((i_px + 3) < no_PointYLetra)
							&& (px[i_px + 1] != null && px[i_px + 2] != null && px[i_px + 3] != null)) {
						// Caso de 2 numeros con un codigo intercalado
						// CORRIGE POR VALORES RELATIVOS
						// secuencia de elementos pointYLetra que seran considerados en elemento de
						// calculo
						// numero(px->sT) CODIGO(px) numero(px->sH) numero(px->sH->sH)
						// numero(px->sH->sH->sH)
						// numero(px[i_px-1]) CODIGO(px[i_px]) numero(px[i_px+1])
						// numero(px[i_px+2])numero (px[i_px+3])
						if (flagMin == 1) {// caso relativo
							// el valor de px->sH->x se agrega al valor px->sT->x y luego -> px->sH->x
							// el valor de px->sH->SH->x se agrega al valor px->sT->x y luego ->
							// px->sH->SH->x
							// el valor de px->sH->SH->SH->x se agrega al valor px->sT->x y luego ->
							// px->sH->SH->SH->x
							// Lo mismo se hace para coordenadas y
							px[i_px + 1].x = px[i_px + 1].x + px[i_px - 1].x;
							px[i_px + 2].x = px[i_px + 2].x + px[i_px - 1].x;
							px[i_px + 3].x = px[i_px + 3].x + px[i_px - 1].x;

							px[i_px + 1].y = px[i_px + 1].y + px[i_px - 1].y;
							px[i_px + 2].y = px[i_px + 2].y + px[i_px - 1].y;
							px[i_px + 3].y = px[i_px + 3].y + px[i_px - 1].y;

							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = px[i_px + 2].x;
							y3 = px[i_px + 2].y;
							x4 = px[i_px + 3].x;
							y4 = px[i_px + 3].y;

						} else {// caso absoluto

							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = px[i_px + 2].x;
							y3 = px[i_px + 2].y;
							x4 = px[i_px + 3].x;
							y4 = px[i_px + 3].y;

						}
						if (DEBUG)
							System.out.println("!N flagBezierC=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("!N flagBezierC=1 px[i_px - 1].x: " + px[i_px - 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierC=1 px[i_px - 1].y: " + px[i_px - 1].y);
						if (DEBUG)
							System.out.println("!N flagBezierC=1 px[i_px + 1].x: " + px[i_px + 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierC=1 px[i_px + 1].y: " + px[i_px + 1].y);
						if (DEBUG)
							System.out.println("!N flagBezierC=1 px[i_px + 2].x: " + px[i_px + 2].x);
						if (DEBUG)
							System.out.println("!N flagBezierC=1 px[i_px + 2].y: " + px[i_px + 2].y);
						if (DEBUG)
							System.out.println("!N flagBezierC=1 px[i_px + 3].x: " + px[i_px + 3].x);
						if (DEBUG)
							System.out.println("!N flagBezierC=1 px[i_px + 3].y: " + px[i_px + 3].y);

						i_px = i_px + 3;// se corre en lista de puntos SALTO
						if (DEBUG)
							System.out.println("!N flagBezierC=1 Despues de salto i_px: " + i_px);
						flagAccion = 1;// orden para generar elemento de calculo
						if (DEBUG) {
							System.out.format("!N flagBezierC=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f\n", x1, y1, x2, y2);
							System.out.format("!N flagBezierC=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f\n", x3, y3, x4, y4);
							stop();
							System.out.println("");

						}
					} else {
						escribe("MAL ESCRITA SECUENCIA DE PUNTOS flagBezierC==1 oper!='n'\n");
						System.exit(0);
					}
				}
				if (flagBezierH == 1) {
					if (DEBUG)
						System.out.println(9);
					if (DEBUG)
						System.out.println(" oper!='n' flagBezierH==1");
					if (((i_px + 1) < no_PointYLetra) && px[i_px + 1] != null) {
						// Caso de 2 numeros con un codigo intercalado
						// CORRIGE POR VALORES RELATIVOS
						// secuencia de elementos pointYLetra que seran considerados en elemento de
						// calculo
						// numero(px->sT) CODIGO(px) numero(px->sH) n
						// numero(px[i_px-1]) CODIGO(px.[i_px]) numero(px[i_px+1]) n
						// x1 x2
						if (flagMin == 1) {// caso relativo

							px[i_px + 1].x = px[i_px - 1].x + px[i_px + 1].x;
							px[i_px + 1].y = px[i_px - 1].y;// mantiene valor anterior

							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = 0.;
							y3 = 0.;
							x4 = 0.;
							y4 = 0.;

						} else {// caso absoluto
							px[i_px + 1].y = px[i_px - 1].y;// mantiene valor anterior
							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = 0.;
							y3 = 0.;
							x4 = 0.;
							y4 = 0.;

						}
						if (DEBUG)
							System.out.println("!N flagBezierH=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("!N flagBezierH=1 px[i_px - 1].x: " + px[i_px - 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierH=1 px[i_px - 1].y: " + px[i_px - 1].y);
						if (DEBUG)
							System.out.println("!N flagBezierH=1 px[i_px + 1].x: " + px[i_px + 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierH=1 px[i_px + 1].y: " + px[i_px + 1].y);

						i_px = i_px + 1;// se corre en lista de puntos SALTO
						if (DEBUG)
							System.out.println("!N flagBezierH=1 Despues de salto i_px: " + i_px);
						oper = 'L';// se calculara como
						flagAccion = 1;// orden para generar elemento de clculo
						if (DEBUG) {
							System.out.format("flagBezierH=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f\n", x1, y1, x2, y2);
							System.out.format("flagBezierH=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f\n", x3, y3, x4, y4);
							stop();
						}

					} else {
						System.out.format("MAL ESCRITA SECUENCIA DE PUNTOS flagBezierH==1 oper!='n'\n");
						System.exit(0);
					}
				}
				if (flagBezierV == 1) {
					if (DEBUG)
						System.out.println(10);
					if (((i_px + 1) < no_PointYLetra) && px[i_px + 1] != null) {
						// Caso de 2 numeros con un codigo intercalado
						// CORRIGE POR VALORES RELATIVOS
						// secuencia de elementos pointYLetra que seran considerados en elemento de
						// calculo
						// numero(px->sT) CODIGO(px) numero(px->sH) n
						// numero(px[i_px-1])CODIGO px[i_px] numero px[i_px+1]n
						// x1 x2

						if (flagMin == 1) {// caso relativo

							px[i_px + 1].y = px[i_px - 1].y + px[i_px + 1].y;
							px[i_px + 1].x = px[i_px - 1].x;// corserva X en movimiento vertical
							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = 0.;
							y3 = 0.;
							x4 = 0.;
							y4 = 0.;

						} else {
							// caso absoluto
							px[i_px + 1].x = px[i_px - 1].x;// corserva X en movimiento vertical
							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = 0.;
							y3 = 0.;
							x4 = 0.;
							y4 = 0.;
						}
						if (DEBUG)
							System.out.println("!N flagBezierV=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("!N flagBezierV=1 px[i_px - 1].x: " + px[i_px - 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierV=1 px[i_px - 1].y: " + px[i_px - 1].y);
						if (DEBUG)
							System.out.println("!N flagBezierV=1 px[i_px + 1].x: " + px[i_px + 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierV=1 px[i_px + 1].y: " + px[i_px + 1].y);

						i_px = i_px + 1;// se corre en lista de puntos SALTO
						if (DEBUG)
							System.out.println("!N flagBezierV=1 Despues de salto i_px: " + i_px);
						oper = 'L';// se calculara como linea
						flagAccion = 1;// orden para generar elemento de clculo
						if (DEBUG) {
							System.out.format("flagBezierV=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f\n", x1, y1, x2, y2);
							System.out.format("flagBezierV=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f\n", x3, y3, x4, y4);
							stop();
						}

					} else {
						System.out.format("MAL ESCRITA SECUENCIA DE PUNTOS flagBezierV==1 oper!='n'\n");
						System.exit(0);
					}
				}

				if (flagBezierQ == 1) {
					if (DEBUG)
						System.out.println(11);
					if (((i_px + 1) < no_PointYLetra) && ((i_px + 2) < no_PointYLetra)
							&& (px[i_px + 1] != null && px[i_px + 2] != null)) {
						// secuencia de elementos pointYLetra que seran considerados en elemento de
						// calculo
						// numero(px->sT) CODIGO(px) numero(px->sH) numero(px->sH->sH)
						// numero px[i_px-1] CODIGO px[i_px] numero px[i_px+1] numero px[i_px+2]
						// x1 x2 x3
						if (flagMin == 1) {// caso relativo

							px[i_px + 1].x = px[i_px + 1].x + px[i_px - 1].x;
							px[i_px + 2].x = px[i_px + 2].x + px[i_px - 1].x;

							px[i_px + 1].y = px[i_px + 1].y + px[i_px - 1].y;
							px[i_px + 2].y = px[i_px + 2].y + px[i_px - 1].y;

							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = px[i_px + 2].x;
							y3 = px[i_px + 2].y;
							x4 = 0.;
							y4 = 0.;
						} else {
							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = px[i_px + 2].x;
							y3 = px[i_px + 2].y;
							x4 = 0.;
							y4 = 0.;
						}
						if (DEBUG)
							System.out.println("!N flagBezierQ=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("!N flagBezierQ=1 px[i_px - 1].x: " + px[i_px - 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierQ=1 px[i_px - 1].y: " + px[i_px - 1].y);
						if (DEBUG)
							System.out.println("!N flagBezierQ=1 px[i_px + 1].x: " + px[i_px + 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierQ=1 px[i_px + 1].y: " + px[i_px + 1].y);
						if (DEBUG)
							System.out.println("!N flagBezierQ=1 px[i_px + 2].x: " + px[i_px + 2].x);
						if (DEBUG)
							System.out.println("!N flagBezierQ=1 px[i_px + 2].y: " + px[i_px + 2].y);

						i_px = i_px + 2;// Se corre en lista SALTO
						if (DEBUG)
							System.out.println("!N flagBezierQ=1 Despues de salto i_px: " + i_px);
						flagAccion = 1;// orden para generar elemento de clculo

						if (DEBUG) {
							System.out.format("flagBezierQ=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f\n", x1, y1, x2, y2);
							System.out.format("flagBezierQ=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f\n", x3, y3, x4, y4);
							stop();
						}

					} else {
						escribe("MAL ESCRITA SECUENCIA DE PUNTOS flagBezierQ==1\n");
						System.exit(0);
					}
				}
				if (flagBezierL == 1) {
					if (DEBUG)
						System.out.println(12);
					if (((i_px + 1) < no_PointYLetra) && ((i_px + 2) < no_PointYLetra)
							&& (px[i_px + 1] != null && px[i_px + 2] != null)) {
						// secuencia de elementos pointYLetra que seran considerados en elemento de
						// calculo
						// numero(px->sT) CODIGO(px) numero(px->sH)
						// numero px[i_px-1] CODIGO px[i_px] numero px[i_px+1]
						// x1 x2
						if (flagMin == 1) {
							if (DEBUG)
								System.out.println("!Na flagMin == 1 flagBezierL=1 Antes de salto i_px: " + i_px);
							if (DEBUG)
								System.out.println("!Na flagMin == 1 flagBezierL=1 px[i_px - 1].x: " + px[i_px - 1].x);
							if (DEBUG)
								System.out.println("!Na flagMin == 1 flagBezierL=1 px[i_px - 1].y: " + px[i_px - 1].y);
							if (DEBUG)
								System.out.println("!Na flagMin == 1 flagBezierL=1 px[i_px + 1].x: " + px[i_px + 1].x);
							if (DEBUG)
								System.out.println("!Na flagMin == 1 flagBezierL=1 px[i_px + 1].y: " + px[i_px + 1].y);

							px[i_px + 1].x = px[i_px + 1].x + px[i_px - 1].x;
							px[i_px + 1].y = px[i_px + 1].y + px[i_px - 1].y;

							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = 0.;
							y3 = 0.;
							x4 = 0.;
							y4 = 0.;

						} else {
							if (DEBUG)
								System.out.println("!Na flagMin == 0 flagBezierL=1 Antes de salto i_px: " + i_px);
							if (DEBUG)
								System.out.println("!Na flagMin == 0 flagBezierL=1 px[i_px - 1].x: " + px[i_px - 1].x);
							if (DEBUG)
								System.out.println("!Na flagMin == 0 flagBezierL=1 px[i_px - 1].y: " + px[i_px - 1].y);
							if (DEBUG)
								System.out.println("!Na flagMin == 0 flagBezierL=1 px[i_px + 1].x: " + px[i_px + 1].x);
							if (DEBUG)
								System.out.println("!Na flagMin == 0 flagBezierL=1 px[i_px + 1].y: " + px[i_px + 1].y);

							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = 0.;
							y3 = 0.;
							x4 = 0.;
							y4 = 0.;
						}
						if (DEBUG)
							System.out.println("!N flagBezierL=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("!N flagBezierL=1 px[i_px - 1].x: " + px[i_px - 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierL=1 px[i_px - 1].y: " + px[i_px - 1].y);
						if (DEBUG)
							System.out.println("!N flagBezierL=1 px[i_px + 1].x: " + px[i_px + 1].x);
						if (DEBUG)
							System.out.println("!N flagBezierL=1 px[i_px + 1].y: " + px[i_px + 1].y);

						i_px = i_px + 1;// Se corre en lista SALTO
						if (DEBUG)
							System.out.println("!N flagBezierL=1 Despues de salto i_px: " + i_px);
						flagAccion = 1;// orden para generar elemento de clculo

						if (DEBUG) {
							System.out.format("flagBezierL=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f\n", x1, y1, x2, y2);
							System.out.format("flagBezierL=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f\n", x3, y3, x4, y4);
							stop();
						}

					} else {
						escribe("MAL ESCRITA SECUENCIA DE PUNTOS flagBezierL==1\n");
						System.exit(0);
					}
				}
				// NOTAR QUE A diferencia de los otros casos,NO SE REVISA EN ESTA SECCION
				// EL CASO; flagBezierM==1
				// Se revisa en la siguiente, solo cuando px contiene un numero y esta activo
				// el flagBezierM. Esto es asi pues NO hay numeros PREVIOS a 'm' o 'M'.
				// que complicarian la decodificacion.
				// En los otros tipos de calculo hay diferencias en la decodificacion entre elos
				// casos en que se esta sobre una letra de codigo, y cuando se esta sobre un
				// numero.
				// Por esto es que hay que tratarlos en forma diferente.

			} else {// oper== 'n' || 'N'
				if (flagBezierM == 1) {
					if (DEBUG)
						System.out.println(13);
					if (DEBUG)
						aviso("flagBezierM==1");
					if (indBezierM == 0) {
						// primer numero en sector m
						xINI = px[i_px].x; // <==========NO ES NECESARIO !!!!!
						yINI = px[i_px].y;
						// Lo guarda para hacer correcciones relativas a este punto
					}
					indBezierM++;
					if (((i_px + 1) < no_PointYLetra) && (px[i_px + 1] != null && px[i_px + 1].t == 'n')) {
						if (DEBUG) {
							System.out.format("flagBezierM==1 " + "px->sH->t=='n' px->sH->x:%f px->sH->y:%f\n",
									px[i_px + 1].x, px[i_px + 1].y);
							stop();
						}

					}
					if (((i_px + 1) < no_PointYLetra) && (px[i_px + 1] != null && px[i_px + 1].t == 'n')) {
						// caso que hay dos o mas numeros despues de codigo m,(M)
						oper = 'L';
						if (flagMin == 1) {
							if (DEBUG)
								System.out.println(14);

							if (DEBUG)
								aviso("flagBezierM==1 flagMin==1");
							oper = 'l';
							px[i_px + 1].x = px[i_px].x + px[i_px + 1].x;
							// agrega al punto siguiente // el actual
							px[i_px + 1].y = px[i_px].y + px[i_px + 1].y;// agrega al punto siguiente // el actual
							x1 = px[i_px].x;
							y1 = px[i_px].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = 0.;
							y3 = 0.;
							x4 = 0.;
							y4 = 0.;

							/*
							 * aviso("flagBezierM==1 flagMin==1"); oper = 'l'; px[i_px ].x = px[i_px].x +
							 * px[i_px - 1].x;// agrega al punto siguiente // el actual px[i_px ].y =
							 * px[i_px].y + px[i_px -1 ].y;// agrega al punto siguiente // el actual x1 =
							 * px[i_px-1].x; y1 = px[i_px-1].y; x2 = px[i_px ].x; y2 = px[i_px ].y; x3 = 0.;
							 * y3 = 0.; x4 = 0.; y4 = 0.;
							 */

							if (DEBUG) {
								System.out.format("flagMin=1. xINI:%f yINI:%f%n", xINI, yINI);
								stop();
							}
						} else {
							if (DEBUG)
								aviso("flagBezierM==1 flagMin==0");
							if (DEBUG)
								System.out.println(15);
							oper = 'L';

							x1 = px[i_px].x;
							y1 = px[i_px].y;
							x2 = px[i_px + 1].x;
							y2 = px[i_px + 1].y;
							x3 = 0.;
							y3 = 0.;
							x4 = 0.;
							y4 = 0.;

							/*
							 * oper = 'L'; x1 = px[i_px-1].x; y1 = px[i_px-1].y; x2 = px[i_px].x; y2 =
							 * px[i_px].y; x3 = 0.; y3 = 0.; x4 = 0.; y4 = 0.;
							 */

							if (DEBUG) {
								escribe("flagMin=0.%n");
								stop();
							}
						}

						if (DEBUG) {
							System.out.format("N flagBezierM=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f%n", x1, y1, x2, y2);
							System.out.format("N flagBezierM=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f%n", x3, y3, x4, y4);
							stop();
						}
						if (DEBUG)
							System.out.println("N flagBezierM=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("N flagBezierM=1 px[i_px - 1].x: " + px[i_px].x);
						if (DEBUG)
							System.out.println("N flagBezierM=1 px[i_px - 1].y: " + px[i_px].y);
						if (DEBUG)
							System.out.println("N flagBezierM=1 px[i_px ].x: " + px[i_px + 1].x);
						if (DEBUG)
							System.out.println("N flagBezierM=1 px[i_px ].y: " + px[i_px + 1

							].y);

						i_px = i_px;// NO se corre en lugar SALTO
						if (DEBUG)
							System.out.println("N flagBezierM=1 Despues de salto i_px: " + i_px);
						flagAccion = 1;// se generara elemento de calculo
					} // solo trabaja si proximo item es numero
				}
				if (flagBezierC == 1) { // ha quedado el tipo de Bezier del codigo anterior
					if (DEBUG)
						System.out.println(16);
					if (((i_px + 1) < no_PointYLetra) && ((i_px + 2) < no_PointYLetra)
							&& (px[i_px + 1] != null && px[i_px + 2] != null)) { // CORRIGE POR RELATIVIDAD
						// CORRIGE PORVALORES RELATIVOS
						// secuencia de elementos pointYLetra que seran considerados
						// en elemento de calculo
						// numero(px->sT) numero(px) numero(px->sH)
						// numero px[i_px-1] numero px[i_px] numeropx[i_px+1] numero px[i_px+2]
						// x1 x2 x3 x4
						if (DEBUG) {
							escribe("Antes de modificar datos flagBezierC==1 caso oper=='n'\n");
							System.out.format("px->sT->x:%f px->sT->y:%f\npx->x:%f px->y:%f\n", px[i_px - 1].x,
									px[i_px - 1].y, px[i_px].x, px[i_px].y);
							System.out.format("px->sH->x:%f px->sH->y:%f\npx->sH->sH->x:%f px->sH->sH->y:%f\n",
									px[i_px + 1].x, px[i_px + 1].y, px[i_px + 2].x, px[i_px + 2].y);
						}
						if (flagMin == 1) {
							oper = 'c';
							px[i_px].x = px[i_px].x + px[i_px - 1].x;
							px[i_px + 1].x = px[i_px + 1].x + px[i_px - 1].x;
							px[i_px + 2].x = px[i_px + 2].x + px[i_px - 1].x;

							px[i_px].y = px[i_px].y + px[i_px - 1].y;
							px[i_px + 1].y = px[i_px + 1].y + px[i_px - 1].y;
							px[i_px + 2].y = px[i_px + 2].y + px[i_px - 1].y;

							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px].x;
							y2 = px[i_px].y;
							x3 = px[i_px + 1].x;
							y3 = px[i_px + 1].y;
							x4 = px[i_px + 2].x;
							y4 = px[i_px + 2].y;

						} else {
							oper = 'C';
							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px].x;
							y2 = px[i_px].y;
							x3 = px[i_px + 1].x;
							y3 = px[i_px + 1].y;
							x4 = px[i_px + 2].x;
							y4 = px[i_px + 2].y;

						}
						if (DEBUG) {
							System.out.format("Despues de modificar datos flagBezierC==1 caso oper=='n'%n");
							System.out.format("px->sT->x:%f px->sT->y:%f\npx->x:%f px->y:%f\n", px[i_px - 1].x,
									px[i_px - 1].y, px[i_px].x, px[i_px].y);
							System.out.format("px->sH->x:%f px->sH->y:%f\npx->sH->sH->x:%f px->sH->sH->y:%f%n",
									px[i_px + 1].x, px[i_px + 1].y, px[i_px + 2].x, px[i_px + 2].y);

							System.out.format("N flagBezierC=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f%n", x1, y1, x2, y2);
							System.out.format("N flagBezierC=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f%n", x3, y3, x4, y4);
							stop();

						}
						if (DEBUG)
							System.out.println("N flagBezierC=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("N flagBezierC=1 px[i_px - 1].x: " + px[i_px - 1].x);
						if (DEBUG)
							System.out.println("N flagBezierC=1 px[i_px - 1].y: " + px[i_px - 1].y);
						if (DEBUG)
							System.out.println("N flagBezierC=1 px[i_px ].x: " + px[i_px].x);
						if (DEBUG)
							System.out.println("N flagBezierC=1 px[i_px ].y: " + px[i_px].y);
						if (DEBUG)
							System.out.println("N flagBezierC=1 px[i_px + 1].x: " + px[i_px + 1].x);
						if (DEBUG)
							System.out.println("N flagBezierC=1 px[i_px + 1].y: " + px[i_px + 1].y);
						if (DEBUG)
							System.out.println("N flagBezierC=1 px[i_px + 2].x: " + px[i_px + 2].x);
						if (DEBUG)
							System.out.println("N flagBezierC=1 px[i_px + 2].y: " + px[i_px + 2].y);

						i_px = i_px + 2;// Se corre en lista SALTO
						if (DEBUG)
							System.out.println("N flagBezierC=1 Despues de salto i_px: " + i_px);
						flagAccion = 1;// se generara elemento de clculo

					} else {
						if (px[i_px + 1].t != '$' && px[i_px + 1].t != ' ') {
							System.out.format("px->sH->t:%c\n", px[i_px + 1].t);
							System.out.format("px->x:%f px->y:%f\n", px[i_px].x, px[i_px].y);
							escribe("MAL ESCRITA SECUENCIA DE PUNTOS flagBezierC==1 oper=='n'\n");
							System.exit(0);
						}
					}
				}
				if (flagBezierQ == 1) {
					if (DEBUG)
						System.out.println(17);
					if (((i_px + 1) < no_PointYLetra) && px[i_px + 1] != null) {
						// secuencia de elementos pointYLetra que seran considerados en elemento de
						// calculo
						// numero(px->sT) numero(px) numero(px->sH)
						// numero px[i_px-1] numero px[i_px] numero px[i_px+1]
						// x1 x2 x3
						if (flagMin == 1) {
							oper = 'q';
							px[i_px].x = px[i_px].x + px[i_px - 1].x;
							px[i_px + 1].x = px[i_px + 1].x + px[i_px - 1].x;

							px[i_px].y = px[i_px].y + px[i_px - 1].y;
							px[i_px + 1].y = px[i_px + 1].y + px[i_px - 1].y;

							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px].x;
							y2 = px[i_px].y;
							x3 = px[i_px + 1].x;
							y3 = px[i_px + 1].y;
							// x4=px[i_px+2].x; y4=px[i_px+2].y;
							x4 = 0.;
							y4 = 0.; // NO SE USA EN BEZIER Q
						} else {
							oper = 'Q';
							x1 = px[i_px - 1].x;
							y1 = px[i_px - 1].y;
							x2 = px[i_px].x;
							y2 = px[i_px].y;
							x3 = px[i_px + 1].x;
							y3 = px[i_px + 1].y;
							// x4=px[i_px+2].x; y3=px[i_px+2].y;
							x4 = 0.;
							y4 = 0.;// NO SE USA EN BEZIER Q
						}

						if (DEBUG)
							System.out.println("N flagBezierQ=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("N flagBezierQ=1 px[i_px - 1].x: " + px[i_px - 1].x);
						if (DEBUG)
							System.out.println("N flagBezierQ=1 px[i_px - 1].y: " + px[i_px - 1].y);
						if (DEBUG)
							System.out.println("N flagBezierQ=1 px[i_px ].x: " + px[i_px].x);
						if (DEBUG)
							System.out.println("N flagBezierQ=1 px[i_px ].y: " + px[i_px].y);
						if (DEBUG)
							System.out.println("N flagBezierQ=1 px[i_px + 1].x: " + px[i_px + 1].x);
						if (DEBUG)
							System.out.println("N flagBezierQ=1 px[i_px + 1].y: " + px[i_px + 1].y);

						i_px = i_px + 1;// Se corre en lista SALTO
						if (DEBUG)
							System.out.println("N flagBezierQ=1 Despues de salto i_px: " + i_px);
						flagAccion = 1;// se generara elemento de clculo
					} else {
						escribe("MAL ESCRITA SECUENCIA DE PUNTOS flagBezierQ==1%n");
						System.exit(0);
					}
				}
				if (flagBezierL == 1) {
					if (DEBUG)
						System.out.println(18);
					oper = 'L';
					// secuencia de elementos pointYLetra que seran considerados en elemento de
					// calculo
					// numero(px->sT) numero(px)
					// numero px[i_px-1] numero px[i_px]
					// x1 x2
					if (flagMin == 1) {
						if (DEBUG)
							System.out.println(18 + " l" + " flaggMin; " + flagMin);
						if (DEBUG)
							System.out.println("Na flagBezierL=1 Antes de salto i_px: " + i_px);
						if (DEBUG)
							System.out.println("Na flagBezierL=1 px[i_px - 1].x: " + px[i_px - 1].x);
						if (DEBUG)
							System.out.println("Na flagBezierL=1 px[i_px - 1].y: " + px[i_px - 1].y);
						if (DEBUG)
							System.out.println("Na flagBezierL=1 px[i_px ].x: " + px[i_px].x);
						if (DEBUG)
							System.out.println("Na flagBezierL=1 px[i_px ].y: " + px[i_px].y);

						oper = 'l';
						px[i_px].x = px[i_px].x + px[i_px - 1].x;
						px[i_px].y = px[i_px].y + px[i_px - 1].y;

						x1 = px[i_px - 1].x;
						y1 = px[i_px - 1].y;
						x2 = px[i_px].x;
						y2 = px[i_px].y;
						x3 = 0.;
						y3 = 0.;
						x4 = 0.;
						y4 = 0.;
					} else {
						oper = 'L';
						if (DEBUG)
							System.out.println(18 + " L" + " flaggMin; " + flagMin);
						x1 = px[i_px - 1].x;
						y1 = px[i_px - 1].y;
						x2 = px[i_px].x;
						y2 = px[i_px].y;
						x3 = 0.;
						y3 = 0.;
						x4 = 0.;
						y4 = 0.;

					}
					if (DEBUG)
						System.out.println("N flagBezierL=1 Antes de salto i_px: " + i_px);
					if (DEBUG)
						System.out.println("N flagBezierL=1 px[i_px - 1].x: " + px[i_px - 1].x);
					if (DEBUG)
						System.out.println("N flagBezierL=1 px[i_px - 1].y: " + px[i_px - 1].y);
					if (DEBUG)
						System.out.println("N flagBezierL=1 px[i_px ].x: " + px[i_px].x);
					if (DEBUG)
						System.out.println("N flagBezierL=1 px[i_px ].y: " + px[i_px].y);

					i_px = i_px;// No hay SALTO
					if (DEBUG)
						System.out.println("N flagBezierL=1 Despues de salto i_px: " + i_px);
					flagAccion = 1;// se generara elemento de clculo
					if (DEBUG)
						System.out.format("en flagBezierL=1.x1:%f y1:%f x2=%f y2=%f\n", x1, y1, x2, y2);

					if (DEBUG)
						System.out.format("N flagBezierL=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f\n", x1, y1, x2, y2);
					if (DEBUG)
						System.out.format("N flagBezierL=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f\n", x3, y3, x4, y4);

					// stop();
				}
				/*
				 * OBS Es posible que los casos: flagBezierV==1 y flagBezierH==1, cuando
				 * oper='n' ,NO se den nunca Los dos casos que siguen puede que nunca sean
				 * visitados
				 */
				if (flagBezierH == 1) {
					if (DEBUG)
						System.out.println(19);
					aviso("---------------ENCONTRE H---------------");
					oper = 'H';
					// secuencia de elementos pointYLetra que seran considerados en elemento de
					// calculo
					// numero(px->sT) numero(px)
					// numero px[i_px-1] numero px[i_px]
					// x1 x2
					if (flagMin == 1) {
						if (DEBUG)
							System.out.println(19 + " h" + " flagMin: " + flagMin);
						oper = 'h';
						px[i_px].x = px[i_px].x + px[i_px - 1].x;
						px[i_px].y = px[i_px - 1].y;// No hay cambios

						x1 = px[i_px - 1].x;
						y1 = px[i_px - 1].y;
						x2 = px[i_px].x;
						y2 = px[i_px - 1].y;
						x3 = 0.;
						y3 = 0.;
						x4 = 0.;
						y4 = 0.;
					} else {
						if (DEBUG)
							System.out.println(19 + " H" + " flagMin: " + flagMin);
						oper = 'H';
						px[i_px].y = px[i_px - 1].y;// No hay cambios
						x1 = px[i_px - 1].x;
						y1 = px[i_px - 1].y;
						x2 = px[i_px].x;
						y2 = px[i_px - 1].y;
						x3 = 0.;
						y3 = 0.;
						x4 = 0.;
						y4 = 0.;

					}
					if (DEBUG)
						System.out.println("N flagBezierH=1 Antes de salto i_px: " + i_px);
					if (DEBUG)
						System.out.println("N flagBezierH=1 px[i_px - 1].x: " + px[i_px - 1].x);
					if (DEBUG)
						System.out.println("N flagBezierH=1 px[i_px - 1].y: " + px[i_px - 1].y);
					if (DEBUG)
						System.out.println("N flagBezierH=1 px[i_px ].x: " + px[i_px].x);
					if (DEBUG)
						System.out.println("N flagBezierH=1 px[i_px ].y: " + px[i_px].y);

					i_px = i_px;
					oper = 'L';
					if (DEBUG)
						System.out.println("N flagBezierH=1 Despues de salto i_px: " + i_px);
					flagAccion = 1;// se generara elemento de clculo
					if (DEBUG)
						System.out.format("flagBezierH == 1 px->x:%f px->y:%f\n", px[i_px].x, px[i_px].y);
					if (DEBUG)
						System.out.format("N flagBezierH=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f\n", x1, y1, x2, y2);
					if (DEBUG)
						System.out.format("N flagBezierH=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f\n", x3, y3, x4, y4);

					// stop();
				}
				if (flagBezierV == 1) {
					if (DEBUG)
						System.out.println(20);
					oper = 'V';
					// secuencia de elementos pointYLetra que seran considerados en elemento de
					// calculo
					// numero(px->sT) numero(px)
					// numero px[i_px-1] numero px[i_px]
					// x1 x2
					if (flagMin == 1) {
						oper = 'v';
						px[i_px].x = px[i_px - 1].x;// No hay cambios
						px[i_px].y = px[i_px].y + px[i_px - 1].y;

						x1 = px[i_px - 1].x;
						y1 = px[i_px - 1].y;
						x2 = px[i_px - 1].x;
						y2 = px[i_px].y;
						x3 = 0.;
						y3 = 0.;
						x4 = 0.;
						y4 = 0.;
					} else {
						oper = 'V';
						px[i_px].x = px[i_px - 1].x;// No hay cambios
						x1 = px[i_px - 1].x;
						y1 = px[i_px - 1].y;
						x2 = px[i_px - 1].x;
						y2 = px[i_px].y;
						x3 = 0.;
						y3 = 0.;
						x4 = 0.;
						y4 = 0.;
					}
					if (DEBUG)
						System.out.println("N flagBezierV=1 Antes de salto i_px: " + i_px);
					if (DEBUG)
						System.out.println("N flagBezierV=1 px[i_px - 1].x: " + px[i_px - 1].x);
					if (DEBUG)
						System.out.println("N flagBezierV=1 px[i_px - 1].y: " + px[i_px - 1].y);
					if (DEBUG)
						System.out.println("N flagBezierV=1 px[i_px ].x: " + px[i_px].x);
					if (DEBUG)
						System.out.println("N flagBezierV=1 px[i_px ].y: " + px[i_px].y);

					i_px = i_px;// NO hay SALTO
					oper = 'L';
					if (DEBUG)
						System.out.println("N flagBezierH=1 Despues de salto i_px: " + i_px);
					flagAccion = 1;// se generara elemento de clculo
					if (DEBUG)
						System.out.format("flagBezierV == 1 px->x:%f px->y:%f\n", px[i_px].x, px[i_px].y);

					if (DEBUG)
						System.out.format("N flagBezierV=1\tx1:\t%f\ty1:\t%f\tx2:\t%f\ty2:\t%f\n", x1, y1, x2, y2);
					if (DEBUG)
						System.out.format("N flagBezierV=1\tx3:\t%f\ty3:\t%f\tx4:\t%f\ty4:\t%f\n", x3, y3, x4, y4);

					if (DEBUG)
						stop();
				}

			}

			if (flagAccion == 1) {
				if (DEBUG)
					System.out.println(21);
				EleCalculo ecal = new EleCalculo(oper, x1, y1, x2, y2, x3, y3, x4, y4);
				listEleCal.add(ecal);
				EleCalculo eleLast = listEleCal.getLast();
				if (eleLast.t == 'Z' || eleLast.t == 'z' || eleLast.t == '$') {// elemento
																				// de
																				// calculo
																				// es
																				// el ultimo
					// System.out.println("Encontre z");
					// stop();
					if (DEBUG) {
						escribe("En e1last->t=='Z'|| e1last->t=='z' || e1last->t=='$' \n");
						stop();
					}

					/*
					 * CORRECCION: Aqui hay que unir el ultimo elemento con el primero Se usa el
					 * ultimo, se revisa el tipo de elemento. De ahi se decide que vector se une con
					 * el primer vector del primer elemento. Se supone que los vectores estaran
					 * corregidos por relatividad antes de esta operacion.
					 */

					if (eleLast.t == 'Z' || eleLast.t == 'z') {
						// System.out.println("Dentro de getLast ==Z");
						// stop();
						aviso("e1last->t=='Z'|| e1last->t=='z'");
						// Revisa el tipo del anterior
						int largo = listEleCal.size();
						int ind_last = largo - 1;// indice del ultimo
						// EleCalculo eleLasti = listEleCal.get(ind_last);
						if (ind_last - 1 >= 0) {
							EleCalculo elePrev = listEleCal.get(ind_last - 1);// elemento de calculo anterior al ultimo

							if (elePrev.t == 'C' || elePrev.t == 'c') {
								listEleCal.getLast().t = 'L';// lo calculara con linea
								listEleCal.getLast().xP1 = elePrev.xP4;// lo calculara con linea desde ultimo
								listEleCal.getLast().yP1 = elePrev.yP4;// lo calculara con linea
								listEleCal.getLast().xP2 = listEleCal.getFirst().xP1;// y lo une con primero
								listEleCal.getLast().yP2 = listEleCal.getFirst().yP1;
							}
							if (elePrev.t == 'Q' || elePrev.t == 'q') {
								listEleCal.getLast().t = 'L';// lo calculara con linea
								listEleCal.getLast().xP1 = elePrev.xP3;
								listEleCal.getLast().yP1 = elePrev.yP3;
								listEleCal.getLast().xP2 = listEleCal.getFirst().xP1;
								listEleCal.getLast().yP2 = listEleCal.getFirst().yP1;
							}
							if (elePrev.t == 'L' || elePrev.t == 'l') {
								listEleCal.getLast().t = 'L';// lo calculara con linea
								listEleCal.getLast().xP1 = elePrev.xP2;
								listEleCal.getLast().yP1 = elePrev.yP2;
								listEleCal.getLast().xP2 = listEleCal.getFirst().xP1;
								listEleCal.getLast().yP2 = listEleCal.getFirst().yP1;
								// incluye el tipo M
							}
							if (elePrev.t == 'H' || elePrev.t == 'h') {
								listEleCal.getLast().t = 'L';// lo calculara con linea
								listEleCal.getLast().xP1 = elePrev.xP2;
								listEleCal.getLast().yP1 = elePrev.yP2;
								listEleCal.getLast().xP2 = listEleCal.getFirst().xP1;
								listEleCal.getLast().yP2 = listEleCal.getFirst().yP1;
							}
							if (elePrev.t == 'V' || elePrev.t == 'v') {
								listEleCal.getLast().t = 'L';// lo calculara con linea
								listEleCal.getLast().xP1 = elePrev.xP2;
								listEleCal.getLast().yP1 = elePrev.yP2;
								listEleCal.getLast().xP2 = listEleCal.getFirst().xP1;
								listEleCal.getLast().yP2 = listEleCal.getFirst().yP1;
							}
						}
					}
					if (eleLast.t == '$') {
						listEleCal.removeLast();
					}

					/* Agrega a la lista de multples PATHS */
					// aviso("Pase por aqui 1\n");

					if (listPath.isEmpty())
						flagPathid = 0;
					Path pathx = new Path(flagPathid, flag_vis, listEleCal);
					flagPathid++;
					listPath.add(pathx);
					// aviso("========>termine de agregar path");

					listEleCal = null;//
					listEleCal = new LinkedList<EleCalculo>();// Reinicia una nueva lista de elementos
					// de calculo
					if (DEBUG) {
						escribe("terminando e1last->t=='Z'|| e1last->t=='z' || e1last->t=='$' %n");
						stop();
					}

				} // 'Z',''z' o '"'
				if (DEBUG && listPath != null && !listPath.isEmpty())
					System.out.format("t:%c\tx1:%f\ty1:%f\tx2:%f\ty2:%f\tx3:%f\ty3:%f\tx4:%f\ty4:%f%n",
							listPath.getLast().path.getLast().t, listPath.getLast().path.getLast().xP1,
							listPath.getLast().path.getLast().yP1, listPath.getLast().path.getLast().xP2,
							listPath.getLast().path.getLast().yP2, listPath.getLast().path.getLast().xP3,
							listPath.getLast().path.getLast().yP3, listPath.getLast().path.getLast().xP4,
							listPath.getLast().path.getLast().yP4);
				if (DEBUG)
					stop();

			}
			if (((i_px + 1) < no_PointYLetra) && (px[i_px + 1] != null))
				if (DEBUG) {
					System.out.format("tipo:%c tipo que sigue:%c \n", px[i_px].t, px[i_px + 1].t);
					stop();
				}
			if (DEBUG)
				System.out.println(22);
			i_px = i_px + 1;//
		} while (i_px < no_PointYLetra);
		// exit(0);
		// aviso("Sali de loop externo");

		Path pathx = null;
		int no_paths = listPath.size();
		int no_eleCal = 0;
		EleCalculo elex = null;
		for (int s = 0; s < no_paths; s++) {
			pathx = listPath.get(s);
			no_eleCal = pathx.path.size();
			for (int s1 = 0; s1 < no_eleCal; s1++) {
				elex = pathx.path.get(s1);
				if (elex.t == 'Z' || elex.t == 'z')
					elex.t = 'L';
			}
		} // corrige Z a L en todos los paths

		/************************* ESCRIBE RESULTADOS *******************************/
		/*
		 * for (int s = 0; s < no_paths; s++) { pathx = listPath.get(s); no_eleCal =
		 * pathx.path.size(); for (int s1 = 0; s1 < no_eleCal; s1++) { elex =
		 * pathx.path.get(s1); System.out.
		 * format("v:%d t:%c\tx1:%f\ty1:%f\tx2:%f\ty2:%f\tx3:%f\ty3:%f\tx4:%f\ty4:%f\n",
		 * pathx.flag_vis, elex.t, elex.xP1, elex.yP1, elex.xP2, elex.yP2, elex.xP3,
		 * elex.yP3, elex.xP4, elex.yP4);
		 * 
		 * } System.out.format("\n"); } aviso("Despues de procesar");
		 */

		/*
		 * 
		 * Genera un directorio dosnde se depositaran los archivos bezier_
		 * 
		 */
		// String directorio_bezier= directorio+ File.separator+ "objetos"+
		// File.separator+ "bezier_svg";

		String directorio_bezier = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "bezier_svg");

		java.nio.file.Path pathD = Paths.get(directorio_bezier);
		try {
			Files.createDirectories(pathD);
		} catch (IOException e11) {
			System.out.println("No pude crear directorio" + pathD);
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

		nombre_svg = directorio_bezier + File.separator + "bezier_" + nombre_svgP;

		String nombre_svgPN = "bezier_" + nombre_svgP;

		File salidaFile = new File(nombre_svg);
		String linea = null;
		Writer salidaOutput = null;
		try {
			salidaOutput = new BufferedWriter(new FileWriter(salidaFile));

			for (int s = 0; s < no_paths; s++) {
				pathx = listPath.get(s);
				no_eleCal = pathx.path.size();
				for (int s1 = 0; s1 < no_eleCal; s1++) {
					elex = pathx.path.get(s1);
					linea = String.format("%d %c %f %f %f %f %f %f %f %f%n", pathx.flag_vis, elex.t, elex.xP1, elex.yP1,
							elex.xP2, elex.yP2, elex.xP3, elex.yP3, elex.xP4, elex.yP4);
					salidaOutput.write(linea);

				}
				linea = String.format("%d %c %f %f %f %f %f %f %f %f%n", pathx.flag_vis, 'x', 0., 0., 0., 0., 0., 0.,
						0., 0.);
				salidaOutput.write(linea);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			escribe("No pude escribir salida");
			System.exit(0);
			// e.printStackTrace();
		}
		try {
			salidaOutput.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			escribe("No pude escribir salida");
			System.exit(0);

		}
		try {
			salidaOutput.close();
			System.out.format("Escribi datos en archivo: %s%n", nombre_svg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			escribe("No pude cerrar archivo de salida");
			System.exit(0);
		}

		// escribe("Termine. Saliendo... Chao..");
		return nombre_svgPN;
	}

	private static void escribe(String frase) {
		System.out.println(frase);
	}

	private static void stop() {
		escribe("presione enter..\n");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return;
	}

	private static void aviso(String av) {
		if (DEBUG) {
			System.out.format("%s\n", av);
			stop();
		}
	}

	private static void salgo() {
		escribe("Saliendo");
		System.exit(0);
	}

	private static void uso() {

		System.out.format("Uso:\nParcer_svg <simp_ws_nombre_svg>\no\nParcer_svg%n");
		System.out.format("Donde <simp_ws_nombre_svg>, es el nombre del" + " del archivo generado por Simplex_svg\n");
		System.exit(0);
		return;
	}
}
