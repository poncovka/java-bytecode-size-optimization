package jbyco.lib;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOptions {

	// map of names and options
	Map<String, AbstractOption> map = new HashMap<>();

	public AbstractOptions() {
		
		// init map
		for (AbstractOption option : all()) {
			for (String name : option.getNames()) {
				map.put(name, option);
			}
		}
	}
	
	public abstract AbstractOption[] all();
	
	public AbstractOption getOption(String name) {
		return map.get(name);
	}
	
	public boolean isOption(String name) {
		return map.containsKey(name);
	}
	
	public void help() {
		
		// TODO print usage
		// TODO print description
		
		// print options and description
		for (AbstractOption option : all()) {
			System.out.printf("%-30s %s\n", Utils.arrayToString(option.getNames(), ", "), option.getDescription());
		}
	}
	
	
}
