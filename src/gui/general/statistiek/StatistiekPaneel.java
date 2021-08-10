package gui.general.statistiek;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import domain.enumerations.StatistiekFilter;
import domain.statistieken.Statistiek;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class StatistiekPaneel extends AnchorPane {
	@FXML
	private Button btnShowGraph;
	@FXML
	private Button btnShowTable;
	@FXML
	private AnchorPane anchorStartingDate;
	@FXML
	private AnchorPane anchorEndDate;
	@FXML
	private AnchorPane anchorNames;
	@FXML
	private TableView<Entry<String, Double>> tableFilters;
	@FXML
	private TableColumn<Entry<String, Double>, CheckBox> tableColumnChecks;
	@FXML
	private TableColumn<Entry<String, Double>, String> tableColumnName;

	private Statistiek<?> statistiek;
	private StatistiekenBekijken scherm;
	private Node visualisation;
	private Button selectedButton;
	private HashSet<String> unselectedNameFilters;

	public StatistiekPaneel(StatistiekenBekijken scherm, Statistiek<?> statistiek) {
		this.scherm = scherm;
		this.statistiek = statistiek;

		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			System.out.println("Probleem met tonen " + this.getClass().getSimpleName());
			ex.printStackTrace();
		}

		bindMangedToVisbleProperties();
		buildGui();
		addGrowHorizontal();

		selectButton(btnShowGraph);
		addGraph();
	}

	private void buildGui() {
		Set<StatistiekFilter> filters = statistiek.getType().getStatistiekFilters();

		if (!filters.contains(StatistiekFilter.StartDatum)) {
			anchorStartingDate.setVisible(false);
		}

		if (!filters.contains(StatistiekFilter.EindDatum)) {
			anchorEndDate.setVisible(false);
		}

		if (!filters.contains(StatistiekFilter.Naam)) {
			anchorNames.setVisible(false);
		} else {
			unselectedNameFilters = new HashSet<>();

			tableColumnChecks.setCellValueFactory(c -> {
				String key = c.getValue().getKey();
				CheckBox checkBox = new CheckBox();
				checkBox.selectedProperty().setValue(true);
				checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
					changeSelectedName(key, newValue);
					updateVisualisation();
				});
				return new SimpleObjectProperty<CheckBox>(checkBox);
			});

			tableColumnName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
			tableColumnName.setText(statistiek.getType().getYAsNaam());

			tableFilters.getItems().addAll(statistiek.getVerwerkteData().entrySet());
		}
	}

	private void addGrowHorizontal() {
		this.setPrefWidth(scherm.getWidth() - 20);
		scherm.widthProperty().addListener(event -> {
			this.setPrefWidth(scherm.getWidth() - 20);
		});
	}

	private void addGraph() {
		switch (statistiek.getType().getGrafiekType()) {
		case HorizontalBarChart -> addHorizontalBarChart();
		case VerticalBarChart -> addVerticalBarChart();
		case PieChart -> addPieChart();
		default -> throw new IllegalArgumentException("Unexpected value: " + statistiek.getType());
		}
	}

	// -- Visualisations --
	private void addTable() {
		TableView<Entry<String, Double>> table = new TableView<>();

		// autonummering
		TableColumn<Entry<String, Double>, String> numberCol = new TableColumn<>("#");
		numberCol.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(e.getValue()) + ""));
		numberCol.setSortable(false);
		table.getColumns().add(numberCol);

		TableColumn<Entry<String, Double>, String> col1 = new TableColumn<>(statistiek.getType().getYAsNaam());
		col1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
		table.getColumns().add(col1);

		TableColumn<Entry<String, Double>, Number> col2 = new TableColumn<>(statistiek.getType().getXAsNaam());
		col2.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(Math.floor(cellData.getValue().getValue() * 100) / 100));
		table.getColumns().add(col2);

		table.getItems().addAll(statistiek.getVerwerkteData().entrySet());

		AnchorPane.setTopAnchor(table, 60.0);
		AnchorPane.setRightAnchor(table, 275.0);
		AnchorPane.setBottomAnchor(table, 21.0);
		AnchorPane.setLeftAnchor(table, 20.0);
		this.getChildren().add(table);

		this.visualisation = table;
	}

	private void addHorizontalBarChart() {
		NumberAxis xAxis = new NumberAxis();
		CategoryAxis yAxis = new CategoryAxis();
		BarChart<Number, String> chart = new BarChart<>(xAxis, yAxis);

		XYChart.Series<Number, String> series = new XYChart.Series<>();
		statistiek.getVerwerkteData().forEach((key, value) -> {
			series.getData().addAll(new XYChart.Data<>(value, key));
		});

		chart.getData().add(series);

		chart.setTitle(statistiek.getType().getTitel());
		xAxis.setLabel(statistiek.getType().getXAsNaam());
		xAxis.setTickLabelRotation(90);
		yAxis.setLabel(statistiek.getType().getYAsNaam());
		chart.setLegendVisible(false);

		AnchorPane.setTopAnchor(chart, 35.0);
		AnchorPane.setRightAnchor(chart, 275.0);
		AnchorPane.setBottomAnchor(chart, 0.0);
		AnchorPane.setLeftAnchor(chart, 10.0);

		this.getChildren().add(chart);
		this.visualisation = chart;
	}

	private void addVerticalBarChart() {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		statistiek.getVerwerkteData().forEach((key, value) -> {
			series.getData().add(new XYChart.Data<>(key, value));
		});
		chart.getData().add(series);

		chart.setTitle(statistiek.getType().getTitel());
		xAxis.setLabel(statistiek.getType().getYAsNaam());
		yAxis.setLabel(statistiek.getType().getXAsNaam());
		chart.setLegendVisible(false);

		AnchorPane.setTopAnchor(chart, 50.0);
		AnchorPane.setRightAnchor(chart, 275.0);
		AnchorPane.setBottomAnchor(chart, 0.0);
		AnchorPane.setLeftAnchor(chart, 10.0);
		this.getChildren().add(chart);

		this.visualisation = chart;
	}

	private void addPieChart() {
		PieChart chart = new PieChart();
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(new ArrayList<>());
		statistiek.getVerwerkteData().forEach((key, value) -> {
			pieChartData.add(new PieChart.Data(key, value));
		});
		chart.setData(pieChartData);

		chart.setTitle(statistiek.getType().getTitel());
		chart.setLegendVisible(false);

		AnchorPane.setTopAnchor(chart, 50.0);
		AnchorPane.setRightAnchor(chart, 275.0);
		AnchorPane.setBottomAnchor(chart, 0.0);
		AnchorPane.setLeftAnchor(chart, 10.0);
		this.getChildren().add(chart);

		this.visualisation = chart;
	}
	// -- Visualisations --

	// -- Event handlers --
	@FXML
	public void showGraph(ActionEvent event) {
		selectButton(btnShowGraph);
		updateVisualisation();
	}

	@FXML
	public void showTable(ActionEvent event) {
		selectButton(btnShowTable);
		updateVisualisation();
	}

	@FXML
	public void changeStartDate(ActionEvent event) {
		DatePicker date = (DatePicker) event.getSource();
		if (date.getValue() == null) {
			// filter verwijderen uit de map
			statistiek.filterData(StatistiekFilter.EindDatum, null);
		} else {
			statistiek.filterData(StatistiekFilter.StartDatum, date.getValue().atStartOfDay());
		}

		updateVisualisation();
	}

	@FXML
	public void changeEndDate(ActionEvent event) {
		DatePicker date = (DatePicker) event.getSource();
		if (date.getValue() == null) {
			// filter verwijderen uit de map
			statistiek.filterData(StatistiekFilter.EindDatum, null);
		} else {
			statistiek.filterData(StatistiekFilter.EindDatum, date.getValue().atStartOfDay());
		}

		updateVisualisation();
	}

	public void changeSelectedName(String key, boolean selected) {
		if (selected) {
			unselectedNameFilters.remove(key);
		} else {
			unselectedNameFilters.add(key);
		}
		statistiek.filterData(StatistiekFilter.Naam, unselectedNameFilters);
	}
	// -- Event handlers --

	// -- Helper methods --
	private void bindMangedToVisbleProperties() {
		anchorStartingDate.managedProperty().bind(anchorStartingDate.visibleProperty());
		anchorEndDate.managedProperty().bind(anchorEndDate.visibleProperty());
		anchorNames.managedProperty().bind(anchorNames.visibleProperty());
	}

	private void updateVisualisation() {
		this.getChildren().remove(visualisation);
		if (selectedButton.equals(btnShowGraph)) {
			addGraph();
		} else {
			addTable();
		}
	}

	private void selectButton(Button button) {
		if (selectedButton != null) {
			selectedButton.setStyle("");
		}
		button.setStyle("-fx-background-color: #d3d3d3;" + "-fx-text-fill: dodgerblue;");
		selectedButton = button;
	}
	// -- Helper mehtods --
}
