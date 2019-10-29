/* This class implements an Apartment Object
 * Implemented by Rob Schwartz as part of CS 1501 Project 3
 * Simple class that outlines attributes of an Apartment
 *
 * Need to be able to compare rents and square footage sizes
 *
 */

public class Apartment implements Comparable<Apartment>{
    private String address;
    private String apartmentNumber;
    private String city;
    private int zipCode;
    private double rent;
    private int squareFootage;

    public Apartment(String ad, String an, String c, int z, double r, int sf){
        this.address = ad;
        this.apartmentNumber = an;
        this. city = c;
        this.zipCode = z;
        this.rent = r;
        this.squareFootage = sf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public int compareTo(Apartment apartment) {
        return apartment.squareFootage - this.squareFootage;
    }

    public double compareToRent(Apartment apartment){
        return apartment.rent - this.rent;
    }


}
