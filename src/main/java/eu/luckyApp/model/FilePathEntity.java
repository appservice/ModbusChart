package eu.luckyApp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy = false)
public class FilePathEntity {

 @Id
 @GeneratedValue(strategy=GenerationType.AUTO)
 private Long id;
 
 @NotNull
 private String absolutePath;
 private String fileName;
 
 private String comment;
/**
 * @return the comment
 */
public String getComment() {
	return comment;
}

/**
 * @param comment the comment to set
 */
public void setComment(String comment) {
	this.comment = comment;
}

/**
 * @return the id
 */
public Long getId() {
	return id;
}

/**
 * @return the absolutePath
 */

public String getAbsolutePath() {
	return absolutePath;
}
/**
 * @return the fileName
 */
public String getFileName() {
	return fileName;
}

/**
 * @param id the id to set
 */
public void setId(Long id) {
	this.id = id;
}

/**
 * @param absolutePath the absolutePath to set
 */
public void setAbsolutePath(String absolutePath) {
	this.absolutePath = absolutePath;
}

/**
 * @param fileName the fileName to set
 */
public void setFileName(String fileName) {
	this.fileName = fileName;
}
 
 

}
