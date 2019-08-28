package com.app.javaMail.controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.app.javaMail.model.Mail;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class SendEmailController {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@RequestMapping(value = "sendEmail", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addImage(@RequestParam("email") String jsonStr) {
	
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			Mail mail = new ObjectMapper().readValue(jsonStr, Mail.class);
			Map<String, Object> data = new HashMap<String, Object>();
			
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Context context = new Context();
		//	context.setVariables(mail.getModel());
			String html = templateEngine.process("path for html page in templates  or fontend folder or html page name in templates folder ", context);

			helper.setTo(mail.getTo());
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom("from-email Id");
			emailSender.send(message);
			
			response.put("email", data);
			response.put("status", "OK");
			response.put("code", "200");
			response.put("message", "Email Send successfully.");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("data", "");
			response.put("status", "ERROR");
			response.put("code", "500");
			response.put("message", "Error while sending email");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
