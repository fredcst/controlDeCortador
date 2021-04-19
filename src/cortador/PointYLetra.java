package cortador;

public class PointYLetra {

	char t; // t='n' numeros, t='o' operacion
	int flag_vis;
	double x;
	double y;

	public PointYLetra(char t, int flag_vis, double x, double y) {
		this.t = t;
		this.flag_vis = flag_vis;
		this.x = x;
		this.y = y;
	}

	public PointYLetra() {
		// TODO Auto-generated constructor stub
	}

}
