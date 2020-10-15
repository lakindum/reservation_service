package au.com.lakindu.reservation_service.model;

public enum Status {
    BOOKED("BOOKED"),
    UNAVAILABLE("UNAVAILABLE");

    private final String StatusCode;

    Status(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getStatus() {
        return this.StatusCode;
    }
}
