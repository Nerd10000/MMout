/*    */ package net.minecraft.world.clock;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface ClockTimeMarkers {
/*  8 */   public static final ResourceKey<? extends Registry<ClockTimeMarker>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("clock_time_marker"));
/*    */   
/* 10 */   public static final ResourceKey<ClockTimeMarker> DAY = createKey("day");
/*    */   
/* 11 */   public static final ResourceKey<ClockTimeMarker> NOON = createKey("noon");
/*    */   
/* 12 */   public static final ResourceKey<ClockTimeMarker> NIGHT = createKey("night");
/*    */   
/* 13 */   public static final ResourceKey<ClockTimeMarker> MIDNIGHT = createKey("midnight");
/*    */   
/* 15 */   public static final ResourceKey<ClockTimeMarker> WAKE_UP_FROM_SLEEP = createKey("wake_up_from_sleep");
/*    */   
/* 16 */   public static final ResourceKey<ClockTimeMarker> ROLL_VILLAGE_SIEGE = createKey("roll_village_siege");
/*    */   
/*    */   static ResourceKey<ClockTimeMarker> createKey(String name) {
/* 19 */     return ResourceKey.create(ROOT_ID, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\clock\ClockTimeMarkers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */