package il.ac.kinneret.mjmay.smtp.model;

public class SharedData {
    private static StringBuilder stringBuilder =  null;

    public static StringBuilder getSB()
    {
        if ( stringBuilder == null)
        {
            stringBuilder = new StringBuilder();
        }
        return stringBuilder;
    }

    public static void clearSB()
    {
        stringBuilder = new StringBuilder();
    }
}