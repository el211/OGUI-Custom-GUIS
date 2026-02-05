# Condition System

The Condition System is the heart of OGUI Enhanced. It allows you to create complex requirements that players must meet before they can purchase or interact with GUI items.

---

## 📚 Table of Contents

- [Overview](#overview)
- [How Conditions Work](#how-conditions-work)
- [All Condition Types](#all-condition-types)
- [Multiple Conditions](#multiple-conditions)
- [Error Messages](#error-messages)
- [Best Practices](#best-practices)

---

## Overview

### What Are Conditions?

Conditions are requirements that must be met before a player can use a GUI item. They can check:
- 💰 **Currency** (Vault money, OreoEssentials currencies)
- ⭐ **Experience** (Levels, points)
- 🎒 **Items** (Vanilla, ItemsAdder, Nexo)
- 🔐 **Permissions** (Rank-based access)
- 🏰 **Regions** (WorldGuard areas)
- 📊 **Dynamic** (PlaceholderAPI checks)

### Key Features

✅ **Two-Phase Checking**
- Phase 1: Check all conditions
- Phase 2: Consume resources (only if all pass)

✅ **Multiple Conditions**
- Combine unlimited conditions
- All must pass for success

✅ **Automatic Error Messages**
- Each condition provides helpful feedback
- Customizable for PlaceholderAPI conditions

---

## How Conditions Work

### The Check Process

When a player clicks an item:

1. **Validation Phase**
   ```
   For each condition:
     ✓ Check if player meets requirement
     ✗ If any fails, stop and show error
   ```

2. **Consumption Phase**
   ```
   For each condition:
     ⚡ Take resources (money, items, XP)
     ⚡ Execute action
   ```

3. **Command Execution**
   ```
   ✓ All conditions passed
   ⚡ Run all commands
   🎉 Success!
   ```

### Example Flow

```yaml
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
  - type: XP_LEVEL
    amount: 50
```

**Player clicks:**
1. Check gems ≥ 1000? ✓
2. Check level ≥ 50? ✓
3. Take 1000 gems ✓
4. Take 50 levels ✓
5. Give item ✓

**If player has gems but not level:**
1. Check gems ≥ 1000? ✓
2. Check level ≥ 50? ✗
3. **STOP** - Show error message
4. No resources taken!

---

## All Condition Types

### 1. VAULT_MONEY 💰

Requires Vault economy money.

**Configuration:**
```yaml
conditions:
  - type: VAULT_MONEY
    amount: 500  # Dollar amount
```

**Requirements:**
- Vault plugin installed
- Economy plugin (EssentialsX, CMI, etc.)

**Error Message:**
```
✖ Insufficient money! Need: $500.00 (You have: $250.00)
```

**Examples:**
```yaml
# Simple money check
conditions:
  - type: VAULT_MONEY
    amount: 1000

# Expensive item
conditions:
  - type: VAULT_MONEY
    amount: 50000
```

---

### 2. OREO_CURRENCY 💎

Requires OreoEssentials custom currency.

**Configuration:**
```yaml
conditions:
  - type: OREO_CURRENCY
    currency: gems    # Currency name
    amount: 1000      # Amount required
```

**Requirements:**
- OreoEssentials plugin installed
- Currency configured in OreoEssentials

**Error Message:**
```
✖ Insufficient Gems! Need: 1,000 (You have: 500)
```

**Examples:**
```yaml
# Gems currency
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000

# Tokens currency
conditions:
  - type: OREO_CURRENCY
    currency: tokens
    amount: 500

# Multiple currencies
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
  - type: OREO_CURRENCY
    currency: tokens
    amount: 500
```

---

### 3. XP_LEVEL ⭐

Requires player experience levels.

**Configuration:**
```yaml
conditions:
  - type: XP_LEVEL
    amount: 50  # Number of levels
```

**Requirements:**
- None (built-in)

**Behavior:**
- Takes levels from player
- Deducts exactly the amount specified

**Error Message:**
```
✖ Insufficient XP levels! Need: 50 (You have: 30)
```

**Examples:**
```yaml
# Low level requirement
conditions:
  - type: XP_LEVEL
    amount: 10

# High level requirement
conditions:
  - type: XP_LEVEL
    amount: 100
```

---

### 4. XP_POINTS ⚡

Requires total experience points.

**Configuration:**
```yaml
conditions:
  - type: XP_POINTS
    amount: 1000  # Total XP points
```

**Requirements:**
- None (built-in)

**Behavior:**
- Takes XP points from player
- Recalculates levels after deduction

**Error Message:**
```
✖ Insufficient XP! Need: 1000 (You have: 500)
```

**Examples:**
```yaml
# Moderate XP
conditions:
  - type: XP_POINTS
    amount: 500

# High XP requirement
conditions:
  - type: XP_POINTS
    amount: 5000
```

**XP_LEVEL vs XP_POINTS:**
- XP_LEVEL: Takes levels (30 levels = 30 levels)
- XP_POINTS: Takes total XP (1000 points ≈ 16 levels)

---

### 5. ITEM 🎒

Requires vanilla Minecraft items.

**Configuration:**
```yaml
conditions:
  - type: ITEM
    material: DIAMOND  # Item material
    amount: 10         # Quantity
```

**Requirements:**
- None (built-in)

**Behavior:**
- Checks player inventory
- Removes items when purchasing

**Error Message:**
```
✖ Insufficient items! Need: 10x diamond (You have: 5)
```

**Examples:**
```yaml
# Single item
conditions:
  - type: ITEM
    material: DIAMOND
    amount: 1

# Large quantity
conditions:
  - type: ITEM
    material: IRON_INGOT
    amount: 64

# Multiple items
conditions:
  - type: ITEM
    material: DIAMOND
    amount: 10
  - type: ITEM
    material: EMERALD
    amount: 5
```

**Valid Materials:**
Any Minecraft material name from [Spigot API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html)

---

### 6. ITEM_CUSTOM_MODEL 🎨

Requires items with specific custom model data.

**Configuration:**
```yaml
conditions:
  - type: ITEM_CUSTOM_MODEL
    material: DIAMOND          # Base material
    custom_model_data: 1001    # Custom model data value
    amount: 5                  # Quantity
```

**Requirements:**
- Resource pack with custom models
- Items with CustomModelData NBT

**Use Case:**
- Custom textured items
- Resource pack items
- Special edition items

**Error Message:**
```
✖ Insufficient items! Need: 5x diamond (CMD: 1001) (You have: 2)
```

**Examples:**
```yaml
# Custom textured diamond
conditions:
  - type: ITEM_CUSTOM_MODEL
    material: DIAMOND
    custom_model_data: 1001
    amount: 1

# Custom weapon
conditions:
  - type: ITEM_CUSTOM_MODEL
    material: STICK
    custom_model_data: 5000
    amount: 1
```

---

### 7. ITEMSADDER 🔥

Requires ItemsAdder custom items.

**Configuration:**
```yaml
conditions:
  - type: ITEMSADDER
    item_id: ruby    # ItemsAdder item ID
    amount: 5        # Quantity
```

**Requirements:**
- ItemsAdder plugin installed
- Item configured in ItemsAdder

**Error Message:**
```
✖ Insufficient items! Need: 5x ruby (You have: 2)
```

**Examples:**
```yaml
# Single ItemsAdder item
conditions:
  - type: ITEMSADDER
    item_id: ruby
    amount: 1

# Multiple ItemsAdder items
conditions:
  - type: ITEMSADDER
    item_id: magic_crystal
    amount: 10
  - type: ITEMSADDER
    item_id: fire_essence
    amount: 5
```

---

### 8. NEXO ❄️

Requires Nexo custom items.

**Configuration:**
```yaml
conditions:
  - type: NEXO
    item_id: sapphire  # Nexo item ID
    amount: 3          # Quantity
```

**Requirements:**
- Nexo plugin installed
- Item configured in Nexo

**Error Message:**
```
✖ Insufficient items! Need: 3x sapphire (You have: 1)
```

**Examples:**
```yaml
# Single Nexo item
conditions:
  - type: NEXO
    item_id: sapphire
    amount: 1

# Multiple Nexo items
conditions:
  - type: NEXO
    item_id: ice_shard
    amount: 10
```

---

### 9. PERMISSION 🔐

Requires permission node.

**Configuration:**
```yaml
conditions:
  - type: PERMISSION
    permission: rank.vip  # Permission node
```

**Requirements:**
- Permission plugin (LuckPerms, etc.)

**Behavior:**
- **Non-consumable** - Only checks, doesn't remove
- Fails if player lacks permission

**Error Message:**
```
✖ You don't have permission: rank.vip
```

**Examples:**
```yaml
# VIP access
conditions:
  - type: PERMISSION
    permission: rank.vip

# Admin only
conditions:
  - type: PERMISSION
    permission: admin.shop

# Multiple permissions
conditions:
  - type: PERMISSION
    permission: rank.vip
  - type: PERMISSION
    permission: shop.special
```

---

### 10. WORLDGUARD_REGION 🏰

Requires player in WorldGuard region.

**Configuration:**
```yaml
conditions:
  - type: WORLDGUARD_REGION
    region: spawn              # Region ID
    require_member: false      # Membership requirement
```

**Requirements:**
- WorldGuard plugin installed
- WorldEdit plugin installed
- Region created

**Options:**
- `require_member: false` - Just be in region
- `require_member: true` - Must be member/owner

**Error Messages:**
```
# If require_member: false
✖ You must be in region: spawn

# If require_member: true
✖ You must be a member of region: vip-zone
```

**Examples:**
```yaml
# Just be in region
conditions:
  - type: WORLDGUARD_REGION
    region: spawn
    require_member: false

# Must be member
conditions:
  - type: WORLDGUARD_REGION
    region: vip-zone
    require_member: true

# Multiple regions
conditions:
  - type: WORLDGUARD_REGION
    region: spawn
  - type: WORLDGUARD_REGION
    region: shop-area
```

---

### 11. PLACEHOLDER 📊

Dynamic conditions using PlaceholderAPI.

**Configuration:**
```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%vault_eco_balance%"  # Placeholder
    operator: ">="                      # Comparison
    value: "10000"                      # Value
    error_message: "&cNeed $10k!"       # Optional custom error
```

**Requirements:**
- PlaceholderAPI plugin installed
- Relevant expansions installed

**Operators:**
- `==` or `equals` - Equals
- `!=` or `not_equals` - Not equals
- `>` - Greater than
- `<` - Less than
- `>=` - Greater than or equal
- `<=` - Less than or equal
- `contains` - Contains substring

**Error Message:**
```
# Default (if no error_message)
✖ Condition not met: %vault_eco_balance% >= 10000

# Custom (with error_message)
✖ Need $10k!
```

**Examples:**

**Balance Check:**
```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%vault_eco_balance%"
    operator: ">="
    value: "10000"
    error_message: "&cYou need $10,000 balance!"
```

**Level Check:**
```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%player_level%"
    operator: ">="
    value: "50"
    error_message: "&cYou must be level 50+"
```

**Playtime Check:**
```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%statistic_hours_played%"
    operator: ">="
    value: "10"
    error_message: "&cYou need 10+ hours playtime!"
```

**Name Check:**
```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%player_name%"
    operator: "equals"
    value: "Notch"
    error_message: "&cOnly Notch can buy this!"
```

**Text Contains:**
```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%luckperms_primary_group%"
    operator: "contains"
    value: "vip"
    error_message: "&cVIP rank required!"
```

---

## Multiple Conditions

You can combine unlimited conditions. All must pass.

### Example: Complex Requirements

```yaml
conditions:
  # Currency requirements
  - type: OREO_CURRENCY
    currency: gems
    amount: 5000
  
  - type: OREO_CURRENCY
    currency: tokens
    amount: 1000
  
  - type: VAULT_MONEY
    amount: 10000
  
  # Level requirement
  - type: XP_LEVEL
    amount: 100
  
  # Item requirement
  - type: ITEM
    material: DIAMOND
    amount: 64
  
  # Permission requirement
  - type: PERMISSION
    permission: rank.vip
  
  # Region requirement
  - type: WORLDGUARD_REGION
    region: premium-area
    require_member: true
  
  # Dynamic requirement
  - type: PLACEHOLDER
    placeholder: "%vault_eco_balance%"
    operator: ">="
    value: "50000"
```

**All 8 conditions must pass!**

---

## Error Messages

### Default Messages

Each condition has a built-in error message:
- Shows what's required
- Shows what player has
- Clear and helpful

### Custom Messages

Only PlaceholderAPI conditions support custom messages:

```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%vault_eco_balance%"
    operator: ">="
    value: "10000"
    error_message: "&c&lINSUFFICIENT FUNDS!
&7You need at least $10,000 in your balance.
&7Current balance: %vault_eco_balance%"
```

**Placeholders in error messages:**
- Use any PlaceholderAPI placeholder
- Will be parsed for the player

---

## Best Practices

### 1. Order Doesn't Matter

Conditions are checked in order, but all are validated before any resources are taken:

```yaml
# These are equivalent:
conditions:
  - type: VAULT_MONEY
    amount: 1000
  - type: XP_LEVEL
    amount: 50

conditions:
  - type: XP_LEVEL
    amount: 50
  - type: VAULT_MONEY
    amount: 1000
```

### 2. Use Appropriate Types

Choose the right condition for your needs:

**✅ Good:**
```yaml
# Check level with XP_LEVEL
conditions:
  - type: XP_LEVEL
    amount: 50
```

**❌ Bad:**
```yaml
# Using PLACEHOLDER for levels (slower)
conditions:
  - type: PLACEHOLDER
    placeholder: "%player_level%"
    operator: ">="
    value: "50"
```

### 3. Minimize Conditions

More conditions = more checks:

**✅ Good:**
```yaml
# Only required conditions
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
```

**❌ Unnecessary:**
```yaml
# Too many redundant checks
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
  - type: PLACEHOLDER
    placeholder: "%oreo_gems%"
    operator: ">="
    value: "1000"  # Redundant!
```

### 4. Test Incrementally

Add conditions one at a time:

1. Start with one condition
2. Test it works
3. Add next condition
4. Test again
5. Repeat

### 5. Provide Context

Use lore to explain requirements:

```yaml
lore:
  - "&7This item requires:"
  - "&a- 5000 Gems"
  - "&e- $10,000"
  - "&b- Level 100"
  - "&7- VIP Rank"
  - "&7- Be in VIP Zone"
  - ""
  - "&eClick to purchase"
```

---

## Advanced Tips

### Currency Conversion

Use conditions to create currency exchanges:

```yaml
# Convert diamonds to gems
items:
  "13":
    material: DIAMOND
    name: "&bSell Diamonds"
    commands:
      - "currency add {player} gems 1000"
    conditions:
      - type: ITEM
        material: DIAMOND
        amount: 10
```

### Rank Upgrades

Require current rank for next rank:

```yaml
# VIP+ requires VIP
conditions:
  - type: PERMISSION
    permission: rank.vip
  - type: OREO_CURRENCY
    currency: gems
    amount: 10000
commands:
  - "lp user {player} parent set vipplus"
```

### Progressive Unlocks

Use PlaceholderAPI for progression:

```yaml
# Require 100 kills for weapon
conditions:
  - type: PLACEHOLDER
    placeholder: "%statistic_mob_kills%"
    operator: ">="
    value: "100"
```

---

## 🎓 Summary

The Condition System in OGUI Enhanced is:

✅ **Powerful** - 11 different types
✅ **Flexible** - Combine unlimited conditions
✅ **Safe** - Two-phase checking prevents partial transactions
✅ **User-Friendly** - Clear error messages
✅ **Performant** - Optimized checking

**Next Steps:**
- [Multiple Conditions](Multiple-Conditions.md) - Complex examples
- [Shop Examples](Shop-Examples.md) - Real configurations
- [PlaceholderAPI Guide](PlaceholderAPI-Conditions.md) - Dynamic conditions
