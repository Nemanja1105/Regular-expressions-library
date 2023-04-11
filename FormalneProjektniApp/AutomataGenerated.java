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
	private String getTransitionQ2-Q3-Q12-Q5-Q6(char symbol, Events transitiona, Events transitionb)
	{
		String state=null;
		if(symbol=='a')
		{
			state="DEAD_STATE";
			transitiona.invoke("Q2-Q3-Q12-Q5-Q6");
		}
		if(symbol=='b')
		{
			state="Q3-Q4-Q12-Q6";
			transitionb.invoke("Q2-Q3-Q12-Q5-Q6");
		}
		return state;
	}
	private String getTransitionQ8-Q9(char symbol, Events transitiona, Events transitionb)
	{
		String state=null;
		if(symbol=='a')
		{
			state="Q10-Q12";
			transitiona.invoke("Q8-Q9");
		}
		if(symbol=='b')
		{
			state="DEAD_STATE";
			transitionb.invoke("Q8-Q9");
		}
		return state;
	}
	private String getTransitionQ3-Q4-Q12-Q6(char symbol, Events transitiona, Events transitionb)
	{
		String state=null;
		if(symbol=='a')
		{
			state="DEAD_STATE";
			transitiona.invoke("Q3-Q4-Q12-Q6");
		}
		if(symbol=='b')
		{
			state="Q3-Q4-Q12-Q6";
			transitionb.invoke("Q3-Q4-Q12-Q6");
		}
		return state;
	}
	private String getTransitionQ10-Q12(char symbol, Events transitiona, Events transitionb)
	{
		String state=null;
		if(symbol=='a')
		{
			state="DEAD_STATE";
			transitiona.invoke("Q10-Q12");
		}
		if(symbol=='b')
		{
			state="DEAD_STATE";
			transitionb.invoke("Q10-Q12");
		}
		return state;
	}
	private String getTransitionDEAD_STATE(char symbol, Events transitiona, Events transitionb)
	{
		String state=null;
		if(symbol=='a')
		{
			state="DEAD_STATE";
			transitiona.invoke("DEAD_STATE");
		}
		if(symbol=='b')
		{
			state="DEAD_STATE";
			transitionb.invoke("DEAD_STATE");
		}
		return state;
	}
	private String getTransitionQ11-Q1-Q7(char symbol, Events transitiona, Events transitionb)
	{
		String state=null;
		if(symbol=='a')
		{
			state="Q2-Q3-Q12-Q5-Q6";
			transitiona.invoke("Q11-Q1-Q7");
		}
		if(symbol=='b')
		{
			state="Q8-Q9";
			transitionb.invoke("Q11-Q1-Q7");
		}
		return state;
	}
	public boolean check(String str, Events in, Events out, Events transitiona, Events transitionb)	{
		String trenutno="Q11-Q1-Q7";
		for(var symbol:str.toCharArray())
		{
			switch(trenutno) {
				case "Q2-Q3-Q12-Q5-Q6":
					out.invoke(trenutno);
					trenutno=getTransitionQ2-Q3-Q12-Q5-Q6(symbol, transitiona, transitionb);
					in.invoke(trenutno);
					break;
				case "Q8-Q9":
					out.invoke(trenutno);
					trenutno=getTransitionQ8-Q9(symbol, transitiona, transitionb);
					in.invoke(trenutno);
					break;
				case "Q3-Q4-Q12-Q6":
					out.invoke(trenutno);
					trenutno=getTransitionQ3-Q4-Q12-Q6(symbol, transitiona, transitionb);
					in.invoke(trenutno);
					break;
				case "Q10-Q12":
					out.invoke(trenutno);
					trenutno=getTransitionQ10-Q12(symbol, transitiona, transitionb);
					in.invoke(trenutno);
					break;
				case "DEAD_STATE":
					out.invoke(trenutno);
					trenutno=getTransitionDEAD_STATE(symbol, transitiona, transitionb);
					in.invoke(trenutno);
					break;
				case "Q11-Q1-Q7":
					out.invoke(trenutno);
					trenutno=getTransitionQ11-Q1-Q7(symbol, transitiona, transitionb);
					in.invoke(trenutno);
					break;
			}
		}
		return trenutno.equals("Q2-Q3-Q12-Q5-Q6") ||  trenutno.equals("Q3-Q4-Q12-Q6") ||  trenutno.equals("Q10-Q12");
	}
}
