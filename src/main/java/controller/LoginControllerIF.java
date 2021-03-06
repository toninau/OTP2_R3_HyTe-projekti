package controller;

import model.Customer;
import model.Staff;

public interface LoginControllerIF {

	abstract boolean checkLoginCustomer(String username, String password);
	abstract Customer getCustomerFromDatabase(String email);
	abstract Staff getStaffFromDatabase(String email);
	abstract boolean checkLoginStaff(String username, String password);
}
