/*     */ package net.minecraft.client;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
/*     */ 
/*     */ public interface DeltaTracker {
/*  13 */   public static final DeltaTracker ZERO = new DefaultValue(0.0F);
/*     */   
/*  14 */   public static final DeltaTracker ONE = new DefaultValue(1.0F);
/*     */   
/*     */   float getGameTimeDeltaTicks();
/*     */   
/*     */   float getGameTimeDeltaPartialTick(boolean paramBoolean);
/*     */   
/*     */   float getRealtimeDeltaTicks();
/*     */   
/*     */   public static class Timer implements DeltaTracker {
/*     */     private float deltaTicks;
/*     */     
/*     */     private float deltaTickResidual;
/*     */     
/*     */     private float realtimeDeltaTicks;
/*     */     
/*     */     private float pausedDeltaTickResidual;
/*     */     
/*     */     private long lastMs;
/*     */     
/*     */     private long lastUiMs;
/*     */     
/*     */     private final float msPerTick;
/*     */     
/*     */     private final FloatUnaryOperator targetMsptProvider;
/*     */     
/*     */     private boolean paused;
/*     */     
/*     */     private boolean frozen;
/*     */     
/*     */     public Timer(float ticksPerSecond, long currentMs, FloatUnaryOperator targetMsptProvider) {
/*  32 */       this.msPerTick = 1000.0F / ticksPerSecond;
/*  33 */       this.lastUiMs = this.lastMs = currentMs;
/*  34 */       this.targetMsptProvider = targetMsptProvider;
/*     */     }
/*     */     
/*     */     public int advanceGameTime(long currentMs) {
/*  38 */       this.deltaTicks = (float)(currentMs - this.lastMs) / this.targetMsptProvider.apply(this.msPerTick);
/*  39 */       this.lastMs = currentMs;
/*  41 */       this.deltaTickResidual += this.deltaTicks;
/*  42 */       int ticks = (int)this.deltaTickResidual;
/*  43 */       this.deltaTickResidual -= ticks;
/*  44 */       return ticks;
/*     */     }
/*     */     
/*     */     public void advanceRealTime(long currentMs) {
/*  48 */       this.realtimeDeltaTicks = (float)(currentMs - this.lastUiMs) / this.msPerTick;
/*  49 */       this.lastUiMs = currentMs;
/*     */     }
/*     */     
/*     */     public void updatePauseState(boolean pauseState) {
/*  53 */       if (pauseState) {
/*  54 */         pause();
/*     */       } else {
/*  56 */         unPause();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void pause() {
/*  61 */       if (!this.paused)
/*  62 */         this.pausedDeltaTickResidual = this.deltaTickResidual; 
/*  64 */       this.paused = true;
/*     */     }
/*     */     
/*     */     private void unPause() {
/*  68 */       if (this.paused)
/*  69 */         this.deltaTickResidual = this.pausedDeltaTickResidual; 
/*  71 */       this.paused = false;
/*     */     }
/*     */     
/*     */     public void updateFrozenState(boolean frozen) {
/*  75 */       this.frozen = frozen;
/*     */     }
/*     */     
/*     */     public float getGameTimeDeltaTicks() {
/*  80 */       return this.deltaTicks;
/*     */     }
/*     */     
/*     */     public float getGameTimeDeltaPartialTick(boolean ignoreFrozenGame) {
/*  85 */       if (!ignoreFrozenGame && this.frozen)
/*  86 */         return 1.0F; 
/*  88 */       return this.paused ? this.pausedDeltaTickResidual : this.deltaTickResidual;
/*     */     }
/*     */     
/*     */     public float getRealtimeDeltaTicks() {
/*  93 */       if (this.realtimeDeltaTicks > 7.0F)
/*  97 */         return 0.5F; 
/*  99 */       return this.realtimeDeltaTicks;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DefaultValue implements DeltaTracker {
/*     */     private final float value;
/*     */     
/*     */     private DefaultValue(float value) {
/* 107 */       this.value = value;
/*     */     }
/*     */     
/*     */     public float getGameTimeDeltaTicks() {
/* 112 */       return this.value;
/*     */     }
/*     */     
/*     */     public float getGameTimeDeltaPartialTick(boolean ignored) {
/* 117 */       return this.value;
/*     */     }
/*     */     
/*     */     public float getRealtimeDeltaTicks() {
/* 122 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\DeltaTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */