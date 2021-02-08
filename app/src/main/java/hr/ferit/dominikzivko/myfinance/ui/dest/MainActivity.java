package hr.ferit.dominikzivko.myfinance.ui.dest;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import hr.ferit.dominikzivko.myfinance.App;
import hr.ferit.dominikzivko.myfinance.AppContainer;
import hr.ferit.dominikzivko.myfinance.MathUtils;
import hr.ferit.dominikzivko.myfinance.NavGraphDirections;
import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.data.AppDatabase;
import hr.ferit.dominikzivko.myfinance.data.BillDetails;
import hr.ferit.dominikzivko.myfinance.data.Category;
import hr.ferit.dominikzivko.myfinance.data.CategoryDetails;
import hr.ferit.dominikzivko.myfinance.data.Party;
import hr.ferit.dominikzivko.myfinance.ui.ViewModelProviders;
import hr.ferit.dominikzivko.myfinance.ui.dest.bills.BillEditorViewModel;
import hr.ferit.dominikzivko.myfinance.ui.dest.categories.CategoriesFragmentDirections;
import hr.ferit.dominikzivko.myfinance.ui.dest.categories.CategoryAdapter;
import hr.ferit.dominikzivko.myfinance.ui.dest.categories.CategoryEditorViewModel;
import hr.ferit.dominikzivko.myfinance.ui.dest.parties.PartiesFragmentDirections;
import hr.ferit.dominikzivko.myfinance.ui.dest.parties.PartyAdapter;
import hr.ferit.dominikzivko.myfinance.ui.dest.parties.PartyEditorViewModel;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    private BottomSheetBehavior<NavigationView> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App app = (App) getApplication();
        app.appContainer = new AppContainer(app);

        // obligatory init for charting library
        Utils.init(getApplicationContext());

        initDatabase();
        initUI();
    }

    private void initDatabase() {
        String[] databases = getApplicationContext().databaseList();
        if (databases.length == 0) {
            AsyncTask.execute(() -> {
                AppDatabase database = AppDatabase.getInstance(getApplicationContext());
                database.categoryDao().insert(Category.getRootCategory());
            });
        }
    }

    private void initUI() {
        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        setupBottomDrawer();
        setupNavAndToolbar();
        setupBottomAppBar();
        setFabAction(view -> startNewBill());
    }

    private void setupBottomDrawer() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::handleNavigationItemClick);
        bottomSheetBehavior = BottomSheetBehavior.from(navigationView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        FrameLayout scrim = findViewById(R.id.scrim);
        scrim.setOnClickListener(v -> closeBottomDrawer());
        scrim.setClickable(false);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                scrim.setClickable(newState != BottomSheetBehavior.STATE_HIDDEN);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float baseAlpha = ResourcesCompat.getFloat(getResources(), R.dimen.material_emphasis_medium);
                float offset = MathUtils.map(slideOffset, -1f, 1f, 0f, 1f);
                int alpha = (int) com.google.android.material.math.MathUtils.lerp(0f, 255f, offset * baseAlpha);
                int color = Color.argb(alpha, 0, 0, 0);
                scrim.setBackgroundColor(color);
            }
        });
    }

    private void setupNavAndToolbar() {
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHost);
        navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();

        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(collapsingToolbarLayout, toolbar, navController, appBarConfiguration);

        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            boolean isTopLevel = destination.getId() == R.id.topLevelFragment;

            appBarLayout.setExpanded(isTopLevel);
            if (isTopLevel) resetFab();
            else bottomAppBar.performShow();
        });
    }

    private void setupBottomAppBar() {
        bottomAppBar.setNavigationOnClickListener(v -> openBottomDrawer());
        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_topLevel_newCategory:
                    startNewCategory();
                    return true;
                case R.id.action_topLevel_newParty:
                    startNewParty();
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isBottomDrawerClosed()) {
            closeBottomDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setFabAction(View.OnClickListener action) {
        fab.setOnClickListener(action);
    }

    public void setFabIcon(@DrawableRes int resId) {
        fab.setImageResource(resId);
    }

    public void resetFab() {
        setFabAction(view -> startNewBill());
        setFabIcon(R.drawable.ic_baseline_add_24);
    }

    public void startNewBill() {
        BillEditorViewModel billEditorViewModel = ViewModelProviders.withBillRepository(this)
                .get(BillEditorViewModel.class);
        billEditorViewModel.resetEditedBill();

        navigateToBillEditor(R.string.new_bill);
    }

    public void startNewCategory() {
        CategoryEditorViewModel categoryEditorViewModel = ViewModelProviders.withBillRepository(this)
                .get(CategoryEditorViewModel.class);
        categoryEditorViewModel.resetEditedCategory();

        navigateToCategoryEditor(R.string.new_bill_category);
    }

    public void startNewParty() {
        PartyEditorViewModel partyEditorViewModel = ViewModelProviders.withBillRepository(this)
                .get(PartyEditorViewModel.class);
        partyEditorViewModel.resetEditedParty();

        navigateToPartyEditor(R.string.new_party);
    }

    public void editBill(BillDetails billDetails) {
        BillEditorViewModel billEditorViewModel = ViewModelProviders.withBillRepository(this)
                .get(BillEditorViewModel.class);
        billEditorViewModel.setEditedBill(billDetails);

        navigateToBillEditor(R.string.edit_bill);
    }

    public void editCategory(CategoryDetails categoryDetails) {
        CategoryEditorViewModel categoryEditorViewModel = ViewModelProviders.withBillRepository(this)
                .get(CategoryEditorViewModel.class);
        categoryEditorViewModel.setEditedCategory(categoryDetails);

        navigateToCategoryEditor(R.string.edit_category);
    }

    public void editParty(Party party) {
        PartyEditorViewModel partyEditorViewModel = ViewModelProviders.withBillRepository(this)
                .get(PartyEditorViewModel.class);
        partyEditorViewModel.setEditedParty(party);

        navigateToPartyEditor(R.string.edit_party);
    }

    public void navigateToBillEditor(@StringRes int titleRes) {
        String title = getResources().getString(titleRes);
        navController.navigate(NavGraphDirections.actionGlobalBillEditor(title));
    }

    public void navigateToCategoryEditor(@StringRes int titleRes) {
        String title = getResources().getString(titleRes);
        navController.navigate(NavGraphDirections.actionGlobalCategoryEditor(title));
    }

    public void navigateToPartyEditor(@StringRes int titleRes) {
        String title = getResources().getString(titleRes);
        navController.navigate(NavGraphDirections.actionGlobalPartyEditor(title));
    }

    private void navigateToCategories() {
        String title = getResources().getString(R.string.categories);
        CategoryAdapter.ItemClickListener itemClickListener = category -> {
            CategoriesFragmentDirections.ActionCategoryListToDetails action =
                    CategoriesFragmentDirections.actionCategoryListToDetails(category.getId());
            navController.navigate(action);
        };
        navController.navigate(NavGraphDirections.actionGlobalCategories(title, itemClickListener));
    }

    private void navigateToParties() {
        String title = getResources().getString(R.string.parties);
        PartyAdapter.ItemClickListener itemClickListener = party -> {
            PartiesFragmentDirections.ActionPartyListToDetails action =
                    PartiesFragmentDirections.actionPartyListToDetails(party.getId());
            navController.navigate(action);
        };
        navController.navigate(NavGraphDirections.actionGlobalParties(title, itemClickListener));
    }

    private void openBottomDrawer() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void closeBottomDrawer() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private boolean isBottomDrawerClosed() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN;
    }

    private boolean handleNavigationItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nav_categories:
                navigateToCategories();
                break;
            case R.id.action_nav_parties:
                navigateToParties();
                break;
            default:
                return false;
        }

        closeBottomDrawer();
        return true;
    }
}