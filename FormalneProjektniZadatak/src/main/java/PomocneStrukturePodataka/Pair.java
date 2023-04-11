package PomocneStrukturePodataka;

import java.util.Objects;

public class Pair<K,V>
{
    public K first;
    public V second;
    public Pair(K key,V value)
    {
        this.first=key;
        this.second=value;
    }
    @Override
    public boolean equals(Object o)
    {
        if(o!=null && o instanceof Pair temp)
            return this.first.equals(temp.first) && this.second.equals(temp.second);
        return false;
    }

    @Override
    public int hashCode() {
        int hash=3;
        hash=7*hash+this.first.hashCode();
        hash=7*hash+this.second.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "("+this.first+", "+this.second+")";
    }

    public static<K,V> Pair<K,V> makePair(K first,V second)
    {
        return new Pair<K,V>(first,second);
    }
}
