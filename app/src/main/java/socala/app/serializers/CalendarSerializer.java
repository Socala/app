package socala.app.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarSerializer implements JsonSerializer<Calendar> {


    public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String a = json.getAsString();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZZ", Locale.US);

        Calendar calendar = Calendar.getInstance();
        try {

            calendar.setTime(dateFormatter.parse(a));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return calendar;
    }

    @Override
    public JsonElement serialize(Calendar src, Type typeOfSrc, JsonSerializationContext context) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZZ", Locale.US);

        return new JsonPrimitive(dateFormatter.format(src.getTime()));
    }
}
