package cortador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Correa {
	private int anchoMax, alto;
	private int anchoPMin, altoPMin;
	private int anchoPMax, altoPMax;
	private int noPiezas;
	private int presicion;
	private String nombreArchivo = null;

	public Correa() {
	}

	public Correa(int alto, int anchoMax) {
		this.alto = alto;
		this.anchoMax = anchoMax;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setAncho(int anchoMax) {
		this.anchoMax = anchoMax;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getAnchoMax() {
		return anchoMax;
	}

	public int getAlto() {
		return alto;
	}

	public int getAnchoPMax() {
		return anchoPMax;
	}

	public int getAltoPMax() {
		return altoPMax;
	}

	public int getAnchoPMin() {
		return anchoPMin;
	}

	public int getAltoPMin() {
		return altoPMin;
	}

	public void setAnchoPMax(int anchoPMax) {
		this.anchoPMax = anchoPMax;
	}

	public void setAnchoPMin(int anchoPMin) {
		this.anchoPMin = anchoPMin;
	}

	public void setAnchoPMin() {
		this.anchoPMin = altoPMin;
	}

	public void setAltoPMax(int altoPMax) {
		this.altoPMax = altoPMax;
	}

	public void setAltoPMin(int altoPMin) {
		this.altoPMin = altoPMin;
	}

	public void setNoPiezas(int noPiezas) {
		this.noPiezas = noPiezas;
	}

	public int getNoPiezas() {
		return noPiezas;
	}

	public int getPresicion() {
		return presicion;
	}

	public void setPresicion(int presicion) {
		this.presicion = presicion;
	}

	public void leeDatosDeCorreaDesdeFile() {

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(nombreArchivo));

			if (scanner.hasNextInt())
				anchoMax = scanner.nextInt();
			// System.out.format("anchoMax:%d\n", anchoMax);
			if (scanner.hasNextInt())
				alto = scanner.nextInt();
			// System.out.format("alto:%d\n", alto);
			if (scanner.hasNextInt())
				noPiezas = scanner.nextInt();
			if (scanner.hasNextInt())
				anchoPMin = scanner.nextInt();
			if (scanner.hasNextInt())
				anchoPMax = scanner.nextInt();
			if (scanner.hasNextInt())
				altoPMin = scanner.nextInt();
			if (scanner.hasNextInt())
				altoPMax = scanner.nextInt();
			if (scanner.hasNextInt())
				presicion = scanner.nextInt();

			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("No pude leer datos de correa desde archivo. Salgo...");
			System.exit(0);
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

}
