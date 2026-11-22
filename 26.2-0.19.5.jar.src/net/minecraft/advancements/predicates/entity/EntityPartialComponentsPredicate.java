/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class EntityPartialComponentsPredicate extends Record implements EntitySubPredicate {
/*    */   private final Map<DataComponentPredicate.Type<?>, DataComponentPredicate> predicates;
/*    */   
/*    */   public EntityPartialComponentsPredicate(Map<DataComponentPredicate.Type<?>, DataComponentPredicate> predicates) {
/* 12 */     this.predicates = predicates;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/EntityPartialComponentsPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityPartialComponentsPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/EntityPartialComponentsPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityPartialComponentsPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/EntityPartialComponentsPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/EntityPartialComponentsPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<DataComponentPredicate.Type<?>, DataComponentPredicate> predicates() {
/* 12 */     return this.predicates;
/*    */   }
/*    */   
/* 15 */   public static final Codec<EntityPartialComponentsPredicate> CODEC = DataComponentPredicate.CODEC.xmap(EntityPartialComponentsPredicate::new, EntityPartialComponentsPredicate::predicates);
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 19 */     for (DataComponentPredicate predicate : this.predicates.values()) {
/* 20 */       if (!predicate.matches((DataComponentGetter)entity))
/* 21 */         return false; 
/*    */     } 
/* 24 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityPartialComponentsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */