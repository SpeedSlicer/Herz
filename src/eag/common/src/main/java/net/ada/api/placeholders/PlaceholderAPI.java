package net.ada.api.placeholders;

import jdk.jfr.Experimental;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
/* TODO Complete */
@Experimental
public class PlaceholderAPI {

    private static final Map<String, PlaceholderCallback> placeholders = new HashMap<>();

    private static final Pattern PLACEHOLDER_PATTERN =
            Pattern.compile("%([^%]+)%");

    public static void addPlaceholder(String placeholderID, PlaceholderCallback call) {
        placeholders.put(placeholderID, call);
    }

    public static String getPlaceholderValue(String id) {
        PlaceholderCallback callback = placeholders.get(id);

        if (callback == null) {
            return null;
        }

        return callback.getPlaceholderValue();
    }

    /* %id% parses as a placeholder */
    public static String adaptPlaceholders(String parse) {
        return PLACEHOLDER_PATTERN.matcher(parse).replaceAll(match -> {
            String id = match.group(1);

            PlaceholderCallback callback = placeholders.get(id);

            if (callback == null) {
                return match.group();
            }

            return callback.getPlaceholderValue();
        });
    }
}