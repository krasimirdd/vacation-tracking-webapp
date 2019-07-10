package dto;

import java.time.LocalDate;

public class RequestDTO {
    private boolean acceptable;
    private LocalDate from;
    private LocalDate to;

    public RequestDTO(boolean acceptable, LocalDate from, LocalDate to) {
        this.acceptable = acceptable;
        this.from = from;
        this.to = to;
    }

    public boolean isAcceptable() {
        return acceptable;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }
}
