package com.ematrix.infotech.uniupdates.Model;

public class University {
    String UniversityID;
    String UniversityName;
    String UniversityWebsite;
    String uploadimg;

    public String getUploadimg() {
        return uploadimg;
    }

    public void setUploadimg(String uploadimg) {
        this.uploadimg = uploadimg;
    }

    public String getUniversityID() {
        return UniversityID;
    }

    public void setUniversityID(String universityID) {
        UniversityID = universityID;
    }

    public String getUniversityName() {
        return UniversityName;
    }

    public void setUniversityName(String universityName) {
        UniversityName = universityName;
    }

    public String getUniversityWebsite() {
        return UniversityWebsite;
    }

    public void setUniversityWebsite(String universityWebsite) {
        UniversityWebsite = universityWebsite;
    }
}
