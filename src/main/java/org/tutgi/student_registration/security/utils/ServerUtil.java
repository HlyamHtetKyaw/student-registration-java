package org.tutgi.student_registration.security.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.tutgi.student_registration.config.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServerUtil{

	private final RedisTemplate<String, String> redisTemplate;
	private static final String OTP_PREFIX = "otp:user:";
	private final JavaMailSender javaMailSender;
	private final EmailService emailService;
	
    public static String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    
    public void sendCodeToEmail(final String email , final long EXPIRATION_MINUTES , final String templateName) {
        String otp = generateOtp();
        redisTemplate.opsForValue().set(OTP_PREFIX  + email, otp, EXPIRATION_MINUTES, TimeUnit.MINUTES);
        try {
            sendEmail(email , otp , templateName);
        } catch (MessagingException | IOException e){
            e.printStackTrace();
        }
    }

    private void sendEmail(String email, String resetCode , String templateName) throws MessagingException , IOException{
        String userName = email.split("@")[0];
        String htmlTemplate = loadTemplate("templates/mailTemplates/"+ templateName +".html");
        String htmlContent =htmlTemplate
                .replace("{{username}}" , userName)
                .replace("{{code}}" , resetCode);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true , "UTF-8");

        helper.setText(htmlContent , true);
        helper.addInline("logoImage", new ClassPathResource("templates/logo/logo.png"));

        this.emailService.sendEmail(email, "Please Verify Your Email", htmlContent);
    }
    
    public String loadTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        InputStream inputStream = resource.getInputStream();

        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);

        return new String(bytes, StandardCharsets.UTF_8);
    }
}