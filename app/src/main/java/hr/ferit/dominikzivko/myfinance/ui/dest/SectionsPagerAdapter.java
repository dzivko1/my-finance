package hr.ferit.dominikzivko.myfinance.ui.dest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hr.ferit.dominikzivko.myfinance.ui.dest.bills.BillsFragment;
import hr.ferit.dominikzivko.myfinance.ui.dest.stats.StatsFragment;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    public SectionsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BillsFragment();
            default:
                return new StatsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}