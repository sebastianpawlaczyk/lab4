package currency;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//fasfsafas
public class Currency
{
    private String name;
    private String code;
    private double rate;
    private int units;

    public Currency(String line) throws Exception
    {
        Pattern pattern = Pattern.compile("currency=\"(\\w+).+units=\"(\\d+).+code=\"(\\w+).+>([-+]?[0-9]*\\.?[0-9]+)");
        Matcher matcher = pattern.matcher(line);

        if (!matcher.find())
        {
            throw new Exception();
        }

        name = matcher.group(1);
        code = matcher.group(3);

        try
        {
            units = Integer.parseInt(matcher.group(2));
            rate = Double.parseDouble(matcher.group(4));
        }
        catch (Exception ex)
        {
            throw new Exception();
        }
    }

    public double getRate()
    {
        return rate;
    }
    public String getCode() { return code; }

    public String toString()
    {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        nf.setMaximumFractionDigits(4);
        String result = code + "\t[" + units + "]\t" + nf.format(rate);
        return result;
    }

    public static void main(String args[]) throws Exception
    {
        Currency c = new Currency("<mid-rate currency=\"Euro\" units=\"1\" code=\"EUR\">4.2966</mid-rate>");
        System.out.println(c);
    }
}
