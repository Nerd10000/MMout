/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
/*    */ 
/*    */ public class FurnaceResultSlot extends Slot {
/*    */   private final Player player;
/*    */   
/*    */   private int removeCount;
/*    */   
/*    */   public FurnaceResultSlot(Player player, Container container, int slot, int x, int y) {
/* 14 */     super(container, slot, x, y);
/* 15 */     this.player = player;
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   public ItemStack remove(int amount) {
/* 25 */     if (hasItem())
/* 26 */       this.removeCount += Math.min(amount, getItem().getCount()); 
/* 28 */     return super.remove(amount);
/*    */   }
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {
/* 33 */     checkTakeAchievements(carried);
/* 34 */     super.onTake(player, carried);
/*    */   }
/*    */   
/*    */   protected void onQuickCraft(ItemStack picked, int count) {
/* 39 */     this.removeCount += count;
/* 40 */     checkTakeAchievements(picked);
/*    */   }
/*    */   
/*    */   protected void checkTakeAchievements(ItemStack carried) {
/* 45 */     carried.onCraftedBy(this.player, this.removeCount);
/* 46 */     Player player = this.player;
/* 46 */     if (player instanceof ServerPlayer) {
/* 46 */       ServerPlayer serverPlayer = (ServerPlayer)player;
/* 47 */       Container container = this.container;
/* 47 */       if (container instanceof AbstractFurnaceBlockEntity) {
/* 47 */         AbstractFurnaceBlockEntity abstractFurnaceBlockEntity = (AbstractFurnaceBlockEntity)container;
/* 48 */         abstractFurnaceBlockEntity.awardUsedRecipesAndPopExperience(serverPlayer);
/*    */       } 
/*    */     } 
/* 51 */     this.removeCount = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\FurnaceResultSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */