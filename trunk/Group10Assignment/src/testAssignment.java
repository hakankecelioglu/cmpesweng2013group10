import static org.junit.Assert.*;

import org.junit.Test;


public class testAssignment {

	@Test
	public void test() {
		Assignment tester = new Assignment();
		assertEquals("Dilara must be 5",5,tester.Dilara());
		assertEquals("Can must be 8",8,tester.Can(3,5));	
		assertEquals("Sabri must be true", true, tester.Sabri());
		assertEquals("Alper must be false", false, tester.Alper());
		assertEquals("Anil must be true", true, tester.Anil());
		assertEquals("Seyma must be 0", 0, tester.Seyma());
		fail("test failed");	
	}

}
