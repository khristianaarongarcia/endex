# Remaining Multi-Language Refactoring Work

## Overview
After reviewing the entire codebase, I found **5 main files** that still have hardcoded user-facing strings that need to be refactored for multi-language support.

---

## Files That Need Refactoring

### 1. ✅ **CustomShopGUI.kt** - ALREADY COMPLETED
- Status: Refactored successfully
- All user-facing strings now use `Lang.get()`

### 2. ❌ **MarketCommand.kt** - NEEDS REFACTORING
- **File**: `src/main/kotlin/org/lokixcz/theendex/commands/MarketCommand.kt`
- **Total Hardcoded Strings**: ~100+
- **Categories of Messages**:
  - Help menu (lines 125-173)
  - Price check messages (lines 179-225)
  - Transaction messages (buy/sell confirmations)
  - Holdings display messages
  - Investment messages
  - Event management messages
  - Admin commands feedback
  - Item management commands

**Example Hardcoded Strings**:
```kotlin
sender.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
sender.sendMessage("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}The Endex ${ChatColor.GRAY}- Market Commands")
sender.sendMessage("${ChatColor.YELLOW}/market ${ChatColor.GRAY}- Open market/shop GUI")
sender.sendMessage("${ChatColor.RED}Unknown material: ${args[1]}")
sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.GREEN}Bought $amount ${mat.name} for ${format(total)}.")
```

### 3. ❌ **EndexCommand.kt** - NEEDS REFACTORING
- **File**: `src/main/kotlin/org/lokixcz/theendex/commands/EndexCommand.kt`
- **Total Hardcoded Strings**: ~40+
- **Categories of Messages**:
  - Plugin info messages (lines 37-38)
  - Help command output (lines 44-63)
  - Permission denied messages
  - Web UI commands
  - Reload command feedback
  - Track command output
  - Version information

**Example Hardcoded Strings**:
```kotlin
sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}Dynamic item market")
sender.sendMessage("${ChatColor.GRAY}Use ${ChatColor.AQUA}/endex help${ChatColor.GRAY} or ${ChatColor.AQUA}/endex market${ChatColor.GRAY} to open the GUI.")
sender.sendMessage("${ChatColor.AQUA}/endex reload${ChatColor.GRAY} — Reload configs and market data")
sender.sendMessage("${ChatColor.RED}No permission.")
sender.sendMessage("${ChatColor.RED}Web server not available.")
```

### 4. ❌ **ShopEditorGUI.kt** - NEEDS REFACTORING
- **File**: `src/main/kotlin/org/lokixcz/theendex/shop/editor/ShopEditorGUI.kt`
- **Total Hardcoded Strings**: ~60+
- **Categories of Messages**:
  - Permission/access denied messages
  - Editor mode change notifications
  - Layout editing feedback
  - Save/delete confirmations
  - Input prompts and cancellations
  - Validation error messages
  - Success confirmations

**Example Hardcoded Strings**:
```kotlin
player.sendMessage("${ChatColor.RED}You don't have permission to use the shop editor!")
player.sendMessage("${ChatColor.YELLOW}⚠ You have unsaved changes! Open the editor again to save.")
player.sendMessage("${ChatColor.GREEN}Layout saved to shops/$shopId.yml")
player.sendMessage("${ChatColor.YELLOW}Mode changed to: ${ChatColor.WHITE}${session.layoutEditMode.name.replace("_", " ")}")
player.sendMessage("${ChatColor.GREEN}Created new shop: $input (ID: $shopId)")
```

### 5. ❌ **MarketActions.kt** - NEEDS MINOR REFACTORING
- **File**: `src/main/kotlin/org/lokixcz/theendex/gui/MarketActions.kt`
- **Total Hardcoded Strings**: 1
- **Message**: Sell validation error

**Example Hardcoded String**:
```kotlin
player.sendMessage("${ChatColor.RED}You don't have any ${prettyName(material)} to sell!")
```

### 6. ❌ **MarketGUI.kt** - NEEDS MINOR REFACTORING
- **File**: `src/main/kotlin/org/lokixcz/theendex/gui/MarketGUI.kt`
- **Total Hardcoded Strings**: 1
- **Message**: Holdings display

**Example Hardcoded String**:
```kotlin
player.sendMessage("${ChatColor.GRAY}  • ${ChatColor.AQUA}${prettyName(mat)}: ${ChatColor.GOLD}$count")
```

---

## Summary Statistics

| File | Complexity | Est. Strings | Priority |
|------|-----------|--------------|----------|
| CustomShopGUI.kt | ✅ DONE | ~20 | ✅ Completed |
| MarketCommand.kt | 🔴 High | 100+ | 🔥 High |
| EndexCommand.kt | 🟡 Medium | 40+ | 🔥 High |
| ShopEditorGUI.kt | 🟡 Medium | 60+ | 🟠 Medium |
| MarketActions.kt | 🟢 Low | 1 | 🟢 Low |
| MarketGUI.kt | 🟢 Low | 1 | 🟢 Low |

**Total Remaining**: ~200+ hardcoded strings across 5 files

---

## Recommended Approach

### Phase 1: Quick Wins (Low-hanging fruit)
1. **MarketActions.kt** - 1 string
2. **MarketGUI.kt** - 1 string

### Phase 2: Command Classes (High user impact)
3. **EndexCommand.kt** - Main plugin commands
4. **MarketCommand.kt** - Market trading commands

### Phase 3: Admin Tools (Lower priority)
5. **ShopEditorGUI.kt** - Admin shop editor

---

## Language Keys Needed

Based on the hardcoded strings found, we'll need to add language keys for:

### Commands Section (`commands.*`)
- Help menus and command lists
- Usage messages
- Permission denied messages
- Success/error feedback

### Market Section (`market.*`)
- Transaction confirmations
- Price information
- Holdings displays
- Investment messages
- Event notifications

### Editor Section (`editor.*`)
- Mode changes
- Save/delete confirmations
- Input prompts
- Validation errors
- Success messages

### General Section (`general.*`)
- Common errors (no permission, not found, etc.)
- Common success messages
- Generic feedback

---

## Next Steps

1. Start with **MarketActions.kt** and **MarketGUI.kt** (easiest)
2. Add necessary language keys to `en.yml`
3. Refactor **EndexCommand.kt** (main commands)
4. Refactor **MarketCommand.kt** (largest file)
5. Refactor **ShopEditorGUI.kt** (admin tool)
6. Test all changes thoroughly
7. Update documentation

---

## Notes

- Some files already have partial Lang support (e.g., MarketCommand.kt has one Lang.get() call on line 931)
- The existing language structure in `en.yml` is well-organized and can be extended
- Consider grouping similar messages to reduce duplication
- Maintain color coding consistency across all messages
