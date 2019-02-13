package com.viper.team.encampus;

public class Tutor extends User
{
    String rate;
    String subject;
    int stars;
    Tutor()
    {

    }
    public Tutor(User user,String rate,String subject)
    {
        this.setCampus(user.getCampus());
        this.setCourse(user.getCourse());
        this.setEmail(user.getEmail());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setUsername(user.getUsername());
        this.setStudentID(user.getStudentID());
        this.rate=rate;
        this.subject=subject;
        this.stars=5;
    }
    @Override
    public String toString()
    {
        return this.getUsername()+"\nR"+subject+"\nSubject:"+rate;
    }



}
