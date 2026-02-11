public class Company {
    private int id;
    private String name;
    private String location;
    private String industry;
    private String hrContact;

    public Company(String name, String location, String industry, String hrContact) {
        this.name = name;
        this.location = location;
        this.industry = industry;
        this.hrContact = hrContact;
    }

    public Company(int id, String name, String location, String industry, String hrContact) {
        this(name, location, industry, hrContact);
        this.id = id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getIndustry() {
        return industry;
    }

    public String getHrContact() {
        return hrContact;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setHrContact(String hrContact) {
        this.hrContact = hrContact;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", industry='" + industry + '\'' +
                ", hrContact='" + hrContact + '\'' +
                '}';
    }
}
