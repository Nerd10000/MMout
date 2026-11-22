/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ enum Mode {
/*     */   DESTROY,
/*     */   HOLLOW,
/*     */   OUTLINE,
/* 164 */   REPLACE(FillCommand.Affector.NOOP, FillCommand.Filter.NOOP);
/*     */   
/*     */   public final FillCommand.Affector affector;
/*     */   
/*     */   public final FillCommand.Filter filter;
/*     */   
/*     */   static {
/* 165 */     OUTLINE = new Mode("OUTLINE", 1, FillCommand.Affector.NOOP, (r, p, b, l) -> 
/* 166 */         (p.getX() == r.minX() || p.getX() == r.maxX() || p.getY() == r.minY() || p.getY() == r.maxY() || p.getZ() == r.minZ() || p.getZ() == r.maxZ()) ? b : null);
/* 172 */     HOLLOW = new Mode("HOLLOW", 2, FillCommand.Affector.NOOP, (r, p, b, l) -> 
/* 173 */         (p.getX() == r.minX() || p.getX() == r.maxX() || p.getY() == r.minY() || p.getY() == r.maxY() || p.getZ() == r.minZ() || p.getZ() == r.maxZ()) ? b : FillCommand.HOLLOW_CORE);
/* 179 */     DESTROY = new Mode("DESTROY", 3, (l, p) -> l.destroyBlock(p, true), FillCommand.Filter.NOOP);
/*     */   }
/*     */   
/*     */   Mode(FillCommand.Affector affector, FillCommand.Filter filter) {
/* 185 */     this.affector = affector;
/* 186 */     this.filter = filter;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FillCommand$Mode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */