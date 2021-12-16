package com.github.alfonsoleandro.autopickup.utils;

import com.github.alfonsoleandro.mputils.misc.MessageEnum;
import org.jetbrains.annotations.NotNull;

public enum Message implements MessageEnum {


    ;

    private final String path;
    private final String dflt;

    Message(String path, String dflt){
        this.path = path;
        this.dflt = dflt;
    }

    @NotNull
    @Override
    public String getPath() {
        return null;
    }

    @NotNull
    @Override
    public String getDefault() {
        return null;
    }
}
