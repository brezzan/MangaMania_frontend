package com.example.mangamania_app.repo;

import static java.lang.Integer.parseInt;

import com.example.mangamania_app.model.Chapter;
import com.example.mangamania_app.model.Manga;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONtoModelFunctions {

    private List<Manga.Type> extractTypes(JSONArray typeArray) {
        List<Manga.Type> types = new ArrayList<>();
        if (typeArray != null ) {
            for (int i = 0; i < typeArray.length(); i++) {
                JSONObject typeObject = typeArray.optJSONObject(i);
                if (typeObject != null) {
                    Manga.Type type = new Manga.Type();
                    type.setName(typeObject.optString("name", ""));
                    type.setUrl(typeObject.optString("url", ""));
                    types.add(type);
                }
            }
        }
        return types;
    }

    public List<Manga> jsontoMangaList(StringBuilder buffer){
        JSONArray arr = null;
        try {
            arr = new JSONArray(buffer.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        List<Manga> data = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {

            JSONObject currentObj = arr.optJSONObject(i);

            String mangaId = currentObj.optString("mangaId","");
            String titleOv = currentObj.optString("titleOv","");
            String titleEn = currentObj.optString("titleEn","");
            String synopsis = currentObj.optString("synopsis","");

            JSONObject alternativeTitlesObj = currentObj.optJSONObject("alternativeTitles");
            Manga.AlternativeTitles alternativeTitles = new Manga.AlternativeTitles();
            if (alternativeTitlesObj != null) {
                alternativeTitles.setJapanese(alternativeTitlesObj.optString("japanese", ""));
                alternativeTitles.setEnglish(alternativeTitlesObj.optString("english", ""));
            }

            JSONObject informationObj = currentObj.optJSONObject("information");
            Manga.Information information = new Manga.Information();
            if (informationObj != null) {
                // Safely handle nested arrays
                List<Manga.Type> informationType = extractTypes(informationObj.optJSONArray("types"));
                List<Manga.Type> informationGenre = extractTypes(informationObj.optJSONArray("genres"));
                List<Manga.Type> informationTheme = extractTypes(informationObj.optJSONArray("themes"));
                List<Manga.Type> informationDemographic = extractTypes(informationObj.optJSONArray("demographics"));
                List<Manga.Type> informationSerialization = extractTypes(informationObj.optJSONArray("serializations"));
                List<Manga.Type> informationAuthors = extractTypes(informationObj.optJSONArray("authors"));

                information.setVolumes(informationObj.optString("volumes", ""));
                information.setChapters(informationObj.optString("chapters", ""));
                information.setStatus(informationObj.optString("status", ""));
                information.setPublished(informationObj.optString("published", ""));
                information.setTypes(informationType);
                information.setGenres(informationGenre);
                information.setThemes(informationTheme);
                information.setDemographics(informationDemographic);
                information.setSerializations(informationSerialization);
                information.setAuthors(informationAuthors);
            }

            JSONObject statisticsObj = currentObj.optJSONObject("statistics");
            Manga.Statistics statistics = new Manga.Statistics();
            if (statisticsObj != null) {
                statistics.setScore(statisticsObj.optString("score", ""));
                statistics.setRanked(statisticsObj.optString("ranked", ""));
                statistics.setPopularity(statisticsObj.optString("popularity", ""));
                statistics.setMembers(statisticsObj.optString("members", ""));
                statistics.setFavorites(statisticsObj.optString("favorites", ""));
            }

            JSONArray charactersArray = currentObj.optJSONArray("characters");
            List<Manga.Character> characters = new ArrayList<>();
            if (charactersArray != null) {
                for (int j = 0; j < charactersArray.length(); j++) {
                    JSONObject characterObj = charactersArray.optJSONObject(j);
                    if (characterObj != null) {
                        Manga.Character character = new Manga.Character();
                        character.setName(characterObj.optString("name", ""));
                        character.setPictureUrl(characterObj.optString("pictureUrl", ""));
                        character.setMyanimelistUrl(characterObj.optString("myanimelistUrl", ""));
                        characters.add(character);
                    }
                }
            }

            String pictureUrl = currentObj.optString("pictureUrl", "");

            Manga manga = new Manga(mangaId, titleOv, titleEn, synopsis, alternativeTitles, information, statistics, characters, pictureUrl);
            data.add(manga);
        }
        return data;
    }

    public Manga jsontoManga(JSONObject buffer){
        JSONObject currentObj = buffer;

        String mangaId = currentObj.optString("mangaId","");
        String titleOv = currentObj.optString("titleOv","");
        String titleEn = currentObj.optString("titleEn","");
        String synopsis = currentObj.optString("synopsis","");

        JSONObject alternativeTitlesObj = currentObj.optJSONObject("alternativeTitles");
        Manga.AlternativeTitles alternativeTitles = new Manga.AlternativeTitles();
        if (alternativeTitlesObj != null) {
            alternativeTitles.setJapanese(alternativeTitlesObj.optString("japanese", ""));
            alternativeTitles.setEnglish(alternativeTitlesObj.optString("english", ""));
        }

        JSONObject informationObj = currentObj.optJSONObject("information");
        Manga.Information information = new Manga.Information();
        if (informationObj != null) {
            // Safely handle nested arrays
            List<Manga.Type> informationType = extractTypes(informationObj.optJSONArray("types"));
            List<Manga.Type> informationGenre = extractTypes(informationObj.optJSONArray("genres"));
            List<Manga.Type> informationTheme = extractTypes(informationObj.optJSONArray("themes"));
            List<Manga.Type> informationDemographic = extractTypes(informationObj.optJSONArray("demographics"));
            List<Manga.Type> informationSerialization = extractTypes(informationObj.optJSONArray("serializations"));
            List<Manga.Type> informationAuthors = extractTypes(informationObj.optJSONArray("authors"));

            information.setVolumes(informationObj.optString("volumes", ""));
            information.setChapters(informationObj.optString("chapters", ""));
            information.setStatus(informationObj.optString("status", ""));
            information.setPublished(informationObj.optString("published", ""));
            information.setTypes(informationType);
            information.setGenres(informationGenre);
            information.setThemes(informationTheme);
            information.setDemographics(informationDemographic);
            information.setSerializations(informationSerialization);
            information.setAuthors(informationAuthors);
        }

        JSONObject statisticsObj = currentObj.optJSONObject("statistics");
        Manga.Statistics statistics = new Manga.Statistics();
        if (statisticsObj != null) {
            statistics.setScore(statisticsObj.optString("score", ""));
            statistics.setRanked(statisticsObj.optString("ranked", ""));
            statistics.setPopularity(statisticsObj.optString("popularity", ""));
            statistics.setMembers(statisticsObj.optString("members", ""));
            statistics.setFavorites(statisticsObj.optString("favorites", ""));
        }

        JSONArray charactersArray = currentObj.optJSONArray("characters");
        List<Manga.Character> characters = new ArrayList<>();
        if (charactersArray != null) {
            for (int j = 0; j < charactersArray.length(); j++) {
                JSONObject characterObj = charactersArray.optJSONObject(j);
                if (characterObj != null) {
                    Manga.Character character = new Manga.Character();
                    character.setName(characterObj.optString("name", ""));
                    character.setPictureUrl(characterObj.optString("pictureUrl", ""));
                    character.setMyanimelistUrl(characterObj.optString("myanimelistUrl", ""));
                    characters.add(character);
                }
            }
        }

        String pictureUrl = currentObj.optString("pictureUrl", "");

        Manga manga = new Manga(mangaId, titleOv, titleEn, synopsis, alternativeTitles, information, statistics, characters, pictureUrl);

        return manga;
    }

    public List<Chapter> jsontoChapterList(StringBuilder buffer){

        JSONArray arr = null;
        try {
            arr = new JSONArray(buffer.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        List<Chapter> data = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {

            JSONObject currentObj = arr.optJSONObject(i);

            String chapterId = currentObj.optString("chapterId","");
            String chapterName = currentObj.optString("chapterName","");
            int chapterNumber =  parseInt(currentObj.optString("chapterNumber",""));
            String description = currentObj.optString("description","");

            JSONObject mangaData = currentObj.optJSONObject("manga");
            Manga manga;

            JSONtoModelFunctions ops = new JSONtoModelFunctions();
            manga = ops.jsontoManga(mangaData);

            Chapter chp = new Chapter(chapterId, chapterName, chapterNumber,description,manga);;
            data.add(chp);
        }
        return data;
    }



}
