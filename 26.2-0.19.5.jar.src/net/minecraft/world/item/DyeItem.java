/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public class DyeItem extends Item implements SignApplicator {
/*    */   public DyeItem(Item.Properties properties) {
/* 16 */     super(properties);
/*    */   }
/*    */   
/*    */   public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
/* 21 */     if (target instanceof Sheep) {
/* 21 */       Sheep sheep = (Sheep)target;
/* 21 */       if (sheep.isAlive() && !sheep.isSheared()) {
/* 22 */         DyeColor dyeColor = (DyeColor)itemStack.get(DataComponents.DYE);
/* 23 */         if (dyeColor != null && sheep.getColor() != dyeColor) {
/* 24 */           sheep.level().playSound((Entity)player, (Entity)sheep, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
/* 25 */           if (!player.level().isClientSide()) {
/* 26 */             sheep.setColor(dyeColor);
/* 27 */             itemStack.shrink(1);
/*    */           } 
/* 29 */           return (InteractionResult)InteractionResult.SUCCESS;
/*    */         } 
/*    */       } 
/*    */     } 
/* 32 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, ItemStack item, Player player) {
/* 37 */     DyeColor dye = (DyeColor)item.get(DataComponents.DYE);
/* 38 */     if (dye != null && 
/* 39 */       sign.updateText(text -> text.setColor(dye), isFrontText)) {
/* 40 */       level.playSound(null, sign.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 41 */       return true;
/*    */     } 
/* 44 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\DyeItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */