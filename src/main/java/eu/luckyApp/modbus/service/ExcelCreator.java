package eu.luckyApp.modbus.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

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

	private List<Measurement> list;

	private List<String> seriesNames;

	public ExcelCreator(List<Measurement> list, List<String> seriesNames) {
		this.list = list;
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
		Sheet sheet2 = wb.createSheet("Wykres");
		Sheet sheet = wb.createSheet("Wartości");

		final int NUM_OF_ROWS = list.size();

		short df = wb.createDataFormat().getFormat("HH:MM:ss");
		CellStyle cs = wb.createCellStyle();
		cs.setDataFormat(df);

		Drawing drawing = sheet2.createDrawingPatriarch();
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 25, 40);

		Chart chart = drawing.createChart(anchor);
		ChartLegend legend = chart.getOrCreateLegend();
		legend.setPosition(LegendPosition.TOP_RIGHT);

		createTitle(chart);

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
			for (Double value : m.getMeasuredValue()) {
				row.createCell(k).setCellValue(value);
				k++;
			}

		}

		int numberOfColumns = 0;
		if (sheet.getRow(1) != null) {
			numberOfColumns = sheet.getRow(1).getLastCellNum();

			//System.out.println(numberOfColumns);

			ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet,
					new CellRangeAddress(0, NUM_OF_ROWS, 0, 0));

			for (int l = 0; l < numberOfColumns - 1; l++) {
				ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(sheet,
						new CellRangeAddress(0, NUM_OF_ROWS, l + 1, l + 1));

				LineChartSeries chartSeries = data.addSeries(xs, ys1);
				if (seriesNames.size() > l)
					chartSeries.setTitle(seriesNames.get(l));
			}
		}
		chart.plot(data, bottomAxis, leftAxis);

		sheet2.setFitToPage(true);

		Date nowDate = getNowDate();

		File f = new File("../energia_" + createFileName(nowDate) + ".xlsx");

		FileOutputStream fileOut = new FileOutputStream(f);
		wb.write(fileOut);

		fileOut.close();
		wb.close();
		FilePathEntity fpe = new FilePathEntity();
		fpe.setAbsolutePath(f.getAbsolutePath());
		fpe.setFileName(f.getName());
		fpe.setComment(addTitleText());
		fpe.setCreationDate(new Date());
		return fpe;

	}

	/**
	 * @param chart
	 */
	private void createTitle(Chart chart) {
		if (chart instanceof XSSFChart) {
			XSSFChart xchart = (XSSFChart) chart;
			CTChart ctChart = xchart.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT("Wykres zużycia powietrza w dniu " + addTitleText() + "\n[m3]");
		}
	}

	/**
	 * @return
	 */
	private String addTitleText() {
		return createTitleName(getNowDate());
	}

	/**
	 * @return
	 */
	private Date getNowDate() {
		Date nowDate;
		if (list.isEmpty()) {
			Long date = new Date().getTime();

			nowDate = new Date(date - 1000 * 3600*7);

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
			sheet.autoSizeColumn(i + 1);
			i++;
		}

	}

}
