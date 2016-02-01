package eu.luckyApp.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Path("/preferences")
@Produces(MediaType.APPLICATION_JSON_VALUE)
//@Scope("singleton")
// @Consumes(MediaType.APPLICATION_JSON_VALUE)
public class PrefsRS {

	Logger LOG=Logger.getLogger(PrefsRS.class);
	Preferences prefs;

	@PostConstruct
	private void init() {
		try {
			
			
			if(!Preferences.userRoot().nodeExists("modbuschart")){	
				prefs=	Preferences.userRoot().node("modbuschart");
				LOG.warn("CREATING DEFAULT PREFERENCE");
				prefs.putDouble("currentPrice", 0.5);
			}else{
				prefs=	Preferences.userRoot().node("modbuschart");
			}
			
		} catch (BackingStoreException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}

	}

	@GET
	public Response getAllPreferences() {

		Map<String, Object> prefsMap = new HashMap<>();
		try {
			String[] keys = prefs.keys();
			for (String key : keys) {
				prefsMap.put(key, prefs.get(key, null));
			}

			return Response.ok(prefsMap).build();
		} catch (BackingStoreException e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).header("error", e.toString()).build();
		}

	}

	@GET
	@Path("/{key}")
	public Response getPreference(@PathParam("key") String key) {

		Object o = prefs.get(key, null);
		if (o != null) {
			return Response.ok(o).build();
		}

		return Response.noContent().header("error", "Preference " + key + "not found!").build();

	}

	@PUT
	@Path("/{key}")
	public Response updatePreference(@PathParam("key") String key, String value) {

		// prefs.
		boolean exists = prefs.get(key, null) != null;
		if (exists) {
			prefs.put(key, value);
			return Response.accepted(value).build();
	}
		return Response.notModified().header("error", "Preference " + key + "not found!").build();

	}
	
/*	@POST
	public Response createPreference(Pref pref){
		
	}*/

}
