package com.github.alfonsoleandro.autopickup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainCommandTabCompleter implements TabCompleter {


    public boolean equalsToStrings(String input, String string){
        for(int i = 0; i < string.length(); i++){
            if(input.equalsIgnoreCase(string.substring(0,i))){
                return true;
            }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> lista = new ArrayList<>();

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("")) {
                lista.add("help");
                lista.add("version");
                lista.add("reload");
                lista.add("toggle");

            } else if(equalsToStrings(args[0], "help")) {
                lista.add("help");

            } else if(equalsToStrings(args[0], "version")) {
                lista.add("version");

            } else if(equalsToStrings(args[0], "reload")) {
                lista.add("reload");

            } else if(equalsToStrings(args[0], "toggle")) {
                lista.add("toggle");
            }

        }

        return lista;
    }


}
