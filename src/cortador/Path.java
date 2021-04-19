package cortador;

import java.util.LinkedList;

public class Path {

	int id;
	int flag_vis;
	LinkedList<EleCalculo> path;

	public Path(int id, int flag_vis, LinkedList<EleCalculo> path

	) {
		this.id = id;
		this.flag_vis = flag_vis;
		this.path = path;

	}
}
