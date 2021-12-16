package com.github.alfonsoleandro.autopickup.managers;

public class AutoPickupSettings {

    private final boolean[] settings = new boolean[5];

    public AutoPickupSettings(boolean autoPickupBlocks,
                              boolean autoPickupMobDrops,
                              boolean autoPickupExpOrbs,
                              boolean autoSmeltBlocks,
                              boolean autoSmeltMob){
        this.settings[0] = autoPickupBlocks;
        this.settings[1] = autoPickupMobDrops;
        this.settings[2] = autoPickupExpOrbs;
        this.settings[3] = autoSmeltBlocks;
        this.settings[4] = autoSmeltMob;
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


}
