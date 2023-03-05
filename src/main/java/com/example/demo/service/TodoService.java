package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

@Service
public class TodoService {
	/*
	public String testService() {
		return "Test Service";
	}
	*/

	@Autowired
	private TodoRepository repository;
	
	public String testService() {
		// TodoEntity 생성
		TodoEntity entity = TodoEntity.builder().title("My first Todo item").build();
// -----
		/* TodoEntity 저장
		 * INSERT INTO TodoRepository
		 * 				(TITLE)
		 * VALUES ("My first Todo item")
		 */
		repository.save(entity);
// -----
		/* TodoEntity 검색
		 * SELECT *
		 * FROM TodoRepository
		 * WHERE ID = entity의 자동 생성된 uuid ID
		 */
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		return savedEntity.getTitle();
	}
}
