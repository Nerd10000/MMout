/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.GameEventTags;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class GameEventTagsProvider extends TagsProvider<GameEvent> {
/*    */   @VisibleForTesting
/* 20 */   static final List<ResourceKey<GameEvent>> VIBRATIONS_EXCEPT_FLAP = List.of((ResourceKey<GameEvent>[])new ResourceKey[] { 
/* 20 */         GameEvent.BLOCK_ATTACH
/* 21 */         .key(), GameEvent.BLOCK_CHANGE
/* 22 */         .key(), GameEvent.BLOCK_CLOSE
/* 23 */         .key(), GameEvent.BLOCK_DESTROY
/* 24 */         .key(), GameEvent.BLOCK_DETACH
/* 25 */         .key(), GameEvent.BLOCK_OPEN
/* 26 */         .key(), GameEvent.BLOCK_PLACE
/* 27 */         .key(), GameEvent.BLOCK_ACTIVATE
/* 28 */         .key(), GameEvent.BLOCK_DEACTIVATE
/* 29 */         .key(), GameEvent.BOUNCE
/* 30 */         .key(), 
/* 30 */         GameEvent.CONTAINER_CLOSE
/* 31 */         .key(), GameEvent.CONTAINER_OPEN
/* 32 */         .key(), GameEvent.DRINK
/* 33 */         .key(), GameEvent.EAT
/* 34 */         .key(), GameEvent.ELYTRA_GLIDE
/* 35 */         .key(), GameEvent.ENTITY_DAMAGE
/* 36 */         .key(), GameEvent.ENTITY_DIE
/* 37 */         .key(), GameEvent.ENTITY_DISMOUNT
/* 38 */         .key(), GameEvent.ENTITY_INTERACT
/* 39 */         .key(), GameEvent.ENTITY_MOUNT
/* 40 */         .key(), 
/* 40 */         GameEvent.ENTITY_PLACE
/* 41 */         .key(), GameEvent.ENTITY_ACTION
/* 42 */         .key(), GameEvent.EQUIP
/* 43 */         .key(), GameEvent.EXPLODE
/* 44 */         .key(), GameEvent.FLUID_PICKUP
/*    */         
/* 46 */         .key(), GameEvent.FLUID_PLACE
/* 47 */         .key(), GameEvent.HIT_GROUND
/* 48 */         .key(), GameEvent.INSTRUMENT_PLAY
/* 49 */         .key(), GameEvent.ITEM_INTERACT_FINISH
/* 50 */         .key(), GameEvent.LIGHTNING_STRIKE
/* 51 */         .key(), 
/* 51 */         GameEvent.NOTE_BLOCK_PLAY
/* 52 */         .key(), GameEvent.PRIME_FUSE
/* 53 */         .key(), GameEvent.PROJECTILE_LAND
/* 54 */         .key(), GameEvent.PROJECTILE_SHOOT
/* 55 */         .key(), GameEvent.SHEAR
/* 56 */         .key(), GameEvent.SPLASH
/* 57 */         .key(), GameEvent.STEP
/* 58 */         .key(), GameEvent.SWIM
/* 59 */         .key(), GameEvent.TELEPORT
/* 60 */         .key(), GameEvent.UNEQUIP
/* 61 */         .key() });
/*    */   
/*    */   public GameEventTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 65 */     super(output, Registries.GAME_EVENT, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 70 */     tag(GameEventTags.VIBRATIONS).addAll(VIBRATIONS_EXCEPT_FLAP).addAll(VibrationSystem.RESONANCE_EVENTS).add(GameEvent.FLAP.key());
/* 72 */     tag(GameEventTags.SHRIEKER_CAN_LISTEN).add(GameEvent.SCULK_SENSOR_TENDRILS_CLICKING.key());
/* 74 */     tag(GameEventTags.WARDEN_CAN_LISTEN).addAll(VIBRATIONS_EXCEPT_FLAP).addAll(VibrationSystem.RESONANCE_EVENTS).add(GameEvent.SHRIEK.key()).addTag(GameEventTags.SHRIEKER_CAN_LISTEN);
/* 76 */     tag(GameEventTags.IGNORE_VIBRATIONS_SNEAKING).add((ResourceKey<GameEvent>[])new ResourceKey[] { GameEvent.HIT_GROUND
/* 77 */           .key(), GameEvent.PROJECTILE_SHOOT
/* 78 */           .key(), GameEvent.STEP
/* 79 */           .key(), GameEvent.SWIM
/* 80 */           .key(), GameEvent.ITEM_INTERACT_START
/* 81 */           .key(), GameEvent.ITEM_INTERACT_FINISH
/* 82 */           .key() });
/* 85 */     tag(GameEventTags.ALLAY_CAN_LISTEN).add(GameEvent.NOTE_BLOCK_PLAY.key());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\GameEventTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */