package com.ezequieldiaz.vacunatorioapp4.util;

import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private final AtomicBoolean pending = new AtomicBoolean(false);

    @MainThread
    public void observe(LifecycleOwner owner, final Observer<? super T> observer) {
        super.observe(owner, t -> {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    @MainThread
    public void setValue(T t) {
        pending.set(true);
        super.setValue(t);
    }

    // Para usar en casos donde no hay datos, solo evento
    @MainThread
    public void call() {
        setValue(null);
    }
}
