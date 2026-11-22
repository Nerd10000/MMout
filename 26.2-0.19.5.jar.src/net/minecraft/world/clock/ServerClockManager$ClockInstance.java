/*     */ package net.minecraft.world.clock;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ class ClockInstance {
/* 155 */   private final Map<ResourceKey<ClockTimeMarker>, ClockTimeMarker> timeMarkers = (Map<ResourceKey<ClockTimeMarker>, ClockTimeMarker>)new Reference2ObjectOpenHashMap();
/*     */   
/*     */   private long totalTicks;
/*     */   
/*     */   private float partialTick;
/*     */   
/* 159 */   private float rate = 1.0F;
/*     */   
/*     */   private boolean paused;
/*     */   
/*     */   public void loadFrom(ClockState state) {
/* 163 */     this.totalTicks = state.totalTicks();
/* 164 */     this.partialTick = state.partialTick();
/* 165 */     this.rate = state.rate();
/* 166 */     this.paused = state.paused();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 170 */     if (!this.paused) {
/* 171 */       this.partialTick += this.rate;
/* 172 */       int fullTicks = Mth.floor(this.partialTick);
/* 173 */       this.partialTick -= fullTicks;
/* 174 */       this.totalTicks += fullTicks;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ClockState packState() {
/* 179 */     return new ClockState(this.totalTicks, this.partialTick, this.rate, this.paused);
/*     */   }
/*     */   
/*     */   public ClockNetworkState packNetworkState(MinecraftServer server) {
/* 183 */     boolean advanceTime = ((Boolean)server.getGlobalGameRules().get(GameRules.ADVANCE_TIME)).booleanValue();
/* 184 */     boolean paused = (this.paused || !advanceTime);
/* 185 */     return new ClockNetworkState(this.totalTicks, this.partialTick, paused ? 0.0F : this.rate);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\clock\ServerClockManager$ClockInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */