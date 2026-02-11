import java.time.LocalDate;

public class Drive {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int availableSeats;
    private double lpa;
    private Company company;
    private double minGPA;

    public Drive(LocalDate startDate, LocalDate endDate, int availableSeats, double lpa, Company company,
            double minGPA) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.availableSeats = availableSeats;
        this.lpa = lpa;
        this.company = company;
        this.minGPA = minGPA;
    }

    public Drive(int id, LocalDate startDate, LocalDate endDate, int availableSeats, double lpa, Company company,
            double minGPA) {
        this(startDate, endDate, availableSeats, lpa, company, minGPA);
        this.id = id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getLpa() {
        return lpa;
    }

    public Company getCompany() {
        return company;
    }

    public double getMinGPA() {
        return minGPA;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setLpa(double lpa) {
        this.lpa = lpa;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setMinGPA(double minGPA) {
        this.minGPA = minGPA;
    }

    @Override
    public String toString() {
        return "Drive{" +
                "id=" + id +
                ", company=" + (company != null ? company.getName() : "N/A") +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", availableSeats=" + availableSeats +
                ", lpa=" + lpa +
                ", minGPA=" + minGPA +
                '}';
    }
}
