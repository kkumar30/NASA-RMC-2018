package data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Sensor
{
    private String name = "";
    private Double value = Double.NaN;

    public Sensor(){}

    public String getName() {return name;}
    public Double getValue() {return value;}

    public void updateSensorData(String sensorData)
    {
        final String patternStr = "(\\w+):(\\-?\\d+)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(sensorData);

        if(m.matches())
        {
            try
            {
                name = m.group(1);
                value = Double.parseDouble(m.group(2));
            }
            catch(Exception e)
            {
                System.out.println("Update Sensors failed due to malformed message");
            }
        }
        else
        {
            System.out.println("Update Sensor Actuals Failed!");
        }


    }
}
