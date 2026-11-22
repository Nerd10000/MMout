/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class null extends Slot {
/*     */   null(Container container, int slot, int x, int y) {
/*  64 */     super(container, slot, x, y);
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack itemStack) {
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/*  72 */     access.execute((level, pos) -> {
/*     */           if (level instanceof ServerLevel) {
/*     */             ServerLevel serverLevel = (ServerLevel)level;
/*     */             ExperienceOrb.award(serverLevel, Vec3.atCenterOf((Vec3i)pos), getExperienceAmount(level));
/*     */           } 
/*     */           level.levelEvent(1042, pos, 0);
/*     */         });
/*  79 */     GrindstoneMenu.this.repairSlots.setItem(0, ItemStack.EMPTY);
/*  80 */     GrindstoneMenu.this.repairSlots.setItem(1, ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   private int getExperienceAmount(Level level) {
/*  84 */     int amount = 0;
/*  85 */     amount += getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(0));
/*  86 */     amount += getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(1));
/*  88 */     if (amount > 0) {
/*  89 */       int halfAmount = (int)Math.ceil(amount / 2.0D);
/*  90 */       return halfAmount + level.getRandom().nextInt(halfAmount);
/*     */     } 
/*  93 */     return 0;
/*     */   }
/*     */   
/*     */   private int getExperienceFromItem(ItemStack item) {
/*  97 */     int amount = 0;
/*  98 */     ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(item);
/*  99 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : (Iterable<Object2IntMap.Entry<Holder<Enchantment>>>)enchantments.entrySet()) {
/* 100 */       Holder<Enchantment> enchant = (Holder<Enchantment>)entry.getKey();
/* 101 */       int lvl = entry.getIntValue();
/* 103 */       if (!enchant.is(EnchantmentTags.CURSE))
/* 104 */         amount += ((Enchantment)enchant.value()).getMinCost(lvl); 
/*     */     } 
/* 108 */     return amount;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\GrindstoneMenu$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */