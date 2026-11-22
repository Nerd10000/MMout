/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public class GlowInkSacItem extends Item implements SignApplicator {
/*    */   public GlowInkSacItem(Item.Properties properties) {
/* 11 */     super(properties);
/*    */   }
/*    */   
/*    */   public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, ItemStack item, Player player) {
/* 16 */     if (sign.updateText(text -> text.setHasGlowingText(true), isFrontText)) {
/* 17 */       level.playSound(null, sign.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 18 */       return true;
/*    */     } 
/* 20 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\GlowInkSacItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */