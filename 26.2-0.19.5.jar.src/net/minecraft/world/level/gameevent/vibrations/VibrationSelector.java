/*    */ package net.minecraft.world.level.gameevent.vibrations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public class VibrationSelector {
/*    */   public static final Codec<VibrationSelector> CODEC;
/*    */   
/*    */   private Optional<VibrationEvent> currentVibrationData;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(()), (App)Codec.LONG.fieldOf("tick").forGetter(())).apply((Applicative)i, VibrationSelector::new));
/*    */   }
/*    */   
/*    */   public VibrationSelector(Optional<VibrationInfo> currentVibration, long tick) {
/* 17 */     this.currentVibrationData = currentVibration.map(vibrationInfo -> new VibrationEvent(vibrationInfo, tick));
/*    */   }
/*    */   
/*    */   public VibrationSelector() {
/* 21 */     this.currentVibrationData = Optional.empty();
/*    */   }
/*    */   
/*    */   public void addCandidate(VibrationInfo newVibration, long tickTime) {
/* 25 */     if (shouldReplaceVibration(newVibration, tickTime))
/* 26 */       this.currentVibrationData = Optional.of(new VibrationEvent(newVibration, tickTime)); 
/*    */   }
/*    */   
/*    */   private boolean shouldReplaceVibration(VibrationInfo newVibration, long tickTime) {
/* 31 */     if (this.currentVibrationData.isEmpty())
/* 32 */       return true; 
/* 34 */     VibrationEvent previousData = this.currentVibrationData.get();
/* 35 */     long previousTick = previousData.tick();
/* 36 */     if (tickTime != previousTick)
/* 38 */       return false; 
/* 40 */     VibrationInfo previousVibration = previousData.event();
/* 41 */     if (newVibration.distance() < previousVibration.distance())
/* 42 */       return true; 
/* 43 */     if (newVibration.distance() > previousVibration.distance())
/* 44 */       return false; 
/* 46 */     return (VibrationSystem.getGameEventFrequency(newVibration.gameEvent()) > VibrationSystem.getGameEventFrequency(previousVibration.gameEvent()));
/*    */   }
/*    */   
/*    */   public Optional<VibrationInfo> chosenCandidate(long time) {
/* 50 */     if (this.currentVibrationData.isEmpty())
/* 51 */       return Optional.empty(); 
/* 53 */     if (((VibrationEvent)this.currentVibrationData.get()).tick() < time)
/* 54 */       return Optional.of(((VibrationEvent)this.currentVibrationData.get()).event()); 
/* 56 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public void startOver() {
/* 60 */     this.currentVibrationData = Optional.empty();
/*    */   }
/*    */   
/*    */   private static final class VibrationEvent extends Record {
/*    */     private final VibrationInfo event;
/*    */     
/*    */     private final long tick;
/*    */     
/*    */     private VibrationEvent(VibrationInfo event, long tick) {
/* 63 */       this.event = event;
/* 63 */       this.tick = tick;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public VibrationInfo event() {
/* 63 */       return this.event;
/*    */     }
/*    */     
/*    */     public long tick() {
/* 63 */       return this.tick;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */