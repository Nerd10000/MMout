/*     */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class MultiPartModel implements BlockStateModel {
/*     */   private final SharedBakedState shared;
/*     */   
/*     */   private final BlockState blockState;
/*     */   
/*     */   private List<BlockStateModel> models;
/*     */   
/*     */   private MultiPartModel(SharedBakedState shared, BlockState blockState) {
/*  27 */     this.shared = shared;
/*  28 */     this.blockState = blockState;
/*     */   }
/*     */   
/*     */   public Material.Baked particleMaterial() {
/*  33 */     return this.shared.particleMaterial;
/*     */   }
/*     */   
/*     */   public int materialFlags() {
/*  38 */     return this.shared.materialFlags;
/*     */   }
/*     */   
/*     */   public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
/*  43 */     if (this.models == null)
/*  44 */       this.models = this.shared.selectModels(this.blockState); 
/*  47 */     long seed = random.nextLong();
/*  49 */     for (BlockStateModel model : this.models) {
/*  50 */       random.setSeed(seed);
/*  51 */       model.collectParts(random, output);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class Selector<T> extends Record {
/*     */     private final Predicate<BlockState> condition;
/*     */     
/*     */     private final T model;
/*     */     
/*     */     public Selector(Predicate<BlockState> condition, T model) {
/*  55 */       this.condition = condition;
/*  55 */       this.model = model;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #55	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #55	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #55	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector<TT;>;
/*     */     }
/*     */     
/*     */     public Predicate<BlockState> condition() {
/*  55 */       return this.condition;
/*     */     }
/*     */     
/*     */     public T model() {
/*  55 */       return this.model;
/*     */     }
/*     */     
/*     */     public <S> Selector<S> with(S newModel) {
/*  57 */       return new Selector(this.condition, (T)newModel);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SharedBakedState {
/*     */     private final List<MultiPartModel.Selector<BlockStateModel>> selectors;
/*     */     
/*     */     private final Material.Baked particleMaterial;
/*     */     
/*     */     private final int materialFlags;
/*     */     
/*  67 */     private final Map<BitSet, List<BlockStateModel>> subsets = new ConcurrentHashMap<>();
/*     */     
/*     */     private static BlockStateModel getFirstModel(List<MultiPartModel.Selector<BlockStateModel>> selectors) {
/*  70 */       if (selectors.isEmpty())
/*  71 */         throw new IllegalArgumentException("Model must have at least one selector"); 
/*  73 */       return ((MultiPartModel.Selector<BlockStateModel>)selectors.getFirst()).model();
/*     */     }
/*     */     
/*     */     private static int computeMaterialFlags(List<MultiPartModel.Selector<BlockStateModel>> selectors) {
/*  77 */       int flags = 0;
/*  78 */       for (MultiPartModel.Selector<BlockStateModel> selector : selectors)
/*  79 */         flags |= ((BlockStateModel)selector.model).materialFlags(); 
/*  81 */       return flags;
/*     */     }
/*     */     
/*     */     public SharedBakedState(List<MultiPartModel.Selector<BlockStateModel>> selectors) {
/*  85 */       this.selectors = selectors;
/*  86 */       BlockStateModel firstModel = getFirstModel(selectors);
/*  87 */       this.particleMaterial = firstModel.particleMaterial();
/*  88 */       this.materialFlags = computeMaterialFlags(selectors);
/*     */     }
/*     */     
/*     */     public List<BlockStateModel> selectModels(BlockState state) {
/*  92 */       BitSet selectedModels = new BitSet();
/*  93 */       for (int i = 0; i < this.selectors.size(); i++) {
/*  94 */         if (((MultiPartModel.Selector)this.selectors.get(i)).condition.test(state))
/*  95 */           selectedModels.set(i); 
/*     */       } 
/*  99 */       return this.subsets.computeIfAbsent(selectedModels, selected -> {
/*     */             ImmutableList.Builder<BlockStateModel> result = ImmutableList.builder();
/*     */             for (int i = 0; i < this.selectors.size(); i++) {
/*     */               if (selected.get(i))
/*     */                 result.add((BlockStateModel)((MultiPartModel.Selector)this.selectors.get(i)).model); 
/*     */             } 
/*     */             return (List)result.build();
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Unbaked implements BlockStateModel.UnbakedRoot {
/*     */     private final List<MultiPartModel.Selector<BlockStateModel.Unbaked>> selectors;
/*     */     
/* 114 */     private final ModelBaker.SharedOperationKey<MultiPartModel.SharedBakedState> sharedStateKey = new ModelBaker.SharedOperationKey<MultiPartModel.SharedBakedState>() {
/*     */         public MultiPartModel.SharedBakedState compute(ModelBaker modelBakery) {
/* 118 */           ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel>> selectors = ImmutableList.builderWithExpectedSize(MultiPartModel.Unbaked.this.selectors.size());
/* 119 */           for (MultiPartModel.Selector<BlockStateModel.Unbaked> selector : MultiPartModel.Unbaked.this.selectors)
/* 120 */             selectors.add(selector.with(((BlockStateModel.Unbaked)selector.model).bake(modelBakery))); 
/* 122 */           return new MultiPartModel.SharedBakedState((List<MultiPartModel.Selector<BlockStateModel>>)selectors.build());
/*     */         }
/*     */       };
/*     */     
/*     */     public Unbaked(List<MultiPartModel.Selector<BlockStateModel.Unbaked>> selectors) {
/* 127 */       this.selectors = selectors;
/*     */     }
/*     */     
/*     */     public Object visualEqualityGroup(BlockState blockState) {
/*     */       static final class Key extends Record {
/*     */         private final MultiPartModel.Unbaked model;
/*     */         
/*     */         private final IntList selectors;
/*     */         
/*     */         Key(MultiPartModel.Unbaked model, IntList selectors) {
/* 132 */           this.model = model;
/* 132 */           this.selectors = selectors;
/*     */         }
/*     */         
/*     */         public final String toString() {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;)Ljava/lang/String;
/*     */           //   6: areturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #132	-> 0
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */         }
/*     */         
/*     */         public final int hashCode() {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;)I
/*     */           //   6: ireturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #132	-> 0
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */         }
/*     */         
/*     */         public final boolean equals(Object o) {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: aload_1
/*     */           //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;Ljava/lang/Object;)Z
/*     */           //   7: ireturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #132	-> 0
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */           //   0	8	1	o	Ljava/lang/Object;
/*     */         }
/*     */         
/*     */         public MultiPartModel.Unbaked model() {
/* 132 */           return this.model;
/*     */         }
/*     */         
/*     */         public IntList selectors() {
/* 132 */           return this.selectors;
/*     */         }
/*     */       };
/* 134 */       IntArrayList intArrayList = new IntArrayList();
/* 135 */       for (int i = 0; i < this.selectors.size(); i++) {
/* 136 */         if (((MultiPartModel.Selector)this.selectors.get(i)).condition.test(blockState))
/* 137 */           intArrayList.add(i); 
/*     */       } 
/* 140 */       return new Key(this, (IntList)intArrayList);
/*     */     }
/*     */     
/*     */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 145 */       this.selectors.forEach(s -> ((BlockStateModel.Unbaked)s.model).resolveDependencies(resolver));
/*     */     }
/*     */     
/*     */     public BlockStateModel bake(BlockState blockState, ModelBaker modelBakery) {
/* 150 */       MultiPartModel.SharedBakedState shared = (MultiPartModel.SharedBakedState)modelBakery.compute(this.sharedStateKey);
/* 151 */       return new MultiPartModel(shared, blockState);
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements ModelBaker.SharedOperationKey<SharedBakedState> {
/*     */     public MultiPartModel.SharedBakedState compute(ModelBaker modelBakery) {
/*     */       ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel>> selectors = ImmutableList.builderWithExpectedSize(MultiPartModel.Unbaked.this.selectors.size());
/*     */       for (MultiPartModel.Selector<BlockStateModel.Unbaked> selector : MultiPartModel.Unbaked.this.selectors)
/*     */         selectors.add(selector.with(((BlockStateModel.Unbaked)selector.model).bake(modelBakery))); 
/*     */       return new MultiPartModel.SharedBakedState((List<MultiPartModel.Selector<BlockStateModel>>)selectors.build());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Key extends Record {
/*     */     private final MultiPartModel.Unbaked model;
/*     */     
/*     */     private final IntList selectors;
/*     */     
/*     */     Key(MultiPartModel.Unbaked model, IntList selectors) {
/*     */       this.model = model;
/*     */       this.selectors = selectors;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #132	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #132	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #132	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MultiPartModel.Unbaked model() {
/*     */       return this.model;
/*     */     }
/*     */     
/*     */     public IntList selectors() {
/*     */       return this.selectors;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\MultiPartModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */