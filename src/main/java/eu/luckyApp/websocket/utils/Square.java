package eu.luckyApp.websocket.utils;

public enum Square {
	blue("#0000FF"),green("#00FF00"),yellow("#00FFFF"),red("FF0000");
	private final String color;	
	
	Square(String color){
		this.color=color;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

}
