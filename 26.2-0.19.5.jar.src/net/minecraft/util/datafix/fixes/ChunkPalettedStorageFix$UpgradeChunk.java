/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ final class UpgradeChunk {
/*     */   private int sides;
/*     */   
/* 485 */   private final ChunkPalettedStorageFix.Section[] sections = new ChunkPalettedStorageFix.Section[16];
/*     */   
/*     */   private final Dynamic<?> level;
/*     */   
/*     */   private final int x;
/*     */   
/*     */   private final int z;
/*     */   
/* 490 */   private final Int2ObjectMap<Dynamic<?>> blockEntities = (Int2ObjectMap<Dynamic<?>>)new Int2ObjectLinkedOpenHashMap(16);
/*     */   
/*     */   public UpgradeChunk(Dynamic<?> level) {
/* 493 */     this.level = level;
/* 494 */     this.x = level.get("xPos").asInt(0) << 4;
/* 495 */     this.z = level.get("zPos").asInt(0) << 4;
/* 497 */     level.get("TileEntities").asStreamOpt().ifSuccess(s -> s.forEach(()));
/* 510 */     boolean convertedFromAlphaFormat = level.get("convertedFromAlphaFormat").asBoolean(false);
/* 512 */     level.get("Sections").asStreamOpt().ifSuccess(s -> s.forEach(()));
/* 520 */     for (ChunkPalettedStorageFix.Section section : this.sections) {
/* 521 */       if (section != null)
/* 525 */         for (ObjectIterator<Int2ObjectMap.Entry<IntList>> objectIterator = section.toFix.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) {
/*     */           IntListIterator<Integer> intListIterator;
/* 525 */           Int2ObjectMap.Entry<IntList> entry = objectIterator.next();
/* 526 */           int dy = section.y << 12;
/* 527 */           switch (entry.getIntKey()) {
/*     */             case 2:
/* 529 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 529 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 530 */                 pos |= dy;
/* 532 */                 Dynamic<?> state = getBlock(pos);
/* 533 */                 if ("minecraft:grass_block".equals(ChunkPalettedStorageFix.getName(state))) {
/* 534 */                   String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 535 */                   if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name))
/* 536 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_GRASS); 
/*     */                 } 
/*     */               } 
/*     */             case 3:
/* 542 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 542 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 543 */                 pos |= dy;
/* 545 */                 Dynamic<?> state = getBlock(pos);
/* 546 */                 if ("minecraft:podzol".equals(ChunkPalettedStorageFix.getName(state))) {
/* 547 */                   String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 548 */                   if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name))
/* 549 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_PODZOL); 
/*     */                 } 
/*     */               } 
/*     */             case 110:
/* 555 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 555 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 556 */                 pos |= dy;
/* 558 */                 Dynamic<?> state = getBlock(pos);
/* 559 */                 if ("minecraft:mycelium".equals(ChunkPalettedStorageFix.getName(state))) {
/* 560 */                   String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 561 */                   if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name))
/* 562 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_MYCELIUM); 
/*     */                 } 
/*     */               } 
/*     */             case 25:
/* 568 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 568 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 569 */                 pos |= dy;
/* 570 */                 Dynamic<?> entity = removeBlockEntity(pos);
/* 571 */                 if (entity != null) {
/* 572 */                   String key = Boolean.toString(entity.get("powered").asBoolean(false)) + Boolean.toString(entity.get("powered").asBoolean(false));
/* 573 */                   setBlock(pos, ChunkPalettedStorageFix.MappingConstants.NOTE_BLOCK_MAP.getOrDefault(key, ChunkPalettedStorageFix.MappingConstants.NOTE_BLOCK_MAP.get("false0")));
/*     */                 } 
/*     */               } 
/*     */             case 26:
/* 578 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 578 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 579 */                 pos |= dy;
/* 580 */                 Dynamic<?> entity = getBlockEntity(pos);
/* 581 */                 Dynamic<?> state = getBlock(pos);
/* 582 */                 if (entity != null) {
/* 583 */                   int color = entity.get("color").asInt(0);
/* 584 */                   if (color != 14 && color >= 0 && color < 16) {
/* 585 */                     String key = ChunkPalettedStorageFix.getProperty(state, "facing") + ChunkPalettedStorageFix.getProperty(state, "facing") + ChunkPalettedStorageFix.getProperty(state, "occupied") + ChunkPalettedStorageFix.getProperty(state, "part");
/* 586 */                     if (ChunkPalettedStorageFix.MappingConstants.BED_BLOCK_MAP.containsKey(key))
/* 587 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.BED_BLOCK_MAP.get(key)); 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             case 176:
/*     */             case 177:
/* 594 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 594 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 595 */                 pos |= dy;
/* 596 */                 Dynamic<?> entity = getBlockEntity(pos);
/* 597 */                 Dynamic<?> state = getBlock(pos);
/* 598 */                 if (entity != null) {
/* 599 */                   int color = entity.get("Base").asInt(0);
/* 600 */                   if (color != 15 && color >= 0 && color < 16) {
/* 601 */                     String key = ChunkPalettedStorageFix.getProperty(state, (entry.getIntKey() == 176) ? "rotation" : "facing") + "_" + ChunkPalettedStorageFix.getProperty(state, (entry.getIntKey() == 176) ? "rotation" : "facing");
/* 602 */                     if (ChunkPalettedStorageFix.MappingConstants.BANNER_BLOCK_MAP.containsKey(key))
/* 603 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.BANNER_BLOCK_MAP.get(key)); 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             case 86:
/* 610 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 610 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 611 */                 pos |= dy;
/* 613 */                 Dynamic<?> state = getBlock(pos);
/* 614 */                 if ("minecraft:carved_pumpkin".equals(ChunkPalettedStorageFix.getName(state))) {
/* 615 */                   String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.DOWN)));
/* 616 */                   if ("minecraft:grass_block".equals(name) || "minecraft:dirt".equals(name))
/* 617 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.PUMPKIN); 
/*     */                 } 
/*     */               } 
/*     */             case 140:
/* 623 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 623 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 624 */                 pos |= dy;
/* 625 */                 Dynamic<?> entity = removeBlockEntity(pos);
/* 626 */                 if (entity != null) {
/* 627 */                   String key = entity.get("Item").asString("") + entity.get("Item").asString("");
/* 628 */                   setBlock(pos, ChunkPalettedStorageFix.MappingConstants.FLOWER_POT_MAP.getOrDefault(key, ChunkPalettedStorageFix.MappingConstants.FLOWER_POT_MAP.get("minecraft:air0")));
/*     */                 } 
/*     */               } 
/*     */             case 144:
/* 633 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 633 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 634 */                 pos |= dy;
/* 635 */                 Dynamic<?> entity = getBlockEntity(pos);
/* 636 */                 if (entity != null) {
/* 637 */                   String key, type = String.valueOf(entity.get("SkullType").asInt(0));
/* 638 */                   String facing = ChunkPalettedStorageFix.getProperty(getBlock(pos), "facing");
/* 640 */                   if ("up".equals(facing) || "down".equals(facing)) {
/* 641 */                     key = type + type;
/*     */                   } else {
/* 643 */                     key = type + type;
/*     */                   } 
/* 646 */                   entity.remove("SkullType");
/* 647 */                   entity.remove("facing");
/* 648 */                   entity.remove("Rot");
/* 650 */                   setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SKULL_MAP.getOrDefault(key, ChunkPalettedStorageFix.MappingConstants.SKULL_MAP.get("0north")));
/*     */                 } 
/*     */               } 
/*     */             case 64:
/*     */             case 71:
/*     */             case 193:
/*     */             case 194:
/*     */             case 195:
/*     */             case 196:
/*     */             case 197:
/* 655 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 655 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 656 */                 pos |= dy;
/* 658 */                 Dynamic<?> state = getBlock(pos);
/* 659 */                 if (ChunkPalettedStorageFix.getName(state).endsWith("_door")) {
/* 660 */                   Dynamic<?> lower = getBlock(pos);
/* 661 */                   if ("lower".equals(ChunkPalettedStorageFix.getProperty(lower, "half"))) {
/* 662 */                     int abovePos = relative(pos, ChunkPalettedStorageFix.Direction.UP);
/* 663 */                     Dynamic<?> upper = getBlock(abovePos);
/* 664 */                     String name = ChunkPalettedStorageFix.getName(lower);
/* 665 */                     if (name.equals(ChunkPalettedStorageFix.getName(upper))) {
/* 666 */                       String facing = ChunkPalettedStorageFix.getProperty(lower, "facing");
/* 667 */                       String open = ChunkPalettedStorageFix.getProperty(lower, "open");
/* 668 */                       String hinge = convertedFromAlphaFormat ? "left" : ChunkPalettedStorageFix.getProperty(upper, "hinge");
/* 669 */                       String powered = convertedFromAlphaFormat ? "false" : ChunkPalettedStorageFix.getProperty(upper, "powered");
/* 670 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.DOOR_MAP.get(name + name + "lower" + facing + hinge + open));
/* 671 */                       setBlock(abovePos, ChunkPalettedStorageFix.MappingConstants.DOOR_MAP.get(name + name + "upper" + facing + hinge + open));
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             case 175:
/* 678 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) {
/* 678 */                 int pos = ((Integer)intListIterator.next()).intValue();
/* 679 */                 pos |= dy;
/* 681 */                 Dynamic<?> block = getBlock(pos);
/* 682 */                 if ("upper".equals(ChunkPalettedStorageFix.getProperty(block, "half"))) {
/* 683 */                   Dynamic<?> below = getBlock(relative(pos, ChunkPalettedStorageFix.Direction.DOWN));
/* 684 */                   String variant = ChunkPalettedStorageFix.getName(below);
/* 685 */                   switch (variant) {
/*     */                     case "minecraft:sunflower":
/* 686 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_SUNFLOWER);
/*     */                     case "minecraft:lilac":
/* 687 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_LILAC);
/*     */                     case "minecraft:tall_grass":
/* 688 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_TALL_GRASS);
/*     */                     case "minecraft:large_fern":
/* 689 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_LARGE_FERN);
/*     */                     case "minecraft:rose_bush":
/* 690 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_ROSE_BUSH);
/*     */                     case "minecraft:peony":
/* 691 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_PEONY);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */           } 
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   private Dynamic<?> getBlockEntity(int pos) {
/* 702 */     return (Dynamic)this.blockEntities.get(pos);
/*     */   }
/*     */   
/*     */   private Dynamic<?> removeBlockEntity(int pos) {
/* 706 */     return (Dynamic)this.blockEntities.remove(pos);
/*     */   }
/*     */   
/*     */   public static int relative(int pos, ChunkPalettedStorageFix.Direction direction) {
/*     */     int x, y;
/* 710 */     switch (direction.getAxis().ordinal()) {
/*     */       default:
/* 710 */         throw new MatchException(null, null);
/*     */       case 0:
/* 712 */         x = (pos & 0xF) + direction.getAxisDirection().getStep();
/* 713 */         return (x < 0 || x > 15) ? -1 : (pos & 0xFFFFFFF0 | x);
/*     */       case 1:
/* 716 */         y = (pos >> 8) + direction.getAxisDirection().getStep();
/* 717 */         return (y < 0 || y > 255) ? -1 : (pos & 0xFF | y << 8);
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 720 */     int z = (pos >> 4 & 0xF) + direction.getAxisDirection().getStep();
/* 721 */     return (z < 0 || z > 15) ? -1 : (pos & 0xFFFFFF0F | z << 4);
/*     */   }
/*     */   
/*     */   private void setBlock(int pos, Dynamic<?> block) {
/* 727 */     if (pos < 0 || pos > 65535)
/*     */       return; 
/* 731 */     ChunkPalettedStorageFix.Section section = getSection(pos);
/* 733 */     if (section == null)
/*     */       return; 
/* 737 */     section.setBlock(pos & 0xFFF, block);
/*     */   }
/*     */   
/*     */   private ChunkPalettedStorageFix.Section getSection(int pos) {
/* 741 */     int sectionY = pos >> 12;
/* 742 */     return (sectionY < this.sections.length) ? this.sections[sectionY] : null;
/*     */   }
/*     */   
/*     */   public Dynamic<?> getBlock(int pos) {
/* 746 */     if (pos < 0 || pos > 65535)
/* 747 */       return ChunkPalettedStorageFix.MappingConstants.AIR; 
/* 750 */     ChunkPalettedStorageFix.Section section = getSection(pos);
/* 752 */     if (section == null)
/* 753 */       return ChunkPalettedStorageFix.MappingConstants.AIR; 
/* 756 */     return section.getBlock(pos & 0xFFF);
/*     */   }
/*     */   
/*     */   public Dynamic<?> write() {
/* 760 */     Dynamic<?> level = this.level;
/* 761 */     if (this.blockEntities.isEmpty()) {
/* 762 */       level = level.remove("TileEntities");
/*     */     } else {
/* 764 */       level = level.set("TileEntities", level.createList(this.blockEntities.values().stream()));
/*     */     } 
/* 767 */     Dynamic<?> indices = level.emptyMap();
/* 768 */     List<Dynamic<?>> sections = Lists.newArrayList();
/* 769 */     for (ChunkPalettedStorageFix.Section section : this.sections) {
/* 770 */       if (section != null) {
/* 771 */         sections.add(section.write());
/* 772 */         indices = indices.set(String.valueOf(section.y), indices.createIntList(Arrays.stream(section.update.toIntArray())));
/*     */       } 
/*     */     } 
/* 776 */     Dynamic<?> tag = level.emptyMap();
/* 777 */     tag = tag.set("Sides", tag.createByte((byte)this.sides));
/* 778 */     tag = tag.set("Indices", indices);
/* 779 */     return level.set("UpgradeData", tag).set("Sections", tag.createList(sections.stream()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$UpgradeChunk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */