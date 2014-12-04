import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
public class GroupMe
{
    public static void main(String [] args)
    {
        Scanner scanner = new Scanner(System.in);
        LinkedList<String> validNames = new LinkedList<String>();
        validNames.add("Ned READ");
        validNames.add("Ryan Dugan");
        validNames.add("John Glascow");
        validNames.add("Spencer Moor");
        validNames.add("Noah Nieting");
        validNames.add("Ben Mathers");
        validNames.add("Connor Valenti");
        validNames.add("Anne McEvoy");
        validNames.add("Mara Halvorson");
        validNames.add("Bridget Gustafson");
        validNames.add("Emily Roebuck");
        int i = 0;
        boolean nextIsName = false;
        HashMap<String, Messenger> stats = new HashMap<String, Messenger>();
        HashMap<String, List<String>> changedNames = new HashMap<String, List<String>>();
        int mostLikes = 0;
        String mostLiked = "";
        String message = "";
        String mostRecentName = "";
        while(scanner.hasNext())
        {
            String text = scanner.nextLine();
            if(nextIsName)
            {
                nextIsName = false;
                if(validNames.contains(text))
                {
                    mostRecentName = text;
                    continue;
                }

            }
            if(text.equals(""))
            {
                nextIsName = true;
            }
            else if(isDate(text))
            {
                continue;
            }
            else if(text.contains("added") && text.contains("to the group"))
            {
                int spaceLoc = text.indexOf(" added ");
                text = text.replace(" to the group","").replace("added ","");
                String personAdded = text.substring(spaceLoc+1);
                if(personAdded.contains(" and "))
                {
                    for(String name: personAdded.split(" and "))
                        validNames.add(name);
                }
                else
                    validNames.add(personAdded);
                System.out.println(personAdded + " added");
            }
            else if(text.contains(" changed name to "))
            {
                int spaceLoc = text.indexOf("changed name to");
                text = text.replace("changed name to ","");
                String fromName = text.substring(0,spaceLoc);
                String toName = text.substring(spaceLoc);
                validNames.remove(fromName);
                validNames.add(toName);
			/*if(changedNames.containsKey(fromName))
				changedNames.get(fromName).add(toName);
			else
			{
				changedNames.put(fromName, new LinkedList<String>());
				changedNames.get(fromName).add(toName);
			}*/
            }
            else if(text.contains(" has rejoined the group"))
            {
                //text = text.remove(" has rejoined the group"))
            }
            else if(text.length() >= 2)
            {
                char space = text.charAt(0);
                char number = text.charAt(1);
                if(space == ' ' && Character.isDigit(number))
                {
                    text = text.replace(" ","");
                    int numLikes = Integer.parseInt(text);
                    if(numLikes > mostLikes)
                    {
                        mostLikes = numLikes;
                        mostLiked = message;
                    }
                    if(stats.containsKey(mostRecentName))
                    {
                        stats.get(mostRecentName).addLikes(numLikes);
                        stats.get(mostRecentName).addMessage();
                    }
                    else
                    {
                        stats.put(mostRecentName, new Messenger(numLikes, mostRecentName));
                    }
                }
                else if(!text.equals(""))
                {
                    if(stats.containsKey(mostRecentName))
                    {
                        stats.get(mostRecentName).addMessage();
                    }
                    else
                    {
                        stats.put(mostRecentName, new Messenger(0, mostRecentName));
                    }
                }

            }
            //i++;
            //if(i > 100)
            //	return;
        }
        int totalLikes = 0;
        int totalMessages = 0;
        double highestPercent = 1.0;
        ArrayList<Messenger> allPeople = new ArrayList<Messenger>();

        for(String s:stats.keySet())
        {
            allPeople.add(stats.get(s));
            System.out.println(s + ": " + stats.get(s).getNumLikes() + " likes over " + stats.get(s).getNumMessages() + " messages");
            double percent = 1.0*stats.get(s).getNumLikes()/(1.0 * stats.get(s).getNumMessages());
            System.out.println(percent);
            if(percent > highestPercent)
                highestPercent = percent;
            totalLikes += stats.get(s).getNumLikes();
            totalMessages += stats.get(s).getNumMessages();
        }
        System.out.println("");
        System.out.println("Total Number Of Messages: " + totalMessages);
        System.out.println("Total Number Of Likes: " + totalLikes);
        System.out.println("");
        for(String s: changedNames.keySet())
        {
            System.out.println(s + " changed their name to:");
            for(String s2: changedNames.get(s))
            {
                System.out.println(s2);
            }
            System.out.println("");
        }
        System.out.println("Most likes on a message: " + mostLikes);
        System.out.println(mostLiked);
        System.out.println("Highest Percent: " + highestPercent);
        System.out.println("");
        System.out.println(allPeople.size());
        Collections.sort(allPeople);
        int n = 0;
        for(Messenger m: allPeople)
        {
            n++;
            System.out.println(n + ": " + m.getName());
        }
    }
    public static boolean isDate(String s)
    {
        if(s.length() <= 5)
            return false;
        if(s.contains(":") && s.charAt(s.length()-1) == 'm' && (s.charAt(s.length()-2) == 'p' || s.charAt(s.length()-2) == 'a') && Character.isDigit(s.charAt(s.length() - 3)))
            return true;
        return false;
    }
    private static class Messenger implements Comparable
    {
        private int numMessages;
        private int numLikes;
        private String name;

        public Messenger(int likes,String name)
        {
            numMessages = 1;
            numLikes = likes;
            this.name = name;
        }

        public int getNumMessages()
        {
            return numMessages;
        }
        public String getName()
        {
            return name;
        }

        public int getNumLikes()
        {
            return numLikes;
        }

        public void setNumMessages(int x)
        {
            numMessages = x;
        }
        public void addMessage()
        {
            numMessages++;
        }

        public void addLikes(int x)
        {
            numLikes += x;
        }

        public void setNumLikes(int x)
        {
            numLikes = x;
        }
        public double getPercent()
        {
            return 1.0*numLikes / (1.0*numMessages);
        }
        @Override
        public int compareTo(Object other)
        {
            if(other instanceof Messenger)
            {
                Messenger m = (Messenger) other;
                if(getPercent() > m.getPercent())
                    return -1;
                else if( getPercent() < m.getPercent())
                    return 1;
                return 0;
            }
            return 0;
        }
    }
}