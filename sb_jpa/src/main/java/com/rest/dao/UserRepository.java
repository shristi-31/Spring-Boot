package com.rest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rest.entities.User;

public interface UserRepository extends CrudRepository<User,Integer>{

	public List<User> findByName(String name);
	
	//custom method
	public List<User> findByNameAndCity(String name,String city);
	
	//our own method
	@Query("select u from User u")
	public List<User> getAllUsers();
	
	@Query("select u from User u where u.name=:n")
	public List<User> getUsersByName(@Param("n") String name);
	
}
