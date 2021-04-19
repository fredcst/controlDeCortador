package cortador;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.Random;
import java.time.LocalTime;

public class SwingWorkerUsingPublish extends SwingWorker<Object, Integer> {
	int total = 100;
	int wait = 10;

// Extending the SwingWorker class
	protected Object doInBackground() {
		logMessage("doInBackground() started");
		int i = 0;
		Random r = new Random();
		try {
			while (i < total) {
				Thread.sleep(wait);
				Integer n = new Integer(r.nextInt());
				publish(n);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logMessage("doInBackground() ended");
		return null;
	}

	protected void process(List<Integer> v) {
		logMessage("process() receiving values: " + v.size());
	}

// Launching my extended SwingWorker class
	public static void main(String[] a) {
		try {
			SwingWorker worker = new SwingWorkerUsingPublish();
			dumpThreads();
			worker.execute();
			while (true) {
				dumpThreads();
				Thread.sleep(200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void dumpThreads() {
		System.out.println(LocalTime.now() + " Thread dump:");
		Thread.currentThread().getThreadGroup().list();
	}

	public static void logMessage(String s) {
		System.out.println(LocalTime.now() + " " + Thread.currentThread().getName() + ": " + s);
	}
}