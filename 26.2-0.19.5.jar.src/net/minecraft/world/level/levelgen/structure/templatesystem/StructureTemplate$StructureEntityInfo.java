/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class StructureEntityInfo {
/*     */   public final Vec3 pos;
/*     */   
/*     */   public final BlockPos blockPos;
/*     */   
/*     */   public final CompoundTag nbt;
/*     */   
/*     */   public StructureEntityInfo(Vec3 pos, BlockPos blockPos, CompoundTag nbt) {
/* 810 */     this.pos = pos;
/* 811 */     this.blockPos = blockPos;
/* 812 */     this.nbt = nbt;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplate$StructureEntityInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */