package eu.luckyApp;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

/**
 * Created by luke on 30.04.16.
 */
@WebAppConfiguration
public class AbstractControllerTest extends AbstractSpringTest {

    protected MockMvc mockMvc;

  //  @Autowired
 //   protected WebApplicationContext context;

  /*  protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
*/
    protected void setUp(Object controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }

    protected String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException, JsonParseException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

}
