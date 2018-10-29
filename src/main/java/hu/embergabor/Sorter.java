package hu.embergabor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.cli.*;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.move;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Sorter {

    private static final Map<String, String> typeMap = createMap();

    public static void main(String[] args) {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input folder");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output folder");
        output.setRequired(true);
        options.addOption(output);

        Option filter = new Option("f", "filter", true, "romtype whitelist separated by commas -f h,!,b,a,f,o,p,t,T,U");
        filter.setRequired(false);
        options.addOption(filter);

        options.addOption("r", false,  "sort by region");
        options.addOption("a", false, "sort by alphabet");
        options.addOption("d", false,  "testrun without moving files");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String inputFolder = cmd.getOptionValue("input");
            String outputFolder = cmd.getOptionValue("output");
            List<String> allowedList = new ArrayList<>();
            if(cmd.hasOption("f")){
                List<String> filterList = Arrays.asList(cmd.getOptionValue("f").split(","));
                for(String fil : filterList){
                    for (Map.Entry<String, String> entry : typeMap.entrySet()) {
                        if(entry.getKey().contains(fil)){
                            allowedList.add(entry.getValue());
                        }
                    }
                }
            }

            Boolean sortByRegion = cmd.hasOption("r");
            Boolean sortByAlphabet = cmd.hasOption("a");
            Boolean doDryrun = cmd.hasOption("d");

            List<Rom> romList = getRomList(inputFolder);
            if(!romList.isEmpty()) {
                if(!allowedList.isEmpty()) {
                    romList = filterList(romList, allowedList);
                    System.out.println(romList.size() + " roms match the filter");
                }
                for (Rom rom : romList) {
                    moveFile(rom, outputFolder, sortByRegion, sortByAlphabet, doDryrun);
                    System.out.println(romList.indexOf(rom)+1 + " of " + romList.size());
                }
            }else{
                System.out.println("No ROMs found in folder!");
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }

    private static List<Rom> getRomList(String sourceDir){
        List<Rom> romList = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(sourceDir))) {
            List<Path> list = paths
                    .filter(Files::isRegularFile)
                    .sorted()
                    .collect(Collectors.toList());
            for(Path path : list){
                String filename = path.getFileName().toString();
                Rom rom = new Rom();
                rom.setFilename(filename);
                if(filename.indexOf("(") > -1) {
                    rom.setTitle(filename.substring(0, filename.indexOf("(")).trim());
                }else{
                    rom.setTitle(filename);
                }
                rom.setRegion(parseRegion(filename));
                rom.setType(parseType(filename));
                rom.setPath(path);
                romList.add(rom);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Found " + romList.size() + " roms.");
        return romList;
    }

    private static String parseRegion(String filename){
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

    private static String parseType(String filename){
        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            if(filename.contains(entry.getKey())){
                return entry.getValue();
            }
        }
        return RomType.UNKNOWN;
    }

    private static Map<String, String> createMap() {
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
        return Collections.unmodifiableMap(map);
    }

    private static List<Rom> filterList(List<Rom> list, List<String> allowedList){
        return list.stream()
                .filter(rom -> allowedList.contains(rom.getType()))
                .collect(Collectors.toList());
    }

    private static void moveFile(Rom rom, String targetDir, boolean sortRegion, boolean sortAlphabet, boolean dryrun){

        if(sortRegion){
                targetDir +=  rom.getRegion() + File.separator;
        }

        if(sortAlphabet) {
            String firstLetter = rom.getFilename().substring(0, 1).toUpperCase();
            if (firstLetter.matches("[0-9]")) {
                firstLetter = "0-9";
            }
            targetDir += firstLetter + File.separator;
        }

        if(!dryrun) {
            try {
                File directory = new File(targetDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                move(rom.getPath(), Paths.get(targetDir + rom.getFilename()), REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(targetDir + rom.getFilename());
        return;
    }
}
