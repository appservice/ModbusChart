package eu.luckyApp.websocket.utils;

public class ActualValue {
	private Square square;

	private double value;

	public ActualValue() {
	}

	public ActualValue(Square square,double value) {
		this.square=square;
		this.value=value;
	}

	/**
	 * @return the square
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @param square the square to set
	 */
	public void setSquare(Square square) {
		this.square = square;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	

}
