package core.vacation;

import REST.App;
import core.user.User;
import dto.RequestDTO;
import enumerate.Status;
import org.hibernate.*;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import util.CalendarUtil;

import java.time.LocalDate;
import java.util.List;

public class VacationServiceImpl implements VacationService {
	private SessionFactory factory;

	public VacationServiceImpl() {

		this.factory = App.getFactory();
	}

	@Override
	public Vacation addVacation(User user, String from, String to, String isPaid) {

		Transaction transaction = null;
		Vacation vacation = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();

			LocalDate fromDate = CalendarUtil.convertToLocalDate(from);
			LocalDate toDate = CalendarUtil.convertToLocalDate(to);
			boolean type = false;
			if (isPaid.equalsIgnoreCase("paid")) {
				type = true;
			}

			vacation = new Vacation(fromDate, toDate, type, Status.PENDING, user);
			session.save(vacation);
			transaction.commit();

		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return vacation;
	}

	@Override
	public Vacation cancelVacation(Vacation vacation) {

		Transaction transaction = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			session.delete(vacation);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				vacation = null;
				transaction.rollback();
			}
		}
		return vacation;
	}

	@Override
	public Vacation getVacationById(long vacationId) {

		Transaction transaction = null;
		Vacation vacation = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "from core.vacation.Vacation where id=:id";
			Query query = session.createQuery(str);
			query.setParameter("id", vacationId);
			List list = query.list();
			if (list.size() != 0) {
				vacation = (Vacation) list.get(0);
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return vacation;
	}

	@Override
	public List<Vacation> listRequestedVacations(long userID, long teamID) {

		Transaction transaction = null;
		List<Vacation> vacations = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			String str = "select * from vacation as v join user as u on v.user_id =:userID where u.team_id =:teamID " +
					"and u.id=:uID";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(Vacation.class);
			query.setParameter("userID", userID);
			query.setParameter("teamID", teamID);
			query.setParameter("uID", userID);
			vacations = (List<Vacation>) query.list();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return vacations;
	}

	@Override
	public RequestDTO ifAcceptableRequest(User user, String from, String to, String isPaid) {

		LocalDate fromDate = CalendarUtil.convertToLocalDate(from);
		LocalDate toDate = CalendarUtil.convertToLocalDate(to);

		List<Vacation> vacations = listRequestedVacations(user.getId(), user.getTeam().getId());

		for (Vacation v : vacations) {
			if (!v.getStatus().equals(Status.REJECTED)) {
				if ((fromDate.isAfter(v.getStartDate()) || fromDate.isEqual(v.getStartDate())) && (toDate.isBefore(v.getEndDate()) || toDate.isEqual(v.getEndDate()))) {
					return new RequestDTO(false, fromDate, toDate);
				} else if (toDate.isAfter(v.getEndDate()) && fromDate.isBefore(v.getEndDate())) {
					fromDate = v.getEndDate().plusDays(1);
				} else if (fromDate.isBefore(v.getStartDate()) && toDate.isAfter(v.getStartDate())) {
					toDate = v.getStartDate().minusDays(1);
				}
			}
		}


		boolean type = false;
		if (isPaid.equalsIgnoreCase("paid")) {
			type = true;
		}

		boolean chronological = CalendarUtil.checkIfChronological(fromDate, toDate);
		if (!chronological) {
			return new RequestDTO(false, fromDate, toDate);
		}

		if (!type) {
			return new RequestDTO(true, fromDate, toDate);
		}

		int businessDaysCount = CalendarUtil.getBusinessDaysCount(fromDate, toDate);
		boolean result = businessDaysCount <= user.getDays_available();

		return new RequestDTO(result, fromDate, toDate);

	}

	@Override
	public boolean ifCancelable(Vacation vacation) {

		return vacation.getStatus().equals(Status.PENDING);
	}
}