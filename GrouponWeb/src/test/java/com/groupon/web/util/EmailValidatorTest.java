package com.groupon.web.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Email validator Testing
 */
@RunWith(JUnit4.class)
public class EmailValidatorTest {

	public String[] validEmailProvider() {
		return new String[] { "mkyong@yahoo.com", "mkyong-100@yahoo.com", "mkyong.100@yahoo.com", "mkyong111@mkyong.com", "mkyong-100@mkyong.net", "mkyong.100@mkyong.com.au",
				"mkyong@1.com", "mkyong@gmail.com.com", "mkyong+100@gmail.com", "mkyong-100@yahoo-test.com" };
	}

	public String[] invalidEmailProvider() {
		return new String[] { "mkyong", "mkyong@.com.my", "mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com", ".mkyong@mkyong.com", "mkyong()*@gmail.com",
				"mkyong@%*.com", "mkyong..2002@gmail.com", "mkyong.@gmail.com", "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a" };
	}

	@Test
	public void validEmailTest() {
		EmailValidator emailValidator = new EmailValidator();
		String[] emails = validEmailProvider();

		for (String temp : emails) {
			boolean valid = emailValidator.validate(temp);
			System.out.println("Email is valid : " + temp + " , " + valid);
			Assert.assertEquals(valid, true);
		}

	}

	@Test
	public void inValidEmailTest() {
		EmailValidator emailValidator = new EmailValidator();
		String[] emails = invalidEmailProvider();

		for (String temp : emails) {
			boolean valid = emailValidator.validate(temp);
			System.out.println("Email is valid : " + temp + " , " + valid);
			Assert.assertEquals(valid, false);
		}
	}
}