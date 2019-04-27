package model;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.hibernate.SessionFactory;

import dao.AppointmentDAO;
import dao.BloodValueDAO;
import dao.CustomerDAO;
import dao.IllnessDAO;
import dao.NotificationDAO;
import dao.PrescriptionDAO;
import dao.StaffDAO;
import dao.UserImageDAO;

/**
 *
 * This manager class manages all DAO-classes in the project.
 *
 */
public class DAOManager implements DAOManager_IF {

	private CustomerDAO customerDAO = null;
	private StaffDAO staffDAO = null;
	private PrescriptionDAO prescriptionDAO = null;
	private IllnessDAO illnessDAO = null;
	private BloodValueDAO bloodValueDAO = null;
	private NotificationDAO notificationDAO = null;
	private AppointmentDAO appointmentDAO = null;
	private UserImageDAO userImageDAO = null;

	private SessionFactory s;
	private ImageLoader i;
	private InfoLoader info;
	File file;

	/**
	 * Class constructor to get the SessionFactory.
	 */
	public DAOManager() {
		s = HibernateUtil.getSessionFactory(false);
		i = ImageLoader.getImageLoader();
		info = InfoLoader.getInfoLoader();
	}

	public void writeAllCustomerInformation(Customer customer) {
		info.writeAppointmentsToFile(customer);
		info.writePrescriptionsToFile(customer);
		// Appointment [] appointments =
		// appointmentDAO.readCustomerAppointments(customer);
		// Prescription [] prescriptions =
		// prescriptionDAO.readCustomersPrescriptions(customer);
		i.writeImagesToFile(customer);
		
	}

	public UserImage[] readCustomerImages() {
		return i.readCustomerImages();
	}

	public Appointment[] readCustomerAppointments() {
		return info.readAppointmentsFromFile();
	}
	
	public Prescription[] readCustomerPrescriptions() {
		return info.readPrescriptionsFromFile();
	}

	/**
	 * 
	 * @param obj
	 */
	public void create(Object obj) {
		if (obj instanceof Staff) {
			getStaffDAO().create((Staff) obj);
		} else if (obj instanceof Customer) {
			getCustomerDAO().create((Customer) obj);
		} else if (obj instanceof Illness) {
			getIllnessDAO().create((Illness) obj);
		} else if (obj instanceof Prescription) {
			getPrescriptionDAO().create((Prescription) obj);
		} else if (obj instanceof Notification) {
			getNotificationDAO().create((Notification) obj);
		} else if (obj instanceof BloodValue) {
			getBloodValueDAO().create((BloodValue) obj);
		} else {
			System.out.println("ei ole");
		}
	}

	public boolean update(Object obj) {
		if (obj instanceof Staff)
			return getStaffDAO().update((Staff) obj);
		else if (obj instanceof Customer)
			return getCustomerDAO().update((Customer) obj);
		else if (obj instanceof Notification)
			return getNotificationDAO().update((Notification) obj);
		else
			return false;
	}

	/**
	 * 
	 * @param obj Name of the wanted object.
	 * @return
	 */
	public Object[] readAll(String obj) {
		switch (obj) {
		case "staff":
			return getStaffDAO().readAll();
		case "customer":
			return getCustomerDAO().readAll();
		default:
			return new Object[0];
		}
	}

	/**
	 * 
	 * @param id  Id of the object to be read.
	 * @param obj Name of the wanted object.
	 * @return
	 */
	public Object readWithID(int id, String obj) {
		switch (obj) {
		case "appointment":
			return getAppointmentDAO().read(id);
		case "notification":
			return getNotificationDAO().read(id);
		case "prescription":
			return getPrescriptionDAO().read(id);
		default:
			return null;
		}
	}

	public Object readWithEmail(String key, String email) {
		switch (key) {
		case "staff":
			return getStaffDAO().read(email);
		case "customer":
			return getCustomerDAO().read(email);
		default:
			return null;
		}
	}

	/**
	 * Creates a Customer Data Access Object. If it exists, return the existing one.
	 * 
	 * @return CustomerDAO.
	 */
	public CustomerDAO getCustomerDAO() {
		if (this.customerDAO == null) {
			this.customerDAO = new CustomerDAO(s);
		}
		return this.customerDAO;
	}

	/**
	 * Creates a Staff Data Access Object. If it exists, return the existing one.
	 * 
	 * @return StaffDAO
	 */
	public StaffDAO getStaffDAO() {
		if (staffDAO == null) {
			staffDAO = new StaffDAO(s);
		}
		return staffDAO;
	}

	/**
	 * Creates a Prescription Data Access Object. If it exists, return the existing
	 * one.
	 * 
	 * @return PrescriptionDAO.
	 */
	public PrescriptionDAO getPrescriptionDAO() {
		if (this.prescriptionDAO == null) {
			this.prescriptionDAO = new PrescriptionDAO(s);
		}
		return this.prescriptionDAO;
	}

	/**
	 * Creates an Illness Data Access Object. If it exists, return the existing one.
	 * 
	 * @return IllnessDAO.
	 */
	public IllnessDAO getIllnessDAO() {
		if (this.illnessDAO == null) {
			this.illnessDAO = new IllnessDAO(s);
		}
		return this.illnessDAO;
	}

	/**
	 * Creates a BloodValue Data Access Object. If it exists, return the existing
	 * one.
	 * 
	 * @return BloodValueDAO
	 */
	public BloodValueDAO getBloodValueDAO() {
		if (this.bloodValueDAO == null) {
			this.bloodValueDAO = new BloodValueDAO(s);
		}
		return this.bloodValueDAO;
	}

	/**
	 * Creates a Notification Data Access Object. If it exists, return the existing
	 * one.
	 * 
	 * @return NotificationDAO
	 */
	public NotificationDAO getNotificationDAO() {
		if (this.notificationDAO == null) {
			this.notificationDAO = new NotificationDAO(s);
		}
		return this.notificationDAO;
	}

	/**
	 * Creates a new appointment data access object if it's null, otherwise returns
	 * existing appointment data access object.
	 * 
	 * @return AppointmentDAO
	 */
	public AppointmentDAO getAppointmentDAO() {
		if (this.appointmentDAO == null) {
			this.appointmentDAO = new AppointmentDAO(s);
		}
		return this.appointmentDAO;
	}

	/**
	 * Creates a new appointment data access object if it's null, otherwise returns
	 * existing appointment data access object.
	 * 
	 * @return AppointmentDAO
	 */
	public UserImageDAO getUserImageDAO() {
		if (this.userImageDAO == null) {
			this.userImageDAO = new UserImageDAO(s);
		}
		return this.userImageDAO;
	}
}
