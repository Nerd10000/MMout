/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LeashFenceKnotEntity extends BlockAttachedEntity {
/*     */   public static final double OFFSET_Y = 0.375D;
/*     */   
/*     */   public LeashFenceKnotEntity(EntityType<? extends LeashFenceKnotEntity> type, Level level) {
/*  36 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public LeashFenceKnotEntity(Level level, BlockPos pos) {
/*  40 */     super(EntityTypes.LEASH_KNOT, level, pos);
/*  41 */     setPos(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */   
/*     */   protected void recalculateBoundingBox() {
/*  50 */     setPosRaw(this.pos.getX() + 0.5D, this.pos.getY() + 0.375D, this.pos.getZ() + 0.5D);
/*  51 */     double halfWidth = getType().getWidth() / 2.0D;
/*  52 */     double height = getType().getHeight();
/*  53 */     setBoundingBox(new AABB(getX() - halfWidth, getY(), getZ() - halfWidth, getX() + halfWidth, getY() + height, getZ() + halfWidth));
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/*  58 */     return (distance < 1024.0D);
/*     */   }
/*     */   
/*     */   public void dropItem(ServerLevel level, Entity causedBy) {
/*  63 */     playSound(SoundEvents.LEAD_UNTIED, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {}
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {}
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/*  76 */     if (level().isClientSide())
/*  77 */       return (InteractionResult)InteractionResult.SUCCESS; 
/*  79 */     if (player.getItemInHand(hand).is(Items.SHEARS)) {
/*  81 */       InteractionResult result = super.interact(player, hand, location);
/*  82 */       if (result instanceof InteractionResult.Success) {
/*  82 */         InteractionResult.Success success = (InteractionResult.Success)result;
/*  82 */         if (success.wasItemInteraction())
/*  83 */           return result; 
/*     */       } 
/*     */     } 
/*  87 */     boolean attachedMob = false;
/*  89 */     List<Leashable> playerLeashable = Leashable.leashableLeashedTo((Entity)player);
/*  91 */     for (Leashable leashable : playerLeashable) {
/*  92 */       if (leashable.canHaveALeashAttachedTo(this)) {
/*  93 */         leashable.setLeashedTo(this, true);
/*  94 */         attachedMob = true;
/*     */       } 
/*     */     } 
/*  98 */     boolean anyDropped = false;
/*  99 */     if (!attachedMob && !player.isSecondaryUseActive()) {
/* 100 */       List<Leashable> knotLeashable = Leashable.leashableLeashedTo(this);
/* 101 */       for (Leashable mob : knotLeashable) {
/* 102 */         if (mob.canHaveALeashAttachedTo((Entity)player)) {
/* 103 */           mob.setLeashedTo((Entity)player, true);
/* 104 */           anyDropped = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 109 */     if (attachedMob || anyDropped) {
/* 110 */       gameEvent((Holder)GameEvent.BLOCK_ATTACH, (Entity)player);
/* 111 */       playSound(SoundEvents.LEAD_TIED);
/* 112 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 115 */     return super.interact(player, hand, location);
/*     */   }
/*     */   
/*     */   public void notifyLeasheeRemoved(Leashable entity) {
/* 120 */     if (Leashable.leashableLeashedTo(this).isEmpty())
/* 121 */       discard(); 
/*     */   }
/*     */   
/*     */   public boolean survives() {
/* 128 */     return level().getBlockState(this.pos).is(BlockTags.FENCES);
/*     */   }
/*     */   
/*     */   public static LeashFenceKnotEntity getOrCreateKnot(Level level, BlockPos pos) {
/* 132 */     return getKnot(level, pos).orElseGet(() -> createKnot(level, pos));
/*     */   }
/*     */   
/*     */   public static Optional<LeashFenceKnotEntity> getKnot(Level level, BlockPos pos) {
/* 136 */     int x = pos.getX();
/* 137 */     int y = pos.getY();
/* 138 */     int z = pos.getZ();
/* 140 */     List<LeashFenceKnotEntity> knots = level.getEntitiesOfClass(LeashFenceKnotEntity.class, new AABB(x - 1.0D, y - 1.0D, z - 1.0D, x + 1.0D, y + 1.0D, z + 1.0D));
/* 141 */     for (LeashFenceKnotEntity knot : knots) {
/* 142 */       if (knot.getPos().equals(pos))
/* 143 */         return Optional.of(knot); 
/*     */     } 
/* 146 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static LeashFenceKnotEntity createKnot(Level level, BlockPos pos) {
/* 150 */     LeashFenceKnotEntity knot = new LeashFenceKnotEntity(level, pos);
/* 151 */     level.addFreshEntity(knot);
/* 152 */     return knot;
/*     */   }
/*     */   
/*     */   public void playPlacementSound() {
/* 156 */     playSound(SoundEvents.LEAD_TIED, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 161 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, 0, getPos());
/*     */   }
/*     */   
/*     */   public Vec3 getRopeHoldPosition(float partialTickTime) {
/* 166 */     return getPosition(partialTickTime).add(0.0D, 0.2D, 0.0D);
/*     */   }
/*     */   
/*     */   public ItemStack getPickResult() {
/* 171 */     return new ItemStack((ItemLike)Items.LEAD);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\decoration\LeashFenceKnotEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */