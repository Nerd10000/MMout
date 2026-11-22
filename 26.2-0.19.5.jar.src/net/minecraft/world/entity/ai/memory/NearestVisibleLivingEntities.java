/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.VisibleForDebug;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*    */ 
/*    */ public class NearestVisibleLivingEntities {
/* 20 */   private static final NearestVisibleLivingEntities EMPTY = new NearestVisibleLivingEntities();
/*    */   
/*    */   private final List<LivingEntity> nearbyEntities;
/*    */   
/*    */   private final Predicate<LivingEntity> lineOfSightTest;
/*    */   
/*    */   private NearestVisibleLivingEntities() {
/* 25 */     this.nearbyEntities = List.of();
/* 26 */     this.lineOfSightTest = (ignored -> false);
/*    */   }
/*    */   
/*    */   public NearestVisibleLivingEntities(ServerLevel level, LivingEntity body, List<LivingEntity> livingEntities) {
/* 30 */     this.nearbyEntities = livingEntities;
/* 31 */     Object2BooleanOpenHashMap<LivingEntity> cache = new Object2BooleanOpenHashMap(livingEntities.size());
/* 32 */     Predicate<LivingEntity> targetTest = targetEntity -> Sensor.isEntityTargetable(level, body, targetEntity);
/* 33 */     this.lineOfSightTest = (otherEntity -> cache.computeIfAbsent(otherEntity, targetTest));
/*    */   }
/*    */   
/*    */   public static NearestVisibleLivingEntities empty() {
/* 37 */     return EMPTY;
/*    */   }
/*    */   
/*    */   @VisibleForDebug
/*    */   public List<LivingEntity> nearbyEntities() {
/* 42 */     return this.nearbyEntities;
/*    */   }
/*    */   
/*    */   public Optional<LivingEntity> findClosest(Predicate<LivingEntity> filter) {
/* 46 */     for (LivingEntity nearbyEntity : this.nearbyEntities) {
/* 47 */       if (filter.test(nearbyEntity) && this.lineOfSightTest.test(nearbyEntity))
/* 48 */         return Optional.of(nearbyEntity); 
/*    */     } 
/* 51 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public Iterable<LivingEntity> findAll(Predicate<LivingEntity> filter) {
/* 58 */     return Iterables.filter(this.nearbyEntities, entity -> (filter.test(entity) && this.lineOfSightTest.test(entity)));
/*    */   }
/*    */   
/*    */   public Stream<LivingEntity> find(Predicate<LivingEntity> filter) {
/* 66 */     return this.nearbyEntities.stream()
/* 67 */       .filter(entity -> (filter.test(entity) && this.lineOfSightTest.test(entity)));
/*    */   }
/*    */   
/*    */   public boolean contains(LivingEntity targetEntity) {
/* 71 */     return (this.nearbyEntities.contains(targetEntity) && this.lineOfSightTest.test(targetEntity));
/*    */   }
/*    */   
/*    */   public boolean contains(Predicate<LivingEntity> filter) {
/* 75 */     for (LivingEntity nearbyEntity : this.nearbyEntities) {
/* 76 */       if (filter.test(nearbyEntity) && this.lineOfSightTest.test(nearbyEntity))
/* 77 */         return true; 
/*    */     } 
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\memory\NearestVisibleLivingEntities.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */