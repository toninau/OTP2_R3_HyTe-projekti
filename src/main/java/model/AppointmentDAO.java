package model;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
/**
 * 
 * Customer appointment DataAccessObject
 *
 */
public class AppointmentDAO {
	/**
	 * Sessionfactory used for CRUD operations
	 */
	private SessionFactory sessionfactory = null;
	/**
	 * Class constructor
	 * @param sessionfactory used in all CRUD methods
	 */
	public AppointmentDAO(SessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}
	/**
	 * CREATE-Method for saving an appointment to the database
	 * @param appointment object to save into the database
	 * @return true if success
	 */
	public boolean create(Appointment appointment) {
		Session session = sessionfactory.openSession();
		Transaction transaction = null;
		boolean success = false;
		try {
			transaction = session.beginTransaction();
			session.saveOrUpdate(appointment);
			transaction.commit();
			success = true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			session.close();
		}
		return success;
	}
	
	/**
	 * READ-Method to read an appointment by ID
	 * @param id of the appointment
	 * @return list appointment
	 */
	public Appointment read(int id) {
		Session session = sessionfactory.openSession();
		Appointment appointment = new Appointment();
		try {
			session = sessionfactory.openSession();
			session.beginTransaction();
			session.load(appointment, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return appointment;
	}
	/**
	 * READ-Method to read all appointments from a customer
	 * @param customer object
	 * @return List of all appointments
	 */
	@SuppressWarnings("unchecked")
	public Appointment[] readCustomerAppointments(Customer customer) {
		Session session = sessionfactory.openSession();
		List<Appointment> result = null;
		try {
			session = sessionfactory.openSession();
			session.beginTransaction();
			String sql = "SELECT * FROM appointment INNER JOIN customer on appointment.customerID = customer.customerID WHERE customer.customerID = :id";
			Query<Appointment> query = session.createSQLQuery(sql).addEntity(Appointment.class);
			query.setParameter("id", customer.getCustomerID());
			result = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Appointment[] returnArray = new Appointment[result.size()];
		return (Appointment[]) result.toArray(returnArray);
	}
	/**
	 * READ-Method to read all appointments made by a staff member
	 * @param henkilökunta Staff object
	 * @return List appointments
	 */
	@SuppressWarnings("unchecked")
	public Appointment[] readStaffAppointments(Staff staff) {
		Session session = sessionfactory.openSession();
		List<Appointment> result = null;
		try {
			session = sessionfactory.openSession();
			session.beginTransaction();
			String sql = "SELECT * FROM appointment INNER JOIN staff on appointment."
					+ "staffID = staff.staffID WHERE staff.staffID = :id";
			Query<Appointment> query = session.createSQLQuery(sql).addEntity(Appointment.class);
			query.setParameter("id", staff.getStaffID());
			result = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Appointment[] returnArray = new Appointment[result.size()];
		return (Appointment[]) result.toArray(returnArray);
	}
	/**
	 * UPDATE-Method to update an appointment in the database
	 * @param appointment an existing appointment
	 * @return true if success
	 */
	public boolean update(Appointment appointment) {
		boolean success = false;
		Session session = sessionfactory.openSession();
		session.beginTransaction();
		Appointment a = (Appointment) session.get(Appointment.class, appointment.getAppointmentID());
		if (a != null) {
			a.setCustomer(appointment.getCustomer());
			a.setStaff(appointment.getStaff());
			a.setInfo(appointment.getInfo());
			a.setTime(appointment.getTime());
			a.setDate(appointment.getDate());
			success = true;
		} else {
			System.out.println("Nothing to update");
		}
		session.getTransaction().commit();
		session.close();
		return success;
	}
	/**
	 * DELETE-Method to remove an appointment from the database
	 * @param id of the Appointment object
	 * @return true if success
	 */
	public boolean delete(int id) {
		boolean success = false;
		Session session = sessionfactory.openSession();
		session.beginTransaction();
		Appointment a = (Appointment) session.get(Appointment.class, id);
		if (a != null) {
			session.delete(a);
			success = true;
		}
		session.getTransaction().commit();
		session.close();
		return success;
	}
}