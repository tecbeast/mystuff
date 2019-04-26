package com.balancedbytes.tool.kodi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingWorker;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class SearchTheTvDbEpisodesTask extends SwingWorker<Void, Void> {

	private File fSeasonDirectory;
	private int fSeasonNr;
	private int fSeriesId;
	private String fSeriesName;
	private Map<String, String> fEpisodeMap;

	public SearchTheTvDbEpisodesTask(File seasonDirectory) {
		fSeasonDirectory = seasonDirectory;
	}

	@Override
	protected Void doInBackground() throws Exception {
		fSeasonNr = 0;
		fSeriesId = 0;		
		fEpisodeMap = new HashMap<String, String>();
		fSeriesName = null;
		try {
			String seasonDirName = fSeasonDirectory.getName().toLowerCase();
			if (seasonDirName.startsWith("staffel ") && (seasonDirName.length() > 8)) {
				fSeasonNr = Integer.parseInt(KodiTools.removeLeadingZeroes(seasonDirName.substring(8)));
			}
			if (seasonDirName.startsWith("season ") && (seasonDirName.length() > 7)) {
				fSeasonNr = Integer.parseInt(KodiTools.removeLeadingZeroes(seasonDirName.substring(7)));
			}
		} catch (NumberFormatException nfe) {
			fSeasonNr = 0;
		}
		if (fSeasonNr > 0) {
			String seriesDirName = fSeasonDirectory.getParentFile().getName();
			try (TheTvDbApiClient client = new TheTvDbApiClient("/RenameKodiEpisodes.properties")) {
				if (client.login()) {
					// publish(client.searchSeries(seriesName));
					JsonObject jsonObject = client.searchSeries(seriesDirName);
		    		if (KodiTools.isNotNull(jsonObject, jsonObject.get("data"))) {
						JsonArray jsonData = jsonObject.get("data").asArray();
						JsonObject jsonSeries = (jsonData.size() > 0) ? jsonData.get(0).asObject() : null;
						if (KodiTools.isNotNull(jsonSeries)) {
							fSeriesId = jsonSeries.getInt("id", 0);
							fSeriesName = jsonSeries.getString("seriesName", "");
							jsonObject = client.queryEpisodes(fSeriesId, fSeasonNr);
				    		if (KodiTools.isNotNull(jsonObject, jsonObject.get("data"))) {
								jsonData = jsonObject.get("data").asArray();
								for (int i = 0; i < jsonData.size(); i++) {
									JsonObject episode = jsonData.get(i).asObject();
									JsonValue airedSeason = episode.get("airedSeason");
									JsonValue airedEpisodeNumber = episode.get("airedEpisodeNumber");
									JsonValue episodeName = episode.get("episodeName");
									if (KodiTools.isNotNull(airedSeason, airedEpisodeNumber, episodeName)) {
										String episodeId = new StringBuilder()
											.append("S").append(KodiTools.formatWithLeadingZeroes(airedSeason.asInt()))
											.append("E").append(KodiTools.formatWithLeadingZeroes(airedEpisodeNumber.asInt()))
											.toString();
										fEpisodeMap.put(episodeId, episodeName.asString());
									}
								}
				    		}							
						}
		    		}
				}
			}
		}
		return null;
	}
	
	public int getSeriesId() {
		return fSeriesId;
	}
	
	public int getSeasonNr() {
		return fSeasonNr;
	}
	
	public String getSeriesName() {
		return fSeriesName;
	}
	
	public Map<String, String> getEpisodeMap() {
		return fEpisodeMap;
	}

}