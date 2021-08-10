package gui.general;

import static util.FxUtil.clearError;
import static util.FxUtil.clearErrorMessage;
import static util.FxUtil.inHierarchy;
import static util.FxUtil.setConfirmationMessage;
import static util.FxUtil.setError;
import static util.FxUtil.setErrorMessage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import domain.Comment;
import domain.Contactpersoon;
import domain.Ticket;
import domain.Werknemer;
import domain.controllers.BeheerTicketsController;
import domain.enumerations.Dienst;
import domain.enumerations.TicketStatus;
import domain.enumerations.TicketUrgency;
import domain.enumerations.WerknemersType;
import gui.Opslaanbaar;
import gui.enumerations.Link;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import util.Controles;
import util.StringFilters;

public class BeheerTickets extends StackPane implements Opslaanbaar {
	@FXML
	private Label lblErrorType;
	@FXML
	private Label lblErrorDivision;
	@FXML
	private Label lblErrorComments;
	@FXML
	private Label lblErrorAddTicket;
	@FXML
	private Label lblErrorSaveChanges;
	@FXML
	private Label lblErrorTicketTitle;
	@FXML
	private Label lblErrorDescription;
	@FXML
	private Label lblErrorTechnician;
	@FXML
	private Label lblErrorAttachments;
	@FXML
	private Label lblType;
	@FXML
	private Label lblStatus;
	@FXML
	private Label lblComments;
	@FXML
	private Label lblContracts;
	@FXML
	private Label lblTicketTitle;
	@FXML
	private Label lblDescription;
	@FXML
	private Label lblAttachments;
	@FXML
	private Label lblCompanyName;
	@FXML
	private Label lblTicketNumber;
	@FXML
	private Label lblCreationDate;
	@FXML
	private Label lblManageTickets;
	@FXML
	private Label lblClientContacts;
	@FXML
	private Label lblTelephoneNumbers;
	@FXML
	private Label lblTicketInformation;
	@FXML
	private Label lblAssignedTechnician;
	@FXML
	private TextField txfFilterTickerNr;
	@FXML
	private TextField txfFilterTitle;
	@FXML
	private TextField txfTicketTitle;
	@FXML
	private TextField txfTicketNumber;
	@FXML
	private TextField txfCreationDate;
	@FXML
	private TextField txfAssignedTechnician;
	@FXML
	private TextField txfCompanyName;
	@FXML
	private TextField txfAttachments;
	@FXML
	private TextArea txaDescription;
	@FXML
	private TextArea txaAddComment;
	@FXML
	private Button btnAddTicket;
	@FXML
	private Button btnShowFilters;
	@FXML
	private Button btnSaveChanges;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnRemoveComment;
	@FXML
	private Button btnAddComment;
	@FXML
	private Button btnRemoveAttachment;
	@FXML
	private Button btnAddAttachment;
	@FXML
	private CheckBox chkFilterCreated;
	@FXML
	private CheckBox chkFilterPending;
	@FXML
	private CheckBox chkFilterFinished;
	@FXML
	private CheckBox chkFilterCanceled;
	@FXML
	private CheckBox chkFilterAwaitingCustomerInformation;
	@FXML
	private CheckBox chkFilterReceivedCustomerInformation;
	@FXML
	private CheckBox chkFilterInDevelopment;
	@FXML
	private CheckBox chkProductionImpacted;
	@FXML
	private CheckBox chkProductionWillBeImpacted;
	@FXML
	private CheckBox chkNoProductionImpact;
	@FXML
	private ComboBox<TicketUrgency> cboType;
	@FXML
	private ComboBox<TicketStatus> cboStatus;
	@FXML
	private ComboBox<Dienst> cboDivision;
	@FXML
	private TableView<Comment> tableComments;
	@FXML
	private TableColumn<Comment, String> colTime;
	@FXML
	private TableColumn<Comment, String> colComment;
	@FXML
	private TableColumn<Comment, String> colBy;
	@FXML
	private ListView<String> listTelephoneNumbers;
	@FXML
	private ListView<String> listAttachments;
	@FXML
	private TableView<Ticket> tableBeheerTicket;
	@FXML
	private TableColumn<Ticket, Number> colNumber;
	@FXML
	private TableColumn<Ticket, String> colTitle;
	@FXML
	private TableColumn<Ticket, String> colStatus;
	@FXML
	private TableColumn<Ticket, String> colUrgency;
	@FXML
	private TableView<Contactpersoon> tableContactpersonen;
	@FXML
	private TableColumn<Contactpersoon, String> colContactNaam;
	@FXML
	private TableColumn<Contactpersoon, String> colContactVoornaam;
	@FXML
	private TableColumn<Contactpersoon, String> colContactEmail;
	@FXML
	private AnchorPane anchorFilters;

	private ObservableList<String> attachements;
	private ObservableList<Comment> comments;
	private Ticket geselecteerdTicket;
	private GebruikerScherm scherm;
	private BeheerTicketsController dc;
	private Link geklikteLink;

	public BeheerTickets(GebruikerScherm scherm, Link geklikteLink) {
		this.scherm = scherm;
		this.geklikteLink = geklikteLink;
		this.dc = new BeheerTicketsController(scherm.getAangemeldeWerknemer());

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
		buildGui();
	}

	private void addGrowHorizontal() {
		this.setPrefWidth(scherm.getWidth() - 300);
		scherm.widthProperty().addListener(event -> {
			this.setPrefWidth(scherm.getWidth() - 300);
		});
	}

	private void buildGui() {
		// Bepalen welke knoppen zichtbaar zijn afhankelijk van het soort werknemer en
		// het gekozen scherm
		if (scherm.getAangemeldeWerknemer().getWerknemersType() != WerknemersType.SupportManager
				|| geklikteLink != Link.OpenTickets) {
			btnAddTicket.setVisible(false);
		} else {
			btnAddTicket.setVisible(true);
		}

		if (geklikteLink != Link.OpenTickets) {
			btnSaveChanges.setVisible(false);
			btnCancel.setVisible(false);
		} else {
			btnSaveChanges.setVisible(true);
			btnCancel.setVisible(true);
		}

		// Label aanpassen bovenaan scherm adhv gekozen scherm
		if (geklikteLink == Link.OpenTickets) {
			lblManageTickets.setText("Open Tickets");
		}
		if (geklikteLink == Link.GeslotenTickets) {
			lblManageTickets.setText("Closed Tickets");
		}
		if (geklikteLink == Link.KnowledgeDatabase) {
			lblManageTickets.setText("Knowledge Database");
		}

		// Filter aanpassen afhankelijk van soort scherm
		if (geklikteLink == Link.OpenTickets) {
			chkFilterCreated.setSelected(true);
			chkFilterPending.setSelected(true);
			chkFilterInDevelopment.setSelected(true);
			chkFilterAwaitingCustomerInformation.setSelected(true);
			chkFilterReceivedCustomerInformation.setSelected(true);
			chkFilterFinished.setSelected(false);
			chkFilterCanceled.setSelected(false);
			chkProductionImpacted.setSelected(false);
			chkProductionWillBeImpacted.setSelected(false);
			chkNoProductionImpact.setSelected(false);
		}
		if (geklikteLink == Link.GeslotenTickets) {
			chkFilterFinished.setSelected(true);
			chkFilterCanceled.setSelected(true);
			chkProductionImpacted.setSelected(false);
			chkProductionWillBeImpacted.setSelected(false);
			chkNoProductionImpact.setSelected(false);
			chkFilterCreated.setSelected(false);
			chkFilterPending.setSelected(false);
			chkFilterInDevelopment.setSelected(false);
			chkFilterAwaitingCustomerInformation.setSelected(false);
			chkFilterReceivedCustomerInformation.setSelected(false);
		}
		// tableView ticket
		colNumber.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
		colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitel()));
		colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
		colUrgency.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));

		SortedList<Ticket> s = new SortedList<>(dc.getTicketsList());
		if (geklikteLink == Link.OpenTickets) {
			dc.pasFilterAan(-1, null, TicketStatus.openstaandeTickets(), null);
		} else {
			dc.pasFilterAan(-1, null, TicketStatus.afgehandeldeTickets(), null);
		}
		s.comparatorProperty().bind(tableBeheerTicket.comparatorProperty());
		tableBeheerTicket.setItems(s);

		// tableView contactPersoon
		colContactNaam.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaam()));
		colContactVoornaam.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVoornaam()));
		colContactEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

		// tableView comments
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		colTime.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getTijdstip().format(formatter)));
		colComment.setCellFactory(new Callback<TableColumn<Comment, String>, TableCell<Comment, String>>() {
			@Override
			public TableCell<Comment, String> call(TableColumn<Comment, String> param) {
				final TableCell<Comment, String> cell = new TableCell<Comment, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setGraphic(null);
						setText(null);
						if (!isEmpty()) {
							Text text = new Text(item.toString());
							text.wrappingWidthProperty().bind(colComment.widthProperty());
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		});

		colComment.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOpmerking()));
		colBy.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getPersoonDieOpmerkingToevoegt()));

		// Comboboxen vullen
		cboStatus.getItems().addAll(TicketStatus.values());
		cboType.getItems().addAll(TicketUrgency.values());
		cboDivision.getItems().addAll(Dienst.values());

		// Inputfilters
		txfTicketNumber.textProperty().addListener((observable, oldValue, newValue) -> {
			txfTicketNumber.setText(StringFilters.numeric(newValue));
		});

		// Controles
		addTextfieldControle(txfTicketTitle, lblErrorTicketTitle,
				e -> Controles.stringNotEmpty(e, "Title may not be empty"));
		addTextfieldControle(txfAttachments, lblErrorAttachments,
				e -> Controles.stringNotEmpty(e, "Attachments may not be empty"));
		addTextareaControle(txaAddComment, lblErrorComments,
				e -> Controles.stringNotEmpty(e, "Commments may not be empty"));
		addTextareaControle(txaDescription, lblErrorDescription,
				e -> Controles.stringNotEmpty(e, "Description may not be empty"));
		addTextfieldControle(txfAssignedTechnician, lblErrorTechnician,
				e -> Controles.stringNotEmpty(e, "Technician may not be empty"));

		// Listeners
		txfAttachments.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue.isEmpty()) {
					Controles.stringNotEmpty(newValue, "Attachments may not be empty");
					btnAddAttachment.setDisable(false);
				} else {
					btnAddAttachment.setDisable(true);
				}
				clearError(lblErrorAttachments, txfAttachments);
			} catch (IllegalArgumentException e) {
				setError(lblErrorAttachments, e.getMessage(), txfAttachments);
				btnAddAttachment.setDisable(true);
			}
			enableCancel();
		});

		txaAddComment.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue.isEmpty()) {
					Controles.stringNotEmpty(newValue, "Comment may not be empty");
					btnAddComment.setDisable(false);
				} else {
					btnAddComment.setDisable(true);
				}
				clearError(lblErrorComments, txaAddComment);
			} catch (IllegalArgumentException e) {
				setError(lblErrorComments, e.getMessage(), txaAddComment);
				btnAddComment.setDisable(true);
			}
			enableCancel();
		});

		// On mouse clicked
		cboType.setOnMouseClicked(e -> {
			if (cboType.getValue() == null) {
				setError(lblErrorType, "A type must be selected", cboType);
			}
		});

		cboDivision.setOnMouseClicked(e -> {
			if (cboDivision.getValue() == null) {
				setError(lblErrorDivision, "A division must be selected", cboDivision);
			}
		});

		// On action
		cboType.setOnAction(e -> {
			clearError(lblErrorType, cboType);
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

		// Scherm invullen als een ticket word geselecteerd
		tableBeheerTicket.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, oldticket, newticket) -> {
					if (newticket != null) {
						if (scherm.getAangemeldeWerknemer().getWerknemersType() == WerknemersType.Technician) {
							newticket.setBekekenDoorTechnieker(true);
							dc.pasTicketAan(newticket);
							scherm.updateAantalGewijzigdeTickets();
						}
						if (btnSaveChanges.isDisable()) {
							geselecteerdTicket = newticket;
							vulDetailschermIn(newticket);
						} else {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("Unsaved changes");
							alert.setHeaderText("You have unsaved changes");
							alert.setContentText("Do you wish to discard your changes?");

							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == ButtonType.OK) {
								if (!txfTicketNumber.getText().isEmpty()) {
									vulDetailschermIn(tableBeheerTicket.getSelectionModel().getSelectedItem());
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

	private void vulDetailschermIn(Ticket ticket) {
		Werknemer toegewezenT = ticket.getToegewezenTechnieker();

		// Textfields
		txfTicketNumber.setText(Integer.toString(ticket.getId()));
		txfCreationDate.setText(ticket.getDatumAanmaak().toString().substring(0, 10));
		txfTicketTitle.setText(ticket.getTitel());
		txaDescription.setText(ticket.getOmschrijving());

		if (toegewezenT != null) {
			txfAssignedTechnician.setText(toegewezenT.getGebruikersnaam());
		}

		// Cbo
		cboStatus.setValue(ticket.getStatus());
		cboType.setValue(ticket.getType());
		cboDivision.setValue(ticket.getDienst());

		// Klant
		tableContactpersonen
				.setItems(FXCollections.observableArrayList(ticket.getContract().getKlant().getContactpersonen()));
		listTelephoneNumbers
				.setItems(FXCollections.observableArrayList(ticket.getContract().getKlant().getTelefoonnummers()));
		txfCompanyName.setText(ticket.getContract().getKlant().getBedrijfsNaam());

		// Comments
		comments = FXCollections.observableArrayList(ticket.getOpmerkingen());
		tableComments.setItems(comments);
		txaAddComment.setText("");

		// Attachments
		attachements = FXCollections.observableArrayList(ticket.getBijlages());
		listAttachments.setItems(attachements);
		txfAttachments.setText("");

		// Disable fields & buttons
		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);

		if (scherm.getAangemeldeWerknemer().getWerknemersType() == WerknemersType.SupportManager
				&& geklikteLink == Link.OpenTickets) {
			// Supportmanager & Open
			// NIET disablen: status, attachments, commments, divison, technieker
			cboStatus.setDisable(false); // NIET
			cboDivision.setDisable(false); // NIET
			cboType.setDisable(true);

			txfTicketNumber.setDisable(true);
			txfCreationDate.setDisable(true);
			txfAssignedTechnician.setDisable(false); // NIET
			txfAttachments.setDisable(false); // NIET
			txfCompanyName.setDisable(true);
			txfFilterTickerNr.setDisable(true);
			txfFilterTitle.setDisable(true);
			txfTicketTitle.setDisable(true);

			txaAddComment.setDisable(false); // NIET
			txaDescription.setDisable(false);
			tableContactpersonen.setDisable(true);

			listAttachments.setDisable(false); // NIET
			tableComments.setDisable(false); // NIET
			listTelephoneNumbers.setDisable(true);
			if (attachements.size() > 1) {
				btnRemoveAttachment.setDisable(false);
			} else {
				btnRemoveAttachment.setDisable(true);
			}
			if (comments.size() > 1) {
				btnRemoveComment.setDisable(false);
			} else {
				btnRemoveComment.setDisable(true);
			}
		}
		if (scherm.getAangemeldeWerknemer().getWerknemersType() == WerknemersType.Technician
				&& geklikteLink == Link.OpenTickets) {
			// Technieker & Open
			// NIET disablen: status, attachments, commments
			cboStatus.setDisable(false); // NIET
			cboDivision.setDisable(true);
			cboType.setDisable(true);

			txfTicketNumber.setDisable(true);
			txfAssignedTechnician.setDisable(true);
			txfAttachments.setDisable(false); // NIET
			txfCompanyName.setDisable(true);
			txfTicketTitle.setDisable(true);

			txaAddComment.setDisable(false); // NIET
			txaDescription.setDisable(false);
			tableContactpersonen.setDisable(true);

			listAttachments.setDisable(false); // NIET
			tableComments.setDisable(false); // NIET
			listTelephoneNumbers.setDisable(true);
			if (attachements.size() > 1) {
				btnRemoveAttachment.setDisable(false);
			} else {
				btnRemoveAttachment.setDisable(true);
			}
			if (comments.size() > 1) {
				btnRemoveComment.setDisable(false);
			} else {
				btnRemoveComment.setDisable(true);
			}
		}
		if (scherm.getAangemeldeWerknemer().getWerknemersType() == WerknemersType.Technician
				&& geklikteLink == Link.GeslotenTickets) {
			// Supportmanager & Technieker & Finished
			// Alles disabled
			cboStatus.setDisable(true);
			cboDivision.setDisable(true);
			cboType.setDisable(true);

			txfTicketNumber.setDisable(true);
			txfCreationDate.setDisable(true);
			txfAssignedTechnician.setDisable(true);
			txfAttachments.setDisable(true);
			txfCompanyName.setDisable(true);
			txfTicketTitle.setDisable(true);

			txaAddComment.setDisable(true);
			txaDescription.setDisable(false);
			tableContactpersonen.setDisable(true);

			listAttachments.setDisable(false);
			tableComments.setDisable(false);
			listTelephoneNumbers.setDisable(true);

			btnRemoveAttachment.setDisable(true);
			btnRemoveComment.setDisable(true);
		}

		// Errors leegmaken
		clearAllErrors();
	}

	private void maakDetailschermLeeg() {
		// Textfields & areas
		txfTicketNumber.setText("");
		txfCreationDate.setText("");
		txfTicketTitle.setText("");
		txaDescription.setText("");
		txfAssignedTechnician.setText("");

		// Cbo
		cboStatus.setValue(TicketStatus.Created);
		cboType.setValue(null);
		cboDivision.setValue(null);

		// Comments
		comments = FXCollections.observableArrayList(new ArrayList<Comment>());
		tableComments.setItems(comments);
		txaAddComment.setText("");

		// Attachments
		attachements = FXCollections.observableArrayList(new ArrayList<String>());
		listAttachments.setItems(attachements);
		txfAssignedTechnician.setText("");

		// Klant
		tableContactpersonen.setItems(FXCollections.observableArrayList(new ArrayList<Contactpersoon>()));
		listTelephoneNumbers.setItems(FXCollections.observableArrayList(new ArrayList<String>()));
		txfCompanyName.setText("");

		// Disable buttons & cbo
		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);
		btnAddAttachment.setDisable(true);
		btnRemoveAttachment.setDisable(true);
		btnAddComment.setDisable(true);
		btnRemoveComment.setDisable(true);

		cboStatus.setDisable(true);
		cboDivision.setDisable(true);
		cboType.setDisable(true);

		txfTicketNumber.setDisable(true);
		txfCreationDate.setDisable(true);
		txfAssignedTechnician.setDisable(true);
		txfAttachments.setDisable(true);
		txfCompanyName.setDisable(true);
		txfTicketTitle.setDisable(true);

		txaAddComment.setDisable(true);
		txaDescription.setDisable(true);
		tableContactpersonen.setDisable(true);

		listAttachments.setDisable(false);
		tableComments.setDisable(false);
		listTelephoneNumbers.setDisable(false);

		// Errors leegmaken
		clearAllErrors();
	}

	@FXML
	public void addTicket(ActionEvent event) {
		if (btnCancel.isDisabled()) {
			maakDetailschermLeeg();
			btnSaveChanges.setDisable(true);
			btnCancel.setDisable(true);
			tableBeheerTicket.getSelectionModel().clearSelection();
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
				tableBeheerTicket.getSelectionModel().clearSelection();
			}
		}
	}

	@FXML
	public void saveChanges(ActionEvent event) {

		try {
			// Controles
			Controles.stringNotEmpty(txfTicketTitle.getText(), "Ticket title may not be empty");
			Controles.stringNotEmpty(txaDescription.getText(), "Ticket description may not be empty");

			if (cboType.getValue() == null) {
				throw new IllegalArgumentException("Ticket type may not be empty");
			}
			if (cboDivision.getValue() == null) {
				throw new IllegalArgumentException("Division may not be empty");
			}

			// Nieuw Ticket
			if (txfTicketNumber.getText().isEmpty()) {
				/*
				 * Klant klant = new Klant(); Ticket ticket = new
				 * Ticket(txfTicketTitle.getText(), cboType.getValue(),
				 * txaDescription.getText(), klant); dc.voegTicketToe(ticket);
				 */
				btnSaveChanges.setDisable(true);
				tableBeheerTicket.setItems(dc.getTicketsList());
				setConfirmationMessage(lblErrorSaveChanges, "Ticket has been saved succesfully");
			}
			// Bestaand Ticket
			else {

				geselecteerdTicket.setOmschrijving(txaDescription.getText());
				geselecteerdTicket.setTitel(txfTicketTitle.getText());
				geselecteerdTicket.setOpmerkingen(comments);
				geselecteerdTicket.setBijlages(attachements);
				geselecteerdTicket.setStatus(cboStatus.getValue());
				geselecteerdTicket.setType(cboType.getValue());
				geselecteerdTicket.setDienst(cboDivision.getValue());

				dc.pasTicketAan(geselecteerdTicket);
				btnSaveChanges.setDisable(true);
				tableBeheerTicket.setItems(dc.getTicketsList());
				setConfirmationMessage(lblErrorSaveChanges, "Changes have been saved succesfully");
				tableBeheerTicket.refresh();
			}
			btnSaveChanges.setDisable(true);
			btnCancel.setDisable(true);
		} catch (

		Exception ex) {
			setErrorMessage(lblErrorSaveChanges, "The form must be filled in correctly");
		}
	}

	@FXML
	public void cancelChanges(ActionEvent event) {
		if (!txfTicketNumber.getText().isEmpty()) {
			vulDetailschermIn(tableBeheerTicket.getSelectionModel().getSelectedItem());
		} else {
			maakDetailschermLeeg();
		}

		btnSaveChanges.setDisable(true);
		btnCancel.setDisable(true);
	}

	@FXML
	public void addComment(ActionEvent event) {
		if (lblErrorComments.getText().isEmpty()) {
			comments.add(new Comment(txaAddComment.getText(), scherm.getAangemeldeWerknemer().getGebruikersnaam(),
					geselecteerdTicket));
			txaAddComment.setText("");
			btnAddComment.setDisable(true);
		}

		if (comments.size() > 0) {
			btnRemoveComment.setDisable(false);
		}

		enableCancel();
		enableSave();
	}

	@FXML
	public void removeComment(ActionEvent event) {
		Comment geselecteerdeComment = tableComments.getSelectionModel().getSelectedItem();
		if (geselecteerdeComment != null) {
			comments.remove(geselecteerdeComment);
			tableComments.getSelectionModel().clearSelection();

			if (comments.size() <= 0) {
				btnRemoveComment.setDisable(true);
			} else {
				lblErrorComments.setText("Please select the comment you want to remove first");
			}

			enableCancel();
			enableSave();
		}
	}

	@FXML
	public void addAttachment(ActionEvent event) {
		if (lblErrorAttachments.getText().isEmpty()) {
			attachements.add(txfAttachments.getText());
			txfAttachments.setText("");
			btnAddAttachment.setDisable(true);
		}

		if (attachements.size() > 0) {
			btnRemoveAttachment.setDisable(false);
		}

		enableSave();
		enableCancel();
	}

	@FXML
	public void removeAttachment(ActionEvent event) {
		String geselecteerdeBijlage = listAttachments.getSelectionModel().getSelectedItem();
		if (geselecteerdeBijlage != null) {
			attachements.remove(geselecteerdeBijlage);
			listAttachments.getSelectionModel().clearSelection();

			if (attachements.size() <= 0) {
				btnRemoveAttachment.setDisable(true);
			} else {
				lblErrorAttachments.setText("Please select the attachment you want to remove first");
			}

			enableSave();
			enableCancel();
		}
	}

	@FXML
	public void showFilters(ActionEvent event) {
		anchorFilters.setVisible(isVisible());
	}

	@FXML
	public void filterOnAction(ActionEvent event) {
		int ticketNr = -1;
		if (!txfFilterTickerNr.getText().isEmpty()) {
			ticketNr = Integer.parseInt(txfFilterTickerNr.getText());
		}

		Set<TicketStatus> statusen = new HashSet<>();
		if (chkFilterCreated.isSelected()) {
			statusen.add(TicketStatus.Created);
		}
		if (chkFilterPending.isSelected()) {
			statusen.add(TicketStatus.Pending);
		}
		if (chkFilterFinished.isSelected()) {
			statusen.add(TicketStatus.Finished);
		}
		if (chkFilterCanceled.isSelected()) {
			statusen.add(TicketStatus.Cancelled);
		}
		if (chkFilterAwaitingCustomerInformation.isSelected()) {
			statusen.add(TicketStatus.AwaitingCustomerInformation);
		}
		if (chkFilterReceivedCustomerInformation.isSelected()) {
			statusen.add(TicketStatus.ReceivedCustomerInformation);
		}
		if (chkFilterInDevelopment.isSelected()) {
			statusen.add(TicketStatus.InDevelopment);
		}
		if (statusen.isEmpty()) {
			statusen = TicketStatus.valueSet();
		}

		Set<TicketUrgency> typen = new HashSet<>();
		if (chkProductionImpacted.isSelected()) {
			typen.add(TicketUrgency.ProductionImpacted);
		}
		if (chkProductionWillBeImpacted.isSelected()) {
			typen.add(TicketUrgency.ProductionWillBeImpacted);
		}
		if (chkNoProductionImpact.isSelected()) {
			typen.add(TicketUrgency.NoProductionImpact);
		}
		if (typen.isEmpty()) {
			Arrays.stream(TicketUrgency.values()).forEach(v -> typen.add(v));
		}

		dc.pasFilterAan(ticketNr, txfFilterTitle.getText(), statusen, typen);
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

	private void addTextareaControle(TextArea textarea, Label label, Consumer<String> controle) {
		textarea.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				controle.accept(newValue);
				clearError(label, textarea);
			} catch (IllegalArgumentException e) {
				setError(label, e.getMessage(), textarea);
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
			Controles.stringNotEmpty(txfTicketTitle.getText(), "Ticket title may not be empty");
			Controles.stringNotEmpty(txaDescription.getText(), "Ticket description may not be empty");

			if (cboType.getValue() == null) {
				throw new IllegalArgumentException("Ticket type may not be empty");
			}
			if (cboDivision.getValue() == null) {
				throw new IllegalArgumentException("Division may not be empty");
			}

			btnSaveChanges.setDisable(false);
		} catch (Exception ex) {
			btnSaveChanges.setDisable(true);
		}
	}

	private void clearAllErrors() {
		clearError(lblErrorTicketTitle, txfTicketTitle);
		clearError(lblErrorDescription, txaDescription);
		clearError(lblErrorAttachments, txfAttachments);
		clearError(lblErrorComments, txaAddComment);
		clearError(lblErrorDivision, cboDivision);
		clearError(lblErrorType, cboType);

		clearErrorMessage(lblErrorAddTicket);
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
