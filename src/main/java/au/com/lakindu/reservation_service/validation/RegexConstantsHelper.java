package au.com.lakindu.reservation_service.validation;

public class RegexConstantsHelper {
    public static final String RESERVATION_ID = "^\\d$";
    public static final String TIMESLOT = "^[0-9]{1,2}([AP][M])-[0-9]{1,2}([AP][M])$";
    public static final String DATE = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
}
