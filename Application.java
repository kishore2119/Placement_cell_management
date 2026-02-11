public class Application {
    private int id;
    private Student student;
    private Drive drive;
    private String applicationDate;
    private String status; // "Applied", "Shortlisted", "Rejected", "Accepted"

    public Application(Student student, Drive drive, String applicationDate, String status) {
        this.student = student;
        this.drive = drive;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    public Application(int id, Student student, Drive drive, String applicationDate, String status) {
        this(student, drive, applicationDate, status);
        this.id = id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Drive getDrive() {
        return drive;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", student=" + (student != null ? student.getStudentID() : "N/A") +
                ", drive=" + (drive != null ? drive.getId() : "N/A") +
                ", applicationDate='" + applicationDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
