package com.gnanendraprasadp.springbatch.exception;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class BatchException implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable throwable, int i) throws SkipLimitExceededException {
        return throwable instanceof NumberFormatException;
    }
}
