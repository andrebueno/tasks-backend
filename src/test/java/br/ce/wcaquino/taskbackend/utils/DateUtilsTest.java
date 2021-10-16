package br.ce.wcaquino.taskbackend.utils;

import java.time.LocalDate;

import org.junit.Test;

import org.junit.Assert;

public class DateUtilsTest {

	@Test
	public void shouldReturnTrueForFutureDates()
	{
		LocalDate futureDate = LocalDate.of(2030, 01, 01);
		
		boolean fReturn = DateUtils.isEqualOrFutureDate(futureDate);
		
		Assert.assertTrue(fReturn);
	}
	
	@Test
	public void shouldReturnFalseForPastDate()
	{
		LocalDate futureDate = LocalDate.of(2010, 01, 01);
		
		boolean fReturn = DateUtils.isEqualOrFutureDate(futureDate);
		
		Assert.assertFalse(fReturn);
	}
	
	@Test
	public void shouldReturnTrueForPresentDate()
	{
		LocalDate futureDate = LocalDate.now();
		
		boolean fReturn = DateUtils.isEqualOrFutureDate(futureDate);
		
		Assert.assertTrue(fReturn);
	}
}
