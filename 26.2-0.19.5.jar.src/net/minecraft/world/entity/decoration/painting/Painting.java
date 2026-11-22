/*     */ package net.minecraft.world.entity.decoration.painting;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.PaintingVariantTags;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.decoration.HangingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Painting extends HangingEntity {
/*  42 */   private static final EntityDataAccessor<Holder<PaintingVariant>> DATA_PAINTING_VARIANT_ID = SynchedEntityData.defineId(Painting.class, EntityDataSerializers.PAINTING_VARIANT);
/*     */   
/*     */   public static final float DEPTH = 0.0625F;
/*     */   
/*     */   public Painting(EntityType<? extends Painting> type, Level level) {
/*  47 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  52 */     super.defineSynchedData(entityData);
/*  53 */     entityData.define(DATA_PAINTING_VARIANT_ID, VariantUtils.getAny(registryAccess(), Registries.PAINTING_VARIANT));
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  58 */     super.onSyncedDataUpdated(accessor);
/*  59 */     if (DATA_PAINTING_VARIANT_ID.equals(accessor))
/*  60 */       recalculateBoundingBox(); 
/*     */   }
/*     */   
/*     */   private void setVariant(Holder<PaintingVariant> variant) {
/*  65 */     this.entityData.set(DATA_PAINTING_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   public Holder<PaintingVariant> getVariant() {
/*  69 */     return (Holder<PaintingVariant>)this.entityData.get(DATA_PAINTING_VARIANT_ID);
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/*  74 */     if (type == DataComponents.PAINTING_VARIANT)
/*  75 */       return (T)castComponentValue(type, getVariant()); 
/*  78 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/*  83 */     applyImplicitComponentIfPresent(components, DataComponents.PAINTING_VARIANT);
/*  84 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/*  89 */     if (type == DataComponents.PAINTING_VARIANT) {
/*  90 */       setVariant((Holder<PaintingVariant>)castComponentValue(DataComponents.PAINTING_VARIANT, value));
/*  91 */       return true;
/*     */     } 
/*  94 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public static Optional<Painting> create(Level level, BlockPos pos, Direction direction) {
/*  98 */     Painting candidate = new Painting(level, pos);
/* 100 */     List<Holder<PaintingVariant>> potentialVariants = new ArrayList<>();
/* 101 */     Objects.requireNonNull(potentialVariants);
/* 101 */     level.registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE).forEach(potentialVariants::add);
/* 102 */     if (potentialVariants.isEmpty())
/* 103 */       return Optional.empty(); 
/* 106 */     candidate.setDirection(direction);
/* 107 */     potentialVariants.removeIf(variant -> {
/*     */           candidate.setVariant(variant);
/*     */           return !candidate.survives();
/*     */         });
/* 112 */     if (potentialVariants.isEmpty())
/* 113 */       return Optional.empty(); 
/* 119 */     int largestPaintingAreaSize = potentialVariants.stream().mapToInt(Painting::variantArea).max().orElse(0);
/* 121 */     potentialVariants.removeIf(variant -> (variantArea(variant) < largestPaintingAreaSize));
/* 122 */     Optional<Holder<PaintingVariant>> selectedVariant = Util.getRandomSafe(potentialVariants, candidate.random);
/* 123 */     if (selectedVariant.isEmpty())
/* 124 */       return Optional.empty(); 
/* 126 */     candidate.setVariant(selectedVariant.get());
/* 127 */     candidate.setDirection(direction);
/* 128 */     return Optional.of(candidate);
/*     */   }
/*     */   
/*     */   private static int variantArea(Holder<PaintingVariant> variant) {
/* 132 */     return ((PaintingVariant)variant.value()).area();
/*     */   }
/*     */   
/*     */   private Painting(Level level, BlockPos blockPos) {
/* 136 */     super(EntityTypes.PAINTING, level, blockPos);
/*     */   }
/*     */   
/*     */   public Painting(Level level, BlockPos blockPos, Direction direction, Holder<PaintingVariant> variant) {
/* 140 */     this(level, blockPos);
/* 141 */     setVariant(variant);
/* 142 */     setDirection(direction);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 147 */     output.store("facing", Direction.LEGACY_ID_CODEC_2D, getDirection());
/* 148 */     super.addAdditionalSaveData(output);
/* 150 */     VariantUtils.writeVariant(output, getVariant());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 155 */     Direction direction = input.read("facing", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
/* 156 */     super.readAdditionalSaveData(input);
/* 157 */     setDirection(direction);
/* 159 */     VariantUtils.readVariant(input, Registries.PAINTING_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */   
/*     */   protected AABB calculateBoundingBox(BlockPos pos, Direction direction) {
/* 164 */     float shiftToBlockWall = 0.46875F;
/* 165 */     Vec3 attachedToWall = Vec3.atCenterOf((Vec3i)pos).relative(direction, -0.46875D);
/* 167 */     PaintingVariant variant = (PaintingVariant)getVariant().value();
/* 168 */     double horizontalOffset = offsetForPaintingSize(variant.width());
/* 169 */     double verticalOffset = offsetForPaintingSize(variant.height());
/* 171 */     Direction left = direction.getCounterClockWise();
/* 172 */     Vec3 position = attachedToWall.relative(left, horizontalOffset).relative(Direction.UP, verticalOffset);
/* 174 */     Direction.Axis axis = direction.getAxis();
/* 175 */     double xSize = (axis == Direction.Axis.X) ? 0.0625D : variant.width();
/* 176 */     double ySize = variant.height();
/* 177 */     double zSize = (axis == Direction.Axis.Z) ? 0.0625D : variant.width();
/* 179 */     return AABB.ofSize(position, xSize, ySize, zSize);
/*     */   }
/*     */   
/*     */   private double offsetForPaintingSize(int size) {
/* 186 */     return (size % 2 == 0) ? 0.5D : 0.0D;
/*     */   }
/*     */   
/*     */   public void dropItem(ServerLevel level, Entity causedBy) {
/* 191 */     if (!((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue())
/*     */       return; 
/* 195 */     playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
/* 197 */     if (causedBy instanceof Player) {
/* 197 */       Player player = (Player)causedBy;
/* 198 */       if (player.hasInfiniteMaterials())
/*     */         return; 
/*     */     } 
/* 203 */     spawnAtLocation(level, (ItemLike)Items.PAINTING);
/*     */   }
/*     */   
/*     */   public void playPlacementSound() {
/* 208 */     playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void snapTo(double x, double y, double z, float yRot, float xRot) {
/* 213 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   public Vec3 trackingPosition() {
/* 218 */     return Vec3.atLowerCornerOf((Vec3i)this.pos);
/*     */   }
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 223 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket((Entity)this, getDirection().get3DDataValue(), getPos());
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 228 */     super.recreateFromPacket(packet);
/* 229 */     setDirection(Direction.from3DDataValue(packet.getData()));
/*     */   }
/*     */   
/*     */   public ItemStack getPickResult() {
/* 234 */     return new ItemStack((ItemLike)Items.PAINTING);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\decoration\painting\Painting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */