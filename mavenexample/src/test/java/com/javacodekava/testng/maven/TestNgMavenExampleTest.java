package com.javacodekava.testng.maven;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestNgMavenExampleTest {
  
  public void exampleOfTestMaven() {
	    System.out.println("test a");
	  
  }
  
  @Parameters({"some text"})
  @Test
  public void exampleOfTestMaven_2(@Optional("some OPTIONAL text")String txt) {
	  System.out.println("test BB " + txt);
	  
  }
  
  @Test
  public void exampleOfTestMaven_3() {
	  System.out.println("test CCC");
	  System.out.println(Consts_test.a);
	  
  }
  
}
