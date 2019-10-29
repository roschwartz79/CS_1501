/* This class is the main driver of the apartment tracker
 * Implemented by Rob Schwartz as part of CS 1501 Project 3
 *
 * Takes in an input file called apartments.txt with input data
 * Then allows the user to select an option within the program
 *
 */

import java.io.*;
import java.util.Scanner;

public class AptTracker {
    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to Apartment Tracker!");

        //set up the heaps
        MaxHeap maxHeap = new MaxHeap();
        MinHeap minHeap = new MinHeap();

        //read in the input data
        File file = new File("apartments.txt");

        Scanner scanner = new Scanner(file);

        //skip the first line
        scanner.nextLine();

        //read in the input file
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] apt = line.split(":");
            maxHeap.addToHeap(new Apartment(apt[0], apt[1], apt[2], Integer.parseInt(apt[3]), Double.parseDouble(apt[4]), Integer.parseInt(apt[5])));
            minHeap.addToHeap(new Apartment(apt[0], apt[1], apt[2], Integer.parseInt(apt[3]), Double.parseDouble(apt[4]), Integer.parseInt(apt[5])));
        }

        boolean userContinue = true;

        while (userContinue) {
            //Print options
            System.out.println("What action would you like to perform?\n(1) Add an apartment\n(2) Update an apartment\n(3) Remove an apartment\n" +
                    "(4) Retrieve lowest rent apartment\n(5) Retrive highest Square Footage apartment\n(6) Retrieve lowest rent by city\n" +
                    "(7) Retrieve max square footage by city\n(8) Exit the program\n");
            Scanner userScanner = new Scanner(System.in);
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String userInput = userScanner.next();

            switch (userInput) {
                case "1":
                    //add an apartment
                    System.out.println("ADD AN APARTMENT");
                    String address, aptnum, city;
                    int zipcode, sqft;
                    double addRent;
                    System.out.println("\nEnter the Address: ");
                    address = stdin.readLine();
                    System.out.println("\nEnter the Apartment Number: ");
                    aptnum = stdin.readLine();
                    System.out.println("\nEnter the City: ");
                    city = stdin.readLine();
                    System.out.println("\nEnter the zipcode: ");
                    zipcode = userScanner.nextInt();
                    System.out.println("\nEnter the rent: ");
                    addRent = userScanner.nextDouble();
                    System.out.println("\nEnter the square footage: ");
                    sqft = userScanner.nextInt();

                    minHeap.addToHeap(new Apartment(address, aptnum, city, zipcode, addRent, sqft));
                    maxHeap.addToHeap(new Apartment(address, aptnum, city, zipcode, addRent, sqft));

                    System.out.println("\nApartment add successful!\n");
                    break;
                case "2":
                    //update an apartment
                    String updateAddress;
                    System.out.println("\nEnter the Address: ");
                    updateAddress = stdin.readLine();
                    System.out.println("\nEnter the Apartment Number: ");
                    String updateAptNum = stdin.readLine();
                    System.out.println("\nEnter the zipcode: ");
                    int updateZipcode = userScanner.nextInt();
                    System.out.println("Enter the new rent: ");
                    double updateRent = userScanner.nextDouble();
                    //find it in the min heap, update it and reheap
                    Apartment apartmentToUpdate = minHeap.findInHeap(updateAddress,updateAptNum,updateZipcode, updateRent);
                    //find it in the max heap and update the rent
                    maxHeap.findInMaxHeap(updateAddress,updateAptNum,updateZipcode, updateRent);
					
                    if (apartmentToUpdate != null){
						System.out.println("New Rent: " + apartmentToUpdate.getRent());
					}
					else{
						System.out.println("That apartment could not be found in the DB");
					}
                    break;
                case "3":
                    //remove an apartment
                    String deleteAddress;
                    System.out.println("\nEnter the Address: ");
                    deleteAddress = stdin.readLine();
                    System.out.println("\nEnter the Apartment Number: ");
                    String deleteAptNum = stdin.readLine();
                    System.out.println("\nEnter the zipcode: ");
                    int deleteZipcode = userScanner.nextInt();
                    boolean successfulMax = maxHeap.removeApartment(deleteAddress, deleteAptNum, deleteZipcode);
                    boolean succesfulMin = minHeap.removeApartment(deleteAddress, deleteAptNum, deleteZipcode);
                    if (successfulMax && succesfulMin){
                        System.out.println("Apartment successfully deleted!");
                    }
                    else{
                        System.out.println("There was an issue deleting, please check the input data.");
                    }
                    break;
                case "4":
                    //retrieve lowest rent apartment
                    Apartment lowRent = minHeap.retrieveLowest();
					if (lowRent != null){
						System.out.println("The current lowest rent apartment:\n"
                            + "Address: " + lowRent.getAddress() + "\n"
                            + "Apartment: " + lowRent.getApartmentNumber() + "\n"
                            + "City: " + lowRent.getCity() + "\n"
                            + "Zip Code: " + lowRent.getZipCode() + "\n"
                            + "Rent: " + lowRent.getRent() + "\n"
                            + "Square Footage: " + lowRent.getSquareFootage() + "\n");
					}
					else{
						System.out.println("There are no Apartments in the DB!");
					}
                    break;
                case "5":
                    //retrieve highest square footage apartment
                    Apartment maxSqFt = maxHeap.retrieveMax();
					if (maxSqFt != null){
						System.out.println("The current highest square footage apartment:\n"
                            + "Address: " + maxSqFt.getAddress() + "\n"
                            + "Apartment: " + maxSqFt.getApartmentNumber() + "\n"
                            + "City: " + maxSqFt.getCity() + "\n"
                            + "Zip Code: " + maxSqFt.getZipCode() + "\n"
                            + "Rent: " + maxSqFt.getRent() + "\n"
                            + "Square Footage: " + maxSqFt.getSquareFootage() + "\n");
					}
					else{
						System.out.println("There are no Apartments in the DB!");
					}
                    break;
                case "6":
                    //retrieve the lowest rent by city
                    System.out.println("Enter the city you are looking at: ");
                    String cityLowRent = userScanner.next();
                    Apartment lowRentByCity = minHeap.retrieveLowestByCity(cityLowRent);
                    if (lowRentByCity != null) {
                        System.out.println("The current lowest rent apartment in " + cityLowRent + ":\n"
                                + "Address: " + lowRentByCity.getAddress() + "\n"
                                + "Apartment: " + lowRentByCity.getApartmentNumber() + "\n"
                                + "City: " + lowRentByCity.getCity() + "\n"
                                + "Zip Code: " + lowRentByCity.getZipCode() + "\n"
                                + "Rent: " + lowRentByCity.getRent() + "\n"
                                + "Square Footage: " + lowRentByCity.getSquareFootage() + "\n");
                    }
                    //if there was no apartment in that city
                    else {
                        System.out.println("There are no apartments in your city. Please check back at another time");
                    }
                    break;
                case "7":
                    //retrieve max square footage by city
                    System.out.println("Enter the city you are looking at:: ");
                    String cityMaxSqft = userScanner.next();
                    Apartment maxSqftByCity = maxHeap.retrieveMaxByCity(cityMaxSqft);
                    if (maxSqftByCity != null) {
                        System.out.println("The current highest square footage apartment in " + cityMaxSqft + ":\n"
                                + "Address: " + maxSqftByCity.getAddress() + "\n"
                                + "Apartment: " + maxSqftByCity.getApartmentNumber() + "\n"
                                + "City: " + maxSqftByCity.getCity() + "\n"
                                + "Zip Code: " + maxSqftByCity.getZipCode() + "\n"
                                + "Rent: " + maxSqftByCity.getRent() + "\n"
                                + "Square Footage: " + maxSqftByCity.getSquareFootage() + "\n");
                    }
                    //if there was no apartment in that city
                    else {
                        System.out.println("There are no apartments in your city. Please check back at another time");
                    }
                    break;
                case "8":
                    //exit the program
                    System.out.println("\n\nGoodbye! Exiting....");
                    System.exit(1);
                    break;
                default:
                    System.out.println("\nPlease choose an option from the list!\n");
            }
        }
    }
}
