package cortador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFrame;

public class BezierX {
	// static String directorio = "/home/ivan/work";
	// static String directorio = DirectorioTrabajo.directorio;

	static int MAX = 1000;

	public BezierX() {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// String directorio = "/home/ivan/work";
		String nombre_svgP = null;

		if (args.length > 1)
			uso();

		if (args.length == 1) {
			nombre_svgP = args[0];

		} else {
			// nom_archivo="simp_ws_drawing.svg";
			Utilidades.escribe("Usando default: bezier_simp_ws_drawing.svg ");
			nombre_svgP = "bezier_simp_ws_drawing.svg";
		}
		BezierX bezierX = new BezierX();
		nombre_svgP = bezierX.trabajo(nombre_svgP);
	}

	public String trabajo(String nombre_svgP) {

		// String directorio_bezier= directorio+ File.separator+ "objetos"+
		// File.separator+ "bezier_svg";
		// String nombre_svg = directorio_bezier + File.separator + nombre_svgP;

		String directorio_bezier = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "bezier_svg");
		String nombre_svg = directorio_bezier + File.separator + nombre_svgP;

		// String nombre_svg = directorio + File.separator + nombre_svgP;

		File file = new File(nombre_svg);
		if (!file.exists() || !file.isFile()) {
			System.out.println("No pude abrir archivo. " + nombre_svg + " Salgo...");
			System.exit(0);
		}

		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Utilidades.escribe("No pude abrir scanner");
			Utilidades.salgo();
			// e.printStackTrace();
		}
		Utilidades.escribe("Antes de leer");
		int vis = 0;
		char tipo = '=';
		double X1 = 0.;
		;
		double Y1 = 0.;
		;
		double X2 = 0.;
		;
		double Y2 = 0.;
		;
		double X3 = 0.;
		;
		double Y3 = 0.;
		double X4 = 0.;
		;
		double Y4 = 0.;
		LinkedList<EleCalculo> eleCalList = null;
		LinkedList<Path> pathList = null;
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
		// while (sc.hasNext()) {
		// System.out.println(sc.next());
		/*
		 * String line = sc.nextLine(); String[] tokens = line.split(" "); if
		 * (tokens.length != 11) { System.out.println("Tokens length "+tokens.length );
		 * Utilidades.escribe("tokens.length diferente de 10"); break; }
		 * System.out.printf("%d %c %n", Integer.getInteger(tokens[0]),
		 * tokens[1].charAt(0)); // Double.valueOf(tokens[2]),
		 * Double.valueOf(tokens[3]), Double.valueOf(tokens[4]), //
		 * Double.valueOf(tokens[5]), Double.valueOf(tokens[6]),
		 * Double.valueOf(tokens[7]), // Double.valueOf(tokens[8]),
		 * Double.valueOf(tokens[9]));
		 */
		// }
		sc.close();

		/* Escritura en pantalla de paths */
		/*
		 * for (int i = 0; i < pathList.size(); i++) { Path pathx = pathList.get(i); int
		 * visi = pathx.flag_vis; for (int j = 0; j < pathx.path.size(); j++) {
		 * EleCalculo elex = pathx.path.get(j); System.out.
		 * format("id: %d vis: %d t:%c x1:%f y1:%f x2:%f y2:%f x3:%f y3:%f x4:%f y4:%f%n"
		 * , pathx.id, pathx.flag_vis, elex.t, elex.xP1, elex.yP1, elex.xP2, elex.yP2,
		 * elex.xP3, elex.yP3, elex.xP4, elex.yP4);
		 * 
		 * }
		 * 
		 * }
		 */
		/*
		 * CALCULO DE EXTREMOS DE PATHS USANDO CURVAS DE BEZIER
		 * 
		 */

		Point res = new Point();
		Point res_prev = new Point();
		Point P1 = new Point();
		Point P2 = new Point();
		Point P3 = new Point();
		Point P4 = new Point();
		boolean esPrimero = true;
		double del = 1. / MAX;

		double xmin = 1000000.;
		double ymin = 1000000.;
		double xmax = -1000000.;
		double ymax = -1000000.;

		res.x = 0.;
		res.y = 0.;
		double tx = 0.;
		for (int i = 0; i < pathList.size(); i++) {
			Path pathx = pathList.get(i);
			if (pathx.flag_vis == 1) {// solo usa para normalizar los path visibles
				for (int j = 0; pathx.path!=null && j < pathx.path.size(); j++) {
					EleCalculo elex = pathx.path.get(j);
					char t_ele = elex.t;
					P1.x = elex.xP1;
					P1.y = elex.yP1;
					P2.x = elex.xP2;
					P2.y = elex.yP2;
					P3.x = elex.xP3;
					P3.y = elex.yP3;
					P4.x = elex.xP4;
					P4.y = elex.yP4;
					for (int ii = 0; ii < MAX + 1; ii++) {
						tx = del * ii;

						if (t_ele == 'c' || t_ele == 'C')
							res = Bezier.vectorBezierC(tx, res, P1, P2, P3, P4);
						if (t_ele == 'q' || t_ele == 'Q')
							res = Bezier.vectorBezierQ(tx, res, P1, P2, P3);
						if (t_ele == 'L' || t_ele == 'l')
							res = Bezier.vectorBezierL(tx, res, P1, P2);

						if (xmin > res.x)
							xmin = res.x;
						if (ymin > res.y)
							ymin = res.y;
						if (xmax < res.x)
							xmax = res.x;
						if (ymax < res.y)
							ymax = res.y;

						if (!esPrimero) {
							// DIBUJA SOLO SI HAY DOS PUNTOS CALCULADOS CON LINEA
						}
						res_prev.x = res.x;
						res_prev.y = res.y;
						esPrimero = false;
					}
				}

			}
		}
		double ancho = xmax - xmin;
		double alto = ymax - ymin;
		// System.out.format("minx:\t%f\tmaxx:\t%f\tminy:\t%f\tmaxy:\t%f%n", xmin, xmax,
		// ymin, ymax);
		// System.out.format("ancho;%f alto:%f%n", ancho, alto);

		/*
		 * Corrimiento de todas las curvas bezier a esquina superior izquierda Origen en
		 * (0,0) y normalizacion por ancho y alto SE TRUNCAN CURVAS DE BEZIER INVISIBLES
		 */

		for (int i = 0; i < pathList.size(); i++) {
			Path pathx = pathList.get(i);

			for (int j = 0;pathx.path!=null && j < pathx.path.size(); j++) {
				EleCalculo elex = pathx.path.get(j);
				elex.xP1 = (elex.xP1 - xmin) / ancho;
				elex.xP2 = (elex.xP2 - xmin) / ancho;
				elex.xP3 = (elex.xP3 - xmin) / ancho;
				elex.xP4 = (elex.xP4 - xmin) / ancho;
				elex.yP1 = (elex.yP1 - ymin) / alto;
				elex.yP2 = (elex.yP2 - ymin) / alto;
				elex.yP3 = (elex.yP3 - ymin) / alto;
				elex.yP4 = (elex.yP4 - ymin) / alto;
				if (pathx.flag_vis == 1 || pathx.flag_vis==0) {
					//Correccion SIEMPRE PRODUCE TRUNCADO
					if (elex.xP1 < 0.)
						elex.xP1 = 0.;
					if (elex.xP1 > 1.)
						elex.xP1 = 1.;
					if (elex.xP2 < 0.)
						elex.xP2 = 0.;
					if (elex.xP2 > 1.)
						elex.xP2 = 1.;
					if (elex.xP3 < 0.)
						elex.xP3 = 0.;
					if (elex.xP3 > 1.)
						elex.xP3 = 1.;
					if (elex.xP4 < 0.)
						elex.xP4 = 0.;
					if (elex.xP4 > 1.)
						elex.xP4 = 1.;
					if (elex.yP1 < 0.)
						elex.yP1 = 0.;
					if (elex.yP1 > 1.)
						elex.yP1 = 1.;
					if (elex.yP2 < 0.)
						elex.yP2 = 0.;
					if (elex.yP2 > 1.)
						elex.yP2 = 1.;
					if (elex.yP3 < 0.)
						elex.yP3 = 0.;
					if (elex.yP3 > 1.)
						elex.yP3 = 1.;
					if (elex.yP4 < 0.)
						elex.yP4 = 0.;
					if (elex.yP4 > 1.)
						elex.yP4 = 1.;
				}

			}
		}

		// Utilidades.escribe("Despues de procesar");

		/*
		 * 
		 * Genera un directorio dosnde se depositaran los archivos normal_
		 * 
		 */
		// String directorio_normal= directorio+ File.separator+ "objetos"+
		// File.separator+ "normal_svg";

		String directorio_normal = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "normal_svg");

		java.nio.file.Path pathD = Paths.get(directorio_normal);
		try {
			Files.createDirectories(pathD);
		} catch (IOException e11) {
			System.out.println("No pude crear directorio" + pathD);
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

		nombre_svg = directorio_normal + File.separator + "normal_" + nombre_svgP;

		String nombre_svgPN = "normal_" + nombre_svgP;
		File salidaFile = new File(nombre_svg);
		String linea = null;
		Writer salidaOutput = null;
		try {
			salidaOutput = new BufferedWriter(new FileWriter(salidaFile));
			for (int i = 0; i < pathList.size(); i++) {
				Path pathx = pathList.get(i);
				int visi = pathx.flag_vis;
				for (int j = 0;pathx.path!=null && j < pathx.path.size(); j++) {
					EleCalculo elex = pathx.path.get(j);
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
			Utilidades.escribe("No pude escribir salida");
			System.exit(0);
			// e.printStackTrace();
		}
		try {
			salidaOutput.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Utilidades.escribe("No pude escribir salida");
			System.exit(0);

		}
		try {
			salidaOutput.close();
			System.out.format("Escribi datos en archivo: %s%n", nombre_svg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Utilidades.escribe("No pude cerrar archivo de salida");
			System.exit(0);
		}
		/*
		 * //1. Create the frame. JFrame frame = new JFrame("FrameDemo");
		 * 
		 * //2. Optional: What happens when the frame closes?
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //frame.setSize(300,
		 * 300); //Component emptyLabel = null; //3. Create components and put them in
		 * the frame. //...create emptyLabel... //
		 * frame.getContentPane().add(emptyLabel, BorderLayout.CENTER); Label
		 * emptyLabel=new Label("Hola"); Dimension d =new Dimension(); d.setSize(300,
		 * 300); emptyLabel.setSize(d); frame.getContentPane().add(emptyLabel,
		 * BorderLayout.CENTER); //4. Size the frame. frame.pack();
		 * 
		 * //5. Show it. frame.setVisible(true);
		 * 
		 * BufferedImage bimg = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
		 * 
		 * //Got graphics out of it, i. e.
		 * 
		 * Graphics g2d = bimg.createGraphics(); g2d.setColor(new Color(100,0,0));
		 * g2d.drawLine(0, 0, 10, 10); // Performed all drawing operations on it, then
		 * drawn the scaled image on JPanel.
		 * 
		 * g2d.drawImage(bimg, 0, 0, emptyLabel.getWidth(), emptyLabel.getHeight(),
		 * null);
		 * 
		 */

		return nombre_svgPN;
	}

	private static void uso() {

		System.out.format("Uso:\nBezierX <bezier_simp_ws_nombre_svg>\no\nBezierX%n");
		System.out.format(
				"Donde <bezier_simp_ws_nombre_svg>, es el nombre del" + " del archivo generado por Parcer_svg\n");
		System.exit(0);
		return;
	}
}
