/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundBundlePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundMoveMinecartPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
/*     */ import net.minecraft.network.protocol.game.VecDeltaCodec;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerEntity {
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int TOLERANCE_LEVEL_ROTATION = 1;
/*     */   
/*     */   private static final double TOLERANCE_LEVEL_POSITION = 7.62939453125E-6D;
/*     */   
/*     */   public static final int FORCED_POS_UPDATE_PERIOD = 60;
/*     */   
/*     */   private static final int FORCED_TELEPORT_PERIOD = 400;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final Entity entity;
/*     */   
/*     */   private final int updateInterval;
/*     */   
/*     */   private final boolean trackDelta;
/*     */   
/*     */   private final Synchronizer synchronizer;
/*     */   
/*  74 */   private final VecDeltaCodec positionCodec = new VecDeltaCodec();
/*     */   
/*     */   private byte lastSentYRot;
/*     */   
/*     */   private byte lastSentXRot;
/*     */   
/*     */   private byte lastSentYHeadRot;
/*     */   
/*     */   private Vec3 lastSentMovement;
/*     */   
/*     */   private int tickCount;
/*     */   
/*     */   private int teleportDelay;
/*     */   
/*  81 */   private List<Entity> lastPassengers = Collections.emptyList();
/*     */   
/*     */   private boolean wasRiding;
/*     */   
/*     */   private boolean wasOnGround;
/*     */   
/*     */   private List<SynchedEntityData.DataValue<?>> trackedDataValues;
/*     */   
/*     */   public ServerEntity(ServerLevel level, Entity entity, int updateInterval, boolean trackDelta, Synchronizer synchronizer) {
/*  89 */     this.level = level;
/*  90 */     this.synchronizer = synchronizer;
/*  91 */     this.entity = entity;
/*  92 */     this.updateInterval = updateInterval;
/*  93 */     this.trackDelta = trackDelta;
/*  96 */     this.positionCodec.setBase(entity.trackingPosition());
/*  98 */     this.lastSentMovement = entity.getDeltaMovement();
/* 100 */     this.lastSentYRot = Mth.packDegrees(entity.getYRot());
/* 101 */     this.lastSentXRot = Mth.packDegrees(entity.getXRot());
/* 102 */     this.lastSentYHeadRot = Mth.packDegrees(entity.getYHeadRot());
/* 103 */     this.wasOnGround = entity.onGround();
/* 105 */     this.trackedDataValues = entity.getEntityData().getNonDefaultValues();
/*     */   }
/*     */   
/*     */   public void sendChanges() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   4: invokevirtual updateDataBeforeSync : ()V
/*     */     //   7: aload_0
/*     */     //   8: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   11: invokevirtual getPassengers : ()Ljava/util/List;
/*     */     //   14: astore_1
/*     */     //   15: aload_1
/*     */     //   16: aload_0
/*     */     //   17: getfield lastPassengers : Ljava/util/List;
/*     */     //   20: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   25: ifne -> 60
/*     */     //   28: aload_0
/*     */     //   29: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   32: new net/minecraft/network/protocol/game/ClientboundSetPassengersPacket
/*     */     //   35: dup
/*     */     //   36: aload_0
/*     */     //   37: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   40: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;)V
/*     */     //   43: aload_0
/*     */     //   44: aload_1
/*     */     //   45: <illegal opcode> test : (Lnet/minecraft/server/level/ServerEntity;Ljava/util/List;)Ljava/util/function/Predicate;
/*     */     //   50: invokeinterface sendToTrackingPlayersFiltered : (Lnet/minecraft/network/protocol/Packet;Ljava/util/function/Predicate;)V
/*     */     //   55: aload_0
/*     */     //   56: aload_1
/*     */     //   57: putfield lastPassengers : Ljava/util/List;
/*     */     //   60: aload_0
/*     */     //   61: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   64: astore_3
/*     */     //   65: aload_3
/*     */     //   66: instanceof net/minecraft/world/entity/decoration/ItemFrame
/*     */     //   69: ifeq -> 208
/*     */     //   72: aload_3
/*     */     //   73: checkcast net/minecraft/world/entity/decoration/ItemFrame
/*     */     //   76: astore_2
/*     */     //   77: aload_0
/*     */     //   78: getfield tickCount : I
/*     */     //   81: bipush #10
/*     */     //   83: irem
/*     */     //   84: ifne -> 208
/*     */     //   87: aload_2
/*     */     //   88: invokevirtual getItem : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   91: astore_3
/*     */     //   92: aload_3
/*     */     //   93: invokevirtual getItem : ()Lnet/minecraft/world/item/Item;
/*     */     //   96: instanceof net/minecraft/world/item/MapItem
/*     */     //   99: ifeq -> 204
/*     */     //   102: aload_3
/*     */     //   103: getstatic net/minecraft/core/component/DataComponents.MAP_ID : Lnet/minecraft/core/component/DataComponentType;
/*     */     //   106: invokevirtual get : (Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;
/*     */     //   109: checkcast net/minecraft/world/level/saveddata/maps/MapId
/*     */     //   112: astore #4
/*     */     //   114: aload #4
/*     */     //   116: aload_0
/*     */     //   117: getfield level : Lnet/minecraft/server/level/ServerLevel;
/*     */     //   120: invokestatic getSavedData : (Lnet/minecraft/world/level/saveddata/maps/MapId;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;
/*     */     //   123: astore #5
/*     */     //   125: aload #5
/*     */     //   127: ifnull -> 204
/*     */     //   130: aload_0
/*     */     //   131: getfield level : Lnet/minecraft/server/level/ServerLevel;
/*     */     //   134: invokevirtual players : ()Ljava/util/List;
/*     */     //   137: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   142: astore #6
/*     */     //   144: aload #6
/*     */     //   146: invokeinterface hasNext : ()Z
/*     */     //   151: ifeq -> 204
/*     */     //   154: aload #6
/*     */     //   156: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   161: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   164: astore #7
/*     */     //   166: aload #5
/*     */     //   168: aload #7
/*     */     //   170: aload_3
/*     */     //   171: aload_2
/*     */     //   172: invokevirtual tickCarriedBy : (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/decoration/ItemFrame;)V
/*     */     //   175: aload #5
/*     */     //   177: aload #4
/*     */     //   179: aload #7
/*     */     //   181: invokevirtual getUpdatePacket : (Lnet/minecraft/world/level/saveddata/maps/MapId;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/network/protocol/Packet;
/*     */     //   184: astore #8
/*     */     //   186: aload #8
/*     */     //   188: ifnull -> 201
/*     */     //   191: aload #7
/*     */     //   193: getfield connection : Lnet/minecraft/server/network/ServerGamePacketListenerImpl;
/*     */     //   196: aload #8
/*     */     //   198: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   201: goto -> 144
/*     */     //   204: aload_0
/*     */     //   205: invokevirtual sendDirtyEntityData : ()V
/*     */     //   208: aload_0
/*     */     //   209: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   212: getfield syncPosition : Z
/*     */     //   215: ifeq -> 249
/*     */     //   218: aload_0
/*     */     //   219: aload_0
/*     */     //   220: getfield tickCount : I
/*     */     //   223: aload_0
/*     */     //   224: getfield updateInterval : I
/*     */     //   227: idiv
/*     */     //   228: aload_0
/*     */     //   229: getfield updateInterval : I
/*     */     //   232: imul
/*     */     //   233: aload_0
/*     */     //   234: getfield updateInterval : I
/*     */     //   237: iadd
/*     */     //   238: putfield tickCount : I
/*     */     //   241: aload_0
/*     */     //   242: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   245: iconst_0
/*     */     //   246: putfield syncPosition : Z
/*     */     //   249: aload_0
/*     */     //   250: getfield tickCount : I
/*     */     //   253: aload_0
/*     */     //   254: getfield updateInterval : I
/*     */     //   257: irem
/*     */     //   258: ifeq -> 284
/*     */     //   261: aload_0
/*     */     //   262: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   265: getfield needsSync : Z
/*     */     //   268: ifne -> 284
/*     */     //   271: aload_0
/*     */     //   272: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   275: invokevirtual getEntityData : ()Lnet/minecraft/network/syncher/SynchedEntityData;
/*     */     //   278: invokevirtual isDirty : ()Z
/*     */     //   281: ifeq -> 1192
/*     */     //   284: aload_0
/*     */     //   285: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   288: invokevirtual getYRot : ()F
/*     */     //   291: invokestatic packDegrees : (F)B
/*     */     //   294: istore_2
/*     */     //   295: aload_0
/*     */     //   296: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   299: invokevirtual getXRot : ()F
/*     */     //   302: invokestatic packDegrees : (F)B
/*     */     //   305: istore_3
/*     */     //   306: iload_2
/*     */     //   307: aload_0
/*     */     //   308: getfield lastSentYRot : B
/*     */     //   311: isub
/*     */     //   312: invokestatic abs : (I)I
/*     */     //   315: iconst_1
/*     */     //   316: if_icmpge -> 332
/*     */     //   319: iload_3
/*     */     //   320: aload_0
/*     */     //   321: getfield lastSentXRot : B
/*     */     //   324: isub
/*     */     //   325: invokestatic abs : (I)I
/*     */     //   328: iconst_1
/*     */     //   329: if_icmplt -> 336
/*     */     //   332: iconst_1
/*     */     //   333: goto -> 337
/*     */     //   336: iconst_0
/*     */     //   337: istore #4
/*     */     //   339: aload_0
/*     */     //   340: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   343: invokevirtual isPassenger : ()Z
/*     */     //   346: ifeq -> 422
/*     */     //   349: iload #4
/*     */     //   351: ifeq -> 396
/*     */     //   354: aload_0
/*     */     //   355: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   358: new net/minecraft/network/protocol/game/ClientboundMoveEntityPacket$Rot
/*     */     //   361: dup
/*     */     //   362: aload_0
/*     */     //   363: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   366: invokevirtual getId : ()I
/*     */     //   369: iload_2
/*     */     //   370: iload_3
/*     */     //   371: aload_0
/*     */     //   372: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   375: invokevirtual onGround : ()Z
/*     */     //   378: invokespecial <init> : (IBBZ)V
/*     */     //   381: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   386: aload_0
/*     */     //   387: iload_2
/*     */     //   388: putfield lastSentYRot : B
/*     */     //   391: aload_0
/*     */     //   392: iload_3
/*     */     //   393: putfield lastSentXRot : B
/*     */     //   396: aload_0
/*     */     //   397: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   400: aload_0
/*     */     //   401: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   404: invokevirtual trackingPosition : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   407: invokevirtual setBase : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   410: aload_0
/*     */     //   411: invokevirtual sendDirtyEntityData : ()V
/*     */     //   414: aload_0
/*     */     //   415: iconst_1
/*     */     //   416: putfield wasRiding : Z
/*     */     //   419: goto -> 1130
/*     */     //   422: aload_0
/*     */     //   423: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   426: astore #7
/*     */     //   428: aload #7
/*     */     //   430: instanceof net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*     */     //   433: ifeq -> 478
/*     */     //   436: aload #7
/*     */     //   438: checkcast net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*     */     //   441: astore #5
/*     */     //   443: aload #5
/*     */     //   445: invokevirtual getBehavior : ()Lnet/minecraft/world/entity/vehicle/minecart/MinecartBehavior;
/*     */     //   448: astore #7
/*     */     //   450: aload #7
/*     */     //   452: instanceof net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   455: ifeq -> 478
/*     */     //   458: aload #7
/*     */     //   460: checkcast net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   463: astore #6
/*     */     //   465: aload_0
/*     */     //   466: aload #6
/*     */     //   468: iload_2
/*     */     //   469: iload_3
/*     */     //   470: iload #4
/*     */     //   472: invokevirtual handleMinecartPosRot : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;BBZ)V
/*     */     //   475: goto -> 1130
/*     */     //   478: aload_0
/*     */     //   479: dup
/*     */     //   480: getfield teleportDelay : I
/*     */     //   483: iconst_1
/*     */     //   484: iadd
/*     */     //   485: putfield teleportDelay : I
/*     */     //   488: aload_0
/*     */     //   489: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   492: invokevirtual trackingPosition : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   495: astore #7
/*     */     //   497: aload_0
/*     */     //   498: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   501: aload #7
/*     */     //   503: invokevirtual delta : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   506: invokevirtual lengthSqr : ()D
/*     */     //   509: ldc2_w 7.62939453125E-6
/*     */     //   512: dcmpl
/*     */     //   513: iflt -> 520
/*     */     //   516: iconst_1
/*     */     //   517: goto -> 521
/*     */     //   520: iconst_0
/*     */     //   521: istore #8
/*     */     //   523: aconst_null
/*     */     //   524: astore #9
/*     */     //   526: iload #8
/*     */     //   528: ifne -> 541
/*     */     //   531: aload_0
/*     */     //   532: getfield tickCount : I
/*     */     //   535: bipush #60
/*     */     //   537: irem
/*     */     //   538: ifne -> 545
/*     */     //   541: iconst_1
/*     */     //   542: goto -> 546
/*     */     //   545: iconst_0
/*     */     //   546: istore #10
/*     */     //   548: iconst_0
/*     */     //   549: istore #11
/*     */     //   551: iconst_0
/*     */     //   552: istore #12
/*     */     //   554: aload_0
/*     */     //   555: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   558: aload #7
/*     */     //   560: invokevirtual encodeX : (Lnet/minecraft/world/phys/Vec3;)J
/*     */     //   563: lstore #13
/*     */     //   565: aload_0
/*     */     //   566: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   569: aload #7
/*     */     //   571: invokevirtual encodeY : (Lnet/minecraft/world/phys/Vec3;)J
/*     */     //   574: lstore #15
/*     */     //   576: aload_0
/*     */     //   577: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   580: aload #7
/*     */     //   582: invokevirtual encodeZ : (Lnet/minecraft/world/phys/Vec3;)J
/*     */     //   585: lstore #17
/*     */     //   587: lload #13
/*     */     //   589: ldc2_w -32768
/*     */     //   592: lcmp
/*     */     //   593: iflt -> 641
/*     */     //   596: lload #13
/*     */     //   598: ldc2_w 32767
/*     */     //   601: lcmp
/*     */     //   602: ifgt -> 641
/*     */     //   605: lload #15
/*     */     //   607: ldc2_w -32768
/*     */     //   610: lcmp
/*     */     //   611: iflt -> 641
/*     */     //   614: lload #15
/*     */     //   616: ldc2_w 32767
/*     */     //   619: lcmp
/*     */     //   620: ifgt -> 641
/*     */     //   623: lload #17
/*     */     //   625: ldc2_w -32768
/*     */     //   628: lcmp
/*     */     //   629: iflt -> 641
/*     */     //   632: lload #17
/*     */     //   634: ldc2_w 32767
/*     */     //   637: lcmp
/*     */     //   638: ifle -> 645
/*     */     //   641: iconst_1
/*     */     //   642: goto -> 646
/*     */     //   645: iconst_0
/*     */     //   646: istore #19
/*     */     //   648: aload_0
/*     */     //   649: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   652: invokevirtual getRequiresPrecisePosition : ()Z
/*     */     //   655: ifne -> 694
/*     */     //   658: iload #19
/*     */     //   660: ifne -> 694
/*     */     //   663: aload_0
/*     */     //   664: getfield teleportDelay : I
/*     */     //   667: sipush #400
/*     */     //   670: if_icmpgt -> 694
/*     */     //   673: aload_0
/*     */     //   674: getfield wasRiding : Z
/*     */     //   677: ifne -> 694
/*     */     //   680: aload_0
/*     */     //   681: getfield wasOnGround : Z
/*     */     //   684: aload_0
/*     */     //   685: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   688: invokevirtual onGround : ()Z
/*     */     //   691: if_icmpeq -> 728
/*     */     //   694: aload_0
/*     */     //   695: aload_0
/*     */     //   696: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   699: invokevirtual onGround : ()Z
/*     */     //   702: putfield wasOnGround : Z
/*     */     //   705: aload_0
/*     */     //   706: iconst_0
/*     */     //   707: putfield teleportDelay : I
/*     */     //   710: aload_0
/*     */     //   711: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   714: invokestatic of : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;
/*     */     //   717: astore #9
/*     */     //   719: iconst_1
/*     */     //   720: istore #11
/*     */     //   722: iconst_1
/*     */     //   723: istore #12
/*     */     //   725: goto -> 873
/*     */     //   728: iload #10
/*     */     //   730: ifeq -> 738
/*     */     //   733: iload #4
/*     */     //   735: ifne -> 748
/*     */     //   738: aload_0
/*     */     //   739: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   742: instanceof net/minecraft/world/entity/projectile/arrow/AbstractArrow
/*     */     //   745: ifeq -> 794
/*     */     //   748: new net/minecraft/network/protocol/game/ClientboundMoveEntityPacket$PosRot
/*     */     //   751: dup
/*     */     //   752: aload_0
/*     */     //   753: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   756: invokevirtual getId : ()I
/*     */     //   759: lload #13
/*     */     //   761: l2i
/*     */     //   762: i2s
/*     */     //   763: lload #15
/*     */     //   765: l2i
/*     */     //   766: i2s
/*     */     //   767: lload #17
/*     */     //   769: l2i
/*     */     //   770: i2s
/*     */     //   771: iload_2
/*     */     //   772: iload_3
/*     */     //   773: aload_0
/*     */     //   774: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   777: invokevirtual onGround : ()Z
/*     */     //   780: invokespecial <init> : (ISSSBBZ)V
/*     */     //   783: astore #9
/*     */     //   785: iconst_1
/*     */     //   786: istore #11
/*     */     //   788: iconst_1
/*     */     //   789: istore #12
/*     */     //   791: goto -> 873
/*     */     //   794: iload #10
/*     */     //   796: ifeq -> 840
/*     */     //   799: new net/minecraft/network/protocol/game/ClientboundMoveEntityPacket$Pos
/*     */     //   802: dup
/*     */     //   803: aload_0
/*     */     //   804: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   807: invokevirtual getId : ()I
/*     */     //   810: lload #13
/*     */     //   812: l2i
/*     */     //   813: i2s
/*     */     //   814: lload #15
/*     */     //   816: l2i
/*     */     //   817: i2s
/*     */     //   818: lload #17
/*     */     //   820: l2i
/*     */     //   821: i2s
/*     */     //   822: aload_0
/*     */     //   823: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   826: invokevirtual onGround : ()Z
/*     */     //   829: invokespecial <init> : (ISSSZ)V
/*     */     //   832: astore #9
/*     */     //   834: iconst_1
/*     */     //   835: istore #11
/*     */     //   837: goto -> 873
/*     */     //   840: iload #4
/*     */     //   842: ifeq -> 873
/*     */     //   845: new net/minecraft/network/protocol/game/ClientboundMoveEntityPacket$Rot
/*     */     //   848: dup
/*     */     //   849: aload_0
/*     */     //   850: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   853: invokevirtual getId : ()I
/*     */     //   856: iload_2
/*     */     //   857: iload_3
/*     */     //   858: aload_0
/*     */     //   859: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   862: invokevirtual onGround : ()Z
/*     */     //   865: invokespecial <init> : (IBBZ)V
/*     */     //   868: astore #9
/*     */     //   870: iconst_1
/*     */     //   871: istore #12
/*     */     //   873: aload_0
/*     */     //   874: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   877: getfield needsSync : Z
/*     */     //   880: ifne -> 919
/*     */     //   883: aload_0
/*     */     //   884: getfield trackDelta : Z
/*     */     //   887: ifne -> 919
/*     */     //   890: aload_0
/*     */     //   891: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   894: astore #21
/*     */     //   896: aload #21
/*     */     //   898: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   901: ifeq -> 1076
/*     */     //   904: aload #21
/*     */     //   906: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   909: astore #20
/*     */     //   911: aload #20
/*     */     //   913: invokevirtual isFallFlying : ()Z
/*     */     //   916: ifeq -> 1076
/*     */     //   919: aload_0
/*     */     //   920: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   923: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   926: astore #21
/*     */     //   928: aload #21
/*     */     //   930: aload_0
/*     */     //   931: getfield lastSentMovement : Lnet/minecraft/world/phys/Vec3;
/*     */     //   934: invokevirtual distanceToSqr : (Lnet/minecraft/world/phys/Vec3;)D
/*     */     //   937: dstore #22
/*     */     //   939: dload #22
/*     */     //   941: ldc2_w 1.0E-7
/*     */     //   944: dcmpl
/*     */     //   945: ifgt -> 965
/*     */     //   948: dload #22
/*     */     //   950: dconst_0
/*     */     //   951: dcmpl
/*     */     //   952: ifle -> 1076
/*     */     //   955: aload #21
/*     */     //   957: invokevirtual lengthSqr : ()D
/*     */     //   960: dconst_0
/*     */     //   961: dcmpl
/*     */     //   962: ifne -> 1076
/*     */     //   965: aload_0
/*     */     //   966: aload #21
/*     */     //   968: putfield lastSentMovement : Lnet/minecraft/world/phys/Vec3;
/*     */     //   971: aload_0
/*     */     //   972: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   975: astore #25
/*     */     //   977: aload #25
/*     */     //   979: instanceof net/minecraft/world/entity/projectile/hurtingprojectile/AbstractHurtingProjectile
/*     */     //   982: ifeq -> 1049
/*     */     //   985: aload #25
/*     */     //   987: checkcast net/minecraft/world/entity/projectile/hurtingprojectile/AbstractHurtingProjectile
/*     */     //   990: astore #24
/*     */     //   992: aload_0
/*     */     //   993: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   996: new net/minecraft/network/protocol/game/ClientboundBundlePacket
/*     */     //   999: dup
/*     */     //   1000: new net/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket
/*     */     //   1003: dup
/*     */     //   1004: aload_0
/*     */     //   1005: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1008: invokevirtual getId : ()I
/*     */     //   1011: aload_0
/*     */     //   1012: getfield lastSentMovement : Lnet/minecraft/world/phys/Vec3;
/*     */     //   1015: invokespecial <init> : (ILnet/minecraft/world/phys/Vec3;)V
/*     */     //   1018: new net/minecraft/network/protocol/game/ClientboundProjectilePowerPacket
/*     */     //   1021: dup
/*     */     //   1022: aload #24
/*     */     //   1024: invokevirtual getId : ()I
/*     */     //   1027: aload #24
/*     */     //   1029: getfield accelerationPower : D
/*     */     //   1032: invokespecial <init> : (ID)V
/*     */     //   1035: invokestatic of : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;
/*     */     //   1038: invokespecial <init> : (Ljava/lang/Iterable;)V
/*     */     //   1041: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1046: goto -> 1076
/*     */     //   1049: aload_0
/*     */     //   1050: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   1053: new net/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket
/*     */     //   1056: dup
/*     */     //   1057: aload_0
/*     */     //   1058: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1061: invokevirtual getId : ()I
/*     */     //   1064: aload_0
/*     */     //   1065: getfield lastSentMovement : Lnet/minecraft/world/phys/Vec3;
/*     */     //   1068: invokespecial <init> : (ILnet/minecraft/world/phys/Vec3;)V
/*     */     //   1071: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1076: aload #9
/*     */     //   1078: ifnull -> 1092
/*     */     //   1081: aload_0
/*     */     //   1082: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   1085: aload #9
/*     */     //   1087: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1092: aload_0
/*     */     //   1093: invokevirtual sendDirtyEntityData : ()V
/*     */     //   1096: iload #11
/*     */     //   1098: ifeq -> 1110
/*     */     //   1101: aload_0
/*     */     //   1102: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   1105: aload #7
/*     */     //   1107: invokevirtual setBase : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   1110: iload #12
/*     */     //   1112: ifeq -> 1125
/*     */     //   1115: aload_0
/*     */     //   1116: iload_2
/*     */     //   1117: putfield lastSentYRot : B
/*     */     //   1120: aload_0
/*     */     //   1121: iload_3
/*     */     //   1122: putfield lastSentXRot : B
/*     */     //   1125: aload_0
/*     */     //   1126: iconst_0
/*     */     //   1127: putfield wasRiding : Z
/*     */     //   1130: aload_0
/*     */     //   1131: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1134: invokevirtual getYHeadRot : ()F
/*     */     //   1137: invokestatic packDegrees : (F)B
/*     */     //   1140: istore #5
/*     */     //   1142: iload #5
/*     */     //   1144: aload_0
/*     */     //   1145: getfield lastSentYHeadRot : B
/*     */     //   1148: isub
/*     */     //   1149: invokestatic abs : (I)I
/*     */     //   1152: iconst_1
/*     */     //   1153: if_icmplt -> 1184
/*     */     //   1156: aload_0
/*     */     //   1157: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   1160: new net/minecraft/network/protocol/game/ClientboundRotateHeadPacket
/*     */     //   1163: dup
/*     */     //   1164: aload_0
/*     */     //   1165: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1168: iload #5
/*     */     //   1170: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;B)V
/*     */     //   1173: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1178: aload_0
/*     */     //   1179: iload #5
/*     */     //   1181: putfield lastSentYHeadRot : B
/*     */     //   1184: aload_0
/*     */     //   1185: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1188: iconst_0
/*     */     //   1189: putfield needsSync : Z
/*     */     //   1192: aload_0
/*     */     //   1193: dup
/*     */     //   1194: getfield tickCount : I
/*     */     //   1197: iconst_1
/*     */     //   1198: iadd
/*     */     //   1199: putfield tickCount : I
/*     */     //   1202: aload_0
/*     */     //   1203: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1206: getfield hurtMarked : Z
/*     */     //   1209: ifeq -> 1240
/*     */     //   1212: aload_0
/*     */     //   1213: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1216: iconst_0
/*     */     //   1217: putfield hurtMarked : Z
/*     */     //   1220: aload_0
/*     */     //   1221: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   1224: new net/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket
/*     */     //   1227: dup
/*     */     //   1228: aload_0
/*     */     //   1229: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1232: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;)V
/*     */     //   1235: invokeinterface sendToTrackingPlayersAndSelf : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1240: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #109	-> 0
/*     */     //   #111	-> 7
/*     */     //   #112	-> 15
/*     */     //   #113	-> 28
/*     */     //   #117	-> 55
/*     */     //   #120	-> 60
/*     */     //   #121	-> 87
/*     */     //   #123	-> 92
/*     */     //   #124	-> 102
/*     */     //   #125	-> 114
/*     */     //   #126	-> 125
/*     */     //   #127	-> 130
/*     */     //   #128	-> 166
/*     */     //   #130	-> 175
/*     */     //   #131	-> 186
/*     */     //   #132	-> 191
/*     */     //   #134	-> 201
/*     */     //   #138	-> 204
/*     */     //   #141	-> 208
/*     */     //   #143	-> 218
/*     */     //   #144	-> 241
/*     */     //   #148	-> 249
/*     */     //   #149	-> 284
/*     */     //   #150	-> 295
/*     */     //   #151	-> 306
/*     */     //   #153	-> 339
/*     */     //   #155	-> 349
/*     */     //   #156	-> 354
/*     */     //   #157	-> 386
/*     */     //   #158	-> 391
/*     */     //   #161	-> 396
/*     */     //   #163	-> 410
/*     */     //   #165	-> 414
/*     */     //   #166	-> 422
/*     */     //   #168	-> 465
/*     */     //   #170	-> 478
/*     */     //   #171	-> 488
/*     */     //   #174	-> 497
/*     */     //   #176	-> 523
/*     */     //   #178	-> 526
/*     */     //   #179	-> 548
/*     */     //   #180	-> 551
/*     */     //   #182	-> 554
/*     */     //   #183	-> 565
/*     */     //   #184	-> 576
/*     */     //   #185	-> 587
/*     */     //   #186	-> 648
/*     */     //   #187	-> 694
/*     */     //   #188	-> 705
/*     */     //   #189	-> 710
/*     */     //   #190	-> 719
/*     */     //   #191	-> 722
/*     */     //   #193	-> 728
/*     */     //   #194	-> 748
/*     */     //   #195	-> 785
/*     */     //   #196	-> 788
/*     */     //   #197	-> 794
/*     */     //   #198	-> 799
/*     */     //   #199	-> 834
/*     */     //   #200	-> 840
/*     */     //   #201	-> 845
/*     */     //   #202	-> 870
/*     */     //   #206	-> 873
/*     */     //   #207	-> 919
/*     */     //   #208	-> 928
/*     */     //   #210	-> 939
/*     */     //   #211	-> 965
/*     */     //   #212	-> 971
/*     */     //   #213	-> 992
/*     */     //   #214	-> 1008
/*     */     //   #215	-> 1024
/*     */     //   #213	-> 1035
/*     */     //   #217	-> 1049
/*     */     //   #222	-> 1076
/*     */     //   #223	-> 1081
/*     */     //   #226	-> 1092
/*     */     //   #228	-> 1096
/*     */     //   #229	-> 1101
/*     */     //   #231	-> 1110
/*     */     //   #232	-> 1115
/*     */     //   #233	-> 1120
/*     */     //   #236	-> 1125
/*     */     //   #239	-> 1130
/*     */     //   #240	-> 1142
/*     */     //   #241	-> 1156
/*     */     //   #242	-> 1178
/*     */     //   #244	-> 1184
/*     */     //   #247	-> 1192
/*     */     //   #248	-> 1202
/*     */     //   #249	-> 1212
/*     */     //   #250	-> 1220
/*     */     //   #252	-> 1240
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   186	15	8	packet	Lnet/minecraft/network/protocol/Packet;
/*     */     //   166	35	7	player	Lnet/minecraft/server/level/ServerPlayer;
/*     */     //   114	90	4	id	Lnet/minecraft/world/level/saveddata/maps/MapId;
/*     */     //   125	79	5	data	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;
/*     */     //   92	116	3	itemStack	Lnet/minecraft/world/item/ItemStack;
/*     */     //   77	131	2	frame	Lnet/minecraft/world/entity/decoration/ItemFrame;
/*     */     //   443	35	5	minecart	Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;
/*     */     //   465	13	6	newMinecartBehavior	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;
/*     */     //   911	8	20	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   992	57	24	projectile	Lnet/minecraft/world/entity/projectile/hurtingprojectile/AbstractHurtingProjectile;
/*     */     //   928	148	21	movement	Lnet/minecraft/world/phys/Vec3;
/*     */     //   939	137	22	diff	D
/*     */     //   497	633	7	currentPosition	Lnet/minecraft/world/phys/Vec3;
/*     */     //   523	607	8	positionChanged	Z
/*     */     //   526	604	9	packet	Lnet/minecraft/network/protocol/Packet;
/*     */     //   548	582	10	pos	Z
/*     */     //   551	579	11	sentPosition	Z
/*     */     //   554	576	12	sentRotation	Z
/*     */     //   565	565	13	xa	J
/*     */     //   576	554	15	ya	J
/*     */     //   587	543	17	za	J
/*     */     //   648	482	19	deltaTooBig	Z
/*     */     //   295	897	2	yRotn	B
/*     */     //   306	886	3	xRotn	B
/*     */     //   339	853	4	shouldSendRotation	Z
/*     */     //   1142	50	5	yHeadRot	B
/*     */     //   0	1241	0	this	Lnet/minecraft/server/level/ServerEntity;
/*     */     //   15	1226	1	passengers	Ljava/util/List;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   186	15	8	packet	Lnet/minecraft/network/protocol/Packet<*>;
/*     */     //   526	604	9	packet	Lnet/minecraft/network/protocol/Packet<Lnet/minecraft/network/protocol/game/ClientGamePacketListener;>;
/*     */     //   15	1226	1	passengers	Ljava/util/List<Lnet/minecraft/world/entity/Entity;>;
/*     */   }
/*     */   
/*     */   private void handleMinecartPosRot(NewMinecartBehavior newMinecartBehavior, byte yRotn, byte xRotn, boolean shouldSendRotation) {
/* 255 */     sendDirtyEntityData();
/* 257 */     if (newMinecartBehavior.lerpSteps.isEmpty()) {
/* 258 */       Vec3 movement = this.entity.getDeltaMovement();
/* 259 */       double diff = movement.distanceToSqr(this.lastSentMovement);
/* 260 */       Vec3 currentPosition = this.entity.trackingPosition();
/* 261 */       boolean positionChanged = (this.positionCodec.delta(currentPosition).lengthSqr() >= 7.62939453125E-6D);
/* 262 */       boolean shouldSendPosition = (positionChanged || this.tickCount % 60 == 0);
/* 264 */       if (shouldSendPosition || shouldSendRotation || diff > 1.0E-7D)
/* 265 */         this.synchronizer.sendToTrackingPlayers((Packet<? super ClientGamePacketListener>)new ClientboundMoveMinecartPacket(this.entity.getId(), List.of(new NewMinecartBehavior.MinecartStep(this.entity.position(), this.entity.getDeltaMovement(), this.entity.getYRot(), this.entity.getXRot(), 1.0F)))); 
/*     */     } else {
/* 268 */       this.synchronizer.sendToTrackingPlayers((Packet<? super ClientGamePacketListener>)new ClientboundMoveMinecartPacket(this.entity.getId(), List.copyOf(newMinecartBehavior.lerpSteps)));
/* 269 */       newMinecartBehavior.lerpSteps.clear();
/*     */     } 
/* 272 */     this.lastSentYRot = yRotn;
/* 273 */     this.lastSentXRot = xRotn;
/* 274 */     this.positionCodec.setBase(this.entity.position());
/*     */   }
/*     */   
/*     */   public void removePairing(ServerPlayer player) {
/* 278 */     this.entity.stopSeenByPlayer(player);
/* 279 */     player.connection.send((Packet)new ClientboundRemoveEntitiesPacket(new int[] { this.entity.getId() }));
/*     */   }
/*     */   
/*     */   public void addPairing(ServerPlayer player) {
/* 283 */     List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
/* 284 */     Objects.requireNonNull(packets);
/* 284 */     sendPairingData(player, packets::add);
/* 285 */     player.connection.send((Packet)new ClientboundBundlePacket(packets));
/* 286 */     this.entity.startSeenByPlayer(player);
/*     */   }
/*     */   
/*     */   public void sendPairingData(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> broadcast) {
/* 290 */     this.entity.updateDataBeforeSync();
/* 291 */     if (this.entity.isRemoved())
/* 292 */       LOGGER.warn("Fetching packet for removed entity {}", this.entity); 
/* 294 */     Packet<ClientGamePacketListener> packet = this.entity.getAddEntityPacket(this);
/* 295 */     broadcast.accept(packet);
/* 297 */     if (this.trackedDataValues != null)
/* 298 */       broadcast.accept(new ClientboundSetEntityDataPacket(this.entity.getId(), this.trackedDataValues)); 
/* 301 */     Entity entity = this.entity;
/* 301 */     if (entity instanceof LivingEntity) {
/* 301 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 302 */       Collection<AttributeInstance> attributes = livingEntity.getAttributes().getSyncableAttributes();
/* 303 */       if (!attributes.isEmpty())
/* 304 */         broadcast.accept(new ClientboundUpdateAttributesPacket(this.entity.getId(), attributes)); 
/*     */     } 
/* 308 */     entity = this.entity;
/* 308 */     if (entity instanceof LivingEntity) {
/* 308 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 309 */       List<Pair<EquipmentSlot, ItemStack>> slots = Lists.newArrayList();
/* 310 */       for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 311 */         ItemStack itemStack = livingEntity.getItemBySlot(slot);
/* 312 */         if (!itemStack.isEmpty())
/* 313 */           slots.add(Pair.of(slot, itemStack.copy())); 
/*     */       } 
/* 316 */       if (!slots.isEmpty())
/* 317 */         broadcast.accept(new ClientboundSetEquipmentPacket(this.entity.getId(), slots)); 
/*     */     } 
/* 323 */     if (!this.entity.getPassengers().isEmpty())
/* 324 */       broadcast.accept(new ClientboundSetPassengersPacket(this.entity)); 
/* 326 */     if (this.entity.isPassenger())
/* 327 */       broadcast.accept(new ClientboundSetPassengersPacket(this.entity.getVehicle())); 
/* 331 */     entity = this.entity;
/* 331 */     if (entity instanceof Leashable) {
/* 331 */       Leashable leashable = (Leashable)entity;
/* 332 */       if (leashable.isLeashed())
/* 333 */         broadcast.accept(new ClientboundSetEntityLinkPacket(this.entity, leashable.getLeashHolder())); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vec3 getPositionBase() {
/* 339 */     return this.positionCodec.getBase();
/*     */   }
/*     */   
/*     */   public Vec3 getLastSentMovement() {
/* 343 */     return this.lastSentMovement;
/*     */   }
/*     */   
/*     */   public float getLastSentXRot() {
/* 347 */     return Mth.unpackDegrees(this.lastSentXRot);
/*     */   }
/*     */   
/*     */   public float getLastSentYRot() {
/* 351 */     return Mth.unpackDegrees(this.lastSentYRot);
/*     */   }
/*     */   
/*     */   public float getLastSentYHeadRot() {
/* 355 */     return Mth.unpackDegrees(this.lastSentYHeadRot);
/*     */   }
/*     */   
/*     */   private void sendDirtyEntityData() {
/* 359 */     SynchedEntityData entityData = this.entity.getEntityData();
/* 360 */     List<SynchedEntityData.DataValue<?>> packedValues = entityData.packDirty();
/* 361 */     if (packedValues != null) {
/* 362 */       this.trackedDataValues = entityData.getNonDefaultValues();
/* 363 */       this.synchronizer.sendToTrackingPlayersAndSelf((Packet<? super ClientGamePacketListener>)new ClientboundSetEntityDataPacket(this.entity.getId(), packedValues));
/*     */     } 
/* 366 */     Entity entity = this.entity;
/* 366 */     if (entity instanceof LivingEntity) {
/* 366 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 367 */       Set<AttributeInstance> attributes = livingEntity.getAttributes().getAttributesToSync();
/* 369 */       if (!attributes.isEmpty())
/* 370 */         this.synchronizer.sendToTrackingPlayersAndSelf((Packet<? super ClientGamePacketListener>)new ClientboundUpdateAttributesPacket(this.entity.getId(), attributes)); 
/* 373 */       attributes.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Synchronizer {
/*     */     void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> param1Packet);
/*     */     
/*     */     void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> param1Packet);
/*     */     
/*     */     void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> param1Packet, Predicate<ServerPlayer> param1Predicate);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */