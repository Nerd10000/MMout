/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class LevelDatToSavedDataPreparationFix extends DataFix {
/*    */   public LevelDatToSavedDataPreparationFix(Schema outputSchema) {
/* 14 */     super(outputSchema, true);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     Type<?> levelType = getInputSchema().getType(References.LEVEL);
/* 20 */     return writeFixAndRead("LevelSplitSavedDataPreparationFix", levelType, getOutputSchema().getType(References.LEVEL), levelData -> {
/*    */           levelData = fixDragonFight(levelData);
/*    */           levelData = fixWanderingTrader(levelData);
/*    */           levelData = fixWeatherData(levelData);
/*    */           levelData = fixScheduledEvents(levelData);
/*    */           return fixWorldGenSettings(levelData);
/*    */         });
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixDragonFight(Dynamic<?> levelData) {
/* 31 */     return levelData.renameAndFixField("DragonFight", "dragon_fight", dragonFight -> {
/*    */           Dynamic<?> newFight = dragonFight.renameField("NeedsStateScanning", "needs_state_scanning").renameField("DragonKilled", "dragon_killed").renameField("PreviouslyKilled", "previously_killed").renameField("Dragon", "dragon_uuid").renameField("ExitPortalLocation", "exit_portal_location").renameField("Gateways", "gateways");
/*    */           boolean isRespawning = dragonFight.get("IsRespawning").asBoolean(false);
/*    */           if (isRespawning)
/*    */             newFight = newFight.set("respawn_stage", levelData.createString("start")).set("respawn_time", levelData.createInt(0)); 
/*    */           return newFight.remove("IsRespawning");
/*    */         });
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixWanderingTrader(Dynamic<?> levelData) {
/* 54 */     Dynamic<?> wanderingTraderData = levelData.emptyMap().set("spawn_delay", levelData.createInt(levelData.get("WanderingTraderSpawnDelay").asInt(24000))).set("spawn_chance", levelData.createInt(levelData.get("WanderingTraderSpawnChance").asInt(25)));
/* 57 */     return levelData
/* 58 */       .set("wandering_trader_migration_data", wanderingTraderData)
/* 59 */       .remove("WanderingTraderSpawnDelay")
/* 60 */       .remove("WanderingTraderSpawnChance")
/* 61 */       .remove("WanderingTraderId");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixWeatherData(Dynamic<?> levelData) {
/* 70 */     Dynamic<?> weatherData = levelData.emptyMap().set("clear_weather_time", levelData.createInt(levelData.get("clearWeatherTime").asInt(0))).set("rain_time", levelData.createInt(levelData.get("rainTime").asInt(0))).set("thunder_time", levelData.createInt(levelData.get("thunderTime").asInt(0))).set("raining", levelData.createBoolean(levelData.get("raining").asBoolean(false))).set("thundering", levelData.createBoolean(levelData.get("thundering").asBoolean(false)));
/* 71 */     return levelData
/* 72 */       .remove("clearWeatherTime")
/* 73 */       .remove("rainTime")
/* 74 */       .remove("thunderTime")
/* 75 */       .remove("raining")
/* 76 */       .remove("thundering")
/* 77 */       .set("weather_data", weatherData);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixScheduledEvents(Dynamic<?> levelData) {
/* 81 */     List<? extends Dynamic<?>> scheduledEvents = levelData.get("ScheduledEvents").asList(Function.identity());
/* 82 */     Dynamic<?> eventList = levelData.createList(scheduledEvents.stream().map(event -> event.renameField("Name", "id").renameField("TriggerTime", "trigger_time").renameAndFixField("Callback", "callback", ())));
/* 90 */     Dynamic<?> newEvents = levelData.emptyMap();
/* 91 */     newEvents = newEvents.set("events", eventList);
/* 92 */     return levelData
/* 93 */       .remove("ScheduledEvents")
/* 94 */       .set("scheduled_events", newEvents);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixWorldGenSettings(Dynamic<?> levelData) {
/* 98 */     return levelData.renameAndFixField("WorldGenSettings", "world_gen_settings", tag -> tag.renameField("generate_features", "generate_structures"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\LevelDatToSavedDataPreparationFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */