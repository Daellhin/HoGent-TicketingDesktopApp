package gui.admin;

import static util.FxUtil.clearError;
import static util.FxUtil.clearErrorMessage;
import static util.FxUtil.inHierarchy;
import static util.FxUtil.setConfirmationMessage;
import static util.FxUtil.setError;
import static util.FxUtil.setErrorMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import domain.Adres;
import domain.Constanten;
import domain.Werknemer;
import domain.controllers.BeheerWerknemersController;
import domain.enumerations.Dienst;
import domain.enumerations.GebruikersStatus;
import domain.enumerations.WerknemersType;
import gui.Opslaanbaar;
import gui.general.GebruikerScherm;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import util.Controles;
import util.StringFilters;

public class BeheerWerknemers extends StackPane implements Opslaanbaar {

	@FXML
	private Label lblErrorAddEmployee;
	@FXML
	private Label lblErrorDivision;
	@FXML
	private Label lblErrorSaveChanges;
	@FXML
	private Label lblErrorName;
	@FXML
	private Label lblErrorUsername;
	@FXML
	private Label lblErrorFirstName;
	@FXML
	private Label lblErrorEmail;
	@FXML
	private Label lblErrorRole;
	@FXML
	private Label lblErrorCountry;
	@FXML
	private Label lblErrorPostalCode;
	@FXML
	private Label lblErrorTown;
	@FXML
	private Label lblErrorStreet;
	@FXML
	private Label lblErrorHouseNr;
	@FXML
	private Label lblErrorBoxNr;
	@FXML
	private Label lblErrorTelephone;
	@FXML
	private TableView<Werknemer> tableBeheerWerknemers;
	@FXML
	private TableColumn<Werknemer, Number> colWerknemernummer;
	@FXML
	private TableColumn<Werknemer, String> colUsername;
	@FXML
	private TableColumn<Werknemer, String> colNaam;
	@FXML
	private TableColumn<Werknemer, String> colVoornaam;
	@FXML
	private TableColumn<Werknemer, String> colRol;
	@FXML
	private TableColumn<Werknemer, String> colStatus;
	@FXML
	private Button btnShowFilters;
	@FXML
	private Label lblEmployeeInformation;
	@FXML
	private Label lblManageEmployees;
	@FXML
	private Button btnAddEmployees;
	@FXML
	private Button btnSaveChanges;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblEmployeeNumber;
	@FXML
	private TextField txfEmployeeNumber;
	@FXML
	private Label lblEmployeeSince;
	@FXML
	private TextField txfEmployeeSince;
	@FXML
	private Label lblUsername;
	@FXML
	private TextField txfUsername;
	@FXML
	private Label lblName;
	@FXML
	private TextField txfName;
	@FXML
	private Label lblFirstName;
	@FXML
	private TextField txfFirstName;
	@FXML
	private Label lblEmail;
	@FXML
	private TextField txfEmail;
	@FXML
	private Label lblStatus;
	@FXML
	private ComboBox<GebruikersStatus> cboStatus;
	@FXML
	private Label lblRole;
	@FXML
	private ComboBox<WerknemersType> cboRole;
	@FXML
	private ComboBox<Dienst> cboDivision;
	@FXML
	private Label lblCountry;
	@FXML
	private ComboBox<String> cboCountry;
	@FXML
	private Label lblTown;
	@FXML
	private Label lblPostalCode;
	@FXML
	private TextField txfPostCode;
	@FXML
	private TextField txfTown;
	@FXML
	private TextField txfStreetName;
	@FXML
	private Label lblStreetName;
	@FXML
	private TextField txfHouseNr;
	@FXML
	private Label lblHouseNr;
	@FXML
	private Label lblBusNr;
	@FXML
	private TextField txfBusNr;
	@FXML
	private Button btnRemoveTelephoneNumber;
	@FXML
	private Button btnAddTelephoneNumber;
	@FXML
	private TextField txfTelephoneNumber;
	@FXML
	private ListView<String> listTelephoneNumbers;
	@FXML
	private Label lblTelephoneNumbers;
	@FXML
	private AnchorPane anchorFilters;
	@FXML
	private TextField txfFilterEmployeeNr;
	@FXML
	private TextField txfFilterUsername;
	@FXML
	private TextField txfFilterName;
	@FXML
	private CheckBox chkFilterActive;
	@FXML
	private CheckBox chkFilterBlocked;
	@FXML
	private CheckBox chkFiltersInactive;
	@FXML
	private TextField txfFilterFirstName;
	@FXML
	private CheckBox chkFilterAdministrator;
	@FXML
	private CheckBox chkFilterSupportManager;
	@FXML
	private CheckBox chkFiltersTechnieker;

	private ObservableList<String> telefoonnummers;
	private List<String> namenLanden;
	private List<Locale> lijstLanden;
	private GebruikersStatus initieleGebruikersStatus;

	private Werknemer geselecteerdeWerknemer;
	private GebruikerScherm scherm;
	private BeheerWerknemersController dc;

	public BeheerWerknemers(GebruikerScherm scherm) {
		this.scherm = scherm;
		this.dc = new BeheerWerknemersController();

		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			System.out.println("Probleem met tonen " + this.getClass().getSimpleName());
			ex.printStackTrace();
		}

		addGrowHorizontal();
		addLanden();
		buildGui();
	}

	private void addGrowHorizontal() {
		this.setPrefWidth(scherm.getWidth() - 300);
		scherm.widthProperty().addListener(event -> {
			this.setPrefWidth(scherm.getWidth() - 300);
		});
	}

	private void addLanden() {
		lijstLanden = new ArrayList<Locale>();
		for (String countryCode : Locale.getISOCountries()) {
			Locale land = new Locale("", countryCode);
			lijstLanden.add(land);
		}

		namenLanden = lijstLanden.stream().map(Locale::getDisplayCountry).collect(Collectors.toList());
		lijstLanden.add(new Locale("ru", "Moon"));
		namenLanden.add("Moon");
		Collections.sort(namenLanden);
	}

	private void buildGui() {
		// TableView werknemers
		colWerknemernummer.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
		colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGebruikersnaam()));
		colNaam.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaam()));
		colVoornaam.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVoornaam()));
		colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
		colRol.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getWerknemersType().toString()));

		SortedList<Werknemer> s = new SortedList<>(dc.getWerknemerList());
		s.comparatorProperty().bind(tableBeheerWerknemers.comparatorProperty());
		tableBeheerWerknemers.setItems(s);

		// Comboboxen vullen
		cboCountry.getItems().addAll(namenLanden);
		cboStatus.getItems().addAll(GebruikersStatus.values());
		cboRole.getItems().addAll(WerknemersType.values());
		cboDivision.getItems().addAll(Dienst.values());

		// Inputfilters
		txfHouseNr.textProperty().addListener((observable, oldValue, newValue) -> {
			txfHouseNr.setText(StringFilters.numeric(newValue));
			txfHouseNr.setText(StringFilters.maxLength(newValue, 7));
		});

		// Controles
		addTextfieldControle(txfUsername, lblErrorUsername,
				e -> Controles.stringMinimum(e, 6, "Username must be 6 charcters long"));
		addTextfieldControle(txfName, lblErrorName,
				e -> Controles.stringContainsLetters(e, "Name must contain letters"));
		addTextfieldControle(txfFirstName, lblErrorFirstName,
				e -> Controles.stringContainsLetters(e, "First name must contain letters"));
		addTextfieldControle(txfPostCode, lblErrorPostalCode,
				e -> Controles.stringNotEmpty(e, "Postal code may not be empty"));
		addTextfieldControle(txfTown, lblErrorTown, e -> Controles.stringCity(e, "Town must be valid"));
		addTextfieldControle(txfStreetName, lblErrorStreet,
				e -> Controles.stringNotEmpty(e, "Street name may not be empty"));
		addTextfieldControle(txfHouseNr, lblErrorHouseNr,
				e -> Controles.stringNotEmpty(e, "House number may not be empty"));
		addTextfieldControle(txfBusNr, lblErrorBoxNr,
				e -> Controles.stringAlphaNumericAllowsNull(e, "Bus number must be aphanumeric"));

		// Listeners
		txfTelephoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue.isEmpty()) {
					Controles.stringTelephoneNumber(newValue,
							"Telephone number must conform to the international standard. Must start with + eg: +32472643661");
					btnAddTelephoneNumber.setDisable(false);
				}
				clearError(lblErrorTelephone, txfTelephoneNumber);
			} catch (IllegalArgumentException e) {
				setError(lblErrorTelephone, e.getMessage(), txfTelephoneNumber);
				btnAddTelephoneNumber.setDisable(true);
			}
			enableCancel();
		});

		txfEmail.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				Controles.stringEmail(newValue, "Email must be in a valid format");
				clearErrorMessage(lblErrorEmail);
				enableSave();
			} catch (IllegalArgumentException e) {
				setErrorMessage(lblErrorEmail, e.getMessage());
			}

			enableCancel();
		});

		// On mouse clicked
		cboCountry.setOnMouseClicked(e -> {
			if (cboCountry.getValue() == null) {
				setError(lblErrorCountry, "A country must be selected", cboCountry);
			}
		});

		cboRole.setOnMouseClicked(e -> {
			if (cboRole.getValue() == null) {
				setError(lblErrorRole, "A role must be selected", cboRole);
			}
		});

		cboDivision.setOnMouseClicked(e -> {
			if (cboDivision.getValue() == null) {
				setError(lblErrorDivision, "A division must be selected", cboDivision);
			}
		});

		// On action
		cboCountry.setOnAction(e -> {
			clearError(lblErrorCountry, cboCountry);
			enableSave();
			enableCancel();
		});

		cboRole.setOnAction(e -> {
			clearError(lblErrorRole, cboRole);
			enableSave();
			enableCancel();
		});

		cboDivision.setOnAction(e -> {
			clearError(lblErrorDivision, cboDivision);
			enableSave();
			enableCancel();
		});

		cboStatus.setOnAction(e -> {
			enableSave();
			enableCancel();
		});

		// Scherm invullen als een werknemer word geselecteerd
		tableBeheerWerknemers.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, oldPerson, newPerson) -> {
					if (newPerson != null) {
						if (btnSaveChanges.isDisable()) {
							geselecteerdeWerknemer = newPerson;
							vulDetailschermIn(newPerson);
						} else {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("Unsaved changes");
							alert.setHeaderText("You have unsaved changes");
							alert.setContentText("Do you wish to discard your changes?");

							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == ButtonType.OK) {
								if (!txfEmployeeNumber.getText().isEmpty()) {
									vulDetailschermIn(tableBeheerWerknemers.getSelectionModel().getSelectedItem());
								} else {
									maakDetailschermLeeg();
								}

								btnSaveChanges.setDisable(true);
								btnCancel.setDisable(true);
							}
						}
					}
				});

		// Checkt of de er buiten het filtermenu is geklikt, hides het menu
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			Node selected = event.getPickResult().getIntersectedNode();
			if (!inHierarchy(anchorFilters, selected) && !inHierarchy(btnShowFilters, selected)) {
				anchorFilters.setVisible(false);
			}
		});

		maakDetailschermLeeg();
	}

	private void vulDetailschermIn(Werknemer newPerson) {
		// om later te controleren of de gebruiker gedeblokeerd word, en er een
		// gedeblokeerde aanmeldpoging moet toegevoegd worden
		initieleGebruikersStatus = newPerson.getStatus();

		// Textfields
		txfEmployeeNumber.setText(Integer.toString(newPerson.getId()));
		txfEmployeeSince.setText(newPerson.getDatumInDienstTreding().toString());
		txfUsername.setText(newPerson.getGebruikersnaam());
		txfFirstName.setText(newPerson.getVoornaam());
		txfName.setText(newPerson.getNaam());
		txfEmail.setText(newPerson.getEmail());
		txfPostCode.setText(newPerson.getAdres().getPostcode());
		txfTown.setText(newPerson.getAdres().getWoonplaats());
		txfStreetName.setText(newPerson.getAdres().getStraat());
		txfHouseNr.setText(Integer.toString(newPerson.getAdres().getHuisnummer()));
		txfBusNr.setText(newPerson.getAdres().getBusnr());

		// Status
		cboStatus.setValue(newPerson.getStatus());
		cboStatus.setDisable(false);

		// Role
		cboRole.setValue(newPerson.getWerknemersType());
		cboRole.setDisable(false);

		// Dienst
		cboDivision.setValue(newPerson.getDienst());
		cboDivision.setDisable(false);

		// Country
		cboCountry.setValue(geefDisplayCountry(newPerson.getAdres().getLand()));

		// Telefoonnummers
		telefoonnummers = FXCollections.observableArrayList(newPerson.getTelefoonnummers());
		listTelephoneNumbers.setItems(telefoonnummers);
		txfTelephoneNumber.setText("");

		// Buttons disablen
		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);

		if (telefoonnummers.size() > Constanten.MIN_AANTAL_TELEFOONNUMMERS) {
			btnRemoveTelephoneNumber.setDisable(false);
		} else {
			btnRemoveTelephoneNumber.setDisable(true);
		}
		btnAddTelephoneNumber.setDisable(true);

		// Errors leegmaken
		clearAllErrors();
	}

	private void maakDetailschermLeeg() {
		// Textfields
		txfEmployeeNumber.setText("");
		txfEmployeeSince.setText("");
		txfUsername.setText("");
		txfFirstName.setText("");
		txfName.setText("");
		txfEmail.setText("");
		txfPostCode.setText("");
		txfTown.setText("");
		txfStreetName.setText("");
		txfHouseNr.setText("");
		txfBusNr.setText("");

		// Status
		cboStatus.setValue(GebruikersStatus.Active);
		cboStatus.setDisable(true);

		// Role
		cboRole.setValue(null);

		// Dienst
		cboDivision.setValue(null);

		// Country
		cboCountry.setValue(null);

		// Telefoonnummers
		telefoonnummers = FXCollections.observableArrayList(new ArrayList<String>());
		listTelephoneNumbers.setItems(telefoonnummers);
		txfTelephoneNumber.setText("");

		// buttons disablen
		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);

		btnRemoveTelephoneNumber.setDisable(true);
		btnAddTelephoneNumber.setDisable(true);

		// errors leegmaken
		clearAllErrors();
	}

	@FXML
	public void addEmployee(ActionEvent event) {
		if (btnCancel.isDisabled()) {
			maakDetailschermLeeg();
			btnSaveChanges.setDisable(true);
			btnCancel.setDisable(true);
			tableBeheerWerknemers.getSelectionModel().clearSelection();
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Unsaved changes");
			alert.setHeaderText("You have unsaved changes");
			alert.setContentText("Do you wish to discard your changes?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				maakDetailschermLeeg();
				btnSaveChanges.setDisable(true);
				btnCancel.setDisable(true);
				tableBeheerWerknemers.getSelectionModel().clearSelection();
			}
		}
	}

	@FXML
	public void saveChanges(ActionEvent event) {
		try {
			// Controles
			Controles.stringMinimum(txfUsername.getText(), 6, "Username must be 6 characters long");
			Controles.stringContainsLetters(txfName.getText(), "Name must contain letters");
			Controles.stringContainsLetters(txfFirstName.getText(), "First name must contain letters");
			Controles.stringContainsLetters(txfEmail.getText(), "Email must be in a valid format");
			Controles.stringNotEmpty(txfPostCode.getText(), "Postal code may not be empty");
			Controles.stringCity(txfTown.getText(), "Town must be valid");
			Controles.stringNotEmpty(txfStreetName.getText(), "Street name may not be empty");
			Controles.stringNotEmpty(txfHouseNr.getText(), "House number may not be empty");

			if (cboCountry.getValue() == null || telefoonnummers.isEmpty() || cboRole.getValue() == null
					|| cboDivision.getValue() == null) {
				throw new Exception();
			}

			// Nieuwe werknemer
			if (txfEmployeeNumber.getText().isEmpty()) {
				if (dc.bestaatUsername(txfUsername.getText())) {
					setErrorMessage(lblErrorUsername, "A username with this name already exists");
					throw new Exception();
				}
				Adres adres;
				if (txfBusNr.getText().isEmpty()) {
					adres = new Adres(geefGeselecteerdLand(), txfPostCode.getText(), txfTown.getText(),
							txfStreetName.getText(), Integer.parseInt(txfHouseNr.getText()));
				} else {
					adres = new Adres(geefGeselecteerdLand(), txfPostCode.getText(), txfTown.getText(),
							txfStreetName.getText(), Integer.parseInt(txfHouseNr.getText()), txfBusNr.getText());
				}
				Werknemer werknemer = new Werknemer(txfUsername.getText(), new ArrayList<String>(telefoonnummers),
						adres, txfName.getText(), txfFirstName.getText(), txfEmail.getText(), cboRole.getValue(),
						cboDivision.getValue());
				dc.voegWerknemerToe(werknemer);
				txfEmployeeNumber.setText(Integer.toString(werknemer.getId()));
				txfEmployeeSince.setText(werknemer.getDatumInDienstTreding().toString());
				btnSaveChanges.setDisable(true);
				btnCancel.setDisable(true);
				tableBeheerWerknemers.setItems(dc.getWerknemerList());
				setConfirmationMessage(lblErrorSaveChanges, "New employee has been saved succesfully");
			}
			// Bestaande werknemer
			else {
				geselecteerdeWerknemer.setGebruikersnaam(txfUsername.getText());
				geselecteerdeWerknemer.setNaam(txfName.getText());
				geselecteerdeWerknemer.setVoornaam(txfFirstName.getText());
				geselecteerdeWerknemer.setTelefoonnummers(telefoonnummers);
				geselecteerdeWerknemer.setEmail(txfEmail.getText());
				geselecteerdeWerknemer.setWerknemersType(cboRole.getValue());
				geselecteerdeWerknemer.setStatus(cboStatus.getValue());
				geselecteerdeWerknemer.setDienst(cboDivision.getValue());
				if (initieleGebruikersStatus == GebruikersStatus.Blocked
						&& cboStatus.getValue() != GebruikersStatus.Blocked) {
					dc.deblokkeerWerknemer(txfUsername.getText());
				}
				geselecteerdeWerknemer.getAdres().setLand(geefGeselecteerdLand());
				geselecteerdeWerknemer.getAdres().setWoonplaats(txfTown.getText());
				geselecteerdeWerknemer.getAdres().setPostcode(txfPostCode.getText());
				geselecteerdeWerknemer.getAdres().setHuisnummer(Integer.parseInt(txfHouseNr.getText()));
				if (txfBusNr.getText() == null || txfBusNr.getText().isEmpty()) {
					geselecteerdeWerknemer.getAdres().setBusnr(null);
				} else {
					geselecteerdeWerknemer.getAdres().setBusnr(txfBusNr.getText());
				}

				dc.pasWerknemerAan(geselecteerdeWerknemer);
				btnSaveChanges.setDisable(true);
				tableBeheerWerknemers.setItems(dc.getWerknemerList());
				setConfirmationMessage(lblErrorSaveChanges, "Changes have been saved succesfully");
				tableBeheerWerknemers.refresh();
			}
			btnSaveChanges.setDisable(true);
			btnCancel.setDisable(true);
		} catch (Exception e) {
			setErrorMessage(lblErrorSaveChanges,
					"The form must be filled in correctly, there must be at least one telephone number and one contact");
		}
	}

	@FXML
	public void cancelChanges(ActionEvent event) {
		if (!txfEmployeeNumber.getText().isEmpty()) {
			vulDetailschermIn(tableBeheerWerknemers.getSelectionModel().getSelectedItem());
		} else {
			maakDetailschermLeeg();
		}

		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);
	}

	@FXML
	// Mag enkel opgeroepen worden als er een geldig telefoonnummer ingevoerd is
	public void addTelephoneNumber(ActionEvent event) {
		telefoonnummers.add(txfTelephoneNumber.getText());
		txfTelephoneNumber.setText("");
		btnAddTelephoneNumber.setDisable(true);

		if (telefoonnummers.size() > Constanten.MIN_AANTAL_TELEFOONNUMMERS) {
			btnRemoveTelephoneNumber.setDisable(false);
		}

		enableCancel();
		enableSave();
	}

	@FXML
	// Kan operoepen worden als er nog geen telefoonnummer geselecteerd is
	public void removeTelephoneNumber(ActionEvent event) {
		String geselecteerdTelefoonnummer = listTelephoneNumbers.getSelectionModel().getSelectedItem();
		if (geselecteerdTelefoonnummer != null) {
			telefoonnummers.remove(geselecteerdTelefoonnummer);
			listTelephoneNumbers.getSelectionModel().clearSelection();

			if (telefoonnummers.size() <= Constanten.MIN_AANTAL_TELEFOONNUMMERS) {
				btnRemoveTelephoneNumber.setDisable(true);
			}

			enableCancel();
			enableSave();
		}
	}

	@FXML
	public void showFilters(ActionEvent event) {
		anchorFilters.setVisible(true);
	}

	@FXML
	public void filterOnAction(Event event) {
		int employeeNr = -1;
		if (!txfFilterEmployeeNr.getText().isEmpty()) {
			employeeNr = Integer.parseInt(txfFilterEmployeeNr.getText());
		}

		Set<GebruikersStatus> statusen = new HashSet<>();
		if (chkFilterActive.isSelected()) {
			statusen.add(GebruikersStatus.Active);
		}
		if (chkFilterBlocked.isSelected()) {
			statusen.add(GebruikersStatus.Blocked);
		}
		if (chkFiltersInactive.isSelected()) {
			statusen.add(GebruikersStatus.Inactive);
		}
		if (statusen.isEmpty()) {
			statusen = GebruikersStatus.valueSet();
		}

		Set<WerknemersType> rollen = new HashSet<>();
		if (chkFilterAdministrator.isSelected()) {
			rollen.add(WerknemersType.Administrator);
		}
		if (chkFilterSupportManager.isSelected()) {
			rollen.add(WerknemersType.SupportManager);
		}
		if (chkFiltersTechnieker.isSelected()) {
			rollen.add(WerknemersType.Technician);
		}
		if (rollen.isEmpty()) {
			rollen = WerknemersType.valueSet();
		}

		dc.pasFilterAan(employeeNr, txfFilterUsername.getText(), txfFilterName.getText(), txfFilterFirstName.getText(),
				statusen, rollen);
	}

	// -- hulpmethodes --
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
			Controles.stringMinimum(txfUsername.getText(), 6, "Username must be 6 charcters long");
			Controles.stringContainsLetters(txfName.getText(), "Name must contain letters");
			Controles.stringContainsLetters(txfFirstName.getText(), "FirstName must contain letters");
			Controles.stringContainsLetters(txfEmail.getText(), "Emailmust contain letters");
			Controles.stringNotEmpty(txfPostCode.getText(), "Postal code may not be empty");
			Controles.stringCity(txfTown.getText(), "Town must be valid");
			Controles.stringNotEmpty(txfStreetName.getText(), "Street name may not be empty");
			Controles.stringNotEmpty(txfHouseNr.getText(), "House number may not be empty");

			if (cboCountry.getValue() == null || telefoonnummers.isEmpty() || cboRole.getValue() == null
					|| cboDivision.getValue() == null) {
				throw new Exception("lijsten leeg");
			}

			btnSaveChanges.setDisable(false);
		} catch (Exception e) {
			btnSaveChanges.setDisable(true);
		}
	}

	private String geefGeselecteerdLand() {
		return lijstLanden.stream().filter(l -> l.getDisplayCountry().equals(cboCountry.getValue())).findFirst().get()
				.getCountry();
	}

	private String geefDisplayCountry(String land) {
		return lijstLanden.stream().filter(l -> l.getCountry().equals(land)).findFirst().get().getDisplayCountry();
	}

	private void clearAllErrors() {
		clearError(lblErrorUsername, txfUsername);
		clearError(lblErrorName, txfName);
		clearError(lblErrorFirstName, txfFirstName);
		clearError(lblErrorEmail, txfEmail);
		clearError(lblErrorRole, cboRole);
		clearError(lblErrorDivision, cboDivision);
		clearError(lblErrorCountry, cboCountry);
		clearError(lblErrorPostalCode, txfPostCode);
		clearError(lblErrorTown, txfTown);
		clearError(lblErrorStreet, txfStreetName);
		clearError(lblErrorHouseNr, txfHouseNr);
		clearError(lblErrorBoxNr, txfBusNr);
		clearError(lblErrorTelephone, txfTelephoneNumber);

		clearErrorMessage(lblErrorAddEmployee);
		clearErrorMessage(lblErrorSaveChanges);
	}

	// -- hulpmethodes --

	@Override
	public boolean veranderScherm() {
		if (btnCancel.isVisible() && !btnCancel.isDisable()) {
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
