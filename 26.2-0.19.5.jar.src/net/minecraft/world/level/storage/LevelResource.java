/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ public final class LevelResource extends Record {
/*    */   private final String id;
/*    */   
/*    */   public LevelResource(String id) {
/*  5 */     this.id = id;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelResource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelResource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelResource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelResource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String id() {
/*  5 */     return this.id;
/*    */   }
/*    */   
/*  6 */   public static final LevelResource PLAYER_ADVANCEMENTS_DIR = new LevelResource("players/advancements");
/*    */   
/*  7 */   public static final LevelResource PLAYER_STATS_DIR = new LevelResource("players/stats");
/*    */   
/*  8 */   public static final LevelResource PLAYER_DATA_DIR = new LevelResource("players/data");
/*    */   
/*  9 */   public static final LevelResource PLAYER_OLD_DATA_DIR = new LevelResource("players");
/*    */   
/* 10 */   public static final LevelResource LEVEL_DATA_FILE = new LevelResource("level.dat");
/*    */   
/* 11 */   public static final LevelResource OLD_LEVEL_DATA_FILE = new LevelResource("level.dat_old");
/*    */   
/* 12 */   public static final LevelResource ICON_FILE = new LevelResource("icon.png");
/*    */   
/* 13 */   public static final LevelResource LOCK_FILE = new LevelResource("session.lock");
/*    */   
/* 14 */   public static final LevelResource GENERATED_DIR = new LevelResource("generated");
/*    */   
/* 15 */   public static final LevelResource DATAPACK_DIR = new LevelResource("datapacks");
/*    */   
/* 16 */   public static final LevelResource MAP_RESOURCE_FILE = new LevelResource("resourcepacks/resources.zip");
/*    */   
/* 17 */   public static final LevelResource DATA = new LevelResource("data");
/*    */   
/* 18 */   public static final LevelResource ROOT = new LevelResource(".");
/*    */   
/*    */   public String toString() {
/* 22 */     return "/" + this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelResource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */