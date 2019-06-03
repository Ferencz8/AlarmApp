package countingsheep.alarm.core.domain;


import com.google.gson.annotations.Expose;

import java.util.Date;

public class User {

    public int id;

    @Expose
    public String firstName;

    @Expose
    public String lastName;

    @Expose
    public String email;

    @Expose
    public String location;

    @Expose
    public String gender;

    @Expose
    public Date birthday;

    @Expose
    public  String profilePictureUrl;
}
