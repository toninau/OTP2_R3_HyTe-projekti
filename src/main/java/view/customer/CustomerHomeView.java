package view.customer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.controlsfx.control.CheckListView;
import org.controlsfx.control.textfield.TextFields;

import controller.CustomerController;
import controller.CustomerControllerIF;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.Appointment;
import model.Notification;
import model.Prescription;
import model.UserImage;
import model.WeatherAPICall;
import view.HyteGUI;
import view.ViewChanger;
import view.enums.Bundles;

/**
 * FXML controller class for customer's home view.
 * 
 * @author IdaKi
 *
 */
public class CustomerHomeView extends ViewChanger implements Initializable {
	@FXML
	private Text messageBox;
	@FXML
	private Label weatherCelsius;
	@FXML
	private TextField locationField;
	@FXML
	private ImageView weatherImageView;
	@FXML
	private Label weatherState;
	@FXML
	private Slider happinessSlider;
	@FXML
	private Label welcome;
	@FXML
	private ListView<String> appointmentReminder;
	@FXML
	private HBox prescriptionBox;
	@FXML
	private ImageView imageMe;
	@FXML
	private ImageView imageSecond;
	@FXML
	private ImageView imageThird;
	@FXML private ImageView locationInputValidation;

	@FXML private ListView<String> messageArea;

	private static final String UPDATE = "update";
	private static final String CREATE = "create";
	private static final Logger LOGGER = Logger.getLogger(CustomerHelpView.class.getName());
	private ResourceBundle bundle;
	private CustomerControllerIF controller;
	private ObservableList<Prescription> prescriptionsList;

	public CustomerHomeView() {
		controller = new CustomerController(this);
	}

	/**
	 * Opens the windows file explorer and sets the chosen image at the chosen image
	 * slot. Sends the image to database.
	 * 
	 * @param imageView The chosen image view.
	 * @param imageSlot This parameter is used to name the images accordingly.
	 * @param action    Decides whether the image is to be created or updated.
	 * @see controller.CustomerController#imageToDatabase(File, int)
	 */
	public void selectImage(ImageView imageView, int imageSlot, String action) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Profile Picture");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*jpg"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			File f = new File(selectedFile.getAbsolutePath());
			String url;
			try {
				url = f.toURI().toURL().toString();
				imageView.setImage(new Image(url));
				if (action.equals(UPDATE)) {
					controller.updateImage(selectedFile, imageSlot);
				} else {
					controller.imageToDatabase(selectedFile, imageSlot);
				}
			} catch (IOException e) {
				LOGGER.warning("Could not open file");
			}
		}
	}

	/**
	 * Event handler for adding and updating customer's profile image, which is the
	 * first image slot.
	 */
	public void selectImageMe() {
		String action;
		if (imageMe.getImage() != null) {
			action = UPDATE;
		} else {
			action = CREATE;
		}
		selectImage(imageMe, 1, action);
	}

	/**
	 * Event handler for the second image slot.
	 */
	public void selectSecondImage() {
		String action;
		if (imageSecond.getImage() != null) {
			action = UPDATE;
		} else {
			action = CREATE;
		}
		selectImage(imageSecond, 2, action);
	}

	/**
	 * Event handler for the third image slot.
	 */
	public void selectThirdImage() {
		String action;
		if (imageThird.getImage() != null) {
			action = UPDATE;
		} else {
			action = CREATE;
		}
		selectImage(imageThird, 3, action);
	}

	/**
	 * Sets all the customer's image from database to their appointed slots
	 * according to the images name.
	 * 
	 * @see controller.CustomerController#imageFromTempFile()
	 */
	public void showImage() {
		UserImage[] a = controller.imageFromTempFile();
		if (a != null) {
			BufferedImage img = null;
			ImageView imagev;
			for (UserImage userImage : a) {
				switch (userImage.getImageName().charAt(userImage.getImageName().length() - 1)) {
				case '1':
					imagev = imageMe;
					break;
				case '2':
					imagev = imageSecond;
					break;
				case '3':
					imagev = imageThird;
					break;
				default:
					imagev = imageMe;
					break;
				}
				try {
					img = ImageIO.read(new ByteArrayInputStream(userImage.getImage()));
					Image image = SwingFXUtils.toFXImage(img, null);
					imagev.setImage(image);
				} catch (IOException e) {
					LOGGER.warning("Could not read file");
				}
			}
		}
	}

	/**
	 * Generates a list view with check boxes and populates the list view.
	 * 
	 * @see #getPrescriptionsList()
	 */
	public void showPrescription() {
		CheckListView<Prescription> checkListView = new CheckListView<>();
		prescriptionsList = getPrescriptionsList();		
		checkListView.getStylesheets().add(getClass().getResource("/css/view.css").toExternalForm());
		checkListView.getStyleClass().add("checkListView");
		prescriptionBox.getChildren().add(checkListView);
		checkListView.setItems(prescriptionsList);

		checkListView.setCellFactory(lv -> {
			CheckBoxListCell<Prescription> cell = new CheckBoxListCell<Prescription>(
					checkListView::getItemBooleanProperty) {
				@Override
				public void updateItem(Prescription p, boolean empty) {
					super.updateItem(p, empty);
					setText(p == null ? ""
							: p.getPrescriptionName() + " " + p.getDosage() + " " + p.getPrescriptionGuide());
				}
			};
			
			cell.getStyleClass().add("checkListCell");
			return cell;
		});
		checkListView.getCheckModel().getCheckedItems().addListener((ListChangeListener<Prescription>) c -> {
			String takenAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm"));

			if(!checkListView.getCheckModel().isEmpty()) {
				Prescription p = checkListView.getCheckModel().getCheckedItems().get(0);

				p.setTakenAt(takenAt);
				controller.updateMedicineTaken(p);
			}
		});
	}

	
	
	/**
	 * Checks the time when the medicine is supposed to be taken and adds them into
	 * an observable list accordingly. e.g. if the medicine is supposed to be taken
	 * in the morning and the current time is before noon, the prescription is added
	 * to the observable list.
	 * 
	 * @return An observable list of the prescriptions.
	 */
	public ObservableList<Prescription> getPrescriptionsList() {
		prescriptionsList = FXCollections.observableArrayList();
		Prescription[] a = controller.prescriptions();
		for (Prescription prescription : a) {

			prescriptionsList.add(prescription);
			
		}
		return prescriptionsList;
	}

	/**
	 * Sets the customer's appointments for today.
	 * 
	 * @see #appointmentList()
	 */
	public void showAppointments() {
		appointmentReminder.getItems().addAll(appointmentList());
	}
	
	public void showMessages() {
		messageArea.setItems(messageList());
		
	}

	/**
	 * Checks the date of the appointments and adds appointments marked with the
	 * current date.
	 * 
	 * @return Observable list of today's appointments
	 */
	public ObservableList<String> appointmentList() {
		boolean isEmpty = false;
		ObservableList<String> data = FXCollections.observableArrayList();
		Appointment[] appointments = controller.customersAppointments();
		
		for (Appointment a : appointments) {
			if (a.getDate().isEqual(LocalDate.now())) {
				data.add(a.toStringCustomer());
				isEmpty = true;
			}
		}
		if (!isEmpty) {
			data.add("No appointments today");
		}
		return data;
	}

	/**
	 * Fired when location is chosen and button is pressed.
	 * 
	 * @param event Mouse clicked.
	 */
	public void updateLocationClicked(Event event) {
		String[] loc = locationField.getText().split(",");
		showWeather(loc[0]);
	}

	/**
	 * Checks if the key pressed is enter, if yes updates the location.
	 * 
	 * @param event Key pressed.
	 */
	public void updateLocation(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			updateLocationClicked(event);
		}
		if(event.getCode().isDigitKey()) {
			locationInputValidation.setVisible(true);
		}else {
			locationInputValidation.setVisible(false);
		}
	}

	/**
	 * Method for showing current weather of the wanted location. Also sets an icon
	 * showing the weather's state.
	 * 
	 * @param location Wanted location for weather.
	 */
	public void showWeather(String location) {
		Image image = null;

		locationField.setText(location);
		try {
			WeatherAPICall weather = new WeatherAPICall(location);
			weatherState.setText(weather.getState());
			weatherCelsius.setText(weather.getCelsius() + "\u00b0C");
			switch (weather.getState()) {
			case "Clouds":
				image = new Image(getClass().getResourceAsStream("/pictures/cloud.png"));
				weatherState.setText(bundle.getString("weather.clouds"));
				break;
			case "Rain":
				image = new Image(getClass().getResourceAsStream("/pictures/rain.png"));
				weatherState.setText(bundle.getString("weather.rain"));
				break;
			case "Clear":
				image = new Image(getClass().getResourceAsStream("/pictures/sunny.png"));
				weatherState.setText(bundle.getString("weather.sunny"));
				break;
			case "Haze":
				image = new Image(getClass().getResourceAsStream("/pictures/spain_flag.png"));
				weatherState.setText(bundle.getString("weather.sunny"));
				break;
			default:
				image = new Image(getClass().getResourceAsStream("/pictures/rain.png"));
				break;
			}
		} catch (Exception e) {
			LOGGER.warning("Could not show weather");
		}
		weatherImageView.setImage(image);
		weatherImageView.setFitHeight(50);
		weatherImageView.setFitWidth(50);
	}

	public ObservableList<String> messageList() {
		ObservableList<String> r = FXCollections.observableArrayList();
		Notification[] notifications = controller.getMyMessages();
		
		for (Notification n: notifications) {
			if (!r.contains(n.getText()))
				r.add(n.getText());
		}
		return r;
	}
	public String timeOfDay() {
		String s;
		int h = LocalTime.now().getHour();
		if (h < 11 ) {
			s = bundle.getString("welcome.morning");
		} else if (h < 17) {
			s = bundle.getString("welcome.day");
		} else {
			s = bundle.getString("welcome.evening");
		}
		
		return s;
	}
	
	/**
	 * Sets the right bundle. Calls the needed methods.
	 * 
	 * @see #showWeather(String)
	 * @see #showAppointments()
	 * @see #showImage()
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bundle = ResourceBundle.getBundle(Bundles.CUSTOMER.getBundleName(), HyteGUI.getLocale());
		String loc = bundle.getString("weather.defaultLocation");
		String welcomeText = timeOfDay();
		welcome.setText(welcomeText + " " + controller.getLoggedCustomer().getFirstName());
		TextFields.bindAutoCompletion(locationField, SuggestionProvider.create(controller.locationSuggestions()));
		showWeather(loc);
		showMessages();
		showPrescription();
		showAppointments();
		showImage();	
	}

	/**
	 * Formats and returns the current date.
	 * 
	 * @return Formatted date.
	 */
	public String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.YYYY");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate);
	}
}
