package eu.luckyApp.model;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends CrudRepository<ServerEntity, Long> {
	
	//public Iterable<ServerEntity> findAll();
	

	
	
	

}
