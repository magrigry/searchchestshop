package com.over2craft.searchchestshop2.Inventories;

import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.over2craft.searchchestshop2.Manager.SignWrapper;
import com.over2craft.searchchestshop2.Manager.SignsFilter;
import com.over2craft.searchchestshop2.Manager.SignsManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ItemsInventory implements InventoryProvider {

    private final SignsFilter filters;

    public ItemsInventory(SignsFilter filters) {
        this.filters = filters;
    }

    public ClickableItem[] getIems() {

        if (filters.signs.size() == 1) {

            HashMap<String, SignWrapper> signsWrappers =  filters.signs.get(filters.signs.keySet().toArray()[0]);

            ClickableItem[] items = new ClickableItem[signsWrappers.size()];
            int i = 0;
            for (Map.Entry<String, SignWrapper> entry : signsWrappers.entrySet()) {

                SignWrapper signWrapper = entry.getValue();
                ItemStack item = new ItemStack(signWrapper.getItemStack());

                ItemMeta im = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
                List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<>();

                lore.add("");

                if (!signWrapper.getExactBuyPrice().equals(BigDecimal.valueOf(-1))) {
                    lore.add(String.format("§aAchetez ici pour §f%s Overs", signWrapper.getExactBuyPrice()));
                }

                if (!signWrapper.getExactSellPrice().equals(BigDecimal.valueOf(-1))) {
                    lore.add(String.format("§aVendez ici pour §f%s Overs", signWrapper.getExactSellPrice()));
                }

                lore.add("");
                lore.add(String.format("§aShop de §f%s", signWrapper.getLine(0)));

                im.setLore(lore);
                item.setItemMeta(im);

                items[i] =  ClickableItem.of(item, e -> e.getWhoClicked().teleport(signWrapper.getLocation()));

                i++;
            }

            return items;
        }

        ClickableItem[] items = new ClickableItem[filters.signs.size()];
        int i = 0;

        for (String key : filters.signs.keySet()) {

            SignWrapper buyLowerPrice = null;
            SignWrapper sellHigherPrice = null;
            boolean aPlayerHasASign = false;

            for (Map.Entry<String, SignWrapper> entry : filters.signs.get(key).entrySet()) {

                String id = entry.getKey();
                SignWrapper sign = entry.getValue();

                if (!ChestShopSign.isAdminShop(sign.getLine(0))) {
                    aPlayerHasASign = true;
                }

                if (buyLowerPrice == null && sign.hasBuyPrice()) {
                    buyLowerPrice = sign;
                }

                if (sign.hasBuyPrice()
                    && (buyLowerPrice.getExactBuyPrice().divide(BigDecimal.valueOf(Integer.parseInt(buyLowerPrice.getLine(1))), RoundingMode.HALF_UP)).compareTo(sign.getExactBuyPrice()) > 0) {
                    buyLowerPrice = sign;
                }

                if (sellHigherPrice == null && sign.hasSellPrice()) {
                    sellHigherPrice = sign;
                }

                if (sign.hasSellPrice()
                    && (sellHigherPrice.getExactSellPrice().divide(BigDecimal.valueOf(Integer.parseInt(sellHigherPrice.getLine(1))), RoundingMode.HALF_UP)).compareTo(sign.getExactSellPrice()) < 0) {
                    sellHigherPrice = sign;
                }
            }

            if (buyLowerPrice == null && sellHigherPrice == null) {
                throw new RuntimeException("An item has no price");
            }

            SignWrapper finalSign = buyLowerPrice == null ? sellHigherPrice : buyLowerPrice;

            ItemStack item = new ItemStack(finalSign.getItemStack().getType());
            ItemMeta im = Bukkit.getItemFactory().getItemMeta(item.getType());

            item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            //im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            List<String> lore = im.getLore() == null ? new ArrayList<>() : im.getLore();

            if (buyLowerPrice != null && buyLowerPrice.hasBuyPrice()) {
                lore.add(String.format("§f%s §avend le moins cher pour §f%s Overs", buyLowerPrice.getLine(0), buyLowerPrice.getExactBuyPrice()));
            }

            if (sellHigherPrice != null && sellHigherPrice.hasSellPrice()) {
                lore.add(String.format("§f%s §aachète pour le plus cher à §f%s Overs", sellHigherPrice.getLine(0), sellHigherPrice.getExactSellPrice()));
            }

            im.setLore(lore);
            item.setItemMeta(im);

            items[i] = ClickableItem.of(item, e -> SmartInv.getInventory(filters.LikeItemId(finalSign.getItemId())).open((Player) e.getWhoClicked()));

            i++;
        }


        return items;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        Pagination pagination = contents.pagination();

        pagination.setItems(getIems());
        pagination.setItemsPerPage(45);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        if (!filters.stackFiltered) {

            contents.set(5, 0, ClickableItem.of(
                    createGuiItem(Material.DIAMOND_SWORD,
                            "Item non stackable",
                            "N'affichez que les items",
                            "qui ne sont pas stackable",
                            "(épée, pioche, etc...)"),
                    e -> SmartInv.getInventory(filters.notStackable()).open(player)));

            contents.set(5, 1, ClickableItem.of(createGuiItem(Material.DIAMOND,
                    "Items stackable", "(comme le bois, le diamants, etc...)"),
                    e -> SmartInv.getInventory(filters.stackable()).open(player)));

        } else {

            contents.set(5, 0, ClickableItem.empty(createGuiItem(Material.BARRIER,
                    "Filtre déjà appliqué", "Utilise le papier pour", "reset les filtres")));

            contents.set(5, 1, ClickableItem.empty(createGuiItem(Material.BARRIER,
                    "Filtre déjà appliqué", "Utilise le papier pour", "reset les filtres")));
        }

        contents.set(5, 3, ClickableItem.of(createGuiItem(Material.ARROW, "Page précédente"),
                e -> SmartInv.getInventory(filters).open(player, pagination.previous().getPage())));

        contents.set(5, 4, ClickableItem.of(createGuiItem(Material.PAPER, "Remettre à 0 les filtres"),
                e -> SmartInv.getInventory(new SignsFilter(SignsManager.signs)).open(player)));

        contents.set(5, 5, ClickableItem.of(createGuiItem(Material.ARROW, "Page suivante"),
                e -> SmartInv.getInventory(filters).open(player, pagination.next().getPage())));

        if (!filters.sellOrByFiltered) {

            contents.set(5, 7, ClickableItem.of(createGuiItem(Material.HONEYCOMB,
                    "Vente uniquement", "Afficher uniquement les items", "que vous pouvez vendre auprès", "des shops"),
                    e -> SmartInv.getInventory(filters.sellableOnly()).open(player)));

            contents.set(5, 8, ClickableItem.of(createGuiItem(Material.STONE,
                    "Achat uniquement", "Afficher uniquement les items", "que vous pouvez acheter aurpès", "des shops"),
                    e -> SmartInv.getInventory(filters.buyableOnly()).open(player)));
        } else {
            contents.set(5, 7, ClickableItem.empty(createGuiItem(Material.BARRIER,
                    "Filtre déjà appliqué", "Utilise le papier pour", "reset les filtres")));

            contents.set(5, 8, ClickableItem.empty(createGuiItem(Material.BARRIER,
                    "Filtre déjà appliqué", "Utilise le papier pour", "reset les filtres")));
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {}

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§b§l" + name);

        ArrayList<String> loreA = new ArrayList<>();

        for (String str:
             lore) {
            loreA.add("§a" + str);
        }

        meta.setLore(loreA);

        item.setItemMeta(meta);

        return item;
    }

}