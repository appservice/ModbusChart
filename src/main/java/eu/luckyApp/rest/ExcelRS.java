package eu.luckyApp.rest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Path("/download")
public class ExcelRS {

	Workbook workbook;
	File file;

	@GET
	@Path("/excel")
	@Produces("application/vnd.ms-excel")
	public Response getFile() {


		StreamingOutput soutput = new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

				try {
					buildExcelDokument(output);
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				}
			}
		};

		ResponseBuilder response = Response.ok(soutput);
		response.header("Content-Disposition", "attachment; filename=dane.xlsx");
		return response.build();
	}

	private void buildExcelDokument(OutputStream os) throws InvalidFormatException, IOException {
		// OutpuStream os=new FileOutputStream();

		workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Zeszyt 1");
		setExcelHeader(sheet, 8);

		
		//os = new FileOutputStream("temp.xlsx");
		workbook.write(os);
		os.close();

	}

	private void setExcelHeader(Sheet sheet, int dataNumber) {
		Row excelHeader = sheet.createRow(0);
		
		Font headerFont=workbook.createFont();
		headerFont.setBold(true);
		CellStyle headerStyle=workbook.createCellStyle();
		headerStyle.setFont(headerFont);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		
		
		Cell a1=excelHeader.createCell(0);
		a1.setCellStyle(headerStyle);
		a1.setCellValue("ID");
		
		
		Cell b1=excelHeader.createCell(1);
		b1.setCellValue("Data");
		b1.setCellStyle(headerStyle);
		
		Cell c1=excelHeader.createCell(2);
		c1.setCellValue("Czas");
		c1.setCellStyle(headerStyle);

		for (int i = 0; i < dataNumber; i++) {
			Cell c=excelHeader.createCell(i + 3);
			c.setCellValue("Czujnik " + (i + 1));
			c.setCellStyle(headerStyle);
			sheet.autoSizeColumn(i + 3);
		}

	}

	private void setExcelRows(Sheet sheet, int dataNumber) {

	}

}
