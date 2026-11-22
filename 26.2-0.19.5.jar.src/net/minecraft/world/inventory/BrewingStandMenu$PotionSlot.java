/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ 
/*     */ class PotionSlot extends Slot {
/*     */   public PotionSlot(Container container, int slot, int x, int y) {
/* 134 */     super(container, slot, x, y);
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack itemStack) {
/* 139 */     return mayPlaceItem(itemStack);
/*     */   }
/*     */   
/*     */   public int getMaxStackSize() {
/* 144 */     return 1;
/*     */   }
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/* 149 */     Optional<Holder<Potion>> potion = ((PotionContents)carried.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).potion();
/* 150 */     if (potion.isPresent() && player instanceof ServerPlayer) {
/* 150 */       ServerPlayer serverPlayer = (ServerPlayer)player;
/* 151 */       CriteriaTriggers.BREWED_POTION.trigger(serverPlayer, potion.get());
/*     */     } 
/* 153 */     super.onTake(player, carried);
/*     */   }
/*     */   
/*     */   public static boolean mayPlaceItem(ItemStack itemStack) {
/* 157 */     return (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.GLASS_BOTTLE));
/*     */   }
/*     */   
/*     */   public Identifier getNoItemIcon() {
/* 162 */     return BrewingStandMenu.EMPTY_SLOT_POTION;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\BrewingStandMenu$PotionSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */