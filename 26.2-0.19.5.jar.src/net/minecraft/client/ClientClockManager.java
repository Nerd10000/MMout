/*    */ package net.minecraft.client;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.clock.ClockManager;
/*    */ import net.minecraft.world.clock.ClockNetworkState;
/*    */ import net.minecraft.world.clock.WorldClock;
/*    */ 
/*    */ public class ClientClockManager implements ClockManager {
/* 13 */   private final Map<Holder<WorldClock>, ClockInstance> clocks = new HashMap<>();
/*    */   
/*    */   private long lastTickGameTime;
/*    */   
/*    */   private ClockInstance getInstance(Holder<WorldClock> definition) {
/* 17 */     return this.clocks.computeIfAbsent(definition, paramHolder -> new ClockInstance());
/*    */   }
/*    */   
/*    */   public void tick(long gameTime) {
/* 21 */     long gameTimeDelta = gameTime - this.lastTickGameTime;
/* 22 */     this.lastTickGameTime = gameTime;
/* 24 */     for (ClockInstance instance : this.clocks.values()) {
/* 25 */       double newPartialTicks = instance.partialTick + gameTimeDelta * instance.rate;
/* 26 */       long fullTicks = Mth.floor(newPartialTicks);
/* 27 */       instance.partialTick = (float)(newPartialTicks - fullTicks);
/* 28 */       instance.totalTicks += fullTicks;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void handleUpdates(long gameTime, Map<Holder<WorldClock>, ClockNetworkState> updates) {
/* 34 */     tick(gameTime);
/* 36 */     updates.forEach((definition, state) -> {
/*    */           ClockInstance clock = getInstance(definition);
/*    */           clock.totalTicks = state.totalTicks();
/*    */           clock.partialTick = state.partialTick();
/*    */           clock.rate = state.rate();
/*    */         });
/*    */   }
/*    */   
/*    */   public long getTotalTicks(Holder<WorldClock> definition) {
/* 46 */     return (getInstance(definition)).totalTicks;
/*    */   }
/*    */   
/*    */   private static class ClockInstance {
/*    */     private long totalTicks;
/*    */     
/*    */     private float partialTick;
/*    */     
/* 52 */     private float rate = 1.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\ClientClockManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */