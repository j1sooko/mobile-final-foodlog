package ddwu.mobile.finalproject.ma01_20190936;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class FoodNutriXmlParser {

    public enum TagType { NONE, WEIGHT, ENERGY };

    final static String TAG_ITEM = "idnt_List";
    final static String TAG_WEIGHT = "food_Weight";
    final static String TAG_ENERGY = "energy_Qy";

    public FoodNutriXmlParser() {
    }

    public ArrayList<FoodNutriDTO> parse(String xml) {

        ArrayList<FoodNutriDTO> resultList = new ArrayList();
        FoodNutriDTO dto = null;

        FoodNutriXmlParser.TagType tagType = FoodNutriXmlParser.TagType.NONE;

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
                            dto = new FoodNutriDTO();
                        } else if (parser.getName().equals(TAG_WEIGHT)) {
                            if (dto != null) tagType = FoodNutriXmlParser.TagType.WEIGHT;
                        } else if (parser.getName().equals(TAG_ENERGY)) {
                            if (dto != null) tagType = FoodNutriXmlParser.TagType.ENERGY;
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
                            //중량과 열량 태그를 만나면 값을 더해야함
                            case WEIGHT:
                                dto.setWeight(Double.parseDouble(parser.getText()));
                                break;
                            case ENERGY:
                                dto.setEnergy(Double.parseDouble(parser.getText()));
                                break;
                        }
                        tagType = FoodNutriXmlParser.TagType.NONE;
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
