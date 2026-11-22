/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Type implements StringRepresentable {
/*  80 */   CLEAR("clear", 100000, 0, false, false),
/*  81 */   RAIN("rain", 0, 100000, true, false),
/*  82 */   THUNDER("thunder", 0, 100000, true, true);
/*     */   
/*     */   public static final Codec<Type> CODEC;
/*     */   
/*     */   private final String id;
/*     */   
/*     */   private final int clearTime;
/*     */   
/*     */   private final int rainTime;
/*     */   
/*     */   private final boolean raining;
/*     */   
/*     */   private final boolean thundering;
/*     */   
/*     */   static {
/*  84 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */   }
/*     */   
/*     */   Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) {
/*  93 */     this.id = id;
/*  94 */     this.clearTime = clearTime;
/*  95 */     this.rainTime = rainTime;
/*  96 */     this.raining = raining;
/*  97 */     this.thundering = thundering;
/*     */   }
/*     */   
/*     */   public void apply(ServerLevel level) {
/* 101 */     level.getServer().setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 106 */     return this.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$Weather$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */