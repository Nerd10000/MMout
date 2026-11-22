/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*     */ import net.minecraft.world.clock.ClockManager;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public final class Timelines extends Record implements TestEnvironmentDefinition<EnvironmentAttributeSystem> {
/*     */   private final List<Holder<Timeline>> timelines;
/*     */   
/*     */   public static final MapCodec<Timelines> CODEC;
/*     */   
/*     */   public Timelines(List<Holder<Timeline>> timelines) {
/* 272 */     this.timelines = timelines;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #272	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #272	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #272	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Timelines;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<Holder<Timeline>> timelines() {
/* 272 */     return this.timelines;
/*     */   }
/*     */   
/*     */   static {
/* 273 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Timeline.CODEC.listOf().fieldOf("timelines").forGetter(Timelines::timelines)).apply((Applicative)i, Timelines::new));
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeSystem setup(ServerLevel level) {
/* 279 */     EnvironmentAttributeSystem.Builder builder = EnvironmentAttributeSystem.builder().addDefaultLayers((Level)level);
/* 281 */     for (Holder<Timeline> timeline : this.timelines)
/* 282 */       builder.addTimelineLayer(timeline, (ClockManager)level.clockManager()); 
/* 285 */     return level.setEnvironmentAttributes(builder.build());
/*     */   }
/*     */   
/*     */   public void teardown(ServerLevel level, EnvironmentAttributeSystem saveData) {
/* 290 */     level.setEnvironmentAttributes(saveData);
/*     */   }
/*     */   
/*     */   public MapCodec<Timelines> codec() {
/* 295 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$Timelines.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */