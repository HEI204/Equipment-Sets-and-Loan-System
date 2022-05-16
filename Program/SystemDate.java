public class SystemDate extends Day {
    private static SystemDate instance;

    private SystemDate(String sDay) { super(sDay); }
    
    
    public static SystemDate getInstance() { return instance; }

    public static void createTheInstance(String sDay)
    {
        if (instance == null)
            instance = new SystemDate(sDay);
        else
            System.out.println("Cannot create one more system date instance");
    }

    public static void checkValidSystemDate(String sDay) throws ExInvalidDateFormat
    {
        // Check the string is in correct date format first
        instance.checkDateFormat(sDay);
        // New day should be later than the current day
		if (instance.compareTo(new Day(sDay)) == 1)
                throw new ExInvalidDateFormat("Invalid new day.  The new day has to be later than the current date " + instance.toString() + ".");
    }
}
