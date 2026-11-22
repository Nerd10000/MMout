/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ChunkHolder;
/*     */ import net.minecraft.server.level.ChunkLevel;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.server.level.Ticket;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TicketStorage extends SavedData {
/*     */   private static final int INITIAL_TICKET_LIST_CAPACITY = 4;
/*     */   
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  45 */   private static final Codec<Pair<ChunkPos, Ticket>> TICKET_ENTRY = Codec.mapPair(ChunkPos.CODEC
/*  46 */       .fieldOf("chunk_pos"), Ticket.CODEC)
/*     */     
/*  48 */     .codec();
/*     */   
/*     */   public static final Codec<TicketStorage> CODEC;
/*     */   
/*     */   static {
/*  50 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)TICKET_ENTRY.listOf().optionalFieldOf("tickets", List.of()).forGetter(TicketStorage::packTickets)).apply((Applicative)i, TicketStorage::fromPacked));
/*     */   }
/*     */   
/*  54 */   public static final SavedDataType<TicketStorage> TYPE = new SavedDataType(Identifier.withDefaultNamespace("chunk_tickets"), TicketStorage::new, CODEC, DataFixTypes.SAVED_DATA_FORCED_CHUNKS);
/*     */   
/*     */   private final Long2ObjectOpenHashMap<List<Ticket>> tickets;
/*     */   
/*     */   private final Long2ObjectOpenHashMap<List<Ticket>> deactivatedTickets;
/*     */   
/*  58 */   private LongSet chunksWithForcedTickets = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   private ChunkUpdated loadingChunkUpdatedListener;
/*     */   
/*     */   private ChunkUpdated simulationChunkUpdatedListener;
/*     */   
/*     */   private TicketStorage(Long2ObjectOpenHashMap<List<Ticket>> tickets, Long2ObjectOpenHashMap<List<Ticket>> deactivatedTickets) {
/*  63 */     this.tickets = tickets;
/*  64 */     this.deactivatedTickets = deactivatedTickets;
/*  65 */     updateForcedChunks();
/*     */   }
/*     */   
/*     */   public TicketStorage() {
/*  69 */     this(new Long2ObjectOpenHashMap(4), new Long2ObjectOpenHashMap());
/*     */   }
/*     */   
/*     */   private static TicketStorage fromPacked(List<Pair<ChunkPos, Ticket>> tickets) {
/*  73 */     Long2ObjectOpenHashMap<List<Ticket>> ticketsToLoad = new Long2ObjectOpenHashMap();
/*  74 */     for (Pair<ChunkPos, Ticket> ticket : tickets) {
/*  75 */       ChunkPos pos = (ChunkPos)ticket.getFirst();
/*  76 */       List<Ticket> ticketsInChunk = (List<Ticket>)ticketsToLoad.computeIfAbsent(pos.pack(), k -> new ObjectArrayList(4));
/*  77 */       ticketsInChunk.add((Ticket)ticket.getSecond());
/*     */     } 
/*  80 */     return new TicketStorage(new Long2ObjectOpenHashMap(4), ticketsToLoad);
/*     */   }
/*     */   
/*     */   private List<Pair<ChunkPos, Ticket>> packTickets() {
/*  84 */     List<Pair<ChunkPos, Ticket>> tickets = new ArrayList<>();
/*  85 */     forEachTicket((pos, ticket) -> {
/*     */           if (ticket.getType().persist())
/*     */             tickets.add(new Pair(pos, ticket)); 
/*     */         });
/*  90 */     return tickets;
/*     */   }
/*     */   
/*     */   private void forEachTicket(BiConsumer<ChunkPos, Ticket> output) {
/*  94 */     forEachTicket(output, this.tickets);
/*  95 */     forEachTicket(output, this.deactivatedTickets);
/*     */   }
/*     */   
/*     */   private static void forEachTicket(BiConsumer<ChunkPos, Ticket> output, Long2ObjectOpenHashMap<List<Ticket>> tickets) {
/*  99 */     for (ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)tickets).iterator(); objectIterator.hasNext(); ) {
/*  99 */       Long2ObjectMap.Entry<List<Ticket>> entry = objectIterator.next();
/* 100 */       ChunkPos chunkPos = ChunkPos.unpack(entry.getLongKey());
/* 101 */       for (Ticket ticket : entry.getValue())
/* 102 */         output.accept(chunkPos, ticket); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void activateAllDeactivatedTickets() {
/* 108 */     for (ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)this.deactivatedTickets).iterator(); objectIterator.hasNext(); ) {
/* 108 */       Long2ObjectMap.Entry<List<Ticket>> entry = objectIterator.next();
/* 109 */       for (Ticket ticket : entry.getValue())
/* 110 */         addTicket(entry.getLongKey(), ticket); 
/*     */     } 
/* 113 */     this.deactivatedTickets.clear();
/*     */   }
/*     */   
/*     */   public void setLoadingChunkUpdatedListener(ChunkUpdated loadingChunkUpdatedListener) {
/* 117 */     this.loadingChunkUpdatedListener = loadingChunkUpdatedListener;
/*     */   }
/*     */   
/*     */   public void setSimulationChunkUpdatedListener(ChunkUpdated simulationChunkUpdatedListener) {
/* 121 */     this.simulationChunkUpdatedListener = simulationChunkUpdatedListener;
/*     */   }
/*     */   
/*     */   public boolean hasTickets() {
/* 125 */     return !this.tickets.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean shouldKeepDimensionActive() {
/* 129 */     for (ObjectIterator<List<Ticket>> objectIterator = this.tickets.values().iterator(); objectIterator.hasNext(); ) {
/* 129 */       List<Ticket> group = objectIterator.next();
/* 130 */       for (Ticket ticket : group) {
/* 131 */         if (ticket.getType().shouldKeepDimensionActive())
/* 132 */           return true; 
/*     */       } 
/*     */     } 
/* 136 */     return false;
/*     */   }
/*     */   
/*     */   public List<Ticket> getTickets(long key) {
/* 140 */     return (List<Ticket>)this.tickets.getOrDefault(key, List.of());
/*     */   }
/*     */   
/*     */   private List<Ticket> getOrCreateTickets(long key) {
/* 144 */     return (List<Ticket>)this.tickets.computeIfAbsent(key, k -> new ObjectArrayList(4));
/*     */   }
/*     */   
/*     */   public void addTicketWithRadius(TicketType type, ChunkPos chunkPos, int radius) {
/* 148 */     Ticket ticket = new Ticket(type, ChunkLevel.byStatus(FullChunkStatus.FULL) - radius);
/* 149 */     addTicket(chunkPos.pack(), ticket);
/*     */   }
/*     */   
/*     */   public void addTicket(Ticket ticket, ChunkPos chunkPos) {
/* 153 */     addTicket(chunkPos.pack(), ticket);
/*     */   }
/*     */   
/*     */   public boolean addTicket(long key, Ticket ticket) {
/* 157 */     List<Ticket> tickets = getOrCreateTickets(key);
/* 158 */     for (Ticket t : tickets) {
/* 159 */       if (isTicketSameTypeAndLevel(ticket, t)) {
/* 160 */         t.resetTicksLeft();
/* 161 */         setDirty();
/* 162 */         return false;
/*     */       } 
/*     */     } 
/* 166 */     int oldSimulationTicketLevel = getTicketLevelAt(tickets, true);
/* 167 */     int oldLoadingTicketLevel = getTicketLevelAt(tickets, false);
/* 168 */     tickets.add(ticket);
/* 170 */     if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS)
/* 171 */       LOGGER.debug("ATI {} {}", ChunkPos.unpack(key), ticket); 
/* 173 */     if (ticket.getType().doesSimulate() && 
/* 174 */       ticket.getTicketLevel() < oldSimulationTicketLevel && this.simulationChunkUpdatedListener != null)
/* 175 */       this.simulationChunkUpdatedListener.update(key, ticket.getTicketLevel(), true); 
/* 178 */     if (ticket.getType().doesLoad() && 
/* 179 */       ticket.getTicketLevel() < oldLoadingTicketLevel && this.loadingChunkUpdatedListener != null)
/* 180 */       this.loadingChunkUpdatedListener.update(key, ticket.getTicketLevel(), true); 
/* 183 */     if (ticket.getType().equals(TicketType.FORCED))
/* 184 */       this.chunksWithForcedTickets.add(key); 
/* 186 */     setDirty();
/* 187 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isTicketSameTypeAndLevel(Ticket ticket, Ticket t) {
/* 191 */     return (t.getType() == ticket.getType() && t.getTicketLevel() == ticket.getTicketLevel());
/*     */   }
/*     */   
/*     */   public int getTicketLevelAt(long key, boolean simulation) {
/* 195 */     return getTicketLevelAt(getTickets(key), simulation);
/*     */   }
/*     */   
/*     */   private static int getTicketLevelAt(List<Ticket> tickets, boolean simulation) {
/* 199 */     Ticket lowestTicket = getLowestTicket(tickets, simulation);
/* 200 */     return (lowestTicket == null) ? (ChunkLevel.MAX_LEVEL + 1) : lowestTicket.getTicketLevel();
/*     */   }
/*     */   
/*     */   private static Ticket getLowestTicket(List<Ticket> tickets, boolean simulation) {
/* 204 */     if (tickets == null)
/* 205 */       return null; 
/* 207 */     Ticket t = null;
/* 208 */     for (Ticket ticket : tickets) {
/* 209 */       if (t == null || ticket.getTicketLevel() < t.getTicketLevel()) {
/* 210 */         if (simulation && ticket.getType().doesSimulate()) {
/* 211 */           t = ticket;
/*     */           continue;
/*     */         } 
/* 212 */         if (!simulation && ticket.getType().doesLoad())
/* 213 */           t = ticket; 
/*     */       } 
/*     */     } 
/* 217 */     return t;
/*     */   }
/*     */   
/*     */   public void removeTicketWithRadius(TicketType type, ChunkPos chunkPos, int radius) {
/* 221 */     Ticket ticket = new Ticket(type, ChunkLevel.byStatus(FullChunkStatus.FULL) - radius);
/* 222 */     removeTicket(chunkPos.pack(), ticket);
/*     */   }
/*     */   
/*     */   public void removeTicket(Ticket ticket, ChunkPos chunkPos) {
/* 226 */     removeTicket(chunkPos.pack(), ticket);
/*     */   }
/*     */   
/*     */   public boolean removeTicket(long key, Ticket ticket) {
/* 230 */     List<Ticket> tickets = (List<Ticket>)this.tickets.get(key);
/* 231 */     if (tickets == null)
/* 232 */       return false; 
/* 235 */     boolean found = false;
/* 236 */     for (Iterator<Ticket> iterator = tickets.iterator(); iterator.hasNext(); ) {
/* 237 */       Ticket t = iterator.next();
/* 238 */       if (isTicketSameTypeAndLevel(ticket, t)) {
/* 239 */         iterator.remove();
/* 240 */         if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS)
/* 241 */           LOGGER.debug("RTI {} {}", ChunkPos.unpack(key), t); 
/* 243 */         found = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 248 */     if (!found)
/* 249 */       return false; 
/* 252 */     if (tickets.isEmpty())
/* 253 */       this.tickets.remove(key); 
/* 256 */     if (ticket.getType().doesSimulate() && this.simulationChunkUpdatedListener != null)
/* 257 */       this.simulationChunkUpdatedListener.update(key, getTicketLevelAt(tickets, true), false); 
/* 259 */     if (ticket.getType().doesLoad() && this.loadingChunkUpdatedListener != null)
/* 260 */       this.loadingChunkUpdatedListener.update(key, getTicketLevelAt(tickets, false), false); 
/* 262 */     if (ticket.getType().equals(TicketType.FORCED))
/* 263 */       updateForcedChunks(); 
/* 265 */     setDirty();
/* 266 */     return true;
/*     */   }
/*     */   
/*     */   private void updateForcedChunks() {
/* 270 */     this.chunksWithForcedTickets = getAllChunksWithTicketThat(t -> t.getType().equals(TicketType.FORCED));
/*     */   }
/*     */   
/*     */   public String getTicketDebugString(long key, boolean simulation) {
/* 274 */     List<Ticket> tickets = getTickets(key);
/* 275 */     Ticket lowestTicket = getLowestTicket(tickets, simulation);
/* 276 */     return (lowestTicket == null) ? "no_ticket" : lowestTicket.toString();
/*     */   }
/*     */   
/*     */   public void purgeStaleTickets(ChunkMap chunkMap) {
/* 280 */     removeTicketIf((ticket, chunkPos) -> {
/*     */           if (canTicketExpire(chunkMap, ticket, chunkPos)) {
/*     */             ticket.decreaseTicksLeft();
/*     */             return ticket.isTimedOut();
/*     */           } 
/*     */           return false;
/* 280 */         }null);
/* 287 */     setDirty();
/*     */   }
/*     */   
/*     */   private boolean canTicketExpire(ChunkMap chunkMap, Ticket ticket, long chunkPos) {
/* 291 */     if (!ticket.getType().hasTimeout())
/* 292 */       return false; 
/* 294 */     if (ticket.getType().canExpireIfUnloaded())
/* 295 */       return true; 
/* 297 */     ChunkHolder updatingChunk = chunkMap.getUpdatingChunkIfPresent(chunkPos);
/* 301 */     return (updatingChunk == null || updatingChunk.isReadyForSaving());
/*     */   }
/*     */   
/*     */   public void deactivateTicketsOnClosing() {
/* 306 */     removeTicketIf((ticket, chunkPos) -> (ticket.getType() != TicketType.UNKNOWN), this.deactivatedTickets);
/*     */   }
/*     */   
/*     */   public void removeTicketIf(TicketPredicate predicate, Long2ObjectOpenHashMap<List<Ticket>> removedTickets) {
/* 310 */     ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> ticketsPerChunkIterator = this.tickets.long2ObjectEntrySet().fastIterator();
/* 311 */     boolean removedForced = false;
/* 312 */     while (ticketsPerChunkIterator.hasNext()) {
/* 313 */       Long2ObjectMap.Entry<List<Ticket>> entry = (Long2ObjectMap.Entry<List<Ticket>>)ticketsPerChunkIterator.next();
/* 314 */       Iterator<Ticket> chunkTicketsIterator = ((List<Ticket>)entry.getValue()).iterator();
/* 315 */       long chunkPos = entry.getLongKey();
/* 316 */       boolean removedSimulation = false;
/* 317 */       boolean removedLoading = false;
/* 318 */       while (chunkTicketsIterator.hasNext()) {
/* 319 */         Ticket ticket = chunkTicketsIterator.next();
/* 320 */         if (predicate.test(ticket, chunkPos)) {
/* 321 */           if (removedTickets != null) {
/* 322 */             List<Ticket> tickets = (List<Ticket>)removedTickets.computeIfAbsent(chunkPos, k -> new ObjectArrayList(((List)entry.getValue()).size()));
/* 323 */             tickets.add(ticket);
/*     */           } 
/* 325 */           chunkTicketsIterator.remove();
/* 326 */           if (ticket.getType().doesLoad())
/* 327 */             removedLoading = true; 
/* 329 */           if (ticket.getType().doesSimulate())
/* 330 */             removedSimulation = true; 
/* 332 */           if (ticket.getType().equals(TicketType.FORCED))
/* 333 */             removedForced = true; 
/*     */         } 
/*     */       } 
/* 337 */       if (!removedLoading && !removedSimulation)
/*     */         continue; 
/* 340 */       if (removedLoading && this.loadingChunkUpdatedListener != null)
/* 341 */         this.loadingChunkUpdatedListener.update(chunkPos, getTicketLevelAt((List<Ticket>)entry.getValue(), false), false); 
/* 343 */       if (removedSimulation && this.simulationChunkUpdatedListener != null)
/* 344 */         this.simulationChunkUpdatedListener.update(chunkPos, getTicketLevelAt((List<Ticket>)entry.getValue(), true), false); 
/* 346 */       setDirty();
/* 347 */       if (((List)entry.getValue()).isEmpty())
/* 348 */         ticketsPerChunkIterator.remove(); 
/*     */     } 
/* 351 */     if (removedForced)
/* 352 */       updateForcedChunks(); 
/*     */   }
/*     */   
/*     */   public void replaceTicketLevelOfType(int newLevel, TicketType ticketType) {
/* 357 */     List<Pair<Ticket, Long>> affectedTickets = new ArrayList<>();
/* 358 */     for (ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> objectIterator = this.tickets.long2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 358 */       Long2ObjectMap.Entry<List<Ticket>> entry = objectIterator.next();
/* 359 */       for (Ticket ticket : entry.getValue()) {
/* 360 */         if (ticket.getType() == ticketType)
/* 361 */           affectedTickets.add(Pair.of(ticket, Long.valueOf(entry.getLongKey()))); 
/*     */       } 
/*     */     } 
/* 365 */     for (Pair<Ticket, Long> pair : affectedTickets) {
/* 366 */       Long key = (Long)pair.getSecond();
/* 367 */       Ticket ticket = (Ticket)pair.getFirst();
/* 368 */       removeTicket(key.longValue(), ticket);
/* 369 */       TicketType type = ticket.getType();
/* 370 */       addTicket(key.longValue(), new Ticket(type, newLevel));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean updateChunkForced(ChunkPos chunkPos, boolean forced) {
/* 375 */     Ticket ticket = new Ticket(TicketType.FORCED, ChunkMap.FORCED_TICKET_LEVEL);
/* 376 */     if (forced)
/* 377 */       return addTicket(chunkPos.pack(), ticket); 
/* 379 */     return removeTicket(chunkPos.pack(), ticket);
/*     */   }
/*     */   
/*     */   public LongSet getForceLoadedChunks() {
/* 385 */     return this.chunksWithForcedTickets;
/*     */   }
/*     */   
/*     */   private LongSet getAllChunksWithTicketThat(Predicate<Ticket> ticketCheck) {
/* 389 */     LongOpenHashSet chunks = new LongOpenHashSet();
/* 390 */     for (ObjectIterator<Long2ObjectMap.Entry<List<Ticket>>> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)this.tickets).iterator(); objectIterator.hasNext(); ) {
/* 390 */       Long2ObjectMap.Entry<List<Ticket>> entry = objectIterator.next();
/* 391 */       for (Ticket ticket : entry.getValue()) {
/* 392 */         if (ticketCheck.test(ticket))
/* 393 */           chunks.add(entry.getLongKey()); 
/*     */       } 
/*     */     } 
/* 398 */     return (LongSet)chunks;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChunkUpdated {
/*     */     void update(long param1Long, int param1Int, boolean param1Boolean);
/*     */   }
/*     */   
/*     */   public static interface TicketPredicate {
/*     */     boolean test(Ticket param1Ticket, long param1Long);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\TicketStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */