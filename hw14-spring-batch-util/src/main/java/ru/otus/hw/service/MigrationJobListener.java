package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MigrationJobListener implements JobExecutionListener {

    private final IdMappingCache idMappingCache;

    @Override
    public void afterJob(JobExecution jobExecution) {

        this.idMappingCache.clearAll();
    }
}
