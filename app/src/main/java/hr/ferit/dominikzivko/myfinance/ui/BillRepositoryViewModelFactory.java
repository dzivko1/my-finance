package hr.ferit.dominikzivko.myfinance.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hr.ferit.dominikzivko.myfinance.data.BillRepository;

public class BillRepositoryViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final BillRepository billRepository;

    public BillRepositoryViewModelFactory(Application application, BillRepository billRepository) {
        this.application = application;
        this.billRepository = billRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
                return modelClass.getConstructor(Application.class, BillRepository.class).newInstance(application, billRepository);
            } else {
                return modelClass.getConstructor(BillRepository.class).newInstance(billRepository);
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
