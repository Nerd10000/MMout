/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface EntitySubPredicate {
/*    */   public static final EntitySubPredicate ALWAYS_TRUE = (paramEntity, paramServerLevel, paramVec3) -> true;
/*    */   
/*    */   default EntitySubPredicate and(EntitySubPredicate other) {
/* 14 */     return (entity, level, position) -> (matches(entity, level, position) && other.matches(entity, level, position));
/*    */   }
/*    */   
/*    */   boolean matches(Entity paramEntity, ServerLevel paramServerLevel, Vec3 paramVec3);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntitySubPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */