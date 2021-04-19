package cortador;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlDeCortadorH {
	JFrame f;
	private int noPiezas = 50;
	private int maxTam = 40;
	private int minTam = 1;
	private double maxTamD = 0.40;
	private double minTamD = 0.01;
	private DibujoDeCortador dibujoDeCortador = null;

	public ControlDeCortadorH() {
	}

	public static void main(String[] args) {
		ControlDeCortadorH controlDeCortadorH = new ControlDeCortadorH();
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				controlDeCortadorH.createAndShowGuiH();
			}
		};
		EventQueue.invokeLater(runner);

	}

	private void createAndShowGuiH() {

		f = new JFrame("Cortador");
		// f.setLocationRelativeTo(null);
		JButton b1 = new JButton("Créer courbes de Bézier");
		JButton b2 = new JButton("Montrer Dessins");
		JButton b3 = new JButton("Ranger les pièces");
		JButton b4 = new JButton("Sortir");
		f.getContentPane().setLayout(new GridLayout(0, 1, 1, 1));
		// b1.setBounds(50, 100, 250, 30);
		// b2.setBounds(50, 135, 250, 30);
		// b3.setBounds(50, 170, 250, 30);
		// b4.setBounds(50, 210, 250, 30);
		JPanel panelNoPiezas = new JPanel();
		panelNoPiezas.setLayout(new GridLayout(1, 0, 1, 1));
		JLabel labelNoPiezas = new JLabel("Nº de pièces");
		panelNoPiezas.add(labelNoPiezas);
		SpinnerModel value = new SpinnerNumberModel(50, // initial value
				0, // minimum value
				80, // maximum value
				1); // step
		JSpinner spinner = new JSpinner(value);

		panelNoPiezas.add(spinner);

		JPanel panelMaxTam = new JPanel();
		panelMaxTam.setLayout(new GridLayout(1, 0, 1, 1));
		JLabel labelMaxTam = new JLabel("Dim. Max");
		panelMaxTam.add(labelMaxTam);
		SpinnerModel valueMaxTam = new SpinnerNumberModel(40, // initial value
				1, // minimum value
				40, // maximum value
				1); // step
		JSpinner spinnerMaxTam = new JSpinner(valueMaxTam);
		panelMaxTam.add(spinnerMaxTam);

		JPanel panelMinTam = new JPanel();
		panelMinTam.setLayout(new GridLayout(1, 0, 1, 1));
		JLabel labelMinTam = new JLabel("Dim. Min");
		panelMinTam.add(labelMinTam);
		SpinnerModel valueMinTam = new SpinnerNumberModel(1, // initial value
				1, // minimum value
				5, // maximum value
				1); // step
		JSpinner spinnerMinTam = new JSpinner(valueMinTam);
		panelMinTam.add(spinnerMinTam);

		JPanel panelRepiteProtos = new JPanel();
		panelRepiteProtos.setLayout(new GridLayout(1, 0, 1, 1));
		JLabel repiteProtos = new JLabel("Repeter dessins");
		//panelRepiteProtos.add(repiteProtos);
		//JCheckBox chBox = new JCheckBox();
		//panelRepiteProtos.add(chBox);

		//chBox.setEnabled(false);

		MyActionListener1 myActionListener1 = new MyActionListener1(b1);
		MyActionListener2 myActionListener2 = new MyActionListener2(b2);
		MyActionListener3 myActionListener3 = new MyActionListener3(b3);
		MyActionListener4 myActionListener4 = new MyActionListener4(b4);
		MyChangeListener myChangeListener = new MyChangeListener();
		MyChangeListenerMaxTam myChangeListenerMaxTam = new MyChangeListenerMaxTam();
		MyChangeListenerMinTam myChangeListenerMinTam = new MyChangeListenerMinTam();
		MyCheckboxAction myCheckAction = new MyCheckboxAction("Bonjour");
		spinner.addChangeListener(myChangeListener);
		spinnerMaxTam.addChangeListener(myChangeListenerMaxTam);
		spinnerMinTam.addChangeListener(myChangeListenerMinTam);
		b1.addActionListener(myActionListener1);
		b2.addActionListener(myActionListener2);
		b3.addActionListener(myActionListener3);
		b4.addActionListener(myActionListener4);
		//chBox.addActionListener(myCheckAction);
		f.add(b1);
		f.add(b2);
		f.add(panelNoPiezas);
		f.add(panelMaxTam);
		f.add(panelMinTam);
		//f.add(panelRepiteProtos);
		f.add(b3);
		f.add(b4);
		f.setSize(350, 250);

		f.pack();
		f.setLayout(null);
		f.setVisible(true);

	}

	class MyActionListener1 implements ActionListener {
		JButton but;

		public MyActionListener1(JButton button) {
			but = button;

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getSource() == but) {

				BezierFull bezierFull = new BezierFull();
				// bezierFull.trabajo(nombre_svgP);

				bezierFull.recorreDirectorio_inkscape_svg_TrabajandoBkg();

			}
		}
	}

	class MyActionListener2 implements ActionListener {
		JButton but;

		public MyActionListener2(JButton button) {
			but = button;

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getSource() == but) {

				SacoDePrototipos sacoDePrototipos = new SacoDePrototipos(1);
				sacoDePrototipos.leeSacoDesdeFile_Bezier_Normal();

				
				cortador.SacoDePrototipos.MuestrarioDeSaco muestrarioDeSaco = sacoDePrototipos.new MuestrarioDeSaco();

				muestrarioDeSaco.muestra_protos();
				JDialog d = new JDialog(f, "DESSINS (PROTOTYPES)");
				d.setLocationRelativeTo(null);
				d.setSize(muestrarioDeSaco.getRECT_WIDTH() + muestrarioDeSaco.getRECT_X(),
						muestrarioDeSaco.getRECT_HEIGHT() + muestrarioDeSaco.getRECT_Y());
				d.add(muestrarioDeSaco);
				d.setVisible(true);

			}
		}
	}

	class MyActionListener3 implements ActionListener {
		JButton but;

		public MyActionListener3(JButton button) {
			but = button;

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getSource() == but) {

				dibujoDeCortador = new DibujoDeCortador(noPiezas, minTamD, maxTamD, minTamD, maxTamD, 0.005, true);

				JDialog d = new JDialog(f, "CORTADOR");
				d.setLocationRelativeTo(null);
				d.add(dibujoDeCortador);

				d.setSize(DibujoDeCortador.getRECT_WIDTH() + 2 * dibujoDeCortador.getRECT_X(),
						4 * DibujoDeCortador.getRECT_HEIGHT() + 3 * dibujoDeCortador.getRECT_Y());

				// set visibility of dialog
				d.setVisible(true);

				d.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						System.out.println("window closed");
					}

					public void windowClosing(WindowEvent e) {
						System.out.println("window closing");
						// d.dispose();
						dibujoDeCortador.getMySacoBasadoEnPrototipos().stopCalculoLargoPar();
						// Termina calculos
					}
				});

				// esquema.createAndShowGui();
				// esquema.muestraPane(f) ;
			}
		}
	}

	class MyActionListener4 implements ActionListener {
		JButton but;

		public MyActionListener4(JButton button) {
			but = button;

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getSource() == but) {
				System.exit(0);

			}
		}
	}

	class MyChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			// TODO Auto-generated method stub
			noPiezas = (int) ((JSpinner) arg0.getSource()).getValue();
			// System.out.println("noPiezas; "+noPiezas);
		}
	}

	class MyChangeListenerMaxTam implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			// TODO Auto-generated method stub
			maxTam = (int) ((JSpinner) arg0.getSource()).getValue();
			int ecalx=dibujoDeCortador.getESCALAX();
			maxTamD=maxTam/((double)ecalx);
		
		}
	}

	class MyChangeListenerMinTam implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			// TODO Auto-generated method stub
			minTam = (int) ((JSpinner) arg0.getSource()).getValue();
			int ecalx=dibujoDeCortador.getESCALAX();
			minTamD=minTam/((double)ecalx);
			// System.out.println("minTam; " + maxTam);
		}
	}

	@SuppressWarnings("serial")
	class MyCheckboxAction extends AbstractAction {
		public MyCheckboxAction(String text) {
			super(text);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBox cbLog = (JCheckBox) e.getSource();
			if (cbLog.isSelected()) {
			} else {
			}
		}
	}

}
