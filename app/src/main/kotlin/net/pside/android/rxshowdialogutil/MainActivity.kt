package net.pside.android.rxshowdialogutil

import android.os.Bundle
import android.widget.Toast
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : RxAppCompatActivity() {

    fun <T> Observable<T>.bindToLifecycle() = compose<T>(this.bindToLifecycle())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { fireObservable() }
    }

    private fun fireObservable() {

        asyncObservable("Fire?", 3)
                .compose(this.bindToLifecycle<String>())
                .compose(RxShowDialogUtil.applyDialog<String>(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toSingle()
                .subscribe {
                    Toast.makeText(this, "Success: $it", Toast.LENGTH_LONG).show()
                }


//                .subscribe(object : SingleSubscriber<String>() {
//                    override fun onSuccess(value: String) {
//                        Toast.makeText(this@MainActivity, "Success: " + value, Toast.LENGTH_SHORT).show()
//                        startActivity(NextActivity.createIntent(this@MainActivity))
//                    }
//
//                    override fun onError(error: Throwable) {
//                        Log.e("MainActivity", "onError: ", error)
//                    }
//                })
    }

    private fun asyncObservable(msg: String, wait: Int): Observable<String> {
        return Observable.just(msg).delay(wait.toLong(), TimeUnit.SECONDS, Schedulers.newThread())
    }
}
