package com.dan190.networkrxjava;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.ipAddress)
    EditText ipAddress;

    @BindView(R.id.port)
    EditText port;

    @BindView(R.id.connectButton)
    Button connectButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.Tree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                Log.i(tag, message);
            }
        });

        connectButton.setEnabled(false);

//        Observable<String> ipAddressObservable = getTextWatcherObservable(ipAddress);
//        Observable<String> portObservable = getTextWatcherObservable(port);


        Observable<CharSequence> ipAddObservable = RxTextView.textChanges(ipAddress).skipInitialValue();
        Observable<CharSequence> portObservable = RxTextView.textChanges(port).skipInitialValue();

        Observable.combineLatest(ipAddObservable, portObservable,
                (charSequence, charSequence2) -> {
                    Timber.log(1, "Charsequences Changed");
                    if(charSequence.length() > 0 &&  charSequence2.length() > 0) return true;
                    return false;
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Boolean aBoolean) {
                        if(aBoolean){
                            connectButton.setEnabled(true);
                        }else{
                            connectButton.setEnabled(false);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });




    }

    @OnClick(R.id.connectButton)
    public void connect(){

    }

    private Observable<String> getTextWatcherObservable(@NonNull final EditText editText){
        final PublishSubject<String> subject = PublishSubject.create();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.onNext(editable.toString());
            }
        });

        return subject;
    }
}
