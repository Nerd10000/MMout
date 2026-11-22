/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.model.effects.SpearAnimations;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Ease;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.SwingAnimationType;
/*     */ 
/*     */ public class HumanoidModel<T extends HumanoidRenderState> extends EntityModel<T> implements ArmedModel<T>, HeadedModel {
/*  32 */   public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(true, 16.0F, 0.0F, 2.0F, 2.0F, 24.0F, Set.of("head"));
/*     */   
/*     */   public static final float OVERLAY_SCALE = 0.25F;
/*     */   
/*     */   public static final float HAT_OVERLAY_SCALE = 0.5F;
/*     */   
/*     */   public static final float LEGGINGS_OVERLAY_SCALE = -0.1F;
/*     */   
/*     */   private static final float DUCK_WALK_ROTATION = 0.005F;
/*     */   
/*     */   private static final float SPYGLASS_ARM_ROT_Y = 0.2617994F;
/*     */   
/*     */   private static final float SPYGLASS_ARM_ROT_X = 1.9198622F;
/*     */   
/*     */   private static final float SPYGLASS_ARM_CROUCH_ROT_X = 0.2617994F;
/*     */   
/*     */   private static final float HIGHEST_SHIELD_BLOCKING_ANGLE = -1.3962634F;
/*     */   
/*     */   private static final float LOWEST_SHIELD_BLOCKING_ANGLE = 0.43633232F;
/*     */   
/*     */   private static final float HORIZONTAL_SHIELD_MOVEMENT_LIMIT = 0.5235988F;
/*     */   
/*     */   public static final float TOOT_HORN_XROT_BASE = 1.4835298F;
/*     */   
/*     */   public static final float TOOT_HORN_YROT_BASE = 0.5235988F;
/*     */   
/*  48 */   protected static final Map<EquipmentSlot, Set<String>> ADULT_ARMOR_PARTS_PER_SLOT = Maps.newEnumMap(Map.of(EquipmentSlot.HEAD, 
/*  49 */         Set.of("head"), EquipmentSlot.CHEST, 
/*  50 */         Set.of("body", "left_arm", "right_arm"), EquipmentSlot.LEGS, 
/*  51 */         Set.of("left_leg", "right_leg", "body"), EquipmentSlot.FEET, 
/*  52 */         Set.of("left_leg", "right_leg")));
/*     */   
/*  55 */   protected static final Map<EquipmentSlot, Set<String>> BABY_ARMOR_PARTS_PER_SLOT = Maps.newEnumMap(Map.of(EquipmentSlot.HEAD, 
/*  56 */         Set.of("head"), EquipmentSlot.CHEST, 
/*  57 */         Set.of("body", "left_arm", "right_arm"), EquipmentSlot.LEGS, 
/*  58 */         Set.of("left_leg", "right_leg", "waist"), EquipmentSlot.FEET, 
/*  59 */         Set.of("left_foot", "right_foot")));
/*     */   
/*     */   public final ModelPart head;
/*     */   
/*     */   public final ModelPart hat;
/*     */   
/*     */   public final ModelPart body;
/*     */   
/*     */   public final ModelPart rightArm;
/*     */   
/*     */   public final ModelPart leftArm;
/*     */   
/*     */   public final ModelPart rightLeg;
/*     */   
/*     */   public final ModelPart leftLeg;
/*     */   
/*     */   public enum ArmPose {
/*  63 */     EMPTY(false, false),
/*  64 */     ITEM(false, false),
/*  65 */     BLOCK(false, false),
/*  66 */     BOW_AND_ARROW(true, true),
/*  67 */     THROW_TRIDENT(false, true),
/*  68 */     CROSSBOW_CHARGE(true, true),
/*  69 */     CROSSBOW_HOLD(true, true),
/*  70 */     SPYGLASS(false, false),
/*  71 */     TOOT_HORN(false, false),
/*  72 */     BRUSH(false, false),
/*  73 */     SPEAR(false, true) {
/*     */       public <S extends ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, ItemStack actualItem) {
/*  76 */         SpearAnimations.thirdPersonUseItem((ArmedEntityRenderState)state, poseStack, ticksUsingItem, arm, actualItem);
/*     */       }
/*     */     };
/*     */     
/*     */     private final boolean twoHanded;
/*     */     
/*     */     private final boolean affectsOffhandPose;
/*     */     
/*     */     ArmPose(boolean twoHanded, boolean affectsOffhandPose) {
/*  85 */       this.twoHanded = twoHanded;
/*  86 */       this.affectsOffhandPose = affectsOffhandPose;
/*     */     }
/*     */     
/*     */     public boolean isTwoHanded() {
/*  90 */       return this.twoHanded;
/*     */     }
/*     */     
/*     */     public boolean affectsOffhandPose() {
/*  94 */       return this.affectsOffhandPose;
/*     */     }
/*     */     
/*     */     public <S extends ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, ItemStack actualItem) {}
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public <S extends ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, ItemStack actualItem) {
/*     */       SpearAnimations.thirdPersonUseItem((ArmedEntityRenderState)state, poseStack, ticksUsingItem, arm, actualItem);
/*     */     }
/*     */   }
/*     */   
/*     */   public HumanoidModel(ModelPart root) {
/* 110 */     this(root, RenderTypes::entityCutout);
/*     */   }
/*     */   
/*     */   public HumanoidModel(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 114 */     super(root, renderType);
/* 115 */     this.head = root.getChild("head");
/* 116 */     this.hat = this.head.getChild("hat");
/* 117 */     this.body = root.getChild("body");
/* 118 */     this.rightArm = root.getChild("right_arm");
/* 119 */     this.leftArm = root.getChild("left_arm");
/* 120 */     this.rightLeg = root.getChild("right_leg");
/* 121 */     this.leftLeg = root.getChild("left_leg");
/*     */   }
/*     */   
/*     */   public static MeshDefinition createMesh(CubeDeformation g, float yOffset) {
/* 125 */     MeshDefinition mesh = new MeshDefinition();
/* 126 */     PartDefinition root = mesh.getRoot();
/* 127 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 128 */         CubeListBuilder.create()
/* 129 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, g), 
/* 130 */         PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
/* 132 */     head.addOrReplaceChild("hat", 
/* 133 */         CubeListBuilder.create()
/* 134 */         .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, g.extend(0.5F)), PartPose.ZERO);
/* 137 */     root.addOrReplaceChild("body", 
/* 138 */         CubeListBuilder.create()
/* 139 */         .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, g), 
/* 140 */         PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
/* 142 */     root.addOrReplaceChild("right_arm", 
/* 143 */         CubeListBuilder.create()
/* 144 */         .texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 145 */         PartPose.offset(-5.0F, 2.0F + yOffset, 0.0F));
/* 147 */     root.addOrReplaceChild("left_arm", 
/* 148 */         CubeListBuilder.create()
/* 149 */         .texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 150 */         PartPose.offset(5.0F, 2.0F + yOffset, 0.0F));
/* 152 */     root.addOrReplaceChild("right_leg", 
/* 153 */         CubeListBuilder.create()
/* 154 */         .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 155 */         PartPose.offset(-1.9F, 12.0F + yOffset, 0.0F));
/* 157 */     root.addOrReplaceChild("left_leg", 
/* 158 */         CubeListBuilder.create()
/* 159 */         .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), 
/* 160 */         PartPose.offset(1.9F, 12.0F + yOffset, 0.0F));
/* 162 */     return mesh;
/*     */   }
/*     */   
/*     */   public static ArmorModelSet<MeshDefinition> createArmorMeshSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/* 166 */     return createArmorMeshSet(HumanoidModel::createBaseArmorMesh, ADULT_ARMOR_PARTS_PER_SLOT, innerDeformation, outerDeformation);
/*     */   }
/*     */   
/*     */   public static ArmorModelSet<MeshDefinition> createBabyArmorMeshSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation, PartPose armOffset) {
/* 170 */     return createArmorMeshSet(cube -> createBabyArmorMesh(cube, armOffset), BABY_ARMOR_PARTS_PER_SLOT, innerDeformation, outerDeformation);
/*     */   }
/*     */   
/*     */   protected static ArmorModelSet<MeshDefinition> createArmorMeshSet(Function<CubeDeformation, MeshDefinition> baseFactory, Map<EquipmentSlot, Set<String>> partsPerSlot, CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/* 179 */     MeshDefinition head = baseFactory.apply(outerDeformation);
/* 180 */     head.getRoot().retainPartsAndChildren(partsPerSlot.get(EquipmentSlot.HEAD));
/* 182 */     MeshDefinition chest = baseFactory.apply(outerDeformation);
/* 183 */     chest.getRoot().retainExactParts(partsPerSlot.get(EquipmentSlot.CHEST));
/* 185 */     MeshDefinition legs = baseFactory.apply(innerDeformation);
/* 186 */     legs.getRoot().retainExactParts(partsPerSlot.get(EquipmentSlot.LEGS));
/* 188 */     MeshDefinition feet = baseFactory.apply(outerDeformation);
/* 189 */     feet.getRoot().retainExactParts(partsPerSlot.get(EquipmentSlot.FEET));
/* 191 */     return new ArmorModelSet(head, chest, legs, feet);
/*     */   }
/*     */   
/*     */   private static MeshDefinition createBaseArmorMesh(CubeDeformation g) {
/* 195 */     MeshDefinition mesh = createMesh(g, 0.0F);
/* 196 */     PartDefinition root = mesh.getRoot();
/* 197 */     root.addOrReplaceChild("right_leg", 
/* 198 */         CubeListBuilder.create()
/* 199 */         .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g.extend(-0.1F)), 
/* 200 */         PartPose.offset(-1.9F, 12.0F, 0.0F));
/* 202 */     root.addOrReplaceChild("left_leg", 
/* 203 */         CubeListBuilder.create()
/* 204 */         .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g.extend(-0.1F)), 
/* 205 */         PartPose.offset(1.9F, 12.0F, 0.0F));
/* 207 */     return mesh;
/*     */   }
/*     */   
/*     */   private static MeshDefinition createBabyArmorMesh(CubeDeformation g, PartPose armOffset) {
/* 211 */     MeshDefinition mesh = new MeshDefinition();
/* 212 */     PartDefinition root = mesh.getRoot();
/* 214 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 215 */         CubeListBuilder.create()
/* 216 */         .texOffs(0, 0).addBox(-4.5F, -7.0F, -4.5F, 9.0F, 8.0F, 8.0F, g), 
/* 217 */         PartPose.offset(0.0F, 15.0F, 0.0F));
/* 220 */     root.addOrReplaceChild("body", 
/* 221 */         CubeListBuilder.create()
/* 222 */         .texOffs(0, 17).addBox(-3.0F, -3.0F, -1.5F, 6.0F, 5.0F, 3.0F, g), 
/* 223 */         PartPose.offset(0.0F, 18.0F, 0.0F));
/* 226 */     root.addOrReplaceChild("waist", 
/* 227 */         CubeListBuilder.create()
/* 228 */         .texOffs(0, 36).addBox(-3.0F, -1.2F, -1.49F, 5.9F, 2.0F, 2.9F, g.extend(-0.1F)), 
/* 229 */         PartPose.offset(0.0F, 19.0F, 0.0F));
/* 232 */     root.addOrReplaceChild("right_arm", 
/* 233 */         CubeListBuilder.create()
/* 234 */         .texOffs(30, 25).addBox(-1.0F, 0.0F, -1.53F, 2.0F, 5.0F, 3.0F, g), 
/* 235 */         PartPose.offset(-3.5F - armOffset.x(), 15.5F + armOffset.y(), 0.0F + armOffset.z()));
/* 238 */     root.addOrReplaceChild("left_arm", 
/* 239 */         CubeListBuilder.create()
/* 240 */         .texOffs(30, 17).addBox(-1.0F, 0.0F, -1.53F, 2.0F, 5.0F, 3.0F, g), 
/* 241 */         PartPose.offset(3.5F + armOffset.x(), 15.5F + armOffset.y(), 0.0F + armOffset.z()));
/* 244 */     root.addOrReplaceChild("inner_body", 
/* 245 */         CubeListBuilder.create()
/* 246 */         .texOffs(0, 17).addBox(-3.0F, -3.0F, -1.5F, 6.0F, 5.0F, 3.0F, g), 
/* 247 */         PartPose.offset(0.0F, 18.0F, 0.0F));
/* 250 */     PartDefinition rightLeg = root.addOrReplaceChild("left_leg", 
/* 251 */         CubeListBuilder.create()
/* 252 */         .texOffs(18, 24).addBox(-2.0F, -0.2F, -2.0F, 3.0F, 4.0F, 3.0F, g.extend(-0.1F)), 
/* 253 */         PartPose.offset(1.5F, 20.0F, 0.5F));
/* 256 */     PartDefinition leftLeg = root.addOrReplaceChild("right_leg", 
/* 257 */         CubeListBuilder.create()
/* 258 */         .texOffs(18, 17).addBox(-1.0F, -0.2F, -2.0F, 3.0F, 4.0F, 3.0F, g.extend(-0.1F)), 
/* 259 */         PartPose.offset(-1.5F, 20.0F, 0.5F));
/* 262 */     rightLeg.addOrReplaceChild("right_foot", 
/* 263 */         CubeListBuilder.create()
/* 264 */         .texOffs(0, 25).addBox(-2.0F, 2.9F, -2.0F, 3.0F, 1.0F, 3.0F, g), 
/* 265 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/* 268 */     leftLeg.addOrReplaceChild("left_foot", 
/* 269 */         CubeListBuilder.create()
/* 270 */         .texOffs(0, 29).mirror().addBox(-1.0F, 2.9F, -2.0F, 3.0F, 1.0F, 3.0F, g).mirror(false), 
/* 271 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/* 274 */     head.addOrReplaceChild("hat", 
/* 275 */         CubeListBuilder.create(), PartPose.ZERO);
/* 279 */     return mesh;
/*     */   }
/*     */   
/*     */   public void setupAnim(T state) {
/* 284 */     super.setupAnim(state);
/* 286 */     ArmPose leftArmPose = ((HumanoidRenderState)state).leftArmPose;
/* 287 */     ArmPose rightArmPose = ((HumanoidRenderState)state).rightArmPose;
/* 288 */     float swimAmount = ((HumanoidRenderState)state).swimAmount;
/* 290 */     boolean fallFlying = ((HumanoidRenderState)state).isFallFlying;
/* 292 */     this.head.xRot = ((HumanoidRenderState)state).xRot * 0.017453292F;
/* 293 */     this.head.yRot = ((HumanoidRenderState)state).yRot * 0.017453292F;
/* 295 */     if (fallFlying) {
/* 296 */       this.head.xRot = -0.7853982F;
/* 297 */     } else if (swimAmount > 0.0F) {
/* 298 */       this.head.xRot = Mth.rotLerpRad(swimAmount, this.head.xRot, -0.7853982F);
/*     */     } 
/* 301 */     float animationPos = ((HumanoidRenderState)state).walkAnimationPos;
/* 302 */     float animationSpeed = ((HumanoidRenderState)state).walkAnimationSpeed;
/* 303 */     this.rightArm.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 2.0F * animationSpeed * 0.5F / ((HumanoidRenderState)state).speedValue;
/* 304 */     this.leftArm.xRot = Mth.cos((animationPos * 0.6662F)) * 2.0F * animationSpeed * 0.5F / ((HumanoidRenderState)state).speedValue;
/* 306 */     this.rightLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed / ((HumanoidRenderState)state).speedValue;
/* 307 */     this.leftLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed / ((HumanoidRenderState)state).speedValue;
/* 308 */     this.rightLeg.yRot = 0.005F;
/* 309 */     this.leftLeg.yRot = -0.005F;
/* 310 */     this.rightLeg.zRot = 0.005F;
/* 311 */     this.leftLeg.zRot = -0.005F;
/* 313 */     if (((HumanoidRenderState)state).isPassenger) {
/* 314 */       this.rightArm.xRot += -0.62831855F;
/* 315 */       this.leftArm.xRot += -0.62831855F;
/* 317 */       this.rightLeg.xRot = -1.4137167F;
/* 318 */       this.rightLeg.yRot = 0.31415927F;
/* 319 */       this.rightLeg.zRot = 0.07853982F;
/* 321 */       this.leftLeg.xRot = -1.4137167F;
/* 322 */       this.leftLeg.yRot = -0.31415927F;
/* 323 */       this.leftLeg.zRot = -0.07853982F;
/*     */     } 
/* 327 */     boolean rightHanded = (((HumanoidRenderState)state).mainArm == HumanoidArm.RIGHT);
/* 328 */     if (((HumanoidRenderState)state).isUsingItem) {
/* 329 */       boolean mainHandUsed = (((HumanoidRenderState)state).useItemHand == InteractionHand.MAIN_HAND);
/* 330 */       if (mainHandUsed == rightHanded) {
/* 331 */         poseRightArm(state);
/* 332 */         if (!((HumanoidRenderState)state).rightArmPose.affectsOffhandPose())
/* 333 */           poseLeftArm(state); 
/*     */       } else {
/* 336 */         poseLeftArm(state);
/* 337 */         if (!((HumanoidRenderState)state).leftArmPose.affectsOffhandPose())
/* 338 */           poseRightArm(state); 
/*     */       } 
/*     */     } else {
/* 342 */       boolean twoHandedOffhand = rightHanded ? leftArmPose.isTwoHanded() : rightArmPose.isTwoHanded();
/* 343 */       if (rightHanded != twoHandedOffhand) {
/* 344 */         poseLeftArm(state);
/* 345 */         if (!((HumanoidRenderState)state).leftArmPose.affectsOffhandPose())
/* 346 */           poseRightArm(state); 
/*     */       } else {
/* 349 */         poseRightArm(state);
/* 350 */         if (!((HumanoidRenderState)state).rightArmPose.affectsOffhandPose())
/* 351 */           poseLeftArm(state); 
/*     */       } 
/*     */     } 
/* 356 */     setupAttackAnimation(state);
/* 358 */     if (((HumanoidRenderState)state).isCrouching) {
/* 359 */       this.body.xRot = 0.5F;
/* 360 */       this.rightArm.xRot += 0.4F;
/* 361 */       this.leftArm.xRot += 0.4F;
/* 362 */       this.rightLeg.z += 4.0F;
/* 363 */       this.leftLeg.z += 4.0F;
/* 364 */       this.head.y += 4.2F;
/* 365 */       this.body.y += 3.2F;
/* 366 */       this.leftArm.y += 3.2F;
/* 367 */       this.rightArm.y += 3.2F;
/*     */     } 
/* 370 */     if (rightArmPose != ArmPose.SPYGLASS)
/* 371 */       AnimationUtils.bobModelPart(this.rightArm, ((HumanoidRenderState)state).ageInTicks, 1.0F); 
/* 373 */     if (leftArmPose != ArmPose.SPYGLASS)
/* 374 */       AnimationUtils.bobModelPart(this.leftArm, ((HumanoidRenderState)state).ageInTicks, -1.0F); 
/* 377 */     if (swimAmount > 0.0F) {
/* 378 */       float swimPos = animationPos % 26.0F;
/* 381 */       HumanoidArm attackArm = ((HumanoidRenderState)state).attackArm;
/* 382 */       float rightArmSwimAmount = (((HumanoidRenderState)state).rightArmPose == ArmPose.SPEAR || (attackArm == HumanoidArm.RIGHT && ((HumanoidRenderState)state).attackTime > 0.0F)) ? 0.0F : swimAmount;
/* 383 */       float leftArmSwimAmount = (((HumanoidRenderState)state).leftArmPose == ArmPose.SPEAR || (attackArm == HumanoidArm.LEFT && ((HumanoidRenderState)state).attackTime > 0.0F)) ? 0.0F : swimAmount;
/* 385 */       if (!((HumanoidRenderState)state).isUsingItem)
/* 386 */         if (swimPos < 14.0F) {
/* 387 */           this.leftArm.xRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.xRot, 0.0F);
/* 388 */           this.rightArm.xRot = Mth.lerp(rightArmSwimAmount, this.rightArm.xRot, 0.0F);
/* 390 */           this.leftArm.yRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.yRot, 3.1415927F);
/* 391 */           this.rightArm.yRot = Mth.lerp(rightArmSwimAmount, this.rightArm.yRot, 3.1415927F);
/* 393 */           this.leftArm.zRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.zRot, 3.1415927F + 1.8707964F * quadraticArmUpdate(swimPos) / quadraticArmUpdate(14.0F));
/* 394 */           this.rightArm.zRot = Mth.lerp(rightArmSwimAmount, this.rightArm.zRot, 3.1415927F - 1.8707964F * quadraticArmUpdate(swimPos) / quadraticArmUpdate(14.0F));
/* 395 */         } else if (swimPos >= 14.0F && swimPos < 22.0F) {
/* 396 */           float internalSwimPos = (swimPos - 14.0F) / 8.0F;
/* 398 */           this.leftArm.xRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.xRot, 1.5707964F * internalSwimPos);
/* 399 */           this.rightArm.xRot = Mth.lerp(rightArmSwimAmount, this.rightArm.xRot, 1.5707964F * internalSwimPos);
/* 401 */           this.leftArm.yRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.yRot, 3.1415927F);
/* 402 */           this.rightArm.yRot = Mth.lerp(rightArmSwimAmount, this.rightArm.yRot, 3.1415927F);
/* 404 */           this.leftArm.zRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.zRot, 5.012389F - 1.8707964F * internalSwimPos);
/* 405 */           this.rightArm.zRot = Mth.lerp(rightArmSwimAmount, this.rightArm.zRot, 1.2707963F + 1.8707964F * internalSwimPos);
/* 406 */         } else if (swimPos >= 22.0F && swimPos < 26.0F) {
/* 407 */           float internalSwimPos = (swimPos - 22.0F) / 4.0F;
/* 409 */           this.leftArm.xRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.xRot, 1.5707964F - 1.5707964F * internalSwimPos);
/* 410 */           this.rightArm.xRot = Mth.lerp(rightArmSwimAmount, this.rightArm.xRot, 1.5707964F - 1.5707964F * internalSwimPos);
/* 412 */           this.leftArm.yRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.yRot, 3.1415927F);
/* 413 */           this.rightArm.yRot = Mth.lerp(rightArmSwimAmount, this.rightArm.yRot, 3.1415927F);
/* 415 */           this.leftArm.zRot = Mth.rotLerpRad(leftArmSwimAmount, this.leftArm.zRot, 3.1415927F);
/* 416 */           this.rightArm.zRot = Mth.lerp(rightArmSwimAmount, this.rightArm.zRot, 3.1415927F);
/*     */         }  
/* 420 */       float amplitude = 0.3F;
/* 421 */       float slowdown = 0.33333334F;
/* 422 */       this.leftLeg.xRot = Mth.lerp(swimAmount, this.leftLeg.xRot, 0.3F * Mth.cos((animationPos * 0.33333334F + 3.1415927F)));
/* 423 */       this.rightLeg.xRot = Mth.lerp(swimAmount, this.rightLeg.xRot, 0.3F * Mth.cos((animationPos * 0.33333334F)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void poseRightArm(T state) {
/* 428 */     switch (((HumanoidRenderState)state).rightArmPose.ordinal()) {
/*     */       case 0:
/* 429 */         this.rightArm.yRot = 0.0F;
/*     */         break;
/*     */       case 2:
/* 430 */         poseBlockingArm(this.rightArm, true);
/*     */         break;
/*     */       case 1:
/* 432 */         this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.31415927F;
/* 433 */         this.rightArm.yRot = 0.0F;
/*     */         break;
/*     */       case 4:
/* 436 */         this.rightArm.xRot = this.rightArm.xRot * 0.5F - 3.1415927F;
/* 437 */         this.rightArm.yRot = 0.0F;
/*     */         break;
/*     */       case 10:
/* 439 */         SpearAnimations.thirdPersonHandUse(this.rightArm, this.head, true, state.getUseItemStackForArm(HumanoidArm.RIGHT), (HumanoidRenderState)state);
/*     */         break;
/*     */       case 3:
/* 441 */         this.rightArm.yRot = -0.1F + this.head.yRot;
/* 442 */         this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
/* 443 */         this.rightArm.xRot = -1.5707964F + this.head.xRot;
/* 444 */         this.leftArm.xRot = -1.5707964F + this.head.xRot;
/*     */         break;
/*     */       case 5:
/* 446 */         AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, ((HumanoidRenderState)state).maxCrossbowChargeDuration, ((HumanoidRenderState)state).ticksUsingItem, true);
/*     */         break;
/*     */       case 6:
/* 447 */         AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
/*     */         break;
/*     */       case 9:
/* 449 */         this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.62831855F;
/* 450 */         this.rightArm.yRot = 0.0F;
/*     */         break;
/*     */       case 7:
/* 453 */         this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (((HumanoidRenderState)state).isCrouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
/* 454 */         this.head.yRot -= 0.2617994F;
/*     */         break;
/*     */       case 8:
/* 457 */         this.rightArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
/* 458 */         this.head.yRot -= 0.5235988F;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void poseLeftArm(T state) {
/* 464 */     switch (((HumanoidRenderState)state).leftArmPose.ordinal()) {
/*     */       case 0:
/* 465 */         this.leftArm.yRot = 0.0F;
/*     */         break;
/*     */       case 2:
/* 466 */         poseBlockingArm(this.leftArm, false);
/*     */         break;
/*     */       case 1:
/* 468 */         this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.31415927F;
/* 469 */         this.leftArm.yRot = 0.0F;
/*     */         break;
/*     */       case 4:
/* 472 */         this.leftArm.xRot = this.leftArm.xRot * 0.5F - 3.1415927F;
/* 473 */         this.leftArm.yRot = 0.0F;
/*     */         break;
/*     */       case 10:
/* 475 */         SpearAnimations.thirdPersonHandUse(this.leftArm, this.head, false, state.getUseItemStackForArm(HumanoidArm.LEFT), (HumanoidRenderState)state);
/*     */         break;
/*     */       case 3:
/* 477 */         this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
/* 478 */         this.leftArm.yRot = 0.1F + this.head.yRot;
/* 479 */         this.rightArm.xRot = -1.5707964F + this.head.xRot;
/* 480 */         this.leftArm.xRot = -1.5707964F + this.head.xRot;
/*     */         break;
/*     */       case 5:
/* 482 */         AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, ((HumanoidRenderState)state).maxCrossbowChargeDuration, ((HumanoidRenderState)state).ticksUsingItem, false);
/*     */         break;
/*     */       case 6:
/* 483 */         AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
/*     */         break;
/*     */       case 9:
/* 485 */         this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.62831855F;
/* 486 */         this.leftArm.yRot = 0.0F;
/*     */         break;
/*     */       case 7:
/* 489 */         this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (((HumanoidRenderState)state).isCrouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
/* 490 */         this.head.yRot += 0.2617994F;
/*     */         break;
/*     */       case 8:
/* 493 */         this.leftArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
/* 494 */         this.head.yRot += 0.5235988F;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void poseBlockingArm(ModelPart arm, boolean right) {
/* 500 */     arm.xRot = arm.xRot * 0.5F - 0.9424779F + Mth.clamp(this.head.xRot, -1.3962634F, 0.43633232F);
/* 501 */     arm.yRot = (right ? -30.0F : 30.0F) * 0.017453292F + Mth.clamp(this.head.yRot, -0.5235988F, 0.5235988F);
/*     */   }
/*     */   
/*     */   protected void setupAttackAnimation(T state) {
/*     */     float swing, aa, bb;
/*     */     ModelPart attackArm;
/* 505 */     float attackTime = ((HumanoidRenderState)state).attackTime;
/* 506 */     if (attackTime <= 0.0F)
/*     */       return; 
/* 510 */     this.body.yRot = Mth.sin((Mth.sqrt(attackTime) * 6.2831855F)) * 0.2F;
/* 511 */     if (((HumanoidRenderState)state).attackArm == HumanoidArm.LEFT)
/* 512 */       this.body.yRot *= -1.0F; 
/* 514 */     float ageScale = ((HumanoidRenderState)state).ageScale;
/* 515 */     this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F * ageScale;
/* 516 */     this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F * ageScale;
/* 517 */     this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F * ageScale;
/* 518 */     this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F * ageScale;
/* 519 */     this.rightArm.yRot += this.body.yRot;
/* 520 */     this.leftArm.yRot += this.body.yRot;
/* 521 */     this.leftArm.xRot += this.body.yRot;
/* 523 */     switch (((HumanoidRenderState)state).swingAnimationType) {
/*     */       case WHACK:
/* 525 */         swing = Ease.outQuart(attackTime);
/* 527 */         aa = Mth.sin((swing * 3.1415927F));
/* 528 */         bb = Mth.sin((attackTime * 3.1415927F)) * -(this.head.xRot - 0.7F) * 0.75F;
/* 530 */         attackArm = getArm(((HumanoidRenderState)state).attackArm);
/* 532 */         attackArm.xRot -= aa * 1.2F + bb;
/* 533 */         attackArm.yRot += this.body.yRot * 2.0F;
/* 534 */         attackArm.zRot += Mth.sin((attackTime * 3.1415927F)) * -0.4F;
/*     */         break;
/*     */       case STAB:
/* 539 */         SpearAnimations.thirdPersonAttackHand(this, (HumanoidRenderState)state);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float quadraticArmUpdate(float x) {
/* 544 */     return -65.0F * x + x * x;
/*     */   }
/*     */   
/*     */   public void translateToHand(HumanoidRenderState state, HumanoidArm arm, PoseStack poseStack) {
/* 549 */     this.root.translateAndRotate(poseStack);
/* 550 */     getArm(arm).translateAndRotate(poseStack);
/*     */   }
/*     */   
/*     */   public ModelPart getArm(HumanoidArm arm) {
/* 554 */     if (arm == HumanoidArm.LEFT)
/* 555 */       return this.leftArm; 
/* 557 */     return this.rightArm;
/*     */   }
/*     */   
/*     */   public ModelPart getHead() {
/* 563 */     return this.head;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\HumanoidModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */