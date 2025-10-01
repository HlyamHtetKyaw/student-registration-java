package org.tutgi.student_registration.features.students.service.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.personal.Job;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;

@Component
public class JobFactory {

    public Job createJob(String jobName, Long parentId) {
        Job job = new Job();
        job.setName(jobName);
        job.setEntityId(parentId);
        job.setEntityType(EntityType.PARENTS);
        return job;
    }
    
    public void updateJob(Job job, ParentName type, EntranceFormUpdateRequest request) {
        if (type == ParentName.FATHER) {
            Optional.ofNullable(request.fatherJob()).ifPresent(job::setName);
        } else {
            Optional.ofNullable(request.motherJob()).ifPresent(job::setName);
        }
    }

}

