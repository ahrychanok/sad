package com.smarsh.notificationservice.api.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.classify.Classifier;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ihar_Karsakou
 */
public class RetryTemplateFactory {

    private RetryTemplateFactory() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger("export-delivery-service");

    public static RetryTemplate createRetryTemplate(Integer maxAttempts, Integer retryTimeout) {
        return createRetryTemplate(maxAttempts, retryTimeout, null);
    }

    public static RetryTemplate createRetryTemplate(
            Integer maxAttempts, Integer retryTimeout, Collection<Class<? extends Throwable>> exceptionsToNotRetry) {

        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(retryTimeout);
        retryTemplate.setBackOffPolicy(backOffPolicy);


        SimpleRetryPolicy policyForRetryable = new SimpleRetryPolicy();
        policyForRetryable.setMaxAttempts(maxAttempts);

        ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();

        retryPolicy.setExceptionClassifier(
                new ExceptionClassifier(
                        exceptionsToNotRetry == null ? Collections.emptySet() : exceptionsToNotRetry,
                        policyForRetryable));

        retryTemplate.setRetryPolicy(retryPolicy);

        retryTemplate.setListeners(new RetryListener[]{new ErrorLoggingRetryListener()});

        return retryTemplate;
    }


    /**
     * @author Ihar_Karsakou
     */
    public static class ErrorLoggingRetryListener implements RetryListener {

        @Override
        public <T, E extends Throwable> boolean open(
                RetryContext context,
                RetryCallback<T, E> callback) {
            return true;
        }

        @Override
        public <T, E extends Throwable> void close(
                RetryContext context,
                RetryCallback<T, E> callback,
                Throwable throwable) {
            //empty call
        }

        @Override
        public <T, E extends Throwable> void onError(
                RetryContext context,
                RetryCallback<T, E> callback,
                Throwable throwable) {

            LOGGER.error("Error while retrying.", throwable);
        }

    }

    /**
     * Implementation for choosing policy to retry or no retries depending on exception.
     *
     * @author Maksim_Naumovich
     */
    private static class ExceptionClassifier implements Classifier<Throwable, RetryPolicy> {

        /**
         *
         */
        private static final long serialVersionUID = 7273695977195715289L;

        private Collection<Class<? extends Throwable>> exceptionsToNotRetry;

        private RetryPolicy policyForRetryable;

        ExceptionClassifier(
                Collection<Class<? extends Throwable>> exceptionsToNotRetry,
                RetryPolicy policyForRetryable) {
            if (exceptionsToNotRetry == null) {
                throw new IllegalArgumentException("exceptionsToNotRetry required");
            }
            this.exceptionsToNotRetry = exceptionsToNotRetry;
            this.policyForRetryable = policyForRetryable;
        }

        @Override
        public RetryPolicy classify(Throwable classifiable) {
            List<Class<? extends Throwable>> collect =
                    exceptionsToNotRetry.stream().filter(exceptionClass -> exceptionClass.isInstance(classifiable)
                            || (classifiable.getCause() != null
                            && exceptionClass.isInstance(classifiable.getCause()))).collect(Collectors.toList());

            return CollectionUtils.isEmpty(collect) ? policyForRetryable : new NeverRetryPolicy();
        }
    }
}
