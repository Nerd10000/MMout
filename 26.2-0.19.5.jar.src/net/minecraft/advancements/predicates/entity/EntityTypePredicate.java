/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class EntityTypePredicate extends Record implements EntitySubPredicate {
/*    */   private final HolderSet<EntityType<?>> types;
/*    */   
/*    */   public EntityTypePredicate(HolderSet<EntityType<?>> types) {
/* 16 */     this.types = types;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/EntityTypePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityTypePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/EntityTypePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityTypePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/EntityTypePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/EntityTypePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<EntityType<?>> types() {
/* 16 */     return this.types;
/*    */   }
/*    */   
/* 17 */   public static final Codec<EntityTypePredicate> CODEC = RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).xmap(EntityTypePredicate::new, EntityTypePredicate::types);
/*    */   
/*    */   public static EntityTypePredicate of(HolderGetter<EntityType<?>> lookup, EntityType<?> type) {
/* 21 */     return new EntityTypePredicate((HolderSet<EntityType<?>>)HolderSet.direct(new Holder[] { (Holder)type.builtInRegistryHolder() }));
/*    */   }
/*    */   
/*    */   public static EntityTypePredicate of(HolderGetter<EntityType<?>> lookup, TagKey<EntityType<?>> type) {
/* 25 */     return new EntityTypePredicate((HolderSet<EntityType<?>>)lookup.getOrThrow(type));
/*    */   }
/*    */   
/*    */   public boolean matches(Holder<EntityType<?>> type) {
/* 29 */     return this.types.contains(type);
/*    */   }
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 34 */     return matches(entity.typeHolder());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityTypePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */