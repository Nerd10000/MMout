/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public final class SpawnContext extends Record {
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final ServerLevelAccessor level;
/*    */   
/*    */   private final EnvironmentAttributeReader environmentAttributes;
/*    */   
/*    */   private final Holder<Biome> biome;
/*    */   
/*    */   public SpawnContext(BlockPos pos, ServerLevelAccessor level, EnvironmentAttributeReader environmentAttributes, Holder<Biome> biome) {
/*  9 */     this.pos = pos;
/*  9 */     this.level = level;
/*  9 */     this.environmentAttributes = environmentAttributes;
/*  9 */     this.biome = biome;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/SpawnContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/SpawnContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/SpawnContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/SpawnContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/SpawnContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/SpawnContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/*  9 */     return this.pos;
/*    */   }
/*    */   
/*    */   public ServerLevelAccessor level() {
/*  9 */     return this.level;
/*    */   }
/*    */   
/*    */   public EnvironmentAttributeReader environmentAttributes() {
/*  9 */     return this.environmentAttributes;
/*    */   }
/*    */   
/*    */   public Holder<Biome> biome() {
/*  9 */     return this.biome;
/*    */   }
/*    */   
/*    */   public static SpawnContext create(ServerLevelAccessor level, BlockPos pos) {
/* 16 */     Holder<Biome> biome = level.getBiome(pos);
/* 17 */     return new SpawnContext(pos, level, level.environmentAttributes(), biome);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\SpawnContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */