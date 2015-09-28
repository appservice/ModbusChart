package eu.luckyApp.settings.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
@Query("SELECT u FROM User u WHERE u.name=:userName ")
public User	findUserByName(@Param("userName") String name);


@Query("SELECT u FROM User u  WHERE (:role MEMBER OF roles))")
public List<User> findUsersByRole(@Param("role")Role role);

}
