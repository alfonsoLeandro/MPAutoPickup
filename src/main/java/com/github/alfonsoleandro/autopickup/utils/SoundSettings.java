package com.github.alfonsoleandro.autopickup.utils;

import org.bukkit.Sound;

import java.util.concurrent.ThreadLocalRandom;

public class SoundSettings {

    private final boolean enabled;
    private final Sound sound;
    private final double minVolume;
    private final double maxVolume;
    private final double minPitch;
    private final double maxPitch;

    public SoundSettings(boolean enabled, Sound sound, String volumeMinMax, String pitchMinMax){
        String[] volume = volumeMinMax.split("-");
        String[] pitch = pitchMinMax.split("-");

        this.enabled = enabled;
        this.sound = sound;
        this.minVolume = Double.parseDouble(volume[0]);
        this.maxVolume = volume.length > 1 ? Double.parseDouble(volume[1]) : Double.parseDouble(volume[0]);
        this.minPitch = Double.parseDouble(pitch[0]);
        this.maxPitch = pitch.length > 1 ? Double.parseDouble(pitch[1]) : Double.parseDouble(pitch[0]);
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    public Sound getSound() {
        return this.sound;
    }

    public float getPitch() {
        return this.minPitch == this.maxPitch ? (float) this.minPitch : (float)ThreadLocalRandom.current().nextDouble(this.minPitch, this.maxPitch);
    }

    public float getVolume() {
        return this.minVolume == this.maxVolume ? (float) this.minVolume : (float)ThreadLocalRandom.current().nextDouble(this.minVolume, this.maxVolume);
    }
}
