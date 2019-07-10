package dto;

import java.time.LocalDate;

public class ReportVacationDaysDTO {
    private long TotalVacationDaysAvailable;
    private long TotalVacationDaysConsumed;
    private String AdminUsername;
    private LocalDate CurrentDate;
    private String TeamName;

    public ReportVacationDaysDTO(long daysAvailable, long daysConsumed, String adminUsername, String teamName) {
        this.TotalVacationDaysAvailable = daysAvailable;
        this.TotalVacationDaysConsumed = daysConsumed;
        this.AdminUsername = adminUsername;
        this.TeamName = teamName;
        this.CurrentDate = LocalDate.now();
    }

    public long getTotalVacationDaysAvailable() {
        return TotalVacationDaysAvailable;
    }

    public void setTotalVacationDaysAvailable(long totalVacationDaysAvailable) {
        TotalVacationDaysAvailable = totalVacationDaysAvailable;
    }

    public long getTotalVacationDaysConsumed() {
        return TotalVacationDaysConsumed;
    }

    public void setTotalVacationDaysConsumed(long totalVacationDaysConsumed) {
        TotalVacationDaysConsumed = totalVacationDaysConsumed;
    }

    public String getAdminUsername() {
        return AdminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        AdminUsername = adminUsername;
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
