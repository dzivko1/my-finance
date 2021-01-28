package hr.ferit.dominikzivko.myfinance.data;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class ObserverCaretaker<T> {

    private final LifecycleOwner lifecycleOwner;
    private Observer<T> observer;

    public ObserverCaretaker(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setObserver(LiveData<T> liveData, Observer<T> observer) {
        if (this.observer != null) {
            liveData.removeObserver(observer);
        }
        liveData.observe(lifecycleOwner, observer);
        this.observer = observer;
    }
}
