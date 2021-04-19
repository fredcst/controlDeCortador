package cortador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class CurvasDeBezierDeObjeto {
	LinkedList<Path> pathList = null;
    String nombre = null;

	public CurvasDeBezierDeObjeto() {
	}

	public static void main(String[] args) {

		CurvasDeBezierDeObjeto curvasDeBezierDeObjeto = new CurvasDeBezierDeObjeto();
		curvasDeBezierDeObjeto.obtiene_curvas_de_bezier_normal("normal_bezier_simp_ws_drawing-7.svg");
		curvasDeBezierDeObjeto.muestra_en_consola_curvas_de_bezier();

	}

	public void obtiene_curvas_de_bezier_normal(String nombre_svgP) {// nombre_svg es el nombre del
		// archivo en
		// directorio : ./objetos/normal_svg

		String s = nombre_svgP;
		String delims = "[_.]+"; // use + to treat consecutive delims as one;
									// omit to treat consecutive delims separately
		String[] tokens = s.split(delims);
		// for(String sx:tokens) {System.out.println(sx);}

		nombre = tokens[(tokens.length - 2)];
		//System.out.println(nombre);

		//String directorio = DirectorioTrabajo.directorio;

		//String directorio_normal = DirectorioBase.getNombreFileFromResources("objetos" + File.separator + "normal_svg"); 
				
				
			//	directorio + File.separator + "objetos" + File.separator + "normal_svg";

		//String nombre_svg = directorio_normal + File.separator + nombre_svgP;

		//String directorio_normal = null;
		//	DirectorioBase dbase = new DirectorioBase();
			//File directorio_normalD = null;
		String 	directorio_normal =DirectorioBase.getNombreFileFromResources("cortador"+File.separator+"objetos" + File.separator + "normal_svg"); 
			//directorio_normal =DirectorioBase.getNombreFileFromResources("objetos" + File.separator + "normal_svg"); 
		//	directorio_normalD = new File(directorio_normal);
		String nombre_svg = directorio_normal + File.separator + nombre_svgP;
		
// String nombre_svg = directorio + File.separator + nombre_svgP;

		File file = new File(nombre_svg);
		if (!file.exists() || !file.isFile()) {
			System.out.println("No pude abrir archivo. " + nombre_svg + " Salgo...");
			System.exit(0);
		}

		Scanner sc = null;
		try {
			sc = new Scanner(file);

// Utilidades.escribe("Antes de leer");
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

				/*
				 * System.out.format("vis: %d ", vis); System.out.format("t: %c ", tipo);
				 * System.out.format("X1: %f ", X1); System.out.format("Y1: %f ", Y1);
				 * System.out.format("X2: %f ", X2); System.out.format("Y2: %f ", Y2);
				 * System.out.format("X3: %f ", X3); System.out.format("Y3: %f ", Y3);
				 * System.out.format("X4: %f ", X4); System.out.format("Y4: %f %n", Y4);
				 */
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

	private void muestra_en_consola_curvas_de_bezier() {
		if (pathList != null) {
			int tamano = pathList.size();
			cortador.Path pt = null;
			int vis = 0;
			int id = 0;
			int tam_path = 0;
			LinkedList<EleCalculo> path = null;
			EleCalculo ex = null;
			for (int i = 0; i < tamano; i++) {
				pt = pathList.get(i);
				vis = pt.flag_vis;
				id = pt.id;
				path = pt.path;
				tam_path = path.size();
				for (int j = 0; j < tam_path; j++) {
					ex = path.get(j);
					System.out.format("vis:%d id:%d t:%c xP1:%f yP1:%f xP2:%f yP2:%f xP3:%f yP3:%f xP4:%f yP4:%f%n",
							vis, id, ex.t, ex.xP1, ex.yP1, ex.xP2, ex.yP2, ex.xP3, ex.yP3, ex.xP4, ex.yP4);
				}
			}

		} else {
			Utilidades.escribe("No hay curvas de Bezier");
			Utilidades.salgo();
		}
		return;
	}
}
