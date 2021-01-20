package hr.ferit.dominikzivko.myfinance.data;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Category.class, parentColumns = {"id"}, childColumns = {"parentCategoryID"})
        },
        indices = {
                @Index("parentCategoryID")
        }
)
public class Category {
    @Ignore
    private static Category ROOT_CATEGORY;

    @Ignore
    public static Category getRootCategory() {
        if (ROOT_CATEGORY == null) {
            ROOT_CATEGORY = new Category("All bills", Color.GRAY, 1);
            ROOT_CATEGORY.setId(1);
        }
        return ROOT_CATEGORY;
    }


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int color;
    private int parentCategoryID;

    @Ignore
    public Category() {
    }

    public Category(String name, int color, int parentCategoryID) {
        this.name = name;
        this.color = color;
        this.parentCategoryID = parentCategoryID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getParentCategoryID() {
        return parentCategoryID;
    }

    public void setParentCategoryID(int parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return color == category.color &&
                parentCategoryID == category.parentCategoryID &&
                Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, parentCategoryID);
    }


    @Ignore
    public static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.equals(newItem);
        }
    };
}
