package br.ce.wcaquino.taskbackend.controller;

import static org.mockito.Mockito.validateMockitoUsage;

import java.time.LocalDate;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;
import org.junit.Assert;
import org.junit.Before;

public class TaskControllerTest {

	@Mock
	private TaskRepo taskRepo;
	
	// esse comando injeta o mock taskRepo dentro da classe TaskController
	@InjectMocks
	private TaskController controller;
	
	@Before
	public void Setup()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void ShouldntSaveTaskWithoutDescription()
	{
		Task todo = new Task();
		todo.setDueDate(LocalDate.now());
		
		try {
			controller.save(todo);
			Assert.fail("Deveria ter gerado a exceção!");
		} catch (ValidationException e) {
			Assert.assertEquals("Fill the task description", e.getMessage());
		}
	}
	
	@Test
	public void ShouldntSaveTaskWithoutDate()
	{
		Task todo = new Task();
		todo.setTask("Descrição");
		
		try {
			controller.save(todo);
			Assert.fail("Deveria ter gerado a exceção!");
		} catch (ValidationException e) {
			Assert.assertEquals("Fill the due date", e.getMessage());
		}
	}
	
	@Test
	public void ShouldntSaveTaskWithPastDate()
	{
		Task todo = new Task();
		todo.setTask("Descrição");
		todo.setDueDate(LocalDate.of(2010, 01, 01));
		
		try {
			controller.save(todo);
			Assert.fail("Deveria ter gerado a exceção!");
		} catch (ValidationException e) {
			Assert.assertEquals("Due date must not be in past", e.getMessage());
		}
	}
	
	@Test
	public void ShouldSaveTaskSuccessfully() throws ValidationException
	{
		Task todo = new Task();
		todo.setTask("Descrição");
		todo.setDueDate(LocalDate.now());
				
		controller.save(todo);
		
		// verifica se o taskRepo foi invocado no método save usando o parâmetro todo
		Mockito.verify(taskRepo).save(todo);
	}
}
