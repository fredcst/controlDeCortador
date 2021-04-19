package cortador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SacoDePrototipos extends SacoDePiezasH {
	private MuestrarioDeSaco muestrarioDeSaco=null;;
	public SacoDePrototipos(int noPiezasH) {
		super(noPiezasH);
		// TODO Auto-generated constructor stub
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
					saco[i].setAncho(1.);
					saco[i].setAlto(1.);
					saco[i].setPosX(0.);
					saco[i].setPosY(0.);
					//saco[i].escalaCurbasDeBezier(1.,1.);//MALO
					saco[i].setRect0(new RectanguloH(0.,0.,saco[i].getAncho(),saco[i].getAlto()));
					i++;
				}

			}
		}

	}

	@SuppressWarnings("serial")
	class MuestrarioDeSaco extends JPanel implements ActionListener {
		int RECT_X = 20;
		int RECT_Y = 20;
		int RECT_HEIGHT = 200;
		int RECT_WIDTH = 400;
		private double ANCHO_WIN = 100;
		private double ALTO_WIN = 100;
		private JButton button = null;
		private int indPieza = 0;
		
		private final int ESCALAX = 100; //ESCALA DE DIMENSIONES DE PIEZAS 
		private final int ESCALAY = 100;

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
			// JLabel label = new JLabel("Modelos de Piezas");
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
				saco[i_fig].dibuja_pieza(g, 100, saco[i_fig].getRect0(),ESCALAX, ESCALAY, 100, 10);
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

		
		//Reemplazada por funcion en clase PiezaH
		public void dibuja_pieza(Graphics g, PiezaH pz, int presicion, int escalax, int escalay, int xOrigen,
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
			int no_paths = pz.pathList.size();
			for (int i = 0; i < no_paths; i++) {
				pathX = pz.pathList.get(i);
				if (pathX.flag_vis == 1) {
					for (int j = 0; pathX.path != null && j < pathX.path.size(); j++) {
						elx = pathX.path.get(j);

						P1.x = elx.xP1 * pz.getAncho() * escalax + pz.getPosXP0() * escalax + xOrigen;
						P1.y = elx.yP1 * pz.getAlto() * escalay + pz.getPosYP0() * escalay + yOrigen;
						P2.x = elx.xP2 * pz.getAncho() * escalax + pz.getPosXP0() * escalax + xOrigen;
						P2.y = elx.yP2 * pz.getAlto() * escalay + pz.getPosYP0() * escalay + yOrigen;
						P3.x = elx.xP3 * pz.getAncho() * escalax + pz.getPosXP0() * escalax + xOrigen;
						P3.y = elx.yP3 * pz.getAlto() * escalay + pz.getPosYP0() * escalay + yOrigen;
						P4.x = elx.xP4 * pz.getAncho() * escalax + pz.getPosXP0() * escalax + xOrigen;
						P4.y = elx.yP4 * pz.getAlto() * escalay + pz.getPosYP0() * escalay + yOrigen;
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

		
		
		
		@Override
		public Dimension getPreferredSize() {
			// so that our GUI is big enough
			return new Dimension(RECT_WIDTH + 2 * RECT_X, 3 * RECT_HEIGHT + 2 * RECT_Y);
		}
	}
	MuestrarioDeSaco getMuestrario() {
		return muestrarioDeSaco;

	}
	public void generaMuestrario() {
		 muestrarioDeSaco = new MuestrarioDeSaco();
	}
	
	public static void main(String[] args) {

		SacoDePrototipos sacoDePrototipos = new SacoDePrototipos(1);
		sacoDePrototipos.leeSacoDesdeFile_Bezier_Normal();
		sacoDePrototipos.generaMuestrario() ;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				sacoDePrototipos.getMuestrario().muestra_piezas_en_pane(); 
			}
		});

	}
}
