/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ 
/*     */ public final class JigsawBlockInfo extends Record {
/*     */   private final StructureTemplate.StructureBlockInfo info;
/*     */   
/*     */   private final JigsawBlockEntity.JointType jointType;
/*     */   
/*     */   private final Identifier name;
/*     */   
/*     */   private final ResourceKey<StructureTemplatePool> pool;
/*     */   
/*     */   private final Identifier target;
/*     */   
/*     */   private final int placementPriority;
/*     */   
/*     */   private final int selectionPriority;
/*     */   
/*     */   public JigsawBlockInfo(StructureTemplate.StructureBlockInfo info, JigsawBlockEntity.JointType jointType, Identifier name, ResourceKey<StructureTemplatePool> pool, Identifier target, int placementPriority, int selectionPriority) {
/* 771 */     this.info = info;
/* 771 */     this.jointType = jointType;
/* 771 */     this.name = name;
/* 771 */     this.pool = pool;
/* 771 */     this.target = target;
/* 771 */     this.placementPriority = placementPriority;
/* 771 */     this.selectionPriority = selectionPriority;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #771	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #771	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public StructureTemplate.StructureBlockInfo info() {
/* 771 */     return this.info;
/*     */   }
/*     */   
/*     */   public JigsawBlockEntity.JointType jointType() {
/* 771 */     return this.jointType;
/*     */   }
/*     */   
/*     */   public Identifier name() {
/* 771 */     return this.name;
/*     */   }
/*     */   
/*     */   public ResourceKey<StructureTemplatePool> pool() {
/* 771 */     return this.pool;
/*     */   }
/*     */   
/*     */   public Identifier target() {
/* 771 */     return this.target;
/*     */   }
/*     */   
/*     */   public int placementPriority() {
/* 771 */     return this.placementPriority;
/*     */   }
/*     */   
/*     */   public int selectionPriority() {
/* 771 */     return this.selectionPriority;
/*     */   }
/*     */   
/*     */   public static JigsawBlockInfo of(StructureTemplate.StructureBlockInfo info) {
/* 781 */     CompoundTag nbt = Objects.<CompoundTag>requireNonNull(info.nbt(), () -> String.valueOf(info) + " nbt was null");
/* 783 */     return new JigsawBlockInfo(info, 
/*     */         
/* 785 */         StructureTemplate.getJointType(nbt, info.state()), nbt
/* 786 */         .read("name", Identifier.CODEC).orElse(JigsawBlockEntity.EMPTY_ID), nbt
/* 787 */         .read("pool", JigsawBlockEntity.POOL_CODEC).orElse(Pools.EMPTY), nbt
/* 788 */         .read("target", Identifier.CODEC).orElse(JigsawBlockEntity.EMPTY_ID), nbt
/* 789 */         .getIntOr("placement_priority", 0), nbt
/* 790 */         .getIntOr("selection_priority", 0));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 796 */     return String.format(Locale.ROOT, "<JigsawBlockInfo | %s | %s | name: %s | pool: %s | target: %s | placement: %d | selection: %d | %s>", new Object[] { this.info.pos, this.info.state, this.name, this.pool.identifier(), this.target, Integer.valueOf(this.placementPriority), Integer.valueOf(this.selectionPriority), this.info.nbt });
/*     */   }
/*     */   
/*     */   public JigsawBlockInfo withInfo(StructureTemplate.StructureBlockInfo info) {
/* 800 */     return new JigsawBlockInfo(info, this.jointType, this.name, this.pool, this.target, this.placementPriority, this.selectionPriority);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplate$JigsawBlockInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */