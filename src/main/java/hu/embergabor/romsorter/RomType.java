package hu.embergabor.romsorter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RomType {
    public static String HACK = "Hack";
    public static String BAD_DUMP = "Bad Dump";
    public static String ALTERNATIVE = "Alternative rom";
    public static String TRAINED = "Trained";
    public static String PIRATE = "Pirate";
    public static String FIXED = "Fixed";
    public static String TRANSLATION = "Translation";
    public static String OVERDUMP = "Overdump";
    public static String VERIFIED = "Verified Good Dump";
    public static String UNLICENSED = "Unlicensed";
    public static String UNKNOWN = "Unknown";

    public static Map<String, String> typeMap;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("[h", RomType.HACK);
        map.put("[b", RomType.BAD_DUMP);
        map.put("[a", RomType.ALTERNATIVE);
        map.put("[f", RomType.FIXED);
        map.put("[o", RomType.OVERDUMP);
        map.put("[p", RomType.PIRATE);
        map.put("[t", RomType.TRAINED);
        map.put("[T", RomType.TRANSLATION);
        map.put("(Unl", RomType.UNLICENSED);
        map.put("[!", RomType.VERIFIED);
        typeMap = Collections.unmodifiableMap(map);
    }

    public static String parseType(String filename){
        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            if(filename.contains(entry.getKey())){
                return entry.getValue();
            }
        }
        return RomType.UNKNOWN;
    }

    public static String parseRegion(String filename){
        Pattern pattern = Pattern.compile("\\([E,U,J]\\)");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find())
        {
            String regioncode = matcher.group(0).replace("(","").replace(")","");
            if("E".equals(regioncode) || "E".equals(regioncode)){
                return "PAL";
            }else if("U".equals(regioncode)){
                return "NTSC-U";
            }else if("J".equals(regioncode)){
                return "NTSC-J";
            }
        }
        return "Other";
    }
}
