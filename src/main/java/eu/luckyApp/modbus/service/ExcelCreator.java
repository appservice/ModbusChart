package eu.luckyApp.modbus.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.chart.CategorySeriesAxisRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.AxisTickMark;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ChartLegend;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.LineChartData;
import org.apache.poi.ss.usermodel.charts.LineChartSeries;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTx;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;

import eu.luckyApp.model.FilePathEntity;
import eu.luckyApp.model.Measurement;

public class ExcelCreator implements Callable<FilePathEntity> {

	Logger LOG = Logger.getLogger(ExcelCreator.class);

	private List<Measurement> list;

	private List<String> seriesNames;

	private double price = Preferences.userRoot().node("modbuschart").getDouble("currentPrice", 0);

	public ExcelCreator(List<Measurement> list, List<String> seriesNames) {
		this.list = list;
		// System.out.println(list.toString());
		this.seriesNames = seriesNames;
	}

	private String shiftChecker(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		if (calendar.get(Calendar.HOUR_OF_DAY) >= 6 && calendar.get(Calendar.HOUR_OF_DAY) < 14) {
			return "I";
		} else if (calendar.get(Calendar.HOUR_OF_DAY) >= 14 && calendar.get(Calendar.HOUR_OF_DAY) < 22) {
			return "II";

		} else {
			return "III";
		}

	}

	@Override
	public FilePathEntity call() throws Exception {

		Workbook wb = new XSSFWorkbook();
		Sheet sheet2 = wb.createSheet("Wykresy zużycia powietrza oraz energii");
		Sheet sheet3 = wb.createSheet("Raport");
		Sheet sheet = wb.createSheet("Wartości");

		final int NUM_OF_ROWS = list.size();

		short df = wb.createDataFormat().getFormat("HH:MM:ss");
		CellStyle cs = wb.createCellStyle();
		cs.setDataFormat(df);

		// sheet2.set

		initFirstRow(sheet);

		for (int myRow = 1; myRow < NUM_OF_ROWS + 1; myRow++) {
			Row row = sheet.createRow(myRow);

			Measurement m = list.get(myRow - 1);
			Date newDate = m.getDate();// new Date(now.getTime()+myRow*1000*60);

			Cell inFirstColumnCell = row.createCell(0);
			inFirstColumnCell.setCellValue(newDate);
			inFirstColumnCell.setCellStyle(cs);

			int k = 1;
			// double sumOfAllValues=0;
			for (Double value : m.getMeasuredValue()) {
				row.createCell(k).setCellValue(value);
				// sumOfAllValues+=value;
				k++;
			}
			row.createCell(k).setCellValue(m.getEnergyConsumption());

			/*
			 * double
			 * price=Preferences.userRoot().node("modbuschart").getDouble(
			 * "currentPrice", 0); if(m.getEnergyConsumption()==0){
			 * //row.createCell(k+1).setCellValue(0); }else{
			 * row.createCell(k+1).setCellValue(sumOfAllValues*price/m.
			 * getEnergyConsumption()); }
			 */

		}

		generateAirChart(sheet, sheet2, NUM_OF_ROWS);
		generateEnergyChart(sheet, sheet2, NUM_OF_ROWS);
		sheet2.setFitToPage(true);

		createRaportHeader(sheet3);
		generateRaportData(sheet3);

		// saveValueForSheet3(sheet3,priceOf1m3Air);

		Date nowDate = getNowDate();

		File f = new File("../energia_" + createFileName(nowDate) + ".xlsx");

		FileOutputStream fileOut = new FileOutputStream(f);
		wb.write(fileOut);

		fileOut.close();
		wb.close();
		FilePathEntity fpe = new FilePathEntity();
		fpe.setAbsolutePath(f.getAbsolutePath());
		fpe.setFileName(f.getName());
		fpe.setComment(plotShiftData());
		fpe.setCreationDate(new Date());
		fpe.setPriceOf1m3Air(calculatePriceOf1m3Air(this.price));
		return fpe;

	}

	private double sumOfAirConsumption() {
		if (!list.isEmpty()) {
			Measurement lastMeasurement = list.get(list.size() - 1);
			double sum = 0;
			for (Double value : lastMeasurement.getMeasuredValue()) {
				sum += value;
			}
			return sum;
		} else
			return 0.0;
	}

	/**
	 * 
	 */
	private double calculatePriceOf1m3Air(double price)

	{
		// if(list.size()>0){
		if (!list.isEmpty()) {
			Measurement lastMeasurement = list.get(list.size() - 1);
			// double
			// price=Preferences.userRoot().node("modbuschart").getDouble("currentPrice",
			// 0.0);
			double sum = sumOfAirConsumption();

			return  lastMeasurement.getEnergyConsumption() * price /sum;

		} else
			return 0.0;
	}

	/**
	 * @param sheet
	 * @param sheet2
	 * @param NUM_OF_ROWS
	 */
	private void generateAirChart(Sheet sheet, Sheet sheet2, final int NUM_OF_ROWS) {
		Drawing drawing = sheet2.createDrawingPatriarch();
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 24, 30);

		Chart chart = drawing.createChart(anchor);
		ChartLegend legend = chart.getOrCreateLegend();
		legend.setPosition(LegendPosition.TOP_RIGHT);

		createAirChartTitle(chart);

		LineChartData data = chart.getChartDataFactory().createLineChartData();

		ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
		bottomAxis.setNumberFormat("gg:mm:ss");
		bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrosses(AxisCrosses.MIN);
		// bottomAxis.setMinorTickMark(AxisTickMark.NONE);
		bottomAxis.setMajorTickMark(AxisTickMark.NONE);
		// bottomAxis.setM
		CategorySeriesAxisRecord categoryAxis = new CategorySeriesAxisRecord();
		categoryAxis.setTickMarkFrequency((short) 10);

		// int numberOfColumns = 0;
		if (sheet.getRow(1) != null) {
			int numberOfColumns = seriesNames.size();// =
														// sheet.getRow(1).getLastCellNum();

			// System.out.println(numberOfColumns);

			ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet,
					new CellRangeAddress(0, NUM_OF_ROWS, 0, 0));

			for (int l = 0; l < numberOfColumns; l++) {
				ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(sheet,
						new CellRangeAddress(0, NUM_OF_ROWS, l + 1, l + 1));

				LineChartSeries chartSeries = data.addSeries(xs, ys1);
				if (seriesNames.size() > l)
					chartSeries.setTitle(seriesNames.get(l));
			}
		}
		chart.plot(data, bottomAxis, leftAxis);
	}

	/**
	 * @param sheet
	 * @param sheet2
	 * @param NUM_OF_ROWS
	 */
	private void generateEnergyChart(Sheet sheet, Sheet sheet2, final int NUM_OF_ROWS) {
		Drawing drawing = sheet2.createDrawingPatriarch();
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 31, 24, 41);

		Chart chart = drawing.createChart(anchor);
		ChartLegend legend = chart.getOrCreateLegend();
		legend.setPosition(LegendPosition.TOP_RIGHT);

		createEnergyChartTitle(chart);

		LineChartData data = chart.getChartDataFactory().createLineChartData();

		ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
		bottomAxis.setNumberFormat("gg:mm:ss");
		bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrosses(AxisCrosses.MIN);
		// bottomAxis.setMinorTickMark(AxisTickMark.NONE);
		bottomAxis.setMajorTickMark(AxisTickMark.NONE);
		// bottomAxis.setM
		CategorySeriesAxisRecord categoryAxis = new CategorySeriesAxisRecord();
		categoryAxis.setTickMarkFrequency((short) 10);

		// int numberOfColumns = 0;
		if (sheet.getRow(1) != null) {
			int numberOfColumns = seriesNames.size();// =
														// sheet.getRow(1).getLastCellNum();

			// System.out.println(numberOfColumns);

			ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet,
					new CellRangeAddress(0, NUM_OF_ROWS, 0, 0));

			// for (int l = 0; l < numberOfColumns - 1; l++) {
			ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(sheet,
					new CellRangeAddress(0, NUM_OF_ROWS, numberOfColumns + 1, numberOfColumns + 1));

			LineChartSeries chartSeries = data.addSeries(xs, ys1);
			chartSeries.setTitle("Zużuycie energii");
		}
		chart.plot(data, bottomAxis, leftAxis);
	}

	/**
	 * @param sheet
	 * @param sheet2
	 * @param NUM_OF_ROWS
	 */
	@SuppressWarnings("unused")
	private void generatePriceCalculatedChart(Sheet sheet, Sheet sheet3, final int NUM_OF_ROWS) {
		Drawing drawing = sheet3.createDrawingPatriarch();
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 24, 41);

		Chart chart = drawing.createChart(anchor);
		ChartLegend legend = chart.getOrCreateLegend();
		legend.setPosition(LegendPosition.TOP_RIGHT);

		createEnergyChartTitle(chart);

		LineChartData data = chart.getChartDataFactory().createLineChartData();

		ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
		bottomAxis.setNumberFormat("gg:mm:ss");
		bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrosses(AxisCrosses.MIN);
		// bottomAxis.setMinorTickMark(AxisTickMark.NONE);
		bottomAxis.setMajorTickMark(AxisTickMark.NONE);
		// bottomAxis.setM
		CategorySeriesAxisRecord categoryAxis = new CategorySeriesAxisRecord();
		categoryAxis.setTickMarkFrequency((short) 10);

		// int numberOfColumns = 0;
		if (sheet.getRow(1) != null) {
			int numberOfColumns = seriesNames.size();// =
														// sheet.getRow(1).getLastCellNum();

			// System.out.println(numberOfColumns);

			ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet,
					new CellRangeAddress(0, NUM_OF_ROWS, 0, 0));

			// for (int l = 0; l < numberOfColumns - 1; l++) {
			ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(sheet,
					new CellRangeAddress(0, NUM_OF_ROWS, numberOfColumns + 2, numberOfColumns + 2));

			LineChartSeries chartSeries = data.addSeries(xs, ys1);
			chartSeries.setTitle("Cena wyprodukowania 1m3 ");
		}
		chart.plot(data, bottomAxis, leftAxis);
	}

	/**
	 * @param chart
	 */
	private void createAirChartTitle(Chart chart) {
		if (chart instanceof XSSFChart) {
			XSSFChart xchart = (XSSFChart) chart;
			CTChart ctChart = xchart.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT("Wykres zużycia powietrza w dniu " + plotShiftData() + "\n[m3]");
		}
	}

	/**
	 * @param chart
	 */
	private void createEnergyChartTitle(Chart chart) {
		if (chart instanceof XSSFChart) {
			XSSFChart xchart = (XSSFChart) chart;
			CTChart ctChart = xchart.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT("Wykres zużycia energii elektrycznej " + plotShiftData() + "\n[kWh]");
		}
	}

	/**
	 * @return
	 */
	private String plotShiftData() {
		return createTitleName(getNowDate());
	}

	/**
	 * @return
	 */
	private Date getNowDate() {
		Date nowDate;
		if (list.isEmpty()) {
			Long date = new Date().getTime();

			nowDate = new Date(date - 1000 * 3600 * 7);

		} else {
			nowDate = list.get(0).getDate();
		}
		return nowDate;
	}

	/**
	 * @param nowDate
	 * @return
	 */
	private String createFileName(Date nowDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
		String dateString = formatter.format(nowDate);
		String s = dateString + "-" + shiftChecker(nowDate);
		return s;
	}

	/**
	 * @param nowDate
	 * @return
	 */
	private String createTitleName(Date nowDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-YYYY");
		String dateString = formatter.format(nowDate);
		String s = dateString + " zmiana " + shiftChecker(nowDate);
		return s;
	}

	/**
	 * @param sheet
	 */
	private void initFirstRow(Sheet sheet) {
		Font headerFont = sheet.getWorkbook().createFont();
		headerFont.setBold(true);
		CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
		headerStyle.setFont(headerFont);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setBorderTop(CellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);

		Row firstRow = sheet.createRow(0);
		Cell firnstCell = firstRow.createCell(0);
		firnstCell.setCellValue("Data");
		firnstCell.setCellStyle(headerStyle);

		int i = 1;
		for (String serieName : this.seriesNames) {
			Cell cell = firstRow.createCell(i);
			cell.setCellValue(serieName);
			cell.setCellStyle(headerStyle);
			sheet.autoSizeColumn(i);
			i++;
		}
		Cell energyCell = firstRow.createCell(i);
		energyCell.setCellValue("Zużycie energii");
		// energyCell.se
		energyCell.setCellStyle(headerStyle);

		/*
		 * Cell priceOf1m3air=firstRow.createCell(i+1);
		 * priceOf1m3air.setCellValue("Cena 1 m3 powietrza"); //energyCell.se
		 * priceOf1m3air.setCellStyle(headerStyle);
		 */

		sheet.autoSizeColumn(i);

	}

	private void createRaportHeader(Sheet sheet) {
		Font headerFont = sheet.getWorkbook().createFont();
		// headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 18);

		CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
		headerStyle.setFont(headerFont);
		// headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 5));

		Row headerRow = sheet.createRow(1);
		Cell headerCell = headerRow.createCell(1);
		headerCell.setCellStyle(headerStyle);
		headerCell.setCellValue("Raport z dnia " + plotShiftData());

	}

	/**
	 * @param sheet3
	 * @param priceOf1m3Air
	 */
	private void generateRaportData(Sheet sheet) {

		Row dataRow1 = sheet.createRow(3);
		dataRow1.createCell(1).setCellValue("Cena energii za 1 kWh:");
		dataRow1.createCell(2).setCellValue(this.price);
		dataRow1.createCell(3).setCellValue("PLN");
		
		
		double energyConsuption=0.0;
		if(!list.isEmpty()){
			energyConsuption=list.get(list.size()-1).getEnergyConsumption();
		}
		Row dataRow2=sheet.createRow(4);
		dataRow2.createCell(1).setCellValue("Energia zużyta:");		
		dataRow2.createCell(2).setCellValue(energyConsuption);
		dataRow2.createCell(3).setCellValue("kWh");
		
		
		Row dataRow3 =sheet.createRow(5);
		dataRow3.createCell(1).setCellValue("Ilość zużytego powietrza:");
		dataRow3.createCell(2).setCellValue(sumOfAirConsumption());
		dataRow3.createCell(3).setCellValue("m3");

		Row dataRow4 = sheet.createRow(6);
		dataRow4.createCell(1).setCellValue("Koszt  1m3 powietrza:");
		dataRow4.createCell(2).setCellValue(calculatePriceOf1m3Air(price));
		dataRow4.createCell(3).setCellValue("PLN/m3");

		sheet.autoSizeColumn(1);

	}

}
