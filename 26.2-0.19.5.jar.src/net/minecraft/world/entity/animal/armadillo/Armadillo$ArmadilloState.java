/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum ArmadilloState implements StringRepresentable {
/* 427 */   IDLE("idle", false, 0, 0) {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/* 430 */       return false;
/*     */     }
/*     */   },
/* 433 */   ROLLING("rolling", true, 10, 1) {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/* 436 */       return (ticksInState > 5L);
/*     */     }
/*     */   },
/* 439 */   SCARED("scared", true, 50, 2) {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/* 442 */       return true;
/*     */     }
/*     */   },
/* 445 */   UNROLLING("unrolling", true, 30, 3) {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/* 448 */       return (ticksInState < 26L);
/*     */     }
/*     */   };
/*     */   
/*     */   private static final Codec<ArmadilloState> CODEC;
/*     */   
/*     */   private static final IntFunction<ArmadilloState> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, ArmadilloState> STREAM_CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean isThreatened;
/*     */   
/*     */   private final int animationDuration;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   static {
/* 453 */     CODEC = (Codec<ArmadilloState>)StringRepresentable.fromEnum(ArmadilloState::values);
/* 454 */     BY_ID = ByIdMap.continuous(ArmadilloState::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 456 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ArmadilloState::id);
/*     */   }
/*     */   
/*     */   ArmadilloState(String name, boolean isThreatened, int animationDuration, int id) {
/* 464 */     this.name = name;
/* 465 */     this.isThreatened = isThreatened;
/* 466 */     this.animationDuration = animationDuration;
/* 467 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 472 */     return this.name;
/*     */   }
/*     */   
/*     */   private int id() {
/* 476 */     return this.id;
/*     */   }
/*     */   
/*     */   public boolean isThreatened() {
/* 482 */     return this.isThreatened;
/*     */   }
/*     */   
/*     */   public int animationDuration() {
/* 486 */     return this.animationDuration;
/*     */   }
/*     */   
/*     */   public abstract boolean shouldHideInShell(long paramLong);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\armadillo\Armadillo$ArmadilloState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */