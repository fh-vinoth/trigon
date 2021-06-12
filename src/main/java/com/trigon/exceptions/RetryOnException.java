package com.trigon.exceptions;


import com.trigon.reports.ReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.trigon.reports.ReportManager.cUtils;


/**
 * Encapsulates retry-on-exception operations
 */
public class RetryOnException {
    protected static final int DEFAULT_RETRIES = 3;
    protected static final long DEFAULT_TIME_TO_WAIT_MS = 300;
    private static final Logger logger = LogManager.getLogger(RetryOnException.class);
    protected int numRetries;
    protected long timeToWaitMS;

    // CONSTRUCTORS
    public RetryOnException(int _numRetries,
                            long _timeToWaitMS) {
        numRetries = _numRetries;
        timeToWaitMS = _timeToWaitMS;
    }

    public RetryOnException() {
        this(DEFAULT_RETRIES, DEFAULT_TIME_TO_WAIT_MS);
    }

    /**
     * shouldRetry
     * Returns true if a retry can be attempted.
     *
     * @return True if retries attempts remain; else false
     */
    public boolean shouldRetry() {
        return (numRetries >= 0);
    }

    /**
     * waitUntilNextTry
     * Waits for timeToWaitMS. Ignores any interrupted exception
     */
    public void waitUntilNextTry() {
        try {
            Thread.sleep(timeToWaitMS);
        } catch (InterruptedException iex) {
        }
    }

    /**
     * exceptionOccurred
     * Call when an exception has occurred in the block. If the
     * retry limit is exceeded, throws an exception.
     * Else waits for the specified time.
     *
     * @throws Exception
     */
    public boolean exceptionOccurred(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        long startTime5 = System.currentTimeMillis();

        Boolean isPresentStatus = false;
        --numRetries;
        if (!shouldRetry()) {
            logger.info("Retry limit exceeded for Element : " + locatorString);
            isPresentStatus = true;
        }
        waitUntilNextTry();

        long endTime5 = System.currentTimeMillis();
        logger.info("Element " + locatorString + " Not found, attempting to retry : Retrying Count" + numRetries + " Time Taken : " + cUtils().getRunDuration(startTime5, endTime5));

        return isPresentStatus;
    }

}