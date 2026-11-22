/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DamageSource {
/*     */   private final Holder<DamageType> type;
/*     */   
/*     */   private final Entity causingEntity;
/*     */   
/*     */   private final Entity directEntity;
/*     */   
/*     */   private final Vec3 damageSourcePosition;
/*     */   
/*     */   public String toString() {
/*  23 */     return "DamageSource (" + type().msgId() + ")";
/*     */   }
/*     */   
/*     */   public float getFoodExhaustion() {
/*  27 */     return type().exhaustion();
/*     */   }
/*     */   
/*     */   public boolean isDirect() {
/*  31 */     return (this.causingEntity == this.directEntity);
/*     */   }
/*     */   
/*     */   private DamageSource(Holder<DamageType> type, Entity directEntity, Entity causingEntity, Vec3 damageSourcePosition) {
/*  35 */     this.type = type;
/*  36 */     this.causingEntity = causingEntity;
/*  37 */     this.directEntity = directEntity;
/*  38 */     this.damageSourcePosition = damageSourcePosition;
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> type, Entity directEntity, Entity causingEntity) {
/*  42 */     this(type, directEntity, causingEntity, null);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> type, Vec3 damageSourcePosition) {
/*  46 */     this(type, null, null, damageSourcePosition);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> type, Entity causingEntity) {
/*  50 */     this(type, causingEntity, causingEntity);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> type) {
/*  54 */     this(type, null, null, null);
/*     */   }
/*     */   
/*     */   public Entity getDirectEntity() {
/*  60 */     return this.directEntity;
/*     */   }
/*     */   
/*     */   public Entity getEntity() {
/*  66 */     return this.causingEntity;
/*     */   }
/*     */   
/*     */   public ItemStack getWeaponItem() {
/*  70 */     return (this.directEntity != null) ? this.directEntity.getWeaponItem() : null;
/*     */   }
/*     */   
/*     */   public Component getLocalizedDeathMessage(LivingEntity victim) {
/*  74 */     String deathMsg = "death.attack." + type().msgId();
/*  75 */     if (this.causingEntity != null || this.directEntity != null) {
/*  76 */       Component name = (this.causingEntity == null) ? this.directEntity.getDisplayName() : this.causingEntity.getDisplayName();
/*  77 */       Entity entity = this.causingEntity;
/*  77 */       LivingEntity livingEntity = (LivingEntity)entity;
/*  77 */       ItemStack held = (entity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY;
/*  79 */       if (!held.isEmpty() && held.has(DataComponents.CUSTOM_NAME))
/*  80 */         return (Component)Component.translatable(deathMsg + ".item", new Object[] { victim.getDisplayName(), name, held.getDisplayName() }); 
/*  82 */       return (Component)Component.translatable(deathMsg, new Object[] { victim.getDisplayName(), name });
/*     */     } 
/*  86 */     LivingEntity source = victim.getKillCredit();
/*  87 */     String playerMsg = deathMsg + ".player";
/*  88 */     if (source != null)
/*  89 */       return (Component)Component.translatable(playerMsg, new Object[] { victim.getDisplayName(), source.getDisplayName() }); 
/*  91 */     return (Component)Component.translatable(deathMsg, new Object[] { victim.getDisplayName() });
/*     */   }
/*     */   
/*     */   public String getMsgId() {
/*  96 */     return type().msgId();
/*     */   }
/*     */   
/*     */   public boolean scalesWithDifficulty() {
/* 100 */     switch (type().scaling()) {
/*     */       default:
/* 100 */         throw new MatchException(null, null);
/*     */       case NEVER:
/*     */       
/*     */       case WHEN_CAUSED_BY_LIVING_NON_PLAYER:
/* 100 */         return 
/*     */           
/* 102 */           (this.causingEntity instanceof LivingEntity && !(this.causingEntity instanceof Player));
/*     */       case ALWAYS:
/*     */         break;
/*     */     } 
/*     */     return true;
/*     */   }
/*     */   
/*     */   public boolean isCreativePlayer() {
/* 108 */     Entity entity = getEntity();
/* 108 */     if (entity instanceof Player) {
/* 108 */       Player player = (Player)entity;
/* 108 */       if ((player.getAbilities()).instabuild);
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   public Vec3 getSourcePosition() {
/* 112 */     if (this.damageSourcePosition != null)
/* 113 */       return this.damageSourcePosition; 
/* 114 */     if (this.directEntity != null)
/* 115 */       return this.directEntity.position(); 
/* 117 */     return null;
/*     */   }
/*     */   
/*     */   public Vec3 sourcePositionRaw() {
/* 121 */     return this.damageSourcePosition;
/*     */   }
/*     */   
/*     */   public boolean is(TagKey<DamageType> tag) {
/* 125 */     return this.type.is(tag);
/*     */   }
/*     */   
/*     */   public boolean is(ResourceKey<DamageType> typeKey) {
/* 129 */     return this.type.is(typeKey);
/*     */   }
/*     */   
/*     */   public DamageType type() {
/* 133 */     return (DamageType)this.type.value();
/*     */   }
/*     */   
/*     */   public Holder<DamageType> typeHolder() {
/* 137 */     return this.type;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\damagesource\DamageSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */