/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface Filter {
/*     */   public static final Filter NOOP;
/*     */   
/*     */   BlockInput filter(BoundingBox paramBoundingBox, BlockPos paramBlockPos, BlockInput paramBlockInput, ServerLevel paramServerLevel);
/*     */   
/*     */   static {
/* 192 */     NOOP = ((r, p, b, l) -> b);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FillCommand$Filter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */