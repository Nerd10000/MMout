/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface Affector {
/*     */   public static final Affector NOOP = (l, p) -> false;
/*     */   
/*     */   boolean affect(ServerLevel paramServerLevel, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FillCommand$Affector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */