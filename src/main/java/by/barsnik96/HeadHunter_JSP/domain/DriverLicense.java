package by.barsnik96.HeadHunter_JSP.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "driver_license_types")
public class DriverLicense
{
    @Id
    @Column(name = "driver_license_ID")
    private String id;

    public DriverLicense() {}

    public DriverLicense(String id)
    {
        this.id = id;
    }
}