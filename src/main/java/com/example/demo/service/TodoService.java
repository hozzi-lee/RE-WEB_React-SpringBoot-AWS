package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	public List<TodoEntity> create(final TodoEntity entity) {
		
		//Validations
		/*
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user.");
		}
		*/
		validate(entity);
		
		repository.save(entity);
		
		log.info("Entity Id : {} is saved.", entity.getUserId());
		
		return repository.findByUserId(entity.getUserId());
	}
	
	public List<TodoEntity> retrieve(final String userId) {
		log.debug("$$$$$ TodoService.retrieve in userId::: {}", userId);
		return repository.findByUserId(userId);
	}
	
	// 리팩토링한 메서드
	private void validate(final TodoEntity entity ) {
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user.");
		}
	}
}