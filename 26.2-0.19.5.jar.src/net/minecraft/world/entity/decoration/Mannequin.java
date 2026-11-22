/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.entity.Avatar;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Mannequin extends Avatar {
/*  30 */   protected static final EntityDataAccessor<ResolvableProfile> DATA_PROFILE = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.RESOLVABLE_PROFILE);
/*     */   
/*  31 */   private static final EntityDataAccessor<Boolean> DATA_IMMOVABLE = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  32 */   private static final EntityDataAccessor<Optional<Component>> DATA_DESCRIPTION = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.OPTIONAL_COMPONENT);
/*     */   
/*     */   private static final byte ALL_LAYERS;
/*     */   
/*     */   static {
/*  34 */     ALL_LAYERS = (byte)Arrays.<PlayerModelPart>stream(PlayerModelPart.values()).mapToInt(PlayerModelPart::getMask).reduce(0, (a, b) -> a | b);
/*     */   }
/*     */   
/*  36 */   private static final Set<Pose> VALID_POSES = Set.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING, Pose.FALL_FLYING, Pose.SLEEPING);
/*     */   
/*     */   public static final Codec<Pose> POSE_CODEC;
/*     */   
/*     */   private static final Codec<Byte> LAYERS_CODEC;
/*     */   
/*     */   static {
/*  37 */     POSE_CODEC = Pose.CODEC.validate(pose -> VALID_POSES.contains(pose) ? DataResult.success(pose) : DataResult.error(()));
/*  39 */     LAYERS_CODEC = PlayerModelPart.CODEC.listOf().xmap(list -> Byte.valueOf((byte)list.stream().mapToInt(PlayerModelPart::getMask).reduce(ALL_LAYERS, ())), mask -> Arrays.<PlayerModelPart>stream(PlayerModelPart.values()).filter(()).toList());
/*     */   }
/*     */   
/*  45 */   public static final ResolvableProfile DEFAULT_PROFILE = (ResolvableProfile)ResolvableProfile.Static.EMPTY;
/*     */   
/*  47 */   private static final Component DEFAULT_DESCRIPTION = (Component)Component.translatable("entity.minecraft.mannequin.label");
/*     */   
/*  49 */   protected static EntityType.EntityFactory<Mannequin> constructor = Mannequin::new;
/*     */   
/*     */   private static final String PROFILE_FIELD = "profile";
/*     */   
/*     */   private static final String HIDDEN_LAYERS_FIELD = "hidden_layers";
/*     */   
/*     */   private static final String MAIN_HAND_FIELD = "main_hand";
/*     */   
/*     */   private static final String POSE_FIELD = "pose";
/*     */   
/*     */   private static final String IMMOVABLE_FIELD = "immovable";
/*     */   
/*     */   private static final String DESCRIPTION_FIELD = "description";
/*     */   
/*     */   private static final String HIDE_DESCRIPTION_FIELD = "hide_description";
/*     */   
/*  59 */   private Component description = DEFAULT_DESCRIPTION;
/*     */   
/*     */   private boolean hideDescription = false;
/*     */   
/*     */   public Mannequin(EntityType<Mannequin> type, Level level) {
/*  63 */     super(type, level);
/*  64 */     this.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf(ALL_LAYERS));
/*     */   }
/*     */   
/*     */   protected Mannequin(Level level) {
/*  68 */     this(EntityTypes.MANNEQUIN, level);
/*     */   }
/*     */   
/*     */   public static Mannequin create(EntityType<Mannequin> type, Level level) {
/*  72 */     return (Mannequin)constructor.create(type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  77 */     super.defineSynchedData(entityData);
/*  79 */     entityData.define(DATA_PROFILE, DEFAULT_PROFILE);
/*  80 */     entityData.define(DATA_IMMOVABLE, Boolean.valueOf(false));
/*  81 */     entityData.define(DATA_DESCRIPTION, Optional.of(DEFAULT_DESCRIPTION));
/*     */   }
/*     */   
/*     */   public ResolvableProfile getProfile() {
/*  86 */     return (ResolvableProfile)this.entityData.get(DATA_PROFILE);
/*     */   }
/*     */   
/*     */   private void setProfile(ResolvableProfile profile) {
/*  90 */     this.entityData.set(DATA_PROFILE, profile);
/*     */   }
/*     */   
/*     */   private boolean getImmovable() {
/*  94 */     return ((Boolean)this.entityData.get(DATA_IMMOVABLE)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setImmovable(boolean immovable) {
/*  98 */     this.entityData.set(DATA_IMMOVABLE, Boolean.valueOf(immovable));
/*     */   }
/*     */   
/*     */   protected Component getDescription() {
/* 102 */     return ((Optional<Component>)this.entityData.get(DATA_DESCRIPTION)).orElse(null);
/*     */   }
/*     */   
/*     */   private void setDescription(Component description) {
/* 106 */     this.description = description;
/* 107 */     updateDescription();
/*     */   }
/*     */   
/*     */   private void setHideDescription(boolean hideDescription) {
/* 111 */     this.hideDescription = hideDescription;
/* 112 */     updateDescription();
/*     */   }
/*     */   
/*     */   private void updateDescription() {
/* 116 */     this.entityData.set(DATA_DESCRIPTION, this.hideDescription ? Optional.empty() : Optional.<Component>of(this.description));
/*     */   }
/*     */   
/*     */   protected boolean isImmobile() {
/* 121 */     return (getImmovable() || super.isImmobile());
/*     */   }
/*     */   
/*     */   public boolean isEffectiveAi() {
/* 126 */     return (!getImmovable() && super.isEffectiveAi());
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 131 */     super.addAdditionalSaveData(output);
/* 133 */     output.store("profile", ResolvableProfile.CODEC, getProfile());
/* 134 */     output.store("hidden_layers", LAYERS_CODEC, this.entityData.get(DATA_PLAYER_MODE_CUSTOMISATION));
/* 135 */     output.store("main_hand", HumanoidArm.CODEC, getMainArm());
/* 136 */     output.store("pose", POSE_CODEC, getPose());
/* 137 */     output.putBoolean("immovable", getImmovable());
/* 138 */     Component description = getDescription();
/* 139 */     if (description != null) {
/* 140 */       if (!description.equals(DEFAULT_DESCRIPTION))
/* 141 */         output.store("description", ComponentSerialization.CODEC, description); 
/*     */     } else {
/* 144 */       output.putBoolean("hide_description", true);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 150 */     super.readAdditionalSaveData(input);
/* 152 */     input.read("profile", ResolvableProfile.CODEC).ifPresent(this::setProfile);
/* 153 */     this.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, input.read("hidden_layers", LAYERS_CODEC).orElse(Byte.valueOf(ALL_LAYERS)));
/* 154 */     setMainArm(input.read("main_hand", HumanoidArm.CODEC).orElse(DEFAULT_MAIN_HAND));
/* 155 */     setPose(input.read("pose", POSE_CODEC).orElse(Pose.STANDING));
/* 156 */     setImmovable(input.getBooleanOr("immovable", false));
/* 157 */     setHideDescription(input.getBooleanOr("hide_description", false));
/* 158 */     setDescription(input.read("description", ComponentSerialization.CODEC).orElse(DEFAULT_DESCRIPTION));
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 163 */     if (type == DataComponents.PROFILE)
/* 164 */       return (T)castComponentValue(type, getProfile()); 
/* 167 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 172 */     applyImplicitComponentIfPresent(components, DataComponents.PROFILE);
/* 173 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 178 */     if (type == DataComponents.PROFILE) {
/* 179 */       setProfile((ResolvableProfile)castComponentValue(DataComponents.PROFILE, value));
/* 180 */       return true;
/*     */     } 
/* 183 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 188 */     super.aiStep();
/* 190 */     updateSwingTime();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\decoration\Mannequin.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */