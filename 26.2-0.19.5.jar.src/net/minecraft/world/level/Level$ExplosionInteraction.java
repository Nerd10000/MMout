/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum ExplosionInteraction implements StringRepresentable {
/* 517 */   NONE("none"),
/* 518 */   BLOCK("block"),
/* 519 */   MOB("mob"),
/* 520 */   TNT("tnt"),
/* 521 */   TRIGGER("trigger");
/*     */   
/*     */   public static final Codec<ExplosionInteraction> CODEC;
/*     */   
/*     */   private final String id;
/*     */   
/*     */   static {
/* 523 */     CODEC = (Codec<ExplosionInteraction>)StringRepresentable.fromEnum(ExplosionInteraction::values);
/*     */   }
/*     */   
/*     */   ExplosionInteraction(String id) {
/* 528 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 533 */     return this.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\Level$ExplosionInteraction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */