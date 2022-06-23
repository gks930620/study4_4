package com.study.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.study.free.vo.FreeBoardVO;
import com.sun.net.httpserver.HttpServer;

public class SimpleExcelFile {
	// Workbook wb = new HSSFWorkbook(); //xls
	private XSSFWorkbook wb; // xlsx
	private Sheet sheet;
	private Row row;
	private Cell cell;
	private Class<?> clazz;
	private List<?> list;

	public SimpleExcelFile(String sheetName, Class<?> clazz, List<?> list)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		wb = new XSSFWorkbook(); // xlsx
		sheet = wb.createSheet(sheetName);
		this.list = list;
		this.clazz = clazz;
		setExcelCoulmnName();
		setExcelBody();
	}

	private void setExcelCoulmnName() {
		row = sheet.createRow(0);
		Field[] fields = clazz.getDeclaredFields(); // 일단 Declared로 했는데 나중에 상속된것관계까지고려한다면 그냥 필드로 바꾸고
													// setAccessible(true)로 바꿔주면 되긴함
		for (int i = 0; i < fields.length; i++) {
			// System.out.println("필드이름 : "+field.getName()+ " 필드 타입:"+field.getType());
			ExcelColumn excelColumn = fields[i].getAnnotation(ExcelColumn.class);
			if (excelColumn != null) {
				cell = row.createCell(i);
				cell.setCellValue(excelColumn.headerName());
			}
		}
	}

	// reflection으로 모든 메소드 실행해야지, get
	private void setExcelBody()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			int rowNum = 0;

			// N^2은 너무 길어..
			Field[] fields = clazz.getDeclaredFields();
			for (int j = 0; j < fields.length; j++) {
				cell = row.createCell(rowNum++);
				ExcelColumn excelColumn = fields[j].getAnnotation(ExcelColumn.class);
				if (excelColumn != null) {
					String methodName = fields[j].getName();
					methodName = methodName.replace(methodName.substring(0, 1),
							methodName.substring(0, 1).toUpperCase());
					try {
						Method method = clazz.getMethod("get" + methodName);
						Object obj = method.invoke(list.get(i));
						// DB에 null값이 있을 수 있으니.
						if (obj != null)
							cell.setCellValue(obj.toString());
					} catch (NoSuchMethodException e) {
						System.out.println("VO 만들 때   get메소드   getFiled1()  이런식으로 안 만들었나???");
					}

				}
			}

		}
	}

	public void write(OutputStream outputStream) throws IOException {
		wb.write(outputStream);
		// wb.close(); //xlsx는 close자동으로 해줌. xls 할거면 close따로 해주기
	}

}
