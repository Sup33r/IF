package com.github.stefvanschie.inventoryframework.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

/**
 * A utility class for working with skulls
 *
 * @since 0.5.0
 */
public final class SkullUtil {

    /**
     * A private constructor to ensure this class isn't instantiated
     *
     * @since 0.5.0
     */
    private SkullUtil() {}

    /**
     * Gets a skull from the specified id. The id is the value from the textures.minecraft.net website after the last
     * '/' character.
     *
     * @param id the skull id
     * @return the skull item
     * @since 0.5.0
     */
    @NotNull
    public static ItemStack getSkull(@NotNull String id) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        assert itemMeta != null;
        setSkull(itemMeta, id);
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Sets the skull of an existing {@link SkullMeta} from the specified id.
     * The id is the value from the textures.minecraft.net website after the last '/' character.
     *
     * @param meta the meta to change
     * @param id the skull id
     */
    public static void setSkull(@NotNull SkullMeta meta, @NotNull String id) {
        UUID uuid = UUID.randomUUID();
        String textureUrl = "http://textures.minecraft.net/texture/" + id;
        String encodedData = Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", textureUrl).getBytes());

        // Create a GameProfile and set the texture property
        GameProfile profile = new GameProfile(uuid, null);
        profile.getProperties().put("textures", new Property("textures", encodedData));

        // Use reflection to set the GameProfile on the SkullMeta
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
