package hr.ferit.dominikzivko.myfinance.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.Objects;

public class CategoryDetails {

    @Embedded
    private Category category;
    @Relation(
            entity = Category.class,
            parentColumn = "parentCategoryID", entityColumn = "id"
    )
    private Category parentCategory;

    public CategoryDetails(Category category, Category parentCategory) {
        this.category = category;
        this.parentCategory = parentCategory;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDetails that = (CategoryDetails) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(parentCategory, that.parentCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, parentCategory);
    }

    @Ignore
    public static final DiffUtil.ItemCallback<CategoryDetails> DIFF_CALLBACK = new DiffUtil.ItemCallback<CategoryDetails>() {
        @Override
        public boolean areItemsTheSame(@NonNull CategoryDetails oldItem, @NonNull CategoryDetails newItem) {
            return oldItem.getCategory().getId() == newItem.getCategory().getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryDetails oldItem, @NonNull CategoryDetails newItem) {
            return oldItem.equals(newItem);
        }
    };
}
