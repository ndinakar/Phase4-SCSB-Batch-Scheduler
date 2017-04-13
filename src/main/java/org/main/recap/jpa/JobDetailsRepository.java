package org.main.recap.jpa;

import org.main.recap.model.jpa.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by rajeshbabuk on 4/4/17.
 */
public interface JobDetailsRepository extends JpaRepository<JobEntity, Integer> {

    JobEntity findByJobName(String jobName);
}
