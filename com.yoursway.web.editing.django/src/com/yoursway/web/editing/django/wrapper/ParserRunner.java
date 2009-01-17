package com.yoursway.web.editing.django.wrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yoursway.utils.YsFileUtils;

public class ParserRunner implements SourcePositionLocator {
	private final String djangoPath;
	private Map<Integer, Source> sourceIds = new HashMap<Integer, Source>();
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private String html;

	public ParserRunner(String djangoPath) {
		this.djangoPath = djangoPath;

	}

	public Fragment find(int position) {
		for (Fragment f : fragments) {
			if (f.start <= position && f.end >= position) {
				return f;
			}
		}
		return null;
	}

	private File getParserFilePath() throws IOException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(
				"/parser/parser.py");
		File tempFile = File.createTempFile("pythonparser", ".py");
		tempFile.deleteOnExit();
		YsFileUtils.saveToFile(stream, tempFile);
		return tempFile;
	}

	public void parse(String url) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("python", getParserFilePath()
				.getPath(), djangoPath, url);
		Process process = builder.start();
		process.getOutputStream().close();
		InputStream stream = process.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		for (String line = reader.readLine(); !"#-------".equals(line.trim()); line = reader.readLine()) {
			String[] parts = line.split(" ", 3);
			if (parts.length != 3) {
				System.out.println(String.format("Invalid line with %d parts: %s", parts.length, line));
				continue;
			}
			Source source = new Source(Integer.valueOf(parts[0]), parts[1], parts[2]);
			sourceIds.put(source.id, source);
		}
		String line = reader.readLine();
		while (!"#-------".equals(line.trim())) {
			String[] parts = line.split(" ", 6);
			Integer[] ints = new Integer[parts.length];
			for (int i = 0; i < parts.length; i++) {
				ints[i] = Integer.valueOf(parts[i]);
			}
			Source source = sourceIds.get(ints[3]);
			Fragment fragment = new Fragment(ints[0], ints[1], ints[2], source, ints[4],
					ints[5]);
			fragments.add(fragment);
			line = reader.readLine();
		}
		line = reader.readLine();
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			sb.append(line);
			sb.append("\n");
			line = reader.readLine();
		}
		html = sb.toString();
		stream.close();
	}
	
	public String getHtml() {
		return html;
	}
}
