/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.math.Transformation;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Brightness;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class Display extends Entity {
/*  46 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int NO_BRIGHTNESS_OVERRIDE = -1;
/*     */   
/*  52 */   private static final EntityDataAccessor<Integer> DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  54 */   private static final EntityDataAccessor<Integer> DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  56 */   private static final EntityDataAccessor<Integer> DATA_POS_ROT_INTERPOLATION_DURATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  58 */   private static final EntityDataAccessor<Vector3fc> DATA_TRANSLATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.VECTOR3);
/*     */   
/*  60 */   private static final EntityDataAccessor<Vector3fc> DATA_SCALE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.VECTOR3);
/*     */   
/*  62 */   private static final EntityDataAccessor<Quaternionfc> DATA_LEFT_ROTATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.QUATERNION);
/*     */   
/*  64 */   private static final EntityDataAccessor<Quaternionfc> DATA_RIGHT_ROTATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.QUATERNION);
/*     */   
/*  66 */   private static final EntityDataAccessor<Byte> DATA_BILLBOARD_RENDER_CONSTRAINTS_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.BYTE);
/*     */   
/*  68 */   private static final EntityDataAccessor<Integer> DATA_BRIGHTNESS_OVERRIDE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  70 */   private static final EntityDataAccessor<Float> DATA_VIEW_RANGE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  72 */   private static final EntityDataAccessor<Float> DATA_SHADOW_RADIUS_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  74 */   private static final EntityDataAccessor<Float> DATA_SHADOW_STRENGTH_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  76 */   private static final EntityDataAccessor<Float> DATA_WIDTH_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  78 */   private static final EntityDataAccessor<Float> DATA_HEIGHT_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  80 */   private static final EntityDataAccessor<Integer> DATA_GLOW_COLOR_OVERRIDE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  82 */   private static final IntSet RENDER_STATE_IDS = IntSet.of(new int[] { DATA_TRANSLATION_ID
/*  83 */         .id(), DATA_SCALE_ID
/*  84 */         .id(), DATA_LEFT_ROTATION_ID
/*  85 */         .id(), DATA_RIGHT_ROTATION_ID
/*  86 */         .id(), DATA_BILLBOARD_RENDER_CONSTRAINTS_ID
/*  87 */         .id(), DATA_BRIGHTNESS_OVERRIDE_ID
/*  88 */         .id(), DATA_SHADOW_RADIUS_ID
/*  89 */         .id(), DATA_SHADOW_STRENGTH_ID
/*  90 */         .id() });
/*     */   
/*     */   private static final int INITIAL_TRANSFORMATION_INTERPOLATION_DURATION = 0;
/*     */   
/*     */   private static final int INITIAL_TRANSFORMATION_START_INTERPOLATION = 0;
/*     */   
/*     */   private static final int INITIAL_POS_ROT_INTERPOLATION_DURATION = 0;
/*     */   
/*     */   private static final float INITIAL_SHADOW_RADIUS = 0.0F;
/*     */   
/*     */   private static final float INITIAL_SHADOW_STRENGTH = 1.0F;
/*     */   
/*     */   private static final float INITIAL_VIEW_RANGE = 1.0F;
/*     */   
/*     */   private static final float INITIAL_WIDTH = 0.0F;
/*     */   
/*     */   private static final float INITIAL_HEIGHT = 0.0F;
/*     */   
/*     */   private static final int NO_GLOW_COLOR_OVERRIDE = -1;
/*     */   
/*     */   public static final String TAG_POS_ROT_INTERPOLATION_DURATION = "teleport_duration";
/*     */   
/*     */   public static final String TAG_TRANSFORMATION_INTERPOLATION_DURATION = "interpolation_duration";
/*     */   
/*     */   public static final String TAG_TRANSFORMATION_START_INTERPOLATION = "start_interpolation";
/*     */   
/*     */   public static final String TAG_TRANSFORMATION = "transformation";
/*     */   
/*     */   public static final String TAG_BILLBOARD = "billboard";
/*     */   
/*     */   public static final String TAG_BRIGHTNESS = "brightness";
/*     */   
/*     */   public static final String TAG_VIEW_RANGE = "view_range";
/*     */   
/*     */   public static final String TAG_SHADOW_RADIUS = "shadow_radius";
/*     */   
/*     */   public static final String TAG_SHADOW_STRENGTH = "shadow_strength";
/*     */   
/*     */   public static final String TAG_WIDTH = "width";
/*     */   
/*     */   public static final String TAG_HEIGHT = "height";
/*     */   
/*     */   public static final String TAG_GLOW_COLOR_OVERRIDE = "glow_color_override";
/*     */   
/*     */   public enum BillboardConstraints implements StringRepresentable {
/* 104 */     FIXED((byte)0, "fixed"),
/* 105 */     VERTICAL((byte)1, "vertical"),
/* 106 */     HORIZONTAL((byte)2, "horizontal"),
/* 107 */     CENTER((byte)3, "center");
/*     */     
/* 110 */     public static final Codec<BillboardConstraints> CODEC = (Codec<BillboardConstraints>)StringRepresentable.fromEnum(BillboardConstraints::values);
/*     */     
/* 111 */     public static final IntFunction<BillboardConstraints> BY_ID = ByIdMap.continuous(BillboardConstraints::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/*     */     private final byte id;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     BillboardConstraints(byte id, String name) {
/* 117 */       this.name = name;
/* 118 */       this.id = id;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 123 */       return this.name;
/*     */     }
/*     */     
/*     */     private byte getId() {
/* 127 */       return this.id;
/*     */     }
/*     */   }
/*     */   
/* 147 */   private long interpolationStartClientTick = -2147483648L;
/*     */   
/*     */   private int interpolationDuration;
/*     */   
/*     */   private float lastProgress;
/*     */   
/*     */   private AABB cullingBoundingBox;
/*     */   
/*     */   private boolean noCulling = true;
/*     */   
/*     */   protected boolean updateRenderState;
/*     */   
/*     */   private boolean updateStartTick;
/*     */   
/*     */   private boolean updateInterpolationDuration;
/*     */   
/*     */   private RenderState renderState;
/*     */   
/* 159 */   private final InterpolationHandler interpolation = new InterpolationHandler(this, 0);
/*     */   
/*     */   public Display(EntityType<?> type, Level level) {
/* 162 */     super(type, level);
/* 163 */     this.noPhysics = true;
/* 164 */     this.cullingBoundingBox = getBoundingBox();
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 169 */     super.onSyncedDataUpdated(accessor);
/* 171 */     if (DATA_HEIGHT_ID.equals(accessor) || DATA_WIDTH_ID.equals(accessor))
/* 172 */       updateCulling(); 
/* 175 */     if (DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID.equals(accessor))
/* 176 */       this.updateStartTick = true; 
/* 179 */     if (DATA_POS_ROT_INTERPOLATION_DURATION_ID.equals(accessor))
/* 180 */       this.interpolation.setInterpolationLength(getPosRotInterpolationDuration()); 
/* 183 */     if (DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID.equals(accessor))
/* 184 */       this.updateInterpolationDuration = true; 
/* 187 */     if (RENDER_STATE_IDS.contains(accessor.id()))
/* 188 */       this.updateRenderState = true; 
/*     */   }
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 194 */     return false;
/*     */   }
/*     */   
/*     */   private static Transformation createTransformation(SynchedEntityData entityData) {
/* 198 */     Vector3fc translation = (Vector3fc)entityData.get(DATA_TRANSLATION_ID);
/* 199 */     Quaternionfc leftRotation = (Quaternionfc)entityData.get(DATA_LEFT_ROTATION_ID);
/* 200 */     Vector3fc scale = (Vector3fc)entityData.get(DATA_SCALE_ID);
/* 201 */     Quaternionfc rightRotation = (Quaternionfc)entityData.get(DATA_RIGHT_ROTATION_ID);
/* 202 */     return new Transformation(translation, leftRotation, scale, rightRotation);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 207 */     Entity vehicle = getVehicle();
/* 208 */     if (vehicle != null && vehicle.isRemoved())
/* 209 */       stopRiding(); 
/* 212 */     if (level().isClientSide()) {
/* 213 */       if (this.updateStartTick) {
/* 214 */         this.updateStartTick = false;
/* 216 */         int interpolationStartDelta = getTransformationInterpolationDelay();
/* 217 */         this.interpolationStartClientTick = (this.tickCount + interpolationStartDelta);
/*     */       } 
/* 220 */       if (this.updateInterpolationDuration) {
/* 221 */         this.updateInterpolationDuration = false;
/* 222 */         this.interpolationDuration = getTransformationInterpolationDuration();
/*     */       } 
/* 225 */       if (this.updateRenderState) {
/* 226 */         this.updateRenderState = false;
/* 228 */         boolean shouldInterpolate = (this.interpolationDuration != 0);
/* 229 */         if (shouldInterpolate && this.renderState != null) {
/* 230 */           this.renderState = createInterpolatedRenderState(this.renderState, this.lastProgress);
/*     */         } else {
/* 232 */           this.renderState = createFreshRenderState();
/*     */         } 
/* 234 */         updateRenderSubState(shouldInterpolate, this.lastProgress);
/*     */       } 
/* 237 */       this.interpolation.interpolate();
/*     */     } 
/*     */   }
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/* 243 */     return this.interpolation;
/*     */   }
/*     */   
/*     */   protected abstract void updateRenderSubState(boolean paramBoolean, float paramFloat);
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 250 */     entityData.define(DATA_POS_ROT_INTERPOLATION_DURATION_ID, Integer.valueOf(0));
/* 251 */     entityData.define(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID, Integer.valueOf(0));
/* 252 */     entityData.define(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID, Integer.valueOf(0));
/* 253 */     entityData.define(DATA_TRANSLATION_ID, new Vector3f());
/* 254 */     entityData.define(DATA_SCALE_ID, new Vector3f(1.0F, 1.0F, 1.0F));
/* 255 */     entityData.define(DATA_RIGHT_ROTATION_ID, new Quaternionf());
/* 256 */     entityData.define(DATA_LEFT_ROTATION_ID, new Quaternionf());
/* 257 */     entityData.define(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, Byte.valueOf(BillboardConstraints.FIXED.getId()));
/* 258 */     entityData.define(DATA_BRIGHTNESS_OVERRIDE_ID, Integer.valueOf(-1));
/* 259 */     entityData.define(DATA_VIEW_RANGE_ID, Float.valueOf(1.0F));
/* 260 */     entityData.define(DATA_SHADOW_RADIUS_ID, Float.valueOf(0.0F));
/* 261 */     entityData.define(DATA_SHADOW_STRENGTH_ID, Float.valueOf(1.0F));
/* 262 */     entityData.define(DATA_WIDTH_ID, Float.valueOf(0.0F));
/* 263 */     entityData.define(DATA_HEIGHT_ID, Float.valueOf(0.0F));
/* 264 */     entityData.define(DATA_GLOW_COLOR_OVERRIDE_ID, Integer.valueOf(-1));
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 269 */     setTransformation(input.read("transformation", Transformation.EXTENDED_CODEC).orElse(Transformation.IDENTITY));
/* 271 */     setTransformationInterpolationDuration(input.getIntOr("interpolation_duration", 0));
/* 272 */     setTransformationInterpolationDelay(input.getIntOr("start_interpolation", 0));
/* 274 */     int teleportDuration = input.getIntOr("teleport_duration", 0);
/* 276 */     setPosRotInterpolationDuration(Mth.clamp(teleportDuration, 0, 59));
/* 278 */     setBillboardConstraints(input.read("billboard", BillboardConstraints.CODEC).orElse(BillboardConstraints.FIXED));
/* 280 */     setViewRange(input.getFloatOr("view_range", 1.0F));
/* 281 */     setShadowRadius(input.getFloatOr("shadow_radius", 0.0F));
/* 282 */     setShadowStrength(input.getFloatOr("shadow_strength", 1.0F));
/* 283 */     setWidth(input.getFloatOr("width", 0.0F));
/* 284 */     setHeight(input.getFloatOr("height", 0.0F));
/* 286 */     setGlowColorOverride(input.getIntOr("glow_color_override", -1));
/* 288 */     setBrightnessOverride(input.read("brightness", Brightness.CODEC).orElse(null));
/*     */   }
/*     */   
/*     */   private void setTransformation(Transformation transformation) {
/* 292 */     this.entityData.set(DATA_TRANSLATION_ID, transformation.translation());
/* 293 */     this.entityData.set(DATA_LEFT_ROTATION_ID, transformation.leftRotation());
/* 294 */     this.entityData.set(DATA_SCALE_ID, transformation.scale());
/* 295 */     this.entityData.set(DATA_RIGHT_ROTATION_ID, transformation.rightRotation());
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 300 */     output.store("transformation", Transformation.EXTENDED_CODEC, createTransformation(this.entityData));
/* 301 */     output.store("billboard", BillboardConstraints.CODEC, getBillboardConstraints());
/* 302 */     output.putInt("interpolation_duration", getTransformationInterpolationDuration());
/* 303 */     output.putInt("teleport_duration", getPosRotInterpolationDuration());
/* 304 */     output.putFloat("view_range", getViewRange());
/* 305 */     output.putFloat("shadow_radius", getShadowRadius());
/* 306 */     output.putFloat("shadow_strength", getShadowStrength());
/* 307 */     output.putFloat("width", getWidth());
/* 308 */     output.putFloat("height", getHeight());
/* 309 */     output.putInt("glow_color_override", getGlowColorOverride());
/* 310 */     output.storeNullable("brightness", Brightness.CODEC, getBrightnessOverride());
/*     */   }
/*     */   
/*     */   public AABB getBoundingBoxForCulling() {
/* 314 */     return this.cullingBoundingBox;
/*     */   }
/*     */   
/*     */   public boolean affectedByCulling() {
/* 318 */     return !this.noCulling;
/*     */   }
/*     */   
/*     */   public PushReaction getPistonPushReaction() {
/* 323 */     return PushReaction.IGNORE;
/*     */   }
/*     */   
/*     */   public boolean isIgnoringBlockTriggers() {
/* 328 */     return true;
/*     */   }
/*     */   
/*     */   public RenderState renderState() {
/* 332 */     return this.renderState;
/*     */   }
/*     */   
/*     */   private void setTransformationInterpolationDuration(int duration) {
/* 336 */     this.entityData.set(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID, Integer.valueOf(duration));
/*     */   }
/*     */   
/*     */   private int getTransformationInterpolationDuration() {
/* 340 */     return ((Integer)this.entityData.get(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID)).intValue();
/*     */   }
/*     */   
/*     */   private void setTransformationInterpolationDelay(int ticks) {
/* 344 */     this.entityData.set(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID, Integer.valueOf(ticks), true);
/*     */   }
/*     */   
/*     */   private int getTransformationInterpolationDelay() {
/* 348 */     return ((Integer)this.entityData.get(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID)).intValue();
/*     */   }
/*     */   
/*     */   private void setPosRotInterpolationDuration(int duration) {
/* 352 */     this.entityData.set(DATA_POS_ROT_INTERPOLATION_DURATION_ID, Integer.valueOf(duration));
/*     */   }
/*     */   
/*     */   private int getPosRotInterpolationDuration() {
/* 356 */     return ((Integer)this.entityData.get(DATA_POS_ROT_INTERPOLATION_DURATION_ID)).intValue();
/*     */   }
/*     */   
/*     */   private void setBillboardConstraints(BillboardConstraints constraints) {
/* 360 */     this.entityData.set(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, Byte.valueOf(constraints.getId()));
/*     */   }
/*     */   
/*     */   private BillboardConstraints getBillboardConstraints() {
/* 364 */     return BillboardConstraints.BY_ID.apply(((Byte)this.entityData.get(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID)).byteValue());
/*     */   }
/*     */   
/*     */   private void setBrightnessOverride(Brightness brightness) {
/* 368 */     this.entityData.set(DATA_BRIGHTNESS_OVERRIDE_ID, Integer.valueOf((brightness != null) ? brightness.pack() : -1));
/*     */   }
/*     */   
/*     */   private Brightness getBrightnessOverride() {
/* 372 */     int value = ((Integer)this.entityData.get(DATA_BRIGHTNESS_OVERRIDE_ID)).intValue();
/* 373 */     return (value != -1) ? Brightness.unpack(value) : null;
/*     */   }
/*     */   
/*     */   private int getPackedBrightnessOverride() {
/* 377 */     return ((Integer)this.entityData.get(DATA_BRIGHTNESS_OVERRIDE_ID)).intValue();
/*     */   }
/*     */   
/*     */   private void setViewRange(float range) {
/* 381 */     this.entityData.set(DATA_VIEW_RANGE_ID, Float.valueOf(range));
/*     */   }
/*     */   
/*     */   private float getViewRange() {
/* 385 */     return ((Float)this.entityData.get(DATA_VIEW_RANGE_ID)).floatValue();
/*     */   }
/*     */   
/*     */   private void setShadowRadius(float size) {
/* 389 */     this.entityData.set(DATA_SHADOW_RADIUS_ID, Float.valueOf(size));
/*     */   }
/*     */   
/*     */   private float getShadowRadius() {
/* 393 */     return ((Float)this.entityData.get(DATA_SHADOW_RADIUS_ID)).floatValue();
/*     */   }
/*     */   
/*     */   private void setShadowStrength(float strength) {
/* 397 */     this.entityData.set(DATA_SHADOW_STRENGTH_ID, Float.valueOf(strength));
/*     */   }
/*     */   
/*     */   private float getShadowStrength() {
/* 401 */     return ((Float)this.entityData.get(DATA_SHADOW_STRENGTH_ID)).floatValue();
/*     */   }
/*     */   
/*     */   private void setWidth(float width) {
/* 405 */     this.entityData.set(DATA_WIDTH_ID, Float.valueOf(width));
/*     */   }
/*     */   
/*     */   private float getWidth() {
/* 409 */     return ((Float)this.entityData.get(DATA_WIDTH_ID)).floatValue();
/*     */   }
/*     */   
/*     */   private void setHeight(float width) {
/* 413 */     this.entityData.set(DATA_HEIGHT_ID, Float.valueOf(width));
/*     */   }
/*     */   
/*     */   private int getGlowColorOverride() {
/* 417 */     return ((Integer)this.entityData.get(DATA_GLOW_COLOR_OVERRIDE_ID)).intValue();
/*     */   }
/*     */   
/*     */   private void setGlowColorOverride(int value) {
/* 421 */     this.entityData.set(DATA_GLOW_COLOR_OVERRIDE_ID, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public float calculateInterpolationProgress(float partialTickTime) {
/* 425 */     int duration = this.interpolationDuration;
/* 426 */     if (duration <= 0)
/* 427 */       return 1.0F; 
/* 430 */     float ticksSinceUpdate = (float)(this.tickCount - this.interpolationStartClientTick);
/* 431 */     float partialTicksSinceLastUpdate = ticksSinceUpdate + partialTickTime;
/* 432 */     float result = Mth.clamp(Mth.inverseLerp(partialTicksSinceLastUpdate, 0.0F, duration), 0.0F, 1.0F);
/* 433 */     this.lastProgress = result;
/* 434 */     return result;
/*     */   }
/*     */   
/*     */   private float getHeight() {
/* 438 */     return ((Float)this.entityData.get(DATA_HEIGHT_ID)).floatValue();
/*     */   }
/*     */   
/*     */   public void setPos(double x, double y, double z) {
/* 443 */     super.setPos(x, y, z);
/* 444 */     updateCulling();
/*     */   }
/*     */   
/*     */   private void updateCulling() {
/* 448 */     float width = getWidth();
/* 449 */     float height = getHeight();
/* 451 */     this.noCulling = (width == 0.0F || height == 0.0F);
/* 453 */     float w = width / 2.0F;
/* 454 */     double x = getX();
/* 455 */     double y = getY();
/* 456 */     double z = getZ();
/* 457 */     this.cullingBoundingBox = new AABB(x - w, y, z - w, x + w, y + height, z + w);
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distanceSqr) {
/* 462 */     return (distanceSqr < Mth.square(getViewRange() * 64.0D * getViewScale()));
/*     */   }
/*     */   
/*     */   public int getTeamColor() {
/* 467 */     int glowColorOverride = getGlowColorOverride();
/* 468 */     return (glowColorOverride != -1) ? glowColorOverride : super.getTeamColor();
/*     */   }
/*     */   
/*     */   private RenderState createFreshRenderState() {
/* 472 */     return new RenderState(
/* 473 */         GenericInterpolator.constant(createTransformation(this.entityData)), 
/* 474 */         getBillboardConstraints(), 
/* 475 */         getPackedBrightnessOverride(), 
/* 476 */         FloatInterpolator.constant(getShadowRadius()), 
/* 477 */         FloatInterpolator.constant(getShadowStrength()), 
/* 478 */         getGlowColorOverride());
/*     */   }
/*     */   
/*     */   private RenderState createInterpolatedRenderState(RenderState previousState, float progress) {
/* 483 */     Transformation currentTransform = previousState.transformation.get(progress);
/* 484 */     float currentShadowRadius = previousState.shadowRadius.get(progress);
/* 485 */     float currentShadowStrength = previousState.shadowStrength.get(progress);
/* 487 */     return new RenderState(new TransformationInterpolator(currentTransform, 
/* 488 */           createTransformation(this.entityData)), 
/* 489 */         getBillboardConstraints(), 
/* 490 */         getPackedBrightnessOverride(), new LinearFloatInterpolator(currentShadowRadius, 
/* 491 */           getShadowRadius()), new LinearFloatInterpolator(currentShadowStrength, 
/* 492 */           getShadowStrength()), 
/* 493 */         getGlowColorOverride());
/*     */   }
/*     */   
/*     */   public static final class RenderState extends Record {
/*     */     private final Display.GenericInterpolator<Transformation> transformation;
/*     */     
/*     */     private final Display.BillboardConstraints billboardConstraints;
/*     */     
/*     */     private final int brightnessOverride;
/*     */     
/*     */     private final Display.FloatInterpolator shadowRadius;
/*     */     
/*     */     private final Display.FloatInterpolator shadowStrength;
/*     */     
/*     */     private final int glowColorOverride;
/*     */     
/*     */     public RenderState(Display.GenericInterpolator<Transformation> transformation, Display.BillboardConstraints billboardConstraints, int brightnessOverride, Display.FloatInterpolator shadowRadius, Display.FloatInterpolator shadowStrength, int glowColorOverride) {
/* 497 */       this.transformation = transformation;
/* 497 */       this.billboardConstraints = billboardConstraints;
/* 497 */       this.brightnessOverride = brightnessOverride;
/* 497 */       this.shadowRadius = shadowRadius;
/* 497 */       this.shadowStrength = shadowStrength;
/* 497 */       this.glowColorOverride = glowColorOverride;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$RenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #497	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$RenderState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$RenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #497	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$RenderState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$RenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #497	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$RenderState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Display.GenericInterpolator<Transformation> transformation() {
/* 497 */       return this.transformation;
/*     */     }
/*     */     
/*     */     public Display.BillboardConstraints billboardConstraints() {
/* 497 */       return this.billboardConstraints;
/*     */     }
/*     */     
/*     */     public int brightnessOverride() {
/* 497 */       return this.brightnessOverride;
/*     */     }
/*     */     
/*     */     public Display.FloatInterpolator shadowRadius() {
/* 497 */       return this.shadowRadius;
/*     */     }
/*     */     
/*     */     public Display.FloatInterpolator shadowStrength() {
/* 497 */       return this.shadowStrength;
/*     */     }
/*     */     
/*     */     public int glowColorOverride() {
/* 497 */       return this.glowColorOverride;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ItemDisplay extends Display {
/*     */     private static final String TAG_ITEM = "item";
/*     */     
/*     */     private static final String TAG_ITEM_DISPLAY = "item_display";
/*     */     
/* 511 */     private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK_ID = SynchedEntityData.defineId(ItemDisplay.class, EntityDataSerializers.ITEM_STACK);
/*     */     
/* 513 */     private static final EntityDataAccessor<Byte> DATA_ITEM_DISPLAY_ID = SynchedEntityData.defineId(ItemDisplay.class, EntityDataSerializers.BYTE);
/*     */     
/* 515 */     private final SlotAccess slot = SlotAccess.of(this::getItemStack, this::setItemStack);
/*     */     
/*     */     private ItemRenderState itemRenderState;
/*     */     
/*     */     public ItemDisplay(EntityType<?> type, Level level) {
/* 520 */       super(type, level);
/*     */     }
/*     */     
/*     */     protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 525 */       super.defineSynchedData(entityData);
/* 526 */       entityData.define(DATA_ITEM_STACK_ID, ItemStack.EMPTY);
/* 527 */       entityData.define(DATA_ITEM_DISPLAY_ID, Byte.valueOf(ItemDisplayContext.NONE.getId()));
/*     */     }
/*     */     
/*     */     public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 532 */       super.onSyncedDataUpdated(accessor);
/* 534 */       if (DATA_ITEM_STACK_ID.equals(accessor) || DATA_ITEM_DISPLAY_ID.equals(accessor))
/* 535 */         this.updateRenderState = true; 
/*     */     }
/*     */     
/*     */     private ItemStack getItemStack() {
/* 540 */       return (ItemStack)this.entityData.get(DATA_ITEM_STACK_ID);
/*     */     }
/*     */     
/*     */     private void setItemStack(ItemStack item) {
/* 544 */       this.entityData.set(DATA_ITEM_STACK_ID, item);
/*     */     }
/*     */     
/*     */     private void setItemTransform(ItemDisplayContext transform) {
/* 548 */       this.entityData.set(DATA_ITEM_DISPLAY_ID, Byte.valueOf(transform.getId()));
/*     */     }
/*     */     
/*     */     private ItemDisplayContext getItemTransform() {
/* 552 */       return ItemDisplayContext.BY_ID.apply(((Byte)this.entityData.get(DATA_ITEM_DISPLAY_ID)).byteValue());
/*     */     }
/*     */     
/*     */     protected void readAdditionalSaveData(ValueInput input) {
/* 557 */       super.readAdditionalSaveData(input);
/* 558 */       setItemStack(input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
/* 559 */       setItemTransform(input.read("item_display", ItemDisplayContext.CODEC).orElse(ItemDisplayContext.NONE));
/*     */     }
/*     */     
/*     */     protected void addAdditionalSaveData(ValueOutput output) {
/* 564 */       super.addAdditionalSaveData(output);
/* 565 */       ItemStack itemStack = getItemStack();
/* 566 */       if (!itemStack.isEmpty())
/* 567 */         output.store("item", ItemStack.CODEC, itemStack); 
/* 569 */       output.store("item_display", ItemDisplayContext.CODEC, getItemTransform());
/*     */     }
/*     */     
/*     */     public SlotAccess getSlot(int slot) {
/* 574 */       if (slot == 0)
/* 575 */         return this.slot; 
/* 577 */       return null;
/*     */     }
/*     */     
/*     */     public ItemRenderState itemRenderState() {
/* 581 */       return this.itemRenderState;
/*     */     }
/*     */     
/*     */     protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 586 */       this.itemRenderState = new ItemRenderState(getItemStack(), getItemTransform());
/*     */     }
/*     */     
/*     */     public static final class ItemRenderState extends Record {
/*     */       private final ItemStack itemStack;
/*     */       
/*     */       private final ItemDisplayContext itemTransform;
/*     */       
/*     */       public ItemRenderState(ItemStack itemStack, ItemDisplayContext itemTransform) {
/* 589 */         this.itemStack = itemStack;
/* 589 */         this.itemTransform = itemTransform;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #589	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #589	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #589	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public ItemStack itemStack() {
/* 589 */         return this.itemStack;
/*     */       }
/*     */       
/*     */       public ItemDisplayContext itemTransform() {
/* 589 */         return this.itemTransform;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ItemRenderState extends Record {
/*     */     private final ItemStack itemStack;
/*     */     
/*     */     private final ItemDisplayContext itemTransform;
/*     */     
/*     */     public ItemRenderState(ItemStack itemStack, ItemDisplayContext itemTransform) {
/* 589 */       this.itemStack = itemStack;
/* 589 */       this.itemTransform = itemTransform;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #589	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #589	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #589	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ItemStack itemStack() {
/* 589 */       return this.itemStack;
/*     */     }
/*     */     
/*     */     public ItemDisplayContext itemTransform() {
/* 589 */       return this.itemTransform;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlockDisplay extends Display {
/*     */     public static final String TAG_BLOCK_STATE = "block_state";
/*     */     
/* 599 */     private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(BlockDisplay.class, EntityDataSerializers.BLOCK_STATE);
/*     */     
/*     */     private BlockRenderState blockRenderState;
/*     */     
/*     */     public BlockDisplay(EntityType<?> type, Level level) {
/* 604 */       super(type, level);
/*     */     }
/*     */     
/*     */     protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 609 */       super.defineSynchedData(entityData);
/* 610 */       entityData.define(DATA_BLOCK_STATE_ID, Blocks.AIR.defaultBlockState());
/*     */     }
/*     */     
/*     */     public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 615 */       super.onSyncedDataUpdated(accessor);
/* 617 */       if (accessor.equals(DATA_BLOCK_STATE_ID))
/* 618 */         this.updateRenderState = true; 
/*     */     }
/*     */     
/*     */     private BlockState getBlockState() {
/* 623 */       return (BlockState)this.entityData.get(DATA_BLOCK_STATE_ID);
/*     */     }
/*     */     
/*     */     private void setBlockState(BlockState blockState) {
/* 627 */       this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
/*     */     }
/*     */     
/*     */     protected void readAdditionalSaveData(ValueInput input) {
/* 632 */       super.readAdditionalSaveData(input);
/* 633 */       setBlockState(input.read("block_state", BlockState.CODEC).orElse(Blocks.AIR.defaultBlockState()));
/*     */     }
/*     */     
/*     */     protected void addAdditionalSaveData(ValueOutput output) {
/* 638 */       super.addAdditionalSaveData(output);
/* 639 */       output.store("block_state", BlockState.CODEC, getBlockState());
/*     */     }
/*     */     
/*     */     public BlockRenderState blockRenderState() {
/* 643 */       return this.blockRenderState;
/*     */     }
/*     */     
/*     */     protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 648 */       this.blockRenderState = new BlockRenderState(getBlockState());
/*     */     }
/*     */     
/*     */     public static final class BlockRenderState extends Record {
/*     */       private final BlockState blockState;
/*     */       
/*     */       public BlockRenderState(BlockState blockState) {
/* 651 */         this.blockState = blockState;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #651	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #651	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #651	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public BlockState blockState() {
/* 651 */         return this.blockState;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class BlockRenderState extends Record {
/*     */     private final BlockState blockState;
/*     */     
/*     */     public BlockRenderState(BlockState blockState) {
/* 651 */       this.blockState = blockState;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #651	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #651	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #651	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockState blockState() {
/* 651 */       return this.blockState;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TextDisplay extends Display {
/*     */     public static final String TAG_TEXT = "text";
/*     */     
/*     */     private static final String TAG_LINE_WIDTH = "line_width";
/*     */     
/*     */     private static final String TAG_TEXT_OPACITY = "text_opacity";
/*     */     
/*     */     private static final String TAG_BACKGROUND_COLOR = "background";
/*     */     
/*     */     private static final String TAG_SHADOW = "shadow";
/*     */     
/*     */     private static final String TAG_SEE_THROUGH = "see_through";
/*     */     
/*     */     private static final String TAG_USE_DEFAULT_BACKGROUND = "default_background";
/*     */     
/*     */     private static final String TAG_ALIGNMENT = "alignment";
/*     */     
/*     */     public static final byte FLAG_SHADOW = 1;
/*     */     
/*     */     public static final byte FLAG_SEE_THROUGH = 2;
/*     */     
/*     */     public static final byte FLAG_USE_DEFAULT_BACKGROUND = 4;
/*     */     
/*     */     public static final byte FLAG_ALIGN_LEFT = 8;
/*     */     
/*     */     public static final byte FLAG_ALIGN_RIGHT = 16;
/*     */     
/*     */     private static final byte INITIAL_TEXT_OPACITY = -1;
/*     */     
/*     */     public static final int INITIAL_BACKGROUND = 1073741824;
/*     */     
/*     */     private static final int INITIAL_LINE_WIDTH = 200;
/*     */     
/*     */     public enum Align implements StringRepresentable {
/* 659 */       CENTER("center"),
/* 660 */       LEFT("left"),
/* 661 */       RIGHT("right");
/*     */       
/* 663 */       public static final Codec<Align> CODEC = (Codec<Align>)StringRepresentable.fromEnum(Align::values);
/*     */       
/*     */       private final String name;
/*     */       
/*     */       static {
/*     */       
/*     */       }
/*     */       
/*     */       Align(String name) {
/* 668 */         this.name = name;
/*     */       }
/*     */       
/*     */       public String getSerializedName() {
/* 673 */         return this.name;
/*     */       }
/*     */     }
/*     */     
/* 696 */     private static final EntityDataAccessor<Component> DATA_TEXT_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.COMPONENT);
/*     */     
/* 697 */     private static final EntityDataAccessor<Integer> DATA_LINE_WIDTH_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.INT);
/*     */     
/* 698 */     private static final EntityDataAccessor<Integer> DATA_BACKGROUND_COLOR_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.INT);
/*     */     
/* 699 */     private static final EntityDataAccessor<Byte> DATA_TEXT_OPACITY_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.BYTE);
/*     */     
/* 700 */     private static final EntityDataAccessor<Byte> DATA_STYLE_FLAGS_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.BYTE);
/*     */     
/* 702 */     private static final IntSet TEXT_RENDER_STATE_IDS = IntSet.of(new int[] { DATA_TEXT_ID
/* 703 */           .id(), DATA_LINE_WIDTH_ID
/* 704 */           .id(), DATA_BACKGROUND_COLOR_ID
/* 705 */           .id(), DATA_TEXT_OPACITY_ID
/* 706 */           .id(), DATA_STYLE_FLAGS_ID
/* 707 */           .id() });
/*     */     
/*     */     private CachedInfo clientDisplayCache;
/*     */     
/*     */     private TextRenderState textRenderState;
/*     */     
/*     */     public TextDisplay(EntityType<?> type, Level level) {
/* 715 */       super(type, level);
/*     */     }
/*     */     
/*     */     protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 720 */       super.defineSynchedData(entityData);
/* 721 */       entityData.define(DATA_TEXT_ID, Component.empty());
/* 722 */       entityData.define(DATA_LINE_WIDTH_ID, Integer.valueOf(200));
/* 723 */       entityData.define(DATA_BACKGROUND_COLOR_ID, Integer.valueOf(1073741824));
/* 724 */       entityData.define(DATA_TEXT_OPACITY_ID, Byte.valueOf((byte)-1));
/* 725 */       entityData.define(DATA_STYLE_FLAGS_ID, Byte.valueOf((byte)0));
/*     */     }
/*     */     
/*     */     public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 730 */       super.onSyncedDataUpdated(accessor);
/* 732 */       if (TEXT_RENDER_STATE_IDS.contains(accessor.id()))
/* 733 */         this.updateRenderState = true; 
/*     */     }
/*     */     
/*     */     private Component getText() {
/* 738 */       return (Component)this.entityData.get(DATA_TEXT_ID);
/*     */     }
/*     */     
/*     */     private void setText(Component text) {
/* 742 */       this.entityData.set(DATA_TEXT_ID, text);
/*     */     }
/*     */     
/*     */     private int getLineWidth() {
/* 746 */       return ((Integer)this.entityData.get(DATA_LINE_WIDTH_ID)).intValue();
/*     */     }
/*     */     
/*     */     private void setLineWidth(int width) {
/* 750 */       this.entityData.set(DATA_LINE_WIDTH_ID, Integer.valueOf(width));
/*     */     }
/*     */     
/*     */     private byte getTextOpacity() {
/* 754 */       return ((Byte)this.entityData.get(DATA_TEXT_OPACITY_ID)).byteValue();
/*     */     }
/*     */     
/*     */     private void setTextOpacity(byte opacity) {
/* 758 */       this.entityData.set(DATA_TEXT_OPACITY_ID, Byte.valueOf(opacity));
/*     */     }
/*     */     
/*     */     private int getBackgroundColor() {
/* 762 */       return ((Integer)this.entityData.get(DATA_BACKGROUND_COLOR_ID)).intValue();
/*     */     }
/*     */     
/*     */     private void setBackgroundColor(int color) {
/* 766 */       this.entityData.set(DATA_BACKGROUND_COLOR_ID, Integer.valueOf(color));
/*     */     }
/*     */     
/*     */     private byte getFlags() {
/* 770 */       return ((Byte)this.entityData.get(DATA_STYLE_FLAGS_ID)).byteValue();
/*     */     }
/*     */     
/*     */     private void setFlags(byte flags) {
/* 774 */       this.entityData.set(DATA_STYLE_FLAGS_ID, Byte.valueOf(flags));
/*     */     }
/*     */     
/*     */     private static byte loadFlag(byte flags, ValueInput input, String id, byte mask) {
/* 778 */       if (input.getBooleanOr(id, false))
/* 779 */         return (byte)(flags | mask); 
/* 781 */       return flags;
/*     */     }
/*     */     
/*     */     protected void readAdditionalSaveData(ValueInput input) {
/* 786 */       super.readAdditionalSaveData(input);
/* 788 */       setLineWidth(input.getIntOr("line_width", 200));
/* 789 */       setTextOpacity(input.getByteOr("text_opacity", (byte)-1));
/* 790 */       setBackgroundColor(input.getIntOr("background", 1073741824));
/* 793 */       byte flags = loadFlag((byte)0, input, "shadow", (byte)1);
/* 794 */       flags = loadFlag(flags, input, "see_through", (byte)2);
/* 795 */       flags = loadFlag(flags, input, "default_background", (byte)4);
/* 797 */       Optional<Align> alignment = input.read("alignment", Align.CODEC);
/* 798 */       if (alignment.isPresent()) {
/* 799 */         switch (((Align)alignment.get()).ordinal()) {
/*     */           default:
/* 799 */             throw new MatchException(null, null);
/*     */           case 0:
/*     */           
/*     */           case 1:
/*     */           
/*     */           case 2:
/*     */             break;
/*     */         } 
/* 799 */         flags = 
/*     */           
/* 802 */           (byte)(flags | 0x10);
/*     */       } 
/* 806 */       setFlags(flags);
/* 808 */       Optional<Component> text = input.read("text", ComponentSerialization.CODEC);
/* 809 */       if (text.isPresent())
/*     */         try {
/* 811 */           Level level = level();
/* 811 */           if (level instanceof ServerLevel) {
/* 811 */             ServerLevel serverLevel = (ServerLevel)level;
/* 812 */             CommandSourceStack context = createCommandSourceStackForNameResolution(serverLevel).withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/* 813 */             MutableComponent mutableComponent = ComponentUtils.resolve(ResolutionContext.create(context), text.get());
/* 814 */             setText((Component)mutableComponent);
/*     */           } else {
/* 816 */             setText((Component)Component.empty());
/*     */           } 
/* 818 */         } catch (Exception e) {
/* 819 */           Display.LOGGER.warn("Failed to parse display entity text {}", text, e);
/*     */         }  
/*     */     }
/*     */     
/*     */     private static void storeFlag(byte flags, ValueOutput output, String id, byte mask) {
/* 825 */       output.putBoolean(id, ((flags & mask) != 0));
/*     */     }
/*     */     
/*     */     protected void addAdditionalSaveData(ValueOutput output) {
/* 830 */       super.addAdditionalSaveData(output);
/* 831 */       output.store("text", ComponentSerialization.CODEC, getText());
/* 832 */       output.putInt("line_width", getLineWidth());
/* 833 */       output.putInt("background", getBackgroundColor());
/* 834 */       output.putByte("text_opacity", getTextOpacity());
/* 836 */       byte flags = getFlags();
/* 837 */       storeFlag(flags, output, "shadow", (byte)1);
/* 838 */       storeFlag(flags, output, "see_through", (byte)2);
/* 839 */       storeFlag(flags, output, "default_background", (byte)4);
/* 840 */       output.store("alignment", Align.CODEC, getAlign(flags));
/*     */     }
/*     */     
/*     */     protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 845 */       if (shouldInterpolate && this.textRenderState != null) {
/* 846 */         this.textRenderState = createInterpolatedTextRenderState(this.textRenderState, progress);
/*     */       } else {
/* 848 */         this.textRenderState = createFreshTextRenderState();
/*     */       } 
/* 850 */       this.clientDisplayCache = null;
/*     */     }
/*     */     
/*     */     public TextRenderState textRenderState() {
/* 854 */       return this.textRenderState;
/*     */     }
/*     */     
/*     */     private TextRenderState createFreshTextRenderState() {
/* 858 */       return new TextRenderState(
/* 859 */           getText(), 
/* 860 */           getLineWidth(), 
/* 861 */           Display.IntInterpolator.constant(getTextOpacity()), 
/* 862 */           Display.IntInterpolator.constant(getBackgroundColor()), 
/* 863 */           getFlags());
/*     */     }
/*     */     
/*     */     private TextRenderState createInterpolatedTextRenderState(TextRenderState previous, float progress) {
/* 868 */       int currentBackground = previous.backgroundColor.get(progress);
/* 869 */       int currentOpacity = previous.textOpacity.get(progress);
/* 871 */       return new TextRenderState(
/* 872 */           getText(), 
/* 873 */           getLineWidth(), new Display.LinearIntInterpolator(currentOpacity, 
/* 874 */             getTextOpacity()), new Display.ColorInterpolator(currentBackground, 
/* 875 */             getBackgroundColor()), 
/* 876 */           getFlags());
/*     */     }
/*     */     
/*     */     public static final class CachedLine extends Record {
/*     */       private final FormattedCharSequence contents;
/*     */       
/*     */       private final int width;
/*     */       
/*     */       public CachedLine(FormattedCharSequence contents, int width) {
/* 880 */         this.contents = contents;
/* 880 */         this.width = width;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #880	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #880	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #880	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public FormattedCharSequence contents() {
/* 880 */         return this.contents;
/*     */       }
/*     */       
/*     */       public int width() {
/* 880 */         return this.width;
/*     */       }
/*     */     }
/*     */     
/*     */     public static final class CachedInfo extends Record {
/*     */       private final List<Display.TextDisplay.CachedLine> lines;
/*     */       
/*     */       private final int width;
/*     */       
/*     */       public CachedInfo(List<Display.TextDisplay.CachedLine> lines, int width) {
/* 882 */         this.lines = lines;
/* 882 */         this.width = width;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #882	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #882	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #882	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public List<Display.TextDisplay.CachedLine> lines() {
/* 882 */         return this.lines;
/*     */       }
/*     */       
/*     */       public int width() {
/* 882 */         return this.width;
/*     */       }
/*     */     }
/*     */     
/*     */     public CachedInfo cacheDisplay(LineSplitter splitter) {
/* 890 */       if (this.clientDisplayCache == null)
/* 891 */         if (this.textRenderState != null) {
/* 892 */           this.clientDisplayCache = splitter.split(this.textRenderState.text(), this.textRenderState.lineWidth());
/*     */         } else {
/* 894 */           this.clientDisplayCache = new CachedInfo(List.of(), 0);
/*     */         }  
/* 898 */       return this.clientDisplayCache;
/*     */     }
/*     */     
/*     */     public static Align getAlign(byte flags) {
/* 902 */       if ((flags & 0x8) != 0)
/* 903 */         return Align.LEFT; 
/* 905 */       if ((flags & 0x10) != 0)
/* 906 */         return Align.RIGHT; 
/* 908 */       return Align.CENTER;
/*     */     }
/*     */     
/*     */     public static final class TextRenderState extends Record {
/*     */       private final Component text;
/*     */       
/*     */       private final int lineWidth;
/*     */       
/*     */       private final Display.IntInterpolator textOpacity;
/*     */       
/*     */       private final Display.IntInterpolator backgroundColor;
/*     */       
/*     */       private final byte flags;
/*     */       
/*     */       public TextRenderState(Component text, int lineWidth, Display.IntInterpolator textOpacity, Display.IntInterpolator backgroundColor, byte flags) {
/* 911 */         this.text = text;
/* 911 */         this.lineWidth = lineWidth;
/* 911 */         this.textOpacity = textOpacity;
/* 911 */         this.backgroundColor = backgroundColor;
/* 911 */         this.flags = flags;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #911	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #911	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #911	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public Component text() {
/* 911 */         return this.text;
/*     */       }
/*     */       
/*     */       public int lineWidth() {
/* 911 */         return this.lineWidth;
/*     */       }
/*     */       
/*     */       public Display.IntInterpolator textOpacity() {
/* 911 */         return this.textOpacity;
/*     */       }
/*     */       
/*     */       public Display.IntInterpolator backgroundColor() {
/* 911 */         return this.backgroundColor;
/*     */       }
/*     */       
/*     */       public byte flags() {
/* 911 */         return this.flags;
/*     */       }
/*     */     }
/*     */     
/*     */     @FunctionalInterface
/*     */     public static interface LineSplitter {
/*     */       Display.TextDisplay.CachedInfo split(Component param2Component, int param2Int);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Align implements StringRepresentable {
/*     */     CENTER("center"),
/*     */     LEFT("left"),
/*     */     RIGHT("right");
/*     */     
/*     */     public static final Codec<Align> CODEC = (Codec<Align>)StringRepresentable.fromEnum(Align::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Align(String name) {
/*     */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*     */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class CachedLine extends Record {
/*     */     private final FormattedCharSequence contents;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     public CachedLine(FormattedCharSequence contents, int width) {
/*     */       this.contents = contents;
/*     */       this.width = width;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #880	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #880	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #880	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public FormattedCharSequence contents() {
/*     */       return this.contents;
/*     */     }
/*     */     
/*     */     public int width() {
/*     */       return this.width;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class CachedInfo extends Record {
/*     */     private final List<Display.TextDisplay.CachedLine> lines;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     public CachedInfo(List<Display.TextDisplay.CachedLine> lines, int width) {
/*     */       this.lines = lines;
/*     */       this.width = width;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #882	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #882	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #882	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<Display.TextDisplay.CachedLine> lines() {
/*     */       return this.lines;
/*     */     }
/*     */     
/*     */     public int width() {
/*     */       return this.width;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class TextRenderState extends Record {
/*     */     private final Component text;
/*     */     
/*     */     private final int lineWidth;
/*     */     
/*     */     private final Display.IntInterpolator textOpacity;
/*     */     
/*     */     private final Display.IntInterpolator backgroundColor;
/*     */     
/*     */     private final byte flags;
/*     */     
/*     */     public TextRenderState(Component text, int lineWidth, Display.IntInterpolator textOpacity, Display.IntInterpolator backgroundColor, byte flags) {
/* 911 */       this.text = text;
/* 911 */       this.lineWidth = lineWidth;
/* 911 */       this.textOpacity = textOpacity;
/* 911 */       this.backgroundColor = backgroundColor;
/* 911 */       this.flags = flags;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #911	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #911	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #911	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Component text() {
/* 911 */       return this.text;
/*     */     }
/*     */     
/*     */     public int lineWidth() {
/* 911 */       return this.lineWidth;
/*     */     }
/*     */     
/*     */     public Display.IntInterpolator textOpacity() {
/* 911 */       return this.textOpacity;
/*     */     }
/*     */     
/*     */     public Display.IntInterpolator backgroundColor() {
/* 911 */       return this.backgroundColor;
/*     */     }
/*     */     
/*     */     public byte flags() {
/* 911 */       return this.flags;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface GenericInterpolator<T> {
/*     */     static <T> GenericInterpolator<T> constant(T value) {
/* 925 */       return progress -> value;
/*     */     }
/*     */     
/*     */     T get(float param1Float);
/*     */   }
/*     */   
/*     */   private static final class TransformationInterpolator extends Record implements GenericInterpolator<Transformation> {
/*     */     private final Transformation previous;
/*     */     
/*     */     private final Transformation current;
/*     */     
/*     */     private TransformationInterpolator(Transformation previous, Transformation current) {
/* 931 */       this.previous = previous;
/* 931 */       this.current = current;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #931	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #931	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #931	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Transformation previous() {
/* 931 */       return this.previous;
/*     */     }
/*     */     
/*     */     public Transformation current() {
/* 931 */       return this.current;
/*     */     }
/*     */     
/*     */     public Transformation get(float progress) {
/* 934 */       if (progress >= 1.0D)
/* 935 */         return this.current; 
/* 937 */       return this.previous.slerp(this.current, progress);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface IntInterpolator {
/*     */     static IntInterpolator constant(int value) {
/* 944 */       return progress -> value;
/*     */     }
/*     */     
/*     */     int get(float param1Float);
/*     */   }
/*     */   
/*     */   private static final class LinearIntInterpolator extends Record implements IntInterpolator {
/*     */     private final int previous;
/*     */     
/*     */     private final int current;
/*     */     
/*     */     private LinearIntInterpolator(int previous, int current) {
/* 950 */       this.previous = previous;
/* 950 */       this.current = current;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$LinearIntInterpolator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #950	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearIntInterpolator;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$LinearIntInterpolator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #950	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearIntInterpolator;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$LinearIntInterpolator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #950	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$LinearIntInterpolator;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int previous() {
/* 950 */       return this.previous;
/*     */     }
/*     */     
/*     */     public int current() {
/* 950 */       return this.current;
/*     */     }
/*     */     
/*     */     public int get(float progress) {
/* 953 */       return Mth.lerpInt(progress, this.previous, this.current);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ColorInterpolator extends Record implements IntInterpolator {
/*     */     private final int previous;
/*     */     
/*     */     private final int current;
/*     */     
/*     */     private ColorInterpolator(int previous, int current) {
/* 957 */       this.previous = previous;
/* 957 */       this.current = current;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ColorInterpolator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #957	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ColorInterpolator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #957	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ColorInterpolator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #957	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int previous() {
/* 957 */       return this.previous;
/*     */     }
/*     */     
/*     */     public int current() {
/* 957 */       return this.current;
/*     */     }
/*     */     
/*     */     public int get(float progress) {
/* 960 */       return ARGB.srgbLerp(progress, this.previous, this.current);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface FloatInterpolator {
/*     */     static FloatInterpolator constant(float value) {
/* 967 */       return progress -> value;
/*     */     }
/*     */     
/*     */     float get(float param1Float);
/*     */   }
/*     */   
/*     */   private static final class LinearFloatInterpolator extends Record implements FloatInterpolator {
/*     */     private final float previous;
/*     */     
/*     */     private final float current;
/*     */     
/*     */     private LinearFloatInterpolator(float previous, float current) {
/* 973 */       this.previous = previous;
/* 973 */       this.current = current;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #973	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #973	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #973	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float previous() {
/* 973 */       return this.previous;
/*     */     }
/*     */     
/*     */     public float current() {
/* 973 */       return this.current;
/*     */     }
/*     */     
/*     */     public float get(float progress) {
/* 976 */       return Mth.lerp(progress, this.previous, this.current);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LineSplitter {
/*     */     Display.TextDisplay.CachedInfo split(Component param1Component, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */