/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LightningBolt;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LightningBoltPredicate extends Record implements EntitySubPredicate {
/*    */   private final MinMaxBounds.Ints blocksSetOnFire;
/*    */   
/*    */   private final Optional<EntityPredicate> entityStruck;
/*    */   
/*    */   public static final Codec<LightningBoltPredicate> CODEC;
/*    */   
/*    */   public LightningBoltPredicate(MinMaxBounds.Ints blocksSetOnFire, Optional<EntityPredicate> entityStruck) {
/* 14 */     this.blocksSetOnFire = blocksSetOnFire;
/* 14 */     this.entityStruck = entityStruck;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/LightningBoltPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/LightningBoltPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/LightningBoltPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/LightningBoltPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/LightningBoltPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/LightningBoltPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints blocksSetOnFire() {
/* 14 */     return this.blocksSetOnFire;
/*    */   }
/*    */   
/*    */   public Optional<EntityPredicate> entityStruck() {
/* 14 */     return this.entityStruck;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("blocks_set_on_fire", MinMaxBounds.Ints.ANY).forGetter(LightningBoltPredicate::blocksSetOnFire), (App)EntityPredicate.CODEC.optionalFieldOf("entity_struck").forGetter(LightningBoltPredicate::entityStruck)).apply((Applicative)i, LightningBoltPredicate::new));
/*    */   }
/*    */   
/*    */   public static LightningBoltPredicate blockSetOnFire(MinMaxBounds.Ints count) {
/* 24 */     return new LightningBoltPredicate(count, Optional.empty());
/*    */   }
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/*    */     LightningBolt bolt;
/* 29 */     if (entity instanceof LightningBolt) {
/* 29 */       bolt = (LightningBolt)entity;
/*    */     } else {
/* 30 */       return false;
/*    */     } 
/* 33 */     return (this.blocksSetOnFire.matches(bolt.getBlocksSetOnFire()) && (this.entityStruck
/* 34 */       .isEmpty() || bolt.getHitEntities().anyMatch(e -> ((EntityPredicate)this.entityStruck.get()).matches(level, position, e))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\LightningBoltPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */