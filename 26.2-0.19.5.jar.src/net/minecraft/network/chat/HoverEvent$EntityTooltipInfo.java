/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ 
/*     */ public class EntityTooltipInfo {
/*     */   public static final MapCodec<EntityTooltipInfo> CODEC;
/*     */   
/*     */   public final EntityType<?> type;
/*     */   
/*     */   public final UUID uuid;
/*     */   
/*     */   public final Optional<Component> name;
/*     */   
/*     */   private List<Component> linesCache;
/*     */   
/*     */   static {
/*  57 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id").forGetter(()), (App)UUIDUtil.LENIENT_CODEC.fieldOf("uuid").forGetter(()), (App)ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(())).apply((Applicative)i, EntityTooltipInfo::new));
/*     */   }
/*     */   
/*     */   public EntityTooltipInfo(EntityType<?> type, UUID uuid, Component name) {
/*  70 */     this(type, uuid, Optional.ofNullable(name));
/*     */   }
/*     */   
/*     */   public EntityTooltipInfo(EntityType<?> type, UUID uuid, Optional<Component> name) {
/*  74 */     this.type = type;
/*  75 */     this.uuid = uuid;
/*  76 */     this.name = name;
/*     */   }
/*     */   
/*     */   public List<Component> getTooltipLines() {
/*  80 */     if (this.linesCache == null) {
/*  81 */       this.linesCache = new ArrayList<>();
/*  82 */       Objects.requireNonNull(this.linesCache);
/*  82 */       this.name.ifPresent(this.linesCache::add);
/*  83 */       this.linesCache.add(Component.translatable("gui.entity_tooltip.type", new Object[] { this.type.getDescription() }));
/*  84 */       this.linesCache.add(Component.literal(this.uuid.toString()));
/*     */     } 
/*  86 */     return this.linesCache;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  91 */     if (this == o)
/*  92 */       return true; 
/*  94 */     if (o == null || getClass() != o.getClass())
/*  95 */       return false; 
/*  98 */     EntityTooltipInfo that = (EntityTooltipInfo)o;
/*  99 */     return (this.type.equals(that.type) && this.uuid.equals(that.uuid) && this.name.equals(that.name));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 104 */     int result = this.type.hashCode();
/* 105 */     result = 31 * result + this.uuid.hashCode();
/* 106 */     result = 31 * result + this.name.hashCode();
/* 107 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\HoverEvent$EntityTooltipInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */