import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class TaxReportFileManager {

    //Hardcoded value of the compatible file name for the type of file that works with this class: taxreport.txt,
    //it is hardcoded because this class is not designed to work with any other file, but only accept pre-formatted files specific to this program.
    public static final String taxReportFileName = "taxreport.txt";
    //Pattern to capture the worded heading in the title/header of the file
    public static final String wordedHeaderPattern = "[a-zA-Z\\s]*";
    //Pattern to capture the four digit employee id for example ("1234" or "0012")
    private static final String employeeIdPattern = "\\d{4}";
    //Pattern to capture the dollar value amount for example ("123123.00...etc" or "0.00")
    private static final String amountValuePattern = "((\\d{1,3}([,\\.])?)*)";
    //Pattern to capture the space between numbers in the table
    private static final String spacePattern = "(\\s)+";


    /**
     * Method to write to the taxreport.txt file with the employeeid, employeeincome and totaltax calculated.
     *
     * @param employeeId     int 4 digit employeeId
     * @param employeeIncome double the income for the employee of employeeId
     * @param totalTax       double calculated total tax as double
     * @return boolean indicating whether the file was written successfully back to the caller.
     */
    public static boolean writeTaxReportEntry(int employeeId, double employeeIncome, double totalTax) {
        //Initiate the boolean indicator to return
        boolean writtenToFile = false;

        //Create a File object to use for obtaining a full path.
        File taxReportFile = new File(taxReportFileName);
        //Absolute path.
        String taxReportFilePath = taxReportFile.getAbsolutePath();

        boolean fileAlreadyExisted = taxReportFile.exists();

        /*
            Try with resources block to open the fileoutputstream to the taxreport.txt file with append mode set on in
            order to be able to write to the file adding to existing records.
        */

        try (FileOutputStream fileOutputStream = new FileOutputStream(taxReportFilePath, true)) {
            PrintWriter printWriter = new PrintWriter(fileOutputStream);

            //If the taxreport.txt file did not already exist, add the title to it to meet the assignment criteria:
            if (!fileAlreadyExisted) {
                printWriter.println("Employee ID    Taxable Income    Tax    ");
            }

            //For the employeeId use padding up to 4 spaces in case the user id contains leading 000.
            String employeeIdString = String.format("%04d", employeeId);
            //Set them all to strings, and use String.format to get up to two decimal places for the doubles to match the assignment table.
            String employeeIncomeString = String.format("%.2f", employeeIncome);
            String totalTaxString = String.format("%.2f", totalTax);

            printWriter.println(employeeIdString +
                    "    " + employeeIncomeString +
                    "    " + totalTaxString);

            writtenToFile = true;
            //Flush the printWriter to force the file to write.
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return writtenToFile;
    }

    /**
     * Method to read the taxreport file using regex patterns to find in lines and create a TaxReportEntry for each
     * entry identified. This method allows the conversion from text file to TaxReportEntry objects.
     *
     * @param pathName String path to file (current working directory or absolute) that contains the tax reports the file is hardcoded
     *                 to be called taxreports.txt
     * @return LinkedList<TaxReportEntry> list of TaxReportEntry objects containing the information corresponding to the
     * employeeID as well as taxable income and total tax.
     * @throws FileNotFoundException
     */
    public static LinkedList<TaxReportEntry> readTaxReportEntries(String pathName) throws FileNotFoundException {
        //Prepare the list of taxReportEntries
        LinkedList<TaxReportEntry> taxReportEntries = new LinkedList<>();

        File taxReportFile = new File(pathName);
        FileInputStream fileInputStream = new FileInputStream(taxReportFile.getAbsolutePath());
        Scanner scanner = new Scanner(fileInputStream);

        //Repeat while there is still lines, as each line represents a tax report record entry.
        while (scanner.hasNextLine()) {

            //If line is not blank (no non-whitespace characters in line) (the following negative lookahead does not consume)
            if (scanner.findInLine("(?=\\S)") != null) {

                //If the line has a worded pattern
                if (scanner.hasNext(wordedHeaderPattern)) {
                    //Skip first line if it includes letters as it is the heading line for the table
                    scanner.nextLine();
                }

                //Check for employee id
                String employeeIdString = scanner.findInLine(employeeIdPattern);
                int employeeId = Integer.valueOf(employeeIdString);
                //Consume spaces to skip
                scanner.findInLine(spacePattern);

                //Check for and retrieve the amount for income
                String taxableIncomeString = scanner.findInLine(amountValuePattern);
                double employeeIncome = Double.valueOf(taxableIncomeString);
                //Consume spaces to skip
                scanner.findInLine(spacePattern);

                //Check for and retrieve the amount for tax
                String totalTaxString = scanner.findInLine(amountValuePattern);
                double totalTax = Double.valueOf(totalTaxString);

                //Save the record found in file to TaxReportEntry object for internal-program usage.
                TaxReportEntry taxReportEntry = new TaxReportEntry(employeeId, employeeIncome, totalTax);
                //Add the taxReportEntry object to the array to be returned
                taxReportEntries.add(taxReportEntry);

                // After processing this line, advance to the next line
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                } else {
                    //If it is end of file break loop.
                    break;
                }

            } else {
                //Skip line because the line is just whitespace type of characters.
                scanner.nextLine();
            }
        }

        return taxReportEntries;
    }

}

/**
 * TaxReportEntry Object used for storing employeeId, employeeIncome and Tax amounts
 */
class TaxReportEntry {

    private int employeeId = 0;
    private double employeeIncome = 0.0;
    private double tax = 0.0;

    public TaxReportEntry(int employeeId, double employeeIncome, double tax) {
        this.employeeId = employeeId;
        this.employeeIncome = employeeIncome;
        this.tax = tax;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public double getEmployeeIncome() {
        return employeeIncome;
    }

    public double getTax() {
        return tax;
    }

    //Method override toString() used mainly for printing the object to display to the user.
    @Override
    public String toString() {
        String employeeIdString = String.format("%04d", employeeId);
        String employeeIncomeString = String.format("%.2f", employeeIncome);
        String totalTaxString = String.format("%.2f", tax);
        return "For Employee ID: " + employeeIdString + "" +
                "\nIncome is: $" + employeeIncomeString + "" +
                "\nTax on that income was: $" + totalTaxString;
    }
}