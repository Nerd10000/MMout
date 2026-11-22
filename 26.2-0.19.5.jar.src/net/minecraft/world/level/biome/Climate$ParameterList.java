/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public class ParameterList<T> {
/*     */   private final List<Pair<Climate.ParameterPoint, T>> values;
/*     */   
/*     */   private final Climate.RTree<T> index;
/*     */   
/*     */   public static <T> Codec<ParameterList<T>> codec(MapCodec<T> valueCodec) {
/* 281 */     return ExtraCodecs.nonEmptyList(RecordCodecBuilder.create(i -> i.group((App)Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), (App)valueCodec.forGetter(Pair::getSecond)).apply((Applicative)i, Pair::of))
/*     */         
/* 284 */         .listOf()).xmap(ParameterList::new, ParameterList::values);
/*     */   }
/*     */   
/*     */   public ParameterList(List<Pair<Climate.ParameterPoint, T>> values) {
/* 288 */     this.values = values;
/* 289 */     this.index = Climate.RTree.create(values);
/*     */   }
/*     */   
/*     */   public List<Pair<Climate.ParameterPoint, T>> values() {
/* 293 */     return this.values;
/*     */   }
/*     */   
/*     */   public T findValue(Climate.TargetPoint target) {
/* 300 */     return findValueIndex(target);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public T findValueBruteForce(Climate.TargetPoint target) {
/* 305 */     Iterator<Pair<Climate.ParameterPoint, T>> iterator = values().iterator();
/* 308 */     Pair<Climate.ParameterPoint, T> first = iterator.next();
/* 309 */     long bestFitness = ((Climate.ParameterPoint)first.getFirst()).fitness(target);
/* 310 */     T best = (T)first.getSecond();
/* 312 */     while (iterator.hasNext()) {
/* 313 */       Pair<Climate.ParameterPoint, T> parameter = iterator.next();
/* 314 */       long fitness = ((Climate.ParameterPoint)parameter.getFirst()).fitness(target);
/* 315 */       if (fitness < bestFitness) {
/* 316 */         bestFitness = fitness;
/* 317 */         best = (T)parameter.getSecond();
/*     */       } 
/*     */     } 
/* 320 */     return best;
/*     */   }
/*     */   
/*     */   public T findValueIndex(Climate.TargetPoint target) {
/* 324 */     return findValueIndex(target, Climate.RTree.Node::distance);
/*     */   }
/*     */   
/*     */   protected T findValueIndex(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) {
/* 328 */     return this.index.search(target, distanceMetric);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$ParameterList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */