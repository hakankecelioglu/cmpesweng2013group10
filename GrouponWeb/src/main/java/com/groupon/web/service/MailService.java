package com.groupon.web.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.groupon.web.util.GrouponLogger;

@Component
public class MailService {
	private GrouponLogger logger = GrouponLogger.getLogger(this.getClass());

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Value("${mail.from}")
	private String mailsFrom;

	@Value("${SITE_URL}")
	private String SITE_URL;
	/**
	 * send an email to user
	 * @param email target email address
	 * @param subject subject of email address
	 * @param msg message content
	 * @param replyTo reply to field of email
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void sendMail(String email, String subject, String msg, String replyTo) throws AddressException, MessagingException, UnsupportedEncodingException {
		logger.debug("sending email to::{0}::subject::{1}", email, subject);

		InternetAddress fromAddress = new InternetAddress(mailsFrom);
		fromAddress.setPersonal("Task Together");

		MimeMessage message = mailSender.createMimeMessage();
		message.setFrom(fromAddress);
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
		message.setSubject(subject);
		message.setContent(msg, "text/html");

		if (replyTo != null) {
			InternetAddress[] replyTos = { new InternetAddress(replyTo) };
			message.setReplyTo(replyTos);
		}

		mailSender.send(message);
	}
	/**
	 * 
	 * @param velocityTemplateName email template
	 * @param to who gets the mail
	 * @param subject	subject of mail
	 * @param params parameters of email
	 * @param replyTo reply to field of email
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void sendEmail(String velocityTemplateName, String to, String subject, Map<String, Object> params, String replyTo) throws AddressException, MessagingException,
			UnsupportedEncodingException {
		logger.debug("sendEmail(velocityTemplateName={0}, to={1}, subject={2})", velocityTemplateName, to, subject);

		params.put("_mail_to", to);
		params.put("_mail_subject", subject);
		params.put("SITE_URL", SITE_URL);

		String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, velocityTemplateName, "UTF-8", params);

		sendMail(to, subject, body, replyTo);
	}

}
