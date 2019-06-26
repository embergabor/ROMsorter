package hu.embergabor.romsorter;

import hu.embergabor.romsorter.model.Rom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Sorter {

    private static final Map<String, String> typeMap = RomType.typeMap;

    public static Integer start(String input, String output, List<String> filterList, Boolean subAlpha, Boolean subRegion, Boolean dryrun) {
            List<String> allowedList = new ArrayList<>();
            if(!filterList.isEmpty()){
                for(String fil : filterList){
                    for (Map.Entry<String, String> entry : typeMap.entrySet()) {
                        if(entry.getKey().contains(fil)){
                            allowedList.add(entry.getValue());
                        }
                    }
                }
            }

            List<Rom> romList = getRomList(input);
            List<Rom> filteredList = romList.stream()
                .filter(rom -> allowedList.contains(rom.getType()))
                .collect(Collectors.toList());

            for (Rom rom : filteredList) {
                moveFile(rom, output, subRegion, subAlpha, dryrun);
                System.out.println(filteredList.indexOf(rom)+1 + " of " + filteredList.size());
            }
            return filteredList.size();
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
                rom.setRegion(RomType.parseRegion(filename));
                rom.setType(RomType.parseType(filename));
                rom.setPath(path);
                rom.setExtension(filename.substring(filename.lastIndexOf(".")));
                romList.add(rom);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Found " + romList.size() + " roms.");
        return romList;
    }

    private static void moveFile(Rom rom, String targetDir, boolean sortRegion, boolean sortAlphabet, boolean dryrun){
        targetDir += File.separator;

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
                copy(rom.getPath(), Paths.get(targetDir + rom.getFilename()), REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(targetDir + rom.getFilename());
        return;
    }
}
