package me.stivodavco.trimAbilities.utilities;

import java.util.Random;

public class RandomnessUtils {
    public static boolean chance(int percent) {
        int random = new Random().nextInt(100);
        return random < Math.clamp(percent, 1, 99);
    }
}
