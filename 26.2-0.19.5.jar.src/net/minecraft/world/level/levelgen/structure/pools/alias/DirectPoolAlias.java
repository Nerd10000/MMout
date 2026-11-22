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
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public final class DirectPoolAlias extends Record implements PoolAliasBinding {
/*    */   private final ResourceKey<StructureTemplatePool> alias;
/*    */   
/*    */   private final ResourceKey<StructureTemplatePool> target;
/*    */   
/*    */   public static final MapCodec<DirectPoolAlias> CODEC;
/*    */   
/*    */   public DirectPoolAlias(ResourceKey<StructureTemplatePool> alias, ResourceKey<StructureTemplatePool> target) {
/* 17 */     this.alias = alias;
/* 17 */     this.target = target;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<StructureTemplatePool> alias() {
/* 17 */     return this.alias;
/*    */   }
/*    */   
/*    */   public ResourceKey<StructureTemplatePool> target() {
/* 17 */     return this.target;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("alias").forGetter(DirectPoolAlias::alias), (App)ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("target").forGetter(DirectPoolAlias::target)).apply((Applicative)i, DirectPoolAlias::new));
/*    */   }
/*    */   
/*    */   public void forEachResolved(RandomSource random, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) {
/* 25 */     aliasAndTargetConsumer.accept(this.alias, this.target);
/*    */   }
/*    */   
/*    */   public Stream<ResourceKey<StructureTemplatePool>> allTargets() {
/* 30 */     return Stream.of(this.target);
/*    */   }
/*    */   
/*    */   public MapCodec<DirectPoolAlias> codec() {
/* 35 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\DirectPoolAlias.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */