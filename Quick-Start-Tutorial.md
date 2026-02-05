# Quick Start Tutorial

Learn to create your first OGUI shop in just **5 minutes**! This tutorial will guide you through creating a simple gems shop using OreoEssentials currencies.

---

## 🎯 What You'll Build

By the end of this tutorial, you'll have a working shop where players can buy items using OreoEssentials gems currency.

**Shop Features:**
- Opens with `/gemsshop` command
- 3 items for purchase
- Uses OreoEssentials "gems" currency
- Professional looking GUI

---

## 📋 Prerequisites

Before starting, ensure you have:
- ✅ OGUI Enhanced installed
- ✅ OreoEssentials installed
- ✅ At least one currency configured in OreoEssentials (e.g., "gems")

**Don't have OreoEssentials?** No problem! You can follow along using `VAULT_MONEY` instead.

---

## 🚀 Step 1: Open Configuration

1. Navigate to your server's plugin folder:
   ```
   plugins/OGUI/
   ```

2. Open `guis.yml` in your favorite text editor:
   - Notepad++ (Windows)
   - VS Code (All platforms)
   - nano (Linux)

3. Clear the file contents (or backup first)

---

## 📝 Step 2: Create Your First Shop

Copy and paste this configuration into `guis.yml`:

```yaml
guis:
  gems_shop:
    title: "&6&l💎 Gems Shop"
    rows: 3
    commands:
      - "gemsshop"
      - "buyshop"
    items:
      "11":
        material: DIAMOND_SWORD
        name: "&b&lDiamond Sword"
        lore:
          - "&7A powerful weapon"
          - ""
          - "&ePrice: &a1000 Gems"
          - "&7Click to purchase"
        commands:
          - "give {player} diamond_sword 1"
        close: false
        conditions:
          - type: OREO_CURRENCY
            currency: gems
            amount: 1000

      "13":
        material: DIAMOND_PICKAXE
        name: "&b&lDiamond Pickaxe"
        lore:
          - "&7Mine faster"
          - ""
          - "&ePrice: &a750 Gems"
          - "&7Click to purchase"
        commands:
          - "give {player} diamond_pickaxe 1"
        close: false
        conditions:
          - type: OREO_CURRENCY
            currency: gems
            amount: 750

      "15":
        material: ELYTRA
        name: "&f&lElytra Wings"
        lore:
          - "&7Fly through the skies"
          - ""
          - "&ePrice: &a5000 Gems"
          - "&7Click to purchase"
        commands:
          - "give {player} elytra 1"
        close: true
        conditions:
          - type: OREO_CURRENCY
            currency: gems
            amount: 5000

      "22":
        material: BARRIER
        name: "&c&lClose Shop"
        lore:
          - "&7Click to close"
        close: true
```

---

## 🔍 Step 3: Understanding the Configuration

Let's break down what each part does:

### Shop Definition
```yaml
gems_shop:
  title: "&6&l💎 Gems Shop"  # GUI title with color codes
  rows: 3                      # Number of rows (1-6)
  commands:                    # Commands to open this GUI
    - "gemsshop"
    - "buyshop"
```

### Item Definition
```yaml
"11":                          # Slot number (0-53)
  material: DIAMOND_SWORD      # Item material
  name: "&b&lDiamond Sword"    # Display name
  lore:                        # Lore lines
    - "&7A powerful weapon"
    - ""
    - "&ePrice: &a1000 Gems"
```

### Commands
```yaml
commands:
  - "give {player} diamond_sword 1"  # {player} = buyer's name
```

### Conditions
```yaml
conditions:
  - type: OREO_CURRENCY        # Condition type
    currency: gems             # Currency name
    amount: 1000               # Required amount
```

### Close Behavior
```yaml
close: false  # Keep GUI open after purchase
close: true   # Close GUI after purchase
```

---

## ⚡ Step 4: Reload and Test

### Reload Configuration

1. In-game, run:
   ```
   /ogui reload
   ```

2. You should see:
   ```
   ✔ OGUI menus reloaded successfully!
   Loaded 1 GUI(s)
   ```

### Test the Shop

1. Open the shop:
   ```
   /gemsshop
   ```

2. You should see a GUI with:
   - Title: "💎 Gems Shop"
   - 3 items for purchase
   - 1 close button

### Give Yourself Gems

To test purchasing, give yourself gems:
```
/currency add [yourname] gems 10000
```

### Try Purchasing

1. Click on the Diamond Sword
2. If you have 1000+ gems:
   - ✅ Gems deducted
   - ✅ Sword given
   - ✅ GUI stays open (close: false)
3. If you don't have enough:
   - ❌ Error message shown
   - ❌ GUI closes
   - ❌ No items given

---

## 🎨 Step 5: Customization

Now let's customize the shop for your server!

### Change Currency

If your currency isn't called "gems", change it:
```yaml
conditions:
  - type: OREO_CURRENCY
    currency: tokens  # Change to your currency name
    amount: 1000
```

### Adjust Prices

Modify the `amount` values:
```yaml
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 500  # Changed from 1000
```

### Add More Items

Copy an existing item and modify:
```yaml
"16":  # New slot
  material: NETHERITE_SWORD
  name: "&5&lNetherite Sword"
  lore:
    - "&7Ultimate weapon"
    - ""
    - "&ePrice: &a10000 Gems"
  commands:
    - "give {player} netherite_sword 1"
    - "enchant {player} sharpness 10"  # Multiple commands!
  close: false
  conditions:
    - type: OREO_CURRENCY
      currency: gems
      amount: 10000
```

### Change Colors

Minecraft color codes:
- `&0` - Black
- `&1` - Dark Blue
- `&2` - Dark Green
- `&3` - Dark Aqua
- `&4` - Dark Red
- `&5` - Dark Purple
- `&6` - Gold
- `&7` - Gray
- `&8` - Dark Gray
- `&9` - Blue
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White
- `&l` - Bold
- `&o` - Italic
- `&n` - Underline

Example:
```yaml
name: "&c&lRED BOLD TEXT"
name: "&9&oBlue Italic Text"
```

---

## ⚙️ Step 6: Using Vault Instead

Don't have OreoEssentials? Use Vault economy:

```yaml
conditions:
  - type: VAULT_MONEY  # Instead of OREO_CURRENCY
    amount: 500        # Dollar amount
```

Full example:
```yaml
guis:
  money_shop:
    title: "&a&l$ Money Shop"
    rows: 3
    commands:
      - "shop"
    items:
      "13":
        material: DIAMOND
        name: "&bDiamond"
        lore:
          - "&7Price: $500"
        commands:
          - "give {player} diamond 1"
        conditions:
          - type: VAULT_MONEY
            amount: 500
```

Test with:
```
/eco give [yourname] 1000
/shop
```

---

## 🔥 Step 7: Adding Multiple Conditions

Let's make a VIP-only item that costs gems:

```yaml
"12":
  material: GOLDEN_APPLE
  name: "&6&lVIP Golden Apple"
  lore:
    - "&7VIP Members Only"
    - ""
    - "&eRequires:"
    - "&7- VIP Rank"
    - "&7- 500 Gems"
  commands:
    - "give {player} golden_apple 1"
  close: false
  conditions:
    - type: PERMISSION
      permission: rank.vip
    - type: OREO_CURRENCY
      currency: gems
      amount: 500
```

Now players need BOTH permission AND gems!

---

## 🎯 Step 8: Common Patterns

### Item Trade (No Currency)
```yaml
conditions:
  - type: ITEM
    material: DIAMOND
    amount: 10
```

### Level Requirement
```yaml
conditions:
  - type: XP_LEVEL
    amount: 50
```

### Multiple Currencies
```yaml
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
  - type: OREO_CURRENCY
    currency: tokens
    amount: 500
  - type: VAULT_MONEY
    amount: 5000
```

---

## ✅ Checklist

After completing this tutorial, you should have:

- [ ] Created your first shop configuration
- [ ] Opened the shop with a command
- [ ] Purchased items successfully
- [ ] Customized prices and names
- [ ] Tested with different gem amounts
- [ ] Understood the basic structure

---

## 🐛 Common Issues

### Shop Won't Open

**Problem:** `/gemsshop` says "Unknown command"

**Solution:**
1. Check you ran `/ogui reload`
2. Verify command is in config under `commands:`
3. Check console for errors

### Can't Purchase Items

**Problem:** Click item but nothing happens

**Solution:**
1. Check you have enough gems
2. Verify OreoEssentials is running
3. Check currency name matches exactly
4. Look at console for errors

### Configuration Errors

**Problem:** "Failed to load GUI"

**Solution:**
1. Validate YAML syntax (use online validator)
2. Check indentation (use spaces, not tabs)
3. Verify all quotes match
4. Check material names are valid

---

## 🚀 Next Steps

Congratulations! You've created your first OGUI shop!

### Learn More
- [Condition System](Condition-System.md) - All condition types
- [Shop Examples](Shop-Examples.md) - More complex examples
- [Configuration Basics](Configuration-Basics.md) - Deep dive into configs

### Advanced Features
- [Multiple Conditions](Multiple-Conditions.md) - Complex requirements
- [Custom Items](Custom-Items.md) - ItemsAdder, Nexo integration
- [PlaceholderAPI](PlaceholderAPI-Conditions.md) - Dynamic conditions

### Get Help
- [FAQ](FAQ.md) - Common questions
- [Troubleshooting](Troubleshooting.md) - Fix issues
- [Discord](https://discord.gg/yourserver) - Community support

---

## 💡 Tips & Tricks

### Professional Naming
Use formatting for better visuals:
```yaml
name: "&6&l◆ &e&lEpic Sword &6&l◆"
lore:
  - ""
  - "&7▸ &fDamage: &c+10"
  - "&7▸ &fSpeed: &a+5"
  - ""
  - "&e⚡ Special Ability ⚡"
  - "&7Right-click to unleash lightning"
  - ""
  - "&6&l» &eClick to Purchase &6&l«"
```

### Slot Layouts
Common patterns:
```
Center Item: Slot 13
Three Items: Slots 11, 13, 15
Five Items: Slots 10, 11, 13, 15, 16
Close Button: Slot 22 (bottom center)
```

### Testing Tips
1. Test without conditions first
2. Add conditions one at a time
3. Use creative mode for testing
4. Give yourself test currency
5. Check console for errors

---

## 🎉 You Did It!

You've successfully created your first OGUI shop!

Feel free to experiment, add more items, and explore advanced features.

**Happy building!** 🛠️
