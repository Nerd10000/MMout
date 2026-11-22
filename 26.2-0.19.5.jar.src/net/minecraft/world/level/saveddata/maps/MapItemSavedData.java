/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.MapDecorations;
/*     */ import net.minecraft.world.item.component.MapItemColor;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ 
/*     */ public class MapItemSavedData extends SavedData {
/*     */   private static final int MAP_SIZE = 128;
/*     */   
/*     */   private static final int HALF_MAP_SIZE = 64;
/*     */   
/*     */   public static final int MAX_SCALE = 4;
/*     */   
/*     */   public static final int TRACKED_DECORATION_LIMIT = 256;
/*     */   
/*     */   private static final String FRAME_PREFIX = "frame-";
/*     */   
/*     */   public static final Codec<MapItemSavedData> CODEC;
/*     */   
/*     */   public final int centerX;
/*     */   
/*     */   public final int centerZ;
/*     */   
/*     */   public final ResourceKey<Level> dimension;
/*     */   
/*     */   private final boolean trackingPosition;
/*     */   
/*     */   private final boolean unlimitedTracking;
/*     */   
/*     */   public final byte scale;
/*     */   
/*     */   static {
/*  52 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(()), (App)Codec.INT.fieldOf("xCenter").forGetter(()), (App)Codec.INT.fieldOf("zCenter").forGetter(()), (App)Codec.BYTE.optionalFieldOf("scale", Byte.valueOf((byte)0)).forGetter(()), (App)Codec.BYTE_BUFFER.fieldOf("colors").forGetter(()), (App)Codec.BOOL.optionalFieldOf("trackingPosition", Boolean.valueOf(true)).forGetter(()), (App)Codec.BOOL.optionalFieldOf("unlimitedTracking", Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.optionalFieldOf("locked", Boolean.valueOf(false)).forGetter(()), (App)MapBanner.CODEC.listOf().optionalFieldOf("banners", List.of()).forGetter(()), (App)MapFrame.CODEC.listOf().optionalFieldOf("frames", List.of()).forGetter(())).apply((Applicative)i, MapItemSavedData::new));
/*     */   }
/*     */   
/*     */   public static final class MapPatch extends Record {
/*     */     private final int startX;
/*     */     
/*     */     private final int startY;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private final byte[] mapColors;
/*     */     
/*     */     public MapPatch(int startX, int startY, int width, int height, byte[] mapColors) {
/*  65 */       this.startX = startX;
/*  65 */       this.startY = startY;
/*  65 */       this.width = width;
/*  65 */       this.height = height;
/*  65 */       this.mapColors = mapColors;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #65	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #65	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #65	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int startX() {
/*  65 */       return this.startX;
/*     */     }
/*     */     
/*     */     public int startY() {
/*  65 */       return this.startY;
/*     */     }
/*     */     
/*     */     public int width() {
/*  65 */       return this.width;
/*     */     }
/*     */     
/*     */     public int height() {
/*  65 */       return this.height;
/*     */     }
/*     */     
/*     */     public byte[] mapColors() {
/*  65 */       return this.mapColors;
/*     */     }
/*     */     
/*  66 */     public static final StreamCodec<ByteBuf, Optional<MapPatch>> STREAM_CODEC = StreamCodec.of(MapPatch::write, MapPatch::read);
/*     */     
/*     */     private static void write(ByteBuf output, Optional<MapPatch> optional) {
/*  69 */       if (optional.isPresent()) {
/*  70 */         MapPatch patch = optional.get();
/*  71 */         output.writeByte(patch.width);
/*  72 */         output.writeByte(patch.height);
/*  73 */         output.writeByte(patch.startX);
/*  74 */         output.writeByte(patch.startY);
/*  75 */         FriendlyByteBuf.writeByteArray(output, patch.mapColors);
/*     */       } else {
/*  77 */         output.writeByte(0);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Optional<MapPatch> read(ByteBuf input) {
/*  82 */       int width = input.readUnsignedByte();
/*  83 */       if (width > 0) {
/*  84 */         int height = input.readUnsignedByte();
/*  85 */         int startX = input.readUnsignedByte();
/*  86 */         int startY = input.readUnsignedByte();
/*  87 */         byte[] mapColors = FriendlyByteBuf.readByteArray(input);
/*  88 */         return Optional.of(new MapPatch(startX, startY, width, height, mapColors));
/*     */       } 
/*  90 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     public void applyToMap(MapItemSavedData map) {
/*  95 */       for (int x = 0; x < this.width; x++) {
/*  96 */         for (int y = 0; y < this.height; y++)
/*  97 */           map.setColor(this.startX + x, this.startY + y, this.mapColors[x + y * this.width]); 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class HoldingPlayer {
/*     */     public final Player player;
/*     */     
/*     */     private boolean dirtyData = true;
/*     */     
/*     */     private int minDirtyX;
/*     */     
/*     */     private int minDirtyY;
/*     */     
/* 108 */     private int maxDirtyX = 127;
/*     */     
/* 109 */     private int maxDirtyY = 127;
/*     */     
/*     */     private boolean dirtyDecorations = true;
/*     */     
/*     */     private int tick;
/*     */     
/*     */     public int step;
/*     */     
/*     */     private HoldingPlayer(Player player) {
/* 115 */       this.player = player;
/*     */     }
/*     */     
/*     */     private MapItemSavedData.MapPatch createPatch() {
/* 119 */       int startX = this.minDirtyX;
/* 120 */       int startY = this.minDirtyY;
/* 121 */       int width = this.maxDirtyX + 1 - this.minDirtyX;
/* 122 */       int height = this.maxDirtyY + 1 - this.minDirtyY;
/* 124 */       byte[] patch = new byte[width * height];
/* 125 */       for (int x = 0; x < width; x++) {
/* 126 */         for (int y = 0; y < height; y++)
/* 127 */           patch[x + y * width] = MapItemSavedData.this.colors[startX + x + (startY + y) * 128]; 
/*     */       } 
/* 130 */       return new MapItemSavedData.MapPatch(startX, startY, width, height, patch);
/*     */     }
/*     */     
/*     */     private Packet<?> nextUpdatePacket(MapId id) {
/*     */       MapItemSavedData.MapPatch patch;
/*     */       Collection<MapDecoration> decorations;
/* 135 */       if (this.dirtyData) {
/* 136 */         this.dirtyData = false;
/* 137 */         patch = createPatch();
/*     */       } else {
/* 139 */         patch = null;
/*     */       } 
/* 143 */       if (this.dirtyDecorations && this.tick++ % 5 == 0) {
/* 144 */         this.dirtyDecorations = false;
/* 145 */         decorations = MapItemSavedData.this.decorations.values();
/*     */       } else {
/* 147 */         decorations = null;
/*     */       } 
/* 150 */       if (decorations != null || patch != null)
/* 151 */         return (Packet<?>)new ClientboundMapItemDataPacket(id, MapItemSavedData.this.scale, MapItemSavedData.this.locked, decorations, patch); 
/* 154 */       return null;
/*     */     }
/*     */     
/*     */     private void markColorsDirty(int x, int y) {
/* 158 */       if (this.dirtyData) {
/* 159 */         this.minDirtyX = Math.min(this.minDirtyX, x);
/* 160 */         this.minDirtyY = Math.min(this.minDirtyY, y);
/* 161 */         this.maxDirtyX = Math.max(this.maxDirtyX, x);
/* 162 */         this.maxDirtyY = Math.max(this.maxDirtyY, y);
/*     */       } else {
/* 164 */         this.dirtyData = true;
/* 165 */         this.minDirtyX = x;
/* 166 */         this.minDirtyY = y;
/* 167 */         this.maxDirtyX = x;
/* 168 */         this.maxDirtyY = y;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void markDecorationsDirty() {
/* 173 */       this.dirtyDecorations = true;
/*     */     }
/*     */   }
/*     */   
/* 183 */   public byte[] colors = new byte[16384];
/*     */   
/*     */   public final boolean locked;
/*     */   
/* 185 */   private final List<HoldingPlayer> carriedBy = Lists.newArrayList();
/*     */   
/* 186 */   private final Map<Player, HoldingPlayer> carriedByPlayers = Maps.newHashMap();
/*     */   
/* 187 */   private final Map<String, MapBanner> bannerMarkers = Maps.newHashMap();
/*     */   
/* 188 */   private final Map<String, MapDecoration> decorations = Maps.newLinkedHashMap();
/*     */   
/* 189 */   private final Map<String, MapFrame> frameMarkers = Maps.newHashMap();
/*     */   
/*     */   private int trackedDecorationCount;
/*     */   
/*     */   public static SavedDataType<MapItemSavedData> type(MapId id) {
/* 193 */     return new SavedDataType(
/* 194 */         Identifier.withDefaultNamespace(id.key()), () -> {
/*     */           throw new IllegalStateException("Should never create an empty map saved data");
/* 194 */         }CODEC, DataFixTypes.SAVED_DATA_MAP_DATA);
/*     */   }
/*     */   
/*     */   private MapItemSavedData(int centerX, int centerZ, byte scale, boolean trackingPosition, boolean unlimitedTracking, boolean locked, ResourceKey<Level> dimension) {
/* 202 */     this.scale = scale;
/* 203 */     this.centerX = centerX;
/* 204 */     this.centerZ = centerZ;
/* 205 */     this.dimension = dimension;
/* 206 */     this.trackingPosition = trackingPosition;
/* 207 */     this.unlimitedTracking = unlimitedTracking;
/* 208 */     this.locked = locked;
/*     */   }
/*     */   
/*     */   private MapItemSavedData(ResourceKey<Level> dimension, int centerX, int centerZ, byte scale, ByteBuffer colors, boolean trackingPosition, boolean unlimitedTracking, boolean locked, List<MapBanner> banners, List<MapFrame> frames) {
/* 212 */     this(centerX, centerZ, (byte)Mth.clamp(scale, 0, 4), trackingPosition, unlimitedTracking, locked, dimension);
/* 214 */     if ((colors.array()).length == 16384)
/* 215 */       this.colors = colors.array(); 
/* 218 */     for (MapBanner banner : banners) {
/* 219 */       this.bannerMarkers.put(banner.getId(), banner);
/* 220 */       addDecoration(banner.getDecoration(), null, banner.getId(), banner.pos().getX(), banner.pos().getZ(), 180.0D, banner.name().orElse(null));
/*     */     } 
/* 223 */     for (MapFrame frame : frames) {
/* 224 */       this.frameMarkers.put(frame.getId(), frame);
/* 225 */       addDecoration(MapDecorationTypes.FRAME, null, getFrameKey(frame.entityId()), frame.pos().getX(), frame.pos().getZ(), frame.rotation(), null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static MapItemSavedData createFresh(double originX, double originY, byte scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension) {
/* 230 */     int size = 128 * (1 << scale);
/* 241 */     int areaX = Mth.floor((originX + 64.0D) / size);
/* 242 */     int areaZ = Mth.floor((originY + 64.0D) / size);
/* 244 */     int x = areaX * size + size / 2 - 64;
/* 245 */     int z = areaZ * size + size / 2 - 64;
/* 247 */     return new MapItemSavedData(x, z, scale, trackingPosition, unlimitedTracking, false, dimension);
/*     */   }
/*     */   
/*     */   public static MapItemSavedData createForClient(byte scale, boolean isLocked, ResourceKey<Level> dimension) {
/* 252 */     return new MapItemSavedData(0, 0, scale, false, false, isLocked, dimension);
/*     */   }
/*     */   
/*     */   public MapItemSavedData locked() {
/* 256 */     MapItemSavedData result = new MapItemSavedData(this.centerX, this.centerZ, this.scale, this.trackingPosition, this.unlimitedTracking, true, this.dimension);
/* 266 */     result.bannerMarkers.putAll(this.bannerMarkers);
/* 267 */     result.decorations.putAll(this.decorations);
/* 268 */     result.trackedDecorationCount = this.trackedDecorationCount;
/* 269 */     System.arraycopy(this.colors, 0, result.colors, 0, this.colors.length);
/* 271 */     return result;
/*     */   }
/*     */   
/*     */   public MapItemSavedData scaled() {
/* 275 */     return createFresh(this.centerX, this.centerZ, (byte)Mth.clamp(this.scale + 1, 0, 4), this.trackingPosition, this.unlimitedTracking, this.dimension);
/*     */   }
/*     */   
/*     */   private static Predicate<ItemStack> mapMatcher(ItemStack mapStack) {
/* 279 */     MapId mapId = (MapId)mapStack.get(DataComponents.MAP_ID);
/* 280 */     return stack -> (stack == mapStack) ? true : (
/*     */       
/* 284 */       (stack.is(mapStack.getItem()) && Objects.equals(mapId, stack.get(DataComponents.MAP_ID))));
/*     */   }
/*     */   
/*     */   public void tickCarriedBy(Player tickingPlayer, ItemStack itemStack, ItemFrame placedInFrame) {
/* 289 */     if (!this.carriedByPlayers.containsKey(tickingPlayer)) {
/* 290 */       HoldingPlayer holdingPlayer = new HoldingPlayer(tickingPlayer);
/* 291 */       this.carriedByPlayers.put(tickingPlayer, holdingPlayer);
/* 292 */       this.carriedBy.add(holdingPlayer);
/*     */     } 
/* 295 */     Predicate<ItemStack> mapMatcher = mapMatcher(itemStack);
/* 296 */     if (!tickingPlayer.getInventory().contains(mapMatcher))
/* 297 */       removeDecoration(tickingPlayer.getPlainTextName()); 
/* 300 */     for (int i = 0; i < this.carriedBy.size(); i++) {
/* 301 */       HoldingPlayer otherHoldingPlayer = this.carriedBy.get(i);
/* 302 */       Player otherPlayer = otherHoldingPlayer.player;
/* 303 */       String otherPlayerName = otherPlayer.getPlainTextName();
/* 305 */       if (otherPlayer.isRemoved() || (placedInFrame == null && !otherPlayer.getInventory().contains(mapMatcher))) {
/* 306 */         this.carriedByPlayers.remove(otherPlayer);
/* 307 */         this.carriedBy.remove(otherHoldingPlayer);
/* 308 */         removeDecoration(otherPlayerName);
/* 309 */       } else if (placedInFrame == null && otherPlayer.level().dimension() == this.dimension && this.trackingPosition) {
/* 310 */         addDecoration(MapDecorationTypes.PLAYER, (LevelAccessor)otherPlayer.level(), otherPlayerName, otherPlayer.getX(), otherPlayer.getZ(), otherPlayer.getYRot(), null);
/*     */       } 
/* 313 */       if (!otherPlayer.equals(tickingPlayer) && hasMapInvisibilityItemEquipped(otherPlayer))
/* 314 */         removeDecoration(otherPlayerName); 
/*     */     } 
/* 318 */     if (placedInFrame != null && this.trackingPosition) {
/* 319 */       BlockPos pos = placedInFrame.getPos();
/* 320 */       MapFrame existingFrame = this.frameMarkers.get(MapFrame.frameId(pos));
/* 323 */       if (existingFrame != null && placedInFrame.getId() != existingFrame.entityId() && this.frameMarkers.containsKey(existingFrame.getId()))
/* 324 */         removeDecoration(getFrameKey(existingFrame.entityId())); 
/* 326 */       MapFrame mapFrame = new MapFrame(pos, placedInFrame.getDirection().get2DDataValue() * 90, placedInFrame.getId());
/* 327 */       addDecoration(MapDecorationTypes.FRAME, (LevelAccessor)tickingPlayer.level(), getFrameKey(placedInFrame.getId()), pos.getX(), pos.getZ(), (placedInFrame.getDirection().get2DDataValue() * 90), null);
/* 328 */       MapFrame oldFrame = this.frameMarkers.put(mapFrame.getId(), mapFrame);
/* 329 */       if (!mapFrame.equals(oldFrame))
/* 330 */         setDirty(); 
/*     */     } 
/* 334 */     MapDecorations staticDecorations = (MapDecorations)itemStack.getOrDefault(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY);
/* 335 */     if (!this.decorations.keySet().containsAll(staticDecorations.decorations().keySet()))
/* 336 */       staticDecorations.decorations().forEach((id, entry) -> {
/*     */             if (!this.decorations.containsKey(id))
/*     */               addDecoration(entry.type(), (LevelAccessor)tickingPlayer.level(), id, entry.x(), entry.z(), entry.rotation(), null); 
/*     */           }); 
/*     */   }
/*     */   
/*     */   private static boolean hasMapInvisibilityItemEquipped(Player player) {
/* 345 */     for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
/* 346 */       if (equipmentSlot != EquipmentSlot.MAINHAND && equipmentSlot != EquipmentSlot.OFFHAND)
/* 350 */         if (player.getItemBySlot(equipmentSlot).is(ItemTags.MAP_INVISIBILITY_EQUIPMENT))
/* 351 */           return true;  
/*     */     } 
/* 355 */     return false;
/*     */   }
/*     */   
/*     */   private void removeDecoration(String string) {
/* 359 */     MapDecoration decoration = this.decorations.remove(string);
/* 360 */     if (decoration != null && ((MapDecorationType)decoration.type().value()).trackCount())
/* 361 */       this.trackedDecorationCount--; 
/* 363 */     setDecorationsDirty();
/*     */   }
/*     */   
/*     */   public static void addTargetDecoration(ItemStack itemStack, BlockPos position, String key, Holder<MapDecorationType> decorationType) {
/* 367 */     MapDecorations.Entry newDecoration = new MapDecorations.Entry(decorationType, position.getX(), position.getZ(), 180.0F);
/* 368 */     itemStack.update(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY, decorations -> decorations.withDecoration(key, newDecoration));
/* 371 */     if (((MapDecorationType)decorationType.value()).hasMapColor())
/* 372 */       itemStack.set(DataComponents.MAP_COLOR, new MapItemColor(((MapDecorationType)decorationType.value()).mapColor())); 
/*     */   }
/*     */   
/*     */   private void addDecoration(Holder<MapDecorationType> type, LevelAccessor level, String key, double xPos, double zPos, double yRot, Component name) {
/* 380 */     int scaling = 1 << this.scale;
/* 381 */     float xDeltaFromCenter = (float)(xPos - this.centerX) / scaling;
/* 382 */     float yDeltaFromCenter = (float)(zPos - this.centerZ) / scaling;
/* 384 */     MapDecorationLocation locationAndType = calculateDecorationLocationAndType(type, level, yRot, xDeltaFromCenter, yDeltaFromCenter);
/* 385 */     if (locationAndType == null) {
/* 386 */       removeDecoration(key);
/*     */       return;
/*     */     } 
/* 391 */     MapDecoration newDecoration = new MapDecoration(locationAndType.type(), locationAndType.x(), locationAndType.y(), locationAndType.rot(), Optional.ofNullable(name));
/* 392 */     MapDecoration previousDecoration = this.decorations.put(key, newDecoration);
/* 393 */     if (!newDecoration.equals(previousDecoration)) {
/* 394 */       if (previousDecoration != null && ((MapDecorationType)previousDecoration.type().value()).trackCount())
/* 395 */         this.trackedDecorationCount--; 
/* 397 */       if (((MapDecorationType)locationAndType.type().value()).trackCount())
/* 398 */         this.trackedDecorationCount++; 
/* 400 */       setDecorationsDirty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private MapDecorationLocation calculateDecorationLocationAndType(Holder<MapDecorationType> type, LevelAccessor level, double yRot, float xDeltaFromCenter, float yDeltaFromCenter) {
/* 405 */     byte clampedXDeltaFromCenter = clampMapCoordinate(xDeltaFromCenter);
/* 406 */     byte clampedYDeltaFromCenter = clampMapCoordinate(yDeltaFromCenter);
/* 408 */     if (type.is(MapDecorationTypes.PLAYER)) {
/* 409 */       Pair<Holder<MapDecorationType>, Byte> typeAndRotation = playerDecorationTypeAndRotation(type, level, yRot, xDeltaFromCenter, yDeltaFromCenter);
/* 410 */       return (typeAndRotation == null) ? null : new MapDecorationLocation((Holder<MapDecorationType>)typeAndRotation.getFirst(), clampedXDeltaFromCenter, clampedYDeltaFromCenter, ((Byte)typeAndRotation.getSecond()).byteValue());
/*     */     } 
/* 413 */     if (isInsideMap(xDeltaFromCenter, yDeltaFromCenter) || this.unlimitedTracking)
/* 414 */       return new MapDecorationLocation(type, clampedXDeltaFromCenter, clampedYDeltaFromCenter, calculateRotation(level, yRot)); 
/* 417 */     return null;
/*     */   }
/*     */   
/*     */   private Pair<Holder<MapDecorationType>, Byte> playerDecorationTypeAndRotation(Holder<MapDecorationType> type, LevelAccessor level, double yRot, float xDeltaFromCenter, float yDeltaFromCenter) {
/* 421 */     if (isInsideMap(xDeltaFromCenter, yDeltaFromCenter))
/* 422 */       return Pair.of(type, Byte.valueOf(calculateRotation(level, yRot))); 
/* 424 */     Holder<MapDecorationType> outsideMapDecorationType = decorationTypeForPlayerOutsideMap(xDeltaFromCenter, yDeltaFromCenter);
/* 425 */     if (outsideMapDecorationType == null)
/* 426 */       return null; 
/* 428 */     return Pair.of(outsideMapDecorationType, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   private byte calculateRotation(LevelAccessor level, double yRot) {
/* 432 */     if (this.dimension == Level.NETHER && level != null) {
/* 433 */       int s = (int)(level.getGameTime() / 10L);
/* 434 */       return (byte)(s * s * 34187121 + s * 121 >> 15 & 0xF);
/*     */     } 
/* 437 */     double adjustedYRot = (yRot < 0.0D) ? (yRot - 8.0D) : (yRot + 8.0D);
/* 438 */     return (byte)(int)(adjustedYRot * 16.0D / 360.0D);
/*     */   }
/*     */   
/*     */   private static boolean isInsideMap(float xd, float yd) {
/* 442 */     int halfSize = 63;
/* 443 */     return (xd >= -63.0F && yd >= -63.0F && xd <= 63.0F && yd <= 63.0F);
/*     */   }
/*     */   
/*     */   private Holder<MapDecorationType> decorationTypeForPlayerOutsideMap(float xDeltaFromCenter, float yDeltaFromCenter) {
/* 447 */     int rangeLimit = 320;
/* 448 */     boolean isWithinLimits = (Math.abs(xDeltaFromCenter) < 320.0F && Math.abs(yDeltaFromCenter) < 320.0F);
/* 449 */     if (isWithinLimits)
/* 450 */       return MapDecorationTypes.PLAYER_OFF_MAP; 
/* 452 */     return this.unlimitedTracking ? 
/* 453 */       MapDecorationTypes.PLAYER_OFF_LIMITS : 
/* 454 */       null;
/*     */   }
/*     */   
/*     */   private static byte clampMapCoordinate(float deltaFromCenter) {
/* 458 */     int halfSize = 63;
/* 459 */     if (deltaFromCenter <= -63.0F)
/* 460 */       return Byte.MIN_VALUE; 
/* 462 */     if (deltaFromCenter >= 63.0F)
/* 463 */       return Byte.MAX_VALUE; 
/* 465 */     return (byte)(int)((deltaFromCenter * 2.0F) + 0.5D);
/*     */   }
/*     */   
/*     */   private static final class MapDecorationLocation extends Record {
/*     */     private final Holder<MapDecorationType> type;
/*     */     
/*     */     private final byte x;
/*     */     
/*     */     private final byte y;
/*     */     
/*     */     private final byte rot;
/*     */     
/*     */     private MapDecorationLocation(Holder<MapDecorationType> type, byte x, byte y, byte rot) {
/* 468 */       this.type = type;
/* 468 */       this.x = x;
/* 468 */       this.y = y;
/* 468 */       this.rot = rot;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #468	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #468	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #468	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<MapDecorationType> type() {
/* 468 */       return this.type;
/*     */     }
/*     */     
/*     */     public byte x() {
/* 468 */       return this.x;
/*     */     }
/*     */     
/*     */     public byte y() {
/* 468 */       return this.y;
/*     */     }
/*     */     
/*     */     public byte rot() {
/* 468 */       return this.rot;
/*     */     }
/*     */   }
/*     */   
/*     */   public Packet<?> getUpdatePacket(MapId id, Player player) {
/* 471 */     HoldingPlayer holdingPlayer = this.carriedByPlayers.get(player);
/* 473 */     if (holdingPlayer == null)
/* 474 */       return null; 
/* 477 */     return holdingPlayer.nextUpdatePacket(id);
/*     */   }
/*     */   
/*     */   private void setColorsDirty(int x, int y) {
/* 481 */     setDirty();
/* 482 */     for (HoldingPlayer holdingPlayer : this.carriedBy)
/* 483 */       holdingPlayer.markColorsDirty(x, y); 
/*     */   }
/*     */   
/*     */   private void setDecorationsDirty() {
/* 489 */     this.carriedBy.forEach(HoldingPlayer::markDecorationsDirty);
/*     */   }
/*     */   
/*     */   public HoldingPlayer getHoldingPlayer(Player player) {
/* 493 */     HoldingPlayer holdingPlayer = this.carriedByPlayers.get(player);
/* 495 */     if (holdingPlayer == null) {
/* 496 */       holdingPlayer = new HoldingPlayer(player);
/* 497 */       this.carriedByPlayers.put(player, holdingPlayer);
/* 498 */       this.carriedBy.add(holdingPlayer);
/*     */     } 
/* 501 */     return holdingPlayer;
/*     */   }
/*     */   
/*     */   public boolean toggleBanner(LevelAccessor level, BlockPos pos) {
/* 505 */     double xPos = pos.getX() + 0.5D;
/* 506 */     double zPos = pos.getZ() + 0.5D;
/* 507 */     int scale = 1 << this.scale;
/* 508 */     double xd = (xPos - this.centerX) / scale;
/* 509 */     double yd = (zPos - this.centerZ) / scale;
/* 510 */     int halfSize = 63;
/* 511 */     if (xd >= -63.0D && yd >= -63.0D && xd <= 63.0D && yd <= 63.0D) {
/* 512 */       MapBanner banner = MapBanner.fromWorld((BlockGetter)level, pos);
/* 513 */       if (banner == null)
/* 514 */         return false; 
/* 517 */       if (this.bannerMarkers.remove(banner.getId(), banner)) {
/* 518 */         removeDecoration(banner.getId());
/* 519 */         setDirty();
/* 520 */         return true;
/*     */       } 
/* 521 */       if (!isTrackedCountOverLimit(256)) {
/* 522 */         this.bannerMarkers.put(banner.getId(), banner);
/* 523 */         addDecoration(banner.getDecoration(), level, banner.getId(), xPos, zPos, 180.0D, banner.name().orElse(null));
/* 524 */         setDirty();
/* 525 */         return true;
/*     */       } 
/*     */     } 
/* 528 */     return false;
/*     */   }
/*     */   
/*     */   public void checkBanners(BlockGetter level, int x, int z) {
/* 532 */     for (Iterator<MapBanner> iterator = this.bannerMarkers.values().iterator(); iterator.hasNext(); ) {
/* 533 */       MapBanner expected = iterator.next();
/* 534 */       if (expected.pos().getX() == x && expected.pos().getZ() == z) {
/* 535 */         MapBanner current = MapBanner.fromWorld(level, expected.pos());
/* 536 */         if (!expected.equals(current)) {
/* 537 */           iterator.remove();
/* 538 */           removeDecoration(expected.getId());
/* 539 */           setDirty();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection<MapBanner> getBanners() {
/* 546 */     return this.bannerMarkers.values();
/*     */   }
/*     */   
/*     */   public void removedFromFrame(BlockPos pos, int entityID) {
/* 550 */     removeDecoration(getFrameKey(entityID));
/* 551 */     this.frameMarkers.remove(MapFrame.frameId(pos));
/* 552 */     setDirty();
/*     */   }
/*     */   
/*     */   public boolean updateColor(int x, int y, byte newColor) {
/* 556 */     byte oldColor = this.colors[x + y * 128];
/* 557 */     if (oldColor != newColor) {
/* 558 */       setColor(x, y, newColor);
/* 559 */       return true;
/*     */     } 
/* 561 */     return false;
/*     */   }
/*     */   
/*     */   public void setColor(int x, int y, byte newColor) {
/* 565 */     this.colors[x + y * 128] = newColor;
/* 566 */     setColorsDirty(x, y);
/*     */   }
/*     */   
/*     */   public boolean isExplorationMap() {
/* 570 */     for (MapDecoration decoration : this.decorations.values()) {
/* 571 */       if (((MapDecorationType)decoration.type().value()).explorationMapElement())
/* 572 */         return true; 
/*     */     } 
/* 575 */     return false;
/*     */   }
/*     */   
/*     */   public void addClientSideDecorations(List<MapDecoration> decorations) {
/* 579 */     this.decorations.clear();
/* 580 */     this.trackedDecorationCount = 0;
/* 581 */     for (int i = 0; i < decorations.size(); i++) {
/* 582 */       MapDecoration decoration = decorations.get(i);
/* 583 */       this.decorations.put("icon-" + i, decoration);
/* 584 */       if (((MapDecorationType)decoration.type().value()).trackCount())
/* 585 */         this.trackedDecorationCount++; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Iterable<MapDecoration> getDecorations() {
/* 591 */     return this.decorations.values();
/*     */   }
/*     */   
/*     */   public boolean isTrackedCountOverLimit(int limit) {
/* 595 */     return (this.trackedDecorationCount > limit);
/*     */   }
/*     */   
/*     */   private static String getFrameKey(int id) {
/* 599 */     return "frame-" + id;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\maps\MapItemSavedData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */