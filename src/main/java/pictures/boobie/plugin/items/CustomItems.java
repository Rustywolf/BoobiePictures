package pictures.boobie.plugin.items;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItems {
    
    public static ItemStack YOUR_STICK = 
            CustomItems.createNew(new ItemStack(Material.STICK))
                    .withName(ChatColor.WHITE + "" + ChatColor.BOLD + "Your Stick")
                    .withLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right Click: Stroke Stick")
                    .getItemStack();
    
    public static ItemStack NEXT_BUTTON = 
            CustomItems.createNew(new ItemStack(Material.INK_SACK, 1, (byte)2))
                    .withName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Next Result")
                    .withLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right Click: Next Result")
                    .getItemStack();
    
    public static ItemStack PREV_BUTTON = 
            CustomItems.createNew(new ItemStack(Material.INK_SACK, 1, (byte)1))
                    .withName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Previous Result")
                    .withLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right Click: Previous Result")
                    .getItemStack();
    
    public static ItemStack CHANGE_SUB = 
            CustomItems.createNew(new ItemStack(Material.PAPER))
                    .withName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Change Subreddit")
                    .withLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right Click: Change Subreddit")
                    .getItemStack();
    
    public static CustomItem createNew(ItemStack item) {
        return new CustomItem(item);
    }
    
    public static boolean compareItems(ItemStack one, ItemStack two) {
        if (one.getType() != two.getType()) {
            return false;
        }
        
        if (!one.hasItemMeta() || !one.getItemMeta().hasDisplayName() || !two.hasItemMeta() || !two.getItemMeta().hasDisplayName()) {
            return false;
        }
        
        return (one.getItemMeta().getDisplayName().equals(two.getItemMeta().getDisplayName()));
    }
    
    public static class CustomItem {
        
        private ItemStack item;
        
        public CustomItem(ItemStack item) {
            this.item = item;
        }
        
        public CustomItem withName(String name) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
            return this;
        }
        
        public CustomItem withLore(String... args) {
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Arrays.asList(args));
            item.setItemMeta(meta);
            return this;
        }
        
        public CustomItem withEnchantment(Enchantment enchantment, int level) {
            item.addUnsafeEnchantment(enchantment, level);
            return this;
        }
        
        public ItemStack getItemStack() {
            return this.item;
        }
        
    }
    
}
