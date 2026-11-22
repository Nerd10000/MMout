/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TargetedEntityPredicate extends Record implements EntitySubPredicate {
/*    */   private final EntityPredicate targetedEntity;
/*    */   
/*    */   public TargetedEntityPredicate(EntityPredicate targetedEntity) {
/* 10 */     this.targetedEntity = targetedEntity;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/TargetedEntityPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/TargetedEntityPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/TargetedEntityPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/TargetedEntityPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/TargetedEntityPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/TargetedEntityPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public EntityPredicate targetedEntity() {
/* 10 */     return this.targetedEntity;
/*    */   }
/*    */   
/* 13 */   public static final Codec<TargetedEntityPredicate> CODEC = EntityPredicate.CODEC.xmap(TargetedEntityPredicate::new, TargetedEntityPredicate::targetedEntity);
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 17 */     Mob mob = (Mob)entity;
/* 17 */     return this.targetedEntity.matches(level, position, (entity instanceof Mob) ? (Entity)mob.getTarget() : null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\TargetedEntityPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */