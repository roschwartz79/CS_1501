/*This class implements an indexable max heap
 * Implemented by Rob Schwartz as part of CS 1501 Project 3
 * Indexed using two different data structures
 *
 * Need to have insert, remove, search methods for a multitude of cases
 *
 */
 
import java.util.HashMap;
 
public class MaxHeap {

    //create heap object
    private static int arraySize = 20;
    private static int itemsInHeap = 0;
    private static Apartment[] heap;
    private static HashMap<String, Integer> indirection;
    //1st level -> KEY: String address VALUE: Next Hashmap
    //2nd level -> KEY: String Apt number VALUE: Next Hashmap
    //3rd level -> KEY: Int zip code VALUE: Integer with location
    private static HashMap<String, HashMap<String, HashMap<Integer, Integer>>> addressMap;

    public MaxHeap() {
        heap = new Apartment[arraySize];
        indirection = new HashMap<>();
        addressMap = new HashMap<>();
    }

    public static void addToHeap(Apartment apartment) {
        //start the node index at where it will be inserted
        int currentNode = itemsInHeap;

        //first check if the apartment is in the locateMap nested HashMap
        if ((addressMap.get(apartment.getAddress()) == null || addressMap.get(apartment.getAddress()).get(apartment.getApartmentNumber()) == null
                || addressMap.get(apartment.getAddress()).get(apartment.getApartmentNumber()).get(apartment.getZipCode()) == null)) {
            //if it doesn't then add it to the hashmap
            int finalCurrentNode = currentNode;
            addressMap.put(apartment.getAddress(), new HashMap() {{
                put(apartment.getApartmentNumber(), new HashMap() {{
                    put(apartment.getZipCode(), finalCurrentNode);
                }});
            }});
        }

        //add the apartment to indirection
        if (!indirection.containsKey(apartment.getCity())) {
            indirection.put(apartment.getCity(), itemsInHeap);
        }


        //if there are no items in the heap
        if (itemsInHeap == 0) {
            heap[0] = apartment;
            itemsInHeap++;

            //Add it to the indexable hashmap
            addressMap.put(apartment.getAddress(), new HashMap() {{
                put(apartment.getApartmentNumber(), new HashMap() {{
                    put(apartment.getZipCode(), 0);
                }});
            }});
        }
        //if there are items
        else {
            //put the item at the next available index
            heap[itemsInHeap] = apartment;

            //check the heap property for the MAX HEAP
            //if a positive value is returned, the parent is greater than the child
            //So, while the value is less than 0, we want to swap the parent and child
            int parentNode = (int) Math.floor((itemsInHeap - 1) / 2);
            while ((heap[currentNode].compareTo(heap[parentNode])) < 0) {
                //put the parent into a temp value
                Apartment tempApartment = heap[parentNode];

                //Move the child up
                heap[parentNode] = apartment;

                //move the parent down
                heap[currentNode] = tempApartment;

                //update the index in the address hash map if we need to update the locations in the array for the
                int finalParentNode = parentNode;
                addressMap.put(apartment.getAddress(), new HashMap() {{
                    put(apartment.getApartmentNumber(), new HashMap() {{
                        put(apartment.getZipCode(), finalParentNode);
                    }});
                }});
                int finalCurrentNode1 = currentNode;
                addressMap.put(tempApartment.getAddress(), new HashMap() {{
                    put(tempApartment.getApartmentNumber(), new HashMap() {{
                        put(tempApartment.getZipCode(), finalCurrentNode1);
                    }});
                }});

                //do we need to update indirection?
                if (indirection.get(tempApartment.getCity()) == parentNode) {
                    indirection.replace(tempApartment.getCity(), currentNode);
                }

                //update where the node is now
                currentNode = parentNode;
                int temp = currentNode;

                //is there another parent
                if (currentNode != 0) {
                    parentNode = (int) Math.floor((temp - 1) / 2);
                }
                //if not, exit as we are at the root
                else {
                    break;
                }
            }

            //increase the # of items in the heap
            itemsInHeap++;
        }

        //Now need to update the indirection for the max sqft by city
        //get the city of the key
        String city = apartment.getCity();
        //If the hashmap contains the key already
        if (indirection.containsKey(city)) {
            //compare the new apartment sqft value with the value already in the hash map index to see if it is higher
            if (apartment.getSquareFootage() > heap[indirection.get(city)].getSquareFootage()) {
                //if it is lower, update the index
                indirection.replace(city, currentNode);
            }
            //if it is not greater, then do not update the hashmap with an index
        }
        //if the key is not in the hashmap, then add it to the hashmap!
        else {
            //add the key and value into the hashmap! :)
            indirection.put(city, currentNode);
        }
    }

    public static Apartment removeFromHeap(int startingLocation) {
        //Get the max square footage
        Apartment apartmentToRemove = heap[startingLocation];

        //swap it with the last node
        heap[startingLocation] = heap[itemsInHeap - 1];

        //set the last node to null
        heap[itemsInHeap - 1] = null;
        itemsInHeap--;

        //Now need to check the children to reestablish heap property
        //start at the top of the heap
        int currentNode = startingLocation, leftIndex, rightIndex;
        while (heap[(2 * currentNode) + 1] != null || heap[(2 * currentNode) + 2] != null) {
            double leftChildDifference, rightChildDifference;
            boolean leftChild = false, rightChild = false;
            //check the left child and right child to see if we need to swap ( if there is a right child)
            leftChildDifference = heap[(2 * currentNode) + 1].compareTo(heap[(currentNode)]);
            if (heap[(2 * currentNode) + 2] != null) {
                rightChildDifference = heap[(2 * currentNode) + 2].compareTo(heap[(currentNode)]);
            } else {
                rightChildDifference = 0;
            }

            //if the difference is less than 0, the parent is less than the child
            if (leftChildDifference < 0) {
                leftChild = true;
            }
            if (rightChildDifference < 0) {
                rightChild = true;
            }

            //if only the leftChild needs to be swapped
            if (leftChild && !rightChild) {
                //swap with the left child
                leftIndex = (2 * currentNode) + 1;
                //get the left child
                Apartment temp = heap[leftIndex];
                //put the parent in the child node
                heap[leftIndex] = heap[currentNode];
                //put the child in the parent
                heap[currentNode] = temp;
                //set new currentNode value
                currentNode = leftIndex;
            }
            //if only the rightChild needs to be swapped
            else if (rightChild && !leftChild) {
                //swap with the right child
                rightIndex = (2 * currentNode) + 2;
                //get the right child
                Apartment temp = heap[rightIndex];
                //put the parent in the child node
                heap[rightIndex] = heap[currentNode];
                //put the child in the parents node
                heap[currentNode] = temp;
                //set the new currentNode value
                currentNode = rightIndex;
            }
            //if both of the children are greater than the currentNode, swap with the greater val
            else if (leftChild && rightChild) {
                //swap with the greater value
                //if the left child is a greater value than the right child -> take the leftChild
                if (Math.abs(leftChildDifference) >= Math.abs(rightChildDifference)) {
                    //swap with the left child
                    leftIndex = (2 * currentNode) + 1;
                    //get the left child
                    Apartment temp = heap[leftIndex];
                    //put the parent in the child node
                    heap[leftIndex] = heap[currentNode];
                    //put the child in the parent
                    heap[currentNode] = temp;
                    //set new currentNode value
                    currentNode = leftIndex;
                } else {
                    //swap with the right child
                    rightIndex = (2 * currentNode) + 2;
                    //get the right child
                    Apartment temp = heap[rightIndex];
                    //put the parent in the child node
                    heap[rightIndex] = heap[currentNode];
                    //put the child in the parents node
                    heap[currentNode] = temp;
                    //set the new currentNode value
                    currentNode = rightIndex;
                }
            }
            //there does not need to be anymore swaps
            else {
                return apartmentToRemove;
            }
        }
        //return the max square footage
        return apartmentToRemove;
    }

    //Retrieve the Max item in the heap, do not delete item
    public static Apartment retrieveMax() {
        return heap[0];
    }

    //retrieve the highest sqft apartment BY CITY and return it, do not delete!
    public static Apartment retrieveMaxByCity(String city) {
        //if the city has been entered to the heap, get the index and return the apartment
        if (indirection.containsKey(city)) {
            return heap[indirection.get(city)];
        }

        //if there is no apartment in that city, do not return an apartment
        return null;
    }

    public static boolean removeApartment(String address, String aptNum, int zipCode) {
        if (addressMap.get(address) == null || addressMap.get(address).get(aptNum) == null
                || addressMap.get(address).get(aptNum).get(zipCode) == null) {
            //if that apartment with the parameters doesn't exist in our database, return null
            return false;
        }
        //if it does exist, return that apartment

        //get the location
        int location = addressMap.get(address).get(aptNum).get(zipCode);

        //remove the node specified by the location
        removeFromHeap(location);

        //return true that is was successfully deleted
        return true;
    }

    public static void findInMaxHeap(String address, String aptNum, int zipCode, double rent) {
        if (addressMap.get(address) == null || addressMap.get(address).get(aptNum) == null
                || addressMap.get(address).get(aptNum).get(zipCode) == null) {
            //if that apartment with the parameters doesn't exist in our database, return null
            return;
        }

        //update the rent
        heap[addressMap.get(address).get(aptNum).get(zipCode)].setRent(rent);
        return;
    }
}
