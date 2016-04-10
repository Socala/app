package socala.app.models;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class CommonTimeOptions {
    public Date startDate;
    public Date endDate;
    public String startTime;
    public String endTime;
    public String[] userIds;
}
