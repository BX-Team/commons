package org.bxteam.commons.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;

/**
 * Utility class for handling item manipulations.
 */
public final class ItemUtil {
    private ItemUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Removes a specified amount of items of a given type from the player's inventory.
     *
     * @param player the player whose inventory to modify
     * @param type the type of item to remove
     * @param amount the amount of items to remove
     */
    public static void removeItem(Player player, Material type, int amount) {
        if (amount <= 0) return;

        PlayerInventory inventory = player.getInventory();
        int remainingAmount = amount;

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() != type) continue;

            int itemAmount = item.getAmount();

            if (itemAmount > remainingAmount) {
                item.setAmount(itemAmount - remainingAmount);
                break;
            } else {
                inventory.clear(slot);
                remainingAmount -= itemAmount;
                if (remainingAmount == 0) break;
            }
        }
    }

    /**
     * Gives an item to the player, or drops it at the player's location if their inventory is full.
     *
     * @param player the player to give the item to
     * @param itemStack the item to give
     */
    public static void giveItem(Player player, ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() <= 0) return;

        PlayerInventory inventory = player.getInventory();
        int maxStackSize = itemStack.getMaxStackSize();
        int amount = itemStack.getAmount();

        while (amount > 0) {
            int giveAmount = Math.min(amount, maxStackSize);
            ItemStack stackToGive = new ItemStack(itemStack);
            stackToGive.setAmount(giveAmount);

            if (hasSpace(inventory, stackToGive)) {
                inventory.addItem(stackToGive);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), stackToGive);
            }

            amount -= giveAmount;
        }
    }

    /**
     * Checks if there is enough space in the inventory to add the specified item.
     *
     * @param inventory the inventory to check
     * @param itemStack the item to check for space
     * @return true if there is space for the item, false otherwise
     */
    @ApiStatus.Internal
    private static boolean hasSpace(PlayerInventory inventory, ItemStack itemStack) {
        HashMap<Integer, ItemStack> leftover = inventory.addItem(itemStack);
        inventory.removeItem(itemStack);
        return leftover.isEmpty();
    }
}
