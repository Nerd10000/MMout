/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.SlotsPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.SlotProvider;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class EntitySlotsPredicate extends Record implements EntitySubPredicate {
/*    */   private final SlotsPredicate slots;
/*    */   
/*    */   public EntitySlotsPredicate(SlotsPredicate slots) {
/* 10 */     this.slots = slots;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/EntitySlotsPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntitySlotsPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/EntitySlotsPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntitySlotsPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/EntitySlotsPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/EntitySlotsPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SlotsPredicate slots() {
/* 10 */     return this.slots;
/*    */   }
/*    */   
/* 13 */   public static final Codec<EntitySlotsPredicate> CODEC = SlotsPredicate.CODEC.xmap(EntitySlotsPredicate::new, EntitySlotsPredicate::slots);
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 17 */     return this.slots.matches((SlotProvider)entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntitySlotsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */