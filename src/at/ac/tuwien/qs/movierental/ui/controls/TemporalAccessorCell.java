package at.ac.tuwien.qs.movierental.ui.controls;

import javafx.scene.control.TableCell;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class TemporalAccessorCell<TYPE> extends TableCell<TYPE, TemporalAccessor> {

    private DateTimeFormatter dateTimeFormatter;

    public TemporalAccessorCell(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    protected void updateItem(TemporalAccessor item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            if (dateTimeFormatter == null) {
                setText(item.toString());
            } else {
                setText(dateTimeFormatter.format(item));
            }
        }
    }
}
