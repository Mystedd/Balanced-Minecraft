package com.balancedmc.sounds;

import com.balancedmc.Main;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {

    public ModSoundEvents() {}

    public static void registerSoundEvents() {}

    public static SoundEvent MUSIC_DISC_INTRO = registerRecord("intro");
    public static SoundEvent MUSIC_DISC_DROOPY_LIKES_RICOCHET = registerRecord("droopy_likes_ricochet");
    public static SoundEvent MUSIC_DISC_DROOPY_LIKES_YOUR_FACE = registerRecord("droopy_likes_your_face");
    public static SoundEvent MUSIC_DISC_DOG = registerRecord("dog");
    public static SoundEvent MUSIC_DISC_DEATH = registerRecord("death");
    public static SoundEvent MUSIC_DISC_KEY = registerRecord("key");
    public static SoundEvent MUSIC_DISC_DOOR = registerRecord("door");

    private static SoundEvent registerRecord(String name) {
        Identifier id = new Identifier(Main.MOD_ID, "music_disc." + name);
        SoundEvent event = SoundEvent.of(id, 75.0F);
        return Registry.register(Registries.SOUND_EVENT, id, event);
    }

}
