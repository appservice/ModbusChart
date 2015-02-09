package eu.luckyApp.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


//@Controller
public class StartController {
	private static  Logger LOG=Logger.getLogger(StartController.class);


	
/*	@RequestMapping(value={"/","index2"},method=RequestMethod.GET)
	public  ModelAndView getMainPage(){
		LOG.info("test");
		ModelAndView myModel=new ModelAndView();
		myModel.setViewName("/index");

		LOG.info(myModel.getViewName());
		return myModel;
	}*/
	
/*	@RequestMapping(value="/401",method=RequestMethod.GET)
	public  String getSecondPage(){
		LOG.info("trzy test");

		return "401.html";
	}*/

	
	@RequestMapping(value="/charts",method=RequestMethod.GET)
	public  String getCharts(){
		LOG.info("trzy test");

		return "index";
	}
	
	/*@RequestMapping(value = "{path}", method = RequestMethod.GET)
	public String redirect(@PathVariable String path) {
	   String route = null;
	   if (path.equals("/") || path.startsWith("/index.html")) {
	     // load index.html
	   } else {
	     route = "redirect:/index.html/#/redirect/" + path; 
	   }
	   return route;
	}*/
	
	@RequestMapping({
	    "/home",
	    "/header",
	    "/footer",
	    "/tracks",
	    "/tracks/{id:\\w+}",
	    "/location",
	    "/about"
	})
	public String index() {
	    return "forward:/index.html";
	}
}
