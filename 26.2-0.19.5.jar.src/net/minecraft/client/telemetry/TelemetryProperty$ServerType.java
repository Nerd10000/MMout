/*     */ package net.minecraft.client.telemetry;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum ServerType implements StringRepresentable {
/* 139 */   REALM("realm"),
/* 140 */   LOCAL("local"),
/* 141 */   OTHER("server");
/*     */   
/*     */   public static final Codec<ServerType> CODEC;
/*     */   
/*     */   private final String key;
/*     */   
/*     */   static {
/* 144 */     CODEC = (Codec<ServerType>)StringRepresentable.fromEnum(ServerType::values);
/*     */   }
/*     */   
/*     */   ServerType(String key) {
/* 149 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 154 */     return this.key;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryProperty$ServerType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */