package com.ebay.url.processor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationTest {
	Application application;

	@BeforeEach
	void setUp() throws Exception {
		application=new Application();
	}

	@Test
	void mainTestWithSingleArgs() {
		application.main(new String[] {"inputData"});
		
	}
	
	@Test
	void mainTestWithNoArgs() {
		application.main(new String[] {});
		
	}

	@Test
	void mainTestWithEmptyArgs() {
		application.main(new String[] {""});
		
	}
	
	@Test
	void mainTestWithEmptySpaceArgs() {
		application.main(new String[] {" "});
		
	}


}
