# ROMsorter
Sorter app for emulator rom files by region and alphabet.

This tool lets you organize and filter your rom collection. You can sort the games into folders by their region, create 
subfolders alphabetically and filter out the unneeded rom versions like hacks, translations, beta roms.
Right now it is a commandline only application.

```
java -jar RomSorter.jar
usage:
 -a                  sort by alphabet
 -d                  testrun without moving files
 -f,--filter <arg>   romtype whitelist separated by commas -f
                     h,!,b,a,f,o,p,t,T,U
 -i,--input <arg>    input folder
 -o,--output <arg>   output folder
 -r                  sort by region
 ```