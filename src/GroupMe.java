import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class GroupMe
{
    private static HashMap<String, Messenger> stats = new HashMap<String, Messenger>();
    private static Calendar creationDate = new GregorianCalendar(2013, 10, 10);
    private static Calendar currentDate = new GregorianCalendar();

    public static void main(String [] args)
    {
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(new FileReader("Files/messages.txt"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Messages.txt not found");
            System.exit(1);
        }
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
        boolean nextIsName = false;
        //HashMap<String, List<String>> changedNames = new HashMap<String, List<String>>();
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
                changeName(fromName, toName);
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
        //for(String s: changedNames.keySet())
        // {
        //    System.out.println(s + " changed their name to:");
        //    for(String s2: changedNames.get(s))
        //    {
        //        System.out.println(s2);
        //    }
        //    System.out.println("");
        //}
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


    /**
     * Edits the main HashMap whenever someone changes their username inside the group.
     * @param oldName The person's previous name.
     * @param newName The name the person is changing their name to.
     */
    public static void changeName(String oldName, String newName){
        int numMessages = stats.get(oldName).getNumMessages();
        int numLikes = stats.get(oldName).getNumLikes();
        stats.remove(oldName);
        stats.put(newName, new Messenger(numLikes, numMessages, newName));
    }

    /**
     * Starts an interactive conversation with the user which allows them to lookup statistics on an individual in the group.
     */
    public static void lookup(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Lookup stats for which group member: ");
            String name = scanner.nextLine();
            if(!stats.containsKey(name)){
                System.out.println("Invalid name.");
            }
            else if(name.equals("exit")){
                break;
            }
            else{
                double avgLikes = stats.get(name).getNumLikes() / stats.get(name).getNumMessages();
                System.out.println(name + " has sent a total of" + stats.get(name).getNumMessages() + " messages since the creation of the group.");
                System.out.println("They have received a total of" + stats.get(name).getNumLikes() + "likes on their messages over this period of time.");
                System.out.println("This give them an average number of " + avgLikes + "likes per message.");
            }
        }
    }

    /**
     * Checks to see if a give String is a date.
     * @param s The String to be checked.
     * @return True if the String is a date, false if it is not.
     */
    public static boolean isDate(String s)
    {
        if(s.length() <= 5)
            return false;
        else if(s.contains(":") && s.charAt(s.length()-1) == 'm' && (s.charAt(s.length()-2) == 'p' || s.charAt(s.length()-2) == 'a') && Character.isDigit(s.charAt(s.length() - 3)))
            return true;
        return false;
    }
}