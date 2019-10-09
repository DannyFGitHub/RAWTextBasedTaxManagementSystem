/**
 * @Author Danny Falero
 * Tax Management System
 * This system allows a user to calculate tax based on a taxrates.txt text file for tax rates reference
 * and saves the records to the taxreport.txt text file for future retrieval.
 * The program also allows the user to search through the saved records in the taxreport.txt returning the latest
 * matching entry for the employee ID.
 */
public class TaxManagementSystem {


    /**
     * Main Program Begins here.
     *
     * @param args String[] cli arguments
     */
    public static void main(String[] args) {
        //Value to control loop.
        boolean stayInProgram = true;

        //Print the welcome screen
        TaxSystemUI.printWelcome();

        while (stayInProgram) {

            //UI Print main menu
            TaxSystemUI.printMenu();

            switch (TaxSystemUI.promptUserChoice()) {
                case 1:
                    //Option 1 : Calculate Tax
                    TaxCalculator.calculateTaxProcess();
                    break;
                case 2:
                    //Option 2 : Search Tax
                    TaxReportSearch.searchTaxReports();
                    break;
                case 3:
                    //Option 3 : Exit
                    if (TaxSystemUI.promptUserYesOrNo("\n-----!!! Are you sure you want to quit?")) {
                        System.out.println("\n===== Goodbye. =====");
                        //End Main While Loop
                        stayInProgram = false;
                    }
                    break;
            }

        }
    }


}
