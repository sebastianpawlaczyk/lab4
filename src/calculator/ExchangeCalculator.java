package calculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import currency.Currency;

public class ExchangeCalculator
{
    private Vector<Currency> rates = new Vector<Currency>();


    public void calculateRates()
    {
        Scanner scInput = new Scanner(System.in);
        while (scInput.hasNextLine())
        {
            String line = scInput.nextLine().trim();
            System.out.println(line);

            if (line.equalsIgnoreCase("exit"))
            {
                break;
            }

            double result = abc(line);
            System.out.println(line + "\t" + result);
        }
        scInput.close();
    }

    protected double calculate(String line)
    {
        Pattern pattern = Pattern.compile("(buy|sel)\\s+(\\d+)\\s+(\\w+)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches())
        {
            String typeOfTransaction = matcher.group(1);
            double amount = Double.parseDouble(matcher.group(2));
            String code = matcher.group(3);

            for (int i = 0; i < rates.size(); i++)
            {
                if (rates.get(i).getCode().equals(code))
                {
                    if (typeOfTransaction.equals("buy"))
                    {
                        return rates.get(i).getRate() * amount;
                    }

                    if (typeOfTransaction.equals("sel"))
                    {
                        return amount / rates.get(i).getRate();
                    }
                }
            }
        }
        return 0;
    }

    protected double abc(String line)
    {
        String [] splited = line.split(" ");

        if (splited.length != 3)
        {
            System.out.println("Wrong input -> format 'buy|sel 99999 USD'");
            return 0;
        }

        Pattern pattern = Pattern.compile("buy|sel");
        Matcher matcher = pattern.matcher(splited[0]);

        if (!matcher.matches())
        {
            System.out.println("Wrong input!!! -> choose buy|sel");
            return 0;
        }

        pattern = Pattern.compile("\\d+");
        matcher = pattern.matcher(splited[1]);

        if (!matcher.matches())
        {
            System.out.println("Wrong input!!! -> enter proper amount of money");
            return 0;
        }

        pattern = Pattern.compile("\\w+");
        matcher = pattern.matcher(splited[2]);

        if (!matcher.matches())
        {
            System.out.println("Wrong input!!! -> enter proper code");
            return 0;
        }

        for (int i = 0; i < rates.size(); i++)
        {
            if (rates.get(i).getCode().equals(splited[2]))
            {
                if (splited[0].equals("buy"))
                {
                    return rates.get(i).getRate() * Double.parseDouble(splited[1]);
                }

                else
                {
                    return Double.parseDouble(splited[1]) / rates.get(i).getRate();
                }
            }
        }
        System.out.println("Wrong input!!! -> wrong specific code");
        return 0;
    }

    public ExchangeCalculator(String fn) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(fn));
        while (sc.hasNextLine())
        {
            String line = sc.nextLine();
            try
            {
                Currency c = new Currency(line);
                rates.addElement(c);
            }
            catch (Exception ex)
            {}
        }
        sc.close();
    }

    public static void main(String args[])
    {
        String fn = "ExchangeRates.txt";
        ExchangeCalculator calc = null;
        try
        {
            calc = new ExchangeCalculator(fn);
        } catch (FileNotFoundException e){
            System.err.println("Unable to read " + fn);
            System.exit(1);
        }
        calc.calculateRates();
        System.exit(0);
    }
}
