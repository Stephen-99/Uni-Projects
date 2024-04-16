package com.example.assignment;


import java.util.Arrays;
import java.util.List;

//data largely taken from https://knoema.com/atlas/ranks, and just googling interesting facts about each country
public class FlagData {
    private List<Flag> flagList = Arrays.asList(
            new Flag(R.drawable.flag_af, Arrays.asList(
                    new Question(10, 5, false, "Q1", "What is the population of Afghanistan?", "38 Million", Arrays.asList("32 Million", "27 Million", "34 Million")),
                    new Question(15, 10, false, "Q2", "The GDP in Afghanistan is $18.7 Billion US?", "True", Arrays.asList("False")),
                    new Question(12, 10, true, "Q3", "What is the percentage of people in afghanistan with a mobile phone?", "59", Arrays.asList("82", "43", "71")),
                    new Question(16, 5, false, "Q4", "What is the military expense for Afghanistan over the past year? (Millions USD)", "227", Arrays.asList("312", "145")),
                    new Question(21, 11, false, "Q5", "The homicide rate in Afghanistan is 6.66.", "True", Arrays.asList("False"))
            )),
            new Flag(R.drawable.flag_ar, Arrays.asList(
                    new Question(10, 5, false, "Q1", "What is the population of Argentina?", "45 Million", Arrays.asList("47 Million", "44 Million", "46 Million")),
                    new Question(12, 4, false, "Q2", "What is the percentage of people with mobile phone subscription in Argentina?", "132", Arrays.asList("127", "121")),
                    new Question(17, 7, true, "Q3", "The unemployment rate in Argentina is 6.7%", "False", Arrays.asList("True")),
                    new Question(6, 5, false, "Q4", "What is the average life expectancy in Argentina? (Years)", "76.7", Arrays.asList("86.7", "83.7")),
                    new Question(20, 12, false, "Q5", "What is Argentina's military expenditure for the past year? (Billion USD)", "3.1", Arrays.asList("2.1", "4.1")),
                    new Question(8, 5, false, "Q6", "Argentina has the highest rates of movie watching in the world.", "True", Arrays.asList("False"))
            )),
            new Flag(R.drawable.flag_au, Arrays.asList(
                    new Question(22, 17, false, "Q1", "What is the unemployment rate in Australia (%)?", "5.18", Arrays.asList("3.24", "9.73")),
                    new Question(31, 25, true, "Q2", "What is Australia's population? (Millions)", "25.7", Arrays.asList("23.7", "24.7", "26.7")),
                    new Question(6, 3, false, "Q3", "What is Australia's yearly military expenditure? (Billions USD)", "25.9", Arrays.asList("21.2")),
                    new Question(12, 5, false, "Q4", "What is the Homicide rate in Australia?", "0.89", Arrays.asList("1.39", "1.69", "1.09")),
                    new Question(15, 7, false, "Q5", "In Australia there is 105 mobile phone subscriptions for every 100 people.", "False", Arrays.asList("True")),
                    new Question(21, 15, true, "Q6", "What is the ratio fo sheep to people in Australia?", "3", Arrays.asList("2", "0.5", "0.75")),
                    new Question(14, 7, false, "Q7", "How many hours a day do Koalas sleep?", "20", Arrays.asList("10", "15", "5")),
                    new Question(18, 11, true, "Q8", "In what year was a ban put in place for saying the word \"mate\" in parliment?", "2005", Arrays.asList("2011", "1998", "2002")),
                    new Question(26, 17, false, "Q9", "Of the 25 most toxic snakes in the world, how many are found in Australia?", "21", Arrays.asList("24", "23", "22"))
            )),
            new Flag(R.drawable.flag_bd, Arrays.asList(
                    new Question(12, 3, true, "Q1", "What is the GDP (Billion US Dollars) of Belgium?", "518", Arrays.asList("618", "418", "718")),
                    new Question(17, 6, false, "Q2", "What is the average life expectancy in Belgium? (Years)", "81.7", Arrays.asList("79.2", "83.4", "80.1")),
                    new Question(9, 5, false, "Q3", "The population in Belgium is 11.5 Million.", "True", Arrays.asList("False")),
                    new Question(15, 8, false, "Q4", "The Military expenditure (Billion USD) of Belgium is:", "4.8", Arrays.asList("4.4", "3.2", "3.6")),
                    new Question(14, 5, false, "Q5", "The homicide rate of Belgium is:", "1.69", Arrays.asList("0.89", "0.56", "0.49"))
            )),
            new Flag(R.drawable.flag_br, Arrays.asList(
                    new Question(7, 3, false, "Q1", "Brazil is part of 4 different time zones.", "False", Arrays.asList("True")),
                    new Question(12, 11, true, "Q2", "How many mobile phone subscriptions are there for evey 100 people in Brazil?", "98.8", Arrays.asList("106.6", "102.5", "103.6")),
                    new Question(21, 10, false, "Q3", "What is the unemployment rate in Brazil?", "14.7", Arrays.asList("13.7", "12.7", "11.7")),
                    new Question(17, 9, false, "Q4", "What is the average life expectancy in Brazil? (Years)", "76.1", Arrays.asList("75.1", "77.1", "74.1")),
                    new Question(15, 11, false, "Q5", "What is the population in Brazil (Million)", "213", Arrays.asList("203", "233")),
                    new Question(11, 5, false, "Q6", "Brazil takes up what percentage of the South American Continent?", "47", Arrays.asList("36", "58")),
                    new Question(10, 4, false, "Q7","Brazil exports more than half of the world's coffee", "False", Arrays.asList("True"))
            )),
            new Flag(R.drawable.flag_ca, Arrays.asList(
                    new Question(8, 6, false, "Q1", "What is Canada's population (Millions of people)?", "37.5", Arrays.asList("27.5", "32.5", "47.5")),
                    new Question(12, 10, false, "Q2", "Canada's military spending in a year is 22 Billion USD", "True", Arrays.asList("False")),
                    new Question(17, 10, true, "Q3", "What is the homicide rate in Canada?", "1.76", Arrays.asList("1.63", "1.55", "1.59")),
                    new Question(20, 5, false, "Q4", "How many mobile phone subscriptions are there per 100 people?", "89.6", Arrays.asList("89.5", "89.7", "89.8")),
                    new Question(16, 9, false, "Q5", "Whay it the employment rate in Canada?", "92.5", Arrays.asList("90.5", "94.5", "96.5")),
                    new Question(21, 18, true, "Q6", "Compared to the rest of the world, how does Canada's oil reserves rank", "3rd", Arrays.asList("2nd", "4th", "5th")),
                    new Question(9, 4, false, "Q7", "How soon did Canada enter space compared to other countries?", "3rd", Arrays.asList("4th", "5th", "6th"))
            )),
            new Flag(R.drawable.flag_de, Arrays.asList(
                    new Question(16, 14, true, "Q1", "What is the unemployment rate in Germany?", "3.16", Arrays.asList("1.24", "7.88")),
                    new Question(12, 8, false, "Q2", "What is the life expectancy in Germany? (Years)", "81.4", Arrays.asList("82.3", "81.7")),
                    new Question(18, 12, false, "Q3", "Germany's Population (Million) is:", "83", Arrays.asList("82", "81", "84")),
                    new Question(9, 4, false, "Q4", "The yearly military expense (Billion USD) in Germany is:", "49", Arrays.asList("56")),
                    new Question(21, 11, false, "Q5", "The homicide rate in Germany is 0.92", "False", Arrays.asList("True")),
                    new Question(10, 5, false, "Q6", "Where does Germany rank worldwide in terms of GDP?", "4th", Arrays.asList("3rd", "5th", "6th"))
            )),
            new Flag(R.drawable.flag_dk, Arrays.asList(
                    new Question(27, 5, false, "Q1", "What is the GDP (Billion US Dollars) of Denmark?", "347.18", Arrays.asList("346.18", "345.18", "344.18")),
                    new Question(7, 2, false, "Q2", "The percentage of mobile phone subscriptions in Denmark isL", "125", Arrays.asList("136", "114", "103")),
                    new Question(12, 5, false, "Q3", "The unemployment rate in Denmark is:", "6.5", Arrays.asList("5.5", "5", "6.0")),
                    new Question(16, 7, false, "Q4", "What is the average life expectancy in Denmark (Years)?", "81", Arrays.asList("80", "79", "82")),
                    new Question(8, 8, true, "Q5", "What is the population in Denmark? (Million)", "5.8", Arrays.asList("4.8", "3.8", "2.8")),
                    new Question(8, 4, false, "Q6", "The Danish flag is the oldest in the world", "True", Arrays.asList("False"))
            )),
            new Flag(R.drawable.flag_es, Arrays.asList(
                    new Question(17, 4, false, "Q1", "What is the homicide rate in Spain?", "0.62", Arrays.asList("1.52", "1.92")),
                    new Question(18, 12, false, "Q2", "The yearly Military expense (Billion USD) in Spain", "17.1", Arrays.asList("18.1", "16.1", "15.1")),
                    new Question(14, 6, false, "Q3", "The number of mobile phone subscriptions per 100 people in Spain is:", "116", Arrays.asList("114", "113", "115")),
                    new Question(4, 1, false, "Q4", "The unemployment rate in spain is 12.2%", "False", Arrays.asList("True")),
                    new Question(3, 5, true, "Q5", "What is the average life expectancy in Spain? (Years)", "83.6", Arrays.asList("82.6", "81.6", "84.6"))
            )),
            new Flag(R.drawable.flag_fr, Arrays.asList(
                    new Question(12, 6, true, "Q1", "What is the ratio of mobile phone subscriptions to people in France?", "1.08", Arrays.asList("0.94", "1.23", "1.17")),
                    new Question(7, 5, false, "Q2", "The population (Million) in France is:", "67.2", Arrays.asList("72.1")),
                    new Question(16, 5, false, "Q3", "The yearly military expense (Billion USD) in France is:", "50", Arrays.asList("40", "30")),
                    new Question(13, 7, false, "Q4", "What is the homicide rate in France?", "1.2", Arrays.asList("0.8", "1.0", "0.6")),
                    new Question(22, 15, false, "Q5", "What is the employment rate in France?", "89.6", Arrays.asList("95.3", "87,5")),
                    new Question(10, 5, false, "Q6", "Ranking countries in Europe, by land mass, where does France rank?:", "3rd", Arrays.asList("1st", "2nd", "4th")),
                    new Question(17, 14, true, "Q7", "France is the most popular tourist destination worldwide.", "True", Arrays.asList("False")),
                    new Question(15, 7, false, "Q8", "where does the French rail network rank when compared to the rest of the world?", "9th", Arrays.asList("8th", "7th", "6th"))
            )),
            new Flag(R.drawable.flag_it, Arrays.asList(
                    new Question(24, 10, false, "Q1", "The unemployment rate in Italy is 12.7%", "True", Arrays.asList("False")),
                    new Question(10, 5, false, "Q2", "Average life expectancy in Italy is:", "83.5", Arrays.asList("82.5", "81.5")),
                    new Question(13, 12, true, "Q3", "Population (Million) in Italy is:", "60", Arrays.asList("57", "54")),
                    new Question(7, 5, false, "Q4", "Yearly Military expense (Billion USD) in Italy is:", "26.8", Arrays.asList("27.8", "25.8")),
                    new Question(17, 10, false, "Q5", "What is the homicide rate in Italy?", "0.57", Arrays.asList("0.89", "1.22")),
                    new Question(14, 6, false, "Q6", "Italy was under a dictatorship for 18 years", "False", Arrays.asList("True")),
                    new Question(21, 15, false, "Q7", "Of Europe's 3 volcanoes, how many are in Italy?", "3", Arrays.asList("2", "1")),
                    new Question(19, 11, true, "Q8", "Ranking countries by how often they are visited, where does Italy rank?:", "5th", Arrays.asList("1st", "7th")),
                    new Question(6, 4, false, "Q9", "Italy has the oldest population.", "False", Arrays.asList("True"))
            )),
            new Flag(R.drawable.flag_jp, Arrays.asList(
                    new Question(22, 15, true, "Q1", "Japan has the highest life expectancy of any country.", "False", Arrays.asList("True")),
                    new Question(14, 8, false, "Q2", "The percentage of mobile phone subscriptions per person in Japan is:", "141", Arrays.asList("131", "121", "111")),
                    new Question(8, 5, false, "Q3", "The unemployment rate in Japan is:", "3", Arrays.asList("6", "9", "12")),
                    new Question(12, 6, false, "Q4", "Average life expectancy in Japan is:", "84.65", Arrays.asList("83.65", "82.65", "85.65")),
                    new Question(17, 10, false, "Q5", "What is the population in Japan? (Millions)", "125.8", Arrays.asList("125.7", "125.6", "125.5"))
            )),
            new Flag(R.drawable.flag_mx, Arrays.asList(
                    new Question(8, 2, false, "Q1", "How much did Mexico spend on military last year? (Million USD)", "6.5", Arrays.asList("2.5", "78.5", "130.5")),
                    new Question(12, 6, false, "Q2", "The homicide rate in Mexico is 29.07.", "True", Arrays.asList("False")),
                    new Question(28, 28, true, "Q3", "The proportion of mobile phone subscriptions to people in Mexico is:", "0.95", Arrays.asList("0.98", "0.91")),
                    new Question(16, 9, false, "Q4", "What is the unemployment in Mexico", "5", Arrays.asList("10", "15", "20")),
                    new Question(19, 9, false, "Q5", "The average life expectancy in Mexico (Years) is:", "75", Arrays.asList("72", "69", "66")),
                    new Question(6, 2, false, "Q6", "Ranking countries by the height of their city capital, where is Mexico ranked?:", "8th", Arrays.asList("1st", "4th", "6th")),
                    new Question(9, 8, true, "Q7", "Mexico has the world's smallest volcano.", "True", Arrays.asList("False"))
            )),
            new Flag(R.drawable.flag_nl, Arrays.asList(
                    new Question(22, 8, false, "Q1", "What is the homicide rate in the Netherlands?", "0.59", Arrays.asList("0.60", "0.61", "0.62")),
                    new Question(12, 7, false, "Q2", "The population in the Netherlands (Millions) is:", "17", Arrays.asList("18", "16", "15")),
                    new Question(16, 10, false, "Q3", "The yearly Military expense (Billion USD) of the Netherlands is:", "12", Arrays.asList("13", "11")),
                    new Question(6, 2, false, "Q4", "In the Netherlands, for every 100 people, there is 124 mobile phone subscriptions.", "True", Arrays.asList("False")),
                    new Question(25, 20, true, "Q5", "What is the unemployment rate in the Netherlands?", "6.5", Arrays.asList("6.4", "6.3", "6.6")),
                    new Question(15, 5, false, "Q6", "Ranking countries by tallest men, the Netherlands is:", "1st", Arrays.asList("2nd", "3rd", "4th")),
                    new Question(21, 12, true, "Q7", "The Netherlands is the worlds largest flower exporter", "True", Arrays.asList("False")),
                    new Question(7, 4, false, "Q8", "Compared to sea level, the lowest point in the Netherlands is:", "-6.7m", Arrays.asList("-1.2m", "-5.8m", "-3.4m")),
                    new Question(14, 5, false, "Q9", "How many million kgs of licquorice do the dutch consume annually?:", "32", Arrays.asList("12", "42")),
                    new Question(28, 24, true, "Q10", "Ranking countries by population density, where does the Netherlands rank?", "1st", Arrays.asList("3rd", "5th", "7th"))
            )),
            new Flag(R.drawable.flag_uk, Arrays.asList(
                    new Question(9, 3, false, "Q1", "What is the proportion of mobile phone subscriptions to people in UK?", "1.18", Arrays.asList("1.21", "1.15", "1.11")),
                    new Question(11, 7, false, "Q2", "THe percentage unemployment in the UK is:", "4.8", Arrays.asList("5.2", "4.4")),
                    new Question(21, 9, false, "Q3", "The average life expectancy in the UK is:", "81.4", Arrays.asList("81.5", "81.3", "81.6")),
                    new Question(17, 15, true, "Q4", "The population of the UK (Millions) is:", "67", Arrays.asList("71", "58")),
                    new Question(7, 4, false, "Q5", "The yearly military expense (Billion USD) of the UK is:", "49", Arrays.asList("47", "45")),
                    new Question(24, 23, true, "Q6", "The homicide rate in the UK is:", "1.2", Arrays.asList("1.1", "1.3", "1.4")),
                    new Question(12, 5, false, "Q7", "How far away is the furthest point in the UK from the sea? (km)", "125", Arrays.asList("79", "167")),
                    new Question(8, 4, false, "Q8", "How high is the tallest mountain in the UK? (m)", "1345", Arrays.asList("1255", "2225"))
            )),
            new Flag(R.drawable.flag_us, Arrays.asList(
                    new Question(15, 7, true, "Q1", "What is the percentage of the labour force unemployed in the US?", "10.36", Arrays.asList("11.36", "9.36", "8.36")),
                    new Question(10, 5, false, "Q2", "The homicide rate in the USA is:", "4.96", Arrays.asList("3.96", "5.96", "6.96")),
                    new Question(9, 6, false, "Q3", "The average life expectancy in the USA is:", "79", Arrays.asList("77", "75", "73")),
                    new Question(17, 15, true, "Q4", "The population of the USA (Millions) is:", "330", Arrays.asList("320", "310")),
                    new Question(10, 5, false, "Q5", "The USA spends te most on military each year.", "True", Arrays.asList("False")),
                    new Question(27, 17, false, "Q6", "The proportion of mobile phone subscriptions to people in the USA is:", "1.29", Arrays.asList("1.19", "1.39", "1.09")),
                    new Question(12, 8, false, "Q7", "ranking countries by total area, USA is:", "4th", Arrays.asList("1st", "2nd", "3rd")),
                    new Question(21, 13, false, "Q8", "Ranking countries by largest river, the USA is:", "4th", Arrays.asList("2nd", "3rd", "5th")),
                    new Question(10, 5, true, "Q9", "What percentage of Americans are said to have eaten pizza in the last month?:", "93", Arrays.asList("72", "85")),
                    new Question(18, 6, false, "Q10", "How many places in the USA hve \"christmas\" in their name?", "182", Arrays.asList("92", "132", "162"))
            ))
    );

    public FlagData() {
    }

    public List<Flag> getFlagList() {
        return flagList;
    }

}
