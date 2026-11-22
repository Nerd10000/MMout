/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class EntityTagPredicate extends Record implements EntitySubPredicate {
/*    */   private final Optional<List<String>> anyOf;
/*    */   
/*    */   private final Optional<List<String>> allOf;
/*    */   
/*    */   private final Optional<List<String>> noneOf;
/*    */   
/*    */   public static final Codec<EntityTagPredicate> CODEC;
/*    */   
/*    */   public EntityTagPredicate(Optional<List<String>> anyOf, Optional<List<String>> allOf, Optional<List<String>> noneOf) {
/* 14 */     this.anyOf = anyOf;
/* 14 */     this.allOf = allOf;
/* 14 */     this.noneOf = noneOf;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/EntityTagPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityTagPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/EntityTagPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityTagPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/EntityTagPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/EntityTagPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<List<String>> anyOf() {
/* 14 */     return this.anyOf;
/*    */   }
/*    */   
/*    */   public Optional<List<String>> allOf() {
/* 14 */     return this.allOf;
/*    */   }
/*    */   
/*    */   public Optional<List<String>> noneOf() {
/* 14 */     return this.noneOf;
/*    */   }
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.listOf().optionalFieldOf("any_of").forGetter(EntityTagPredicate::anyOf), (App)Codec.STRING.listOf().optionalFieldOf("all_of").forGetter(EntityTagPredicate::allOf), (App)Codec.STRING.listOf().optionalFieldOf("none_of").forGetter(EntityTagPredicate::noneOf)).apply((Applicative)i, EntityTagPredicate::new));
/*    */   }
/*    */   
/*    */   public boolean matches(Set<String> tags) {
/* 27 */     if (this.anyOf.isPresent() && !containsAtLeastOne(tags, this.anyOf.get()))
/* 28 */       return false; 
/* 31 */     if (this.noneOf.isPresent() && containsAtLeastOne(tags, this.noneOf.get()))
/* 32 */       return false; 
/* 35 */     if (this.allOf.isPresent() && !containsAllOf(tags, this.allOf.get()))
/* 36 */       return false; 
/* 39 */     return true;
/*    */   }
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 44 */     return matches(entity.entityTags());
/*    */   }
/*    */   
/*    */   private static boolean containsAtLeastOne(Set<String> provided, List<String> tags) {
/* 48 */     for (String tag : tags) {
/* 49 */       if (provided.contains(tag))
/* 50 */         return true; 
/*    */     } 
/* 53 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean containsAllOf(Set<String> provided, List<String> tags) {
/* 57 */     for (String tag : tags) {
/* 58 */       if (!provided.contains(tag))
/* 59 */         return false; 
/*    */     } 
/* 62 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityTagPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */