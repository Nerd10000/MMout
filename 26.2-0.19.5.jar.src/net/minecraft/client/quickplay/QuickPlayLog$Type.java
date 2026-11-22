/*     */ package net.minecraft.client.quickplay;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Type implements StringRepresentable {
/*  86 */   SINGLEPLAYER("singleplayer"),
/*  87 */   MULTIPLAYER("multiplayer"),
/*  88 */   REALMS("realms");
/*     */   
/*     */   private static final Codec<Type> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/*  90 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */   }
/*     */   
/*     */   Type(String name) {
/*  95 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 100 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\quickplay\QuickPlayLog$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */