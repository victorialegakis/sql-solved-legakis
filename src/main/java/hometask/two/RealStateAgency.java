package hometask.two;

import hometask.two.enums.Neighborhood;
import hometask.two.enums.PropertyType;
import hometask.two.exceptions.CustomerEmailInputException;
import hometask.two.exceptions.CustomerNumberInputException;
import hometask.two.exceptions.IllegalOperationException;
import hometask.two.exceptions.PropertyNotFoundException;
import hometask.two.generics.CustomLinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Scanner;

public class RealStateAgency {
    private static final String nameOfAgency = "Asahi Real Estate";
    private ArrayList<Property> listOfProperties;
    private CustomLinkedList<Broker> listOfBrokers;
    private ArrayList<Customer> listOfCustomers;

    final Logger LOG_AGENCY = LogManager.getLogger(Main.class.getName());

    public RealStateAgency() {
        this.listOfProperties = new ArrayList<>();
        this.listOfBrokers = new CustomLinkedList<>();
        this.listOfCustomers = new ArrayList<>();
    }


    public static void showNameOfAgency() {
        System.out.println("Welcome to " + nameOfAgency + " services. We are glad to have you here.");
    }

    public int quantityOfProperties() {

        return listOfProperties.size();
    }


    public void showBrokers() {
        System.out.println(listOfBrokers);
    }

    public ArrayList getListByNeighborhood(Neighborhood n) {
        ArrayList<Property> propertiesByNeighborhood = new ArrayList<>();
        for (Property p : listOfProperties) {
            if (p.getNeighborhood() == n) {
                propertiesByNeighborhood.add(p);
            }
        }
        return propertiesByNeighborhood;
    }

    public ArrayList<Property> getListByPropertyTypeAndNeighborhood(PropertyType type, Neighborhood n) {
        ArrayList<Property> listByNeighborhood = getListByNeighborhood(n);
        ArrayList<Property> propertiesByTypeAndNeighborhood = new ArrayList<>();
        for (Property p : listByNeighborhood) {
            String pClass = p.getClass().getSimpleName();
            if (pClass.equalsIgnoreCase(String.valueOf(type))) {
                propertiesByTypeAndNeighborhood.add(p);
            }

        }
        return propertiesByTypeAndNeighborhood;
    }

    public ArrayList<Property> getListByPropertyTypeNeighborhoodAndBudget(PropertyType type, Neighborhood n, int budget) {
        ArrayList<Property> listByPropertyTypeAndNeighborhood = getListByPropertyTypeAndNeighborhood(type, n);
        ArrayList<Property> propertiesByTypeNeighborhoodAndBudget = new ArrayList<>();
        for (Property p : listByPropertyTypeAndNeighborhood) {
            if (p.getBasePrice() <= budget) {
                propertiesByTypeNeighborhoodAndBudget.add(p);
            }
        }
        return propertiesByTypeNeighborhoodAndBudget;
    }

    public void addBroker(Broker broker) {
        this.listOfBrokers.addLast(broker);
    }

    public void addProperty(Property property) {
        this.listOfProperties.add(property);
    }

    private void addCustomer(Customer customer) {
        this.listOfCustomers.add(customer);
    }

    public Customer askAndSaveCustomerData() throws CustomerNumberInputException, CustomerEmailInputException {
        //apply try except and logger
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the following:");
        System.out.println("First name: ");
        String fn = input.nextLine();
        System.out.println("Last name: ");
        String ln = input.nextLine();
        System.out.println("Mail Address: ");
        String ma = input.nextLine();
        if (!ma.contains("@")) {
            throw new CustomerEmailInputException();
        }
        System.out.println("Phone number: ");
        long pn = input.nextLong();
        if (pn > Integer.MAX_VALUE) {
            throw new CustomerNumberInputException();
        }
        Customer customer = new Customer(fn, ln, pn, ma);
        addCustomer(customer);
        return customer;
    }

    public Property getPropertyByID(String idSelected) throws PropertyNotFoundException {
        Property propertyByID = null;
        for (Property p : listOfProperties) {
            if (p.getId().equalsIgnoreCase(idSelected)) {
                propertyByID = p;
                break;
            } else {
                throw new PropertyNotFoundException();
            }
        }
        return propertyByID;
    }

    public void showSaleOrLeaseInfo(Property propertySelected, int op) throws IllegalOperationException {
        try {
            String propertySelectedClass = propertySelected.getClass().getSimpleName();
            if (propertySelectedClass.equals("Land")) {
                if (op == 1) {
                    throw new IllegalOperationException();
                } else {
                    //downcasting
                    Land landSelected = (Land) propertySelected;
                    landSelected.getSaleInfo();
                }
            } else {
                //downcasting
                Edified edifiedSelected = (Edified) propertySelected;
                if (op == 1) {
                    edifiedSelected.getLeaseInfo();
                } else {
                    edifiedSelected.getSaleInfo();
                }
            }
        } catch (NullPointerException e) {
            LOG_AGENCY.error("The property has not been selected");
        }
    }
}