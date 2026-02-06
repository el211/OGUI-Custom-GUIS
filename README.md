# OGUI  🎨

**A powerful, feature-rich GUI plugin for Minecraft servers with support for custom items, currencies, NPCs, and more!**



---


---

## ✨ Features

- 🎨 **Beautiful Color Support** - Hex colors, gradients, and rainbow text
- 💰 **Economy Integration** - Vault money & OreoEssentials currencies
- 🎮 **Custom Items** - ItemsAdder & Nexo support
- 🤖 **NPC Integration** - ModeledNPCs support
- 🗺️ **Location Checks** - WorldGuard regions, warps, and distance
- ⚡ **16 Condition Types** - Flexible requirement system
- 🌐 **PlaceholderAPI** - Full PAPI support everywhere
- 🔄 **Multi-Language** - Fully customizable messages

---

## 📥 Installation

1. **Download** the latest `OGUI-Enhanced.jar`
2. **Place** it in your server's `plugins/` folder
3. **Restart** your server
4. **Edit** `plugins/OGUI/guis.yml` to create your GUIs
5. **Reload** with `/ogui reload`

### Dependencies:
- **Required:** None!
- **Optional:**
   - Vault (for economy)
   - PlaceholderAPI (for placeholders)
   - ItemsAdder (for custom items)
   - Nexo (for custom items)
   - WorldGuard (for region checks)
   - ModeledNPCs (for NPC GUIs)
   - OreoEssentials (for currencies/warps)

---

## 🚀 Quick Start

Create your first GUI in `guis.yml`:

```yaml
guis:
  my_shop:
    title: "&6&lMy Shop"
    rows: 3
    
    items:
      10:
        material: DIAMOND
        name: "&bBuy Diamond"
        lore:
          - "&7Price: &a$100"
          - "&eClick to purchase!"
        
        # What happens when clicked
        commands:
          - "[player] give {player} diamond 1"
        
        # What's required to buy
        conditions:
          - type: VAULT_MONEY
            amount: 100
```

**Open the GUI:** `/ogui open my_shop`

---

## 🔧 How It Works

### 1. **Define Your GUI**
Create a GUI in `guis.yml` with a title, size, and items.

### 2. **Add Items**
Each item has:
- **Material** - What it looks like
- **Name & Lore** - Display text
- **Commands** - What happens on click
- **Conditions** - Requirements to click

### 3. **Set Conditions**
Conditions check if the player meets requirements (money, items, permissions, etc.)

### 4. **Open the GUI**
Use commands, NPCs, or the `/ogui open <id>` command

---

## 📋 Conditions Guide

OGUI Enhanced supports **16 different condition types**. Here's every single one with examples:

---

### 💰 **1. VAULT_MONEY** - Require Money

Requires player to have a certain amount of money (uses Vault).

**Example:**
```yaml
conditions:
  - type: VAULT_MONEY
    amount: 100.50
```

**What it checks:**
- ✅ Player has at least $100.50
- ❌ Player has less than $100.50

**What it does:**
- Deducts $100.50 from player's balance when clicked

---

### 🏆 **2. PERMISSION** - Require Permission

Requires player to have a specific permission node.

**Example:**
```yaml
conditions:
  - type: PERMISSION
    permission: "shop.vip"
```

**What it checks:**
- ✅ Player has `shop.vip` permission
- ❌ Player doesn't have the permission

**What it does:**
- Just checks permission (doesn't remove it)

---

### ⭐ **3. XP_LEVEL** - Require XP Levels

Requires player to have a certain number of XP levels.

**Example:**
```yaml
conditions:
  - type: XP_LEVEL
    amount: 30
```

**What it checks:**
- ✅ Player has 30+ levels
- ❌ Player has less than 30 levels

**What it does:**
- Removes 30 levels from the player

---

### ✦ **4. XP_POINTS** - Require XP Points

Requires player to have a certain amount of total XP points.

**Example:**
```yaml
conditions:
  - type: XP_POINTS
    amount: 1000
```

**What it checks:**
- ✅ Player has 1000+ XP points
- ❌ Player has less than 1000 points

**What it does:**
- Removes 1000 XP points from the player

---

### 📦 **5. ITEM** - Require Vanilla Items

Requires player to have vanilla Minecraft items in inventory.

**Example:**
```yaml
conditions:
  - type: ITEM
    material: DIAMOND
    amount: 10
```

**What it checks:**
- ✅ Player has 10+ diamonds
- ❌ Player has less than 10 diamonds

**What it does:**
- Removes 10 diamonds from inventory

---

### 🎨 **6. ITEM_CUSTOM_MODEL** - Require Custom Model Data Items

Requires vanilla items with specific custom model data (resource pack items).

**Example:**
```yaml
conditions:
  - type: ITEM_CUSTOM_MODEL
    material: DIAMOND
    amount: 5
    custom_model_data: 1001
```

**What it checks:**
- ✅ Player has 5+ diamonds with CustomModelData=1001
- ❌ Player doesn't have enough

**What it does:**
- Removes 5 diamonds with that specific model data

---

### ✨ **7. ITEMSADDER** - Require ItemsAdder Items

Requires custom items from the ItemsAdder plugin.

**Example:**
```yaml
conditions:
  - type: ITEMSADDER
    item_id: "custom_sword"
    amount: 1
```

**What it checks:**
- ✅ Player has 1+ "custom_sword" from ItemsAdder
- ❌ Player doesn't have the item

**What it does:**
- Removes 1 custom_sword from inventory

**Requirements:** ItemsAdder plugin installed

---

### ⚡ **8. NEXO** - Require Nexo Items

Requires custom items from the Nexo plugin.

**Example:**
```yaml
conditions:
  - type: NEXO
    item_id: "magic_wand"
    amount: 1
```

**What it checks:**
- ✅ Player has 1+ "magic_wand" from Nexo
- ❌ Player doesn't have the item

**What it does:**
- Removes 1 magic_wand from inventory

**Requirements:** Nexo plugin installed

---

### 💎 **9. OREO_CURRENCY** - Require OreoEssentials Currency

Requires custom currencies from OreoEssentials.

**Example:**
```yaml
conditions:
  - type: OREO_CURRENCY
    currency: "tokens"
    amount: 50
```

**What it checks:**
- ✅ Player has 50+ tokens
- ❌ Player has less than 50 tokens

**What it does:**
- Removes 50 tokens from player's balance

**Requirements:** OreoEssentials plugin installed

---

### 🗺️ **10. OREO_WARPS** - Require Warp Exists/Permission

Checks if a warp exists or if player has permission to use it.

**Example:**
```yaml
conditions:
  - type: OREO_WARPS
    warp: "spawn"
    check_exists: true
    check_permission: true
```

**What it checks:**
- ✅ Warp "spawn" exists AND player has permission
- ❌ Warp doesn't exist OR no permission

**What it does:**
- Just checks (doesn't teleport or remove anything)

**Requirements:** OreoEssentials plugin installed

---

### 📍 **11. OREO_WARPS_LOCATION** - Require Player at Warp Location

Requires player to be physically standing near a warp location.

**Example:**
```yaml
conditions:
  - type: OREO_WARPS_LOCATION
    warp: "arena"
    radius: 10.0
    error_message: "&cYou must be at the arena! (Within 10 blocks)"
```

**What it checks:**
- ✅ Player is within 10 blocks of "arena" warp
- ❌ Player is too far away

**What it does:**
- Just checks location (doesn't teleport)

**Requirements:** OreoEssentials plugin installed

---

### 🏰 **12. WORLDGUARD_REGION** - Require WorldGuard Region

Requires player to be in a specific WorldGuard region.

**Example:**
```yaml
conditions:
  - type: WORLDGUARD_REGION
    region: "spawn"
    require_member: false
```

**What it checks:**
- ✅ Player is inside "spawn" region
- ❌ Player is outside the region

**With `require_member: true`:**
- ✅ Player is a member/owner of the region
- ❌ Player is not a member

**Requirements:** WorldGuard plugin installed

---

### 🔮 **13. PLACEHOLDER** - Require PlaceholderAPI Condition

Advanced condition using PlaceholderAPI placeholders.

**Example:**
```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%player_level%"
    operator: ">="
    value: "50"
    error_message: "&cYou need level 50! (Current: %player_level%)"
```

**Operators:**
- `==` or `equals` - Exact match
- `!=` or `not_equals` - Not equal
- `>` - Greater than
- `<` - Less than
- `>=` - Greater than or equal
- `<=` - Less than or equal
- `contains` - Text contains

**What it checks:**
- ✅ Player's level >= 50
- ❌ Player's level < 50

**Requirements:** PlaceholderAPI plugin installed

---

### 🌤️ **14. WEATHER** - Require Specific Weather

Requires the world to have specific weather.

**Example:**
```yaml
conditions:
  - type: WEATHER
    weather: "rain"
```

**Valid weather types:**
- `clear`, `sun`, `sunny` - Clear weather
- `rain`, `raining` - Raining
- `thunder`, `thundering`, `storm` - Thunderstorm

**What it checks:**
- ✅ Current weather matches requirement
- ❌ Weather is different

---

### 🌍 **15. WORLD** - Require Specific World

Requires player to be in specific world(s).

**Example (Whitelist):**
```yaml
conditions:
  - type: WORLD
    worlds:
      - "world"
      - "world_nether"
    blacklist: false
```

**Example (Blacklist):**
```yaml
conditions:
  - type: WORLD
    worlds:
      - "creative_world"
    blacklist: true
```

**What it checks:**
- Whitelist: ✅ Player is in listed world
- Blacklist: ✅ Player is NOT in listed world

---

### 🤖 **16. MODELED_NPC** - Require Near NPC

Requires player to be near a specific ModeledNPC.

**Example:**
```yaml
conditions:
  - type: MODELED_NPC
    npc_id: 5
    radius: 10.0
```

**What it checks:**
- ✅ Player is within 10 blocks of NPC #5
- ❌ Player is too far from the NPC

**Requirements:** ModeledNPCs plugin installed

---

## 🎯 Complete Examples

### Example 1: Simple Shop Item

**Requirement:** $500 money

```yaml
items:
  10:
    material: DIAMOND_SWORD
    name: "&b&lDiamond Sword"
    lore:
      - "&7A powerful weapon!"
      - ""
      - "&ePrice: &a$500"
      - "&aClick to purchase!"
    
    commands:
      - "[player] give {player} diamond_sword 1"
    
    conditions:
      - type: VAULT_MONEY
        amount: 500
```

---

### Example 2: VIP Item with Permission

**Requirements:**
- VIP permission
- 10 diamonds

```yaml
items:
  11:
    material: GOLDEN_APPLE
    name: "&6&lVIP Golden Apple"
    lore:
      - "&7Only for VIP members!"
      - ""
      - "&eCost: &f10 Diamonds"
      - "&ePermission: &fshop.vip"
    
    commands:
      - "[console] give {player} golden_apple 1"
    
    conditions:
      - type: PERMISSION
        permission: "shop.vip"
      
      - type: ITEM
        material: DIAMOND
        amount: 10
```

---

### Example 3: High-Level Trade

**Requirements:**
- Level 50+
- 1000 XP points
- Custom ItemsAdder item

```yaml
items:
  12:
    material: NETHER_STAR
    name: "&d&lMystic Star"
    lore:
      - "&7An extremely rare item!"
      - ""
      - "&eRequirements:"
      - "&7- Level 50+"
      - "&7- 1000 XP Points"
      - "&7- 1x Magic Essence"
    
    commands:
      - "[console] give {player} nether_star 1"
    
    conditions:
      - type: XP_LEVEL
        amount: 50
      
      - type: XP_POINTS
        amount: 1000
      
      - type: ITEMSADDER
        item_id: "magic_essence"
        amount: 1
```

---

### Example 4: Location-Specific Item

**Requirements:**
- Must be at spawn warp
- Must be in spawn region
- Must have permission

```yaml
items:
  13:
    material: ENDER_PEARL
    name: "&5&lTeleport Home"
    lore:
      - "&7Only usable at spawn!"
      - ""
      - "&eRequirements:"
      - "&7- Be at spawn warp"
      - "&7- Be in spawn region"
      - "&7- Have home.use permission"
    
    commands:
      - "[player] home"
    
    conditions:
      - type: OREO_WARPS_LOCATION
        warp: "spawn"
        radius: 20.0
      
      - type: WORLDGUARD_REGION
        region: "spawn"
        require_member: false
      
      - type: PERMISSION
        permission: "home.use"
```

---

### Example 5: Advanced Currency Exchange

**Requirements:**
- 100 tokens (OreoEssentials)
- In specific world
- During rain
- Near NPC trader

```yaml
items:
  14:
    material: EMERALD
    name: "&a&lExchange Tokens"
    lore:
      - "&7Trade tokens for emeralds!"
      - ""
      - "&eRequirements:"
      - "&7- 100 Tokens"
      - "&7- In trading world"
      - "&7- Rainy weather"
      - "&7- Near token trader NPC"
    
    commands:
      - "[console] give {player} emerald 64"
    
    conditions:
      - type: OREO_CURRENCY
        currency: "tokens"
        amount: 100
      
      - type: WORLD
        worlds:
          - "world_trading"
        blacklist: false
      
      - type: WEATHER
        weather: "rain"
      
      - type: MODELED_NPC
        npc_id: 10
        radius: 5.0
```

---

### Example 6: PlaceholderAPI Smart Item

**Requirements:**
- Player must have played 10+ hours
- Kill/Death ratio > 2.0
- Custom placeholder check

```yaml
items:
  15:
    material: DIAMOND_CHESTPLATE
    name: "&b&lPro Player Armor"
    lore:
      - "&7For skilled players only!"
      - ""
      - "&eRequirements:"
      - "&7- 10+ hours played"
      - "&7- K/D Ratio > 2.0"
    
    commands:
      - "[console] give {player} diamond_chestplate 1"
      - "[console] lore set 1 &dPro Player Edition"
    
    conditions:
      - type: PLACEHOLDER
        placeholder: "%statistic_hours_played%"
        operator: ">="
        value: "10"
      
      - type: PLACEHOLDER
        placeholder: "%player_kill_death_ratio%"
        operator: ">"
        value: "2.0"
```

---

### Example 7: Multi-Currency Purchase

**Requirements:**
- $1000 money
- 50 tokens
- 10 gems
- VIP rank

```yaml
items:
  16:
    material: BEACON
    name: "&e&l&nLEGENDARY BEACON"
    lore:
      - "&7The ultimate prize!"
      - ""
      - "&eCost:"
      - "&7- $1000 Money"
      - "&7- 50 Tokens"
      - "&7- 10 Gems"
      - "&7- VIP Rank Required"
    
    commands:
      - "[console] give {player} beacon 1"
      - "[console] broadcast {player} just bought a LEGENDARY BEACON!"
    
    conditions:
      - type: VAULT_MONEY
        amount: 1000
      
      - type: OREO_CURRENCY
        currency: "tokens"
        amount: 50
      
      - type: OREO_CURRENCY
        currency: "gems"
        amount: 10
      
      - type: PERMISSION
        permission: "rank.vip"
```

---

### Example 8: Complete GUI with Multiple Items

A full GUI example with different condition combinations:

```yaml
guis:
  advanced_shop:
    title: "&6&l✦ Advanced Shop ✦"
    rows: 4
    
    # Bind to NPC (optional)
    npc_id: 5
    
    # Bind to command (optional)
    commands:
      - "shop"
      - "advshop"
    
    items:
      # Money item
      10:
        material: DIAMOND
        name: "&b&lDiamond Pack"
        lore:
          - "&7Get 10 diamonds!"
          - "&ePrice: &a$500"
        commands:
          - "[console] give {player} diamond 10"
        conditions:
          - type: VAULT_MONEY
            amount: 500
      
      # Permission + Items
      12:
        material: GOLDEN_APPLE
        name: "&6&lVIP Apple"
        lore:
          - "&7VIP exclusive!"
          - "&eCost: &f5 Diamonds"
        commands:
          - "[player] give {player} golden_apple 1"
        conditions:
          - type: PERMISSION
            permission: "shop.vip"
          - type: ITEM
            material: DIAMOND
            amount: 5
      
      # XP Trade
      14:
        material: EXPERIENCE_BOTTLE
        name: "&a&lXP Bottle"
        lore:
          - "&7Trade XP for bottles!"
          - "&eCost: &f10 Levels"
        commands:
          - "[console] give {player} experience_bottle 5"
        conditions:
          - type: XP_LEVEL
            amount: 10
      
      # Custom Currency
      16:
        material: NETHER_STAR
        name: "&d&lMystic Item"
        lore:
          - "&7Purchased with tokens!"
          - "&eCost: &f100 Tokens"
        commands:
          - "[console] give {player} nether_star 1"
        conditions:
          - type: OREO_CURRENCY
            currency: "tokens"
            amount: 100
      
      # Location-based
      20:
        material: ENDER_PEARL
        name: "&5&lSpawn Only Item"
        lore:
          - "&7Only at spawn!"
        commands:
          - "[player] spawn"
        conditions:
          - type: WORLDGUARD_REGION
            region: "spawn"
          - type: OREO_WARPS_LOCATION
            warp: "spawn"
            radius: 10.0
      
      # Weather-based
      22:
        material: WATER_BUCKET
        name: "&b&lRain Item"
        lore:
          - "&7Only during rain!"
        commands:
          - "[console] give {player} water_bucket 1"
        conditions:
          - type: WEATHER
            weather: "rain"
      
      # Close button (no conditions)
      31:
        material: BARRIER
        name: "&c&lClose"
        commands:
          - "[close]"
```

---

## 🎨 Color System

OGUI Enhanced supports **advanced color formatting**:

### Legacy Colors
```yaml
name: "&c&lRed Bold &a&nGreen Underline"
```

### Hex Colors
```yaml
name: "&#FF0000Red &#00FF00Green &#0000FFBlue"
```

### Gradients
```yaml
name: "<gradient:#FF0000:#0000FF>Rainbow Text!</gradient>"
```

### Rainbow
```yaml
name: "<rainbow>Party Time!</rainbow>"
```

### PlaceholderAPI
```yaml
lore:
  - "&7Player: &f%player_name%"
  - "&7Balance: &a$%vault_eco_balance%"
```

### Combined
```yaml
name: "<gradient:#FFD700:#FF8C00>&l✦ %player_name%'s Shop ✦</gradient>"
lore:
  - "&#00FFFFBalance: &#32CD32$%vault_eco_balance%"
  - "<rainbow>Premium Items Available!</rainbow>"
```

---

## ⚙️ Commands & Permissions

### Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/ogui` | Show help | `ogui.use` |
| `/ogui open <gui> [player]` | Open a GUI | `ogui.open` |
| `/ogui reload` | Reload configs | `ogui.reload` |

### Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `ogui.use` | Use /ogui command | `true` |
| `ogui.open` | Open GUIs | `true` |
| `ogui.reload` | Reload plugin | `op` |
| `ogui.command.<gui>` | Use GUI command | `true` |

---

## 📁 File Structure

```
plugins/
└── OGUI/
    ├── guis.yml          # Your GUI configurations
    ├── lang.yml          # Language/messages file
    └── config.yml        # Plugin settings (auto-generated)
```

---

## 🔧 Configuration Tips

### Best Practices

1. **Use Clear Names**
   ```yaml
   # ✅ Good
   items:
     10:
       name: "&b&lDiamond Sword - $500"
   
   # ❌ Bad
   items:
     10:
       name: "&bSword"
   ```

2. **Always Add Lore**
   ```yaml
   lore:
     - "&7What this item does"
     - ""
     - "&eRequirements:"
     - "&7- List requirements here"
   ```

3. **Test One Condition at a Time**
   ```yaml
   # Start simple:
   conditions:
     - type: VAULT_MONEY
       amount: 100
   
   # Then add more:
   conditions:
     - type: VAULT_MONEY
       amount: 100
     - type: PERMISSION
       permission: "shop.vip"
   ```

4. **Use Descriptive GUI IDs**
   ```yaml
   # ✅ Good
   guis:
     vip_shop:
     starter_kit:
     admin_panel:
   
   # ❌ Bad
   guis:
     gui1:
     test:
     thing:
   ```

---

## 🐛 Troubleshooting

### GUI Won't Open
- Check console for errors
- Verify GUI ID in `/ogui open <id>`
- Check player has permission `ogui.command.<gui>`

### Conditions Not Working
- Verify required plugin is installed (Vault, PlaceholderAPI, etc.)
- Check console for error messages
- Test each condition individually
- Verify syntax is correct

### NPC Not Opening GUI
- Confirm ModeledNPCs is installed
- Check `npc_id` matches NPC's actual ID
- Verify NPC exists in the world
- Test with `/ogui open <gui>` first

### Colors Not Showing
- Server must be 1.16+ for hex/gradients
- Use Paper/Purpur (Spigot has limited RGB)
- Check ColorUtil.java is properly installed

---

## 📚 Advanced Features

### Command Variables

Use these in your `commands:` section:

| Variable | Description | Example |
|----------|-------------|---------|
| `{player}` | Player name | `give {player} diamond 1` |
| `[player]` | Run as player | `[player] spawn` |
| `[console]` | Run as console | `[console] give {player} diamond 1` |
| `[close]` | Close GUI | `[close]` |
| `[message]` | Send message | `[message] &aSuccess!` |

### NPC Integration

Bind GUIs to NPCs:

```yaml
guis:
  shop:
    title: "&6Shop"
    npc_id: 5  # Opens when clicking NPC #5
    rows: 3
    # ... items ...
```

### Custom Commands

Create custom commands to open GUIs:

```yaml
guis:
  shop:
    title: "&6Shop"
    commands:
      - "shop"
      - "store"
      - "buy"
    # ... items ...
```

Players can now use `/shop`, `/store`, or `/buy` to open!

---

## 💡 Tips & Tricks

### 1. Free Items (No Conditions)
```yaml
items:
  10:
    material: APPLE
    name: "&aFree Apple"
    commands:
      - "[console] give {player} apple 1"
    # No conditions = free!
```

### 2. Close Button
```yaml
items:
  31:
    material: BARRIER
    name: "&c&lClose"
    commands:
      - "[close]"
```

### 3. Info Item (No Click)
```yaml
items:
  4:
    material: BOOK
    name: "&e&lWelcome!"
    lore:
      - "&7This is an info item"
      - "&7It does nothing when clicked"
    # No commands = just display
```

### 4. Multiple GUIs Navigation
```yaml
items:
  26:
    material: ARROW
    name: "&aNext Page"
    commands:
      - "[player] ogui open shop_page2"
```

### 5. Rank-Based Items
```yaml
items:
  10:
    material: DIAMOND_SWORD
    name: "&bVIP Sword"
    commands:
      - "[console] give {player} diamond_sword 1"
    conditions:
      - type: PLACEHOLDER
        placeholder: "%vault_rank%"
        operator: "=="
        value: "VIP"
```

---

## 🎓 Learning Path

### Beginner
1. Create simple GUI with `/ogui open`
2. Add basic items with money cost
3. Add lore and colors
4. Test with friends

### Intermediate
1. Use multiple conditions
2. Add permission-based items
3. Create NPC shops
4. Use custom items (ItemsAdder/Nexo)

### Advanced
1. Use PlaceholderAPI conditions
2. Create location-based items
3. Combine 5+ conditions
4. Use gradients and hex colors
5. Create multi-page GUIs

---

## 🤝 Support

- **Issues:** [GitHub Issues](https://github.com/yourusername/ogui-enhanced/issues)




---

## 🌟 Credits

**Created by:** OreoStudios
**Special Thanks:** SmartInvs

---

## 📈 Changelog

### v1.0.0
- ✨ Initial release
- 🎨 Full color support (hex, gradients, rainbow)
- 💰 16 condition types
- 🤖 NPC integration
- 🌐 PlaceholderAPI support
- 🔄 Multi-language system

---

**Made with ❤️ for the Minecraft community**