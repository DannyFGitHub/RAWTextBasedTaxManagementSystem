import java.io.FileNotFoundException;
import java.util.LinkedHashSet;

/**
 * TaxCalculator is in charge of calculating Tax and progressing the user through calculating tax for different
 * employee ids.
 */
public class TaxCalculator {

    /**
     * Method that manages the Tax Calculations and controls user interactions. It checks for taxrates.txt and if its
     * not found it will prompt the user in order to get a path name for where it should look instead.
     */
    public static void calculateTaxProcess() {
        String taxRateFileName = "taxrates.txt";
        LinkedHashSet<TaxRate> taxRates = null;
        try {
            taxRates = TaxRateFileReader.readTaxRatesFile();
            System.out.println("[ Tax Rates ] : File Loaded Successfully");
        } catch (FileNotFoundException e) {
            System.out.println("[ Tax Rates ] Issue : Tax Rate File was not found. " + taxRateFileName);
            String newTaxRateFilePath = TaxSystemUI.promptForPath("\nPlease provide the Tax Rate file. It should be called \"" + taxRateFileName + "\"", taxRateFileName);
            try {
                taxRates = TaxRateFileReader.readTaxRatesFile(newTaxRateFilePath);
                System.out.println("[ Tax Rates ] File Loaded Successfully");
            } catch (Exception ex) {
                //Warn user that the file was not read properly as the file or its contents may be invalid.
                System.out.println("[ Tax Rates ] Issue : The file could not be loaded successfully, please make sure the file contains valid tax rate information.");
                //Return void to prevent further execution.
                return;
            }
        }
        if (taxRates != null) {
            do {
                //Prompt user for id
                int employeeId = TaxSystemUI.promptForID("\nPlease enter the four digit Employee ID to calculate tax based on income:");
                //Prompt user for income
                double employeeIncome = TaxSystemUI.promptForIncome(employeeId);
                //Calculate Tax
                double totalTax = taxCalculator(taxRates, employeeIncome);


                //Display to user with proper formatting:

                // Employee Id is displayed with padded zeros as a user with 0001 is a valid Id and must be displayed that way.
                System.out.printf("\nFor Employee ID: %04d", employeeId);
                //Rounding off to nearest two decimal places to correlate with output of assignment criteria.
                System.out.printf("\nWith Income: $%.2f", employeeIncome);
                //Rounding off to nearest two decimal places to correlate with output of assignment criteria.
                System.out.printf("\nThe total tax is: $%.2f", totalTax);
                System.out.println();

                //write To file - if the taxreport.txt is written successfully notify the user
                boolean writtenToFile = TaxReportFileManager.writeTaxReportEntry(employeeId, employeeIncome, totalTax);
                if (writtenToFile) {
                    System.out.println("\n>[]< Record written to File.");
                    System.out.println();
                }
                //Use the TaxSystemUI function for yes or no answer.
            } while (TaxSystemUI.promptUserYesOrNo("\n----!!! Calculate Tax for Another Employee?"));
        }
    }

    /**
     * Calculates tax using the TaxRates
     *
     * @param taxRates
     * @param income
     * @return
     */
    public static double taxCalculator(LinkedHashSet<TaxRate> taxRates, double income) {
        //Initiate value as 0
        double totalTax = 0.0;
        //Iterate through tax rates to find where the income sits.
        for (TaxRate taxRate : taxRates) {
            if (income >= taxRate.getLowerThreshold() && income <= taxRate.getHigherThreshold()) {

                //Print the taxRate
                System.out.println("\n" + taxRate + "\n");

                //Get the base tax to be charged for being in the threshold bracket
                totalTax = taxRate.getBaseTax();
                if (taxRate.getRateCents() != 0) {
                    //Perform the calculation using the formula in the assignment criteria.
                    totalTax = +(income - taxRate.getRateThreshold()) * (taxRate.getRateCents() / 100);
                }
            }
        }
        return totalTax;
    }

}
