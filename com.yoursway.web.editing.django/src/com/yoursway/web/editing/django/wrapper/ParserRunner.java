package com.yoursway.web.editing.django.wrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserRunner {
	private final String djangoPath;
	Map<Integer, Source> sourceIds = new HashMap<Integer, Source>(); 
	List<Fragment> fragments = new ArrayList<Fragment>(); 

	public ParserRunner(String djangoPath) {
		this.djangoPath = djangoPath;
		
	}

	public Fragment find(int position){
		for(Fragment f: fragments){
			if(f.start <= position && f.end >= position){
				return f;
			}
		}
		return null;
	}
	
	void parse(String url) throws IOException{
		ProcessBuilder builder = new ProcessBuilder("python", "parser.py", djangoPath, url);
		Process process = builder.start();
		InputStream stream = process.getInputStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = reader.readLine();
		while(!"#--------".equals(line)){
			String[] parts = line.split(" ", 2);
			Source source = new Source(Integer.valueOf(parts[0]), parts[1], parts[2]);
			sourceIds.put(source.id, source);
			line = reader.readLine();
		}
		line = reader.readLine();
		while(!"#--------".equals(line)){
			String[] parts = line.split(" ", 5);
			Integer[] ints = new Integer[parts.length];
			for(int i=0; i<parts.length; i++){
				ints[i] = Integer.valueOf(parts[i]);
			}
			Source source = sourceIds.get(ints[3]);
			Fragment fragment = new Fragment(ints[0], ints[1], ints[2], source, ints[4], ints[5]);
			fragments.add(fragment);
			line = reader.readLine();
		}
	}
}
