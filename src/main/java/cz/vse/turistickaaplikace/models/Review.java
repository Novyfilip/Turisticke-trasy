package cz.vse.turistickaaplikace.models;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Review {

    private int id;

    private int value;

    private String dateTime;

    private String comment;

    public Review() {}

    public int getId() {
        return id;
    }


    public String getDateTime() {
        return dateTime;
    }

    public int getReviewValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setDateTime(String dateTimeStamp) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeStamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.dateTime = offsetDateTime.format(DateTimeFormatter.ofPattern("d MMMM, uuuu", new Locale("cs", "CZ")));
    }

    public void setReviewValue(int value) {
        this.value = value;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
