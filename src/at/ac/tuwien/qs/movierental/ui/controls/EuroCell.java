package at.ac.tuwien.qs.movierental.ui.controls;

import javafx.scene.control.TableCell;

import java.text.DecimalFormat;

public class EuroCell<TYPE> extends TableCell<TYPE, Integer> {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("'€ '0.00");

    @Override
    protected void updateItem(Integer  priceInCent, boolean empty) {
        super.updateItem(priceInCent, empty);
        if (priceInCent == null || empty) {
            setText(null);
        } else {
            setText(DECIMAL_FORMAT.format(priceInCent/100.0));
        }
    }
}
