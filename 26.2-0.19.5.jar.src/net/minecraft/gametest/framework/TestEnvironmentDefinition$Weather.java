/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public final class Weather extends Record implements TestEnvironmentDefinition<TestEnvironmentDefinition.Weather.Type> {
/*     */   private final Type weather;
/*     */   
/*     */   public static final MapCodec<Weather> CODEC;
/*     */   
/*     */   public Weather(Type weather) {
/*  78 */     this.weather = weather;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #78	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #78	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #78	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Type weather() {
/*  78 */     return this.weather;
/*     */   }
/*     */   
/*     */   public enum Type implements StringRepresentable {
/*  80 */     CLEAR("clear", 100000, 0, false, false),
/*  81 */     RAIN("rain", 0, 100000, true, false),
/*  82 */     THUNDER("thunder", 0, 100000, true, true);
/*     */     
/*  84 */     public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */     
/*     */     private final String id;
/*     */     
/*     */     private final int clearTime;
/*     */     
/*     */     private final int rainTime;
/*     */     
/*     */     private final boolean raining;
/*     */     
/*     */     private final boolean thundering;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) {
/*  93 */       this.id = id;
/*  94 */       this.clearTime = clearTime;
/*  95 */       this.rainTime = rainTime;
/*  96 */       this.raining = raining;
/*  97 */       this.thundering = thundering;
/*     */     }
/*     */     
/*     */     public void apply(ServerLevel level) {
/* 101 */       level.getServer().setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering);
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 106 */       return this.id;
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/* 110 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Type.CODEC.fieldOf("weather").forGetter(Weather::weather)).apply((Applicative)i, Weather::new));
/*     */   }
/*     */   
/*     */   public Type setup(ServerLevel level) {
/*     */     Type previous;
/* 117 */     if (level.isThundering()) {
/* 118 */       previous = Type.THUNDER;
/* 119 */     } else if (level.isRaining()) {
/* 120 */       previous = Type.RAIN;
/*     */     } else {
/* 122 */       previous = Type.CLEAR;
/*     */     } 
/* 124 */     this.weather.apply(level);
/* 125 */     return previous;
/*     */   }
/*     */   
/*     */   public void teardown(ServerLevel level, Type saveData) {
/* 130 */     level.resetWeatherCycle();
/* 131 */     saveData.apply(level);
/*     */   }
/*     */   
/*     */   public MapCodec<Weather> codec() {
/* 136 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$Weather.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */