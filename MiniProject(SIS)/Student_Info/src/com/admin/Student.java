package com.admin;




public class Student {
    private String name;
    private int id;
    private String course;
    private double grade;
    //getters and setters encapsulation

    public Student(String name, int id, String course, double grade) {
        this.name = name;
        this.id = id;
        this.course = course;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public double getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", ID: " + id + ", Course: " + course + ", Grade: " + grade;
    }
}


