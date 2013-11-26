package com.groupon.web.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.groupon.web.AbstractGrouponTest;
import com.groupon.web.dao.model.User;
import com.groupon.web.exception.GrouponException;
import com.groupon.web.util.GrouponWebUtils;

public class MailTest extends AbstractGrouponTest {
	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	@Value("${SITE_URL}")
	private String SITE_URL;

	@Test
	public void sendWelcomeMail() throws GrouponException {
		User user = userService.getUserByUsernameAndPassword("admin", GrouponWebUtils.hashPasswordForDB("123456"));
		assertNotNull(user);

		String userCode = GrouponWebUtils.generateUserHash(user);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("signup_approval_link", SITE_URL + "emailApproval?code=" + userCode);
		params.put("signup_username", "Crazy MC");

		try {
			mailService.sendEmail("mails/signupMail.vm", "serdarkuzucu13@gmail.com", "Test Welcome", params, "serdar.kuzucu@inomera.com");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
