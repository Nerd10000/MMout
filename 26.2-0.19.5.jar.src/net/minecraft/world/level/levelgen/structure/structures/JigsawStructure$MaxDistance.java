/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ public final class MaxDistance extends Record {
/*     */   private final int horizontal;
/*     */   
/*     */   private final int vertical;
/*     */   
/*     */   public MaxDistance(int horizontal, int vertical) {
/* 123 */     this.horizontal = horizontal;
/* 123 */     this.vertical = vertical;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int horizontal() {
/* 123 */     return this.horizontal;
/*     */   }
/*     */   
/*     */   public int vertical() {
/* 123 */     return this.vertical;
/*     */   }
/*     */   
/* 127 */   private static final Codec<Integer> HORIZONTAL_VALUE_CODEC = Codec.intRange(1, 128);
/*     */   
/*     */   private static final Codec<MaxDistance> FULL_CODEC;
/*     */   
/*     */   public static final Codec<MaxDistance> CODEC;
/*     */   
/*     */   static {
/* 129 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)HORIZONTAL_VALUE_CODEC.fieldOf("horizontal").forGetter(MaxDistance::horizontal), (App)ExtraCodecs.intRange(1, DimensionType.Y_SIZE).optionalFieldOf("vertical", Integer.valueOf(DimensionType.Y_SIZE)).forGetter(MaxDistance::vertical)).apply((Applicative)i, MaxDistance::new));
/* 134 */     CODEC = Codec.either(FULL_CODEC, HORIZONTAL_VALUE_CODEC).xmap(either -> (MaxDistance)either.map(Function.identity(), MaxDistance::new), distance -> (distance.horizontal == distance.vertical) ? Either.right(Integer.valueOf(distance.horizontal)) : Either.left(distance));
/*     */   }
/*     */   
/*     */   public MaxDistance(int value) {
/* 140 */     this(value, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\JigsawStructure$MaxDistance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */