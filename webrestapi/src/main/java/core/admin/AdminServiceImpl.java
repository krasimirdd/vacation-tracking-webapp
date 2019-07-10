package core.admin;

import REST.App;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.team.Team;
import core.user.User;
import core.vacation.Vacation;
import dto.ReportEmployeeDTO;
import dto.ReportVacationDaysDTO;
import enumerate.Status;
import org.hibernate.*;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.json.JSONObject;
import util.CalendarUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static REST.App.emailUtil;
import static util.Messages.Email.*;

public class AdminServiceImpl implements AdminService {
	private SessionFactory factory;

	public AdminServiceImpl() {

		this.factory = App.getFactory();
	}

	@Override
	public void changeVacationStatus(String adminName, long requestId, Status newStatus) {

		Transaction transaction = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			long adminID = getAdminByUsername(adminName).getId();
			String str = "select * from vacation as v join user as u on  v.user_id =u.id join team as t on t.id = u" +
					".team_id join admin as ad on ad.id =:id where v.id =:requestID";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(Vacation.class);
			query.setParameter("id", adminID);
			query.setParameter("requestID", requestId);

			Vacation vac = (Vacation) query.getSingleResult();
			vac.setStatus(newStatus);

			session.merge(vac);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	@Override
	public Admin getAdminByUsername(String username) {

		Transaction transaction = null;
		Admin admin = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "from core.admin.Admin where username=:username";
			Query query = session.createQuery(str);
			query.setParameter("username", username);
			List list = query.list();
			if (list.size() > 0) {
				admin = (Admin) list.get(0);
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return admin;
	}

	@Override
	public Admin getAdminByTeamId(long teamId) {

		Transaction transaction = null;
		Admin admin = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "select * from admin as a join team as t on t.admin_id=a.id where t.id=:teamId";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(Admin.class);
			query.setParameter("teamId", teamId);
			List list = query.list();
			admin = (Admin) list.get(0);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return admin;
	}

	@Override
	public boolean checkIfAdminExists(String username, String encryptedPassword) {

		Transaction transaction = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "from core.admin.Admin where username=:username and password=:password";
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
	public boolean isAuthorized(Admin admin, Team team) {

		return admin.getId() == team.getAdmin().getId();
	}

	@Override
	public String generateVacationReport(long daysAvailable, long daysConsumed, Admin admin) {

		ReportVacationDaysDTO report = new ReportVacationDaysDTO(daysAvailable, daysConsumed, admin.getUsername(),
				admin.getTeam().getName());
		JSONObject input = new JSONObject(report);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(input);
	}

	@Override
	public String generateUserReport(long employeesCount, long totalEmployees, Admin admin) {

		ReportEmployeeDTO employee = new ReportEmployeeDTO(admin.getUsername(), employeesCount, totalEmployees,
				admin.getTeam().getName());
		JSONObject input = new JSONObject(employee);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(input);
	}

	@Override
	public String tryToApprove(long requestId, String reason, Vacation vacation, Team team, User user, Admin admin) {

		boolean isOverbooked = this.checkIfPeriodIsOverbooked(
				vacation.getStartDate(),
				vacation.getEndDate(),
				admin.getId(),
				team.getUsers().size()
		);

		if (!isOverbooked) {
			this.changeVacationStatus(admin.getUsername(), requestId, Status.APPROVED);
			emailUtil.sendEmailTo(Status.APPROVED, user.getEmail(), reason);
			return EMAIL_SENDING;
		} else {
			this.changeVacationStatus(admin.getUsername(), requestId, Status.REJECTED);
			emailUtil.sendEmailTo(Status.REJECTED, user.getEmail(), OVERBOOKED);
			return OVERBOOKED_FLASH;
		}
	}

	private boolean checkIfPeriodIsOverbooked(LocalDate start_date, LocalDate end_date, long adminId, int usersCount) {

		List<LocalDate> daysBetween = CalendarUtil.getListOfDaysBetween(start_date, end_date);
		List<Vacation> allVacations = getVacationsByReeferAdmin(adminId);
		List<Vacation> vacations =
				allVacations.stream().filter(v -> (v.getStatus().equals(Status.APPROVED))).collect(Collectors.toList());

		if (usersCount < 2) {
			return true;
		}

		if (vacations.size() == 0) {
			return false;
		}

		for (LocalDate day : daysBetween) {
			int counter = getNumberOfVacationsForDay(vacations, day);

			double count = Double.parseDouble(String.valueOf(usersCount));
			if ((counter / count) * 100 >= 40) {
				return true;
			}
		}

		return false;
	}

	private int getNumberOfVacationsForDay(List<Vacation> vacations, LocalDate day) {

		int counter = 0;
		for (Vacation vac : vacations) {

			for (LocalDate i = vac.getStartDate();
			     i.isBefore(vac.getEndDate().plusDays(1));
			     i = i.plusDays(1)) {

				if (day.isEqual(i)) {
					counter++;
				}
			}
		}
		return counter;
	}

	private List<Vacation> getVacationsByReeferAdmin(long id) {

		List<Vacation> vacations = null;

		try (Session session = factory.openSession()) {
			String str = "select * from vacation as v join user as u on  v.user_id =u.id join team as t on t.id = u" +
					".team_id where admin_id=:id";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(Vacation.class);
			query.setParameter("id", id);

			vacations = (List<Vacation>) query.list();

		} catch (HibernateException e) {
			e.getStackTrace();
		}
		return vacations;
	}
}
