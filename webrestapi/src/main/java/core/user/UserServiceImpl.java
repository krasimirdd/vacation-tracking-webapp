package core.user;

import REST.App;
import core.vacation.Vacation;
import org.hibernate.*;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import util.CalendarUtil;
import util.CipherUtil;

import java.util.List;
import java.util.Set;

public class UserServiceImpl implements UserService {
	private SessionFactory factory;

	public UserServiceImpl() {

		this.factory = App.getFactory();
	}

	@Override
	public boolean addUser(User user) {

		Transaction transaction = null;
		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "from core.user.User where username=:name or email=:email";
			Query query = session.createQuery(str);
			query.setParameter("name", user.getUsername());
			query.setParameter("email", user.getEmail());
			List list = query.list();
			if (list.size() == 0) {
				String encryptedPassword = CipherUtil.encrypt(user.getPassword());
				user.setPassword(encryptedPassword);
				Long userId = (Long) session.save(user);
				transaction.commit();
				return true;
			}

		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return false;
	}

	@Override
	public boolean checkIfExists(String username, String encryptedPassword) {

		Transaction transaction = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "from core.user.User where username=:username and password=:password";
			Query query = session.createQuery(str);
			query.setParameter("username", username);
			query.setParameter("password", encryptedPassword);
			List list = query.list();

			if (list.size() > 0) {
				return true;
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return false;
	}

	@Override
	public User getUserByName(String username) {

		Transaction transaction = null;
		User user = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "from core.user.User where username=:username";
			Query query = session.createQuery(str);
			query.setParameter("username", username);
			List list = query.list();
			if (list.size() > 0) {
				user = (User) list.get(0);
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return user;
	}

	@Override
	public User getUserByVacationId(long vacationId) {

		Transaction transaction = null;
		User user = null;
		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "select * from user as u join vacation as v on v.user_id=u.id where v.id=:vacationId";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(User.class);
			query.setParameter("vacationId", vacationId);
			List list = query.list();
			if (list.size() > 0) {
				user = (User) list.get(0);
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}

		return user;

	}

	@Override
	public List<User> getUsersByTeamID(long id) {

		Transaction transaction = null;
		List<User> users = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "select * from user as u where u.team_id=:id";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(User.class);
			query.setParameter("id", id);

			List list = query.list();
			if (list.size() > 0) {
				users = (List<User>) list;
			}

		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return users;
	}

	@Override
	public void updateVacationInfo(User user, Vacation vacation, boolean add) {

		Transaction transaction = null;
		Transaction deleteTransaction = null;
		try (Session session = factory.openSession()) {

			int businessDaysCount = CalendarUtil.getBusinessDaysCount(
					vacation.getStartDate(), vacation.getEndDate());

			if (add) {
				transaction = session.beginTransaction();

				if (vacation.isPaid()) {
					user.setDays_available(user.getDays_available() - businessDaysCount);
					user.setDays_consumed(user.getDays_consumed() + businessDaysCount);
					session.update(user);
					transaction.commit();
				} else {
					transaction.rollback();
					session.close();
				}
			} else {

				if (vacation.isPaid()) {
					deleteTransaction = session.beginTransaction();
					user.setDays_available(user.getDays_available() + businessDaysCount);
					user.setDays_consumed(user.getDays_consumed() - businessDaysCount);
					removeVacationFromSet(user, vacation);
					if (deleteTransaction.getStatus().equals(TransactionStatus.ACTIVE)) {
						session.update(user);
						deleteTransaction.commit();
					}


				} else {
					removeVacationFromSet(user, vacation);
					session.update(user);
				}
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			} else if (deleteTransaction != null) {
				deleteTransaction.rollback();
			}
		}
	}

	@Override
	public long getDaysAvailable(long adminID) {

		long daysAvailable = 0;
		try (Session session = factory.openSession()) {
			String str = "select * from vacation.user as u join team as t on t.id = u.team_id where t" +
					".admin_id=:adminID";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(User.class);
			query.setParameter("adminID", adminID);

			List<User> list = (List<User>) query.list();

			for (User user : list) {
				daysAvailable += user.getDays_available();
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return daysAvailable;
	}

	@Override
	public long getDaysConsumed(long adminID) {

		long daysConsumed = 0;
		try (Session session = factory.openSession()) {
			String str = "select * from vacation.user as u join team as t on t.id = u.team_id where t" +
					".admin_id=:adminID";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(User.class);
			query.setParameter("adminID", adminID);

			List<User> list = (List<User>) query.list();

			for (User user : list) {
				daysConsumed += user.getDays_consumed();
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return daysConsumed;
	}

	@Override
	public long getEmployeesWithDaysLeft(long adminID) {

		long employees = 0;
		try (Session session = factory.openSession()) {
			String str = "select * from vacation.user as u join team as t on t.id = u.team_id where t" +
					".admin_id=:adminID";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(User.class);
			query.setParameter("adminID", adminID);

			List<User> list = (List<User>) query.list();

			for (User user : list) {
				if (user.getDays_available() > 0) {
					employees++;
				}
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return employees;

	}

	private void removeVacationFromSet(User user, Vacation vacation) {

		Set<Vacation> vacations = user.getVacations();
		vacations.removeIf(s -> s.getId() == vacation.getId());
	}
}
