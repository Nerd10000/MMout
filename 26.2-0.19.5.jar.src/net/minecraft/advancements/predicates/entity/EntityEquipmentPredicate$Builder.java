/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ 
/*     */ public class Builder {
/*  94 */   private Optional<ItemPredicate> head = Optional.empty();
/*     */   
/*  95 */   private Optional<ItemPredicate> chest = Optional.empty();
/*     */   
/*  96 */   private Optional<ItemPredicate> legs = Optional.empty();
/*     */   
/*  97 */   private Optional<ItemPredicate> feet = Optional.empty();
/*     */   
/*  98 */   private Optional<ItemPredicate> body = Optional.empty();
/*     */   
/*  99 */   private Optional<ItemPredicate> mainhand = Optional.empty();
/*     */   
/* 100 */   private Optional<ItemPredicate> offhand = Optional.empty();
/*     */   
/*     */   public static Builder equipment() {
/* 103 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder head(ItemPredicate.Builder head) {
/* 107 */     this.head = Optional.of(head.build());
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   public Builder chest(ItemPredicate.Builder chest) {
/* 112 */     this.chest = Optional.of(chest.build());
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public Builder legs(ItemPredicate.Builder legs) {
/* 117 */     this.legs = Optional.of(legs.build());
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public Builder feet(ItemPredicate.Builder feet) {
/* 122 */     this.feet = Optional.of(feet.build());
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public Builder body(ItemPredicate.Builder body) {
/* 127 */     this.body = Optional.of(body.build());
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public Builder mainhand(ItemPredicate.Builder mainhand) {
/* 132 */     this.mainhand = Optional.of(mainhand.build());
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public Builder offhand(ItemPredicate.Builder offhand) {
/* 137 */     this.offhand = Optional.of(offhand.build());
/* 138 */     return this;
/*     */   }
/*     */   
/*     */   public EntityEquipmentPredicate build() {
/* 142 */     return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.body, this.mainhand, this.offhand);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityEquipmentPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */