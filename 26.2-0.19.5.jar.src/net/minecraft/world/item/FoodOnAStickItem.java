/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.ItemSteerable;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class FoodOnAStickItem<T extends Entity & ItemSteerable> extends Item {
/*    */   private final EntityType<T> canInteractWith;
/*    */   
/*    */   private final int consumeItemDamage;
/*    */   
/*    */   public FoodOnAStickItem(EntityType<T> canInteractWith, int consumeItemDamage, Item.Properties properties) {
/* 18 */     super(properties);
/* 20 */     this.canInteractWith = canInteractWith;
/* 21 */     this.consumeItemDamage = consumeItemDamage;
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 26 */     ItemStack itemStack = player.getItemInHand(hand);
/* 27 */     if (level.isClientSide())
/* 28 */       return (InteractionResult)InteractionResult.PASS; 
/* 31 */     Entity vehicle = player.getControlledVehicle();
/* 33 */     if (player.isPassenger() && vehicle instanceof ItemSteerable) {
/* 33 */       ItemSteerable steerable = (ItemSteerable)vehicle;
/* 33 */       if (vehicle.is(this.canInteractWith) && 
/* 34 */         steerable.boost()) {
/* 35 */         EquipmentSlot slot = hand.asEquipmentSlot();
/* 36 */         ItemStack result = itemStack.hurtAndConvertOnBreak(this.consumeItemDamage, Items.FISHING_ROD, (LivingEntity)player, slot);
/* 37 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(result);
/*    */       } 
/*    */     } 
/* 41 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 43 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\FoodOnAStickItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */