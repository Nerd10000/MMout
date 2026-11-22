/*     */ package net.minecraft.world.level.border;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ public final class Settings extends Record {
/*     */   private final double centerX;
/*     */   
/*     */   private final double centerZ;
/*     */   
/*     */   private final double damagePerBlock;
/*     */   
/*     */   private final double safeZone;
/*     */   
/*     */   private final int warningBlocks;
/*     */   
/*     */   private final int warningTime;
/*     */   
/*     */   private final double size;
/*     */   
/*     */   private final long lerpTime;
/*     */   
/*     */   private final double lerpTarget;
/*     */   
/*     */   public Settings(double centerX, double centerZ, double damagePerBlock, double safeZone, int warningBlocks, int warningTime, double size, long lerpTime, double lerpTarget) {
/* 557 */     this.centerX = centerX;
/* 557 */     this.centerZ = centerZ;
/* 557 */     this.damagePerBlock = damagePerBlock;
/* 557 */     this.safeZone = safeZone;
/* 557 */     this.warningBlocks = warningBlocks;
/* 557 */     this.warningTime = warningTime;
/* 557 */     this.size = size;
/* 557 */     this.lerpTime = lerpTime;
/* 557 */     this.lerpTarget = lerpTarget;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/border/WorldBorder$Settings;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #557	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/border/WorldBorder$Settings;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #557	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/border/WorldBorder$Settings;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #557	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public double centerX() {
/* 557 */     return this.centerX;
/*     */   }
/*     */   
/*     */   public double centerZ() {
/* 557 */     return this.centerZ;
/*     */   }
/*     */   
/*     */   public double damagePerBlock() {
/* 557 */     return this.damagePerBlock;
/*     */   }
/*     */   
/*     */   public double safeZone() {
/* 557 */     return this.safeZone;
/*     */   }
/*     */   
/*     */   public int warningBlocks() {
/* 557 */     return this.warningBlocks;
/*     */   }
/*     */   
/*     */   public int warningTime() {
/* 557 */     return this.warningTime;
/*     */   }
/*     */   
/*     */   public double size() {
/* 557 */     return this.size;
/*     */   }
/*     */   
/*     */   public long lerpTime() {
/* 557 */     return this.lerpTime;
/*     */   }
/*     */   
/*     */   public double lerpTarget() {
/* 557 */     return this.lerpTarget;
/*     */   }
/*     */   
/* 558 */   public static final Settings DEFAULT = new Settings(0.0D, 0.0D, 0.2D, 5.0D, 5, 300, 5.9999968E7D, 0L, 0.0D);
/*     */   
/*     */   public static final Codec<Settings> CODEC;
/*     */   
/*     */   static {
/* 559 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.doubleRange(-2.9999984E7D, 2.9999984E7D).fieldOf("center_x").forGetter(Settings::centerX), (App)Codec.doubleRange(-2.9999984E7D, 2.9999984E7D).fieldOf("center_z").forGetter(Settings::centerZ), (App)Codec.DOUBLE.fieldOf("damage_per_block").forGetter(Settings::damagePerBlock), (App)Codec.DOUBLE.fieldOf("safe_zone").forGetter(Settings::safeZone), (App)Codec.INT.fieldOf("warning_blocks").forGetter(Settings::warningBlocks), (App)Codec.INT.fieldOf("warning_time").forGetter(Settings::warningTime), (App)Codec.DOUBLE.fieldOf("size").forGetter(Settings::size), (App)Codec.LONG.fieldOf("lerp_time").forGetter(Settings::lerpTime), (App)Codec.DOUBLE.fieldOf("lerp_target").forGetter(Settings::lerpTarget)).apply((Applicative)i, Settings::new));
/*     */   }
/*     */   
/*     */   public Settings(WorldBorder worldBorder) {
/* 572 */     this(worldBorder.centerX, worldBorder.centerZ, worldBorder.damagePerBlock, worldBorder.safeZone, worldBorder.warningBlocks, worldBorder.warningTime, worldBorder.extent
/*     */         
/* 579 */         .getSize(), worldBorder.extent
/* 580 */         .getLerpTime(), worldBorder.extent
/* 581 */         .getLerpTarget());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\border\WorldBorder$Settings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */