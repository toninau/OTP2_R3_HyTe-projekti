package controller;

import java.util.Map;

import model.Customer;
import model.Staff;

public interface AdminControllerIF {
	boolean addStaff();
	boolean addCustomer(Map<String, String> map);
	Staff[] findStaffAll();
	Customer[] findCustomerAll();
	Customer findCustomerWithID(String id);
	Staff findStaffWithID(String id);
	boolean updateStaff(Staff staff);
	void updateCustomer(Customer customer);
	boolean removeStaffFromDatabase(String id);
	boolean removeCustomerFromDatabase(String id);
	boolean addCustomerToStaff(Customer customer, Staff staff);
}
