//@@author itsNatTan
package seedu.flashlingo.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.flashlingo.commons.core.LogsCenter;
import seedu.flashlingo.model.flashcard.FlashCard;
import seedu.flashlingo.session.SessionManager;

/**
 * Panel containing the list of persons.
 */
public class FlashcardListPanel extends UiPart<Region> {
    private static final String FXML = "FlashcardListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(FlashcardListPanel.class);

    @FXML
    private ListView<FlashCard> flashcardListView;
    @FXML
    private StackPane pieChartContainer;
    private MainWindow mw;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public FlashcardListPanel(ObservableList<FlashCard> flashcardList, MainWindow mw) {
        super(FXML);
        this.mw = mw;
        flashcardListView.setItems(flashcardList);
        flashcardListView.setCellFactory(listView -> new FlashCardListViewCell());
    }
    public void displayPieChart(PieChart pieChart) {
        pieChartContainer.getChildren().clear();
        pieChartContainer.getChildren().add(pieChart);
    }
    public void hidePieChart() {
        pieChartContainer.getChildren().clear();
    }
    public void update() {
        flashcardListView.setCellFactory(listView -> new FlashCardListViewCell());
    }
    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class FlashCardListViewCell extends ListCell<FlashCard> {
        @Override
        protected void updateItem(FlashCard fc, boolean empty) {
            super.updateItem(fc, empty);

            if (empty || fc == null) {
                setGraphic(null);
                setText(null);
            } else {
                if (SessionManager.getInstance().isReviewSession()) {
                    setGraphic(new FlashcardBox(fc, getIndex() + 1, mw).getRoot());
                } else {
                    setGraphic(new FlashcardBoxNoButton(fc, getIndex() + 1, mw).getRoot());
                }
            }
        }
    }

}
