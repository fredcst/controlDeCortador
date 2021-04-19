package cortador;

public class EleCalculo {

	char t; // t='C' o 'c'; 'Q' o 'q'; 'L' o 'l' ;'H' o 'h' ;'V' o 'v'
	double xP1;
	double yP1;
	double xP2;
	double yP2;
	double xP3;
	double yP3;
	double xP4;
	double yP4;

	public EleCalculo(char t, // t='C' o 'c'; 'Q' o 'q'; 'L' o 'l' ;'H' o 'h' ;'V' o 'v'
			double xP1, double yP1, double xP2, double yP2, double xP3, double yP3, double xP4, double yP4) {
		this.t = t;
		this.xP1 = xP1;
		this.xP2 = xP2;
		this.xP3 = xP4;
		this.xP4 = xP4;
		this.yP1 = yP1;
		this.yP2 = yP2;
		this.yP3 = yP4;
		this.yP4 = yP4;

	}

	public char getT() {
		return t;
	}

	public void setT(char t) {
		this.t = t;
	}

	public double getxP1() {
		return xP1;
	}

	public void setxP1(double xP1) {
		this.xP1 = xP1;
	}

	public double getyP1() {
		return yP1;
	}

	public void setyP1(double yP1) {
		this.yP1 = yP1;
	}

	public double getxP2() {
		return xP2;
	}

	public void setxP2(double xP2) {
		this.xP2 = xP2;
	}

	public double getyP2() {
		return yP2;
	}

	public void setyP2(double yP2) {
		this.yP2 = yP2;
	}

	public double getxP3() {
		return xP3;
	}

	public void setxP3(double xP3) {
		this.xP3 = xP3;
	}

	public double getyP3() {
		return yP3;
	}

	public void setyP3(double yP3) {
		this.yP3 = yP3;
	}

	public double getxP4() {
		return xP4;
	}

	public void setxP4(double xP4) {
		this.xP4 = xP4;
	}

	public double getyP4() {
		return yP4;
	}

	public void setyP4(double yP4) {
		this.yP4 = yP4;
	}

}
