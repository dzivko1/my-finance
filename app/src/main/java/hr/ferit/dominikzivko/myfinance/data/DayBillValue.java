package hr.ferit.dominikzivko.myfinance.data;

import java.util.Date;

public class DayBillValue {

    private Date date;
    private double value;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
