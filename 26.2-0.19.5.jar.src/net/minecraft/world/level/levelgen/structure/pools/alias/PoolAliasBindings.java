/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class PoolAliasBindings {
/*    */   public static MapCodec<? extends PoolAliasBinding> bootstrap(Registry<MapCodec<? extends PoolAliasBinding>> registry) {
/* 16 */     Registry.register(registry, "random", RandomPoolAlias.CODEC);
/* 17 */     Registry.register(registry, "random_group", RandomGroupPoolAlias.CODEC);
/* 18 */     return (MapCodec<? extends PoolAliasBinding>)Registry.register(registry, "direct", DirectPoolAlias.CODEC);
/*    */   }
/*    */   
/*    */   public static void registerTargetsAsPools(BootstrapContext<StructureTemplatePool> context, Holder<StructureTemplatePool> emptyPool, List<PoolAliasBinding> aliasBindings) {
/* 22 */     aliasBindings.stream()
/* 23 */       .flatMap(PoolAliasBinding::allTargets)
/* 24 */       .map(key -> key.identifier().getPath())
/* 25 */       .forEach(path -> Pools.register(context, path, new StructureTemplatePool(emptyPool, List.of(Pair.of(StructurePoolElement.single(path), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\PoolAliasBindings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */