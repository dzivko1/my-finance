package hr.ferit.dominikzivko.myfinance;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import hr.ferit.dominikzivko.myfinance.data.AppDatabase;
import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.ui.BillRepositoryViewModelFactory;

public class AppContainer {

    public BillRepositoryViewModelFactory billRepositoryViewModelFactory;

    public AppContainer(App application) {
        AppDatabase database = AppDatabase.getInstance(application);
        Executor executor = Executors.newSingleThreadExecutor();
        BillRepository billRepository = new BillRepository(executor, database.billDao(), database.categoryDao(), database.partyDao());
        billRepositoryViewModelFactory = new BillRepositoryViewModelFactory(application, billRepository);
    }
}
