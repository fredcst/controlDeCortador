package cortador;

public class RectanguloH {

	private double posX, posY, dX, dY;

	public RectanguloH(double posX, double posY, double dX, double dY) {

		this.posX = posX;
		this.posY = posY;
		this.dX = dX;
		this.dY = dY;
	}

	public RectanguloH() {

	}

	public void setPosX(double xpos) {
		posX = xpos;
	}

	public void setPosY(double ypos) {
		posY = ypos;
	}

	public void setDX(double dX) {
		this.dX = dX;
	}

	public void setDY(double dY) {
		this.dY = dY;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public double getDX() {
		return dX;
	}

	public double getDY() {
		return dY;
	}

	public static RectanguloH copiaRectanguloH(RectanguloH rec) {
		RectanguloH recCpy = null;
		recCpy = new RectanguloH(rec.posX, rec.posY, rec.dX, rec.dY);

		return recCpy;
	}
}
