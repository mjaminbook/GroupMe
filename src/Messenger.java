/**
 * Created by Connor Valenti on 12/4/14.
 */
public class Messenger implements Comparable
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

    public Messenger(int likes, int messages, String name){
        numMessages = messages;
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