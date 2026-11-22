/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.EvokerFangs;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ class EvokerAttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */   protected int getCastingTime() {
/* 154 */     return 40;
/*     */   }
/*     */   
/*     */   protected int getCastingInterval() {
/* 159 */     return 100;
/*     */   }
/*     */   
/*     */   protected void performSpellCasting() {
/* 165 */     LivingEntity target = Evoker.this.getTarget();
/* 166 */     double minY = Math.min(target.getY(), Evoker.this.getY());
/* 167 */     double maxY = Math.max(target.getY(), Evoker.this.getY()) + 1.0D;
/* 168 */     float angleTowardsTarget = (float)Mth.atan2(target.getZ() - Evoker.this.getZ(), target.getX() - Evoker.this.getX());
/* 169 */     if (Evoker.this.distanceToSqr((Entity)target) < 9.0D) {
/*     */       int i;
/* 171 */       for (i = 0; i < 5; i++) {
/* 172 */         float angle = angleTowardsTarget + i * 3.1415927F * 0.4F;
/* 173 */         createSpellEntity(Evoker.this.getX() + Mth.cos(angle) * 1.5D, Evoker.this.getZ() + Mth.sin(angle) * 1.5D, minY, maxY, angle, 0);
/*     */       } 
/* 176 */       for (i = 0; i < 8; i++) {
/* 177 */         float angle = angleTowardsTarget + i * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
/* 178 */         createSpellEntity(Evoker.this.getX() + Mth.cos(angle) * 2.5D, Evoker.this.getZ() + Mth.sin(angle) * 2.5D, minY, maxY, angle, 3);
/*     */       } 
/*     */     } else {
/* 182 */       for (int i = 0; i < 16; i++) {
/* 183 */         double reach = 1.25D * (i + 1);
/* 184 */         int spellSpeed = 1 * i;
/* 185 */         createSpellEntity(Evoker.this.getX() + Mth.cos(angleTowardsTarget) * reach, Evoker.this.getZ() + Mth.sin(angleTowardsTarget) * reach, minY, maxY, angleTowardsTarget, spellSpeed);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createSpellEntity(double x, double z, double minY, double maxY, float angle, int delayTicks) {
/* 192 */     BlockPos pos = BlockPos.containing(x, maxY, z);
/* 193 */     boolean success = false;
/* 194 */     double topOffset = 0.0D;
/*     */     do {
/* 196 */       BlockPos below = pos.below();
/* 197 */       BlockState belowState = Evoker.this.level().getBlockState(below);
/* 198 */       if (belowState.isFaceSturdy((BlockGetter)Evoker.this.level(), below, Direction.UP)) {
/* 199 */         if (!Evoker.this.level().isEmptyBlock(pos)) {
/* 200 */           BlockState blockState = Evoker.this.level().getBlockState(pos);
/* 201 */           VoxelShape shape = blockState.getCollisionShape((BlockGetter)Evoker.this.level(), pos);
/* 202 */           if (!shape.isEmpty())
/* 203 */             topOffset = shape.max(Direction.Axis.Y); 
/*     */         } 
/* 206 */         success = true;
/*     */         break;
/*     */       } 
/* 209 */       pos = pos.below();
/* 210 */     } while (pos.getY() >= Mth.floor(minY) - 1);
/* 211 */     if (success) {
/* 212 */       Evoker.this.level().addFreshEntity((Entity)new EvokerFangs(Evoker.this.level(), x, pos.getY() + topOffset, z, angle, delayTicks, (LivingEntity)Evoker.this));
/* 213 */       Evoker.this.level().gameEvent((Holder)GameEvent.ENTITY_PLACE, new Vec3(x, pos.getY() + topOffset, z), GameEvent.Context.of((Entity)Evoker.this));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getSpellPrepareSound() {
/* 219 */     return SoundEvents.EVOKER_PREPARE_ATTACK;
/*     */   }
/*     */   
/*     */   protected SpellcasterIllager.IllagerSpell getSpell() {
/* 224 */     return SpellcasterIllager.IllagerSpell.FANGS;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Evoker$EvokerAttackSpellGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */