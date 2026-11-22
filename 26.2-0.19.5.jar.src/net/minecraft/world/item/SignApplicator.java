/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public interface SignApplicator {
/*    */   boolean tryApplyToSign(Level paramLevel, SignBlockEntity paramSignBlockEntity, boolean paramBoolean, ItemStack paramItemStack, Player paramPlayer);
/*    */   
/*    */   default boolean canApplyToSign(SignText text, ItemStack item, Player player) {
/* 13 */     return text.hasMessage(player);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\SignApplicator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */