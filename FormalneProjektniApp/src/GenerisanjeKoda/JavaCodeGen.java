package GenerisanjeKoda;
import Automati.Dka;
import PomocneStrukturePodataka.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class JavaCodeGen implements ICodeGen
{
    private Dka automat;
    private String path;
    private StringBuilder builder=new StringBuilder();
    public JavaCodeGen(Dka automat,String path)
    {
        this.automat=automat;
        this.path=path;
    }

    @Override
    public void genCode()throws IOException
    {
        this.generateImportHeader();
        builder.append(this.eventClassGenerate()+"\n");
        builder.append("public class Automata\n{\n");
        for(var state:this.automat.getStates()) {
            builder.append("\t");
            this.generateGetTransitionMethod(state);
            builder.append("\n");
        }
        this.generateCheckMethod();
        this.builder.append("}\n");
        this.saveToFile();
    }


    private void saveToFile()throws IOException
    {
        Random rand=new Random();
        String name=Integer.toString(rand.nextInt(100000000));
        PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(this.path+"Automata"+name+".java")));
        writer.write(builder.toString());
        writer.close();
    }

    private void generateCheckMethod()
    {
        this.builder.append("\t");
        this.generateCheckMethodName();
        this.builder.append("\t{\n");
        this.builder.append("\t\tString trenutno=\""+this.automat.getStartState()+"\";\n");
        this.builder.append("\t\tfor(var symbol:str.toCharArray())\n");
        this.builder.append("\t\t{\n");
        this.builder.append("\t\t\tswitch(trenutno) {\n");
        for(var state:this.automat.getStates())
        {
            this.builder.append("\t\t\t\tcase \""+state+"\":\n");
            this.builder.append("\t\t\t\t\tout.invoke(trenutno);\n");
            // this.builder.append("\t\t\t\t\ttrenutno=getTransition"+state+"(symbol);\n");
            this.generateGetTransitionCall(state);
            this.builder.append("\t\t\t\t\tin.invoke(trenutno);\n");
            this.builder.append("\t\t\t\t\tbreak;\n");
        }
        this.builder.append("\t\t\t}\n");
        this.builder.append("\t\t}\n");
        this.generateFinalIntersection();
        this.builder.append("\t}\n");
    }

    private void generateGetTransitionCall(String state)
    {
        this.builder.append("\t\t\t\t\ttrenutno=getTransition"+state+"(symbol");
        for(var symbol:this.automat.getAlphabet())
            this.builder.append(", transition"+symbol);
        this.builder.append(");\n");
    }

    private void generateFinalIntersection()
    {
        this.builder.append("\t\treturn");
        Iterator<String> iter=this.automat.getFinalStates().iterator();
        while(iter.hasNext())
        {
            this.builder.append(" trenutno.equals(\""+iter.next()+"\")");
            if(iter.hasNext())
                this.builder.append(" || ");
        }
        this.builder.append(";\n");
    }


    private void generateCheckMethodName()
    {
        this.builder.append("public boolean check(String str, Events in, Events out");
        for(var symbol:this.automat.getAlphabet())
        {
            this.builder.append(", Events transition"+symbol);
        }
        this.builder.append(")");

    }



    private void generateGetTransitionMethod(String state)
    {
        this.generateNameForTransitionMethod(state);
        this.builder.append("\t{\n");
        this.builder.append("\t\tString state=null;\n");
        for(var symbol:this.automat.getAlphabet())
        {
            this.builder.append("\t\tif(symbol=='"+symbol+"')\n");
            this.builder.append("\t\t{\n");
            this.builder.append("\t\t\tstate=\""+this.automat.getTranstion(Pair.makePair(state,symbol))+"\";\n");
            this.builder.append("\t\t\ttransition"+symbol+".invoke(\""+state+"\");\n");
            this.builder.append("\t\t}\n");
        }
        this.builder.append("\t\treturn state;\n");
        this.builder.append("\t}");
    }



    private void generateNameForTransitionMethod(String state)
    {
        this.builder.append("private String getTransition"+state+"(char symbol");
        for(var symbol:this.automat.getAlphabet())
        {
            this.builder.append(", Events transition"+symbol);
        }
        this.builder.append(")\n");
    }

    private void generateImportHeader()
    {
        this.builder.append("import java.util.Queue;\n" +
                "import java.util.function.Consumer;\n" +
                "import java.util.LinkedList;\n\n");
    }

    private String eventClassGenerate()
    {
        return "class Events<T>\n" +
                "{\n" +
                "    private Queue<Consumer<T>> events=new LinkedList<>();\n" +
                "    public void subscribe(Consumer<T> consumer)\n" +
                "    {\n" +
                "        events.offer(consumer);\n" +
                "    }\n" +
                "    public void unSubscribeLast()\n" +
                "    {\n" +
                "        events.poll();\n" +
                "    }\n" +
                "    public void invoke(T state)\n" +
                "    {\n" +
                "        events.forEach((consumer)->{consumer.accept(state);});\n" +
                "    }\n" +
                "}";
    }

}
