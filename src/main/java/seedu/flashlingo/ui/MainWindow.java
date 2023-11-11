package seedu.flashlingo.ui;

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.flashlingo.commons.core.GuiSettings;
import seedu.flashlingo.commons.core.LogsCenter;
import seedu.flashlingo.logic.Logic;
import seedu.flashlingo.logic.commands.CommandResult;
import seedu.flashlingo.logic.commands.exceptions.CommandException;
import seedu.flashlingo.logic.parser.exceptions.ParseException;
import seedu.flashlingo.model.Model;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {
    private static final String THEME_FILE_PATH_PREFIX = "/view/";
    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private FlashcardListPanel flashcardListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    @FXML
    private Scene scene;
    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane flashCardListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    private Model model;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic, Model model) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.model = model;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());
        setColorTheme(logic.getTheme());

        setAccelerators();

        helpWindow = new HelpWindow();
        primaryStage.setScene(scene);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        flashcardListPanel = new FlashcardListPanel(logic.getFilteredFlashCardList(), this);
        flashCardListPanelPlaceholder.getChildren().add(flashcardListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    private PieChart createPieChart(double successRate) {
        PieChart.Data successData = new PieChart.Data("Words Remembered", successRate);
        PieChart.Data failureData = new PieChart.Data("Words to be worked on", 100 - successRate);

        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(successData, failureData);

        return pieChart;
    }
    private void handleStats(CommandResult statsCommandResult) {
        PieChart pieChart = createPieChart(statsCommandResult.getSuccessRate());
        flashcardListPanel.displayPieChart(pieChart);
    }
    /**
     * Attempts to open the UserGuide in the default browser.
     * Upon failure, pop up a HelpWindow with the URL shown, or focuses on it if it is already open
     */
    //@@author itsNatTan
    @FXML
    public void handleHelp() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://ay2324s1-cs2103t-t11-4.github.io/tp/UserGuide.html"));
            } catch (Exception e) {
                if (!helpWindow.isShowing()) {
                    helpWindow.show();
                } else {
                    helpWindow.focus();
                }
            }
        } else {
            if (!helpWindow.isShowing()) {
                helpWindow.show();
            } else {
                helpWindow.focus();
            }
        }
    }
    //@@author
    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Executes the command and returns the result.
     *
     * @see Logic#execute(String)
     */
    public CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            flashcardListPanel.hidePieChart();
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());

            if (commandResult.isStatsResult()) {
                handleStats(commandResult);
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.isSwitchTheme()) {
                setColorTheme(logic.getTheme());
                commandResult = new CommandResult(commandResult.getFeedbackToUser(), false, false, false);
            }
            flashcardListPanel.update();
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    private void setColorTheme(String theme) {
        if (theme.equals("Default")) {
            switchTheme("Light");
        } else {
            switchTheme("Dark");
        }
    }

    private void switchTheme(String theme) {
        scene.getStylesheets().clear();
        String themeFilePath = THEME_FILE_PATH_PREFIX + theme + "Theme.css";
        String extensionsFilePath = THEME_FILE_PATH_PREFIX + "Extensions.css";
        scene.getStylesheets().add(getClass().getResource(themeFilePath).toExternalForm());
        scene.getStylesheets().add(getClass().getResource(extensionsFilePath).toExternalForm());
    }
}
