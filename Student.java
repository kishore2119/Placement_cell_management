public class Student {
    String name;
    int age;
    String major;
    double gpa;
    String studentID;
    String email;

    public Student(String studentID,String name, int age, String major, double gpa, String email) {
        this.name = name;
        this.age = age;
        this.major = major;
        this.gpa = gpa;
        this.studentID = studentID;
        this.email = email;
    }
}
