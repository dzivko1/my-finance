package hr.ferit.dominikzivko.myfinance.ui;

import androidx.activity.ComponentActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import hr.ferit.dominikzivko.myfinance.App;

public class ViewModelProviders {

    public static ViewModelProvider withBillRepository(ComponentActivity ownerActivity) {
        App app = (App) ownerActivity.getApplication();
        ViewModelProvider.Factory factory = app.appContainer.billRepositoryViewModelFactory;
        return new ViewModelProvider(ownerActivity, factory);
    }

    public static ViewModelProvider withBillRepository(Fragment ownerFragment) {
        App app = (App) ownerFragment.requireActivity().getApplication();
        ViewModelProvider.Factory factory = app.appContainer.billRepositoryViewModelFactory;
        return new ViewModelProvider(ownerFragment, factory);
    }
}
