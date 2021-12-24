package com.github.alfonsoleandro.autopickup.utils;

import com.github.alfonsoleandro.mputils.misc.MessageEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Message implements MessageEnum {
    AP_BLOCKS_ENABLED("config.messages.autoPickup.blocks.enabled","&fBlock drops AutoPickup &fhas been &aenabled &ffor you"),
    AP_BLOCKS_DISABLED("config.messages.autoPickup.blocks.disabled","&fBlock drops AutoPickup &fhas been &cdisabled &ffor you"),
    AP_MOB_ENABLED("config.messages.autoPickup.mob.enabled","&fMob drops AutoPickup &fhas been &aenabled &ffor you"),
    AP_MOB_DISABLED("config.messages.autoPickup.mob.disabled","&fMob drops AutoPickup &fhas been &cdisabled &ffor you"),
    AP_EXP_ENABLED("config.messages.autoPickup.exp.enabled","&fExperience AutoPickup &fhas been &aenabled &ffor you"),
    AP_EXP_DISABLED("config.messages.autoPickup.exp.disabled","&fExperience AutoPickup &fhas been &cdisabled &ffor you"),
    AS_BLOCKS_ENABLED("config.messages.autoSmelt.blocks.enabled","&fBlock drops AutoSmelt &fhas been &aenabled &ffor you"),
    AS_BLOCKS_DISABLED("config.messages.autoSmelt.blocks.disabled","&fBlock drops AutoSmelt &fhas been &cdisabled &ffor you"),
    AS_MOB_ENABLED("config.messages.autoSmelt.mob.enabled","&fMob drops AutoSmelt &fhas been &aenabled &ffor you"),
    AS_MOB_DISABLED("config.messages.autoSmelt.mob.disabled","&fMob drops AutoSmelt &fhas been &cdisabled &ffor you"),
    CAREFUL_BREAK_ENABLED("config.messages.careful break.enabled","&fCareful break &fhas been &aenabled &ffor you"),
    CAREFUL_BREAK_DISABLED("config.messages.careful break.disabled","&fCareful break &fhas been &cdisabled &ffor you"),
    CAREFUL_BREAK_DISABLED_IN_CONFIG("config.messages.careful break.disabled in config","&cCareful break is disabled for this server"),
    CAREFUL_SMELT_ENABLED("config.messages.careful smelt.enabled","&fCareful smelt &fhas been &aenabled &ffor you"),
    CAREFUL_SMELT_DISABLED("config.messages.careful smelt.disabled","&fCareful smelt &fhas been &cdisabled &ffor you"),
    CAREFUL_SMELT_DISABLED_IN_CONFIG("config.messages.careful smelt.disabled in config","&cCareful smelt is disabled for this server"),

    CANNOT_CONSOLE("config.messages.cannot send from console", "&cThat command can only be sent by a player"),
    FULL_INV("&cYour inventory is full. &fSome items may be dropped to the ground"),
    NO_PERMISSION("&cNo permission"),
    PLACEHOLDER_ENABLED("config.messages.placeholder status.enabled", "&aEnabled"),
    PLACEHOLDER_DISABLED("config.messages.placeholder status.disabled", "&cDisabled"),


    ;

    private final String path;
    private final String dflt;

    Message(String path, String dflt){
        this.path = path;
        this.dflt = dflt;
    }

    Message(String dflt){
        this(null, dflt);
    }

    @NotNull
    @Override
    public String getPath() {
        return this.path == null ?
                "config.messages."+this.toString().toLowerCase(Locale.ROOT).replace("_"," ")
                :
                this.path;
    }

    @NotNull
    @Override
    public String getDefault() {
        return this.dflt;
    }
}
