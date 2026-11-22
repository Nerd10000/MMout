/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum WeatherState implements StringRepresentable {
/*  75 */   UNAFFECTED("unaffected"),
/*  76 */   EXPOSED("exposed"),
/*  77 */   WEATHERED("weathered"),
/*  78 */   OXIDIZED("oxidized");
/*     */   
/*     */   public static final IntFunction<WeatherState> BY_ID;
/*     */   
/*     */   public static final Codec<WeatherState> CODEC;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, WeatherState> STREAM_CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/*  80 */     BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*  81 */     CODEC = (Codec<WeatherState>)StringRepresentable.fromEnum(WeatherState::values);
/*  82 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*     */   }
/*     */   
/*     */   WeatherState(String name) {
/*  87 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/*  92 */     return this.name;
/*     */   }
/*     */   
/*     */   public static void forEach(Consumer<WeatherState> consumer) {
/*  96 */     for (WeatherState weatherState : values())
/*  97 */       consumer.accept(weatherState); 
/*     */   }
/*     */   
/*     */   public WeatherState next() {
/* 102 */     return BY_ID.apply(ordinal() + 1);
/*     */   }
/*     */   
/*     */   public WeatherState previous() {
/* 106 */     return BY_ID.apply(ordinal() - 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopper$WeatherState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */