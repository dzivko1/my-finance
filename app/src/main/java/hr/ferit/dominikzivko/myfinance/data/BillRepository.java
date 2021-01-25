package hr.ferit.dominikzivko.myfinance.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

public class BillRepository {

    private final BillDao billDao;
    private final CategoryDao categoryDao;
    private final PartyDao partyDao;

    private final Executor executor;

    public BillRepository(Executor executor, BillDao billDao, CategoryDao categoryDao, PartyDao partyDao) {
        this.executor = executor;
        this.billDao = billDao;
        this.categoryDao = categoryDao;
        this.partyDao = partyDao;
    }

    private void runAsync(Runnable runnable) {
        executor.execute(runnable);
    }

    public LiveData<List<BillDetails>> getBills() {
        return billDao.getAll();
    }

    public LiveData<List<CategoryDetails>> getCategories() {
        return categoryDao.getAll();
    }

    public LiveData<List<Party>> getParties() {
        return partyDao.getAll();
    }

    public LiveData<BillDetails> getBillByID(int billID) {
        return billDao.getByID(billID);
    }

    public LiveData<CategoryDetails> getCategoryByID(int categoryID) {
        return categoryDao.getByID(categoryID);
    }

    public LiveData<Party> getPartyByID(int partyID) {
        return partyDao.getByID(partyID);
    }

    public LiveData<Integer> getBillCount() {
        return billDao.count();
    }

    public LiveData<Integer> getCategoryCount() {
        return categoryDao.count();
    }

    public LiveData<Double> getSummedBillsValue() {
        return billDao.summedValue();
    }

    public LiveData<Double> getSummedBillsValueForThisMonth() {
        return billDao.summedValueThisMonth();
    }

    public LiveData<List<CategoryTotal>> getTotalByCategory() {
        return billDao.totalByCategory();
    }

    public LiveData<List<DayBillValue>> getBillDayValuesWithCategory(int categoryID) {
        return billDao.getDayValuesWithCategory(categoryID);
    }

    public void addBill(Bill bill) {
        runAsync(() -> billDao.insert(bill));
    }

    public void updateBill(Bill bill) {
        runAsync(() -> billDao.update(bill));
    }

    public void deleteBill(int billID) {
        runAsync(() -> billDao.delete(billID));
    }

    public void addCategory(Category category) {
        runAsync(() -> categoryDao.insert(category));
    }

    public void updateCategory(Category category) {
        runAsync(() -> categoryDao.update(category));
    }

    public void deleteCategory(int categoryID) {
        runAsync(() -> categoryDao.delete(categoryID));
    }

    public void addParty(Party party) {
        runAsync(() -> partyDao.insert(party));
    }

    public void updateParty(Party party) {
        runAsync(() -> partyDao.update(party));
    }

    public void deleteParty(int partyID) {
        runAsync(() -> partyDao.delete(partyID));
    }

}
