import DB_connections.*;

/**
 * Admin service class that provides business-logic methods
 * for managing placement drives, companies, students, and applications.
 */
public class Admin {

    // ==================== Drive Management ====================

    public boolean addDrive(int companyId, String startDate, String endDate,
            int seats, double lpa, double minGpa) {
        return DriveDB.addDrive(companyId, startDate, endDate, seats, lpa, minGpa);
    }

    public boolean updateDrive(int driveId, int companyId, String startDate, String endDate,
            int seats, double lpa, double minGpa) {
        return DriveDB.updateDrive(driveId, companyId, startDate, endDate, seats, lpa, minGpa);
    }

    public boolean deleteDrive(int driveId) {
        return DriveDB.deleteDrive(driveId);
    }

    // ==================== Company Management ====================

    public boolean addCompany(String name, String location, String industry, String hrContact) {
        return CompanyDB.addCompany(name, location, industry, hrContact);
    }

    public boolean updateCompany(int id, String name, String location, String industry, String hrContact) {
        return CompanyDB.updateCompany(id, name, location, industry, hrContact);
    }

    public boolean deleteCompany(int id) {
        return CompanyDB.deleteCompany(id);
    }

    // ==================== Student Management ====================

    public boolean deleteStudent(String rollNum) {
        return StudentDB.deleteStudent(rollNum);
    }

    // ==================== Application Management ====================

    public boolean updateApplicationStatus(int appId, String newStatus) {
        return ApplicationDB.updateStatus(appId, newStatus);
    }

    public boolean deleteApplication(int appId) {
        return ApplicationDB.deleteApplication(appId);
    }
}
