package playground.yo.assertion.gen1;

import org.junit.Test;

import playground.yo.assertion.AbstractTest;

public class SimpleAssert extends AbstractTest{

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * NEEDS -ea in JVM option to work
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	
	@Test
	public void test_null(){
		assert nullstr == null;
	}
	
	
	@Test
	public void test_error(){
		assert "super string".equals(nullstr);
	}
	
	@Test
	public void test_error_with_message() {
		assert ("super string".equals(nullstr)) : "nullstr pas égal à superstring";
	}


	@Override
	public void test_string_equal() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void test_not_null() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void test_string_partial() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void test_is_in_collection() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void test_object_attribut_in_collection() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void test_collection_size_contains() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void test_exception() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void test_error_wo_message() {
		// TODO Auto-generated method stub
		
	}
	
}
