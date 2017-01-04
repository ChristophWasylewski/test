package at.ac.tuwien.qs.movierental.ui.controls;

import at.ac.tuwien.qs.movierental.Rental;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.util.function.Consumer;

public class RentalActionButton<TYPE> extends TableCell<TYPE, Rental> {

    final Button payRentalButton = new Button("Rückgabe JA/NEIN");
    final Button removeButton = new Button("Entfernen");

    public RentalActionButton(Consumer<Rental> removeRentalButtonPressed, Consumer<Rental> payRentalButtonPressed) {
        payRentalButton.setOnAction(event -> payRentalButtonPressed.accept((Rental) RentalActionButton.this.getTableView().getItems().get(RentalActionButton.this.getIndex())));
        removeButton.setOnAction(event -> removeRentalButtonPressed.accept((Rental) RentalActionButton.this.getTableView().getItems().get(RentalActionButton.this.getIndex())));
    }

    @Override
    protected void updateItem(Rental item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            if (item.getId() != null) {
                setGraphic(payRentalButton);
            } else {
                setGraphic(removeButton);
            }
        }
    }
}
