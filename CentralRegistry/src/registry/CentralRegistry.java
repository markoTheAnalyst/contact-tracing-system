package registry;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.GET;

@Path("/patients")
public class CentralRegistry {
	PatientService service;

	public CentralRegistry() {
		this.service = new PatientService();
	}
	
	@GET
	@Path("/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByToken(@PathParam("token") String token) {
		return Response.status(200).entity("ej ok").build();
		/*Patient patient = service.getByToken(token);
		if (patient != null) {
			return Response.status(200).entity(patient).build();
		} else {
			return Response.status(404).build();
		}*/
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByToken() {
		return Response.status(200).entity("ej ok").build();
		/*Patient patient = service.getByToken(token);
		if (patient != null) {
			return Response.status(200).entity(patient).build();
		} else {
			return Response.status(404).build();
		}*/
	}

}
