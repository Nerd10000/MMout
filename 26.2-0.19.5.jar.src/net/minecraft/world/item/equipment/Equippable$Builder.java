/*     */ package net.minecraft.world.item.equipment;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ 
/*     */ public class Builder {
/*     */   private final EquipmentSlot slot;
/*     */   
/* 166 */   private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
/*     */   
/* 167 */   private Optional<ResourceKey<EquipmentAsset>> assetId = Optional.empty();
/*     */   
/* 168 */   private Optional<Identifier> cameraOverlay = Optional.empty();
/*     */   
/* 169 */   private Optional<HolderSet<EntityType<?>>> allowedEntities = Optional.empty();
/*     */   
/*     */   private boolean dispensable = true;
/*     */   
/*     */   private boolean swappable = true;
/*     */   
/*     */   private boolean damageOnHurt = true;
/*     */   
/*     */   private boolean equipOnInteract;
/*     */   
/*     */   private boolean canBeSheared;
/*     */   
/* 175 */   private Holder<SoundEvent> shearingSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP);
/*     */   
/*     */   private Builder(EquipmentSlot slot) {
/* 178 */     this.slot = slot;
/*     */   }
/*     */   
/*     */   public Builder setEquipSound(Holder<SoundEvent> equipSound) {
/* 182 */     this.equipSound = equipSound;
/* 183 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setAsset(ResourceKey<EquipmentAsset> assetId) {
/* 187 */     this.assetId = Optional.of(assetId);
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCameraOverlay(Identifier cameraOverlay) {
/* 192 */     this.cameraOverlay = Optional.of(cameraOverlay);
/* 193 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setAllowedEntities(EntityType<?>... allowedEntities) {
/* 197 */     return setAllowedEntities((HolderSet<EntityType<?>>)HolderSet.direct(EntityType::builtInRegistryHolder, (Object[])allowedEntities));
/*     */   }
/*     */   
/*     */   public Builder setAllowedEntities(HolderSet<EntityType<?>> allowedEntities) {
/* 201 */     this.allowedEntities = Optional.of(allowedEntities);
/* 202 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDispensable(boolean dispensable) {
/* 206 */     this.dispensable = dispensable;
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSwappable(boolean swappable) {
/* 211 */     this.swappable = swappable;
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDamageOnHurt(boolean damageOnHurt) {
/* 216 */     this.damageOnHurt = damageOnHurt;
/* 217 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setEquipOnInteract(boolean equipOnInteract) {
/* 221 */     this.equipOnInteract = equipOnInteract;
/* 222 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCanBeSheared(boolean canBeSheared) {
/* 226 */     this.canBeSheared = canBeSheared;
/* 227 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setShearingSound(Holder<SoundEvent> shearingSound) {
/* 231 */     this.shearingSound = shearingSound;
/* 232 */     return this;
/*     */   }
/*     */   
/*     */   public Equippable build() {
/* 236 */     return new Equippable(this.slot, this.equipSound, this.assetId, this.cameraOverlay, this.allowedEntities, this.dispensable, this.swappable, this.damageOnHurt, this.equipOnInteract, this.canBeSheared, this.shearingSound);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\Equippable$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */