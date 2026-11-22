/*     */ package net.minecraft.client.player;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.ClientAvatarEntity;
/*     */ import net.minecraft.client.entity.ClientAvatarState;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class AbstractClientPlayer extends Player implements ClientAvatarEntity {
/*     */   private PlayerInfo playerInfo;
/*     */   
/*     */   private final boolean showExtraEars;
/*     */   
/*  25 */   private final ClientAvatarState clientAvatarState = new ClientAvatarState();
/*     */   
/*     */   public AbstractClientPlayer(ClientLevel level, GameProfile gameProfile) {
/*  28 */     super((Level)level, gameProfile);
/*  30 */     this.showExtraEars = "deadmau5".equals(getGameProfile().name());
/*     */   }
/*     */   
/*     */   public GameType gameMode() {
/*  35 */     PlayerInfo info = getPlayerInfo();
/*  36 */     return (info != null) ? info.getGameMode() : null;
/*     */   }
/*     */   
/*     */   protected PlayerInfo getPlayerInfo() {
/*  40 */     if (this.playerInfo == null)
/*  41 */       this.playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(getUUID()); 
/*  43 */     return this.playerInfo;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  48 */     this.clientAvatarState.tick(position(), getDeltaMovement());
/*  49 */     super.tick();
/*     */   }
/*     */   
/*     */   protected void addWalkedDistance(float distance) {
/*  53 */     this.clientAvatarState.addWalkDistance(distance);
/*     */   }
/*     */   
/*     */   public ClientAvatarState avatarState() {
/*  58 */     return this.clientAvatarState;
/*     */   }
/*     */   
/*     */   public PlayerSkin getSkin() {
/*  63 */     PlayerInfo info = getPlayerInfo();
/*  64 */     return (info == null) ? DefaultPlayerSkin.get(getUUID()) : info.getSkin();
/*     */   }
/*     */   
/*     */   public Parrot.Variant getParrotVariantOnShoulder(boolean left) {
/*  69 */     return (left ? getShoulderParrotLeft() : getShoulderParrotRight()).orElse(null);
/*     */   }
/*     */   
/*     */   public void rideTick() {
/*  74 */     super.rideTick();
/*  75 */     avatarState().resetBob();
/*     */   }
/*     */   
/*     */   public void aiStep() {
/*  80 */     updateBob();
/*  81 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected void updateBob() {
/*     */     float tBob;
/*  86 */     if (!onGround() || isDeadOrDying() || isSwimming()) {
/*  87 */       tBob = 0.0F;
/*     */     } else {
/*  89 */       tBob = Math.min(0.1F, (float)getDeltaMovement().horizontalDistance());
/*     */     } 
/*  92 */     avatarState().updateBob(tBob);
/*     */   }
/*     */   
/*     */   public float getFieldOfViewModifier(boolean firstPerson, float effectScale) {
/*  96 */     float modifier = 1.0F;
/*  98 */     if ((getAbilities()).flying)
/*  99 */       modifier *= 1.1F; 
/* 102 */     float walkingSpeed = getAbilities().getWalkingSpeed();
/* 103 */     if (walkingSpeed != 0.0F) {
/* 104 */       float speedFactor = (float)getAttributeValue(Attributes.MOVEMENT_SPEED) / walkingSpeed;
/* 105 */       modifier *= (speedFactor + 1.0F) / 2.0F;
/*     */     } 
/* 108 */     if (isUsingItem())
/* 109 */       if (getUseItem().is(Items.BOW)) {
/* 110 */         float scale = Math.min(getTicksUsingItem() / 20.0F, 1.0F);
/* 111 */         modifier *= 1.0F - Mth.square(scale) * 0.15F;
/* 112 */       } else if (firstPerson && isScoping()) {
/* 113 */         return 0.1F;
/*     */       }  
/* 117 */     return Mth.lerp(effectScale, 1.0F, modifier);
/*     */   }
/*     */   
/*     */   public boolean showExtraEars() {
/* 122 */     return this.showExtraEars;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\player\AbstractClientPlayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */