package hr.ferit.dominikzivko.myfinance.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import hr.ferit.dominikzivko.myfinance.R;
import hr.ferit.dominikzivko.myfinance.data.Address;

public class AddressView extends LinearLayout {

    @BindingAdapter("address")
    public static void setAddress(AddressView view, Address address) {
        view.setAddress(address);
    }

    private TextView tvStreetAndNumber;
    private TextView tvZipcodeAndCity;

    public AddressView(Context context) {
        super(context);
        init(context);
    }

    public AddressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AddressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_address, this);

        tvStreetAndNumber = findViewById(R.id.addressView_streetAndNumber);
        tvZipcodeAndCity = findViewById(R.id.addressView_zipcodeAndCity);
    }

    @SuppressLint("SetTextI18n")
    public void setAddress(Address address) {
        if (address == null) return;

        tvStreetAndNumber.setText(address.getStreet() + " " + address.getStreetNumber());
        tvZipcodeAndCity.setText(address.getZipcode() + " " + address.getCity());
    }
}
