/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class DamageSourcePredicate extends Record {
/*    */   private final List<TagPredicate<DamageType>> tags;
/*    */   
/*    */   private final Optional<EntityPredicate> directEntity;
/*    */   
/*    */   private final Optional<EntityPredicate> sourceEntity;
/*    */   
/*    */   private final Optional<Boolean> isDirect;
/*    */   
/*    */   public static final Codec<DamageSourcePredicate> CODEC;
/*    */   
/*    */   public DamageSourcePredicate(List<TagPredicate<DamageType>> tags, Optional<EntityPredicate> directEntity, Optional<EntityPredicate> sourceEntity, Optional<Boolean> isDirect) {
/* 17 */     this.tags = tags;
/* 17 */     this.directEntity = directEntity;
/* 17 */     this.sourceEntity = sourceEntity;
/* 17 */     this.isDirect = isDirect;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/DamageSourcePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/DamageSourcePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/DamageSourcePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/DamageSourcePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/DamageSourcePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/DamageSourcePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<TagPredicate<DamageType>> tags() {
/* 17 */     return this.tags;
/*    */   }
/*    */   
/*    */   public Optional<EntityPredicate> directEntity() {
/* 17 */     return this.directEntity;
/*    */   }
/*    */   
/*    */   public Optional<EntityPredicate> sourceEntity() {
/* 17 */     return this.sourceEntity;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> isDirect() {
/* 17 */     return this.isDirect;
/*    */   }
/*    */   
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)TagPredicate.<T>codec(Registries.DAMAGE_TYPE).listOf().optionalFieldOf("tags", List.of()).forGetter(DamageSourcePredicate::tags), (App)EntityPredicate.CODEC.optionalFieldOf("direct_entity").forGetter(DamageSourcePredicate::directEntity), (App)EntityPredicate.CODEC.optionalFieldOf("source_entity").forGetter(DamageSourcePredicate::sourceEntity), (App)Codec.BOOL.optionalFieldOf("is_direct").forGetter(DamageSourcePredicate::isDirect)).apply((Applicative)i, DamageSourcePredicate::new));
/*    */   }
/*    */   
/*    */   public boolean matches(ServerPlayer player, DamageSource source) {
/* 31 */     return matches(player.level(), player.position(), source);
/*    */   }
/*    */   
/*    */   public boolean matches(ServerLevel level, Vec3 position, DamageSource source) {
/* 35 */     for (TagPredicate<DamageType> tag : this.tags) {
/* 36 */       if (!tag.matches(source.typeHolder()))
/* 37 */         return false; 
/*    */     } 
/* 40 */     if (this.directEntity.isPresent() && !((EntityPredicate)this.directEntity.get()).matches(level, position, source.getDirectEntity()))
/* 41 */       return false; 
/* 43 */     if (this.sourceEntity.isPresent() && !((EntityPredicate)this.sourceEntity.get()).matches(level, position, source.getEntity()))
/* 44 */       return false; 
/* 46 */     if (this.isDirect.isPresent() && ((Boolean)this.isDirect.get()).booleanValue() != source.isDirect())
/* 47 */       return false; 
/* 50 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 54 */     private final ImmutableList.Builder<TagPredicate<DamageType>> tags = ImmutableList.builder();
/*    */     
/* 55 */     private Optional<EntityPredicate> directEntity = Optional.empty();
/*    */     
/* 56 */     private Optional<EntityPredicate> sourceEntity = Optional.empty();
/*    */     
/* 57 */     private Optional<Boolean> isDirect = Optional.empty();
/*    */     
/*    */     public static Builder damageType() {
/* 60 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder tag(TagPredicate<DamageType> tag) {
/* 64 */       this.tags.add(tag);
/* 65 */       return this;
/*    */     }
/*    */     
/*    */     public Builder direct(EntityPredicate.Builder directEntity) {
/* 69 */       this.directEntity = Optional.of(directEntity.build());
/* 70 */       return this;
/*    */     }
/*    */     
/*    */     public Builder source(EntityPredicate.Builder sourceEntity) {
/* 74 */       this.sourceEntity = Optional.of(sourceEntity.build());
/* 75 */       return this;
/*    */     }
/*    */     
/*    */     public Builder isDirect(boolean direct) {
/* 79 */       this.isDirect = Optional.of(Boolean.valueOf(direct));
/* 80 */       return this;
/*    */     }
/*    */     
/*    */     public DamageSourcePredicate build() {
/* 84 */       return new DamageSourcePredicate((List<TagPredicate<DamageType>>)this.tags.build(), this.directEntity, this.sourceEntity, this.isDirect);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\DamageSourcePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */