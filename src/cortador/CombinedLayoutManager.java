package cortador;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class CombinedLayoutManager extends JFrame implements ActionListener {
	JButton buttonOK = null;
	JButton buttonCancel = null;
	JButton buttonExit = null;
	MyThread myThread = null;
	JTextArea myTextArea = null;
	String myMensaje = null;

	public static void main(String[] args) {
		CombinedLayoutManager e = new CombinedLayoutManager();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				e.generaCombinedLayoutManager();
			}
		});

	}

	public void generaCombinedLayoutManager() {

		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(getHeader(), BorderLayout.NORTH);
		pane.add(getTextArea(), BorderLayout.CENTER);
		pane.add(getButtonPanel(), BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		pack();
		setVisible(true);
	}

	protected JComponent getHeader() {
		JLabel label = new JLabel("Embedded Layout Manager Test", JLabel.CENTER);
		label.setFont(new Font("Courier", Font.BOLD, 24));
		return label;
	}

	protected JComponent getTextArea() {
		myTextArea = new JTextArea(10, 10);
		return myTextArea;
	}

	protected JComponent getButtonPanel() {
		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(1, 3, 10, 0));
		buttonOK = new JButton("Ok");
		buttonCancel = new JButton("Cancel");
		buttonExit = new JButton("Exit");
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		buttonExit.addActionListener(this);
		inner.add(buttonOK);
		inner.add(buttonCancel);
		inner.add(buttonExit);
		return inner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == buttonOK) {
			System.out.println("boton1 presionado");
			myThread = new MyThread();
			myThread.start();
			System.out.println("Thread id: " + myThread.getId());
			try {
				wait(myThread.getId());
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1);
			}
			escribe("Trabajo de Thrad: " + myMensaje);
			trabaja();
		} else if (e.getSource() == buttonCancel) {
			if (myThread.isAlive()) {
				System.out.println("myThread esta viva");
				myThread.interrupt();

			} else {
				escribe("Thread esta muerta");
			}
			System.out.println("boton2 presionado");
		} else if (e.getSource() == buttonExit) {
			System.exit(0);
		}

	}

	private void trabaja() {
		try {
			wait(myThread.getId());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		}
		escribe("Trabajo de Thrad: " + myMensaje);	
	}

	private class MyThread extends Thread {

		public MyThread() {
		}

		public void run() {

			System.out.println("Hola. Estoy en Thread");
			int i = 0;
			while (i < 10) {
				System.out.println("i :" + i);
				if (myTextArea != null) {
					myTextArea.setText("i: " + i);
					myMensaje = new String("i: " + i);
				}
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println(e);
				}
				i++;
			}
		}
	}

	private void escribe(String str) {
		System.out.println(str);
	}
}
