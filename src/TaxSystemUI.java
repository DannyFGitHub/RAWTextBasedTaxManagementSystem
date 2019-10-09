import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * TaxSystemUI is the multi purpose class to prepare and validate user input and output to display (console/terminal).
 */
public class TaxSystemUI {

    /**
     * Simple welcome printer. Separated for looping purposes. That way printMenu can be called and the welcome is not
     * spammed for the user.
     * Additionally, the method was added here as this is the class in charge of UI printing that is not a custom error or user warnings.
     */
    public static void printWelcome() {
        System.out.println("Welcome to Tax Management System of XYZ");
    }

    /**
     * Simple Menu Printer
     */
    public static void printMenu() {
        System.out.println("\n_________[ MAIN MENU ]_______" +
                "\n\n" +
                "Please select one of the following options:" +
                "\n" +
                "1. Calculate Tax" +
                "\n" +
                "2. Search Tax" +
                "\n" +
                "3. Exit" +
                "\n");
    }

    /**
     * Prompt user for menu choice and validate input.
     *
     * @return int Integer corresponding to user's choice.
     */
    public static int promptUserChoice() {

        //Possible Choices
        int[] options = new int[]{1, 2, 3};
        //Initiating variable
        int userChoice = 0;

        Scanner scanner = new Scanner(System.in);

        //Loop until valid input is given.
        while (userChoice == 0) {
            System.out.println("Please type either 1, 2 or 3 then press Enter:");
            System.out.print("--> ");

            try {
                int tempChoice = scanner.nextInt();
                if ((tempChoice + "").length() > 1) {
                    System.out.println("[ Invalid Input ] : Valued entered has more than one number.");
                    userChoice = 0;
                } else {
                    userChoice = tempChoice;
                }
                for (int i = 0; i < options.length; i++) {
                    if (userChoice == options[i]) {
                        return userChoice;
                    }
                }
            } catch (Exception incorrectChoice) {
                userChoice = 0;
                System.out.println("[ Invalid Input ] : The input was invalid. ");
            }

            //Consume next to reset the scanner
            scanner.nextLine();
        }

        return userChoice;
    }

    /**
     * UI method to display a simple prompt with a provided String prompt and input validation for
     * an existing File's filepath (additionally checking if it exists). This allows the prompt code to be written once.
     *
     * @param prompt             String variable with the text to display to the user when running this prompt.
     * @param fileNameConstraint String containing the file name that the method is looking for, if the filePath
     *                           provided by the user does not contain this string it will be rejected. This is done
     *                           to make sure the user is aware of which file we are asking for. The user is expected
     *                           to rename the file if necessary.
     * @return String with the file path to the file that the user has submitted.
     */
    public static String promptForPath(String prompt, String fileNameConstraint) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(prompt + "\nPlease provide full valid file path, an absolute or current working directory path may be used (Absolute recommended):");
            System.out.print("--> ");
            String inputFilePath = scanner.nextLine();

            try {
                File taxRatesFile = new File(inputFilePath);
                //Check if file exists
                if (taxRatesFile.exists()) {
                    //Check if the filename of the file is what we wanted.
                    if (taxRatesFile.getName().matches(fileNameConstraint)) {
                        return taxRatesFile.getAbsolutePath();
                    } else {
                        System.out.println("[ Invalid File ] The file you provided is valid, but it's not named correctly.");
                        System.out.println("[ Invalid File ] The file should be called: " + fileNameConstraint);
                    }
                } else {
                    System.out.println("[ Invalid File Path ] The path \"" + taxRatesFile.getAbsolutePath() + "\" was not found.");
                }
            } catch (InvalidPathException invalidPathError) {
                System.out.println("[ Invalid File Path ] The path provided was not valid, please check that you are entering the file path correctly.");
                System.out.println("[ Invalid File Path ] Your input was: " + inputFilePath);
            }
        }
    }

    /**
     * UI Method to display simple Y or N prompt with input validation. This allows the prompt code to be written once.
     * Prompt user for Y or N question. Takes custom prompt as String and displays it to user. It was created separately, static
     * and generically as possible to allow to be used for multiple purposes where yes or no input is required.
     *
     * @return boolean representing user's choice.
     */
    public static boolean promptUserYesOrNo(String prompt) {
        Scanner scanner = new Scanner(System.in);

        //Loop until valid input is given.
        while (true) {
            System.out.println(prompt + "\nPlease enter either Y (for yes) or N (for No):");
            System.out.print("--> ");

            try {
                String tempChoice = scanner.next();
                if ((tempChoice + "").length() > 1) {
                    System.out.println("[ Invalid Input ] : Valued entered has more than one letter.");
                } else {
                    //If it contains "y" after making the text lowercase to match to catch both uppercase and lowercase
                    if (tempChoice.trim().toLowerCase().contains("y")) {
                        return true;
                        //If it contains "n" after making the text lowercase to match to catch both uppercase and lowercase
                    } else if (tempChoice.trim().toLowerCase().contains("n")) {
                        return false;
                    } else {
                        System.out.println("[ Invalid Input ] : The input was invalid. ");
                    }
                }
            } catch (Exception incorrectChoice) {
                System.out.println("[ Invalid Input ] : The input was invalid. ");
            }

            //Consume next to reset the scanner
            scanner.nextLine();
        }


    }

    /**
     * UI method to display simple prompt for employee ID and validate input to match 4 digit number. Created static and separately as it is used by multiple Classes.
     *
     * @param prompt String prompt wording to display to the user for requesting the 4 digit number.
     * @return int expected returned is 4 digit code.
     */
    public static int promptForID(String prompt) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(prompt);
            System.out.print("--> ");
            try {
                String employeeId = scanner.next("(\\s?)\\d{4}(\\s?)");
                return Integer.valueOf(employeeId);
            } catch (InputMismatchException formatMismatch) {
                System.out.println("[ Invalid Input ] : The id provided was invalid. Please make sure it is a 4 digit integer.");
            }
            scanner.nextLine();
        }
    }

    /**
     * UI method to display simple prompt for the user to enter 4 digit employee id using a regex to validate input and
     * cleaning up the string to parse and return a double
     *
     * @param employeeIDToMentionInPrompt int value to be shown to the user as the employee id for which the income is required for.
     * @return double value of the dollar value of the income that the user has provided.
     */
    public static double promptForIncome(int employeeIDToMentionInPrompt) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nPlease enter the INCOME for the employee with employee id: " + String.format("%04d", employeeIDToMentionInPrompt) + " to calculate tax based on income:");
            System.out.print("--> ");
            try {
                //Get the next dollar value of income from user input. Valid includes: $12,000 12000 12,000.00
                String employeeId = scanner.next("(\\$?)(\\d{1,3}([,\\.])?)*");
                //Remove anything that's not 0 to 9 and a decimal point and convert it to a double
                return Double.valueOf(employeeId.trim().replaceAll("[^0-9\\.]", ""));
            } catch (InputMismatchException formatMismatch) {
                System.out.println("[ Invalid Input ] : The number provided was invalid. Please check that it is a valid amount and try again.");
            }
            scanner.nextLine();
        }
    }

}
