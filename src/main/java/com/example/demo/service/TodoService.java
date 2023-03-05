package com.example.demo.service;

import java.util.List;
import java.util.Optional;

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
	
	public List<TodoEntity> update(final TodoEntity entity) {
		
		//(1) 저장할 엔티티가 유효한지 확인.
		validate(entity);
		
		//(2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 불가능.
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		/* 일반
		if(original.isPresent()) {
			final TodoEntity todo = original.get();
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			repository.save(todo);
		}
		*/
		//Lambda
		original.ifPresent(todo -> {
			//(3) 반환된 TodoEntity가 존재하면 값을 새 Entity 값으로 덮어 씌움.
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			//(4) 데이터베이스에 새 값을 저장.
			repository.save(todo);
		});
		
		// Retrieve Todo에서 만든 메서드를 이용해 사용자의 모든 Todo 리스트를 리턴
		return retrieve(entity.getUserId());
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