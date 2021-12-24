package com.github.alfonsoleandro.autopickup.managers;

public class AutoPickupSettings {

    private final boolean[] settings = new boolean[7];

    public AutoPickupSettings(boolean autoPickupBlocks,
                              boolean autoPickupMobDrops,
                              boolean autoPickupExpOrbs,
                              boolean autoSmeltBlocks,
                              boolean autoSmeltMob,
                              boolean carefulBreak,
                              boolean carefulSmelt){
        this.settings[0] = autoPickupBlocks;
        this.settings[1] = autoPickupMobDrops;
        this.settings[2] = autoPickupExpOrbs;
        this.settings[3] = autoSmeltBlocks;
        this.settings[4] = autoSmeltMob;
        this.settings[5] = carefulBreak;
        this.settings[6] = carefulSmelt;
    }


    public boolean autoPickupBlocksEnabled(){
        return this.settings[0];
    }

    public boolean autoPickupMobDropsEnabled(){
        return this.settings[1];
    }

    public boolean autoPickupExpEnabled(){
        return this.settings[2];
    }

    public boolean autoSmeltBlocksEnabled(){
        return this.settings[3];
    }

    public boolean autoSmeltMobEnabled(){
        return this.settings[4];
    }

    public boolean carefulBreakEnabled(){
        return this.settings[5];
    }

    public boolean carefulSmeltEnabled(){
        return this.settings[6];
    }


    public void setAutoPickupBlocks(boolean value){
        this.settings[0] = value;
    }

    public void setAutoPickupMobDrops(boolean value){
        this.settings[1] = value;
    }

    public void setAutoPickupExp(boolean value){
        this.settings[2] = value;
    }

    public void setAutoSmeltBlocks(boolean value){
        this.settings[3] = value;
    }

    public void setAutoSmeltMobs(boolean value){
        this.settings[4] = value;
    }

    public void setCarefulBreak(boolean value){
        this.settings[5] = value;
    }

    public void setCarefulSmelt(boolean value){
        this.settings[6] = value;
    }


}
