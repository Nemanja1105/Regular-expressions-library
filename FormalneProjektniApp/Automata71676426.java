import java.util.Queue;
import java.util.function.Consumer;
import java.util.LinkedList;

class Events<T>
{
    private Queue<Consumer<T>> events=new LinkedList<>();
    public void subscribe(Consumer<T> consumer)
    {
        events.offer(consumer);
    }
    public void unSubscribeLast()
    {
        events.poll();
    }
    public void invoke(T state)
    {
        events.forEach((consumer)->{consumer.accept(state);});
    }
}
public class Automata
{
	private String getTransitionq1(char symbol, Events transition0, Events transition1)
	{
		String state=null;
		if(symbol=='0')
		{
			state="q1";
			transition0.invoke("q1");
		}
		if(symbol=='1')
		{
			state="q0";
			transition1.invoke("q1");
		}
		return state;
	}
	private String getTransitionq0(char symbol, Events transition0, Events transition1)
	{
		String state=null;
		if(symbol=='0')
		{
			state="q0";
			transition0.invoke("q0");
		}
		if(symbol=='1')
		{
			state="q1";
			transition1.invoke("q0");
		}
		return state;
	}
	public boolean check(String str, Events in, Events out, Events transition0, Events transition1)	{
		String trenutno="q0";
		for(var symbol:str.toCharArray())
		{
			switch(trenutno) {
				case "q1":
					out.invoke(trenutno);
					trenutno=getTransitionq1(symbol, transition0, transition1);
					in.invoke(trenutno);
					break;
				case "q0":
					out.invoke(trenutno);
					trenutno=getTransitionq0(symbol, transition0, transition1);
					in.invoke(trenutno);
					break;
			}
		}
		return trenutno.equals("q1");
	}
}
