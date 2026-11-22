/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ public enum OrderPolicy {
/* 110 */   ORDERED(t -> {
/*     */     
/*     */     }),
/* 111 */   SHUFFLED(ShufflingList::shuffle);
/*     */   
/*     */   private final Consumer<ShufflingList<?>> consumer;
/*     */   
/*     */   OrderPolicy(Consumer<ShufflingList<?>> consumer) {
/* 117 */     this.consumer = consumer;
/*     */   }
/*     */   
/*     */   public void apply(ShufflingList<?> list) {
/* 121 */     this.consumer.accept(list);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\GateBehavior$OrderPolicy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */