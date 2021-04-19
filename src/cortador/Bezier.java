package cortador;

public class Bezier {

	public Bezier() {}
	
	
	public static Point vectorBezierC(double t, Point res, Point p1, Point p2, Point p3, Point p4) {
		Point res1 = res;
		res1.x = (1. - t) * (1. - t) * (1. - t) * (p1.x) + 3. * t * (1. - t) * (1. - t) * (p2.x)
				+ 3. * t * t * (1. - t) * (p3.x) + t * t * t * (p4.x);
		res1.y = (1. - t) * (1. - t) * (1. - t) * (p1.y) + 3. * t * (1. - t) * (1. - t) * (p2.y)
				+ 3. * t * t * (1. - t) * (p3.y) + t * t * t * (p4.y);
		return res1;
	}

	public static Point vectorBezierQ(double t, Point res, Point p1, Point p2, Point p3) {
		Point res1 = res;
		res1 = res;
		res1.x = (1. - t) * (1. - t) * (p1.x) + 2. * t * (1. - t) * (p2.x) + t * t * (p3.x);

		res1.y = (1. - t) * (1. - t) * (p1.y) + 2. * t * (1. - t) * (p2.y) + t * t * (p3.y);
		return res1;
	}

	public static Point vectorBezierL(double t, Point res, Point p1, Point p2) {
		Point res1 = res;

		res1.x = (1. - t) * (p1.x) + t * (p2.x);

		res1.y = (1. - t) * (p1.y) + t * (p2.y);
		return res1;
	}

}
