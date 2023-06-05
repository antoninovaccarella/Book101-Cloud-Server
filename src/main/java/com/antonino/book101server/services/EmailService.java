package com.antonino.book101server.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:email.properties")
public class EmailService {
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String ourmail;

    @Value("${website.url}")
    private String url;

    @Value("${mail.registration.object}")
    private String registrationObject;
    @Value("${mail.registration.content}")
    private String registrationContent;

    @Value("${mail.changeEmail.object}")
    private String changeEmailObject;
    @Value("${mail.changeEmail.content}")
    private String changeEmailContent;

    @Value("${mail.rentaddholder.object}")
    private String rentAddHolderObject;
    @Value("${mail.rentaddholder.content}")
    private String rentAddHolderContent;

    @Value("${mail.forgotpassword.object}")
    private String forgotPasswordObject;
    @Value("${mail.forgotpassword.content}")
    private String forgotPasswordContent;


}
