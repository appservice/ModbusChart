package eu.luckyApp.modbus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.luckyApp.model.FilePathEntity;
import eu.luckyApp.model.Measurement;

public class ExcelCreatorTest {

	private ExcelCreator ec;
	List<Measurement> testList;
	List<String> seriesNames;
	//private Measurement m;

	@Before
	public void setUp() throws Exception {
		
		this.testList=createMockList();
	
		seriesNames=new ArrayList<>();
		seriesNames.add("iperwsza");
		seriesNames.add("druga");
		seriesNames.add("trzecia seria");
		ec=new ExcelCreator(testList,seriesNames);
		
	}

	/**
	 * 
	 */
	private List<Measurement>  createMockList() {
		List<Measurement> myTestList = new ArrayList<>();				
		Measurement m=new Measurement();
		m.setDate(new Date());		
		m.setEnergyConsumption(34.8);
		List<Double>testValue1=DoubleStream.iterate(0, n->n+3).limit(1).boxed().collect(Collectors.toList());
		m.setMeasuredValue(testValue1);
		
		Measurement m2=new Measurement();
		long time=new Date().getTime();
		m2.setEnergyConsumption(22.1);
		m2.setDate(new Date(time+60000) );		
		List<Double>testValue2=DoubleStream.iterate(0, n->n+5).limit(7).boxed().collect(Collectors.toList());
		m2.setMeasuredValue(testValue2);
		Measurement m3=new Measurement();
		long time3=new Date().getTime();
		m3.setDate(new Date(time3+60000) );		
		List<Double>testValue3=DoubleStream.iterate(0, n->n+1).limit(7).boxed().collect(Collectors.toList());
		m3.setMeasuredValue(testValue3);
		
		for(int j=0;j<2000;j++){
			myTestList.add(m);
			myTestList.add(m2);
		}
		
	//	myTestList.add(m);
	//	myTestList.add(m2);
	//	myTestList.add(m3);
		return myTestList;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExcelCreator() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCall() {
		try {
			
			ExecutorService e=Executors.newSingleThreadExecutor();
			Future<FilePathEntity> f=	e.submit(ec);
			
			System.out.println(f.get().getAbsolutePath());
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
