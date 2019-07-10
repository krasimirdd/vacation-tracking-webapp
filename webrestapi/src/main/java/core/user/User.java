package core.user;

import core.vacation.Vacation;
import core.team.Team;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "days_available")
    private int days_available;

    @Column(name = "days_consumed")
    private int days_consumed;

    @Column(name = "email", unique = true)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Vacation> vacations;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private Team team;

    public User() {
    }

    public User(String username, String password, String email, int days_available, int days_consumed) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.days_available = 20;
        this.days_consumed = 0;
    }

    public User(String username, String password, String email, Team team) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.team = team;
        this.days_available = 20;
        this.days_consumed = 0;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDays_available() {
        return days_available;
    }

    public void setDays_available(int days_available) {
        this.days_available = days_available;
    }

    public int getDays_consumed() {
        return days_consumed;
    }

    public void setDays_consumed(int days_consumed) {
        this.days_consumed = days_consumed;
    }

    public Set<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(Set<Vacation> vacations) {
        this.vacations = vacations;
    }

}
