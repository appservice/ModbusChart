package eu.luckyApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.luckyApp.model.Measurement;
import eu.luckyApp.repository.MeasurementRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ModbusChartApplication.class)
public class ModbusChartApplicationTests {

	@Autowired
	MeasurementRepository measurementRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void addToDatabaseTest() throws ParseException {

		long startDate = generateStartData();
		System.out.println("start date: " + startDate);

		long numberIteration = 60 * 24 * 365;
		for (long i = 0; i < numberIteration; i++) {
			List<Double> valueList = valueListGenerator(i);

			Date myDate = dateGenerator(startDate, i);

			Measurement m = new Measurement();
			m.setMeasuredValue(valueList);
			m.setDate(myDate);
			System.out.println(i + " " + myDate);
			measurementRepository.save(m);

		}
	}

	private Date dateGenerator(long starDate, long i) throws ParseException {

		long initialDate = starDate + 1000 * 60 * i;
		// System.out.println("initialData "+initialDate);
		Date myDate = new Date(initialDate);
		return myDate;
	}

	private List<Double> valueListGenerator(long i) {
		List<Double> valueList = new ArrayList<>();
		Random randomGenerator = new Random();

		valueList.add(randomGenerator.nextDouble() * 50);
		valueList.add(randomGenerator.nextDouble() * 20 + 25);
		valueList.add(randomGenerator.nextDouble() * 20 - 30 + i / 5000);

		valueList.add(Math.sin((double) i / 100) * 30);
		valueList.add(Math.cos((double) i / 100) * 20
				+ randomGenerator.nextDouble() * 5);
		valueList.add(Math.sin((double) i / 150 + 0.2) + 20);
		valueList.add(Math.sin((double) i / 80) - 20
				+ randomGenerator.nextDouble() * 3);

		valueList.add(Math.sqrt(i/100));

		return valueList;
	}

	private long generateStartData() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = (Date) formatter.parse("20-06-2014");
		return date.getTime();
	}

}
