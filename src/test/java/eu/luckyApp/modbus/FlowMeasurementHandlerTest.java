package eu.luckyApp.modbus;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.luckyApp.modbus.service.ExcelCreator;
import eu.luckyApp.model.FilePathEntity;
import eu.luckyApp.model.Measurement;

public class FlowMeasurementHandlerTest {
	private List<Measurement> mesList;

	
	@Before
	public void setUp() throws Exception {
		mesList = new ArrayList<>();
		
		
		for(int i=0;i<10000;i++){
			Measurement m=new Measurement();
		m.setDate(new Date(new Date().getTime()+i*2000));
		for(int j=0;j<7;j++){
			m.getMeasuredValue().add(4.0+Math.cos(i/1000));
		}
		
		mesList.add(m);
		}
	//	System.out.println(mesList);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClearMeasurementList() {
			ExecutorService es = Executors.newSingleThreadExecutor();

		//	List seriesNames = serverRepository.findOne(1L).getSensorsName();
			
			List<String> seriesNames=Arrays.asList(new String[]{"jeden","dwa","trzy","cztery","pięć","sześć","siedem"});
			Future<FilePathEntity> fpe = es.submit(new ExcelCreator(mesList, seriesNames));
			long currentTime=System.currentTimeMillis();
			System.out.println("czas przed clear: "+(System.currentTimeMillis()-currentTime));

			mesList=new ArrayList<>();
			System.out.println("czas po clear: "+(System.currentTimeMillis()-currentTime));

			
			try {

				System.out.println(fpe.get().getAbsolutePath());
				System.out.println("czas po future: "+(System.currentTimeMillis()-currentTime));
	
			
			}catch(Exception  e){
				e.printStackTrace();
				
			}
			es.shutdown();
			
		//	mesList.clear();
		
		
	}

}
