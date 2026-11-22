/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*     */ 
/*     */ public final class LeashData {
/*     */   public static final Codec<LeashData> CODEC;
/*     */   
/*     */   private int delayedLeashHolderId;
/*     */   
/*     */   public Entity leashHolder;
/*     */   
/*     */   public Either<UUID, BlockPos> delayedLeashInfo;
/*     */   
/*     */   public double angularMomentum;
/*     */   
/*     */   static {
/* 389 */     CODEC = Codec.xor(UUIDUtil.CODEC.fieldOf("UUID").codec(), BlockPos.CODEC).xmap(LeashData::new, data -> {
/*     */           Entity patt0$temp = data.leashHolder;
/*     */           if (patt0$temp instanceof LeashFenceKnotEntity) {
/*     */             LeashFenceKnotEntity leashKnot = (LeashFenceKnotEntity)patt0$temp;
/*     */             return Either.right(leashKnot.getPos());
/*     */           } 
/*     */           return (data.leashHolder != null) ? Either.left(data.leashHolder.getUUID()) : Objects.<Either>requireNonNull(data.delayedLeashInfo, "Invalid LeashData had no attachment");
/*     */         });
/*     */   }
/*     */   
/*     */   private LeashData(Either<UUID, BlockPos> delayedLeashInfo) {
/* 401 */     this.delayedLeashInfo = delayedLeashInfo;
/*     */   }
/*     */   
/*     */   private LeashData(Entity entity) {
/* 405 */     this.leashHolder = entity;
/*     */   }
/*     */   
/*     */   private LeashData(int entityId) {
/* 409 */     this.delayedLeashHolderId = entityId;
/*     */   }
/*     */   
/*     */   public void setLeashHolder(Entity leashHolder) {
/* 413 */     this.leashHolder = leashHolder;
/* 414 */     this.delayedLeashInfo = null;
/* 415 */     this.delayedLeashHolderId = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Leashable$LeashData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */