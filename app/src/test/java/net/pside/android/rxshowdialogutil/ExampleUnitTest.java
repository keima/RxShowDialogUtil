package net.pside.android.rxshowdialogutil;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void testObservableConcat() throws Exception {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        System.out.println("Start.");

        // merge だっけ concat だっけ

        Observable.merge(
                asyncObservable("uno", 4),
                asyncObservable("dos", 3),
                asyncObservable("tres", 2),
                asyncObservable("cuatro", 1)
        ).asObservable().doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("onNext: " + s);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                System.out.println("onCompleted");
            }
        }).subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList("uno", "dos", "tres", "cuatro"));
    }

    private Observable<String> asyncObservable(String msg, int wait) {
        return Observable.just(msg).delay(wait, TimeUnit.SECONDS, Schedulers.newThread());
    }
}