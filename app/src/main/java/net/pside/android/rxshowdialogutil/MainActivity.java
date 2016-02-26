package net.pside.android.rxshowdialogutil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireObservable();
            }
        });
    }

    private void fireObservable() {

        asyncObservable("Fire?", 3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .compose(this.<String>bindToLifecycle())
                .compose(RxShowDialogUtil.<String>applyDialog(this))
                .toSingle()
                .subscribe(new SingleSubscriber<String>() {
                    @Override
                    public void onSuccess(String value) {
                        Toast.makeText(MainActivity.this, "Success: " + value, Toast.LENGTH_SHORT).show();
                        startActivity(NextActivity.createIntent(MainActivity.this));
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e("MainActivity", "onError: ", error);
                    }
                });
    }

    private Observable<String> asyncObservable(String msg, int wait) {
        return Observable.just(msg).delay(wait, TimeUnit.SECONDS, Schedulers.newThread());
    }
}
