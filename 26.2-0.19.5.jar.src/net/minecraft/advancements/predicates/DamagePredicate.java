/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ 
/*    */ public final class DamagePredicate extends Record {
/*    */   private final MinMaxBounds.Doubles dealtDamage;
/*    */   
/*    */   private final MinMaxBounds.Doubles takenDamage;
/*    */   
/*    */   private final Optional<EntityPredicate> sourceEntity;
/*    */   
/*    */   private final Optional<Boolean> blocked;
/*    */   
/*    */   private final Optional<DamageSourcePredicate> type;
/*    */   
/*    */   public static final Codec<DamagePredicate> CODEC;
/*    */   
/*    */   public DamagePredicate(MinMaxBounds.Doubles dealtDamage, MinMaxBounds.Doubles takenDamage, Optional<EntityPredicate> sourceEntity, Optional<Boolean> blocked, Optional<DamageSourcePredicate> type) {
/* 11 */     this.dealtDamage = dealtDamage;
/* 11 */     this.takenDamage = takenDamage;
/* 11 */     this.sourceEntity = sourceEntity;
/* 11 */     this.blocked = blocked;
/* 11 */     this.type = type;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/DamagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/DamagePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/DamagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/DamagePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/DamagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/DamagePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles dealtDamage() {
/* 11 */     return this.dealtDamage;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles takenDamage() {
/* 11 */     return this.takenDamage;
/*    */   }
/*    */   
/*    */   public Optional<EntityPredicate> sourceEntity() {
/* 11 */     return this.sourceEntity;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> blocked() {
/* 11 */     return this.blocked;
/*    */   }
/*    */   
/*    */   public Optional<DamageSourcePredicate> type() {
/* 11 */     return this.type;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("dealt", MinMaxBounds.Doubles.ANY).forGetter(DamagePredicate::dealtDamage), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("taken", MinMaxBounds.Doubles.ANY).forGetter(DamagePredicate::takenDamage), (App)EntityPredicate.CODEC.optionalFieldOf("source_entity").forGetter(DamagePredicate::sourceEntity), (App)Codec.BOOL.optionalFieldOf("blocked").forGetter(DamagePredicate::blocked), (App)DamageSourcePredicate.CODEC.optionalFieldOf("type").forGetter(DamagePredicate::type)).apply((Applicative)i, DamagePredicate::new));
/*    */   }
/*    */   
/*    */   public boolean matches(ServerPlayer player, DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
/* 27 */     if (!this.dealtDamage.matches(originalDamage))
/* 28 */       return false; 
/* 30 */     if (!this.takenDamage.matches(actualDamage))
/* 31 */       return false; 
/* 33 */     if (this.sourceEntity.isPresent() && !((EntityPredicate)this.sourceEntity.get()).matches(player, source.getEntity()))
/* 34 */       return false; 
/* 36 */     if (this.blocked.isPresent() && ((Boolean)this.blocked.get()).booleanValue() != blocked)
/* 37 */       return false; 
/* 39 */     if (this.type.isPresent() && !((DamageSourcePredicate)this.type.get()).matches(player, source))
/* 40 */       return false; 
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 46 */     private MinMaxBounds.Doubles dealtDamage = MinMaxBounds.Doubles.ANY;
/*    */     
/* 47 */     private MinMaxBounds.Doubles takenDamage = MinMaxBounds.Doubles.ANY;
/*    */     
/* 48 */     private Optional<EntityPredicate> sourceEntity = Optional.empty();
/*    */     
/* 49 */     private Optional<Boolean> blocked = Optional.empty();
/*    */     
/* 50 */     private Optional<DamageSourcePredicate> type = Optional.empty();
/*    */     
/*    */     public static Builder damageInstance() {
/* 53 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder dealtDamage(MinMaxBounds.Doubles dealtDamage) {
/* 57 */       this.dealtDamage = dealtDamage;
/* 58 */       return this;
/*    */     }
/*    */     
/*    */     public Builder takenDamage(MinMaxBounds.Doubles takenDamage) {
/* 62 */       this.takenDamage = takenDamage;
/* 63 */       return this;
/*    */     }
/*    */     
/*    */     public Builder sourceEntity(EntityPredicate sourceEntity) {
/* 67 */       this.sourceEntity = Optional.of(sourceEntity);
/* 68 */       return this;
/*    */     }
/*    */     
/*    */     public Builder blocked(Boolean blocked) {
/* 72 */       this.blocked = Optional.of(blocked);
/* 73 */       return this;
/*    */     }
/*    */     
/*    */     public Builder type(DamageSourcePredicate type) {
/* 77 */       this.type = Optional.of(type);
/* 78 */       return this;
/*    */     }
/*    */     
/*    */     public Builder type(DamageSourcePredicate.Builder type) {
/* 82 */       this.type = Optional.of(type.build());
/* 83 */       return this;
/*    */     }
/*    */     
/*    */     public DamagePredicate build() {
/* 87 */       return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\DamagePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */