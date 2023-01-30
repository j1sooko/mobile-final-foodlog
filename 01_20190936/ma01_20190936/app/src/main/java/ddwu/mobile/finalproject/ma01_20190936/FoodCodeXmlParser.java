package ddwu.mobile.finalproject.ma01_20190936;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class FoodCodeXmlParser {


    public enum TagType { NONE, FOODCODE, FOODNAME };

    final static String TAG_ITEM = "item";
    final static String TAG_CODE = "food_Code";
    final static String TAG_NAME = "food_Name";

    public FoodCodeXmlParser() {
    }

    public ArrayList<FoodCodeDTO> parse(String xml) {

        ArrayList<FoodCodeDTO> resultList = new ArrayList();
        FoodCodeDTO dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto = new FoodCodeDTO();
                        } else if (parser.getName().equals(TAG_CODE)) {
                            if (dto != null) tagType = TagType.FOODCODE;
                        } else if (parser.getName().equals(TAG_NAME)) {
                            if (dto != null) tagType = TagType.FOODNAME;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case FOODCODE:
                                dto.setFoodCode(parser.getText());
                                break;
                            case FOODNAME:
                                dto.setFoodName(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
