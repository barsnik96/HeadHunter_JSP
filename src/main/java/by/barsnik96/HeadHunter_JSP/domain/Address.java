package by.barsnik96.HeadHunter_JSP.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_ID")
    private int id;
    
    @Column(name = "address_city")
    private String city;

    @Column(name = "address_street")
    private String street;

    @Column(name = "address_building")
    private String building;

    @Column(name = "address_description")
    private String description;

    @Column(name = "address_lat")
    private double lat;

    @Column(name = "address_lng")
    private double lng;

    public Address() {}

    public Address(int id, String city, String street, String building, String description, double lat, double lng)
    {
        this.id = id;
        this.city = city;
        this.street = street;
        this.building = building;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
    }
}