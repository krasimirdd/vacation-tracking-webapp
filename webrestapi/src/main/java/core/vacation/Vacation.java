package core.vacation;

import enumerate.Status;
import core.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacation")
public class Vacation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "isPaid")
    private boolean isPaid;

    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Vacation() {
    }

    public Vacation(LocalDate startDate, LocalDate endDate, boolean isPaid, Status status, User user) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPaid = isPaid;
        this.status = status;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append(this.getId()).append("\t|\t")
                .append(this.getStartDate()).append("\t|\t")
                .append(this.getEndDate()).append("\t|\t")
                .append(this.isPaid()).append("\t|\t")
                .append(this.getStatus());

        return builder.toString();
    }
}
