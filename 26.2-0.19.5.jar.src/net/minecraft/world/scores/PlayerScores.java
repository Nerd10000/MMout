/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ class PlayerScores {
/* 16 */   private final Reference2ObjectOpenHashMap<Objective, Score> scores = new Reference2ObjectOpenHashMap(16, 0.5F);
/*    */   
/*    */   public Score get(Objective objective) {
/* 19 */     return (Score)this.scores.get(objective);
/*    */   }
/*    */   
/*    */   public Score getOrCreate(Objective objective, Consumer<Score> newResultCallback) {
/* 23 */     return (Score)this.scores.computeIfAbsent(objective, obj -> {
/*    */           Score newScore = new Score();
/*    */           newResultCallback.accept(newScore);
/*    */           return newScore;
/*    */         });
/*    */   }
/*    */   
/*    */   public boolean remove(Objective objective) {
/* 31 */     return (this.scores.remove(objective) != null);
/*    */   }
/*    */   
/*    */   public boolean hasScores() {
/* 35 */     return !this.scores.isEmpty();
/*    */   }
/*    */   
/*    */   public Object2IntMap<Objective> listScores() {
/* 39 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 40 */     this.scores.forEach((objective, score) -> result.put(objective, score.value()));
/* 41 */     return (Object2IntMap<Objective>)object2IntOpenHashMap;
/*    */   }
/*    */   
/*    */   void setScore(Objective objective, Score score) {
/* 46 */     this.scores.put(objective, score);
/*    */   }
/*    */   
/*    */   Map<Objective, Score> listRawScores() {
/* 51 */     return Collections.unmodifiableMap((Map<? extends Objective, ? extends Score>)this.scores);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\PlayerScores.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */