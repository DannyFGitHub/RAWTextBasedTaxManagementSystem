import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * TaxReportSearch class takes care of reading the taxreports.txt file and performing searches over the data.
 * It loads the data read from the file onto a LinkedList of TaxReportEntry objects (LinkedList chosen due to the
 * ability to grow and maintain order) (Order is important as earliest and latest entries must be kept in order for
 * retrieval purposes in case one employee ID has multiple records).
 */
public class TaxReportSearch {

    public static final String taxReportFileName = "taxreport.txt";

    /**
     * Search Tax Rerports main method with input validation provided by the TaxSystemUI class and file check prior to processing.
     */
    public static void searchTaxReports() {
        LinkedList<TaxReportEntry> taxReportEntries = null;

        try {
            taxReportEntries = TaxReportFileManager.readTaxReportEntries(taxReportFileName);
            System.out.println("[ Tax Reports ] File Loaded Successfully");
        } catch (FileNotFoundException e) {
            //File taxreport.txt was not found in the current working directory. Prompt the user to find it and provide the path (relative or absolute)
            System.out.println("[ Tax Reports ] Issue : Tax Reports File was not found. " + taxReportFileName);
            String newTaxReportFilePath = TaxSystemUI.promptForPath("\nPlease provide the Tax Report file. It should be called \"" + taxReportFileName + "\"", taxReportFileName);
            try {
                taxReportEntries = TaxReportFileManager.readTaxReportEntries(newTaxReportFilePath);
                System.out.println("[ Tax Reports ] File Loaded Successfully");
            } catch (Exception ex) {
                //Warn user that the file was not read properly as the file or its contents may be invalid.
                System.out.println("[ Tax Reports ] Issue : The file could not be loaded successfully, please make sure the file contains valid tax report information, including the corresponding headers." +
                        "\n\"Employee ID   Taxable Income   Tax\"");
                //Return void to prevent further method execution until user reviews the file and tries again.
                return;
            }
        }

        //If the file was read correctly, and the entries is not null (the LinkedList was initiated and assigned)
        if (taxReportEntries != null) {
            //Reverse the entries (Important step in order to be able to easily search backwards) another option is to use a doubly linked list.
            Collections.reverse(taxReportEntries);
            do {
                //Prompt user for the employee ID to search for:
                int employeeId = TaxSystemUI.promptForID("\nPlease enter the four digit Employee ID to SEARCH for tax reports based on income:");

                //Call the searchID function, it will either return an TaxReportEntry object or null
                TaxReportEntry taxReportEntryResult = searchID(taxReportEntries, employeeId);

                //If a result was returned and not null
                if (taxReportEntryResult != null) {
                    System.out.println("\n <[]> The following latest result was found: ");
                    //Display it for the user.
                    System.out.println("\n" + taxReportEntryResult);
                } else {
                    //If nothing was found show the warning.
                    System.out.println("\n <!!> The Employee ID: " + String.format("%04d", employeeId) + " was not found.");
                }
                //Give the option to loop, functionality provided by the TaxSystemUI class.
            } while (TaxSystemUI.promptUserYesOrNo("\n----!! Would you like to continue searching tax report records for employee Ids?"));
        }

    }

    /**
     * Search the TaxReportEntries LinkedList to find the TaxReportEntry that contains the employeeId that the user
     * is looking for. The LinkedList MUST be reversed before this method is called.
     *
     * @param reversedTaxReportEntries LinkedList<TaxReportEntry> list of TaxReportEntry files that must be initiated and loaded from file.
     * @param employeeId int number corresponding to the employee id of the employee to search and match.
     * @return TaxReportEntry or NULL TaxReportEntry returned indicates that a matching entry was found.
     */
    public static TaxReportEntry searchID(LinkedList<TaxReportEntry> reversedTaxReportEntries, int employeeId) {
        for (TaxReportEntry taxEntry : reversedTaxReportEntries) {
            if (taxEntry.getEmployeeId() == employeeId) {
                return taxEntry;
            }
        }
        return null;
    }
}
