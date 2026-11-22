/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Data {
/*     */   public static final Codec<Data> CODEC;
/*     */   
/*     */   public static final String NBT_TAG_KEY = "listener";
/*     */   
/*     */   private VibrationInfo currentVibration;
/*     */   
/*     */   private int travelTimeInTicks;
/*     */   
/*     */   private final VibrationSelector selectionStrategy;
/*     */   
/*     */   private boolean reloadVibrationParticle;
/*     */   
/*     */   static {
/* 161 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(()), (App)VibrationSelector.CODEC.fieldOf("selector").forGetter(Data::getSelectionStrategy), (App)ExtraCodecs.optionalAlwaysPresentFieldOf(ExtraCodecs.NON_NEGATIVE_INT, "event_delay", Integer.valueOf(0)).forGetter(Data::getTravelTimeInTicks)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   private Data(VibrationInfo currentVibration, VibrationSelector selectionStrategy, int travelTimeInTicks, boolean reloadVibrationParticle) {
/* 175 */     this.currentVibration = currentVibration;
/* 176 */     this.travelTimeInTicks = travelTimeInTicks;
/* 177 */     this.selectionStrategy = selectionStrategy;
/* 178 */     this.reloadVibrationParticle = reloadVibrationParticle;
/*     */   }
/*     */   
/*     */   public Data() {
/* 182 */     this(null, new VibrationSelector(), 0, false);
/*     */   }
/*     */   
/*     */   public VibrationSelector getSelectionStrategy() {
/* 186 */     return this.selectionStrategy;
/*     */   }
/*     */   
/*     */   public VibrationInfo getCurrentVibration() {
/* 190 */     return this.currentVibration;
/*     */   }
/*     */   
/*     */   public void setCurrentVibration(VibrationInfo currentVibration) {
/* 194 */     this.currentVibration = currentVibration;
/*     */   }
/*     */   
/*     */   public int getTravelTimeInTicks() {
/* 198 */     return this.travelTimeInTicks;
/*     */   }
/*     */   
/*     */   public void setTravelTimeInTicks(int travelTimeInTicks) {
/* 202 */     this.travelTimeInTicks = travelTimeInTicks;
/*     */   }
/*     */   
/*     */   public void decrementTravelTime() {
/* 206 */     this.travelTimeInTicks = Math.max(0, this.travelTimeInTicks - 1);
/*     */   }
/*     */   
/*     */   public boolean shouldReloadVibrationParticle() {
/* 210 */     return this.reloadVibrationParticle;
/*     */   }
/*     */   
/*     */   public void setReloadVibrationParticle(boolean reloadVibrationParticle) {
/* 214 */     this.reloadVibrationParticle = reloadVibrationParticle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */