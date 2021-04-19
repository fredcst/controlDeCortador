package cortador;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.util.TreeSet;

import javax.swing.*;



public class DibujoDeCortador extends JPanel {
	private static final int RECT_X = 30;
	private static final int RECT_Y = RECT_X;
	private static final int RECT_WIDTH = 1000;
	private static final int RECT_HEIGHT = 100;
	private static int t_pres = 100; // No de valores de t usados en calculo de curva de Bezier
	private boolean statusAcercamientoPiezas = false;
	private boolean statusAcercamientoPiezas1 = false;
	// SacoDePiezas sacoDePiezas = null;
	SacoBasadoEnPrototipos mySacoBasadoEnPrototipos = null;
	private int xDeCorte = 0;
	// SacoDePiezas sacoDePiezasB = null;
	// CalculosCpy calculador = null;
	private int estadoProgreso;
	private final int ESCALAX = 100;
	private final int ESCALAY = 100;
	private DibujoDeCortador myDibujoDeCortador = null;
	private PiezaH[] mySaco = null;
	private boolean estatusDespligueInicialDePiezas=true;

	// SacoDePiezas.MuestrarioDeSaco muestrarioDeSaco = sacoDePiezas.new
	// MuestrarioDeSaco();
	public DibujoDeCortador(int noPiezas, double anchoMin, double anchoMax, double altoMin, double altoMax,
			double delta, boolean acel) {
		mySacoBasadoEnPrototipos = new SacoBasadoEnPrototipos();
		mySacoBasadoEnPrototipos.setMyDibujoDeCortador(this);
		mySacoBasadoEnPrototipos.trabajoCompleto(noPiezas, anchoMin, anchoMax, altoMin, altoMax, delta, acel);
		mySaco = mySacoBasadoEnPrototipos.getSaco();
		
	}

	public int getRECT_X() {
		return RECT_X;
	}

	public int getRECT_Y() {
		return RECT_Y;
	}

	public static int getRECT_WIDTH() {
		return RECT_WIDTH;
	}

	public static int getRECT_HEIGHT() {
		return RECT_HEIGHT;
	}

	public void setEstatusAcercamientoDePiezas(boolean est) {
		statusAcercamientoPiezas = est;
	}

	public void setEstatusAcercamientoDePiezas1(boolean est) {
		statusAcercamientoPiezas1 = est;
	}

	public void escribeEstadoDeProgreso(int stadoProg) {

		estadoProgreso = stadoProg;
		repaint();
	}

	/**
	 * Rutina que dibuja un panel con 4 bandas de ancho RECT_HEIGHT y RECT_WIDTH ,
	 * donde se dibujaran las piezasH en diferentes estados de evolucion La escala
	 * fisica de las piezas es ESCALAXxESCALAY. Esto significa que el maxima
	 * dimension X (Y) de la pieza es ESCALX (ESCALY) pixeles. Esto corresponde al
	 * alto del agujero de la correa de corte. Todas las otras piezas tiene anchos y
	 * altos que son fracciones de estas dimensiones.S ESCALAX y ESCALAY fijan las
	 * dimensiones de las piezasH.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw the rectangle here
		Color cOld = g.getColor();
		Color cNew = new Color(0, 0, 0);
		g.setColor(cNew);
		g.drawRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
		g.drawRect(RECT_X, RECT_Y + RECT_HEIGHT, RECT_WIDTH, RECT_HEIGHT);
		g.drawRect(RECT_X, RECT_Y + 2 * RECT_HEIGHT, RECT_WIDTH, RECT_HEIGHT);
		g.drawRect(RECT_X, RECT_Y + 3 * RECT_HEIGHT, RECT_WIDTH, RECT_HEIGHT);
		// g.drawRect(RECT_X, RECT_Y + 4 * RECT_HEIGHT, RECT_WIDTH, RECT_HEIGHT);
		
		for (int i = 0; i < mySaco.length; i++) {
			if(estatusDespligueInicialDePiezas) {
			Color cp = mySaco[i].getColorDePieza();
			g.setColor(cp);

			if (mySaco[i] != null && mySaco[i].getRect0() != null) {

				mySaco[i].dibuja_pieza(g, t_pres, mySaco[i].getRect0(), ESCALAX, ESCALAY, RECT_X, RECT_Y);
			} else {

			}

			if (mySaco[i] != null && mySaco[i].getRectEjeX() != null) {

				mySaco[i].dibuja_pieza(g, t_pres, mySaco[i].getRectEjeX(), ESCALAX, ESCALAY, RECT_X,
						RECT_HEIGHT + RECT_Y);
			} else {

			}

			if (mySaco[i] != null && mySaco[i].getRectCols() != null) {

				mySaco[i].dibuja_pieza(g, t_pres, mySaco[i].getRectCols(), ESCALAX, ESCALAY, RECT_X,
						2 * RECT_HEIGHT + RECT_Y);
			} else {

			}
			
			if (mySaco[i] != null && mySaco[i].getRectF() != null) {

				mySaco[i].dibuja_pieza(g, t_pres, mySaco[i].getRectF(), ESCALAX, ESCALAY, RECT_X,
						3 * RECT_HEIGHT + RECT_Y);
			} else {

			}
			}
		}

		if (!statusAcercamientoPiezas) {

			g.drawString("Calculando: " + String.format("%d por 100 hecho", estadoProgreso), RECT_X + RECT_WIDTH / 2,
					3 * RECT_HEIGHT + 3 * RECT_Y);

		}

		g.drawString("0", RECT_X, 2 * RECT_HEIGHT + RECT_Y - 10);
		g.drawString("100", RECT_X + ESCALAX - 10, 2 * RECT_HEIGHT + RECT_Y - 10);
		g.drawLine(RECT_X + ESCALAX, 2 * RECT_HEIGHT + RECT_Y, RECT_X + ESCALAX, 2 * RECT_HEIGHT + RECT_Y - 10);
		g.drawString("100", 2, 2 * RECT_HEIGHT + RECT_Y + RECT_HEIGHT);
		xDeCorte = (int) (ESCALAX * mySacoBasadoEnPrototipos.getxDeCorteCols());
		g.setColor(cOld);
		g.drawString("XDeCorte: " + String.format("%d ", xDeCorte), RECT_X + RECT_WIDTH / 2,
				2 * RECT_HEIGHT + 3 * RECT_Y);
		g.drawLine(RECT_X + xDeCorte, 2 * RECT_HEIGHT + RECT_Y - 10, RECT_X + xDeCorte,
				2 * RECT_HEIGHT + RECT_Y + RECT_HEIGHT + 10);

		xDeCorte = (int) (ESCALAX * mySacoBasadoEnPrototipos.getxDeCorteF());
		if (statusAcercamientoPiezas) {
			g.drawString("XDeCorte: " + String.format("%d ", xDeCorte), RECT_X + RECT_WIDTH / 2,
					3 * RECT_HEIGHT + 3 * RECT_Y);
			g.drawLine(RECT_X + xDeCorte, 3 * RECT_HEIGHT + RECT_Y - 10, RECT_X + xDeCorte,
					3 * RECT_HEIGHT + RECT_Y + RECT_HEIGHT + 10);
		}

	}

	public Dimension getPreferredSize() {
		// so that our GUI is big enough
		return new Dimension(RECT_WIDTH + 2 * RECT_X, 4 * RECT_HEIGHT + 2 * RECT_Y);
	}

	// create the GUI explicitly on the Swing event thread
	public void createAndShowGui() {
		
		DibujoDeCortador mainPanel = myDibujoDeCortador;
		JFrame frame;
		frame = new JFrame("Cortador");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public DibujoDeCortador getMyDibujoDeCortador() {
		return myDibujoDeCortador;
	}

	public void setMyDibujoDeCortador(DibujoDeCortador myDibujoDeCortador) {
		this.myDibujoDeCortador = myDibujoDeCortador;
	}

	public static void main(String[] args) {
		DibujoDeCortador myDibujoDeCortadorL = new DibujoDeCortador(10, 0.001, 0.4, 0.001, 0.4, 0.01, false);
		myDibujoDeCortadorL.setMyDibujoDeCortador(myDibujoDeCortadorL);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				myDibujoDeCortadorL.createAndShowGui();
			}
		});
	}

	public SacoBasadoEnPrototipos getMySacoBasadoEnPrototipos() {
		return mySacoBasadoEnPrototipos;
	}

	public void setMySacoBasadoEnPrototipos(SacoBasadoEnPrototipos mySacoBasadoEnPrototipos) {
		this.mySacoBasadoEnPrototipos = mySacoBasadoEnPrototipos;
	}

	public int getESCALAX() {
		return ESCALAX;
	}

	public int getESCALAY() {
		return ESCALAY;
	}

	public void setEstatusDespligueInicialDePiezas(boolean b) {
		estatusDespligueInicialDePiezas=b;
		// TODO Auto-generated method stub
		
	}
}
