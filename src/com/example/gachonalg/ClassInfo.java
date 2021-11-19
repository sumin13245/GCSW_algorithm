package com.example.gachonalg;

class lesson {
    String lessonName;
    String professorName;
    String date;
    String building;
    int lesson_num;

    public void setInfo(String lessonName, String professorName, String date, String building, int lesson_num) {
        this.lessonName = lessonName;
        this.professorName = professorName;
        this.date = date;
        this.building = building;
        this.lesson_num = lesson_num;
    }

    public String getLessonName() {
        return lessonName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getDate() {
        return date;
    }

    public String getBuilding() {
        return building;
    }

    public int getLesson_num() {
        return lesson_num;
    }
}