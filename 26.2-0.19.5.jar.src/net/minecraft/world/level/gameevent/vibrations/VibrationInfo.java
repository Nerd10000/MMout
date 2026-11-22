/*    */ package net.minecraft.world.level.gameevent.vibrations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class VibrationInfo extends Record {
/*    */   private final Holder<GameEvent> gameEvent;
/*    */   
/*    */   private final float distance;
/*    */   
/*    */   private final Vec3 pos;
/*    */   
/*    */   private final UUID uuid;
/*    */   
/*    */   private final UUID projectileOwnerUuid;
/*    */   
/*    */   private final Entity entity;
/*    */   
/*    */   public static final Codec<VibrationInfo> CODEC;
/*    */   
/*    */   public VibrationInfo(Holder<GameEvent> gameEvent, float distance, Vec3 pos, UUID uuid, UUID projectileOwnerUuid, Entity entity) {
/* 17 */     this.gameEvent = gameEvent;
/* 17 */     this.distance = distance;
/* 17 */     this.pos = pos;
/* 17 */     this.uuid = uuid;
/* 17 */     this.projectileOwnerUuid = projectileOwnerUuid;
/* 17 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<GameEvent> gameEvent() {
/* 17 */     return this.gameEvent;
/*    */   }
/*    */   
/*    */   public float distance() {
/* 17 */     return this.distance;
/*    */   }
/*    */   
/*    */   public Vec3 pos() {
/* 17 */     return this.pos;
/*    */   }
/*    */   
/*    */   public UUID uuid() {
/* 17 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public UUID projectileOwnerUuid() {
/* 17 */     return this.projectileOwnerUuid;
/*    */   }
/*    */   
/*    */   public Entity entity() {
/* 17 */     return this.entity;
/*    */   }
/*    */   
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)GameEvent.CODEC.fieldOf("game_event").forGetter(VibrationInfo::gameEvent), (App)Codec.floatRange(0.0F, Float.MAX_VALUE).fieldOf("distance").forGetter(VibrationInfo::distance), (App)Vec3.CODEC.fieldOf("pos").forGetter(VibrationInfo::pos), (App)UUIDUtil.CODEC.lenientOptionalFieldOf("source").forGetter(()), (App)UUIDUtil.CODEC.lenientOptionalFieldOf("projectile_owner").forGetter(())).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   public VibrationInfo(Holder<GameEvent> gameEvent, float distance, Vec3 pos, UUID uuid, UUID projectileOwnerUuid) {
/* 34 */     this(gameEvent, distance, pos, uuid, projectileOwnerUuid, null);
/*    */   }
/*    */   
/*    */   public VibrationInfo(Holder<GameEvent> gameEvent, float distance, Vec3 pos, Entity entity) {
/* 38 */     this(gameEvent, distance, pos, (entity == null) ? null : entity.getUUID(), getProjectileOwner(entity), entity);
/*    */   }
/*    */   
/*    */   private static UUID getProjectileOwner(Entity entity) {
/* 42 */     if (entity instanceof Projectile) {
/* 42 */       Projectile projectile = (Projectile)entity;
/* 42 */       if (projectile.getOwner() != null)
/* 43 */         return projectile.getOwner().getUUID(); 
/*    */     } 
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   public Optional<Entity> getEntity(ServerLevel level) {
/* 49 */     return Optional.<Entity>ofNullable(this.entity).or(() -> {
/*    */           Objects.requireNonNull(level);
/*    */           return Optional.<UUID>ofNullable(this.uuid).map(level::getEntity);
/*    */         });
/*    */   }
/*    */   
/*    */   public Optional<Entity> getProjectileOwner(ServerLevel level) {
/* 53 */     return getEntity(level)
/* 54 */       .filter(e -> e instanceof Projectile)
/* 55 */       .map(e -> (Projectile)e)
/* 56 */       .map(Projectile::getOwner)
/* 57 */       .or(() -> {
/*    */           Objects.requireNonNull(level);
/*    */           return Optional.<UUID>ofNullable(this.projectileOwnerUuid).map(level::getEntity);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */