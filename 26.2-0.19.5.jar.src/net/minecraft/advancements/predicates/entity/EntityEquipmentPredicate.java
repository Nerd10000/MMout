/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class EntityEquipmentPredicate extends Record implements EntitySubPredicate {
/*     */   private final Optional<ItemPredicate> head;
/*     */   
/*     */   private final Optional<ItemPredicate> chest;
/*     */   
/*     */   private final Optional<ItemPredicate> legs;
/*     */   
/*     */   private final Optional<ItemPredicate> feet;
/*     */   
/*     */   private final Optional<ItemPredicate> body;
/*     */   
/*     */   private final Optional<ItemPredicate> mainhand;
/*     */   
/*     */   private final Optional<ItemPredicate> offhand;
/*     */   
/*     */   public static final Codec<EntityEquipmentPredicate> CODEC;
/*     */   
/*     */   public EntityEquipmentPredicate(Optional<ItemPredicate> head, Optional<ItemPredicate> chest, Optional<ItemPredicate> legs, Optional<ItemPredicate> feet, Optional<ItemPredicate> body, Optional<ItemPredicate> mainhand, Optional<ItemPredicate> offhand) {
/*  24 */     this.head = head;
/*  24 */     this.chest = chest;
/*  24 */     this.legs = legs;
/*  24 */     this.feet = feet;
/*  24 */     this.body = body;
/*  24 */     this.mainhand = mainhand;
/*  24 */     this.offhand = offhand;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/EntityEquipmentPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #24	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityEquipmentPredicate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/EntityEquipmentPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #24	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityEquipmentPredicate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/EntityEquipmentPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #24	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/EntityEquipmentPredicate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<ItemPredicate> head() {
/*  24 */     return this.head;
/*     */   }
/*     */   
/*     */   public Optional<ItemPredicate> chest() {
/*  24 */     return this.chest;
/*     */   }
/*     */   
/*     */   public Optional<ItemPredicate> legs() {
/*  24 */     return this.legs;
/*     */   }
/*     */   
/*     */   public Optional<ItemPredicate> feet() {
/*  24 */     return this.feet;
/*     */   }
/*     */   
/*     */   public Optional<ItemPredicate> body() {
/*  24 */     return this.body;
/*     */   }
/*     */   
/*     */   public Optional<ItemPredicate> mainhand() {
/*  24 */     return this.mainhand;
/*     */   }
/*     */   
/*     */   public Optional<ItemPredicate> offhand() {
/*  24 */     return this.offhand;
/*     */   }
/*     */   
/*     */   static {
/*  33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ItemPredicate.CODEC.optionalFieldOf("head").forGetter(EntityEquipmentPredicate::head), (App)ItemPredicate.CODEC.optionalFieldOf("chest").forGetter(EntityEquipmentPredicate::chest), (App)ItemPredicate.CODEC.optionalFieldOf("legs").forGetter(EntityEquipmentPredicate::legs), (App)ItemPredicate.CODEC.optionalFieldOf("feet").forGetter(EntityEquipmentPredicate::feet), (App)ItemPredicate.CODEC.optionalFieldOf("body").forGetter(EntityEquipmentPredicate::body), (App)ItemPredicate.CODEC.optionalFieldOf("mainhand").forGetter(EntityEquipmentPredicate::mainhand), (App)ItemPredicate.CODEC.optionalFieldOf("offhand").forGetter(EntityEquipmentPredicate::offhand)).apply((Applicative)i, EntityEquipmentPredicate::new));
/*     */   }
/*     */   
/*     */   public static EntityEquipmentPredicate captainPredicate(HolderGetter<Item> items, HolderGetter<BannerPattern> patternGetter) {
/*  44 */     return Builder.equipment()
/*  45 */       .head(
/*  46 */         ItemPredicate.Builder.item()
/*  47 */         .of(items, new ItemLike[] { (ItemLike)Items.BANNER.white() }).withComponents(
/*  49 */           DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.someOf(
/*  50 */               Raid.getBannerComponentPatch(patternGetter).split().added(), new DataComponentType[] { DataComponents.BANNER_PATTERNS, DataComponents.ITEM_NAME })).build()))
/*     */       
/*  55 */       .build();
/*     */   }
/*     */   
/*     */   public boolean matches(Entity entity) {
/*     */     LivingEntity livingEntity;
/*  59 */     if (entity instanceof LivingEntity) {
/*  59 */       livingEntity = (LivingEntity)entity;
/*     */     } else {
/*  60 */       return false;
/*     */     } 
/*  63 */     if (this.head.isPresent() && !((ItemPredicate)this.head.get()).test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.HEAD)))
/*  64 */       return false; 
/*  66 */     if (this.chest.isPresent() && !((ItemPredicate)this.chest.get()).test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.CHEST)))
/*  67 */       return false; 
/*  69 */     if (this.legs.isPresent() && !((ItemPredicate)this.legs.get()).test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.LEGS)))
/*  70 */       return false; 
/*  72 */     if (this.feet.isPresent() && !((ItemPredicate)this.feet.get()).test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.FEET)))
/*  73 */       return false; 
/*  75 */     if (this.body.isPresent() && !((ItemPredicate)this.body.get()).test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.BODY)))
/*  76 */       return false; 
/*  78 */     if (this.mainhand.isPresent() && !((ItemPredicate)this.mainhand.get()).test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.MAINHAND)))
/*  79 */       return false; 
/*  81 */     if (this.offhand.isPresent() && !((ItemPredicate)this.offhand.get()).test((ItemInstance)livingEntity.getItemBySlot(EquipmentSlot.OFFHAND)))
/*  82 */       return false; 
/*  85 */     return true;
/*     */   }
/*     */   
/*     */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/*  90 */     return matches(entity);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  94 */     private Optional<ItemPredicate> head = Optional.empty();
/*     */     
/*  95 */     private Optional<ItemPredicate> chest = Optional.empty();
/*     */     
/*  96 */     private Optional<ItemPredicate> legs = Optional.empty();
/*     */     
/*  97 */     private Optional<ItemPredicate> feet = Optional.empty();
/*     */     
/*  98 */     private Optional<ItemPredicate> body = Optional.empty();
/*     */     
/*  99 */     private Optional<ItemPredicate> mainhand = Optional.empty();
/*     */     
/* 100 */     private Optional<ItemPredicate> offhand = Optional.empty();
/*     */     
/*     */     public static Builder equipment() {
/* 103 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder head(ItemPredicate.Builder head) {
/* 107 */       this.head = Optional.of(head.build());
/* 108 */       return this;
/*     */     }
/*     */     
/*     */     public Builder chest(ItemPredicate.Builder chest) {
/* 112 */       this.chest = Optional.of(chest.build());
/* 113 */       return this;
/*     */     }
/*     */     
/*     */     public Builder legs(ItemPredicate.Builder legs) {
/* 117 */       this.legs = Optional.of(legs.build());
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public Builder feet(ItemPredicate.Builder feet) {
/* 122 */       this.feet = Optional.of(feet.build());
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public Builder body(ItemPredicate.Builder body) {
/* 127 */       this.body = Optional.of(body.build());
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     public Builder mainhand(ItemPredicate.Builder mainhand) {
/* 132 */       this.mainhand = Optional.of(mainhand.build());
/* 133 */       return this;
/*     */     }
/*     */     
/*     */     public Builder offhand(ItemPredicate.Builder offhand) {
/* 137 */       this.offhand = Optional.of(offhand.build());
/* 138 */       return this;
/*     */     }
/*     */     
/*     */     public EntityEquipmentPredicate build() {
/* 142 */       return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.body, this.mainhand, this.offhand);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityEquipmentPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */