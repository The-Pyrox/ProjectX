package com.pyrox.projectx;

/**
 * Created by poojanrathod on 7/17/17.
 */

public class Userdetails {
    private String email,password,faculty,department,designation,semester,firstname,lastname;

    public Userdetails(String email, String password, String faculty, String department, String designation, String semester, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.faculty = faculty;
        this.department = department;
        this.designation = designation;
        this.semester = semester;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesgination() {
        return designation;
    }

    public void setDesgination(String desgination) {
        this.designation = desgination;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
