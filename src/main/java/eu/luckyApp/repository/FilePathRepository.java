package eu.luckyApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.luckyApp.model.FilePathEntity;

@Repository
public interface FilePathRepository extends JpaRepository<FilePathEntity, Long> {

}
