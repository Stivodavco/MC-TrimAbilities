package me.stivodavco.trimAbilities.abstractClasses;

import org.bukkit.inventory.meta.trim.ArmorTrim;

import java.util.HashMap;
import java.util.List;

public abstract class TrimSet {
    public HashMap<Integer, ArmorTrim> armor = new HashMap<>(); // Integer = inventory slot

}
