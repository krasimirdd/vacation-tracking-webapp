package REST;

import core.admin.Admin;
import core.admin.routes.*;
import core.home.HomeGetRoute;
import core.login.LoginGetRoute;
import core.login.LoginPostRoute;
import core.logout.LogoutGetRoute;
import core.signup.*;
import core.team.Team;
import core.user.User;
import core.user.routes.*;
import core.vacation.Vacation;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import spark.Filter;
import spark.ModelAndView;
import spark.Request;
import spark.servlet.SparkApplication;
import spark.template.handlebars.HandlebarsTemplateEngine;
import util.EmailUtil;
import util.Setup;

import java.sql.Connection;
import java.sql.SQLException;

import static spark.Spark.*;
import static util.Messages.HTML.FLASH_MESSAGE_KEY;


public class App implements SparkApplication {
	private static SessionFactory factory = initSessionFactory();

	public static EmailUtil emailUtil = new EmailUtil();

	private static final String[] protectedRoutes = {"/user/", "/admin/"};

	public App() {

	}

	@Override
	public void init() {

		port(8080);

		staticFileLocation("/public");

		HandlebarsTemplateEngine hbs = new HandlebarsTemplateEngine();

		setupProtectedFilters();

		before("/user/*", (request, response) -> {
			Object user = request.session(true).attribute("user");
			if (!(user instanceof User)) {
				halt(401, new HandlebarsTemplateEngine().render(new ModelAndView(null,"forbidden.hbs")) );
			}
		});
		before("/admin/*", (request, response) -> {
			Object user = request.session(true).attribute("user");
			if (!(user instanceof Admin)) {
				halt(401, new HandlebarsTemplateEngine().render(new ModelAndView(null,"forbidden.hbs")) );
			}
		});

		before("/*",((request, response) -> {
			response.header("Cache-control", "no-store, must-revalidate, private,no-cache");
			response.header("Pragma", "no-cache");
			response.header("Expires", "0");
		}));
		get("/", new HomeGetRoute(), hbs);

		get("/sign-up/", new SignUpGetRoute(), hbs);
		post("/sign-up/", new SignUpPostRoute(), hbs);

		get("/successfullySigned/", new SuccessfullySignedGetRoute(), hbs);
		get("/sign-up/terms/", new SignUpGetTerms(), hbs);

		get("/login/", new LoginGetRoute(), hbs);
		post("/login/", new LoginPostRoute(), hbs);

		get("/logout/", new LogoutGetRoute(), hbs);

		get("/user/", new UserGetRoute(), hbs);
		post("/user/", new UserPostRoute(), hbs);

		get("/user/history/:username", new UserHistoryGetRoute(), hbs);
		post("/user/history/:username", new UserHistoryPostRoute(), hbs);

		get("/admin/", new AdminGetRoute(), hbs);
		post("/admin/", new AdminPostRoute(), hbs);

		get("/admin/reports/", new AdminReportsGetRoute(), hbs);
		post("/admin/reports/", new AdminReportsPostRoute(), hbs);
	}

	@Override
	public void destroy() {

	}

	private static void setupProtectedFilters() {

		Filter f = ((request, response) -> {

			if (request.session(true).attribute("user") == null) {
				halt(401, new HandlebarsTemplateEngine().render(new ModelAndView(null,"forbidden.hbs")) );
			}
		});


		for (String route : protectedRoutes) {
			before(route, f);
		}
	}

	public static void setFlashMessage(Request req, String message) {

		req.session().attribute(FLASH_MESSAGE_KEY, message);
	}

	public static String getFlashMessage(Request req) {

		String message = req.session().attribute(FLASH_MESSAGE_KEY);
		req.session().removeAttribute(FLASH_MESSAGE_KEY);
		return message;
	}

	public static SessionFactory getFactory() {

		return factory;
	}

	private static SessionFactory initSessionFactory() {
		// Prepare the Hibernate configuration
		StandardServiceRegistry reg = new StandardServiceRegistryBuilder().configure().build();
		MetadataSources metaDataSrc = new MetadataSources(reg);

		// Get database connection
		Connection con = null;
		try {
			con = metaDataSrc.getServiceRegistry().getService(ConnectionProvider.class).getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JdbcConnection jdbcCon = new JdbcConnection(con);

		// Initialize Liquibase and run the update
		Database database = null;
		try {
			database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcCon);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		Liquibase liquibase = new Liquibase(Setup.DbSetup.SQL_FILE_1, new ClassLoaderResourceAccessor(), database);


		// Create Hibernate SessionFactory
		SessionFactory factory = metaDataSrc
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(Vacation.class)
				.addAnnotatedClass(Admin.class)
				.addAnnotatedClass(Team.class)
				.buildMetadata().buildSessionFactory();

		try {
			liquibase.update("team");
		} catch (LiquibaseException e) {
			e.printStackTrace();
		}
		liquibase = new Liquibase(Setup.DbSetup.SQL_FILE_2, new ClassLoaderResourceAccessor(), database);
		try {
			liquibase.update("admin");
		} catch (LiquibaseException e) {
			e.printStackTrace();
		}
//        liquibase = new Liquibase("import3.sql", new ClassLoaderResourceAccessor(), database);
//        liquibase.update("core.user");

		return factory;
	}

}
