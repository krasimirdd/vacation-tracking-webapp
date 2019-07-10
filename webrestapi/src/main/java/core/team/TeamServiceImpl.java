package core.team;

import REST.App;
import core.vacation.Vacation;
import org.hibernate.*;
import org.hibernate.query.NativeQuery;

public class TeamServiceImpl implements TeamService {
	private SessionFactory factory;

	public TeamServiceImpl() {
		this.factory = App.getFactory();
	}

	public Team getTeamById(long id) {

		Transaction transaction = null;
		Team team = null;

		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			team = session.get(Team.class, id);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return team;

	}

	public Team getTeamByVacation(Vacation vacation) {

		Team team = null;
		try (Session session = factory.openSession()) {
			long vacationID = vacation.getId();
			String str = "select * from team as t join user as u on  t.id =u.team_id join vacation as v on v.user_id=" +
					" " +
					"u.id where v.id =:vacationID";
			NativeQuery query = session.createNativeQuery(str);
			query.addEntity(Team.class);
			query.setParameter("vacationID", vacationID);

			team = (Team) query.getSingleResult();

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return team;
	}
}