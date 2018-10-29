package hu.ember;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.move;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Main {

    public static void main(String[] args) {
        startSorter(args[0],Boolean.valueOf(args[1]),Boolean.valueOf(args[2]),Boolean.valueOf(args[3]));
    }

    private static void startSorter(String sourceDir, boolean sortRegion, boolean sortAlphabet, boolean dryrun){
        try (Stream<Path> paths = Files.walk(Paths.get(sourceDir))) {
            List<Path> list = paths
                    .filter(Files::isRegularFile)
                    .sorted()
                    .collect(Collectors.toList());

            list = filterList(list);

            for(Path path : list){
                moveFile(path, sourceDir, sortRegion, sortAlphabet, dryrun);
                System.out.println(list.indexOf(path) + " of " + list.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Path> filterList(List<Path> list){
        return list.stream()
                .filter(path -> !path.getFileName().toString().contains("[h"))
                .filter(path -> !path.getFileName().toString().contains("[b"))
                .filter(path -> !path.getFileName().toString().contains("[a"))
                .filter(path -> !path.getFileName().toString().contains("[o"))
                .filter(path -> !path.getFileName().toString().contains("[t"))
                .filter(path -> !path.getFileName().toString().contains("(F)"))
                .filter(path -> !path.getFileName().toString().contains("(G)"))
                .filter(path -> !path.getFileName().toString().contains("(PD)"))
                .filter(path -> !path.getFileName().toString().contains("(S)"))
                .filter(path -> !path.getFileName().toString().contains("(Beta)"))
                //.filter(path -> !path.getFileName().toString().contains("[T"))
                .filter(path -> !path.getFileName().toString().contains("Hack"))
                .filter(path -> !path.getFileName().toString().contains("Demo"))
                .collect(Collectors.toList());
    }

    private static Path moveFile(Path path, String targetDir, boolean sortRegion, boolean sortAlphabet, boolean dryrun){

        if(sortRegion){
            Pattern pattern = Pattern.compile("\\([E,U,J]\\)");
            Matcher matcher = pattern.matcher(path.getFileName().toString());
            if (matcher.find())
            {
                String regioncode = matcher.group(0).replace("(","").replace(")","");
                if("E".equals(regioncode) || "E".equals(regioncode)){
                    targetDir +=  "PAL" + File.separator;
                }else if("U".equals(regioncode)){
                    targetDir +=  "NTSC-U" + File.separator;
                }else if("J".equals(regioncode)){
                    targetDir +=  "NTSC-J" + File.separator;
                }
            }else{
                targetDir +=  "Other" + File.separator;
            }
        }

        if(sortAlphabet) {
            String firstLetter = path.getFileName().toString().substring(0, 1).toUpperCase();
            if (firstLetter.matches("[0-9]")) {
                firstLetter = "0-9";
            }
            targetDir += firstLetter + File.separator;
        }

        System.out.println(targetDir + path.getFileName());
        if(!dryrun) {
            try {
                File directory = new File(targetDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                move(path, Paths.get(targetDir + path.getFileName()), REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}
