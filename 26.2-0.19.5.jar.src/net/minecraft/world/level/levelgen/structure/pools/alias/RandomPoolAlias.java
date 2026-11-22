/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public final class RandomPoolAlias extends Record implements PoolAliasBinding {
/*    */   private final ResourceKey<StructureTemplatePool> alias;
/*    */   
/*    */   private final WeightedList<ResourceKey<StructureTemplatePool>> targets;
/*    */   
/*    */   public static final MapCodec<RandomPoolAlias> CODEC;
/*    */   
/*    */   public RandomPoolAlias(ResourceKey<StructureTemplatePool> alias, WeightedList<ResourceKey<StructureTemplatePool>> targets) {
/* 19 */     this.alias = alias;
/* 19 */     this.targets = targets;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<StructureTemplatePool> alias() {
/* 19 */     return this.alias;
/*    */   }
/*    */   
/*    */   public WeightedList<ResourceKey<StructureTemplatePool>> targets() {
/* 19 */     return this.targets;
/*    */   }
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("alias").forGetter(RandomPoolAlias::alias), (App)WeightedList.nonEmptyCodec(ResourceKey.codec(Registries.TEMPLATE_POOL)).fieldOf("targets").forGetter(RandomPoolAlias::targets)).apply((Applicative)i, RandomPoolAlias::new));
/*    */   }
/*    */   
/*    */   public void forEachResolved(RandomSource random, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) {
/* 27 */     aliasAndTargetConsumer.accept(this.alias, (ResourceKey<StructureTemplatePool>)this.targets.getRandomOrThrow(random));
/*    */   }
/*    */   
/*    */   public Stream<ResourceKey<StructureTemplatePool>> allTargets() {
/* 32 */     return this.targets.unwrap().stream().map(Weighted::value);
/*    */   }
/*    */   
/*    */   public MapCodec<RandomPoolAlias> codec() {
/* 37 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\RandomPoolAlias.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */