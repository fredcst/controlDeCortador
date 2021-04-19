package cortador;

//Java program to illustrate  
//working of SwingWorker 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

public class SwingWorkerSample implements PropertyChangeListener {

	private static JLabel statusLabel;
	private static JFrame mainFrame;
	static SwingWorker<String, Integer> sw1;

	public void swingWorkerSample() {
		mainFrame = new JFrame("Swing Worker");
		mainFrame.setSize(400, 400);
		mainFrame.setLayout(new GridLayout(2, 1));

		mainFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});

		statusLabel = new JLabel("Not Completed", JLabel.CENTER);
		mainFrame.add(statusLabel);

		JButton btn = new JButton("Start counter");
		btn.setPreferredSize(new Dimension(5, 5));

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button clicked, thread started");
				startThread();
			}

		});

		mainFrame.add(btn);
		// mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private void startThread() {

		sw1 = new SwingWorker<String, Integer>() {

			@Override
			protected String doInBackground() throws Exception {
				// define what thread will do here
				for (int i = 10; i >= 0; i--) {
					Thread.sleep(100);
					System.out.println("Value in thread : " + i);
					publish(i);
					// publish("Hola: "+i);
				}

				String res = "Finished Execution";
				return res;
			}

			@Override
			protected void process(List chunks) {
				// define what the event dispatch thread
				// will do with the intermediate results received
				// while the thread is executing
				int val = (int) chunks.get(chunks.size() - 1);

				statusLabel.setText(String.valueOf(val));
			}

			@Override
			protected void done() {
				// this method is called when the background
				// thread finishes execution
				try {
					String statusMsg = (String) get();
					System.out.println("Inside done function");
					statusLabel.setText(statusMsg);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		};

		// executes the swingworker on worker thread
		sw1.addPropertyChangeListener(this);
		sw1.execute();
	}

	public static void main(String[] args) {
		SwingWorkerSample mySwingWorkerSample = new SwingWorkerSample();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mySwingWorkerSample.swingWorkerSample();
			}

		});
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getSource() == sw1) {
			System.out.println("Me llego mensaje");
			System.out.println(sw1.getState());

		}
	}
	/*
	 * 
	 * 
	 * final JLabel label; class MeaningOfLifeFinder extends SwingWorker<String,
	 * Object> {
	 * 
	 * @Override public String doInBackground() { return findTheMeaningOfLife(); }
	 * 
	 * @Override protected void done() { try { label.setText(get()); } catch
	 * (Exception ignore) { } } }
	 * 
	 * (new MeaningOfLifeFinder()).execute();
	 * 
	 * 
	 */
	/*
	 * class PrimeNumbersTask extends SwingWorker<List<Integer>, Integer> {
	 * PrimeNumbersTask(JTextArea textArea, int numbersToFind) { //initialize }
	 * 
	 * @Override public List<Integer> doInBackground() { while (! enough && !
	 * isCancelled()) { number = nextPrimeNumber(); publish(number); setProgress(100
	 * * numbers.size() / numbersToFind); } } return numbers; }
	 * 
	 * @Override protected void process(List<Integer> chunks) { for (int number :
	 * chunks) { textArea.append(number + "\n"); } } }
	 */
	/*
	 * JTextArea textArea = new JTextArea(); final JProgressBar progressBar = new
	 * JProgressBar(0, 100); PrimeNumbersTask task = new PrimeNumbersTask(textArea,
	 * N); task.addPropertyChangeListener( new PropertyChangeListener() { public
	 * void propertyChange(PropertyChangeEvent evt) { if
	 * ("progress".equals(evt.getPropertyName())) {
	 * progressBar.setValue((Integer)evt.getNewValue()); } } });
	 * 
	 * task.execute(); System.out.println(task.get()); //prints all prime numbers we
	 * have got
	 */
	}
