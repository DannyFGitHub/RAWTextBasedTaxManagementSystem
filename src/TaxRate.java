public class TaxRate {

    private double lowerThreshold = 0;
    private double higherThreshold = 0;
    private double baseTax = 0;
    private double rateCents = 0;
    private double rateThreshold = 0;

    /**
     * Constructor to build a TaxRate object with arguments.
     * @param lowerThreshold double This is the lower threshold of the Tax Rate bracket.
     * @param higherThreshold double This is the higher threshold of the Tax Rate Bracket.
     * @param baseTax double This is the set amount that you will be charged for being within the brackets.
     * @param rateCents double This is the amount of cents per dollar to be charged if the rateThreshold is exceeded.
     * @param rateThreshold double This is the amount that once exceeded the rateCents will begin to charge per dollar exceeded.
     */
    public TaxRate(double lowerThreshold, double higherThreshold, double baseTax, double rateCents, double rateThreshold) {
        this.lowerThreshold = lowerThreshold;
        this.higherThreshold = higherThreshold;
        this.baseTax = baseTax;
        this.rateCents = rateCents;
        this.rateThreshold = rateThreshold;
    }

    /**
     * Get Lower Threshold
     *
     * @return double the lower threshold as a double in dollar value
     */
    public double getLowerThreshold() {
        return lowerThreshold;
    }

    /**
     * Get Higher Threshold (if it was set up as -1 it will return the Highest Double value possible AKA  Double.MAX_VALUE)
     *
     * @return double the higher threshold as a double in dollar value
     */
    public double getHigherThreshold() {
        if (higherThreshold == -1) {
            return Double.MAX_VALUE;
        } else {
            return higherThreshold;
        }
    }

    /**
     * Get Bracket Base Tax
     *
     * @return double the base tax for the bracket as a double in dollar value
     */
    public double getBaseTax() {
        return baseTax;
    }

    /**
     * Get Cent of the rate per bracket
     *
     * @return double the rate for the bracket as a double in cents
     */
    public double getRateCents() {
        return rateCents;
    }

    /**
     * Get rate threshold limit of the bracket
     *
     * @return double the rate threshold to which apply the rate cents to, as a double in dollar value
     */
    public double getRateThreshold() {
        return rateThreshold;
    }

    /**
     * Overriding toString() here in order to be able to print to user with a customer String with information
     * of the TaxRate instance.
     * @return String This is the string to be displayed to the user with information about the Tax Rate.
     */
    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        //If higherThreshold is equal to -1 then it is above the lowerThreshold indefinitely
        if (higherThreshold == -1) {
            stringBuilder.append("For the threshold $" + lowerThreshold + " and above");
        } else {
            stringBuilder.append("For the threshold $" + lowerThreshold + " to $" + higherThreshold);
        }

        stringBuilder.append(" the base tax rate is: $" + baseTax);

        if (rateCents != 0 && rateThreshold != 0) {
            stringBuilder.append(" plus " + rateCents + "c at the rate of every $1 over $" + rateThreshold);
        }
        return stringBuilder.toString();
    }

}