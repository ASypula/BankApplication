import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Dictionary {
    private String lang;
    private String filePath = "LanguageDictionaries/LangDictionary.json";
    JSONParser parser = new JSONParser();
    JSONObject jsonObject;

    public Dictionary(String language) {
        this.lang = language;
        this.filePath = filePath.substring(0, 21) + language + filePath.substring(21);
        try {
            Object obj = parser.parse(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            this.jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getText(String textKey) {
        String text;
        text = (String) jsonObject.get(textKey);
        return text;
    }
}

// Dictionary dict = new Dictionary("Eng");
// dict.getText("Name");