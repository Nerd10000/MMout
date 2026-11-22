/*    */ package net.minecraft.world.level.dimension;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ 
/*    */ public final class LevelStem extends Record {
/*    */   private final Holder<DimensionType> type;
/*    */   
/*    */   private final ChunkGenerator generator;
/*    */   
/*    */   public static final Codec<LevelStem> CODEC;
/*    */   
/*    */   public LevelStem(Holder<DimensionType> type, ChunkGenerator generator) {
/* 11 */     this.type = type;
/* 11 */     this.generator = generator;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/LevelStem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/LevelStem;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/LevelStem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/LevelStem;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/LevelStem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/dimension/LevelStem;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<DimensionType> type() {
/* 11 */     return this.type;
/*    */   }
/*    */   
/*    */   public ChunkGenerator generator() {
/* 11 */     return this.generator;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)DimensionType.CODEC.fieldOf("type").forGetter(LevelStem::type), (App)ChunkGenerator.CODEC.fieldOf("generator").forGetter(LevelStem::generator)).apply((Applicative)i, i.stable(LevelStem::new)));
/*    */   }
/*    */   
/* 20 */   public static final ResourceKey<LevelStem> OVERWORLD = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("overworld"));
/*    */   
/* 21 */   public static final ResourceKey<LevelStem> NETHER = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("the_nether"));
/*    */   
/* 22 */   public static final ResourceKey<LevelStem> END = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("the_end"));
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\LevelStem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */