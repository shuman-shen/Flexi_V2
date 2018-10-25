package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

import javafx.application.Application;
import model.*;
import view.MainWindow;

public class MainWindowControl {

    private FlexiRentSystem flexiModel;
    private LocalDate today;
    // private ArrayList<String> items;
    private Property property;

    public MainWindowControl(FlexiRentSystem f) {
        flexiModel = f;
        // filteredList = new ArrayList<Property>(flexiModel.getPropertyList());

    }

    public FlexiRentSystem getFlexiRentSystem() {
        return flexiModel;
    }

    public ArrayList<Property> getFilteredList() {
        return flexiModel.getFilteredList();
    }

//  
    public ArrayList<Property> getWholeList() throws Exception {
        flexiModel.getMainList();
        return flexiModel.getPropertyList();
    }

    public ArrayList<String> getAllSuburb() {
        return flexiModel.getSuburbList();
    }

    public ArrayList<String> getAllID() throws SQLException {
        flexiModel.getAllID();
        return flexiModel.getFilteredID();
    }

    public void rent(String propertyID, String customerId, LocalDate rentDate, int numOfRentDay) throws Exception {
        flexiModel.setFilterList(propertyID);
        flexiModel.getFilteredList().get(0).rent(customerId, rentDate, numOfRentDay);
    }

    public void returnProperty(String propertyID, LocalDate returnDate) throws Exception {
        flexiModel.setFilterList(propertyID);

        flexiModel.getFilteredList().get(0).returnProperty(returnDate);
    }

    public void performMaintenance(String propertyID) throws SQLException {
        flexiModel.setFilterList(propertyID);
        flexiModel.getFilteredList().get(0).performMaintenance();
    }

    public void completeMaintenance(String propertyID, LocalDate completionDate) throws SQLException {
        flexiModel.setFilterList(propertyID);
        flexiModel.getFilteredList().get(0).completeMaintenance(completionDate);
    }

    public void exportData(String input) throws FileNotFoundException {
        // TODO Auto-generated method stub
        File file1 = new File(input);

        try {
            PrintWriter pw = new PrintWriter(file1);

            for (Property p : flexiModel.getPropertyList()) {

                pw.write(p.toString());

                if (p.getRecords().size() > 0) {

                    for (RentalRecord r : p.getRecords()) {

                        pw.write(r.toString());
                    }

                }
            }

            pw.close(); // don't forget this method
        } catch (FileNotFoundException e) {
            throw e;
        }

    }

    public void importData(String input) throws Exception {

        File file1 = new File(input);

        // items = new ArrayList<String>();

        try (Scanner in = new Scanner(file1)) {

            String lastMaintenance = "none";
            String image = "none";
            String desc = "none";

            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] items = line.split(":");

                if (items.length >= 9) {
                    String pID = items[0];
                    int streetNum = Integer.parseInt(items[1]);
                    String streetName = items[2];
                    String suburb = items[3];
                    int bedNum = Integer.parseInt(items[5]);
                    String st = items[6];
                    int status = convertStatus(st);

                    if (items[4].startsWith("P")) {
                        lastMaintenance = items[7];
                        image = items[8];
                        desc = items[9];

                    } else {
                        image = items[7];
                        desc = items[8];
                    }

                    flexiModel.importData(pID, streetNum, streetName, suburb, bedNum, lastMaintenance, status, image,
                            desc);

                    flexiModel.setFilterList(pID);
                    property = flexiModel.getFilteredList().get(0);

                }

                else {

                    String rID = items[0];

                    String[] c = rID.split("_");
                    String cID = "C_" + c[3];

                    String rent = items[1];
                    String estDate = items[2];
                    String actualDate = items[3];
                    String fee = items[4];
                    String lateFee = items[5];
                    // double fee = Double.parseDouble(items[4]);
                    // double lateFee = Double.parseDouble(items[5]);

                    property.insertNewRecord(property.getPropertyID(), rID, cID, rent, estDate, actualDate, fee,
                            lateFee);

                }

            }
        } catch (Exception other) {
            throw other;
        }

    }

    public String createApartment(String image, String streetNum, String streetName, String suburb, int bedNum,
            String desc) throws Exception {
        String id = "1";

        if (image.equals("") || streetName.equals("") || streetNum.equals("") || suburb.equals("") || desc.equals("")) {
            throw new Exception("Empty fields exist");
        } else {

            int streetNo = Integer.parseInt(streetNum);
            id = flexiModel.addApartment(streetNo, streetName, suburb, bedNum, image, desc);
            System.out.println(id + " return to control");

        }
        return id;
    }

    public String createPremiumSuite(String image, String streetNum, String streetName, String suburb,
            LocalDate lastMaintainDate, String desc) throws Exception {
        today = LocalDate.now();

        if (image.equals("") || streetName.equals("") || streetNum.equals("") || suburb.equals("")
                || lastMaintainDate.equals("") || desc.equals("")) {
            throw new Exception("Empty fields exist");
        } else if (lastMaintainDate.compareTo(today) > 0) {
            throw new Exception("Last maintenance Date shall not be later than today.");

        } else {
            System.out.println("RECEIVED");
            int streetNo = Integer.parseInt(streetNum);

            String id = flexiModel.addPremiumSuite(streetNo, streetName, suburb, lastMaintainDate, image, desc);
            System.out.println(id + " return to control");

            return id;
        }

    }

    public ArrayList<RentalRecord> getRecordList(String propertyID) throws SQLException {
        flexiModel.setFilterList(propertyID);
        ArrayList<RentalRecord> r = flexiModel.getFilteredList().get(0).getRecords();
        return r;

    }

    public boolean setFilter(String type, String bedNum, String condition, String suburbText) throws SQLException {

        String pType;
        int num;
        int cType;
        String suburb;
        if (type.startsWith("All")) {
            pType = "All";
        } else
            pType = type;

        if (bedNum.startsWith("All")) {
            num = 0;
        } else {
            num = Integer.parseInt(Character.toString(bedNum.charAt(0)));
        }

        if (condition.startsWith("All")) {
            cType = 0;
        } else if (condition.equals("Available")) {
            cType = 2;
        } else if (condition.equals("Rent")) {
            cType = 1;
        } else {
            cType = 3;
        }

        if (suburbText.equals("All Suburbs")) {
            suburb = "General";
        } else {

            suburb = suburbText;
        }
        if (pType.equals("All") && num == 0 && cType == 0 && suburb.equals("General"))
            return false;
        else {
            flexiModel.setFilterList(pType, num, cType, suburb);

            return true;
        }

    }

    public boolean setFilter(String propertyID) throws SQLException {
        return flexiModel.setFilterList(propertyID);
    }

    public int convertStatus(String st) {
        int i = 0;

        if (st.startsWith("A")) {
            i = 2;
        } else if (st.startsWith("U")) {
            i = 3;
        } else
            i = 1;

        return i;

    }

}
