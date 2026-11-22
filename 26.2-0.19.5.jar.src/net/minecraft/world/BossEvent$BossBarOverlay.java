/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum BossBarOverlay implements StringRepresentable {
/* 126 */   PROGRESS("progress"),
/* 127 */   NOTCHED_6("notched_6"),
/* 128 */   NOTCHED_10("notched_10"),
/* 129 */   NOTCHED_12("notched_12"),
/* 130 */   NOTCHED_20("notched_20");
/*     */   
/*     */   public static final Codec<BossBarOverlay> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 133 */     CODEC = (Codec<BossBarOverlay>)StringRepresentable.fromEnum(BossBarOverlay::values);
/*     */   }
/*     */   
/*     */   BossBarOverlay(String name) {
/* 138 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 142 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 147 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\BossEvent$BossBarOverlay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */