package gui.supportManager;

import static util.FxUtil.clearError;
import static util.FxUtil.clearErrorMessage;
import static util.FxUtil.inHierarchy;
import static util.FxUtil.setConfirmationMessage;
import static util.FxUtil.setError;
import static util.FxUtil.setErrorMessage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import domain.ContractType;
import domain.controllers.BeheerContractTypesController;
import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.ContractTypeStatus;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;
import gui.Opslaanbaar;
import gui.general.GebruikerScherm;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.Controles;
import util.StringFilters;

public class BeheerContractTypes extends StackPane implements Opslaanbaar {
	@FXML
	private Label lblManageContractTypes;
	@FXML
	private Button btnAddContractTypes;
	@FXML
	private Button btnShowFilters;
	@FXML
	private TableView<ContractType> tableBeheerContractTypes;
	@FXML
	private TableColumn<ContractType, String> colNaam;
	@FXML
	private TableColumn<ContractType, String> colStatus;
	@FXML
	private TableColumn<ContractType, Number> colLopendeContracten;
	@FXML
	private AnchorPane anchorFilters;
	@FXML
	private TextField txfFilterName;
	@FXML
	private TextField txfFilterMinLopendeContracten;
	@FXML
	private CheckBox chkFilterActive;
	@FXML
	private CheckBox chkFilterInactive;
	@FXML
	private Label lblErrorSaveChanges;
	@FXML
	private TextField txfNaam;
	@FXML
	private ComboBox<TicketAanmaakTijd> cboTicketTijd;
	@FXML
	private Label lblErrorName;
	@FXML
	private Label lblErrorTicketTime;
	@FXML
	private TextField txfMaxAfhandeltijd;
	@FXML
	private ComboBox<ContractDoorlooptijd> cboMinContractLengte;
	@FXML
	private Label lblErrorMaxAfhandeltijd;
	@FXML
	private Label lblErrorMinContractLengte;
	@FXML
	private Label lblStatus;
	@FXML
	private ComboBox<ContractTypeStatus> cboStatus;
	@FXML
	private TextField txfCost;
	@FXML
	private Label lblErrorStatus;
	@FXML
	private Label lblErrorCost;
	@FXML
	private CheckBox chkEmail;
	@FXML
	private CheckBox chkTelefoon;
	@FXML
	private CheckBox chkApp;
	@FXML
	private Label lblErrorTicketManner;
	@FXML
	private VBox vboxDetails;
	@FXML
	private Label lblDetailsNumberContracts;
	@FXML
	private Label lblDetailsNumberTickets;
	@FXML
	private Label lblDetailsTicketSuccesRate;
	@FXML
	private Button btnSaveChanges;
	@FXML
	private Button btnCancel;

	private GebruikerScherm scherm;
	private BeheerContractTypesController dc;
	private ContractType geselecteerdContractType;
	private boolean bestaandContractType;

	public BeheerContractTypes(GebruikerScherm scherm) {
		this.scherm = scherm;
		this.dc = new BeheerContractTypesController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			System.out.println("Probleem met tonen " + this.getClass().getSimpleName());
			ex.printStackTrace();
		}

		buildGui();
		addGrowHorizontal();
	}

	private void addGrowHorizontal() {
		this.setPrefWidth(scherm.getWidth() - 300);
		scherm.widthProperty().addListener(event -> {
			this.setPrefWidth(scherm.getWidth() - 300);
		});
	}

	private void buildGui() {
		chkFilterActive.setSelected(true);

		colNaam.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaam()));
		colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
		colLopendeContracten.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getAantalLopendeContracten()));
		SortedList<ContractType> s = new SortedList<>(dc.getContractTypesList());
		s.comparatorProperty().bind(tableBeheerContractTypes.comparatorProperty());
		tableBeheerContractTypes.setItems(s);

		cboMinContractLengte.getItems().addAll(ContractDoorlooptijd.values());
		cboStatus.getItems().addAll(ContractTypeStatus.values());
		cboTicketTijd.getItems().addAll(TicketAanmaakTijd.values());

		txfFilterMinLopendeContracten.textProperty().addListener((observable, oldValue, newValue) -> {
			txfFilterMinLopendeContracten.setText(StringFilters.numeric(newValue));
		});

		addTextfieldControle(txfNaam, lblErrorName, e -> Controles.stringNotEmpty(e, "Name may not be empty"));
		addTextfieldControle(txfMaxAfhandeltijd, lblErrorMaxAfhandeltijd,
				e -> Controles.stringOnlyNumbers(e, "Maximum processing time must be a number"));
		addTextfieldControle(txfCost, lblErrorCost,
				e -> Controles.stringBigDecimal(e, "Cost must be a numerical value"));

		cboTicketTijd.setOnMouseClicked(e -> {
			if (cboTicketTijd.getValue() == null) {
				setError(lblErrorTicketTime, "A ticket submission type must be selected", cboTicketTijd);
			}
		});

		cboMinContractLengte.setOnMouseClicked(e -> {
			if (cboMinContractLengte.getValue() == null) {
				setError(lblErrorMinContractLengte, "A minimum contract length must be selected", cboMinContractLengte);
			}
		});

		cboStatus.setOnMouseClicked(e -> {
			if (cboStatus.getValue() == null) {
				setError(lblErrorStatus, "A status must be selected", cboStatus);
			}
		});

		cboTicketTijd.setOnAction(e -> {
			clearError(lblErrorTicketTime, cboTicketTijd);
			enableSave();
			enableCancel();
		});

		cboMinContractLengte.setOnAction(e -> {
			clearError(lblErrorMinContractLengte, cboMinContractLengte);
			enableSave();
			enableCancel();
		});

		cboStatus.setOnAction(e -> {
			clearError(lblErrorStatus, cboStatus);
			enableSave();
			enableCancel();
		});

		chkApp.setOnAction(e -> {
			enableSave();
			enableCancel();
		});

		chkTelefoon.setOnAction(e -> {
			enableSave();
			enableCancel();
		});

		chkEmail.setOnAction(e -> {
			enableSave();
			enableCancel();
		});

		this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			Node selected = event.getPickResult().getIntersectedNode();
			if (!inHierarchy(anchorFilters, selected) && !inHierarchy(btnShowFilters, selected)) {
				anchorFilters.setVisible(false);
			}
		});

		tableBeheerContractTypes.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, oldcontracttype, newcontracttype) -> {
					if (newcontracttype != null) {
						if (btnCancel.isDisable()) {
							geselecteerdContractType = newcontracttype;
							vulDetailschermIn(geselecteerdContractType);
						} else {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("Unsaved changes");
							alert.setHeaderText("You have unsaved changes");
							alert.setContentText("Do you wish to discard your changes?");

							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == ButtonType.OK) {
								geselecteerdContractType = newcontracttype;
								vulDetailschermIn(geselecteerdContractType);
							}
						}
					}
				});

		maakDetailschermLeeg();

	}

	private void vulDetailschermIn(ContractType contractType) {
		txfNaam.setText(contractType.getNaam());
		txfMaxAfhandeltijd.setText(Integer.toString(contractType.getTicketAfhandeltijd()));
		txfCost.setText(contractType.getPrijs().toString());
		cboTicketTijd.setValue(contractType.getTicketAanmaakTijd());
		cboMinContractLengte.setValue(contractType.getMinimaleDoorlooptijd());
		cboStatus.setValue(contractType.getStatus());
		cboStatus.setDisable(false);

		Set<TicketAanmaakManier> aanmaken = contractType.getTicketAanmaakManier();
		if (aanmaken.contains(TicketAanmaakManier.Email)) {
			chkEmail.setSelected(true);
		} else {
			chkEmail.setSelected(false);
		}
		if (aanmaken.contains(TicketAanmaakManier.Telephone)) {
			chkTelefoon.setSelected(true);
		} else {
			chkTelefoon.setSelected(false);
		}
		if (aanmaken.contains(TicketAanmaakManier.Application)) {
			chkApp.setSelected(true);
		} else {
			chkApp.setSelected(false);
		}

		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);
		bestaandContractType = true;
		showDetailsContractType();
		clearAllErrors();

		if (contractType.getAantalLopendeContracten() > 0) {
			txfNaam.setDisable(true);
			txfCost.setDisable(true);
			txfMaxAfhandeltijd.setDisable(true);
			chkApp.setDisable(true);
			chkEmail.setDisable(true);
			chkTelefoon.setDisable(true);
			cboMinContractLengte.setDisable(true);
			cboTicketTijd.setDisable(true);
		}

		if (contractType.getAantalLopendeContracten() == 0) {
			txfNaam.setDisable(false);
			txfCost.setDisable(false);
			txfMaxAfhandeltijd.setDisable(false);
			chkApp.setDisable(false);
			chkEmail.setDisable(false);
			chkTelefoon.setDisable(false);
			cboMinContractLengte.setDisable(false);
			cboTicketTijd.setDisable(false);
		}

	}

	private void maakDetailschermLeeg() {
		txfNaam.setText("");
		txfMaxAfhandeltijd.setText("");
		txfCost.setText("");

		cboMinContractLengte.setValue(null);
		cboStatus.setValue(ContractTypeStatus.Active);
		cboStatus.setDisable(true);
		cboTicketTijd.setValue(null);

		chkApp.setSelected(false);
		chkEmail.setSelected(false);
		chkTelefoon.setSelected(false);

		txfNaam.setDisable(false);
		txfCost.setDisable(false);
		txfMaxAfhandeltijd.setDisable(false);
		chkApp.setDisable(false);
		chkEmail.setDisable(false);
		chkTelefoon.setDisable(false);
		cboMinContractLengte.setDisable(false);
		cboTicketTijd.setDisable(false);

		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);

		lblDetailsNumberContracts.setText("");
		lblDetailsNumberTickets.setText("");
		lblDetailsTicketSuccesRate.setText("");

		hideDetailsContractType();
		clearAllErrors();
		bestaandContractType = false;

	}

	@FXML
	public void addContractType(ActionEvent event) {
		if (btnCancel.isDisabled()) {
			tableBeheerContractTypes.getSelectionModel().clearSelection();
			maakDetailschermLeeg();

		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Unsaved changes");
			alert.setHeaderText("You have unsaved changes");
			alert.setContentText("Do you wish to discard your changes?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				maakDetailschermLeeg();
				tableBeheerContractTypes.getSelectionModel().clearSelection();
			}
		}
	}

	@FXML
	public void showFilters(ActionEvent event) {
		anchorFilters.setVisible(isVisible());
	}

	@FXML
	public void filterOnAction(Event event) {
		int minAantalLopendeContracten = -1;
		if (!txfFilterMinLopendeContracten.getText().isEmpty()) {
			minAantalLopendeContracten = Integer.parseInt(txfFilterMinLopendeContracten.getText());
		}
		Set<ContractTypeStatus> statussen = new HashSet<>();
		if (chkFilterActive.isSelected()) {
			statussen.add(ContractTypeStatus.Active);
		}
		if (chkFilterInactive.isSelected()) {
			statussen.add(ContractTypeStatus.Inactive);
		}
		if (statussen.isEmpty()) {
			statussen = ContractTypeStatus.valueSet();
		}

		dc.pasFilterAan(txfFilterName.getText(), statussen, minAantalLopendeContracten);
	}

	@FXML
	public void saveChanges(ActionEvent event) {
		try {
			clearAllErrors();
			Controles.stringNotEmpty(txfNaam.getText(), "Name may not be empty");
			Controles.stringOnlyNumbers(txfMaxAfhandeltijd.getText(), "Maximum processing time must be a number");
			Controles.stringBigDecimal(txfCost.getText(), "Cost must be a numerical value");

			Set<TicketAanmaakManier> aanmaken = new HashSet<>();
			if (chkEmail.isSelected()) {
				aanmaken.add(TicketAanmaakManier.Email);
			}
			if (chkApp.isSelected()) {
				aanmaken.add(TicketAanmaakManier.Application);
			}
			if (chkTelefoon.isSelected()) {
				aanmaken.add(TicketAanmaakManier.Telephone);
			}

			if (aanmaken.isEmpty()) {
				setErrorMessage(lblErrorTicketManner, "At least one manner of submitting a ticket must be selected");
				throw new Exception();
			}

			if (cboMinContractLengte.getValue() == null || cboStatus.getValue() == null
					|| cboTicketTijd.getValue() == null) {
				throw new Exception();
			}

			// Een nieuw contracttype toevoegen
			if (!bestaandContractType) {
				if (dc.bestaatContractTypeNaam(txfNaam.getText())) {
					setErrorMessage(lblErrorName, "A contract type with this name already exists");
					throw new Exception();
				}
				ContractType contractType = new ContractType(txfNaam.getText(), aanmaken, cboTicketTijd.getValue(),
						Integer.parseInt(txfMaxAfhandeltijd.getText()), cboMinContractLengte.getValue(),
						new BigDecimal(txfCost.getText()));
				dc.voegContractTypeToe(contractType);
				geselecteerdContractType = contractType;
				showDetailsContractType();
				tableBeheerContractTypes.setItems(dc.getContractTypesList());
			}
			// Een bestaand contracttype aanpassen
			else {
				geselecteerdContractType.setMinimaleDoorlooptijd(cboMinContractLengte.getValue());
				geselecteerdContractType.setNaam(txfNaam.getText());
				geselecteerdContractType.setPrijs(new BigDecimal(txfCost.getText()));
				geselecteerdContractType.setStatus(cboStatus.getValue());
				geselecteerdContractType.setTicketAanmaakManier(aanmaken);
				geselecteerdContractType.setTicketAanmaakTijd(cboTicketTijd.getValue());
				geselecteerdContractType.setTicketAfhandeltijd(Integer.parseInt(txfMaxAfhandeltijd.getText()));
				dc.pastContractTypeAan(geselecteerdContractType);
				tableBeheerContractTypes.refresh();
			}
			setConfirmationMessage(lblErrorSaveChanges, "Changes have been saved succesfully");
			btnSaveChanges.setDisable(true);
			btnCancel.setDisable(true);
			cboStatus.setDisable(false);
		} catch (Exception ex) {
			setErrorMessage(lblErrorSaveChanges,
					"Please ensure everything is filled in correctly. At least 1 method of submitting a ticket is required.");
		}
	}

	@FXML
	public void cancelChanges(ActionEvent event) {
		if (vboxDetails.isManaged()) {
			vulDetailschermIn(tableBeheerContractTypes.getSelectionModel().getSelectedItem());
		} else {
			maakDetailschermLeeg();
		}
	}

	private void hideDetailsContractType() {
		vboxDetails.setManaged(false);
		vboxDetails.setVisible(false);
	}

	private void showDetailsContractType() {
		lblDetailsNumberContracts.setText(Integer.toString(geselecteerdContractType.getAantalLopendeContracten()));
		lblDetailsNumberTickets.setText(
				Integer.toString(dc.geefAantalBehandeldeTicketsVanContractType(geselecteerdContractType.getNaam())));
		lblDetailsTicketSuccesRate.setText(Double.toString(
				dc.geefPercentageOpTijdBehandeldeTicketsVanContractType(geselecteerdContractType.getNaam())) + "%");
		vboxDetails.setManaged(true);
		vboxDetails.setVisible(true);
	}

	private void addTextfieldControle(TextField textfield, Label label, Consumer<String> controle) {
		textfield.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				controle.accept(newValue);
				clearError(label, textfield);
			} catch (IllegalArgumentException e) {
				setError(label, e.getMessage(), textfield);
			}
			enableCancel();
			enableSave();
		});
	}

	private void enableCancel() {
		btnCancel.setDisable(false);
	}

	private void enableSave() {
		try {
			Controles.stringNotEmpty(txfNaam.getText(), "Name may not be empty");
			Controles.stringOnlyNumbers(txfMaxAfhandeltijd.getText(), "Maximum processing time must be a number");
			Controles.stringBigDecimal(txfCost.getText(), "Cost may not be empty");

			if (cboStatus.getValue() == null) {
				throw new IllegalArgumentException("Status may not be empty");
			}
			if (cboTicketTijd.getValue() == null) {
				throw new IllegalArgumentException("Ticket submission time may not be empty");
			}
			if (cboMinContractLengte.getValue() == null) {
				throw new IllegalArgumentException("Minimum contract length may not be empty");
			}

			if (!chkApp.isSelected() && !chkEmail.isSelected() && !chkTelefoon.isSelected()) {
				throw new IllegalArgumentException("At least one checkbox most be selected for submission manner");
			}

			btnSaveChanges.setDisable(false);
		} catch (Exception ex) {
			btnSaveChanges.setDisable(true);
		}
	}

	private void clearAllErrors() {
		clearError(lblErrorName, txfNaam);
		clearError(lblErrorTicketTime, cboTicketTijd);
		clearError(lblErrorMaxAfhandeltijd, txfMaxAfhandeltijd);
		clearError(lblErrorMinContractLengte, cboMinContractLengte);
		clearError(lblErrorStatus, cboStatus);
		clearError(lblErrorCost, txfCost);

		clearErrorMessage(lblErrorTicketManner);
		clearErrorMessage(lblErrorSaveChanges);
	}

	@Override
	public boolean veranderScherm() {
		if (!btnCancel.isDisable()) {
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
}
