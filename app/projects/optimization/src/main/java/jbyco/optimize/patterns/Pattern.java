package jbyco.optimize.patterns;

import java.util.List;

public interface Pattern<Item> {

    public String getPattern();
    public boolean check(List<Item> matched);
    public boolean replace(List<Item> matched);

}
