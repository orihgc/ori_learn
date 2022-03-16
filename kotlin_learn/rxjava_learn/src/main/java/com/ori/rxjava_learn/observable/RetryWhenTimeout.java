package com.ori.rxjava_learn.observable;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author rexy
 */
public class RetryWhenTimeout implements Function<Observable<Throwable>, ObservableSource<?>> {

    int mRetryCounter;
    int[] mRetryDelay;

    public RetryWhenTimeout(int[] timeDelayInScends) {
        mRetryCounter = -1;
        if (timeDelayInScends == null || timeDelayInScends.length == 0) {
            mRetryDelay = new int[]{1};
        } else {
            mRetryDelay = timeDelayInScends;
        }
    }

    @Override
    public Observable apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap((Function<Throwable, Observable<Long>>) throwable -> {
            ++mRetryCounter;
            if (mRetryCounter < mRetryDelay.length) {
                return Observable.timer(mRetryDelay[mRetryCounter], TimeUnit.SECONDS);
            } else {
                return Observable.error(throwable);
            }
        });
    }
}