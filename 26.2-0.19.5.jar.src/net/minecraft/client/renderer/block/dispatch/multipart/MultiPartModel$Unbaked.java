/*     */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class Unbaked implements BlockStateModel.UnbakedRoot {
/*     */   private final List<MultiPartModel.Selector<BlockStateModel.Unbaked>> selectors;
/*     */   
/* 114 */   private final ModelBaker.SharedOperationKey<MultiPartModel.SharedBakedState> sharedStateKey = new ModelBaker.SharedOperationKey<MultiPartModel.SharedBakedState>() {
/*     */       public MultiPartModel.SharedBakedState compute(ModelBaker modelBakery) {
/* 118 */         ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel>> selectors = ImmutableList.builderWithExpectedSize(MultiPartModel.Unbaked.this.selectors.size());
/* 119 */         for (MultiPartModel.Selector<BlockStateModel.Unbaked> selector : MultiPartModel.Unbaked.this.selectors)
/* 120 */           selectors.add(selector.with(((BlockStateModel.Unbaked)selector.model).bake(modelBakery))); 
/* 122 */         return new MultiPartModel.SharedBakedState((List<MultiPartModel.Selector<BlockStateModel>>)selectors.build());
/*     */       }
/*     */     };
/*     */   
/*     */   public Unbaked(List<MultiPartModel.Selector<BlockStateModel.Unbaked>> selectors) {
/* 127 */     this.selectors = selectors;
/*     */   }
/*     */   
/*     */   public Object visualEqualityGroup(BlockState blockState) {
/*     */     static final class Key extends Record {
/*     */       private final MultiPartModel.Unbaked model;
/*     */       
/*     */       private final IntList selectors;
/*     */       
/*     */       Key(MultiPartModel.Unbaked model, IntList selectors) {
/* 132 */         this.model = model;
/* 132 */         this.selectors = selectors;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #132	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #132	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #132	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public MultiPartModel.Unbaked model() {
/* 132 */         return this.model;
/*     */       }
/*     */       
/*     */       public IntList selectors() {
/* 132 */         return this.selectors;
/*     */       }
/*     */     };
/* 134 */     IntArrayList intArrayList = new IntArrayList();
/* 135 */     for (int i = 0; i < this.selectors.size(); i++) {
/* 136 */       if (((MultiPartModel.Selector)this.selectors.get(i)).condition.test(blockState))
/* 137 */         intArrayList.add(i); 
/*     */     } 
/* 140 */     return new Key(this, (IntList)intArrayList);
/*     */   }
/*     */   
/*     */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 145 */     this.selectors.forEach(s -> ((BlockStateModel.Unbaked)s.model).resolveDependencies(resolver));
/*     */   }
/*     */   
/*     */   public BlockStateModel bake(BlockState blockState, ModelBaker modelBakery) {
/* 150 */     MultiPartModel.SharedBakedState shared = (MultiPartModel.SharedBakedState)modelBakery.compute(this.sharedStateKey);
/* 151 */     return new MultiPartModel(shared, blockState);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\MultiPartModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */