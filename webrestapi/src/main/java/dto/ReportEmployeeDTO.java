package dto;

import java.time.LocalDate;

public class ReportEmployeeDTO {
	private String AdminUsername;
	private long EmployeesVacationDaysAvailable;
	private long TotalEmployeesCount;
	private LocalDate CurrentDate;
	private String TeamName;

	public ReportEmployeeDTO(String adminUsername, long employeesCount, long employeesDaysLeftAvailable, String teamName) {

		this.AdminUsername = adminUsername;
		this.EmployeesVacationDaysAvailable = employeesDaysLeftAvailable;
		this.TotalEmployeesCount = employeesCount;
		this.TeamName = teamName;
		this.CurrentDate = LocalDate.now();
	}

	public String getAdminUsername() {

		return AdminUsername;
	}

	public void setAdminUsername(String adminUsername) {

		AdminUsername = adminUsername;
	}

	public long getEmployeesVacationDaysAvailable() {

		return EmployeesVacationDaysAvailable;
	}

	public void setEmployeesVacationDaysAvailable(long employeesVacationDaysAvailable) {

		EmployeesVacationDaysAvailable = employeesVacationDaysAvailable;
	}

	public LocalDate getCurrentDate() {

		return CurrentDate;
	}

	public void setCurrentDate(LocalDate currentDate) {

		CurrentDate = currentDate;
	}

	public String getTeamName() {

		return TeamName;
	}

	public void setTeamName(String teamName) {

		TeamName = teamName;
	}
}
