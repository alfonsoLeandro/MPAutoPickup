package com.github.alfonsoleandro.autopickup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainCommandTabCompleter implements TabCompleter {


    public boolean equalsToStrings(String input, String string){
        return input.equalsIgnoreCase(string.substring(0, Math.min(input.length(), string.length())));
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> list = new ArrayList<>();

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("")) {
                list.add("help");
                list.add("version");
                list.add("reload");
                list.add("toggle");
                list.add("enable");
                list.add("disable");

            } else if(equalsToStrings(args[0], "help")) {
                list.add("help");

            } else if(equalsToStrings(args[0], "version")) {
                list.add("version");

            } else if(equalsToStrings(args[0], "reload")) {
                list.add("reload");
            } else if(equalsToStrings(args[0], "toggle")) {
                list.add("toggle");
            } else if(equalsToStrings(args[0], "enable")){
                list.add("enable");
            } else if(equalsToStrings(args[0], "disable")){
                list.add("disable");
            }

        }

        return list;
    }


}
