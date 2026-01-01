package org.tutgi.student_registration.core.nrc.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.core.nrc.models.NRCData;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Getter
public class NrcDataLoadingService {
	private NRCData nrcData;
	
	@Getter(AccessLevel.NONE)
	@Value("classpath:data/NRC_Data.min.json")
	private Resource nrcDataResource;

	@PostConstruct
	public void load() throws IOException {
		log.info("Before nrc data initialize: {}", nrcData);
		ObjectMapper objectMapper = new ObjectMapper();
		this.nrcData = objectMapper.readValue(nrcDataResource.getInputStream(), NRCData.class);
		log.info("After nrc data initialize: {}", nrcData.hashCode());
	}
}
