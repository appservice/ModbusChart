
package eu.luckyApp.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.luckyApp.model.Measurement;
import eu.luckyApp.repository.MeasurementRepository;

//@Path("/servers/{id}/measurements/download/stream")
public class ExcelRsAsStram {

	public static final Logger LOG = Logger.getLogger(ExcelRsAsStram.class);

//	@Autowired
	MeasurementRepository measurementRepository;

	private SXSSFWorkbook workbook;
	private int lastColumnNumber;
	private Long serverId;
	//private Stream<Measurement> measurements;
	private int  rowNumber=1;
//	@GET
//	@Path("/excel")
//	@Produces("application/vnd.ms-excel")
	public Response getFile(@PathParam("id") Long id, @QueryParam("startDate") long startDate, @QueryParam("endDate") long endDate,
			@DefaultValue("0") @QueryParam("timePeriod") Long timePeriod) {
		this.serverId = id;
	LOG.info("wystartowala");	

	//	this.measurements = checkQueryParams(startDate, endDate, timePeriod);

		// -------------StreaminOutput---------- to return excel file in
		// outputStream----------
		StreamingOutput soutput = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

				try {
					buildExcelDokument(output, serverId);
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				}
			}
		};

		ResponseBuilder response = Response.ok(soutput);
		String fileName="dane_"+new Date().getTime()+".xlsx";
		response.header("Content-Disposition", "attachment; filename="+fileName);
		return response.build();
	}

	// ---------------------checkQueryParams---------------------------------------------
	private Stream<Measurement> checkQueryParams(long startDate, long endDate, Long timePeriod) {

		if (timePeriod != 0) {
			Date now = new Date();
			Date beginDate = new Date(now.getTime() - timePeriod);

			return null;//measurementRepository.findAllFromServerByStartDate(/*serverId, */beginDate,1);

		} else

		// with startDate parameter
		if (startDate != 0 && endDate == 0) {
			return null;//measurementRepository.findAllFromServerByStartDate(/*serverId,*/ new Date(startDate),1);

		} else
		// with startDate and endDate parameters
		if (startDate < endDate) {

			return null;//measurementRepository.findAllFromServerByDates(/*serverId,*/ new Date(startDate), new Date(endDate),1);
		} else
			return measurementRepository.findAllFromServerStream(/*serverId*/);
	}

	
	
	
	// -----------buildExcelDokument------------------------------
	private void buildExcelDokument(OutputStream os, Long serverId) throws InvalidFormatException, IOException {
		// OutpuStream os=new FileOutputStream();

		workbook = new SXSSFWorkbook(100);
		Sheet sheet = workbook.createSheet("Zeszyt 1");

		//setExcelHeader(sheet);
		setExcelRows(sheet, serverId);
		//setExcelHeader(sheet, lastColumnNumber);

		// os = new FileOutputStream("temp.xlsx");
		workbook.write(os);
		os.flush();
		workbook.dispose();
		os.close();

	}

	private void setExcelHeader(Sheet sheet, int lastColumnNumber) {
		
	
		Row excelHeader = sheet.createRow(0);
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(headerFont);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setBorderTop(CellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);

		Cell a1 = excelHeader.createCell(0);
		a1.setCellStyle(headerStyle);
		a1.setCellValue("ID");

		Cell b1 = excelHeader.createCell(1);
		b1.setCellValue("Data");
		b1.setCellStyle(headerStyle);

		for (int i = 1; i < lastColumnNumber; i++) {
			Cell c = excelHeader.createCell(i + 1);
			c.setCellValue("Czujnik " + (i));
			c.setCellStyle(headerStyle);
			sheet.autoSizeColumn(i + 1);
		}

	}

	private void setExcelRows(Sheet sheet, Long serverId) {
		
	//	Iterator<Measurement> it1 = measurements.iterator();
		

		//checking the last column number
/*		while(it1.hasNext()){
			int i=it1.next().getMeasuredValue().size();
			lastColumnNumber = Math.max(lastColumnNumber, i + 1);
		}*/
		lastColumnNumber=10;
		setExcelHeader(sheet,lastColumnNumber);
		LOG.info("dziala");

		LOG.info("serverId: " + serverId);
		// Iterable<Measurement> measurements =
		// measurementRepository.findAllFromServer(1l);

		// --------------dateStyle---------------------------
		CellStyle dateStyle = workbook.createCellStyle();
		Short dataFormat = workbook.createDataFormat().getFormat("dd-MM-YYYY HH:mm:ss");
		dateStyle.setDataFormat(dataFormat);
		dateStyle.setBorderLeft(CellStyle.BORDER_THIN);
		dateStyle.setBorderRight(CellStyle.BORDER_THIN);
		dateStyle.setBorderTop(CellStyle.BORDER_THIN);
		dateStyle.setBorderBottom(CellStyle.BORDER_THIN);

		// ------------- value style--------------
		CellStyle valueStyle = workbook.createCellStyle();
		valueStyle.setBorderLeft(CellStyle.BORDER_THIN);
		valueStyle.setBorderRight(CellStyle.BORDER_THIN);
		valueStyle.setBorderTop(CellStyle.BORDER_THIN);
		valueStyle.setBorderBottom(CellStyle.BORDER_THIN);

		//int  rowNumber = 1;
		//Iterator<Measurement> iterator = measurements.iterator();

		//while (iterator.hasNext()) {
		
		LOG.info("stream function befoure");
		try(Stream<Measurement> stream=measurementRepository.findAllFromServerStream()){
			
			LOG.info("stream function");
			stream.limit(10).forEach(m->{
				LOG.info(m.getId()+" "+m.getDate());
			/*Row row = sheet.createRow(rowNumber);
	
			
			// -----id cell---------
			Cell idCell = row.createCell(0);
			idCell.setCellValue(rowNumber);
			idCell.setCellStyle(valueStyle);

			// -----data cell-----------
			Cell dateCell = row.createCell(1);
			dateCell.setCellValue(m.getDate());
			dateCell.setCellStyle(dateStyle);*/
			// time.setCellType(Cell.);

			// ----------measuring cells---------------------
/*			for (int i = 0; i < m.getMeasuredValue().size(); i++) {

				Cell valueCell = row.createCell(i + 2); // 2 position in right
				//valueCell.setCellValue(m.getMeasuredValue().get(i));
				valueCell.setCellStyle(valueStyle);

				// set lastColumnNumber
				//lastColumnNumber = Math.max(lastColumnNumber, i + 2);
			}*/

		//	rowNumber++;
			}) ;
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
		//}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.createFreezePane(0, 1);

	}

}