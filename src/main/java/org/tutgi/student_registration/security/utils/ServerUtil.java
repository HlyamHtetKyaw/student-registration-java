package org.tutgi.student_registration.security.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.tutgi.student_registration.config.exceptions.ExpiredException;
import org.tutgi.student_registration.config.exceptions.InvalidOtpException;
import org.tutgi.student_registration.config.service.EmailService;
import org.tutgi.student_registration.data.redis.RedisKeys;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServerUtil{

	private final RedisTemplate<String, String> redisTemplate;
	
	@Value("${otp.expiration.minutes}")
	private long otpExpirationMinutes;
	
	@Value("${verified.expiration.minutes}")
	private long verifiedExpirationMinutes;
	
	@Value("${frontend.login.url}")
	private String frontendUrl;
	
	private final EmailService emailService;
	
    public static String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    
    public static String generatePassword() {
        int length = 9;
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*";
        String all = upper + lower + digits + special;

        SecureRandom rand = new SecureRandom();
        List<Character> pwd = new ArrayList<>(Arrays.asList(
            upper.charAt(rand.nextInt(upper.length())),
            lower.charAt(rand.nextInt(lower.length())),
            digits.charAt(rand.nextInt(digits.length())),
            special.charAt(rand.nextInt(special.length()))
        ));

        while (pwd.size() < length)
            pwd.add(all.charAt(rand.nextInt(all.length())));

        Collections.shuffle(pwd, rand);
        StringBuilder sb = new StringBuilder();
        pwd.forEach(sb::append);
        return sb.toString();
    }
    
    public void sendCodeToEmail(final String email, final String templateName,final String expTime) {
        String otp = generateOtp();
        redisTemplate.opsForValue().set(RedisKeys.OTP_PREFIX  + email, otp, otpExpirationMinutes, TimeUnit.MINUTES);
        try {
        	sendOtpEmail(email , otp , templateName,expTime);
        } catch (MessagingException | IOException e){
            e.printStackTrace();
        }
    }
    
    public void sendPasswordToEmail(final String email, final String templateName,final String password) {
        try {
            sendPasswordEmail(email, password, templateName);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    
    public long verifyOtp(final String email,final String submittedOtp) {
    	String key = RedisKeys.OTP_PREFIX + email;
    	String storedOtp = redisTemplate.opsForValue().get(key);

    	if (storedOtp == null) {
    	    throw new ExpiredException("OTP expired or not found");
    	}

    	if (!storedOtp.equals(submittedOtp)) {
    	    throw new InvalidOtpException("Incorrect OTP");
    	}
    	redisTemplate.delete(key);
    	redisTemplate.opsForValue().set(RedisKeys.VERIFIED_PREFIX+email, "passed",verifiedExpirationMinutes,TimeUnit.MINUTES);
    	return verifiedExpirationMinutes;
    }
    
    public boolean isVerified(final String email) {
    	return redisTemplate.hasKey(RedisKeys.VERIFIED_PREFIX + email);
    }
    
    public void deleteVerify(final String email) {
    	redisTemplate.delete(RedisKeys.VERIFIED_PREFIX + email);
    }
    
    
    private void sendOtpEmail(String email, String resetCode , String templateName,String expTime) throws MessagingException , IOException{
        String userName = email.split("@")[0];
        String htmlTemplate = loadTemplate("templates/mailTemplates/"+ templateName +".html");
        String htmlContent =htmlTemplate
                .replace("{{username}}" , userName)
                .replace("{{code}}" , resetCode)
                .replace("{{expTime}}", expTime);
        this.emailService.sendEmail(email, "Please Verify Your Email", htmlContent);
    }
    
    private void sendPasswordEmail(String email, String password, String templateName) throws MessagingException, IOException {
        String userName = email.split("@")[0];
        String htmlTemplate = loadTemplate("templates/mailTemplates/" + templateName + ".html");
        String htmlContent = htmlTemplate
            .replace("{{username}}", userName)
            .replace("{{email}}", email)
            .replace("{{password}}", password)
            .replace("{{frontendUrl}}", frontendUrl);
        this.emailService.sendEmail(email, "Your Account Is Created", htmlContent);
    }
    public String loadTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        InputStream inputStream = resource.getInputStream();

        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);

        return new String(bytes, StandardCharsets.UTF_8);
    }
}