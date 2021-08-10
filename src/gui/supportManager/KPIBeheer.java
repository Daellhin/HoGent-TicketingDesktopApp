package gui.supportManager;

import static util.FxUtil.clearErrorMessage;
import static util.FxUtil.setConfirmationMessage;
import static util.FxUtil.setErrorMessage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import domain.controllers.StatistiekController;
import domain.enumerations.StatistiekType;
import gui.Opslaanbaar;
import gui.general.GebruikerScherm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;

public class KPIBeheer extends GridPane implements Opslaanbaar {
	@FXML
	private ListView<StatistiekType> lijstTrackedStatistics;
	@FXML
	private ListView<StatistiekType> lijstUntrackedStatistics;
	@FXML
	private Button btnTrackStatistiek;
	@FXML
	private Button btnUntrackStatistiek;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnSaveChanges;
	@FXML
	private Label lblErrorSaveChanges;

	private GebruikerScherm scherm;
	private StatistiekController dc;
	private List<StatistiekType> initieleTrackedStatistieken;

	public KPIBeheer(GebruikerScherm scherm) {
		this.scherm = scherm;
		this.dc = new StatistiekController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			System.out.println("Probleem met tonen " + this.getClass().getSimpleName());
			ex.printStackTrace();
		}
		buildGUI();
		addGrowHorizontal();
	}

	private void addGrowHorizontal() {
		this.setPrefWidth(scherm.getWidth() - 300);
		scherm.widthProperty().addListener(event -> {
			this.setPrefWidth(scherm.getWidth() - 300);
		});
	}

	private void buildGUI() {
		disableSaveAndCancel(true);
		btnTrackStatistiek.setDisable(true);
		btnUntrackStatistiek.setDisable(true);

		initieleTrackedStatistieken = dc.getTrackedStatistiekTypes();
		lijstTrackedStatistics.setItems(dc.getTrackedStatistiekTypes());
		lijstUntrackedStatistics.setItems(dc.getUntrackedStatistiekTypes());
		lijstTrackedStatistics.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lijstUntrackedStatistics.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		lijstTrackedStatistics.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, oldValue, newValue) -> {
					if (newValue != null) {
						lijstUntrackedStatistics.getSelectionModel().clearSelection();
						btnUntrackStatistiek.setDisable(false);
						btnTrackStatistiek.setDisable(true);
					}
				});

		lijstUntrackedStatistics.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, oldValue, newValue) -> {
					if (newValue != null) {
						lijstTrackedStatistics.getSelectionModel().clearSelection();
						btnUntrackStatistiek.setDisable(true);
						btnTrackStatistiek.setDisable(false);
					}
				});
	}

	// Event Listener on Button[#btnTrackStatistiek].onAction
	@FXML
	public void trackStatistiek(ActionEvent event) {
		lijstTrackedStatistics.getItems().addAll(lijstUntrackedStatistics.getSelectionModel().getSelectedItems());
		lijstUntrackedStatistics.getItems().removeAll(lijstUntrackedStatistics.getSelectionModel().getSelectedItems());
		lijstUntrackedStatistics.getSelectionModel().clearSelection();
		btnTrackStatistiek.setDisable(true);
		disableSaveAndCancel(false);
		clearErrorMessage(lblErrorSaveChanges);
	}

	// Event Listener on Button[#btnUntrackStatistiek].onAction
	@FXML
	public void untrackStatistiek(ActionEvent event) {
		lijstUntrackedStatistics.getItems().addAll(lijstTrackedStatistics.getSelectionModel().getSelectedItems());
		lijstTrackedStatistics.getItems().removeAll(lijstTrackedStatistics.getSelectionModel().getSelectedItems());
		lijstTrackedStatistics.getSelectionModel().clearSelection();
		btnUntrackStatistiek.setDisable(true);
		disableSaveAndCancel(false);
		clearErrorMessage(lblErrorSaveChanges);
	}

	// Event Listener on Button[#btnCancel].onAction
	@FXML
	public void cancelChanges(ActionEvent event) {
		lijstTrackedStatistics.setItems(dc.getTrackedStatistiekTypes());
		lijstUntrackedStatistics.setItems(dc.getUntrackedStatistiekTypes());
		clearErrorMessage(lblErrorSaveChanges);
	}

	// Event Listener on Button[#btnSaveChanges].onAction
	@FXML
	public void saveChanges(ActionEvent event) {
		try {
			List<StatistiekType> statistieken = lijstTrackedStatistics.getItems();
			dc.trackStatistiekTypes(statistieken);
			initieleTrackedStatistieken = statistieken;
			disableSaveAndCancel(true);
			setConfirmationMessage(lblErrorSaveChanges, "Changes have been saved succesfully");
		} catch (Exception ex) {
			setErrorMessage(lblErrorSaveChanges, "Something went wrong and your changes have not been saved.");
		}
	}

	@Override
	public boolean veranderScherm() {
		if (!initieleTrackedStatistieken.equals(lijstTrackedStatistics.getItems())) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Unsaved changes");
			alert.setHeaderText("You have unsaved changes");
			alert.setContentText("Do you wish to discard your changes?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private void disableSaveAndCancel(boolean disable) {
		btnSaveChanges.setDisable(disable);
		btnCancel.setDisable(disable);
	}
}
