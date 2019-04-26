package com.balancedbytes.tool.kodi;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Tool to rename episodes in a directory according to title names from
 * thetvdb.com.
 * 
 * @author TecBeast
 */
public class RenameKodiEpisodesTask extends SwingWorker<Void, String> {

	public static long DELAY = 100; // delay in ms

	public static final Pattern FILE_PATTERN = Pattern.compile("^(S([0-9]+)E([0-9]+)).*");

	private File fDirectory;
	private Map<String, String> fEpisodeMap;
	private Document fDocument;

	public RenameKodiEpisodesTask(File directory, Map<String, String> episodeMap, Document document) {
		fDirectory = directory;
		fEpisodeMap = episodeMap;
		fDocument = document;
	}

	@Override
	protected Void doInBackground() throws Exception {
		setProgress(0);
		Map<String, File> fileMap = buildFileMap();
		String[] keys = fileMap.keySet().toArray(new String[fileMap.size()]);
		Arrays.sort(keys);
		for (int i = 0; i < keys.length; i++) {
			setProgress((i + 1) * 100 / keys.length);
			if (DELAY > 0) {
				Thread.sleep(DELAY);
			}
			File newFile = rename(keys[i], fileMap.get(keys[i]));
			if (newFile != null) {
				publish(newFile.getName());
			}
		}
		return null;
	}

	@Override
	protected void process(List<String> chunks) {
		for (String message : chunks) {
			try {
				fDocument.insertString(fDocument.getLength(), message + "\n", null);
			} catch (BadLocationException e) {
				// no insert
			}
		}
	}

	private File rename(String episodeKey, File file) throws IOException {

		if (!KodiTools.isProvided(episodeKey) || (file == null) || !KodiTools.isProvided(fEpisodeMap.get(episodeKey))) {
			return null;
		}
		
		int suffixPos = file.getName().lastIndexOf('.');
		String fileSuffix = (suffixPos >= 0) ? file.getName().substring(suffixPos + 1) : "";
		
		StringBuilder newFileName = new StringBuilder();
		newFileName.append(episodeKey);
		newFileName.append(" ").append(escape(fEpisodeMap.get(episodeKey)));
		if (KodiTools.isProvided(fileSuffix)) {
			newFileName.append(".").append(fileSuffix);
		}
		
		File newFile = new File(file.getParent(), newFileName.toString());
		file.renameTo(newFile);

		return newFile;

	}

	private Map<String, File> buildFileMap() {
		Map<String, File> fileMap = new HashMap<String, File>();
		for (File file : fDirectory.listFiles()) {
			if (!file.isFile()) {
				continue;
			}
			String fileName = file.getName().toUpperCase();
			Matcher matcher = FILE_PATTERN.matcher(fileName);
			if (matcher.matches()) {
				fileMap.put(matcher.group(1), file);
			}
		}
		return fileMap;
	}

	private String escape(String label) {
		if ((label != null) && (label.length() > 0)) {
			label = label.replaceAll("ä", "ae");
			label = label.replaceAll("Ä", "Ae");
			label = label.replaceAll("ö", "oe");
			label = label.replaceAll("Ö", "Oe");
			label = label.replaceAll("ü", "ue");
			label = label.replaceAll("Ü", "Ue");
			label = label.replaceAll("ß", "ss");
			label = label.replaceAll("\\?", "");
			label = label.replaceAll("/", "_");
			label = label.replaceAll(":", "_");
			label = label.replaceAll("’", "");
		}
		return label;
	}

}