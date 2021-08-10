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
import domain.Contactpersoon;
import domain.Contract;
import domain.Klant;
import domain.controllers.BeheerKlantenController;
import domain.enumerations.GebruikersStatus;
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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import util.Controles;
import util.StringFilters;

public class BeheerKlanten extends StackPane implements Opslaanbaar {
	@FXML
	private TableView<Klant> tableBeheerKlanten;
	@FXML
	private TableColumn<Klant, String> colUsername;
	@FXML
	private TableColumn<Klant, Number> colKlantnummer;
	@FXML
	private TableColumn<Klant, String> colStatus;
	@FXML
	private TableColumn<Klant, String> colNaamBedrijf;
	@FXML
	private Label lblManageCustomers;
	@FXML
	private Button btnAddCustomer;
	@FXML
	private Label lblCustomerInformation;
	@FXML
	private Label lblCustomerNumber;
	@FXML
	private TextField txfcustomerNumber;
	@FXML
	private Label lblCustomerSince;
	@FXML
	private TextField txfCustomerSince;
	@FXML
	private Label lblUsername;
	@FXML
	private TextField txfUsername;
	@FXML
	private Label lblStatus;
	@FXML
	private ComboBox<GebruikersStatus> cboStatus;
	@FXML
	private Label lblErrorUsername;
	@FXML
	private Label lblCompanyName;
	@FXML
	private TextField txfCompanyName;
	@FXML
	private Label lblErrorCompanyName;
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
	private Label lblErrorPostalCode;
	@FXML
	private Label lblErrorTown;
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
	private TextField txfStreetName;
	@FXML
	private Label lblErrorStreet;
	@FXML
	private Label lblErrorHouseNr;
	@FXML
	private Label lblTelephoneNumbers;
	@FXML
	private Button btnAddTelephoneNumber;
	@FXML
	private ListView<String> listTelephoneNumbers;
	@FXML
	private Button btnRemoveTelephoneNumber;
	@FXML
	private Label lblErrorTelephone;
	@FXML
	private Label lblErrorBoxNr;
	@FXML
	private Button btnSaveChanges;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblContracts;
	@FXML
	private TableView<Contract> contractsTable;
	@FXML
	private TableColumn<Contract, Number> colContractsNumber;
	@FXML
	private TableColumn<Contract, String> colContractsType;
	@FXML
	private TableColumn<Contract, String> colContractsStatus;
	@FXML
	private TableColumn<Contract, String> colContractsStartDate;
	@FXML
	private TableColumn<Contract, String> colContractsEndDate;
	@FXML
	private Label lblContacts;
	@FXML
	private TableView<Contactpersoon> contactsTable;
	@FXML
	private TableColumn<Contactpersoon, String> colContactsName;
	@FXML
	private TableColumn<Contactpersoon, String> colContactsFirstName;
	@FXML
	private TableColumn<Contactpersoon, String> colContactsEmail;
	@FXML
	private Button btnRemoveContact;
	@FXML
	private Button btnAddContact;
	@FXML
	private TextField txfNewContactName;
	@FXML
	private TextField txfNewContactFirstName;
	@FXML
	private TextField txfNewContactEmail;
	@FXML
	private TextField txfTelephoneNumber;
	@FXML
	private Label lblErrorContactName;
	@FXML
	private Label lblErrorContactFirstName;
	@FXML
	private Label lblErrorContactEmail;
	@FXML
	private Label lblErrorAddCustomer;
	@FXML
	private Label lblErrorSaveChanges;
	@FXML
	private Label lblErrorCountry;
	@FXML
	private Button btnShowFilters;
	@FXML
	private AnchorPane anchorFilters;
	@FXML
	private TextField txfFilterCustomerNr;
	@FXML
	private TextField txfFilterUsername;
	@FXML
	private TextField txfFilterCompanyName;
	@FXML
	private CheckBox chkFilterActive;
	@FXML
	private CheckBox chkFilterBlocked;
	@FXML
	private CheckBox chkFiltersInactive;

	private ObservableList<String> telefoonnummers;
	private ObservableList<Contactpersoon> contactpersonen;
	private List<String> namenLanden;
	private List<Locale> lijstLanden;
	private GebruikersStatus initieleGebruikersStatus;

	private Klant geselecteerdeKlant;
	private GebruikerScherm scherm;
	private BeheerKlantenController dc;

	public BeheerKlanten(GebruikerScherm scherm) {
		this.scherm = scherm;
		this.dc = new BeheerKlantenController();

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
		// TableView klanten
		colKlantnummer.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
		colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGebruikersnaam()));
		colNaamBedrijf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBedrijfsNaam()));
		colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));

		SortedList<Klant> s = new SortedList<>(dc.getKlantList());
		s.comparatorProperty().bind(tableBeheerKlanten.comparatorProperty());
		tableBeheerKlanten.setItems(s);

		// TableView contracten
		colContractsNumber.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNummer()));
		colContractsType.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getContractType().getNaam()));
		colContractsStatus
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
		colContractsStartDate.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getStartDatum().toString()));
		colContractsEndDate.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getEindDatum().toString()));

		// TableView contactpersoonen
		colContactsName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaam()));
		colContactsName.setCellFactory(TextFieldTableCell.forTableColumn());
		colContactsFirstName
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVoornaam()));
		colContactsFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
		colContactsEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
		colContactsEmail.setCellFactory(TextFieldTableCell.forTableColumn());

		// Comboboxen vullen
		cboCountry.getItems().addAll(namenLanden);
		cboStatus.getItems().addAll(GebruikersStatus.values());

		// Inputfilters
		txfHouseNr.textProperty().addListener((observable, oldValue, newValue) -> {
			txfHouseNr.setText(StringFilters.numeric(newValue));
			txfHouseNr.setText(StringFilters.maxLength(newValue, 7));
		});

		// Controles
		addTextfieldControle(txfUsername, lblErrorUsername,
				e -> Controles.stringMinimum(e, 6, "Username must be 6 charcters long"));
		addTextfieldControle(txfCompanyName, lblErrorCompanyName,
				e -> Controles.stringContainsLetters(e, "Company name must contain letters"));
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

		txfNewContactName.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue.isEmpty()) {
					Controles.stringOnlyLetters(newValue, "Name may only contain letters");
				}
				clearError(lblErrorContactName, txfNewContactName);
			} catch (IllegalArgumentException e) {
				setError(lblErrorContactName, e.getMessage(), txfNewContactName);
			}
			checkAddContactButton();
			enableCancel();
		});

		txfNewContactFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue.isEmpty()) {
					Controles.stringOnlyLetters(newValue, "First name may only contain letters");
				}
				clearError(lblErrorContactFirstName, txfNewContactFirstName);
			} catch (IllegalArgumentException e) {
				setError(lblErrorContactFirstName, e.getMessage(), txfNewContactFirstName);
			}
			checkAddContactButton();
			enableCancel();
		});

		txfNewContactEmail.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue.isEmpty()) {
					Controles.stringEmail(newValue, "Email must be in a valid format");
				}
				clearError(lblErrorContactEmail, txfNewContactEmail);
			} catch (IllegalArgumentException e) {
				setError(lblErrorContactEmail, e.getMessage(), txfNewContactEmail);
			}
			checkAddContactButton();
			enableCancel();
		});

		// On mouse clicked
		cboCountry.setOnMouseClicked(e -> {
			if (cboCountry.getValue() == null) {
				setError(lblErrorCountry, "A country must be selected", cboCountry);
			}
		});

		// On action
		cboCountry.setOnAction(e -> {
			clearError(lblErrorCountry, cboCountry);
			enableSave();
			enableCancel();
		});

		cboStatus.setOnAction(e -> {
			enableSave();
			enableCancel();
		});

		// Scherm invullen als een klant word geselecteerd
		tableBeheerKlanten.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, oldPerson, newPerson) -> {
					if (newPerson != null) {
						if (btnSaveChanges.isDisable()) {
							geselecteerdeKlant = newPerson;
							vulDetailschermIn(newPerson);
						} else {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("Unsaved changes");
							alert.setHeaderText("You have unsaved changes");
							alert.setContentText("Do you wish to discard your changes?");

							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == ButtonType.OK) {
								if (!txfcustomerNumber.getText().isEmpty()) {
									vulDetailschermIn(tableBeheerKlanten.getSelectionModel().getSelectedItem());
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

	private void vulDetailschermIn(Klant newPerson) {
		// om later te controleren of de gebruiker gedeblokeerd word, en er een
		// gedeblokeerde aanmeldpoging moet toegevoegd worden
		initieleGebruikersStatus = newPerson.getStatus();

		// Textfields
		txfcustomerNumber.setText(Integer.toString(newPerson.getId()));
		txfCustomerSince.setText(newPerson.getDatumKlantGeworden().toString());
		txfUsername.setText(newPerson.getGebruikersnaam());
		txfCompanyName.setText(newPerson.getBedrijfsNaam());
		txfPostCode.setText(newPerson.getAdres().getPostcode());
		txfTown.setText(newPerson.getAdres().getWoonplaats());
		txfStreetName.setText(newPerson.getAdres().getStraat());
		txfHouseNr.setText(Integer.toString(newPerson.getAdres().getHuisnummer()));
		txfBusNr.setText(newPerson.getAdres().getBusnr());

		// Country
		cboCountry.setValue(geefDisplayCountry(newPerson.getAdres().getLand()));

		// Status
		cboStatus.setValue(newPerson.getStatus());
		cboStatus.setDisable(false);

		// Telefoonnummers
		telefoonnummers = FXCollections.observableArrayList(newPerson.getTelefoonnummers());
		listTelephoneNumbers.setItems(telefoonnummers);
		txfTelephoneNumber.setText("");

		// Contracten
		contractsTable.setItems(FXCollections.observableArrayList(newPerson.getContracten()));

		// Contacten
		contactpersonen = FXCollections.observableArrayList(newPerson.getContactpersonen());
		contactsTable.setItems(contactpersonen);
		txfNewContactName.setText("");
		txfNewContactFirstName.setText("");
		txfNewContactEmail.setText("");

		// Buttons disablen
		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);

		if (telefoonnummers.size() > Constanten.MIN_AANTAL_TELEFOONNUMMERS) {
			btnRemoveTelephoneNumber.setDisable(false);
		} else {
			btnRemoveTelephoneNumber.setDisable(true);
		}
		btnAddTelephoneNumber.setDisable(true);

		if (contactpersonen.size() > Constanten.MIN_AANTAL_CONTACTPERSONEN) {
			btnRemoveContact.setDisable(false);
		} else {
			btnRemoveContact.setDisable(true);
		}
		btnAddContact.setDisable(true);

		// Errors leegmaken
		clearAllErrors();
	}

	private void maakDetailschermLeeg() {
		// Textfields
		txfcustomerNumber.setText("");
		txfCustomerSince.setText("");
		txfUsername.setText("");
		txfCompanyName.setText("");
		txfPostCode.setText("");
		txfTown.setText("");
		txfStreetName.setText("");
		txfHouseNr.setText("");
		txfBusNr.setText("");

		// Country
		cboCountry.setValue(null);

		// Status
		cboStatus.setValue(GebruikersStatus.Active);
		cboStatus.setDisable(true);

		// Telefoonnummers
		telefoonnummers = FXCollections.observableArrayList(new ArrayList<String>());
		listTelephoneNumbers.setItems(telefoonnummers);
		txfTelephoneNumber.setText("");

		// Contracten
		contractsTable.setItems(FXCollections.observableArrayList(new ArrayList<Contract>()));

		// Contacten
		contactpersonen = FXCollections.observableArrayList(new ArrayList<Contactpersoon>());
		contactsTable.setItems(contactpersonen);
		txfNewContactEmail.setText("");
		txfNewContactFirstName.setText("");
		txfNewContactName.setText("");

		// Buttons disablen
		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);

		btnRemoveTelephoneNumber.setDisable(true);
		btnAddTelephoneNumber.setDisable(true);

		btnRemoveContact.setDisable(true);
		btnAddContact.setDisable(true);

		// Errors leegmaken
		clearAllErrors();
	}

	@FXML
	public void addCustomer(ActionEvent event) {
		if (btnCancel.isDisabled()) {
			maakDetailschermLeeg();
			btnSaveChanges.setDisable(true);
			btnCancel.setDisable(true);
			tableBeheerKlanten.getSelectionModel().clearSelection();
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
				tableBeheerKlanten.getSelectionModel().clearSelection();
			}
		}
	}

	@FXML
	public void saveChanges(ActionEvent event) {
		try {
			// Controles
			Controles.stringMinimum(txfUsername.getText(), 6, "Username must be 6 charcters long");
			Controles.stringContainsLetters(txfCompanyName.getText(), "Company name must contain letters");
			Controles.stringNotEmpty(txfPostCode.getText(), "Postal code may not be empty");
			Controles.stringCity(txfTown.getText(), "Town must be valid");
			Controles.stringNotEmpty(txfStreetName.getText(), "Street name may not be empty");
			Controles.stringNotEmpty(txfHouseNr.getText(), "House number may not be empty");

			if (cboCountry.getValue() == null || telefoonnummers.isEmpty() || contactpersonen.isEmpty()) {
				throw new Exception("cboCountry telefoonnummers contactpersonen zijn leeg");
			}

			// Nieuwe klant
			if (txfcustomerNumber.getText().isEmpty()) {
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

				Klant klant = new Klant(txfUsername.getText(), new ArrayList<String>(telefoonnummers), adres,
						txfCompanyName.getText(), new ArrayList<Contactpersoon>(contactpersonen));
				dc.voegKlantToe(klant);
				txfcustomerNumber.setText(Integer.toString(klant.getId()));
				txfCustomerSince.setText(klant.getDatumKlantGeworden().toString());
				btnSaveChanges.setDisable(true);

				tableBeheerKlanten.setItems(dc.getKlantList());
				setConfirmationMessage(lblErrorSaveChanges, "New customer has been saved succesfully");
			}
			// Bestaande klant
			else {
				geselecteerdeKlant.setGebruikersnaam(txfUsername.getText());
				geselecteerdeKlant.setTelefoonnummers(telefoonnummers);
				geselecteerdeKlant.setBedrijfsNaam(txfCompanyName.getText());
				if (initieleGebruikersStatus == GebruikersStatus.Blocked
						&& cboStatus.getValue() != GebruikersStatus.Blocked) {
					dc.deblokkeerKlant(txfUsername.getText());
				}
				geselecteerdeKlant.setStatus(cboStatus.getValue());
				geselecteerdeKlant.getAdres().setLand(geefGeselecteerdLand());
				geselecteerdeKlant.getAdres().setWoonplaats(txfTown.getText());
				geselecteerdeKlant.getAdres().setPostcode(txfPostCode.getText());
				geselecteerdeKlant.getAdres().setHuisnummer(Integer.parseInt(txfHouseNr.getText()));
				if (txfBusNr.getText() == null || txfBusNr.getText().isEmpty()) {
					geselecteerdeKlant.getAdres().setBusnr(null);
				} else {
					geselecteerdeKlant.getAdres().setBusnr(txfBusNr.getText());
				}
				geselecteerdeKlant.setContactpersonen(contactpersonen);
				dc.pasKlantAan(geselecteerdeKlant);
				btnSaveChanges.setDisable(true);
				tableBeheerKlanten.setItems(dc.getKlantList());
				setConfirmationMessage(lblErrorSaveChanges, "Changes have been saved succesfully");
				tableBeheerKlanten.refresh();
			}
			btnSaveChanges.setDisable(true);
			btnCancel.setDisable(true);
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(lblErrorSaveChanges,
					"The form must be filled in correctly, there must be at least one telephone number and one contact");
		}
	}

	@FXML
	public void cancelChanges(ActionEvent event) {
		if (!txfcustomerNumber.getText().isEmpty()) {
			vulDetailschermIn(tableBeheerKlanten.getSelectionModel().getSelectedItem());
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
	// Mag enkel opgeroepen worden als er geldigige contactinformatie ingevoerd is
	public void addContact(ActionEvent event) {
		contactpersonen.add(new Contactpersoon(txfNewContactEmail.getText(), txfNewContactName.getText(),
				txfNewContactFirstName.getText()));
		txfNewContactEmail.setText("");
		txfNewContactFirstName.setText("");
		txfNewContactName.setText("");
		btnAddContact.setDisable(true);

		if (contactpersonen.size() > Constanten.MIN_AANTAL_CONTACTPERSONEN) {
			btnRemoveContact.setDisable(false);
		}

		enableSave();
		enableCancel();
	}

	@FXML
	// Kan operoepen worden als er nog geen geldigige contactinformatie ingevoerd is
	public void removeContact(ActionEvent event) {
		Contactpersoon geselecteerdeContactpersoon = contactsTable.getSelectionModel().getSelectedItem();
		if (geselecteerdeContactpersoon != null) {
			contactpersonen.remove(geselecteerdeContactpersoon);
			contactsTable.getSelectionModel().clearSelection();

			if (contactpersonen.size() <= Constanten.MIN_AANTAL_CONTACTPERSONEN) {
				btnRemoveContact.setDisable(true);
			}

			enableCancel();
			enableSave();
		}
	}

	@FXML
	public void contactNaamEditInCell(CellEditEvent<Contactpersoon, String> event) {
		try {
			event.getRowValue().setNaam(event.getNewValue());
			clearErrorMessage(lblErrorContactName);
		} catch (IllegalArgumentException e) {
			setErrorMessage(lblErrorContactName, e.getMessage());
		}
		enableSave();
		enableCancel();
	}

	@FXML
	public void contactVoornaamEditInCell(CellEditEvent<Contactpersoon, String> event) {
		try {
			event.getRowValue().setVoornaam(event.getNewValue());
			clearErrorMessage(lblErrorContactFirstName);
		} catch (IllegalArgumentException e) {
			setErrorMessage(lblErrorContactFirstName, e.getMessage());
		}
		enableSave();
		enableCancel();
	}

	@FXML
	public void contactEmailEditInCell(CellEditEvent<Contactpersoon, String> event) {
		try {
			event.getRowValue().setEmail(event.getNewValue());
			clearErrorMessage(lblErrorContactEmail);
		} catch (IllegalArgumentException e) {
			setErrorMessage(lblErrorContactEmail, e.getMessage());
		}
		enableSave();
		enableCancel();
	}

	@FXML
	public void showFilters(ActionEvent event) {
		anchorFilters.setVisible(true);
	}

	@FXML
	public void filterOnAction(Event event) {
		int customerNr = -1;
		if (!txfFilterCustomerNr.getText().isEmpty()) {
			customerNr = Integer.parseInt(txfFilterCustomerNr.getText());
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

		dc.pasFilterAan(customerNr, txfFilterUsername.getText(), txfFilterCompanyName.getText(), statusen);
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
			Controles.stringContainsLetters(txfCompanyName.getText(), "Company name must contain letters");
			Controles.stringNotEmpty(txfPostCode.getText(), "Postal code may not be empty");
			Controles.stringCity(txfTown.getText(), "Town must be valid");
			Controles.stringNotEmpty(txfStreetName.getText(), "Street name may not be empty");
			Controles.stringNotEmpty(txfHouseNr.getText(), "House number may not be empty");

			if (cboCountry.getValue() == null || telefoonnummers.isEmpty() || contactpersonen.isEmpty()) {
				throw new Exception("lijsten leeg");
			}

			btnSaveChanges.setDisable(false);
		} catch (Exception e) {
			btnSaveChanges.setDisable(true);
		}
	}

	private void checkAddContactButton() {
		try {
			Controles.stringOnlyLetters(txfNewContactName.getText(), "Name may only contain letters");
			Controles.stringOnlyLetters(txfNewContactFirstName.getText(), "First name may only contain letters");
			Controles.stringEmail(txfNewContactEmail.getText(), "Email must be in a valid format");
			btnAddContact.setDisable(false);
		} catch (Exception e) {
			btnAddContact.setDisable(true);
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
		clearError(lblErrorCompanyName, txfCompanyName);
		clearError(lblErrorCountry, cboCountry);
		clearError(lblErrorPostalCode, txfPostCode);
		clearError(lblErrorTown, txfTown);
		clearError(lblErrorStreet, txfStreetName);
		clearError(lblErrorHouseNr, txfHouseNr);
		clearError(lblErrorBoxNr, txfBusNr);

		clearError(lblErrorTelephone, txfTelephoneNumber);

		clearError(lblErrorContactName, txfNewContactName);
		clearError(lblErrorContactFirstName, txfNewContactFirstName);
		clearError(lblErrorContactEmail, txfNewContactEmail);

		clearErrorMessage(lblErrorAddCustomer);
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
