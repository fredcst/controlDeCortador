package cortador;

import java.io.IOException;

public class Utilidades {
	public static void escribe(String frase) {
		System.out.println(frase);
	}

	public static void stop() {
		escribe("presione enter..\n");
		try {
			 System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return;
	}

	public static void aviso(boolean DEBUG,String av) {
		if (DEBUG) {
			System.out.format("%s\n", av);
			stop();
		}
	}

	public static void salgo() {
		escribe("Saliendo");
		System.exit(0);
	}
	
	public static int getRandomNumberInRange(int min, int max) {

		if (min > max) {
			
			throw new IllegalArgumentException("max must be greater or equal than min");
		}

		return (int) (Math.random() * ((max - min) + 1)) + min;
	}
	public static double getRandomDoubleNumberInRange(double min, double max) {

		if (min > max) {
			throw new IllegalArgumentException("max must be greater or equal than min");
		}

		return  (  Math.random()*(max - min) + min);
	}
	public static void uso() {

		System.out.format("Uso:\nParcer_svg <simp_ws_nombre_svg>\no\nParcer_svg\n");
		System.out.format("Donde <simp_ws_nombre_svg>, es el nombre del" + " del archivo generado por Simplex_svg\n");
		System.exit(0);
		return;
	}
}
