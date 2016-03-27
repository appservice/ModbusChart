package eu.luckyApp.rest;

import eu.luckyApp.model.FilePathEntity;
import eu.luckyApp.repository.FilePathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Path(value = "/filepathes")
@Produces(MediaType.APPLICATION_JSON)
public class FilePathRS {

	@Autowired
	private FilePathRepository filePathRepository;

	@GET
	public Page<FilePathEntity> getAll(@QueryParam(value = "page")@DefaultValue("1") int page,@QueryParam("size")@DefaultValue("31") int size) {
		Pageable pageable=new PageRequest(page, size,Sort.Direction.DESC,"id");
		//Collections.reverse(myList);
		return filePathRepository.findAll(pageable);

	}

	@GET
	@Path("/{id}")
	public Response getOne(@PathParam("id") Long id) {
		return Response.ok(filePathRepository.getOne(id)).build();
	}

	
	
	@GET
	@Path("/{id}/file")
	@Produces("application/vnd.ms-excel")
	public Response getFile(@PathParam("id") Long id) {
		StreamingOutput soutput = output -> {
            java.nio.file.Path file=Paths.get(filePathRepository.getOne(id).getAbsolutePath());
            byte[] buffer = Files.readAllBytes(file);
            output.write(buffer);


        };
		
		ResponseBuilder response = Response.ok(soutput);
		String fileName=filePathRepository.getOne(id).getFileName();
		response.header("Content-Disposition", "attachment; filename="+fileName);
		
		return response.build();
	}

	
	@DELETE
	@Path("{id}")
	public Response deleteOne(@PathParam("id")Long id){
		FilePathEntity filePath=filePathRepository.findOne(id);
		java.nio.file.Path file=Paths.get(filePath.getAbsolutePath());
		try {
			//if(file.toFile().exists())
			Files.deleteIfExists(file);
			filePathRepository.delete(id);
			return Response.noContent().build();
		
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().header("error", e).build();
			
		}
		
		
	}
}
