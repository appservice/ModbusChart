package eu.luckyApp.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.luckyApp.model.ServerEntity;

@Repository
public interface ServerRepository extends CrudRepository<ServerEntity, Long> {
	
	//public Iterable<ServerEntity> findAll();
	

	
	
	

}
