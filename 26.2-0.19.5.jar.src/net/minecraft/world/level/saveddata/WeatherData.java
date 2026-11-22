/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ 
/*    */ public final class WeatherData extends SavedData {
/*    */   public static final Codec<WeatherData> CODEC;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("clear_weather_time").forGetter(WeatherData::getClearWeatherTime), (App)Codec.INT.fieldOf("rain_time").forGetter(WeatherData::getRainTime), (App)Codec.INT.fieldOf("thunder_time").forGetter(WeatherData::getThunderTime), (App)Codec.BOOL.fieldOf("raining").forGetter(WeatherData::isRaining), (App)Codec.BOOL.fieldOf("thundering").forGetter(WeatherData::isThundering)).apply((Applicative)i, WeatherData::new));
/*    */   }
/*    */   
/* 17 */   public static final SavedDataType<WeatherData> TYPE = new SavedDataType<>(
/* 18 */       Identifier.withDefaultNamespace("weather"), WeatherData::new, CODEC, DataFixTypes.SAVED_DATA_WEATHER);
/*    */   
/*    */   private int clearWeatherTime;
/*    */   
/*    */   private int rainTime;
/*    */   
/*    */   private int thunderTime;
/*    */   
/*    */   private boolean raining;
/*    */   
/*    */   private boolean thundering;
/*    */   
/*    */   public WeatherData() {}
/*    */   
/*    */   public WeatherData(int clearWeatherTime, int rainTime, int thunderTime, boolean raining, boolean thundering) {
/* 40 */     this.clearWeatherTime = clearWeatherTime;
/* 41 */     this.rainTime = rainTime;
/* 42 */     this.thunderTime = thunderTime;
/* 43 */     this.raining = raining;
/* 44 */     this.thundering = thundering;
/*    */   }
/*    */   
/*    */   public int getClearWeatherTime() {
/* 48 */     return this.clearWeatherTime;
/*    */   }
/*    */   
/*    */   public void setClearWeatherTime(int clearWeatherTime) {
/* 52 */     this.clearWeatherTime = clearWeatherTime;
/* 53 */     setDirty();
/*    */   }
/*    */   
/*    */   public boolean isThundering() {
/* 57 */     return this.thundering;
/*    */   }
/*    */   
/*    */   public void setThundering(boolean thundering) {
/* 61 */     this.thundering = thundering;
/* 62 */     setDirty();
/*    */   }
/*    */   
/*    */   public int getThunderTime() {
/* 66 */     return this.thunderTime;
/*    */   }
/*    */   
/*    */   public void setThunderTime(int thunderTime) {
/* 70 */     this.thunderTime = thunderTime;
/* 71 */     setDirty();
/*    */   }
/*    */   
/*    */   public boolean isRaining() {
/* 75 */     return this.raining;
/*    */   }
/*    */   
/*    */   public void setRaining(boolean raining) {
/* 79 */     this.raining = raining;
/* 80 */     setDirty();
/*    */   }
/*    */   
/*    */   public int getRainTime() {
/* 84 */     return this.rainTime;
/*    */   }
/*    */   
/*    */   public void setRainTime(int rainTime) {
/* 88 */     this.rainTime = rainTime;
/* 89 */     setDirty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\WeatherData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */