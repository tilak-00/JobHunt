package com.example.jobhunt.dto;

public class JobDTO {

    private String company;
    private String role;
    private String eligibility;
    private String qualification;
    private String skills;
    private String experience;
    private String salary;
    private String lastDate;  // Still as String from frontend
    private String location;
    private String applyLink;
    private String mode;      // ✅ New field added

    // Getters and setters
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getLastDate() { return lastDate; }
    public void setLastDate(String lastDate) { this.lastDate = lastDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getApplyLink() { return applyLink; }
    public void setApplyLink(String applyLink) { this.applyLink = applyLink; }

    public String getMode() { return mode; }             // ✅ Getter
    public void setMode(String mode) { this.mode = mode; } // ✅ Setter
}
