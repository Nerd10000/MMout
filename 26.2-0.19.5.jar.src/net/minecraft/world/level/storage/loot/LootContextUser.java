/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ 
/*    */ public interface LootContextUser extends Validatable {
/*    */   default Set<ContextKey<?>> getReferencedContextParams() {
/* 10 */     return Set.of();
/*    */   }
/*    */   
/*    */   default void validate(ValidationContext context) {
/* 15 */     context.validateContextUsage(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContextUser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */