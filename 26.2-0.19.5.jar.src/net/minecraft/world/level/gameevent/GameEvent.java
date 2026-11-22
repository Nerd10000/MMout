/*     */ package net.minecraft.world.level.gameevent;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class GameEvent extends Record {
/*     */   private final int notificationRadius;
/*     */   
/*     */   public GameEvent(int notificationRadius) {
/*  15 */     this.notificationRadius = notificationRadius;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/GameEvent;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/GameEvent;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/GameEvent;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/GameEvent;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/GameEvent;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/GameEvent;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*  19 */   public static final Holder.Reference<GameEvent> BLOCK_ACTIVATE = register("block_activate");
/*     */   
/*  20 */   public static final Holder.Reference<GameEvent> BLOCK_ATTACH = register("block_attach");
/*     */   
/*  21 */   public static final Holder.Reference<GameEvent> BLOCK_CHANGE = register("block_change");
/*     */   
/*  22 */   public static final Holder.Reference<GameEvent> BLOCK_CLOSE = register("block_close");
/*     */   
/*  23 */   public static final Holder.Reference<GameEvent> BLOCK_DEACTIVATE = register("block_deactivate");
/*     */   
/*  24 */   public static final Holder.Reference<GameEvent> BLOCK_DESTROY = register("block_destroy");
/*     */   
/*  25 */   public static final Holder.Reference<GameEvent> BLOCK_DETACH = register("block_detach");
/*     */   
/*  26 */   public static final Holder.Reference<GameEvent> BLOCK_OPEN = register("block_open");
/*     */   
/*  27 */   public static final Holder.Reference<GameEvent> BLOCK_PLACE = register("block_place");
/*     */   
/*  28 */   public static final Holder.Reference<GameEvent> BOUNCE = register("bounce");
/*     */   
/*  29 */   public static final Holder.Reference<GameEvent> CONTAINER_CLOSE = register("container_close");
/*     */   
/*  30 */   public static final Holder.Reference<GameEvent> CONTAINER_OPEN = register("container_open");
/*     */   
/*  31 */   public static final Holder.Reference<GameEvent> DRINK = register("drink");
/*     */   
/*  32 */   public static final Holder.Reference<GameEvent> EAT = register("eat");
/*     */   
/*  33 */   public static final Holder.Reference<GameEvent> ELYTRA_GLIDE = register("elytra_glide");
/*     */   
/*  34 */   public static final Holder.Reference<GameEvent> ENTITY_DAMAGE = register("entity_damage");
/*     */   
/*  35 */   public static final Holder.Reference<GameEvent> ENTITY_DIE = register("entity_die");
/*     */   
/*  36 */   public static final Holder.Reference<GameEvent> ENTITY_DISMOUNT = register("entity_dismount");
/*     */   
/*  37 */   public static final Holder.Reference<GameEvent> ENTITY_INTERACT = register("entity_interact");
/*     */   
/*  38 */   public static final Holder.Reference<GameEvent> ENTITY_MOUNT = register("entity_mount");
/*     */   
/*  39 */   public static final Holder.Reference<GameEvent> ENTITY_PLACE = register("entity_place");
/*     */   
/*  40 */   public static final Holder.Reference<GameEvent> ENTITY_ACTION = register("entity_action");
/*     */   
/*  41 */   public static final Holder.Reference<GameEvent> EQUIP = register("equip");
/*     */   
/*  42 */   public static final Holder.Reference<GameEvent> EXPLODE = register("explode");
/*     */   
/*  43 */   public static final Holder.Reference<GameEvent> FLAP = register("flap");
/*     */   
/*  44 */   public static final Holder.Reference<GameEvent> FLUID_PICKUP = register("fluid_pickup");
/*     */   
/*  45 */   public static final Holder.Reference<GameEvent> FLUID_PLACE = register("fluid_place");
/*     */   
/*  46 */   public static final Holder.Reference<GameEvent> HIT_GROUND = register("hit_ground");
/*     */   
/*  47 */   public static final Holder.Reference<GameEvent> INSTRUMENT_PLAY = register("instrument_play");
/*     */   
/*  48 */   public static final Holder.Reference<GameEvent> ITEM_INTERACT_FINISH = register("item_interact_finish");
/*     */   
/*  49 */   public static final Holder.Reference<GameEvent> ITEM_INTERACT_START = register("item_interact_start");
/*     */   
/*  50 */   public static final Holder.Reference<GameEvent> JUKEBOX_PLAY = register("jukebox_play", 10);
/*     */   
/*  51 */   public static final Holder.Reference<GameEvent> JUKEBOX_STOP_PLAY = register("jukebox_stop_play", 10);
/*     */   
/*  52 */   public static final Holder.Reference<GameEvent> LIGHTNING_STRIKE = register("lightning_strike");
/*     */   
/*  53 */   public static final Holder.Reference<GameEvent> NOTE_BLOCK_PLAY = register("note_block_play");
/*     */   
/*  54 */   public static final Holder.Reference<GameEvent> PRIME_FUSE = register("prime_fuse");
/*     */   
/*  55 */   public static final Holder.Reference<GameEvent> PROJECTILE_LAND = register("projectile_land");
/*     */   
/*  56 */   public static final Holder.Reference<GameEvent> PROJECTILE_SHOOT = register("projectile_shoot");
/*     */   
/*  57 */   public static final Holder.Reference<GameEvent> SCULK_SENSOR_TENDRILS_CLICKING = register("sculk_sensor_tendrils_clicking");
/*     */   
/*  58 */   public static final Holder.Reference<GameEvent> SHEAR = register("shear");
/*     */   
/*  59 */   public static final Holder.Reference<GameEvent> SHRIEK = register("shriek", 32);
/*     */   
/*  60 */   public static final Holder.Reference<GameEvent> SPLASH = register("splash");
/*     */   
/*  61 */   public static final Holder.Reference<GameEvent> STEP = register("step");
/*     */   
/*  62 */   public static final Holder.Reference<GameEvent> SWIM = register("swim");
/*     */   
/*  63 */   public static final Holder.Reference<GameEvent> TELEPORT = register("teleport");
/*     */   
/*  64 */   public static final Holder.Reference<GameEvent> UNEQUIP = register("unequip");
/*     */   
/*  65 */   public static final Holder.Reference<GameEvent> RESONATE_1 = register("resonate_1");
/*     */   
/*  66 */   public static final Holder.Reference<GameEvent> RESONATE_2 = register("resonate_2");
/*     */   
/*  67 */   public static final Holder.Reference<GameEvent> RESONATE_3 = register("resonate_3");
/*     */   
/*  68 */   public static final Holder.Reference<GameEvent> RESONATE_4 = register("resonate_4");
/*     */   
/*  69 */   public static final Holder.Reference<GameEvent> RESONATE_5 = register("resonate_5");
/*     */   
/*  70 */   public static final Holder.Reference<GameEvent> RESONATE_6 = register("resonate_6");
/*     */   
/*  71 */   public static final Holder.Reference<GameEvent> RESONATE_7 = register("resonate_7");
/*     */   
/*  72 */   public static final Holder.Reference<GameEvent> RESONATE_8 = register("resonate_8");
/*     */   
/*  73 */   public static final Holder.Reference<GameEvent> RESONATE_9 = register("resonate_9");
/*     */   
/*  74 */   public static final Holder.Reference<GameEvent> RESONATE_10 = register("resonate_10");
/*     */   
/*  75 */   public static final Holder.Reference<GameEvent> RESONATE_11 = register("resonate_11");
/*     */   
/*  76 */   public static final Holder.Reference<GameEvent> RESONATE_12 = register("resonate_12");
/*     */   
/*  77 */   public static final Holder.Reference<GameEvent> RESONATE_13 = register("resonate_13");
/*     */   
/*  78 */   public static final Holder.Reference<GameEvent> RESONATE_14 = register("resonate_14");
/*     */   
/*  79 */   public static final Holder.Reference<GameEvent> RESONATE_15 = register("resonate_15");
/*     */   
/*     */   public static final int DEFAULT_NOTIFICATION_RADIUS = 16;
/*     */   
/*  83 */   public static final Codec<Holder<GameEvent>> CODEC = (Codec<Holder<GameEvent>>)RegistryFixedCodec.create(Registries.GAME_EVENT);
/*     */   
/*     */   public static Holder<GameEvent> bootstrap(Registry<GameEvent> registry) {
/*  86 */     return (Holder<GameEvent>)BLOCK_ACTIVATE;
/*     */   }
/*     */   
/*     */   public int notificationRadius() {
/*  94 */     return this.notificationRadius;
/*     */   }
/*     */   
/*     */   private static Holder.Reference<GameEvent> register(String name) {
/*  98 */     return register(name, 16);
/*     */   }
/*     */   
/*     */   private static Holder.Reference<GameEvent> register(String name, int notificationRadius) {
/* 102 */     return Registry.registerForHolder((Registry)BuiltInRegistries.GAME_EVENT, Identifier.withDefaultNamespace(name), new GameEvent(notificationRadius));
/*     */   }
/*     */   
/*     */   public static final class Context extends Record {
/*     */     private final Entity sourceEntity;
/*     */     
/*     */     private final BlockState affectedState;
/*     */     
/*     */     public Context(Entity sourceEntity, BlockState affectedState) {
/* 105 */       this.sourceEntity = sourceEntity;
/* 105 */       this.affectedState = affectedState;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/gameevent/GameEvent$Context;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/GameEvent$Context;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/gameevent/GameEvent$Context;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/GameEvent$Context;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/gameevent/GameEvent$Context;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Entity sourceEntity() {
/* 105 */       return this.sourceEntity;
/*     */     }
/*     */     
/*     */     public BlockState affectedState() {
/* 105 */       return this.affectedState;
/*     */     }
/*     */     
/*     */     public static Context of(Entity sourceEntity) {
/* 107 */       return new Context(sourceEntity, null);
/*     */     }
/*     */     
/*     */     public static Context of(BlockState state) {
/* 111 */       return new Context(null, state);
/*     */     }
/*     */     
/*     */     public static Context of(Entity sourceEntity, BlockState state) {
/* 115 */       return new Context(sourceEntity, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ListenerInfo implements Comparable<ListenerInfo> {
/*     */     private final Holder<GameEvent> gameEvent;
/*     */     
/*     */     private final Vec3 source;
/*     */     
/*     */     private final GameEvent.Context context;
/*     */     
/*     */     private final GameEventListener recipient;
/*     */     
/*     */     private final double distanceToRecipient;
/*     */     
/*     */     public ListenerInfo(Holder<GameEvent> gameEvent, Vec3 source, GameEvent.Context context, GameEventListener recipient, Vec3 recipientPos) {
/* 127 */       this.gameEvent = gameEvent;
/* 128 */       this.source = source;
/* 129 */       this.context = context;
/* 130 */       this.recipient = recipient;
/* 131 */       this.distanceToRecipient = source.distanceToSqr(recipientPos);
/*     */     }
/*     */     
/*     */     public int compareTo(ListenerInfo other) {
/* 136 */       return Double.compare(this.distanceToRecipient, other.distanceToRecipient);
/*     */     }
/*     */     
/*     */     public Holder<GameEvent> gameEvent() {
/* 140 */       return this.gameEvent;
/*     */     }
/*     */     
/*     */     public Vec3 source() {
/* 144 */       return this.source;
/*     */     }
/*     */     
/*     */     public GameEvent.Context context() {
/* 148 */       return this.context;
/*     */     }
/*     */     
/*     */     public GameEventListener recipient() {
/* 152 */       return this.recipient;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\GameEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */