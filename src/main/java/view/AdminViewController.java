package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import model.Customer;
import model.CustomerDAO;
import model.DAOManager;
import model.Staff;
import model.StaffDAO;

/**
 * Luokka kontrolloi ylläpitäjän näkymää
 *
 */
public class AdminViewController implements Initializable {

	@FXML
	Tab editTab;
	@FXML
	Button logout;

	@FXML
	Tab addTab;
	@FXML
	TextField fNameStaff;
	@FXML
	TextField sNameStaff;
	@FXML
	TextField phoneNroStaff;
	@FXML
	TextField emailStaff;
	@FXML
	ChoiceBox<String> profession;
	@FXML
	Button addCustomer;

	@FXML
	Button addStaff;
	@FXML
	TextField hetuCust;
	@FXML
	TextField fNameCust;
	@FXML
	TextField sNameCust;
	@FXML
	TextField phoneNroCust;
	@FXML
	TextField emailCust;
	@FXML
	TextField addressCust;
	@FXML
	TextField ICECust;

	@FXML
	Button find;
	@FXML
	Button findCustomer;
	@FXML
	TextField findStaffName;
	@FXML
	TextField customerID;

	/**
	 * Olio, jota käytetään muiden data access objectien hallinnoimiseen.
	 */

	private DAOManager daoM;
	private Controller c;
	private SuggestionHandler SuggestionHandler;

	/**
	 * Ylläpitäjän näkymän konstruktori. Luo myös data access object managerin.
	 */
	public AdminViewController() {
		daoM = new DAOManager();
		c = new Controller(this);
		SuggestionHandler = new SuggestionHandler();
	}

	/**
	 * Metodi asiakkaan luontia varten. Käynnistyy napin painalluksesta.
	 */
	public void addCustomer() {
		c.addCustomer();
	}

	/**
	 * Metodi työntekijän luontia varten. Käynnistyy napin painalluksesta.
	 */
	public void addStaff() {
		c.addStaff();
	}

	/**
	 * Etsii halutun työntekijän tietokannasta ID:n avulla.
	 */
	public void findStaff() {
		Staff[] staffs = c.findStaff();
		SuggestionHandler.setStaffLista(staffs);
		System.out.println("findstaffkutsuttu");
	}
	
	public void showStaffSuggestions() {
		ArrayList<Staff> a = SuggestionHandler.findWithPrefix(getStaffName());
		for (Staff staff : a) {
			System.out.println(staff.getSurname());
		}
	}

	/**
	 * Etsii halutun asiakkaan tietokannasta ID:n avulla.
	 */
	public void findCustomer() {
		Customer customer = new Customer();
		if (customer.getCustomerID() != getCustomerID()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Tiedot");
			alert.setContentText("Tarkista tiedot");
			alert.showAndWait();
		} else {
			String[] tiedot = { customer.getFirstName(), customer.getSurname(), customer.getSSN(),
					customer.getAddress(), customer.getPhoneNumber(), customer.getIceNumber(), customer.getEmail() };

			for (String string : tiedot) {
				System.out.println(string);
			}
		}
	}

	@FXML
	public void logout() {

	}

	public String getStaffName() {
		return this.findStaffName.getText();
	}

	/**
	 * Palauttaa asiakkaan henkilötunnuksen.
	 * 
	 * @return Asiakkaan henkilötunnus.
	 */
	public String getCustHetu() {
		return this.hetuCust.getText();
	}

	/**
	 * Palauttaa asiakkaan kotiosoitteen.
	 * 
	 * @return Asiakkaan kotiosoite.
	 */
	public String getCustAddress() {
		return this.addressCust.getText();
	}

	/**
	 * Palauttaa asiakkaan lähiomaisen numeron hätätilanteessa.
	 * 
	 * @return Asiakkaan lähiomaisen numeron ICE.
	 */
	public String getCustICE() {
		return this.ICECust.getText();
	}

	/**
	 * Palauttaa asiakkaan sähköpostiosoitteen.
	 * 
	 * @return Asiakkaan sähköpostiosoite.
	 */
	public String getCustEmail() {
		return this.emailCust.getText();
	}

	/**
	 * Palauttaa asiakkaan puhelinnumeron.
	 * 
	 * @return Asiakkaan puhelinnumero.
	 */
	public String getCustPhone() {
		return this.phoneNroCust.getText();
	}

	/**
	 * Palauttaa asiakkaan sukunimen.
	 * 
	 * @return Asiakkaan sukunimi.
	 */
	public String getCustSurname() {
		return this.sNameCust.getText();
	}

	/**
	 * Palauttaa asiakkaan etunimen.
	 * 
	 * @return Asiakkaan etunimi.
	 */
	public String getCustFirstname() {
		return this.fNameCust.getText();
	}

	/**
	 * Palauttaa asiakkaan ID:n.
	 * 
	 * @return Asiakkaan ID.
	 */
	public int getCustomerID() {
		return Integer.parseInt(this.customerID.getText());
	}


	/**
	 * Palauttaa työntekijän etunimen.
	 * 
	 * @return Työnteijän etunimi.
	 */
	public String getStaffFirstName() {
		return this.fNameStaff.getText();
	}

	/**
	 * Palauttaa työntekijän sukunimen.
	 * 
	 * @return Työntekijän sukunimi.
	 */
	public String getStaffSurname() {
		return this.sNameStaff.getText();
	}

	/**
	 * Palauttaa työntekijän puhelinnumeron.
	 * 
	 * @return Työntekijän puhelinnumero.
	 */
	public String getStaffPhone() {
		return this.phoneNroStaff.getText();
	}

	/**
	 * Palauttaa työntekijän sähköpostiosoitteen.
	 * 
	 * @return Työntekijän sähköpostiosoite.
	 */
	public String getStaffEmail() {
		return this.emailStaff.getText();
	}

	/**
	 * Palauttaa työntekijän ammatin.
	 * 
	 * @return Työntekijän ammatti.
	 */
	public String getProfession() {
		return (String) this.profession.getValue();
	}

	/**
	 * Asettaa valintalaatikkoon vaihtoehdot.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		profession.setValue("Lääkäri");
		profession.getItems().add("Lääkäri");
		profession.getItems().add("Hoitaja");
		profession.getItems().add("Asiakaspalvelija");
	}

}
