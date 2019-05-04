package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import view.staff.StaffAppointmentView;
import view.staff.StaffHomeView;

public class StaffController {
	
	private static Staff staff;
	private Customer[] staffCustomers;
	private DAOManager_IF daom;
	private StaffHomeView staffHomeView;
	private StaffAppointmentView appointmentView;

    /**
     * Empty constructor
     */
	public StaffController() {
		daom = new DAOManager();
	}

    /**
     * Controller constructor for the StaffHomeView
     * @param staffHomeView
     */
	public StaffController(StaffHomeView staffHomeView) {
		this.staffHomeView = staffHomeView;
		if (daom == null)
			daom = new DAOManager();
	}

    /**
     * Controller constructor for the StaffAppointmentView
     * @param appointmentView
     */
	public StaffController(StaffAppointmentView appointmentView) {
		this.appointmentView = appointmentView;
		if (daom == null)
			daom = new DAOManager();
	}

    /**
     * Gets all appointments from the database
     * @return List of appointments
     */
	public ObservableList<Appointment> allAppointments() {
		ObservableList<Appointment> list = FXCollections.observableArrayList();
		Appointment[] appointments = daom.getAppointmentDAO().readStaffAppointments(staff);	
		for (Appointment appointment : appointments) {
			list.add(appointment);
		}		
		return list;
	}
	
	/**
	 * Sets the current logged employee.
	 * @param staff Logged employee.
	 */
	public void loggedStaff(Staff staff) {
		StaffController.staff = staff;
	}

    /**
     * Gets the staff object currently logged in
     * @return staff object
     */
	public Staff getLoggedStaff() {
		return StaffController.staff;
	}

	public boolean sendNotification(Notification notification) {
		notification.setStaff(staff);
	    if (daom.getNotificationDAO().create(notification)) {
	    	return true;
		}
	    return false;
    }

    /**
     * Gets the customers associated with this staff member
     * @return List of customers
     */
	public ObservableList<Customer> getStaffCustomers() {
		ObservableList<Customer> customerList = FXCollections.observableArrayList();
		staffCustomers = daom.getStaffDAO().readStaffMembersCustomers(staff);
		
		for (Customer customer : staffCustomers) {
			customerList.add(customer);
			System.out.println(customer.getCustomerID());
		}
		return customerList;
	}

    /**
     * Gets the customers appointments
     * @param customer object
     * @return List of appointments
     */
	public ObservableList<Appointment> getCustomersAppointments(Customer customer) {
		for (Customer c: staffCustomers) {
			if (c.equals(customer)) {
				Appointment[] appointmentList = daom.getAppointmentDAO().readCustomerAppointments(c);
				ObservableList<Appointment> list = FXCollections.observableArrayList(appointmentList);
				return list;
			}
		}
		return null;
	}

    /**
     * Adds an appointment to the database
     * @param appointment object
     * @return true if success
     */
    public boolean addAppointment(Appointment appointment) {
        return daom.getAppointmentDAO().create(appointment);
    }

    /**
     * Saves the new values in the modified appointment to the database
     * @param appointment object
     * @return true if success
     */
	public boolean saveAppointment(Appointment appointment) {
		return daom.getAppointmentDAO().update(appointment);
	}


	public String getDailyHappenings() {
		// TODO Auto-generated method stub
		return null;
	}
}
