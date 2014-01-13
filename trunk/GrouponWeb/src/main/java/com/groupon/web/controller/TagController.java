package com.groupon.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupon.web.dao.model.Tag;
import com.groupon.web.service.TagService;

@Controller
@RequestMapping(value = "/tags")
public class TagController extends AbstractBaseController {

	@Autowired
	private TagService tagService;

	@Value("${TAG_AUTOCOMPLETE_MAX_RESULTS}")
	private int tagAutocompleteMaxResults;

	/**
	 * Searches tags by a given search text
	 * @param term the search text
	 * @return the tags matching the given search text as json response
	 */
	@RequestMapping(value = "/searchTags")
	public ResponseEntity<Map<String, Object>> getTags(@RequestParam String term) {
		Map<String, Object> response = new HashMap<String, Object>();
		LinkedList<String> respList = new LinkedList<String>();

		if (StringUtils.isNotBlank(term)) {
			try {
				URL url = getWikipediaSearchUrl(term, tagAutocompleteMaxResults);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				int responseCode = conn.getResponseCode();

				if (responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					String inputLine;
					StringBuffer responseBuffer = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						responseBuffer.append(inputLine);
					}
					in.close();

					JSONArray jsonArray = new JSONArray(responseBuffer.toString());
					if (jsonArray.length() > 1) {
						JSONArray list = jsonArray.getJSONArray(1);

						for (int i = 0; i < list.length(); i++) {
							respList.add(list.getString(i));
						}
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (respList.size() < tagAutocompleteMaxResults) {
			List<Tag> tags = tagService.searchTags(term, 0, tagAutocompleteMaxResults - respList.size());
			for (Tag tag : tags) {
				respList.addFirst(tag.getName());
			}
		}

		response.put("tags", respList);
		return prepareSuccessResponse(response);
	}
	
	private URL getWikipediaSearchUrl(String term, int maxResults) throws MalformedURLException {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://en.wikipedia.org/w/api.php?action=opensearch&search=");
		urlBuilder.append(term);
		urlBuilder.append("&limit=");
		urlBuilder.append(maxResults);
		urlBuilder.append("&namespace=0&format=json");
		return new URL(urlBuilder.toString());
	}
}
