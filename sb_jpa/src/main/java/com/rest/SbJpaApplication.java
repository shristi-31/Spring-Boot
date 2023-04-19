package com.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.rest.dao.UserRepository;
import com.rest.entities.User;

@SpringBootApplication
public class SbJpaApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SbJpaApplication.class, args);
		UserRepository userRepo = ctx.getBean(UserRepository.class);
		User u1=new User();
		u1.setName("pappu");
		u1.setCity("bhopal");
		u1.setStatus("busy");
		
		User u2=new User();
		u2.setName("gappu");
		u2.setCity("indore");
		u2.setStatus("active");
		
	//saving single user	userRepo.save(u1);
		//userRepo.save(u2);
		
		ArrayList<User> list=new ArrayList<>();
		list.add(u1);
		list.add(u2);
		
		//saving multiple users at a same time
		userRepo.saveAll(list);
		
		//update the user of id 8
		Optional<User> optional= userRepo.findById(9);
		User u = optional.get();
		u.setName("mukku");
		userRepo.save(u);
		System.out.println(u);
		
		//getting the data
		//findById -> get()
		//findAll
		Iterable<User> itr = userRepo.findAll();
		//one way to iterate
		Iterator<User> iterator = itr.iterator();
		while(iterator.hasNext())
		{
			User user=iterator.next();
			System.out.println(user);
		}
		
		//second way to iterate
		itr.forEach(new Consumer<User>() {

			@Override
			public void accept(User t) {
				// TODO Auto-generated method stub
			System.out.println(t);	
			}
		
		});
		//or
		itr.forEach(user->{System.out.println(user);});
		
		
		//deleting the user
		//delete(),deleteAll(),deleteById()
		userRepo.deleteById(28);
		
		
		
		System.out.println("here is the result acc. to name");
		//custom finder methods
		List<User> l = userRepo.findByName("pappu");
		l.forEach(e->System.out.println(e));
		
		System.out.println("here is the result acc. to name and city");
		//custom finder methods
		List<User> li = userRepo.findByNameAndCity("gappu","indore");
		li.forEach(e->System.out.println(e));
		
	
		//firing our own queries using 1. @query JPQL(just like hql)  2. native queries
		//1.
		System.out.println("all users");
		List<User> lis=userRepo.getAllUsers();
		lis.forEach(e->System.out.println(e));
		
		System.out.println("---------");
		List<User> listt=userRepo.getUsersByName("mukku");
		listt.forEach(e->System.out.println(e));
		

	}

}
