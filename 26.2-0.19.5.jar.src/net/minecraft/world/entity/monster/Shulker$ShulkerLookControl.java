/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ class ShulkerLookControl extends LookControl {
/*     */   public ShulkerLookControl(Mob mob) {
/* 124 */     super(mob);
/*     */   }
/*     */   
/*     */   protected void clampHeadRotationToBody() {}
/*     */   
/*     */   protected Optional<Float> getYRotD() {
/* 134 */     Direction attachFace = Shulker.this.getAttachFace().getOpposite();
/* 137 */     Vector3f forward = attachFace.getRotation().transform(new Vector3f(Shulker.FORWARD));
/* 139 */     Vec3i upNormal = attachFace.getUnitVec3i();
/* 140 */     Vector3f right = new Vector3f(upNormal.getX(), upNormal.getY(), upNormal.getZ());
/* 141 */     right.cross((Vector3fc)forward);
/* 143 */     double xd = this.wantedX - this.mob.getX();
/* 144 */     double yd = this.wantedY - this.mob.getEyeY();
/* 145 */     double zd = this.wantedZ - this.mob.getZ();
/* 148 */     Vector3f out = new Vector3f((float)xd, (float)yd, (float)zd);
/* 149 */     float deltaRight = right.dot((Vector3fc)out);
/* 150 */     float deltaForward = forward.dot((Vector3fc)out);
/* 152 */     return (Math.abs(deltaRight) > 1.0E-5F || Math.abs(deltaForward) > 1.0E-5F) ? Optional.<Float>of(Float.valueOf((float)(Mth.atan2(-deltaRight, deltaForward) * 57.2957763671875D))) : Optional.<Float>empty();
/*     */   }
/*     */   
/*     */   protected Optional<Float> getXRotD() {
/* 157 */     return Optional.of(Float.valueOf(0.0F));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Shulker$ShulkerLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */