/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class RaiderPredicate extends Record implements EntitySubPredicate {
/*    */   private final boolean hasRaid;
/*    */   
/*    */   private final boolean isCaptain;
/*    */   
/*    */   public static final Codec<RaiderPredicate> CODEC;
/*    */   
/*    */   public RaiderPredicate(boolean hasRaid, boolean isCaptain) {
/* 11 */     this.hasRaid = hasRaid;
/* 11 */     this.isCaptain = isCaptain;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/RaiderPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/RaiderPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/RaiderPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/RaiderPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/RaiderPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/RaiderPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean hasRaid() {
/* 11 */     return this.hasRaid;
/*    */   }
/*    */   
/*    */   public boolean isCaptain() {
/* 11 */     return this.isCaptain;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("has_raid", Boolean.valueOf(false)).forGetter(RaiderPredicate::hasRaid), (App)Codec.BOOL.optionalFieldOf("is_captain", Boolean.valueOf(false)).forGetter(RaiderPredicate::isCaptain)).apply((Applicative)i, RaiderPredicate::new));
/*    */   }
/*    */   
/* 17 */   public static final RaiderPredicate CAPTAIN_WITHOUT_RAID = new RaiderPredicate(false, true);
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 21 */     if (entity instanceof Raider) {
/* 21 */       Raider raider = (Raider)entity;
/* 22 */       return (raider.hasRaid() == this.hasRaid && raider.isCaptain() == this.isCaptain);
/*    */     } 
/* 24 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\RaiderPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */