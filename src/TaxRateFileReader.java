import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TaxRateFileReader is the class used for reading the file and converting it to a LinkedHashSet of TaxRate objects
 * on which we can perform calculations with. This is done so rather than reading the file every time the calculations
 * are performed, the file is loaded into the program, and then calculations are performed over the Set of Tax Rates.
 */
public class TaxRateFileReader {

    //Pattern to capture the threshold for example ("$123,123...etc - $123,123...etc")
    private static final Pattern thresholdPattern = Pattern.compile("^(((\\s*)?)(\\$?)((\\s?)*)(\\d{1,3}([,\\.])?)*((\\s?)(\\p{Pd})(\\s?)(\\$?)((\\s?)*)(\\d{1,3}([,\\.])?)*|(\\s?)and(\\s?)over))");
    //Pattern to capture the base amount for example ("$123,123...etc" or "0")
    private static final Pattern baseAmountPattern = Pattern.compile("(\\$?)(\\d{1,3}([,\\.])?)*(\\s?)plus|\\s(\\$?)(\\s[^over]\\d{1,3}([,\\.])?)*$");
    //Pattern to Capture the rate for the bracket ("123.1231c for each $1 over $123,123,etc"...)
    private static final Pattern ratesAmountPattern = Pattern.compile("(\\d{1,3}([,\\.])?)*c for each \\$1 over (\\$?)(\\d{1,3}([,\\.])?)*$");


    /**
     * Simply overloads and calls the readTaxRatesFile method injecting the pathname as "taxrates.txt" if no pathnames were provided.
     *
     * @return LinkedHashSet<TaxRate> a collection of TaxRate objects as a LinkedHashSet as order and uniqueness is required.
     * @throws FileNotFoundException error thrown in case that the file is not found, should be used by the caller as indication of invalid path.
     */
    public static LinkedHashSet<TaxRate> readTaxRatesFile() throws FileNotFoundException {
        //If no file path is provided, look for taxrates.txt in the current directory.
        return readTaxRatesFile("taxrates.txt");
    }

    /**
     * Function to read the taxrates.txt file given in the String pathName, to read the file and return a TaxRate LinkedHashSet with all the tax rates as objects
     *
     * @param pathName String given to indicate the file path (absolute or current working directory) of the taxrates.txt file.
     * @return LinkedHashSet<TaxRate> a collection of TaxRate objects as a LinkedHashSet as order and uniqueness is required.
     * @throws FileNotFoundException error thrown in case that the file is not found, should be used by the caller as indication of invalid path.
     */
    public static LinkedHashSet<TaxRate> readTaxRatesFile(String pathName) throws FileNotFoundException {

        //Use a LinkedHashSet (due to only needing unique values/no duplicates in the set of rates)
        LinkedHashSet<TaxRate> taxRates = new LinkedHashSet<>();

        File taxRatesFile = new File(pathName);
        FileInputStream fileInputStream = new FileInputStream(taxRatesFile.getAbsolutePath());
        Scanner scanner = new Scanner(fileInputStream);

        //read each line of the file until the end
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //process the line and convert to a TaxRate object
            TaxRate taxRateObject = fileLineToTaxRate(line);
            //Only if the TaxRate object was successfully created from the line add it to Tax Rates Set.
            if (taxRateObject != null) {
                taxRates.add(taxRateObject);
            }
        }

        return taxRates;
    }

    /**
     * Function to convert a line of text to a TaxRate object
     *
     *
     * @param line String This should be the raw line read from the file following the format and expectations discussed in the submitted discussion.
     * @return TaxRate or Null It will return TaxRate if a taxrate threshold was successfully read and provided. It will return null if there is no threshold data found (e.g. in the case of the heading)
     */
    public static TaxRate fileLineToTaxRate(String line) {

        TaxRate taxRate = null;

            double lowerThreshold = 0;
            double higherThreshold = 0;
            double baseTax = 0;
            double rateCents = 0;
            double rateThreshold = 0;

            Matcher matchThreshold = thresholdPattern.matcher(line);
            if (matchThreshold.find()) {
                String[] minAndMaxThresholds = matchThreshold.group().split("(\\p{Pd})|and");

                if (minAndMaxThresholds.length < 2) {
                    //There is no valid threshold in this line.
                    return null;
                }

                //Get Lower Threshold as Double
                String minThreshold = minAndMaxThresholds[0].trim().replaceAll("[^0-9\\.]", "");
                lowerThreshold = Double.valueOf(minThreshold);

                //Get Higher Threshold as Double
                if (minAndMaxThresholds[1].contains("over")) {
                    higherThreshold = -1;
                } else {
                    String maxThreshold = minAndMaxThresholds[1].trim().replaceAll("[^0-9\\.]", "");
                    higherThreshold = Double.valueOf(maxThreshold);
                }

                //Continue within Find Loop of the Threshold bracket:

                //Get the Bracket Base Amount if any
                Matcher matchStaticAmount = baseAmountPattern.matcher(line);
                //If it finds a match
                if (matchStaticAmount.find()) {
                    //Clean up the match to only contain valid numeric characters
                    String baseAmountString = matchStaticAmount.group().trim().replaceAll("(\\s?)plus", "").replaceAll("[^0-9\\.]", "");
                    //Convert to double
                    baseTax = Double.valueOf(baseAmountString);
                }

                //Get the rate if any
                Matcher ratesAmountThreshold = ratesAmountPattern.matcher(line);
                //If it finds a match
                if (ratesAmountThreshold.find()) {
                    //Split the rates into cents and cents rate lower limit for the bracket
                    String[] ratesAmount = ratesAmountThreshold.group().split("(\\s?)c for each \\$1 over (\\s?)");
                    //Get the first cents string
                    String centRateString = ratesAmount[0].trim().replaceAll("[^0-9\\.]", "");
                    //Convert to double
                    rateCents = Double.valueOf(centRateString);
                    //Get the second string containing the bracket tax threshold limit
                    String rateThresholdString = ratesAmount[1].trim().replaceAll("[^0-9\\.]", "");
                    //Convert to double
                    rateThreshold = Double.valueOf(rateThresholdString);
                }

                //Populate and create a TaxRate object with the data read from the line.
                taxRate = new TaxRate(lowerThreshold, higherThreshold, baseTax, rateCents, rateThreshold);

            } else {
                //Return null as there is no threshold found in this line.
                return null;
            }

        //Return the data in the form of the TaxRate object.
        return taxRate;
    }

}
