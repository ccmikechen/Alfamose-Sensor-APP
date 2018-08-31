package sensor;
public class ShoePoint {
	
	public float x;
	
	public float y;
	
	public ShoePoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(ShoePoint p) {
		return ((this.x == p.x) && (this.y == p.y));
	}
	
	public String toString() {
		return "#ShoePoint <" + x + ", " + y + ">";
	}
	
}