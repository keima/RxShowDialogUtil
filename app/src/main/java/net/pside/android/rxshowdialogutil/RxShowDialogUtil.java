package net.pside.android.rxshowdialogutil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class RxShowDialogUtil {
    private static final String TAG = RxShowDialogUtil.class.getSimpleName();

    public static Dialog dialog;

    public static <T> Observable.Transformer<T, T> applyDialog(final Activity context) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_progress);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            WindowManager.LayoutParams attr = dialog.getWindow().getAttributes();
            attr.width = ViewGroup.LayoutParams.MATCH_PARENT;
            attr.height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(attr);
        }

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (!context.isFinishing() && dialog != null && !dialog.isShowing()) {
                                    Log.d(TAG, "doOnSubscribe(): show!");
                                    dialog.show();
                                }
                            }
                        })
                        .doOnUnsubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (!context.isFinishing() && dialog != null && dialog.isShowing()) {
                                    Log.d(TAG, "doOnUnsubscribe(): dismiss!");
                                    dialog.dismiss();
                                }
                                dialog = null;
                            }
                        })
                        .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                            @Override
                            public Observable<? extends T> call(Throwable throwable) {
                                if (throwable instanceof WindowManager.BadTokenException) {
                                    Log.e(TAG, "onErrorResumeNext: ", throwable);
                                    return null;
                                }
                                return Observable.error(throwable);
                            }
                        });
            }
        };
    }
}