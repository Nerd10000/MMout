/*      */ package net.minecraft.client.multiplayer;
/*      */ 
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.level.entity.LevelCallback;
/*      */ 
/*      */ final class EntityCallbacks implements LevelCallback<Entity> {
/*      */   public void onCreated(Entity entity) {}
/*      */   
/*      */   public void onDestroyed(Entity entity) {}
/*      */   
/*      */   public void onTickingStart(Entity entity) {
/* 1119 */     ClientLevel.this.tickingEntities.add(entity);
/*      */   }
/*      */   
/*      */   public void onTickingEnd(Entity entity) {
/* 1124 */     ClientLevel.this.tickingEntities.remove(entity);
/*      */   }
/*      */   
/*      */   public void onTrackingStart(Entity entity) {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: dup
/*      */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   5: pop
/*      */     //   6: astore_2
/*      */     //   7: iconst_0
/*      */     //   8: istore_3
/*      */     //   9: aload_2
/*      */     //   10: iload_3
/*      */     //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/world/entity/Entity;I)I
/*      */     //   16: lookupswitch default -> 98, 0 -> 44, 1 -> 68
/*      */     //   44: aload_2
/*      */     //   45: checkcast net/minecraft/client/player/AbstractClientPlayer
/*      */     //   48: astore #4
/*      */     //   50: aload_0
/*      */     //   51: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */     //   54: getfield players : Ljava/util/List;
/*      */     //   57: aload #4
/*      */     //   59: invokeinterface add : (Ljava/lang/Object;)Z
/*      */     //   64: pop
/*      */     //   65: goto -> 98
/*      */     //   68: aload_2
/*      */     //   69: checkcast net/minecraft/world/entity/boss/enderdragon/EnderDragon
/*      */     //   72: astore #5
/*      */     //   74: aload_0
/*      */     //   75: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */     //   78: getfield dragonParts : Ljava/util/List;
/*      */     //   81: aload #5
/*      */     //   83: invokevirtual getSubEntities : ()[Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*      */     //   86: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
/*      */     //   89: invokeinterface addAll : (Ljava/util/Collection;)Z
/*      */     //   94: pop
/*      */     //   95: goto -> 98
/*      */     //   98: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1129	-> 0
/*      */     //   #1130	-> 44
/*      */     //   #1131	-> 68
/*      */     //   #1135	-> 98
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   50	18	4	player	Lnet/minecraft/client/player/AbstractClientPlayer;
/*      */     //   74	24	5	dragon	Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;
/*      */     //   0	99	0	this	Lnet/minecraft/client/multiplayer/ClientLevel$EntityCallbacks;
/*      */     //   0	99	1	entity	Lnet/minecraft/world/entity/Entity;
/*      */   }
/*      */   
/*      */   public void onTrackingEnd(Entity entity) {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: invokevirtual unRide : ()V
/*      */     //   4: aload_1
/*      */     //   5: dup
/*      */     //   6: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   9: pop
/*      */     //   10: astore_2
/*      */     //   11: iconst_0
/*      */     //   12: istore_3
/*      */     //   13: aload_2
/*      */     //   14: iload_3
/*      */     //   15: <illegal opcode> typeSwitch : (Lnet/minecraft/world/entity/Entity;I)I
/*      */     //   20: lookupswitch default -> 102, 0 -> 48, 1 -> 72
/*      */     //   48: aload_2
/*      */     //   49: checkcast net/minecraft/client/player/AbstractClientPlayer
/*      */     //   52: astore #4
/*      */     //   54: aload_0
/*      */     //   55: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */     //   58: getfield players : Ljava/util/List;
/*      */     //   61: aload #4
/*      */     //   63: invokeinterface remove : (Ljava/lang/Object;)Z
/*      */     //   68: pop
/*      */     //   69: goto -> 102
/*      */     //   72: aload_2
/*      */     //   73: checkcast net/minecraft/world/entity/boss/enderdragon/EnderDragon
/*      */     //   76: astore #5
/*      */     //   78: aload_0
/*      */     //   79: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */     //   82: getfield dragonParts : Ljava/util/List;
/*      */     //   85: aload #5
/*      */     //   87: invokevirtual getSubEntities : ()[Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*      */     //   90: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
/*      */     //   93: invokeinterface removeAll : (Ljava/util/Collection;)Z
/*      */     //   98: pop
/*      */     //   99: goto -> 102
/*      */     //   102: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1139	-> 0
/*      */     //   #1140	-> 4
/*      */     //   #1141	-> 48
/*      */     //   #1142	-> 72
/*      */     //   #1146	-> 102
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   54	18	4	player	Lnet/minecraft/client/player/AbstractClientPlayer;
/*      */     //   78	24	5	dragon	Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;
/*      */     //   0	103	0	this	Lnet/minecraft/client/multiplayer/ClientLevel$EntityCallbacks;
/*      */     //   0	103	1	entity	Lnet/minecraft/world/entity/Entity;
/*      */   }
/*      */   
/*      */   public void onSectionChange(Entity entity) {}
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientLevel$EntityCallbacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */