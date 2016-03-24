package com.javacodekava.testng.maven;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class NewTestNG2 {

	  public void newTestNG() {
		    System.out.println("test ng 1");
		  
	  }
	  
	  @Parameters({"some text"})
	  @Test
	  public void newTestNG_2(@Optional("some OPTIONAL text from test ng 2")String txt) {
		  System.out.println("test ng 2 " + txt);
		  
	  }
	  
	  @Test
	  public void newTestNG_3() {
		  System.out.println("test ng 3");
		  System.out.println(Consts_test.a);
		  
	  }
}
