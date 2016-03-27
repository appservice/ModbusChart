package eu.luckyApp.modbus.service;

import eu.luckyApp.repository.FilePathRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Private company LuckyApp
 * Created by LMochel on 2016-03-21.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeletingDataServiceTest {

   @Mock
    FilePathRepository filePathRepository;

    DeletingDataService dds;

    @Before
    public void setUp() throws Exception {


        dds=new DeletingDataService();

    }

    @Test
    @Ignore
    public void deleteOlderThanYear() throws Exception {
        dds.deleteOlderThanYear();
    }
}