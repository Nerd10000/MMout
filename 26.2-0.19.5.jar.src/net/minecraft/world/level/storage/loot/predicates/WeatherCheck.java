/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class WeatherCheck extends Record implements LootItemCondition {
/*    */   private final Optional<Boolean> isRaining;
/*    */   
/*    */   private final Optional<Boolean> isThundering;
/*    */   
/*    */   public static final MapCodec<WeatherCheck> MAP_CODEC;
/*    */   
/*    */   public WeatherCheck(Optional<Boolean> isRaining, Optional<Boolean> isThundering) {
/* 11 */     this.isRaining = isRaining;
/* 11 */     this.isThundering = isThundering;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/WeatherCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> isRaining() {
/* 11 */     return this.isRaining;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> isThundering() {
/* 11 */     return this.isThundering;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("raining").forGetter(WeatherCheck::isRaining), (App)Codec.BOOL.optionalFieldOf("thundering").forGetter(WeatherCheck::isThundering)).apply((Applicative)i, WeatherCheck::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WeatherCheck> codec() {
/* 22 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 27 */     ServerLevel level = context.getLevel();
/* 29 */     if (this.isRaining.isPresent() && ((Boolean)this.isRaining.get()).booleanValue() != level.isRaining())
/* 30 */       return false; 
/* 33 */     if (this.isThundering.isPresent() && ((Boolean)this.isThundering.get()).booleanValue() != level.isThundering())
/* 34 */       return false; 
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/* 41 */     private Optional<Boolean> isRaining = Optional.empty();
/*    */     
/* 42 */     private Optional<Boolean> isThundering = Optional.empty();
/*    */     
/*    */     public Builder setRaining(boolean raining) {
/* 45 */       this.isRaining = Optional.of(Boolean.valueOf(raining));
/* 46 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setThundering(boolean thundering) {
/* 50 */       this.isThundering = Optional.of(Boolean.valueOf(thundering));
/* 51 */       return this;
/*    */     }
/*    */     
/*    */     public WeatherCheck build() {
/* 56 */       return new WeatherCheck(this.isRaining, this.isThundering);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder weather() {
/* 61 */     return new Builder();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\WeatherCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */