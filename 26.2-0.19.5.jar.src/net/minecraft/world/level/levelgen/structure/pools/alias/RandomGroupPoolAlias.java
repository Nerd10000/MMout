/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public final class RandomGroupPoolAlias extends Record implements PoolAliasBinding {
/*    */   private final WeightedList<List<PoolAliasBinding>> groups;
/*    */   
/*    */   public static final MapCodec<RandomGroupPoolAlias> CODEC;
/*    */   
/*    */   public RandomGroupPoolAlias(WeightedList<List<PoolAliasBinding>> groups) {
/* 20 */     this.groups = groups;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WeightedList<List<PoolAliasBinding>> groups() {
/* 20 */     return this.groups;
/*    */   }
/*    */   
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeightedList.nonEmptyCodec(Codec.list(PoolAliasBinding.CODEC)).fieldOf("groups").forGetter(RandomGroupPoolAlias::groups)).apply((Applicative)i, RandomGroupPoolAlias::new));
/*    */   }
/*    */   
/*    */   public void forEachResolved(RandomSource random, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) {
/* 27 */     ((List)this.groups.getRandomOrThrow(random)).forEach(binding -> binding.forEachResolved(random, aliasAndTargetConsumer));
/*    */   }
/*    */   
/*    */   public Stream<ResourceKey<StructureTemplatePool>> allTargets() {
/* 32 */     return this.groups.unwrap().stream()
/* 33 */       .flatMap(weightedEntry -> ((List)weightedEntry.value()).stream())
/* 34 */       .flatMap(PoolAliasBinding::allTargets);
/*    */   }
/*    */   
/*    */   public MapCodec<RandomGroupPoolAlias> codec() {
/* 39 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\RandomGroupPoolAlias.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */