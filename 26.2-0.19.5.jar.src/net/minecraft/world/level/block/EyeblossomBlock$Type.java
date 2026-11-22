/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.TrailParticleOption;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public enum Type {
/* 42 */   OPEN(true, MobEffects.BLINDNESS, 11.0F, SoundEvents.EYEBLOSSOM_OPEN_LONG, SoundEvents.EYEBLOSSOM_OPEN, 16545810),
/* 43 */   CLOSED(false, MobEffects.NAUSEA, 7.0F, SoundEvents.EYEBLOSSOM_CLOSE_LONG, SoundEvents.EYEBLOSSOM_CLOSE, 6250335);
/*    */   
/*    */   private final boolean open;
/*    */   
/*    */   private final Holder<MobEffect> effect;
/*    */   
/*    */   private final float effectDuration;
/*    */   
/*    */   private final SoundEvent longSwitchSound;
/*    */   
/*    */   private final SoundEvent shortSwitchSound;
/*    */   
/*    */   private final int particleColor;
/*    */   
/*    */   Type(boolean open, Holder<MobEffect> effect, float duration, SoundEvent longSwitchSound, SoundEvent shortSwitchSound, int particleColor) {
/* 53 */     this.open = open;
/* 54 */     this.effect = effect;
/* 55 */     this.effectDuration = duration;
/* 56 */     this.longSwitchSound = longSwitchSound;
/* 57 */     this.shortSwitchSound = shortSwitchSound;
/* 58 */     this.particleColor = particleColor;
/*    */   }
/*    */   
/*    */   public Block block() {
/* 62 */     return this.open ? Blocks.OPEN_EYEBLOSSOM : Blocks.CLOSED_EYEBLOSSOM;
/*    */   }
/*    */   
/*    */   public BlockState state() {
/* 66 */     return block().defaultBlockState();
/*    */   }
/*    */   
/*    */   public Type transform() {
/* 70 */     return fromBoolean(!this.open);
/*    */   }
/*    */   
/*    */   public boolean emitSounds() {
/* 74 */     return this.open;
/*    */   }
/*    */   
/*    */   public static Type fromBoolean(boolean open) {
/* 78 */     return open ? OPEN : CLOSED;
/*    */   }
/*    */   
/*    */   public void spawnTransformParticle(ServerLevel level, BlockPos pos, RandomSource random) {
/* 82 */     Vec3 start = Vec3.atCenterOf((Vec3i)pos);
/* 83 */     double lifetime = 0.5D + random.nextDouble();
/* 84 */     Vec3 velocity = new Vec3(random.nextDouble() - 0.5D, random.nextDouble() + 1.0D, random.nextDouble() - 0.5D);
/* 85 */     Vec3 target = start.add(velocity.scale(lifetime));
/* 86 */     TrailParticleOption particle = new TrailParticleOption(target, this.particleColor, (int)(20.0D * lifetime));
/* 87 */     level.sendParticles((ParticleOptions)particle, start.x, start.y, start.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */   
/*    */   public SoundEvent longSwitchSound() {
/* 91 */     return this.longSwitchSound;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\EyeblossomBlock$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */