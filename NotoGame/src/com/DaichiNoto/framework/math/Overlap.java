package com.DaichiNoto.framework.math;

/**
 * 衝突判定クラス
 * @author Daichi Noto
 *
 */
public class Overlap {
	/**
	 * 二つの円の衝突判定
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static boolean OverlapCircles(Circle c1, Circle c2) {
		float distance = c1.m_center.distSquared(c2.m_center);
		float radiusSum = c1.m_radius + c2.m_radius;
		return distance <= radiusSum * radiusSum;
	}

	/**
	 * 二つの四角形の衝突判定
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static boolean OverlapRectangles(Rectangle r1, Rectangle r2) {
		if(r1.m_lowerLeft.getX() < r2.m_lowerLeft.getX() + r2.m_width &&
				r1.m_lowerLeft.getX() + r1.m_width > r2.m_lowerLeft.getX() &&
				r1.m_lowerLeft.getY() < r2.m_lowerLeft.getY() + r2.m_height &&
				r1.m_lowerLeft.getY() + r1.m_height > r2.m_lowerLeft.getY())
			return true;
		else
			return false;
	}

	/**
	 * 円と四角形の衝突判定
	 * @param c
	 * @param r
	 * @return
	 */
	public static boolean OverlapCircleRectangle(Circle c, Rectangle r) {
		float closestX = c.m_center.getX();
		float closestY = c.m_center.getY();

		if(c.m_center.getX() < r.m_lowerLeft.getX()) {
			closestX = r.m_lowerLeft.getX();
		}
		else if(c.m_center.getX() > r.m_lowerLeft.getX() + r.m_width) {
			closestX = r.m_lowerLeft.getX() + r.m_width;
		}

		if(c.m_center.getY() < r.m_lowerLeft.getY()) {
			closestY = r.m_lowerLeft.getY();
		}
		else if(c.m_center.getY() > r.m_lowerLeft.getY() + r.m_height) {
			closestY = r.m_lowerLeft.getY() + r.m_height;
		}

		return c.m_center.distSquared(closestX, closestY) < c.m_radius * c.m_radius;
	}

	/**
	 * 円と点の衝突判定
	 * @param c
	 * @param p
	 * @return
	 */
	public static boolean PointInCircle(Circle c, Vector2 p) {
		return c.m_center.distSquared(p) < c.m_radius * c.m_radius;
	}

	public static boolean PointInCircle(Circle c, float x, float y) {
		return c.m_center.distSquared(x, y) < c.m_radius * c.m_radius;
	}

	/**
	 * 四角形と点の衝突判定
	 * @param r
	 * @param p
	 * @return
	 */
	public static boolean PointInRectangle(Rectangle r, Vector2 p) {
		return r.m_lowerLeft.getX() <= p.getX() && r.m_lowerLeft.getX() + r.m_width >= p.getX() &&
		r.m_lowerLeft.getY() <= p.getY() && r.m_lowerLeft.getY() + r.m_height >= p.getY();
	}
	
	public static boolean PointInRectangle(Rectangle r, float x, float y) {
		return r.m_lowerLeft.getX() <= x && r.m_lowerLeft.getX() + r.m_width >= x &&
		r.m_lowerLeft.getY() <= y && r.m_lowerLeft.getY() + r.m_height >= y;
	}
	
	/**
	 * 球の衝突判定
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean OverlapSpheres(Sphere s1, Sphere s2) {
		float distance = s1.m_center.distSquared(s2.m_center);
		float radiusSum = s1.m_radius + s2.m_radius;
		return distance <= radiusSum * radiusSum;
	}

	/**
	 * 球と点の衝突判定
	 * @param c
	 * @param p
	 * @return
	 */
	public static boolean PointInSphere(Sphere c, Vector3 p) {
		return c.m_center.distSquared(p) < c.m_radius * c.m_radius;
	}

	public static boolean PointInSphere(Sphere c, float x, float y, float z) {
		return c.m_center.distSquared(x, y, z) < c.m_radius * c.m_radius;
	}	

}
