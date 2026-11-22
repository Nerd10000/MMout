/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.EntityDataSerializers;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.world.entity.player.PlayerModelPart;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class Avatar extends LivingEntity {
/* 16 */   public static final HumanoidArm DEFAULT_MAIN_HAND = HumanoidArm.RIGHT;
/*    */   
/*    */   public static final int DEFAULT_MODEL_CUSTOMIZATION = 0;
/*    */   
/*    */   public static final float DEFAULT_EYE_HEIGHT = 1.62F;
/*    */   
/* 19 */   public static final Vec3 DEFAULT_VEHICLE_ATTACHMENT = new Vec3(0.0D, 0.6D, 0.0D);
/*    */   
/*    */   private static final float CROUCH_BB_HEIGHT = 1.5F;
/*    */   
/*    */   private static final float SWIMMING_BB_WIDTH = 0.6F;
/*    */   
/*    */   public static final float SWIMMING_BB_HEIGHT = 0.6F;
/*    */   
/* 25 */   protected static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.8F)
/* 26 */     .withEyeHeight(1.62F)
/* 27 */     .withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, DEFAULT_VEHICLE_ATTACHMENT));
/*    */   
/* 29 */   protected static final Map<Pose, EntityDimensions> POSES = (Map<Pose, EntityDimensions>)ImmutableMap.builder()
/* 30 */     .put(Pose.STANDING, STANDING_DIMENSIONS)
/* 31 */     .put(Pose.SLEEPING, SLEEPING_DIMENSIONS)
/* 32 */     .put(Pose.FALL_FLYING, EntityDimensions.scalable(0.6F, 0.6F).withEyeHeight(0.4F))
/* 33 */     .put(Pose.SWIMMING, EntityDimensions.scalable(0.6F, 0.6F).withEyeHeight(0.4F))
/* 34 */     .put(Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6F, 0.6F).withEyeHeight(0.4F))
/* 35 */     .put(Pose.CROUCHING, EntityDimensions.scalable(0.6F, 1.5F)
/* 36 */       .withEyeHeight(1.27F)
/* 37 */       .withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, DEFAULT_VEHICLE_ATTACHMENT)))
/*    */     
/* 39 */     .put(Pose.DYING, EntityDimensions.fixed(0.2F, 0.2F).withEyeHeight(1.62F))
/* 40 */     .build();
/*    */   
/* 42 */   protected static final EntityDataAccessor<HumanoidArm> DATA_PLAYER_MAIN_HAND = SynchedEntityData.defineId(Avatar.class, EntityDataSerializers.HUMANOID_ARM);
/*    */   
/* 43 */   protected static final EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION = SynchedEntityData.defineId(Avatar.class, EntityDataSerializers.BYTE);
/*    */   
/*    */   protected Avatar(EntityType<? extends LivingEntity> type, Level level) {
/* 46 */     super(type, level);
/*    */   }
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 51 */     super.defineSynchedData(entityData);
/* 53 */     entityData.define(DATA_PLAYER_MAIN_HAND, DEFAULT_MAIN_HAND);
/* 54 */     entityData.define(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf((byte)0));
/*    */   }
/*    */   
/*    */   public HumanoidArm getMainArm() {
/* 59 */     return (HumanoidArm)this.entityData.get(DATA_PLAYER_MAIN_HAND);
/*    */   }
/*    */   
/*    */   public void setMainArm(HumanoidArm mainArm) {
/* 63 */     this.entityData.set(DATA_PLAYER_MAIN_HAND, mainArm);
/*    */   }
/*    */   
/*    */   public boolean isModelPartShown(PlayerModelPart part) {
/* 67 */     return ((((Byte)getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION)).byteValue() & part.getMask()) == part.getMask());
/*    */   }
/*    */   
/*    */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 72 */     return POSES.getOrDefault(pose, STANDING_DIMENSIONS);
/*    */   }
/*    */   
/*    */   public abstract ResolvableProfile getProfile();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Avatar.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */