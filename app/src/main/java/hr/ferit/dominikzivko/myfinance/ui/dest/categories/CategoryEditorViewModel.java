package hr.ferit.dominikzivko.myfinance.ui.dest.categories;

import androidx.lifecycle.ViewModel;

import hr.ferit.dominikzivko.myfinance.ActionFailedException;
import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.data.BillRepository;
import hr.ferit.dominikzivko.myfinance.data.Category;
import hr.ferit.dominikzivko.myfinance.data.CategoryDetails;

public class CategoryEditorViewModel extends ViewModel {

    private final BillRepository billRepository;

    private Category editedCategory;
    private Category parentCategory;

    public CategoryEditorViewModel(BillRepository billRepository) {
        this.billRepository = billRepository;
        resetEditedCategory();
    }

    public String getName() {
        return editedCategory.getName();
    }

    public void setName(String name) {
        editedCategory.setName(name);
    }

    public int getColor() {
        return editedCategory.getColor();
    }

    public void setColor(int color) {
        editedCategory.setColor(color);
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        if (parentCategory == null)
            parentCategory = Category.getRootCategory();

        this.parentCategory = parentCategory;
        this.editedCategory.setParentCategoryID(parentCategory.getId());
    }

    public void setEditedCategory(CategoryDetails categoryDetails) {
        if (categoryDetails == null) {
            resetEditedCategory();
            return;
        }
        this.editedCategory = categoryDetails.getCategory();
        setParentCategory(categoryDetails.getParentCategory());
    }

    public void resetEditedCategory() {
        this.editedCategory = new Category();
        setParentCategory(null);
    }

    public void tryApply() throws ActionFailedException {
        if (!checkValid()) throw new ActionFailedException(R.string.error_name_required);

        if (editedCategory.getId() == 0) {
            billRepository.addCategory(editedCategory);
        } else {
            billRepository.updateCategory(editedCategory);
        }
    }

    private boolean checkValid() {
        return getName() != null && !getName().trim().isEmpty();
    }
}