public class Student {
    private String studentID;
    private String name;
    private int age;
    private String major;
    private double gpa;
    private String email;

    public Student(String studentID, String name, int age, String major, double gpa, String email) {
        this.studentID = studentID;
        this.name = name;
        this.age = age;
        this.major = major;
        this.gpa = gpa;
        this.email = email;
    }

    // Getters
    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getMajor() {
        return major;
    }

    public double getGpa() {
        return gpa;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentID='" + studentID + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", major='" + major + '\'' +
                ", gpa=" + gpa +
                ", email='" + email + '\'' +
                '}';
    }
}
