package org.tutgi.student_registration.features.profile.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.profile.service.StaffService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {
	private final StorageService storageService;
	@Override
	public Resource retrieveFile(String filePath) {
		return storageService.loadAsResource(filePath);
	}

}
