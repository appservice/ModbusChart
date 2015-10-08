package eu.luckyApp.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.luckyApp.model.FilePathEntity;

@Repository
public interface FilePathRepository extends JpaRepository<FilePathEntity, Long> {

	@Query("SELECT f FROM FilePathEntity f WHERE   f.creationDate <:date")
	public Iterable<FilePathEntity> findOlderThan(@Param("date") Date date);
	
}
