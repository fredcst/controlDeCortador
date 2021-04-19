package cortador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.swing.SwingWorker;

public class BezierFull {
	
	public BezierFull() {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Comence BezierFull");
		String nombre_svgP = null;
		System.out.println("No. de argumentos: " + args.length);
	
		if (args.length >= 2)
			uso();
		if (args.length == 0) {
			System.out.println("Usando archivo default: drawing.svg");

			nombre_svgP = "drawing.svg";

		}
		/*
		 * String s1="Sachin "; String s2="Tendulkar"; String s3=s1.concat(s2);
		 */
		if (args.length == 1) {
			nombre_svgP = args[0];
			if (Objects.equals(nombre_svgP, "-h"))
				uso();
		}
		// nombre_svg = directorio + File.separator+nombre_svgP;
		// nom_pre = null;

		BezierFull bezierFull = new BezierFull();
		// bezierFull.trabajo(nombre_svgP);

		bezierFull.recorreDirectorio_inkscape_svg_Trabajando();

		System.out.println("Termine BezierFull");

	}

	public void recorreDirectorio_inkscape_svg_TrabajandoBkg() {
		LecturaDePrototipos myLecturaDePrototipos = new LecturaDePrototipos();
		myLecturaDePrototipos.execute();
	}

	public void recorreDirectorio_inkscape_svg_Trabajando() {
		// System.out.println("recorreDirectorio_inkscape_svg_Trabajando");
		// String directorio_inkscape=directorio+
		// File.separator+"objetos"+File.separator+"inkscape_svg";

		String directorio_inkscape = null;
		// DirectorioBase dbase = new DirectorioBase();
		File directorio_inkscapeD = null;

		// final File directorio_normalD = new File(directorio_normal);
		directorio_inkscape = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "inkscape_svg");
		directorio_inkscapeD = new File(directorio_inkscape);
		//
		// System.out.println("directorio Inkscape: "+directorio_inkscape);
		// System.exit(0);
		// directorio_inkscapeD = new File(directorio_inkscape);
		/*
		 * Recorre el directorio objetos/inkscape_svg y trabaja todos los aechivos que
		 * estan en el
		 */
		// System.out.println("no.files :"+directorio_inkscapeD.listFiles().length);
		for (final File fileEntry : directorio_inkscapeD.listFiles()) {
			if (fileEntry.isDirectory()) {
				// listFilesForFolder(fileEntry);
			} else {
				String nombre_svgP = fileEntry.getName();// obtiene nombre despues de
															// ultimo: / del path del archivo
				// System.out.println("nombre_svgP: "+nombre_svgP);
				trabajo(nombre_svgP); // trabaja archivo
			}
		}

	}

	public void trabajo(String nombre_svgP) {

		Simplex_svg simplex_svg = new Simplex_svg();
		nombre_svgP = simplex_svg.trabajo(nombre_svgP);// simplifica archivo inkscape
		System.out.println(nombre_svgP);
		Parcer_svg parcer_svg = new Parcer_svg();
		nombre_svgP = parcer_svg.trabajo(nombre_svgP);// genera curvas de bezier
		System.out.println(nombre_svgP);
		BezierX bezierX = new BezierX();
		nombre_svgP = bezierX.trabajo(nombre_svgP);// genera curvas de bezier normalizadas
		System.out.println(nombre_svgP);

	}

	private void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				// System.out.println(fileEntry.getName());
			}
		}

	}

	private static void uso() {

		System.out.format("Uso:\nBezierFull <nombre_svg>\no\nsimplexC_svg\n");
		System.out.format("Donde <nombre_svg>, es el nombre del archivo generado por inkscqpe\n");
		System.exit(0);
		return;
	}

	private class LecturaDePrototipos extends SwingWorker<Object, Integer> {

		public LecturaDePrototipos() {

		}

		@Override
		protected Object doInBackground() throws Exception {
			recorreDirectorio_inkscape_svg_Trabajando();
			return null;
		}

		protected void process(List<Integer> chunk) {
		}

		protected void done() {

			System.out.println("Termine  de Leer prototipos...");

		}

	}

}
