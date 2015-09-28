package eu.luckyApp.websocket.utils;

import java.util.ArrayList;
import java.util.List;

import eu.luckyApp.model.Measurement;

public class SquareManager {
	private long timeInterval;
	private List<Boolean> wasGreenSquare;
	private List<Long> tempTime;
	private long tresholdTimeToYellow;
	private long tresholdTimeToRed;
	private double tresholdValue;
	// private int squareAmount=7;

	public SquareManager() {

		wasGreenSquare = new ArrayList<>();// new boolean[squareAmount];

		this.tempTime = new ArrayList<>();// new long[squareAmount];
		this.tresholdTimeToYellow = 60000;// default one minute
		this.tresholdTimeToRed = 300000;// default five minutes
		this.tresholdValue=4;//default treshold value

	}

	/**
	 * @param timeInterval
	 *            the timeInterval to set
	 */
	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * @param tresholdTimeToYellow
	 *            the tresholdTimeToYellow to set
	 */
	public void setTresholdTimeToYellow(long tresholdTimeToYellow) {
		this.tresholdTimeToYellow = tresholdTimeToYellow;
	}

	/**
	 * @param tresholdTimeToRed
	 *            the tresholdTimeToRed to set
	 */
	public void setTresholdTimeToRed(long tresholdTimeToRed) {
		this.tresholdTimeToRed = tresholdTimeToRed;
	}

	/**
	 * @param squareAmount
	 *            the squareAmount to set
	 */
	/*
	 * public void setSquareAmount(int squareAmount) { this.squareAmount =
	 * squareAmount; }
	 */

	/**
	 * @param measurement
	 *            the measurement to set
	 */

	public List<Square> calculateSquare(Measurement m) {

		int squaresNumber = m.getMeasuredValue().size();
		List<Square> squareList = new ArrayList<>(m.getMeasuredValue().size());
		while (wasGreenSquare.size() < squaresNumber) {
			wasGreenSquare.add(false);
			tempTime.add(0L);
		}

		int i = 0;
		for (double value : m.getMeasuredValue()) {

			squareList.add(calculateSquare(value, i));
			i++;
		}

		return squareList;

	}

	private Square calculateSquare(double value, int i) {

		if (value >= tresholdValue) {
			wasGreenSquare.set(i, true);
			tempTime.set(i, 0L);
			return Square.green;
		} else if (!wasGreenSquare.get(i)) {

			return Square.blue;
		} else {
			tempTime.set(i, tempTime.get(i) + timeInterval);
			// tempTime[i] += timeInterval;
			if (tempTime.get(i) < tresholdTimeToYellow) {
				return Square.green;
			}

			else {

				if (tempTime.get(i) < tresholdTimeToRed) {
					return Square.yellow;
				} else {
					return Square.red;
				}

			}

		}

	}

	/**
	 * @param measurement
	 *            the measurement to set
	 */

	public List<ActualValue> calculateActualValue(Measurement m) {

		int squaresNumber = m.getMeasuredValue().size();
		List<ActualValue> actualValueList = new ArrayList<>(m.getMeasuredValue().size());
		while (wasGreenSquare.size() < squaresNumber) {
			wasGreenSquare.add(false);
			tempTime.add(0L);
		}

		int i = 0;
		for (double value : m.getMeasuredValue()) {

			actualValueList.add(calculateActualValue(value, i));
			i++;
		}

		return actualValueList;

	}

	private ActualValue calculateActualValue(double value, int i) {

		if (value >= tresholdValue) {
			wasGreenSquare.set(i, true);
			tempTime.set(i, 0L);
			return new ActualValue(Square.green, value);
		} else if (!wasGreenSquare.get(i)) {

			return new ActualValue(Square.blue, value);
		} else {
			tempTime.set(i, tempTime.get(i) + timeInterval);
			// tempTime[i] += timeInterval;
			if (tempTime.get(i) < tresholdTimeToYellow) {
				return new ActualValue(Square.green, value);
			
			}

			else {

				if (tempTime.get(i) < tresholdTimeToRed) {
					return new ActualValue(Square.yellow, value);
				} else {
					return new ActualValue(Square.red, value);
					
				}

			}

		}

	}

	/**
	 * @return the tresholdValue
	 */
	public double getTresholdValue() {
		return tresholdValue;
	}

	/**
	 * @param tresholdValue the tresholdValue to set
	 */
	public void setTresholdValue(double tresholdValue) {
		this.tresholdValue = tresholdValue;
	}

	public void clear() {
		for (int i = 0; i < this.wasGreenSquare.size(); i++) {
			this.wasGreenSquare.set(i, false);
			this.tempTime.set(i, 0l);

		}

	}

}
