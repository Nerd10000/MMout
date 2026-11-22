/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestAssertPosException extends GameTestAssertException {
/*    */   private final BlockPos absolutePos;
/*    */   
/*    */   private final BlockPos relativePos;
/*    */   
/*    */   public GameTestAssertPosException(Component baseMessage, BlockPos absolutePos, BlockPos relativePos, int tick) {
/* 12 */     super(baseMessage, tick);
/* 13 */     this.absolutePos = absolutePos;
/* 14 */     this.relativePos = relativePos;
/*    */   }
/*    */   
/*    */   public Component getDescription() {
/* 19 */     return (Component)Component.translatable("test.error.position", new Object[] { this.message, Integer.valueOf(this.absolutePos.getX()), Integer.valueOf(this.absolutePos.getY()), Integer.valueOf(this.absolutePos.getZ()), Integer.valueOf(this.relativePos.getX()), Integer.valueOf(this.relativePos.getY()), Integer.valueOf(this.relativePos.getZ()), Integer.valueOf(this.tick) });
/*    */   }
/*    */   
/*    */   public Component getMessageToShowAtBlock() {
/* 23 */     return this.message;
/*    */   }
/*    */   
/*    */   public BlockPos getRelativePos() {
/* 27 */     return this.relativePos;
/*    */   }
/*    */   
/*    */   public BlockPos getAbsolutePos() {
/* 31 */     return this.absolutePos;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestAssertPosException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */