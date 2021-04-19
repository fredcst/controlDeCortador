package cortador;

import java.util.ArrayList;

public class ColumnaH {
	private ArrayList<PiezaH> myPiezasH = null;
	private double altoTotal = 0.;// Lleva el valor del alto total de columna
	private double anchoMaxCol = 0;// Lleva el valor del ancho de la columna

	private boolean estaEnTop = false;

	public ColumnaH() {
		myPiezasH = new ArrayList<PiezaH>();
		estaEnTop = false;
	}

	public ArrayList<PiezaH> getMyPiezasH() {
		return myPiezasH;
	}

	public void setColH(ArrayList<PiezaH> colH) {
		this.myPiezasH = colH;
	}

	public double getAltoTotal() {
		return altoTotal;
	}

	public void setAltoTotal(double altoTotal) {
		this.altoTotal = altoTotal;
	}

	public double getAnchoMaxCol() {
		return anchoMaxCol;
	}

	public void setAnchoMaxCol(int anchoMaxCol) {
		this.anchoMaxCol = anchoMaxCol;
	}

	public void setEstaEnTop(boolean estaEnTop) {
		this.estaEnTop = estaEnTop;
	}

	/**
	 * Obtiene el ancho maximo de la columna, considerenado las piezas que han sido
	 * apiladas al momento de la pregunta.
	 * 
	 * @return: valor del ancho maximo
	 */
	public double getMaxAnchoCol() {
		double maxanchox = 0.;
		for (PiezaH pz : myPiezasH) {
			if (maxanchox < pz.getAncho())
				maxanchox = pz.getAncho();
		}
		anchoMaxCol = maxanchox;
		return maxanchox;
	}

	/**
	 * Obtiene el alto total de la columna, considerenado las piezas que han sido
	 * apiladas al momento de la pregunta..
	 * 
	 * @return
	 */
	public double getAltoTotalCol() {
		double altox = 0;
		for (PiezaH pz : myPiezasH)
			altox = altox + pz.getAlto();
		altoTotal = altox;
		return altox;
	}

	/**
	 * Agrega pieza a columna si alto total de coluna es menor que valor de :limite
	 * Altototal es una variable estatica para cada columna Trabaja solo si
	 * topFlag=false
	 * 
	 * @param pz
	 * @param limite
	 * @return
	 */
	/*
	 * public boolean agregaPieza(boolean topFlag, PiezaH pz, double altoHueco) {
	 * boolean res = false; if (myPiezasH != null && !topFlag && pz.isUsado() ==
	 * false) { altoTotal = altoTotalPrev + pz.getAlto(); if (myPiezasH.size() == 0)
	 * anchoMaxCol = 0;// asegurarse que maxX comienza en 0 if (altoTotal <
	 * altoHueco) { if (anchoMaxCol < pz.getAncho()) anchoMaxCol = pz.getAncho(); if
	 * (myPiezasH.size() == 0) pz.setPosY(0); else pz.setPosY(altoTotalPrev);
	 * myPiezasH.add(pz); pz.setUsado(true);// coloca pieza como usada res = true;
	 * altoTotalPrev = altoTotal; // NO SE HA AJUSTADO COORDENADA X DE PIEZAS } }
	 * return res; }
	 */

	public ArrayList<PiezaH> getArrayDeColunna() {

		return myPiezasH;

	}

	public void setArrayDeColunna(ArrayList<PiezaH> colx) {

		myPiezasH = colx;

	}

	public void muestraEnConsolaColumnaH() {
		if (myPiezasH != null) {
			System.out.println("AltoTotal: " + altoTotal + " anchoMax: " + anchoMaxCol);
			String frase;
			for (PiezaH e : myPiezasH) {
				frase = String.format("id:\t%d\tancho:\t%f\talto:\t%f ", e.getIndice(), e.getAncho(), e.getAlto());
				System.out.println(frase + " rotada?: " + e.isEstaRotadaM90());
			}
		}

		return;
	}

	public void setAnchoMax(double d) {
		// TODO Auto-generated method stub
		anchoMaxCol = d;
	}

	public boolean estaEnTop() {
		// TODO Auto-generated method stub
		return estaEnTop;
	}

	public void reajustaMaxDeAnchoDeColConAnchoDe(PiezaH piezaH) {
		// TODO Auto-generated method stub
		if (piezaH.getAncho() > anchoMaxCol)
			anchoMaxCol = piezaH.getAncho();
	}

	public void reajustaAltoTotalDeColConAltoDe(PiezaH piezaH) {
		// TODO Auto-generated method stub
		altoTotal = altoTotal + piezaH.getAlto();
	}

	public boolean isEmpty() {
		boolean res = false;
		if (myPiezasH == null || myPiezasH.isEmpty())
			res = true;
		// TODO Auto-generated method stub
		return res;
	}


	public void muestraEnConsolaCoordenadasDePiezasEnColumnaConRespectoAPiezaQueLasContiene() {
		if (myPiezasH != null) {
			for (PiezaH e : myPiezasH) {
				e.muestraEnConsolaCoordendasDePiezaConRespectoAPiezaQueLaContiene();
			}
		}
		return;
	}

	public void muestraEnConsolaAltoTotalDeColumna() {
		System.out.println("altoTotal: " + altoTotal);

	}
}
