/*     */ package net.minecraft.client.telemetry;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum GameMode implements StringRepresentable {
/* 160 */   SURVIVAL("survival", 0),
/* 161 */   CREATIVE("creative", 1),
/* 162 */   ADVENTURE("adventure", 2),
/* 163 */   SPECTATOR("spectator", 6),
/* 165 */   HARDCORE("hardcore", 99);
/*     */   
/*     */   public static final Codec<GameMode> CODEC;
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   static {
/* 168 */     CODEC = (Codec<GameMode>)StringRepresentable.fromEnum(GameMode::values);
/*     */   }
/*     */   
/*     */   GameMode(String key, int id) {
/* 174 */     this.key = key;
/* 175 */     this.id = id;
/*     */   }
/*     */   
/*     */   public int id() {
/* 179 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 184 */     return this.key;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryProperty$GameMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */