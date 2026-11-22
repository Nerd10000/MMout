/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ public interface PostSpawnProcessor<T extends Entity> {
/*    */   default PostSpawnProcessor<T> andThen(PostSpawnProcessor<? super T> after) {
/*  9 */     Objects.requireNonNull(after);
/* 10 */     return t -> {
/*    */         apply((T)t);
/*    */         after.apply(t);
/*    */       };
/*    */   }
/*    */   
/*    */   static <T extends Entity> PostSpawnProcessor<T> nop() {
/* 17 */     return paramEntity -> {
/*    */       
/*    */       };
/*    */   }
/*    */   
/*    */   void apply(T paramT);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\PostSpawnProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */