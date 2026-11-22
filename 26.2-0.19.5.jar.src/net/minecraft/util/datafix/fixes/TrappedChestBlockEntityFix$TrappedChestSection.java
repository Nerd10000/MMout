/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public final class TrappedChestSection extends LeavesFix.Section {
/*     */   private IntSet chestIds;
/*     */   
/*     */   public TrappedChestSection(Typed<?> section, Schema inputSchema) {
/* 114 */     super(section, inputSchema);
/*     */   }
/*     */   
/*     */   protected boolean skippable() {
/* 119 */     this.chestIds = (IntSet)new IntOpenHashSet();
/* 121 */     for (int i = 0; i < this.palette.size(); i++) {
/* 122 */       Dynamic<?> paletteTag = this.palette.get(i);
/* 123 */       String blockName = paletteTag.get("Name").asString("");
/* 124 */       if (Objects.equals(blockName, "minecraft:trapped_chest"))
/* 125 */         this.chestIds.add(i); 
/*     */     } 
/* 129 */     return this.chestIds.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isTrappedChest(int block) {
/* 133 */     return this.chestIds.contains(block);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\TrappedChestBlockEntityFix$TrappedChestSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */