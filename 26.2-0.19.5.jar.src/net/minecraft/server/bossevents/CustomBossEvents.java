/*    */ package net.minecraft.server.bossevents;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class CustomBossEvents extends SavedData {
/* 21 */   private static final Codec<Map<Identifier, CustomBossEvent.Packed>> EVENTS_CODEC = (Codec<Map<Identifier, CustomBossEvent.Packed>>)Codec.unboundedMap(Identifier.CODEC, CustomBossEvent.Packed.CODEC);
/*    */   
/*    */   private static final Codec<CustomBossEvents> CODEC;
/*    */   
/*    */   static {
/* 22 */     CODEC = EVENTS_CODEC.xmap(events -> {
/*    */           CustomBossEvents r = new CustomBossEvents();
/*    */           events.forEach(());
/*    */           return r;
/*    */         }c -> Util.mapValues(c.events, CustomBossEvent::pack));
/*    */   }
/*    */   
/* 29 */   public static final SavedDataType<CustomBossEvents> TYPE = new SavedDataType(
/* 30 */       Identifier.withDefaultNamespace("custom_boss_events"), CustomBossEvents::new, CODEC, DataFixTypes.SAVED_DATA_CUSTOM_BOSS_EVENTS);
/*    */   
/* 36 */   private final Map<Identifier, CustomBossEvent> events = Maps.newHashMap();
/*    */   
/*    */   public CustomBossEvent get(Identifier id) {
/* 39 */     return this.events.get(id);
/*    */   }
/*    */   
/*    */   public CustomBossEvent create(RandomSource random, Identifier id, Component name) {
/* 43 */     CustomBossEvent result = new CustomBossEvent(Mth.createInsecureUUID(random), id, name, this::setDirty);
/* 44 */     this.events.put(id, result);
/* 45 */     setDirty();
/* 46 */     return result;
/*    */   }
/*    */   
/*    */   public void remove(CustomBossEvent event) {
/* 50 */     if (this.events.remove(event.customId()) != null)
/* 51 */       setDirty(); 
/*    */   }
/*    */   
/*    */   public Collection<Identifier> getIds() {
/* 56 */     return this.events.keySet();
/*    */   }
/*    */   
/*    */   public Collection<CustomBossEvent> getEvents() {
/* 60 */     return this.events.values();
/*    */   }
/*    */   
/*    */   public void onPlayerConnect(ServerPlayer player) {
/* 64 */     for (CustomBossEvent event : this.events.values())
/* 65 */       event.onPlayerConnect(player); 
/*    */   }
/*    */   
/*    */   public void onPlayerDisconnect(ServerPlayer player) {
/* 70 */     for (CustomBossEvent event : this.events.values())
/* 71 */       event.onPlayerDisconnect(player); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\bossevents\CustomBossEvents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */