/*      */ package net.minecraft.client.player;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.stream.Stream;
/*      */ import java.util.stream.StreamSupport;
/*      */ import net.minecraft.client.ClientRecipeBook;
/*      */ import net.minecraft.client.KeyMapping;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.inventory.BookEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.JigsawBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.SignEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.TestBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.TestInstanceBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.options.HasGamemasterPermissionReaction;
/*      */ import net.minecraft.client.multiplayer.ClientLevel;
/*      */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*      */ import net.minecraft.client.multiplayer.chat.ChatAbilities;
/*      */ import net.minecraft.client.resources.sounds.AmbientSoundHandler;
/*      */ import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
/*      */ import net.minecraft.client.resources.sounds.BubbleColumnAmbientSoundHandler;
/*      */ import net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.RidingEntitySoundInstance;
/*      */ import net.minecraft.client.resources.sounds.RidingMinecartSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundHandler;
/*      */ import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundInstances;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSwingPacket;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.server.dialog.Dialog;
/*      */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.server.permissions.Permissions;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.StatsCounter;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.TickThrottler;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.MoverType;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*      */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*      */ import net.minecraft.world.entity.player.Abilities;
/*      */ import net.minecraft.world.entity.player.Input;
/*      */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*      */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*      */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*      */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*      */ import net.minecraft.world.inventory.ClickAction;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.component.UseEffects;
/*      */ import net.minecraft.world.item.component.WritableBookContent;
/*      */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.block.Portal;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.EntityHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class LocalPlayer extends AbstractClientPlayer {
/*  114 */   public static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int POSITION_REMINDER_INTERVAL = 20;
/*      */   
/*      */   private static final int WATER_VISION_MAX_TIME = 600;
/*      */   
/*      */   private static final int WATER_VISION_QUICK_TIME = 100;
/*      */   
/*      */   private static final float WATER_VISION_QUICK_PERCENT = 0.6F;
/*      */   
/*      */   private static final double SUFFOCATING_COLLISION_CHECK_SCALE = 0.35D;
/*      */   
/*      */   private static final double MINOR_COLLISION_ANGLE_THRESHOLD_RADIAN = 0.13962633907794952D;
/*      */   
/*      */   public final ClientPacketListener connection;
/*      */   
/*      */   private final StatsCounter stats;
/*      */   
/*      */   private final ClientRecipeBook recipeBook;
/*      */   
/*  126 */   private final TickThrottler dropSpamThrottler = new TickThrottler(20, 1280);
/*      */   
/*  128 */   private final List<AmbientSoundHandler> ambientSoundHandlers = Lists.newArrayList();
/*      */   
/*  130 */   private PermissionSet permissions = PermissionSet.NO_PERMISSIONS;
/*      */   
/*      */   private ChatAbilities chatAbilities;
/*      */   
/*      */   private double xLast;
/*      */   
/*      */   private double yLast;
/*      */   
/*      */   private double zLast;
/*      */   
/*      */   private float yRotLast;
/*      */   
/*      */   private float xRotLast;
/*      */   
/*      */   private boolean lastOnGround;
/*      */   
/*      */   private boolean lastHorizontalCollision;
/*      */   
/*      */   private boolean crouching;
/*      */   
/*      */   private boolean wasSprinting;
/*      */   
/*      */   private int positionReminder;
/*      */   
/*      */   private boolean flashOnSetHealth;
/*      */   
/*  146 */   public ClientInput input = new ClientInput();
/*      */   
/*      */   private Input lastSentInput;
/*      */   
/*      */   protected final Minecraft minecraft;
/*      */   
/*      */   protected int sprintTriggerTime;
/*      */   
/*      */   private static final int EXPERIENCE_DISPLAY_UNREADY_TO_SET = -2147483648;
/*      */   
/*      */   private static final int EXPERIENCE_DISPLAY_READY_TO_SET = -2147483647;
/*      */   
/*  152 */   public int experienceDisplayStartTick = Integer.MIN_VALUE;
/*      */   
/*      */   public float yBob;
/*      */   
/*      */   public float xBob;
/*      */   
/*      */   public float yBobO;
/*      */   
/*      */   public float xBobO;
/*      */   
/*      */   private int jumpRidingTicks;
/*      */   
/*      */   private float jumpRidingScale;
/*      */   
/*      */   public float portalEffectIntensity;
/*      */   
/*      */   public float oPortalEffectIntensity;
/*      */   
/*      */   private boolean startedUsingItem;
/*      */   
/*      */   private InteractionHand usingItemHand;
/*      */   
/*      */   private boolean handsBusy;
/*      */   
/*      */   private boolean autoJumpEnabled = true;
/*      */   
/*      */   private int autoJumpTime;
/*      */   
/*      */   private boolean wasFallFlying;
/*      */   
/*      */   private int waterVisionTime;
/*      */   
/*      */   private boolean showDeathScreen = true;
/*      */   
/*      */   private boolean doLimitedCrafting = false;
/*      */   
/*      */   public LocalPlayer(Minecraft minecraft, ClientLevel level, ClientPacketListener connection, StatsCounter stats, ClientRecipeBook recipeBook, Input lastSentInput, boolean wasSprinting, ChatAbilities chatAbilities) {
/*  179 */     super(level, connection.getLocalGameProfile());
/*  180 */     this.minecraft = minecraft;
/*  181 */     this.connection = connection;
/*  182 */     this.stats = stats;
/*  183 */     this.recipeBook = recipeBook;
/*  184 */     this.lastSentInput = lastSentInput;
/*  185 */     this.wasSprinting = wasSprinting;
/*  186 */     this.ambientSoundHandlers.add(new UnderwaterAmbientSoundHandler(this, minecraft.getSoundManager()));
/*  187 */     this.ambientSoundHandlers.add(new BubbleColumnAmbientSoundHandler(this));
/*  188 */     this.ambientSoundHandlers.add(new BiomeAmbientSoundsHandler(this, minecraft.getSoundManager()));
/*  189 */     this.chatAbilities = chatAbilities;
/*      */   }
/*      */   
/*      */   public void heal(float heal) {}
/*      */   
/*      */   public boolean startRiding(Entity entity, boolean force, boolean sendEventAndTriggers) {
/*  199 */     if (!super.startRiding(entity, force, sendEventAndTriggers))
/*  200 */       return false; 
/*  202 */     if (entity instanceof AbstractMinecart) {
/*  202 */       AbstractMinecart minecart = (AbstractMinecart)entity;
/*  206 */       this.minecraft.getSoundManager().play((SoundInstance)new RidingMinecartSoundInstance(this, minecart, true, SoundEvents.MINECART_INSIDE_UNDERWATER, 0.0F, 0.75F, 1.0F));
/*  207 */       this.minecraft.getSoundManager().play((SoundInstance)new RidingMinecartSoundInstance(this, minecart, false, SoundEvents.MINECART_INSIDE, 0.0F, 0.75F, 1.0F));
/*  208 */     } else if (entity instanceof HappyGhast) {
/*  208 */       HappyGhast happyGhast = (HappyGhast)entity;
/*  209 */       this.minecraft.getSoundManager().play((SoundInstance)new RidingEntitySoundInstance(this, (Entity)happyGhast, false, SoundEvents.HAPPY_GHAST_RIDING, happyGhast.getSoundSource(), 0.0F, 1.0F, 5.0F));
/*  210 */     } else if (entity instanceof AbstractNautilus) {
/*  210 */       AbstractNautilus nautilus = (AbstractNautilus)entity;
/*  211 */       this.minecraft.getSoundManager().play((SoundInstance)new RidingEntitySoundInstance(this, (Entity)nautilus, true, SoundEvents.NAUTILUS_RIDING, nautilus.getSoundSource(), 0.0F, 1.0F, 5.0F));
/*      */     } 
/*  213 */     return true;
/*      */   }
/*      */   
/*      */   public void removeVehicle() {
/*  218 */     super.removeVehicle();
/*  219 */     this.handsBusy = false;
/*      */   }
/*      */   
/*      */   public float getViewXRot(float a) {
/*  224 */     return getXRot();
/*      */   }
/*      */   
/*      */   public float getViewYRot(float a) {
/*  229 */     if (isPassenger())
/*  230 */       return super.getViewYRot(a); 
/*  232 */     return getYRot();
/*      */   }
/*      */   
/*      */   public void tick() {
/*  237 */     if (!this.connection.hasClientLoaded())
/*      */       return; 
/*  241 */     this.dropSpamThrottler.tick();
/*  242 */     super.tick();
/*  244 */     if (!this.lastSentInput.equals(this.input.keyPresses)) {
/*  245 */       this.connection.send((Packet)new ServerboundPlayerInputPacket(this.input.keyPresses));
/*  246 */       this.lastSentInput = this.input.keyPresses;
/*      */     } 
/*  249 */     if (isPassenger()) {
/*  250 */       this.connection.send((Packet)new ServerboundMovePlayerPacket.Rot(getYRot(), getXRot(), onGround(), this.horizontalCollision));
/*  251 */       Entity vehicle = getRootVehicle();
/*  252 */       if (vehicle != this && vehicle.isLocalInstanceAuthoritative()) {
/*  253 */         this.connection.send((Packet)ServerboundMoveVehiclePacket.fromEntity(vehicle));
/*  254 */         sendIsSprintingIfNeeded();
/*      */       } 
/*      */     } else {
/*  257 */       sendPosition();
/*      */     } 
/*  260 */     for (AmbientSoundHandler soundHandler : this.ambientSoundHandlers)
/*  261 */       soundHandler.tick(); 
/*      */   }
/*      */   
/*      */   public float getCurrentMood() {
/*  266 */     for (AmbientSoundHandler ambientSoundHandler : this.ambientSoundHandlers) {
/*  267 */       if (ambientSoundHandler instanceof BiomeAmbientSoundsHandler) {
/*  267 */         BiomeAmbientSoundsHandler biomeAmbientSoundsHandler = (BiomeAmbientSoundsHandler)ambientSoundHandler;
/*  268 */         return biomeAmbientSoundsHandler.getMoodiness();
/*      */       } 
/*      */     } 
/*  271 */     return 0.0F;
/*      */   }
/*      */   
/*      */   private void sendPosition() {
/*  275 */     sendIsSprintingIfNeeded();
/*  277 */     if (isControlledCamera()) {
/*  278 */       double deltaX = getX() - this.xLast;
/*  279 */       double deltaY = getY() - this.yLast;
/*  280 */       double deltaZ = getZ() - this.zLast;
/*  282 */       double deltaYRot = (getYRot() - this.yRotLast);
/*  283 */       double deltaXRot = (getXRot() - this.xRotLast);
/*  285 */       this.positionReminder++;
/*  287 */       boolean move = (Mth.lengthSquared(deltaX, deltaY, deltaZ) > Mth.square(2.0E-4D) || this.positionReminder >= 20);
/*  288 */       boolean rot = (deltaYRot != 0.0D || deltaXRot != 0.0D);
/*  290 */       if (move && rot) {
/*  291 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.PosRot(position(), getYRot(), getXRot(), onGround(), this.horizontalCollision));
/*  292 */       } else if (move) {
/*  293 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.Pos(position(), onGround(), this.horizontalCollision));
/*  294 */       } else if (rot) {
/*  295 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.Rot(getYRot(), getXRot(), onGround(), this.horizontalCollision));
/*  296 */       } else if (this.lastOnGround != onGround() || this.lastHorizontalCollision != this.horizontalCollision) {
/*  297 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.StatusOnly(onGround(), this.horizontalCollision));
/*      */       } 
/*  300 */       if (move) {
/*  301 */         this.xLast = getX();
/*  302 */         this.yLast = getY();
/*  303 */         this.zLast = getZ();
/*  304 */         this.positionReminder = 0;
/*      */       } 
/*  306 */       if (rot) {
/*  307 */         this.yRotLast = getYRot();
/*  308 */         this.xRotLast = getXRot();
/*      */       } 
/*  310 */       this.lastOnGround = onGround();
/*  311 */       this.lastHorizontalCollision = this.horizontalCollision;
/*  313 */       this.autoJumpEnabled = ((Boolean)this.minecraft.options.autoJump().get()).booleanValue();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void sendIsSprintingIfNeeded() {
/*  318 */     boolean isSprinting = isSprinting();
/*  319 */     if (isSprinting != this.wasSprinting) {
/*  320 */       ServerboundPlayerCommandPacket.Action action = isSprinting ? ServerboundPlayerCommandPacket.Action.START_SPRINTING : ServerboundPlayerCommandPacket.Action.STOP_SPRINTING;
/*  321 */       this.connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this, action));
/*  322 */       this.wasSprinting = isSprinting;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean drop(boolean all) {
/*  327 */     ServerboundPlayerActionPacket.Action action = all ? ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS : ServerboundPlayerActionPacket.Action.DROP_ITEM;
/*  328 */     ItemStack prediction = getInventory().removeFromSelected(all);
/*  329 */     this.connection.send((Packet)new ServerboundPlayerActionPacket(action, BlockPos.ZERO, Direction.DOWN));
/*  330 */     return !prediction.isEmpty();
/*      */   }
/*      */   
/*      */   public void swing(InteractionHand hand) {
/*  335 */     super.swing(hand);
/*  336 */     this.connection.send((Packet)new ServerboundSwingPacket(hand));
/*      */   }
/*      */   
/*      */   public void respawn() {
/*  340 */     this.connection.send((Packet)new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
/*  341 */     KeyMapping.resetToggleKeys();
/*      */   }
/*      */   
/*      */   public void closeContainer() {
/*  346 */     this.connection.send((Packet)new ServerboundContainerClosePacket(this.containerMenu.containerId));
/*  347 */     clientSideCloseContainer();
/*      */   }
/*      */   
/*      */   public void clientSideCloseContainer() {
/*  352 */     super.closeContainer();
/*  353 */     this.minecraft.gui.setScreen(null);
/*      */   }
/*      */   
/*      */   public void hurtTo(float newHealth) {
/*  357 */     if (this.flashOnSetHealth) {
/*  358 */       float dmg = getHealth() - newHealth;
/*  359 */       if (dmg <= 0.0F) {
/*  360 */         setHealth(newHealth);
/*  361 */         if (dmg < 0.0F)
/*  362 */           this.invulnerableTime = 10; 
/*      */       } else {
/*  365 */         this.lastHurt = dmg;
/*  366 */         this.invulnerableTime = 20;
/*  367 */         setHealth(newHealth);
/*  368 */         this.hurtDuration = 10;
/*  369 */         this.hurtTime = this.hurtDuration;
/*      */       } 
/*      */     } else {
/*  372 */       setHealth(newHealth);
/*  373 */       this.flashOnSetHealth = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onUpdateAbilities() {
/*  379 */     this.connection.send((Packet)new ServerboundPlayerAbilitiesPacket(getAbilities()));
/*      */   }
/*      */   
/*      */   public void setReducedDebugInfo(boolean reducedDebugInfo) {
/*  384 */     super.setReducedDebugInfo(reducedDebugInfo);
/*  385 */     this.minecraft.debugEntries.rebuildCurrentList();
/*      */   }
/*      */   
/*      */   public boolean isLocalPlayer() {
/*  390 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isSuppressingSlidingDownLadder() {
/*  395 */     return (!(getAbilities()).flying && super.isSuppressingSlidingDownLadder());
/*      */   }
/*      */   
/*      */   public boolean canSpawnSprintParticle() {
/*  400 */     return (!(getAbilities()).flying && super.canSpawnSprintParticle());
/*      */   }
/*      */   
/*      */   protected void sendRidingJump() {
/*  404 */     this.connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this, ServerboundPlayerCommandPacket.Action.START_RIDING_JUMP, Mth.floor(getJumpRidingScale() * 100.0F)));
/*      */   }
/*      */   
/*      */   public void sendOpenInventory() {
/*  408 */     this.connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this, ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY));
/*      */   }
/*      */   
/*      */   public StatsCounter getStats() {
/*  412 */     return this.stats;
/*      */   }
/*      */   
/*      */   public ClientRecipeBook getRecipeBook() {
/*  416 */     return this.recipeBook;
/*      */   }
/*      */   
/*      */   public void removeRecipeHighlight(RecipeDisplayId recipe) {
/*  420 */     if (this.recipeBook.willHighlight(recipe)) {
/*  421 */       this.recipeBook.removeHighlight(recipe);
/*  422 */       this.connection.send((Packet)new ServerboundRecipeBookSeenRecipePacket(recipe));
/*      */     } 
/*      */   }
/*      */   
/*      */   public PermissionSet permissions() {
/*  428 */     return this.permissions;
/*      */   }
/*      */   
/*      */   public void setPermissions(PermissionSet newPermissions) {
/*  432 */     boolean previousGamemasterPermission = this.permissions.hasPermission(Permissions.COMMANDS_GAMEMASTER);
/*  433 */     boolean newGamemasterPermission = newPermissions.hasPermission(Permissions.COMMANDS_GAMEMASTER);
/*  434 */     this.permissions = newPermissions;
/*  436 */     if (previousGamemasterPermission != newGamemasterPermission) {
/*  437 */       Screen screen = this.minecraft.gui.screen();
/*  437 */       if (screen instanceof HasGamemasterPermissionReaction) {
/*  437 */         HasGamemasterPermissionReaction hasGamemasterPermissionReaction = (HasGamemasterPermissionReaction)screen;
/*  438 */         hasGamemasterPermissionReaction.onGamemasterPermissionChanged(newGamemasterPermission);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public ChatAbilities chatAbilities() {
/*  444 */     return this.chatAbilities;
/*      */   }
/*      */   
/*      */   public void refreshChatAbilities() {
/*  448 */     this.chatAbilities = this.minecraft.computeChatAbilities();
/*  449 */     this.minecraft.gui.hud.getChat().setVisibleMessageFilter(this.chatAbilities.visibleMessagesFilter());
/*      */   }
/*      */   
/*      */   public void sendSystemMessage(Component message) {
/*  455 */     this.minecraft.gui.chatListener().handleSystemMessage(message, true);
/*      */   }
/*      */   
/*      */   public void sendOverlayMessage(Component message) {
/*  460 */     this.minecraft.gui.chatListener().handleOverlay(message);
/*      */   }
/*      */   
/*      */   private void moveTowardsClosestSpace(double x, double z) {
/*  464 */     BlockPos pos = BlockPos.containing(x, getY(), z);
/*  466 */     if (!suffocatesAt(pos))
/*      */       return; 
/*  470 */     double xd = x - pos.getX();
/*  471 */     double zd = z - pos.getZ();
/*  473 */     Direction dir = null;
/*  474 */     double closest = Double.MAX_VALUE;
/*  476 */     Direction[] directions = { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH };
/*  477 */     for (Direction direction : directions) {
/*  478 */       double axisDistance = direction.getAxis().choose(xd, 0.0D, zd);
/*  479 */       double distanceToEdge = (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (1.0D - axisDistance) : axisDistance;
/*  480 */       if (distanceToEdge < closest && !suffocatesAt(pos.relative(direction))) {
/*  481 */         closest = distanceToEdge;
/*  482 */         dir = direction;
/*      */       } 
/*      */     } 
/*  486 */     if (dir != null) {
/*  488 */       Vec3 oldMovement = getDeltaMovement();
/*  489 */       if (dir.getAxis() == Direction.Axis.X) {
/*  490 */         setDeltaMovement(0.1D * dir.getStepX(), oldMovement.y, oldMovement.z);
/*      */       } else {
/*  492 */         setDeltaMovement(oldMovement.x, oldMovement.y, 0.1D * dir.getStepZ());
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean suffocatesAt(BlockPos pos) {
/*  499 */     AABB boundingBox = getBoundingBox();
/*  500 */     AABB testArea = (new AABB(pos.getX(), boundingBox.minY, pos.getZ(), pos.getX() + 1.0D, boundingBox.maxY, pos.getZ() + 1.0D)).deflate(1.0E-7D);
/*  501 */     return level().collidesWithSuffocatingBlock((Entity)this, testArea);
/*      */   }
/*      */   
/*      */   public void setExperienceValues(float experienceProgress, int totalExp, int experienceLevel) {
/*  505 */     if (experienceProgress != this.experienceProgress)
/*  506 */       setExperienceDisplayStartTickToTickCount(); 
/*  508 */     this.experienceProgress = experienceProgress;
/*  509 */     this.totalExperience = totalExp;
/*  510 */     this.experienceLevel = experienceLevel;
/*      */   }
/*      */   
/*      */   private void setExperienceDisplayStartTickToTickCount() {
/*  515 */     if (this.experienceDisplayStartTick == Integer.MIN_VALUE) {
/*  516 */       this.experienceDisplayStartTick = -2147483647;
/*      */     } else {
/*  518 */       this.experienceDisplayStartTick = this.tickCount;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  524 */     switch (id) {
/*      */       case 24:
/*  525 */         setPermissions(PermissionSet.NO_PERMISSIONS);
/*      */         return;
/*      */       case 25:
/*  526 */         setPermissions((PermissionSet)LevelBasedPermissionSet.MODERATOR);
/*      */         return;
/*      */       case 26:
/*  527 */         setPermissions((PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/*      */         return;
/*      */       case 27:
/*  528 */         setPermissions((PermissionSet)LevelBasedPermissionSet.ADMIN);
/*      */         return;
/*      */       case 28:
/*  529 */         setPermissions((PermissionSet)LevelBasedPermissionSet.OWNER);
/*      */         return;
/*      */     } 
/*  530 */     super.handleEntityEvent(id);
/*      */   }
/*      */   
/*      */   public void setShowDeathScreen(boolean show) {
/*  535 */     this.showDeathScreen = show;
/*      */   }
/*      */   
/*      */   public boolean shouldShowDeathScreen() {
/*  539 */     return this.showDeathScreen;
/*      */   }
/*      */   
/*      */   public void setDoLimitedCrafting(boolean value) {
/*  543 */     this.doLimitedCrafting = value;
/*      */   }
/*      */   
/*      */   public boolean getDoLimitedCrafting() {
/*  547 */     return this.doLimitedCrafting;
/*      */   }
/*      */   
/*      */   public void playSound(SoundEvent sound, float volume, float pitch) {
/*  552 */     level().playLocalSound(getX(), getY(), getZ(), sound, getSoundSource(), volume, pitch, false);
/*      */   }
/*      */   
/*      */   public void startUsingItem(InteractionHand hand) {
/*  557 */     ItemStack itemStack = getItemInHand(hand);
/*  558 */     if (itemStack.isEmpty() || isUsingItem())
/*      */       return; 
/*  562 */     super.startUsingItem(hand);
/*  564 */     this.startedUsingItem = true;
/*  565 */     this.usingItemHand = hand;
/*      */   }
/*      */   
/*      */   public boolean isUsingItem() {
/*  570 */     return this.startedUsingItem;
/*      */   }
/*      */   
/*      */   private boolean isSlowDueToUsingItem() {
/*  574 */     return (isUsingItem() && !((UseEffects)this.useItem.getOrDefault(DataComponents.USE_EFFECTS, UseEffects.DEFAULT)).canSprint());
/*      */   }
/*      */   
/*      */   private float itemUseSpeedMultiplier() {
/*  578 */     return ((UseEffects)this.useItem.getOrDefault(DataComponents.USE_EFFECTS, UseEffects.DEFAULT)).speedMultiplier();
/*      */   }
/*      */   
/*      */   public void stopUsingItem() {
/*  583 */     super.stopUsingItem();
/*  584 */     this.startedUsingItem = false;
/*      */   }
/*      */   
/*      */   public InteractionHand getUsedItemHand() {
/*  590 */     return Objects.<InteractionHand>requireNonNullElse(this.usingItemHand, InteractionHand.MAIN_HAND);
/*      */   }
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  595 */     super.onSyncedDataUpdated(accessor);
/*  597 */     if (DATA_LIVING_ENTITY_FLAGS.equals(accessor)) {
/*  598 */       boolean serverUsingItem = ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x1) > 0);
/*  599 */       InteractionHand serverUsingHand = ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x2) > 0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
/*  601 */       if (serverUsingItem && !this.startedUsingItem) {
/*  602 */         startUsingItem(serverUsingHand);
/*  603 */       } else if (!serverUsingItem && this.startedUsingItem) {
/*  604 */         stopUsingItem();
/*      */       } 
/*      */     } 
/*  607 */     if (DATA_SHARED_FLAGS_ID.equals(accessor) && 
/*  608 */       isFallFlying() && !this.wasFallFlying)
/*  609 */       this.minecraft.getSoundManager().play((SoundInstance)new ElytraOnPlayerSoundInstance(this)); 
/*      */   }
/*      */   
/*      */   public PlayerRideableJumping jumpableVehicle() {
/*  615 */     Entity entity = getControlledVehicle();
/*  615 */     if (entity instanceof PlayerRideableJumping) {
/*  615 */       PlayerRideableJumping playerRideableJumping = (PlayerRideableJumping)entity;
/*  615 */       if (playerRideableJumping.canJump());
/*      */     } 
/*  615 */     return null;
/*      */   }
/*      */   
/*      */   public float getJumpRidingScale() {
/*  619 */     return this.jumpRidingScale;
/*      */   }
/*      */   
/*      */   public boolean isTextFilteringEnabled() {
/*  624 */     return this.minecraft.isTextFilteringEnabled();
/*      */   }
/*      */   
/*      */   public void openTextEdit(SignBlockEntity sign, boolean isFrontText) {
/*  629 */     if (sign instanceof HangingSignBlockEntity) {
/*  629 */       HangingSignBlockEntity hangingSign = (HangingSignBlockEntity)sign;
/*  630 */       this.minecraft.gui.setScreen((Screen)new HangingSignEditScreen((SignBlockEntity)hangingSign, isFrontText, this.minecraft.isTextFilteringEnabled()));
/*      */     } else {
/*  632 */       this.minecraft.gui.setScreen((Screen)new SignEditScreen(sign, isFrontText, this.minecraft.isTextFilteringEnabled()));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void openMinecartCommandBlock(MinecartCommandBlock commandBlock) {
/*  638 */     this.minecraft.gui.setScreen((Screen)new MinecartCommandBlockEditScreen(commandBlock));
/*      */   }
/*      */   
/*      */   public void openCommandBlock(CommandBlockEntity commandBlock) {
/*  643 */     this.minecraft.gui.setScreen((Screen)new CommandBlockEditScreen(commandBlock));
/*      */   }
/*      */   
/*      */   public void openStructureBlock(StructureBlockEntity structureBlock) {
/*  648 */     this.minecraft.gui.setScreen((Screen)new StructureBlockEditScreen(structureBlock));
/*      */   }
/*      */   
/*      */   public void openTestBlock(TestBlockEntity testBlock) {
/*  653 */     this.minecraft.gui.setScreen((Screen)new TestBlockEditScreen(testBlock));
/*      */   }
/*      */   
/*      */   public void openTestInstanceBlock(TestInstanceBlockEntity testInstanceBlock) {
/*  658 */     this.minecraft.gui.setScreen((Screen)new TestInstanceBlockEditScreen(testInstanceBlock));
/*      */   }
/*      */   
/*      */   public void openJigsawBlock(JigsawBlockEntity jigsawBlock) {
/*  663 */     this.minecraft.gui.setScreen((Screen)new JigsawBlockEditScreen(jigsawBlock));
/*      */   }
/*      */   
/*      */   public void openDialog(Holder<Dialog> dialog) {
/*  668 */     this.connection.showDialog(dialog, this.minecraft.gui.screen());
/*      */   }
/*      */   
/*      */   public void openItemGui(ItemStack itemStack, InteractionHand hand) {
/*  673 */     WritableBookContent content = (WritableBookContent)itemStack.get(DataComponents.WRITABLE_BOOK_CONTENT);
/*  674 */     if (content != null)
/*  675 */       this.minecraft.gui.setScreen((Screen)new BookEditScreen(this, itemStack, hand, content)); 
/*      */   }
/*      */   
/*      */   public void crit(Entity entity) {
/*  681 */     this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.CRIT);
/*      */   }
/*      */   
/*      */   public void magicCrit(Entity entity) {
/*  686 */     this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.ENCHANTED_HIT);
/*      */   }
/*      */   
/*      */   public boolean isShiftKeyDown() {
/*  691 */     return this.input.keyPresses.shift();
/*      */   }
/*      */   
/*      */   public boolean isCrouching() {
/*  696 */     return this.crouching;
/*      */   }
/*      */   
/*      */   public boolean isMovingSlowly() {
/*  700 */     return (isCrouching() || isVisuallyCrawling());
/*      */   }
/*      */   
/*      */   public void applyInput() {
/*  705 */     if (isControlledCamera()) {
/*  706 */       Vec2 modifiedInput = modifyInput(this.input.getMoveVector());
/*  707 */       this.xxa = modifiedInput.x;
/*  708 */       this.zza = modifiedInput.y;
/*  709 */       this.jumping = this.input.keyPresses.jump();
/*  710 */       this.yBobO = this.yBob;
/*  711 */       this.xBobO = this.xBob;
/*  712 */       this.xBob += (getXRot() - this.xBob) * 0.5F;
/*  713 */       this.yBob += (getYRot() - this.yBob) * 0.5F;
/*      */     } else {
/*  715 */       super.applyInput();
/*      */     } 
/*      */   }
/*      */   
/*      */   private Vec2 modifyInput(Vec2 input) {
/*  729 */     if (input.lengthSquared() == 0.0F)
/*  730 */       return input; 
/*  733 */     Vec2 newInput = input.scale(0.98F);
/*  734 */     if (isUsingItem() && !isPassenger())
/*  735 */       newInput = newInput.scale(itemUseSpeedMultiplier()); 
/*  738 */     if (isMovingSlowly()) {
/*  739 */       float sneakingMovementFactor = (float)getAttributeValue(Attributes.SNEAKING_SPEED);
/*  740 */       newInput = newInput.scale(sneakingMovementFactor);
/*      */     } 
/*  743 */     return modifyInputSpeedForSquareMovement(newInput);
/*      */   }
/*      */   
/*      */   private static Vec2 modifyInputSpeedForSquareMovement(Vec2 input) {
/*  747 */     float length = input.length();
/*  748 */     if (length <= 0.0F)
/*  749 */       return input; 
/*  752 */     Vec2 direction = input.scale(1.0F / length);
/*  753 */     float distanceToUnitSquare = distanceToUnitSquare(direction);
/*  755 */     float modifiedLength = Math.min(length * distanceToUnitSquare, 1.0F);
/*  756 */     return direction.scale(modifiedLength);
/*      */   }
/*      */   
/*      */   private static float distanceToUnitSquare(Vec2 direction) {
/*  760 */     float directionX = Math.abs(direction.x);
/*  761 */     float directionY = Math.abs(direction.y);
/*  762 */     float tan = (directionY > directionX) ? (directionX / directionY) : (directionY / directionX);
/*  763 */     return Mth.sqrt(1.0F + Mth.square(tan));
/*      */   }
/*      */   
/*      */   protected boolean isControlledCamera() {
/*  767 */     return (this.minecraft.getCameraEntity() == this);
/*      */   }
/*      */   
/*      */   public void resetPos() {
/*  771 */     setPose(Pose.STANDING);
/*  772 */     if (level() != null) {
/*  773 */       double testY = getY();
/*  774 */       while (testY > level().getMinY() && testY <= level().getMaxY()) {
/*  775 */         setPos(getX(), testY, getZ());
/*  776 */         if (level().noCollision((Entity)this))
/*      */           break; 
/*  779 */         testY++;
/*      */       } 
/*  781 */       setDeltaMovement(Vec3.ZERO);
/*  782 */       setXRot(0.0F);
/*      */     } 
/*  785 */     setHealth(getMaxHealth());
/*  786 */     this.deathTime = 0;
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*  791 */     if (this.sprintTriggerTime > 0)
/*  792 */       this.sprintTriggerTime--; 
/*  795 */     if (!(this.minecraft.gui.screen() instanceof net.minecraft.client.gui.screens.LevelLoadingScreen)) {
/*  796 */       handlePortalTransitionEffect((getActivePortalLocalTransition() == Portal.Transition.CONFUSION));
/*  797 */       processPortalCooldown();
/*      */     } 
/*  800 */     boolean wasJumping = this.input.keyPresses.jump();
/*  801 */     boolean wasShiftKeyDown = this.input.keyPresses.shift();
/*  802 */     boolean hasForwardImpulse = this.input.hasForwardImpulse();
/*  804 */     Abilities abilities = getAbilities();
/*  805 */     this
/*      */       
/*  810 */       .crouching = (!abilities.flying && !isSwimming() && !isPassenger() && canPlayerFitWithinBlocksAndEntitiesWhen(Pose.CROUCHING) && (isShiftKeyDown() || (!isSleeping() && !canPlayerFitWithinBlocksAndEntitiesWhen(Pose.STANDING))));
/*  812 */     this.input.tick();
/*  813 */     this.minecraft.getTutorial().onInput(this.input);
/*  815 */     boolean wasAutoJump = false;
/*  816 */     if (this.autoJumpTime > 0) {
/*  817 */       this.autoJumpTime--;
/*  818 */       wasAutoJump = true;
/*  819 */       this.input.makeJump();
/*      */     } 
/*  822 */     if (!this.noPhysics) {
/*  823 */       moveTowardsClosestSpace(getX() - getBbWidth() * 0.35D, getZ() + getBbWidth() * 0.35D);
/*  824 */       moveTowardsClosestSpace(getX() - getBbWidth() * 0.35D, getZ() - getBbWidth() * 0.35D);
/*  825 */       moveTowardsClosestSpace(getX() + getBbWidth() * 0.35D, getZ() - getBbWidth() * 0.35D);
/*  826 */       moveTowardsClosestSpace(getX() + getBbWidth() * 0.35D, getZ() + getBbWidth() * 0.35D);
/*      */     } 
/*  829 */     if (wasShiftKeyDown || (isSlowDueToUsingItem() && !isPassenger()) || this.input.keyPresses.backward())
/*  830 */       this.sprintTriggerTime = 0; 
/*  833 */     if (canStartSprinting()) {
/*  834 */       if (!hasForwardImpulse)
/*  835 */         if (this.sprintTriggerTime > 0) {
/*  836 */           setSprinting(true);
/*      */         } else {
/*  838 */           this.sprintTriggerTime = ((Integer)this.minecraft.options.sprintWindow().get()).intValue();
/*      */         }  
/*  842 */       if (this.input.keyPresses.sprint())
/*  843 */         setSprinting(true); 
/*      */     } 
/*  847 */     if (isSprinting())
/*  848 */       if (isSwimming()) {
/*  849 */         if (shouldStopSwimSprinting())
/*  850 */           setSprinting(false); 
/*  852 */       } else if (shouldStopRunSprinting()) {
/*  853 */         setSprinting(false);
/*      */       }  
/*  857 */     boolean justToggledCreativeFlight = false;
/*  858 */     if (abilities.mayfly)
/*  859 */       if (this.minecraft.gameMode.isSpectator()) {
/*  860 */         if (!abilities.flying) {
/*  861 */           abilities.flying = true;
/*  862 */           justToggledCreativeFlight = true;
/*  863 */           onUpdateAbilities();
/*      */         } 
/*  865 */       } else if (!wasJumping && this.input.keyPresses.jump() && !wasAutoJump) {
/*  866 */         if (this.jumpTriggerTime == 0) {
/*  867 */           this.jumpTriggerTime = 7;
/*  868 */         } else if (!isSwimming() && (getVehicle() == null || jumpableVehicle() != null)) {
/*  869 */           abilities.flying = !abilities.flying;
/*  871 */           if (abilities.flying && onGround())
/*  872 */             jumpFromGround(); 
/*  874 */           justToggledCreativeFlight = true;
/*  875 */           onUpdateAbilities();
/*  876 */           this.jumpTriggerTime = 0;
/*      */         } 
/*      */       }  
/*  881 */     if (this.input.keyPresses.jump() && !justToggledCreativeFlight && !wasJumping && !onClimbable() && 
/*  882 */       tryToStartFallFlying())
/*  883 */       this.connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING)); 
/*  886 */     this.wasFallFlying = isFallFlying();
/*  888 */     if (isInWater() && this.input.keyPresses.shift() && isAffectedByFluids())
/*  889 */       goDownInWater(); 
/*  892 */     if (isEyeInFluid(FluidTags.WATER)) {
/*  893 */       int speed = isSpectator() ? 10 : 1;
/*  894 */       this.waterVisionTime = Mth.clamp(this.waterVisionTime + speed, 0, 600);
/*  895 */     } else if (this.waterVisionTime > 0) {
/*  896 */       isEyeInFluid(FluidTags.WATER);
/*  897 */       this.waterVisionTime = Mth.clamp(this.waterVisionTime - 10, 0, 600);
/*      */     } 
/*  900 */     if (abilities.flying && isControlledCamera()) {
/*  902 */       int inputYa = 0;
/*  904 */       if (this.input.keyPresses.shift())
/*  905 */         inputYa--; 
/*  907 */       if (this.input.keyPresses.jump())
/*  908 */         inputYa++; 
/*  911 */       if (inputYa != 0)
/*  912 */         setDeltaMovement(getDeltaMovement().add(0.0D, (inputYa * abilities.getFlyingSpeed() * 3.0F), 0.0D)); 
/*      */     } 
/*  916 */     PlayerRideableJumping jumpableVehicle = jumpableVehicle();
/*  917 */     if (jumpableVehicle != null && jumpableVehicle.getJumpCooldown() == 0) {
/*  918 */       if (this.jumpRidingTicks < 0) {
/*  919 */         this.jumpRidingTicks++;
/*  920 */         if (this.jumpRidingTicks == 0)
/*  922 */           this.jumpRidingScale = 0.0F; 
/*      */       } 
/*  925 */       if (wasJumping && !this.input.keyPresses.jump()) {
/*  927 */         this.jumpRidingTicks = -10;
/*  928 */         jumpableVehicle.onPlayerJump(Mth.floor(getJumpRidingScale() * 100.0F));
/*  929 */         sendRidingJump();
/*  930 */       } else if (!wasJumping && this.input.keyPresses.jump()) {
/*  932 */         this.jumpRidingTicks = 0;
/*  933 */         this.jumpRidingScale = 0.0F;
/*  934 */       } else if (wasJumping) {
/*  936 */         this.jumpRidingTicks++;
/*  937 */         if (this.jumpRidingTicks < 10) {
/*  938 */           this.jumpRidingScale = this.jumpRidingTicks * 0.1F;
/*      */         } else {
/*  940 */           this.jumpRidingScale = 0.8F + 2.0F / (this.jumpRidingTicks - 9) * 0.1F;
/*      */         } 
/*      */       } 
/*      */     } else {
/*  944 */       this.jumpRidingScale = 0.0F;
/*      */     } 
/*  947 */     super.aiStep();
/*  948 */     if (onGround() && abilities.flying && !this.minecraft.gameMode.isSpectator()) {
/*  949 */       abilities.flying = false;
/*  950 */       onUpdateAbilities();
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean shouldStopRunSprinting() {
/*  955 */     return (!isSprintingPossible((getAbilities()).flying) || 
/*  956 */       !this.input.hasForwardImpulse() || (this.horizontalCollision && !this.minorHorizontalCollision));
/*      */   }
/*      */   
/*      */   private boolean shouldStopSwimSprinting() {
/*  961 */     return (!isSprintingPossible(true) || 
/*  962 */       !isInWater() || (
/*  963 */       !this.input.hasForwardImpulse() && !onGround() && !this.input.keyPresses.shift()));
/*      */   }
/*      */   
/*      */   public Portal.Transition getActivePortalLocalTransition() {
/*  967 */     return (this.portalProcess == null) ? Portal.Transition.NONE : this.portalProcess.getPortalLocalTransition();
/*      */   }
/*      */   
/*      */   protected void tickDeath() {
/*  973 */     this.deathTime++;
/*  974 */     if (this.deathTime == 20)
/*  975 */       remove(Entity.RemovalReason.KILLED); 
/*      */   }
/*      */   
/*      */   private void handlePortalTransitionEffect(boolean active) {
/*  980 */     this.oPortalEffectIntensity = this.portalEffectIntensity;
/*  981 */     float step = 0.0F;
/*  983 */     if (active && this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
/*  984 */       if (this.minecraft.gui.screen() != null && !this.minecraft.gui.screen().isAllowedInPortal()) {
/*  985 */         if (this.minecraft.gui.screen() instanceof net.minecraft.client.gui.screens.inventory.AbstractContainerScreen)
/*  986 */           closeContainer(); 
/*  988 */         this.minecraft.gui.setScreen(null);
/*      */       } 
/*  991 */       if (this.portalEffectIntensity == 0.0F)
/*  992 */         this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRIGGER, this.random.nextFloat() * 0.4F + 0.8F, 0.25F)); 
/*  994 */       step = 0.0125F;
/*  995 */       this.portalProcess.setAsInsidePortalThisTick(false);
/*  996 */     } else if (this.portalEffectIntensity > 0.0F) {
/*  997 */       step = -0.05F;
/*      */     } 
/* 1000 */     this.portalEffectIntensity = Mth.clamp(this.portalEffectIntensity + step, 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public void rideTick() {
/* 1005 */     super.rideTick();
/* 1006 */     this.handsBusy = false;
/* 1008 */     Entity entity = getControlledVehicle();
/* 1008 */     if (entity instanceof AbstractBoat) {
/* 1008 */       AbstractBoat boat = (AbstractBoat)entity;
/* 1009 */       boat.setInput(this.input.keyPresses.left(), this.input.keyPresses.right(), this.input.keyPresses.forward(), this.input.keyPresses.backward());
/* 1011 */       this.handsBusy |= (this.input.keyPresses.left() || this.input.keyPresses.right() || this.input.keyPresses.forward() || this.input.keyPresses.backward()) ? 1 : 0;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isHandsBusy() {
/* 1016 */     return this.handsBusy;
/*      */   }
/*      */   
/*      */   public void move(MoverType moverType, Vec3 delta) {
/* 1021 */     double prevX = getX();
/* 1022 */     double prevZ = getZ();
/* 1023 */     super.move(moverType, delta);
/* 1025 */     float deltaX = (float)(getX() - prevX);
/* 1026 */     float deltaZ = (float)(getZ() - prevZ);
/* 1027 */     updateAutoJump(deltaX, deltaZ);
/* 1029 */     addWalkedDistance(Mth.length(deltaX, deltaZ) * 0.6F);
/*      */   }
/*      */   
/*      */   public boolean isAutoJumpEnabled() {
/* 1033 */     return this.autoJumpEnabled;
/*      */   }
/*      */   
/*      */   public boolean shouldRotateWithMinecart() {
/* 1038 */     return ((Boolean)this.minecraft.options.rotateWithMinecart().get()).booleanValue();
/*      */   }
/*      */   
/*      */   protected void updateAutoJump(float xa, float za) {
/* 1042 */     if (!canAutoJump())
/*      */       return; 
/* 1077 */     Vec3 moveBegin = position();
/* 1078 */     Vec3 moveEnd = moveBegin.add(xa, 0.0D, za);
/* 1079 */     Vec3 moveDiff = new Vec3(xa, 0.0D, za);
/* 1082 */     float currentSpeed = getSpeed();
/* 1083 */     float moveDistSq = (float)moveDiff.lengthSqr();
/* 1084 */     if (moveDistSq <= 0.001F) {
/* 1089 */       Vec2 move = this.input.getMoveVector();
/* 1090 */       float inputXa = currentSpeed * move.x;
/* 1091 */       float inputZa = currentSpeed * move.y;
/* 1094 */       float sin = Mth.sin((getYRot() * 0.017453292F));
/* 1095 */       float cos = Mth.cos((getYRot() * 0.017453292F));
/* 1096 */       moveDiff = new Vec3((inputXa * cos - inputZa * sin), moveDiff.y, (inputZa * cos + inputXa * sin));
/* 1098 */       moveDistSq = (float)moveDiff.lengthSqr();
/* 1101 */       if (moveDistSq <= 0.001F)
/*      */         return; 
/*      */     } 
/* 1107 */     float moveDistInverted = Mth.invSqrt(moveDistSq);
/* 1108 */     Vec3 moveDir = moveDiff.scale(moveDistInverted);
/* 1112 */     Vec3 facingDir3 = getForward();
/* 1113 */     float facingVsMovingDotProduct2 = (float)(facingDir3.x * moveDir.x + facingDir3.z * moveDir.z);
/* 1114 */     if (facingVsMovingDotProduct2 < -0.15F)
/*      */       return; 
/* 1118 */     CollisionContext context = CollisionContext.of((Entity)this);
/* 1123 */     BlockPos ceilingPos = BlockPos.containing(getX(), (getBoundingBox()).maxY, getZ());
/* 1124 */     BlockState aboveBlock1 = level().getBlockState(ceilingPos);
/* 1125 */     if (!aboveBlock1.getCollisionShape((BlockGetter)level(), ceilingPos, context).isEmpty())
/*      */       return; 
/* 1129 */     ceilingPos = ceilingPos.above();
/* 1130 */     BlockState aboveBlock2 = level().getBlockState(ceilingPos);
/* 1131 */     if (!aboveBlock2.getCollisionShape((BlockGetter)level(), ceilingPos, context).isEmpty())
/*      */       return; 
/* 1142 */     float lookAheadSteps = 7.0F;
/* 1143 */     float jumpHeight = 1.2F;
/* 1144 */     if (hasEffect(MobEffects.JUMP_BOOST))
/* 1145 */       jumpHeight += (getEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75F; 
/* 1149 */     float lookAheadDist = Math.max(currentSpeed * 7.0F, 1.0F / moveDistInverted);
/* 1152 */     Vec3 segBegin = moveBegin;
/* 1153 */     Vec3 segEnd = moveEnd.add(moveDir.scale(lookAheadDist));
/* 1157 */     float playerWidth = getBbWidth();
/* 1158 */     float playerHeight = getBbHeight();
/* 1159 */     AABB testBox = (new AABB(segBegin, segEnd.add(0.0D, playerHeight, 0.0D))).inflate(playerWidth, 0.0D, playerWidth);
/* 1163 */     segBegin = segBegin.add(0.0D, 0.5099999904632568D, 0.0D);
/* 1164 */     segEnd = segEnd.add(0.0D, 0.5099999904632568D, 0.0D);
/* 1168 */     Vec3 rightDir = moveDir.cross(new Vec3(0.0D, 1.0D, 0.0D));
/* 1169 */     Vec3 rightOffset = rightDir.scale((playerWidth * 0.5F));
/* 1172 */     Vec3 leftSegBegin = segBegin.subtract(rightOffset);
/* 1173 */     Vec3 leftSegEnd = segEnd.subtract(rightOffset);
/* 1174 */     Vec3 rightSegBegin = segBegin.add(rightOffset);
/* 1175 */     Vec3 rightSegEnd = segEnd.add(rightOffset);
/* 1178 */     Iterable<VoxelShape> collisions = level().getCollisions((Entity)this, testBox);
/* 1179 */     Iterator<AABB> shape = StreamSupport.stream(collisions.spliterator(), false).flatMap(s -> s.toAabbs().stream()).iterator();
/* 1184 */     float obstacleHeight = Float.MIN_VALUE;
/* 1186 */     while (shape.hasNext()) {
/* 1187 */       AABB box = shape.next();
/* 1188 */       if (!box.intersects(leftSegBegin, leftSegEnd) && !box.intersects(rightSegBegin, rightSegEnd))
/*      */         continue; 
/* 1195 */       obstacleHeight = (float)box.maxY;
/* 1196 */       Vec3 obstacleShapeCenter = box.getCenter();
/* 1197 */       BlockPos obstacleBlockPos = BlockPos.containing((Position)obstacleShapeCenter);
/* 1200 */       for (int steps = 1; steps < jumpHeight; steps++) {
/* 1201 */         BlockPos abovePos1 = obstacleBlockPos.above(steps);
/* 1202 */         BlockState aboveBlock = level().getBlockState(abovePos1);
/*      */         VoxelShape blockShape;
/* 1203 */         if (!(blockShape = aboveBlock.getCollisionShape((BlockGetter)level(), abovePos1, context)).isEmpty()) {
/* 1204 */           obstacleHeight = (float)blockShape.max(Direction.Axis.Y) + abovePos1.getY();
/* 1205 */           if (obstacleHeight - getY() > jumpHeight)
/*      */             return; 
/*      */         } 
/* 1209 */         if (steps > 1) {
/* 1210 */           ceilingPos = ceilingPos.above();
/* 1211 */           BlockState aboveBlock3 = level().getBlockState(ceilingPos);
/* 1212 */           if (!aboveBlock3.getCollisionShape((BlockGetter)level(), ceilingPos, context).isEmpty())
/*      */             return; 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1223 */     if (obstacleHeight == Float.MIN_VALUE)
/*      */       return; 
/* 1232 */     float ydelta = (float)(obstacleHeight - getY());
/* 1233 */     if (ydelta <= 0.5F || ydelta > jumpHeight)
/*      */       return; 
/* 1238 */     this.autoJumpTime = 1;
/*      */   }
/*      */   
/*      */   protected boolean isHorizontalCollisionMinor(Vec3 movement) {
/* 1243 */     float yRotInRadians = getYRot() * 0.017453292F;
/* 1244 */     double yRotSin = Mth.sin(yRotInRadians);
/* 1245 */     double yRotCos = Mth.cos(yRotInRadians);
/* 1246 */     double globalXA = this.xxa * yRotCos - this.zza * yRotSin;
/* 1247 */     double globalZA = this.zza * yRotCos + this.xxa * yRotSin;
/* 1248 */     double aLengthSquared = Mth.square(globalXA) + Mth.square(globalZA);
/* 1249 */     double movementLengthSquared = Mth.square(movement.x) + Mth.square(movement.z);
/* 1250 */     if (aLengthSquared < 9.999999747378752E-6D || movementLengthSquared < 9.999999747378752E-6D)
/* 1251 */       return false; 
/* 1253 */     double dotProduct = globalXA * movement.x + globalZA * movement.z;
/* 1254 */     double angleBetweenDesiredAndActualMovement = Math.acos(dotProduct / Math.sqrt(aLengthSquared * movementLengthSquared));
/* 1255 */     return (angleBetweenDesiredAndActualMovement < 0.13962633907794952D);
/*      */   }
/*      */   
/*      */   private boolean canAutoJump() {
/* 1259 */     return (isAutoJumpEnabled() && this.autoJumpTime <= 0 && 
/*      */       
/* 1261 */       onGround() && 
/* 1262 */       !isStayingOnGroundSurface() && 
/* 1263 */       !isPassenger() && 
/* 1264 */       isMoving() && 
/* 1265 */       getBlockJumpFactor() >= 1.0D);
/*      */   }
/*      */   
/*      */   private boolean isMoving() {
/* 1269 */     return (this.input.getMoveVector().lengthSquared() > 0.0F);
/*      */   }
/*      */   
/*      */   private boolean isSprintingPossible(boolean allowedInShallowWater) {
/* 1273 */     return (!isMobilityRestricted() && (
/* 1274 */       isPassenger() ? vehicleCanSprint(getVehicle()) : hasEnoughFoodToDoExhaustiveManoeuvres()) && (allowedInShallowWater || 
/* 1275 */       !isInShallowWater()));
/*      */   }
/*      */   
/*      */   private boolean canStartSprinting() {
/* 1279 */     return (!isSprinting() && this.input
/* 1280 */       .hasForwardImpulse() && 
/* 1281 */       isSprintingPossible((getAbilities()).flying) && 
/* 1282 */       !isSlowDueToUsingItem() && (
/* 1283 */       !isFallFlying() || isUnderWater()) && (
/* 1284 */       !isMovingSlowly() || isUnderWater()));
/*      */   }
/*      */   
/*      */   private boolean vehicleCanSprint(Entity vehicle) {
/* 1288 */     return (vehicle.canSprint() && vehicle.isLocalInstanceAuthoritative());
/*      */   }
/*      */   
/*      */   public float getWaterVision() {
/* 1292 */     if (!isEyeInFluid(FluidTags.WATER))
/* 1293 */       return 0.0F; 
/* 1295 */     float max = 600.0F;
/* 1296 */     float mid = 100.0F;
/* 1297 */     if (this.waterVisionTime >= 600.0F)
/* 1298 */       return 1.0F; 
/* 1300 */     float a = Mth.clamp(this.waterVisionTime / 100.0F, 0.0F, 1.0F);
/* 1301 */     float b = (this.waterVisionTime < 100.0F) ? 0.0F : Mth.clamp((this.waterVisionTime - 100.0F) / 500.0F, 0.0F, 1.0F);
/* 1302 */     return a * 0.6F + b * 0.39999998F;
/*      */   }
/*      */   
/*      */   public void onGameModeChanged(GameType gameType) {
/* 1306 */     if (gameType == GameType.SPECTATOR)
/* 1308 */       setDeltaMovement(getDeltaMovement().with(Direction.Axis.Y, 0.0D)); 
/*      */   }
/*      */   
/*      */   public boolean isUnderWater() {
/* 1314 */     return this.wasUnderwater;
/*      */   }
/*      */   
/*      */   protected boolean updateIsUnderwater() {
/* 1319 */     boolean oldIsUnderwater = this.wasUnderwater;
/* 1320 */     boolean newIsUnderwater = super.updateIsUnderwater();
/* 1322 */     if (isSpectator())
/* 1323 */       return this.wasUnderwater; 
/* 1326 */     if (!oldIsUnderwater && newIsUnderwater) {
/* 1327 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/* 1328 */       this.minecraft.getSoundManager().play((SoundInstance)new UnderwaterAmbientSoundInstances.UnderwaterAmbientSoundInstance(this));
/*      */     } 
/* 1331 */     if (oldIsUnderwater && !newIsUnderwater)
/* 1332 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.AMBIENT, 1.0F, 1.0F, false); 
/* 1335 */     return this.wasUnderwater;
/*      */   }
/*      */   
/*      */   public Vec3 getRopeHoldPosition(float partialTickTime) {
/* 1340 */     if (this.minecraft.options.getCameraType().isFirstPerson()) {
/* 1341 */       float yRot = Mth.lerp(partialTickTime * 0.5F, getYRot(), this.yRotO) * 0.017453292F;
/* 1342 */       float xRot = Mth.lerp(partialTickTime * 0.5F, getXRot(), this.xRotO) * 0.017453292F;
/* 1343 */       double handDir = (getMainArm() == HumanoidArm.RIGHT) ? -1.0D : 1.0D;
/* 1344 */       Vec3 offset = new Vec3(0.39D * handDir, -0.6D, 0.3D);
/* 1345 */       return offset.xRot(-xRot).yRot(-yRot).add(getEyePosition(partialTickTime));
/*      */     } 
/* 1347 */     return super.getRopeHoldPosition(partialTickTime);
/*      */   }
/*      */   
/*      */   public void updateTutorialInventoryAction(ItemStack itemCarried, ItemStack itemInSlot, ClickAction clickAction) {
/* 1352 */     this.minecraft.getTutorial().onInventoryAction(itemCarried, itemInSlot, clickAction);
/*      */   }
/*      */   
/*      */   public float getVisualRotationYInDegrees() {
/* 1357 */     return getYRot();
/*      */   }
/*      */   
/*      */   public void handleCreativeModeItemDrop(ItemStack stack) {
/* 1362 */     this.minecraft.gameMode.handleCreativeModeItemDrop(stack);
/*      */   }
/*      */   
/*      */   public boolean canDropItems() {
/* 1367 */     return this.dropSpamThrottler.isUnderThreshold();
/*      */   }
/*      */   
/*      */   public TickThrottler getDropSpamThrottler() {
/* 1371 */     return this.dropSpamThrottler;
/*      */   }
/*      */   
/*      */   public Input getLastSentInput() {
/* 1375 */     return this.lastSentInput;
/*      */   }
/*      */   
/*      */   public HitResult raycastHitResult(float a, Entity cameraEntity) {
/* 1379 */     ItemStack itemStack = getActiveItem();
/* 1380 */     AttackRange itemAttackRange = (AttackRange)itemStack.get(DataComponents.ATTACK_RANGE);
/* 1381 */     double blockInteractionRange = blockInteractionRange();
/* 1382 */     HitResult hitResult = null;
/* 1383 */     if (itemAttackRange != null) {
/* 1384 */       hitResult = itemAttackRange.getClosesetHit(cameraEntity, a, EntitySelector.CAN_BE_PICKED);
/* 1385 */       if (hitResult instanceof BlockHitResult)
/* 1386 */         hitResult = filterHitResult(hitResult, cameraEntity.getEyePosition(a), blockInteractionRange); 
/*      */     } 
/* 1390 */     if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
/* 1391 */       double entityInteractionRange = entityInteractionRange();
/* 1392 */       hitResult = pick(cameraEntity, blockInteractionRange, entityInteractionRange, a);
/*      */     } 
/* 1394 */     return hitResult;
/*      */   }
/*      */   
/*      */   private static HitResult pick(Entity cameraEntity, double blockInteractionRange, double entityInteractionRange, float partialTicks) {
/* 1399 */     double maxDistance = Math.max(blockInteractionRange, entityInteractionRange);
/* 1400 */     double maxDistanceSq = Mth.square(maxDistance);
/* 1402 */     Vec3 from = cameraEntity.getEyePosition(partialTicks);
/* 1404 */     HitResult blockHitResult = cameraEntity.pick(maxDistance, partialTicks, false);
/* 1405 */     double blockDistanceSq = blockHitResult.getLocation().distanceToSqr(from);
/* 1408 */     if (blockHitResult.getType() != HitResult.Type.MISS) {
/* 1409 */       maxDistanceSq = blockDistanceSq;
/* 1410 */       maxDistance = Math.sqrt(maxDistanceSq);
/*      */     } 
/* 1413 */     Vec3 direction = cameraEntity.getViewVector(partialTicks);
/* 1414 */     Vec3 to = from.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance);
/* 1416 */     float overlap = 1.0F;
/* 1417 */     AABB box = cameraEntity.getBoundingBox().expandTowards(direction.scale(maxDistance)).inflate(1.0D, 1.0D, 1.0D);
/* 1418 */     EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, EntitySelector.CAN_BE_PICKED, maxDistanceSq);
/* 1420 */     if (entityHitResult != null && entityHitResult.getLocation().distanceToSqr(from) < blockDistanceSq)
/* 1421 */       return filterHitResult((HitResult)entityHitResult, from, entityInteractionRange); 
/* 1423 */     return filterHitResult(blockHitResult, from, blockInteractionRange);
/*      */   }
/*      */   
/*      */   private static HitResult filterHitResult(HitResult hitResult, Vec3 from, double maxRange) {
/* 1428 */     Vec3 hitLocation = hitResult.getLocation();
/* 1429 */     if (!hitLocation.closerThan((Position)from, maxRange)) {
/* 1430 */       Vec3 location = hitResult.getLocation();
/* 1431 */       Direction direction = Direction.getApproximateNearest(location.x - from.x, location.y - from.y, location.z - from.z);
/* 1432 */       return (HitResult)BlockHitResult.miss(location, direction, BlockPos.containing((Position)location));
/*      */     } 
/* 1434 */     return hitResult;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\player\LocalPlayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */